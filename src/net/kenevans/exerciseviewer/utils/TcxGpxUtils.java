package net.kenevans.exerciseviewer.utils;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import net.kenevans.gpxcombined.GpxType;
import net.kenevans.gpxcombined.MetadataType;
import net.kenevans.gpxcombined.TrkType;
import net.kenevans.trainingcenterdatabasev2.ActivityListT;
import net.kenevans.trainingcenterdatabasev2.ActivityT;
import net.kenevans.trainingcenterdatabasev2.PlanT;
import net.kenevans.trainingcenterdatabasev2.SportT;
import net.kenevans.trainingcenterdatabasev2.TrainingCenterDatabaseT;
import net.kenevans.trainingcenterdatabasev2.TrainingT;
import net.kenevans.trainingcenterdatabasev2.TrainingTypeT;
import net.kenevans.trainingcenterdatabasev2.parser.TCXParser;

/*
 * Created on Feb 26, 2019
 * By Kenneth Evans, Jr.
 */

public class TcxGpxUtils
{
    public static final String LS = System.getProperty("line.separator");

    /**
     * Get various metadata for the given TrainingCenterDatabaseT.
     * 
     * @param tcx
     * @return
     */
    public static String getMetaData(TrainingCenterDatabaseT tcx) {
        StringBuilder sb = new StringBuilder();
        // Metadata
        String desc = TCXParser.getMetadataDescriptionFromTcx(tcx);
        sb.append("MetadataDescription: " + desc);

        ActivityListT activities;
        List<ActivityT> activityList;
        TrainingT training;
        TrainingTypeT trainingType;
        SportT sport;
        PlanT plan;
        String planName, notes, sportName;
        XMLGregorianCalendar id;
        // Loop over activities (Correspond to a track)
        activities = tcx.getActivities();
        // Loop over activities
        activityList = activities.getActivity();
        int nActivities = 0;
        for(ActivityT activity : activityList) {
            nActivities++;
            sportName = "";
            desc = "";
            id = activity.getId();
            sport = activity.getSport();
            if(sport != null) {
                sportName = sport.name();
            }
            sb.append(LS + "Activity " + nActivities + ": " + id + " Sport="
                + sportName);
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
                desc += LS + "Notes: " + notes;
            }
            sb.append(LS + desc);

        }
        return sb.toString();
    }

    /**
     * Get various metadata for the given GpxType.
     * 
     * @param gpx
     * @return
     */
    public static String getMetaData(GpxType gpx) {
        StringBuilder sb = new StringBuilder();
        String desc = null;
        MetadataType metadata;
        List<TrkType> trkList;
        // Metadata
        int nNull = 0;
        metadata = gpx.getMetadata();
        if(metadata != null) {
            if(desc != null) {
                desc = metadata.getDesc();
            } else {
                nNull++;
            }
        }
        sb.append("MetadataDescription: " + desc);

        trkList = gpx.getTrk();
        int nTrks = 0;
        if(trkList != null) {
            for(TrkType trk : trkList) {
                nTrks++;
                sb.append(LS + "Track " + nTrks);
                desc = trk.getDesc();
                if(desc != null) {
                    sb.append(LS + "description: " + desc);
                } else {
                    nNull++;
                }
            }
        }
        if(nNull == nTrks + 1) {
            // All null
            return null;
        }
        return sb.toString();
    }

}
