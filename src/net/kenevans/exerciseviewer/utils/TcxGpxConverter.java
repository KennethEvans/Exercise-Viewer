package net.kenevans.exerciseviewer.utils;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import net.kenevans.gpxcombined.ExtensionsType;
import net.kenevans.gpxcombined.GpxType;
import net.kenevans.gpxcombined.MetadataType;
import net.kenevans.gpxcombined.PersonType;
import net.kenevans.gpxcombined.TrackPointExtensionT;
import net.kenevans.gpxcombined.TrkType;
import net.kenevans.gpxcombined.TrksegType;
import net.kenevans.gpxcombined.WptType;
import net.kenevans.trainingcenterdatabasev2.AbstractSourceT;
import net.kenevans.trainingcenterdatabasev2.ActivityLapT;
import net.kenevans.trainingcenterdatabasev2.ActivityListT;
import net.kenevans.trainingcenterdatabasev2.ActivityT;
import net.kenevans.trainingcenterdatabasev2.HeartRateInBeatsPerMinuteT;
import net.kenevans.trainingcenterdatabasev2.PlanT;
import net.kenevans.trainingcenterdatabasev2.PositionT;
import net.kenevans.trainingcenterdatabasev2.TrackT;
import net.kenevans.trainingcenterdatabasev2.TrackpointT;
import net.kenevans.trainingcenterdatabasev2.TrainingCenterDatabaseT;
import net.kenevans.trainingcenterdatabasev2.TrainingT;
import net.kenevans.trainingcenterdatabasev2.TrainingTypeT;

/*
 * Created on Nov 3, 2019
 * By Kenneth Evans, Jr.
 */

public class TcxGpxConverter
{
    private static String AUTHOR = "TCXGpxParser (kenevans.net)";

    /**
     * Converts a TCX TrainingCenterDatabaseT to a GPX 1.1 GpxType type by
     * copying common fields. Note that this implementation may not be complete
     * and may not convert everything that could be converted.
     * 
     * @param tcx The TrainingCenterDatabaseT to convert.
     * @return The GpxType.
     */
    public static GpxType convertTCXtoGpx(TrainingCenterDatabaseT tcx) {
        if(tcx == null) {
            return null;
        }
        String desc, notes, planName;
        double lat, lon;
        Double ele;
        // TCX types
        ActivityListT activities;
        List<ActivityT> activityList;
        List<ActivityLapT> lapList;
        List<TrackT> trackList;
        List<TrackpointT> trackpointList;
        PositionT position;
        HeartRateInBeatsPerMinuteT hrBpm;
        Short hr, cad;
        XMLGregorianCalendar time;
        TrainingT training;
        PlanT plan;
        TrainingTypeT trainingType;
        // GPX types
        GpxType gpxNew;
        MetadataType metadata;
        PersonType person;
        TrkType trk;
        TrksegType trkseg;
        WptType trkpt;
        ExtensionsType extension;
        TrackPointExtensionT trkptExension;
        gpxNew = new GpxType();
        // Metadata
        metadata = new MetadataType();
        person = new PersonType();
        person.setName(AUTHOR);
        metadata.setAuthor(person);
        desc = getMetadataDescriptionFromTcx(tcx);
        if(desc != null) {
            metadata.setDesc(desc);
        }
        gpxNew.setMetadata(metadata);

        // Check if some trackpoints have position and some not.
        // Do not want to make a trackpoint for the ones with no position
        // to avoid lat,lon = 0,0 points
        boolean positionFound = positionFound(tcx);

        // Activities (Correspond to a track)
        activities = tcx.getActivities();
        // Loop over activities
        activityList = activities.getActivity();
        for(ActivityT activity : activityList) {
            trk = new TrkType();
            gpxNew.getTrk().add(trk);
            // Get the description from the notes and training plan
            desc = "";
            planName = "";
            notes = "";
            training = activity.getTraining();
            if(training != null) {
                plan = training.getPlan();
                if(plan != null) {
                    trainingType = plan.getType();
                    desc += "Training Type: " + trainingType;
                    planName = plan.getName();
                    if(planName != null && !planName.isEmpty()) {
                        if(!desc.isEmpty()) {
                            desc += " ";
                        }
                        desc += "PlanName: " + planName;
                    }
                }
            }
            notes = activity.getNotes();
            if(notes != null && !notes.isEmpty()) {
                if(!desc.isEmpty()) {
                    desc += " ";
                }
                desc += "Notes: " + notes;
            }
            if(!desc.isEmpty()) {
                trk.setDesc(desc);
            }
            // Loop over laps (Correspond to a track segment(s))
            lapList = activity.getLap();
            for(ActivityLapT lap : lapList) {
                // Loop over tracks
                trackList = lap.getTrack();
                for(TrackT track : trackList) {
                    trackpointList = track.getTrackpoint();
                    trkseg = new TrksegType();
                    trk.getTrkseg().add(trkseg);
                    // loop over trackpoints
                    for(TrackpointT trackPoint : trackpointList) {
                        trkpt = new WptType();
                        position = trackPoint.getPosition();
                        if(position != null) {
                            lat = position.getLatitudeDegrees();
                            lon = position.getLongitudeDegrees();
                            trkpt.setLat(BigDecimal.valueOf(lat));
                            trkpt.setLon(BigDecimal.valueOf(lon));
                            trkseg.getTrkpt().add(trkpt);
                        } else if(!positionFound) {
                            trkpt.setLat(BigDecimal.valueOf(0));
                            trkpt.setLon(BigDecimal.valueOf(0));
                            trkseg.getTrkpt().add(trkpt);
                        } else {
                            // This TCX has positions. This trackpoint doesn't
                            // and we don't want to write lat,lon = 0,0 with
                            // valid positions, so skip this trackpoint.
                            continue;
                        }
                        ele = trackPoint.getAltitudeMeters();
                        if(ele == null) {
                            trkpt.setEle(BigDecimal.valueOf(0));
                        } else {
                            trkpt.setEle(BigDecimal.valueOf(ele));
                        }
                        time = trackPoint.getTime();
                        if(time != null) {
                            trkpt.setTime(time);
                        }
                        // Extension
                        hrBpm = trackPoint.getHeartRateBpm();
                        cad = trackPoint.getCadence();
                        if(hrBpm != null || cad != null) {
                            extension = new ExtensionsType();
                            trkptExension = new TrackPointExtensionT();
                            if(hrBpm != null) {
                                hr = hrBpm.getValue();
                                if(hr != null) {
                                    trkptExension.setHr(hr);
                                }
                            }
                            if(cad != null) {
                                trkptExension.setCad(cad);
                            }
                            extension.getAny().add(trkptExension);
                            trkpt.setExtensions(extension);
                        }
                    }
                }
            }
        }
        return gpxNew;
    }

    public static boolean positionFound(TrainingCenterDatabaseT tcx) {
        ActivityListT activities;
        List<ActivityT> activityList;
        List<ActivityLapT> lapList;
        List<TrackT> trackList;
        List<TrackpointT> trackpointList;
        PositionT position;
        activities = tcx.getActivities();

        // Loop over activities
        activityList = activities.getActivity();
        for(ActivityT activity : activityList) {
            // Loop over laps (Correspond to a track segment(s))
            lapList = activity.getLap();
            for(ActivityLapT lap : lapList) {
                // Loop over tracks
                trackList = lap.getTrack();
                for(TrackT track : trackList) {
                    trackpointList = track.getTrackpoint();
                    // loop over trackpoints
                    for(TrackpointT trackPoint : trackpointList) {
                        position = trackPoint.getPosition();
                        if(position != null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static String getMetadataDescriptionFromTcx(
        TrainingCenterDatabaseT tcx) {
        if(tcx == null) {
            return null;
        }
        AbstractSourceT author = tcx.getAuthor();
        if(author == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if(author.getName() != null) {
            sb.append(author.getName());
        }
        String desc = sb.toString();
        if(desc.length() == 0) {
            return null;
        }
        return desc;
    }

}
