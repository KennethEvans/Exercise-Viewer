package net.kenevans.exerciseviewer.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import net.kenevans.core.utils.Utils;
import net.kenevans.exerciseviewer.utils.GpxUtils;
import net.kenevans.exerciseviewer.utils.TcxGpxUtils;
import net.kenevans.trainingcenterdatabasev2.ActivityLapT;
import net.kenevans.trainingcenterdatabasev2.ActivityListT;
import net.kenevans.trainingcenterdatabasev2.ActivityT;
import net.kenevans.trainingcenterdatabasev2.HeartRateInBeatsPerMinuteT;
import net.kenevans.trainingcenterdatabasev2.PositionT;
import net.kenevans.trainingcenterdatabasev2.TrackT;
import net.kenevans.trainingcenterdatabasev2.TrackpointT;
import net.kenevans.trainingcenterdatabasev2.TrainingCenterDatabaseT;
import net.kenevans.trainingcenterdatabasev2.parser.TCXParser;

/*
 * Created on February 25, 2019
 * By Kenneth Evans, Jr.
 */

/**
 * TcxFileModel is a model for TCX data.
 * 
 * @author Kenneth Evans, Jr.
 */
public class TcxFileModel implements IFileModel, IConstants
{
    private String fileName;
    private TrainingCenterDatabaseT tcx;
    private long[] hrTimeVals;
    private double[] hrVals;
    private long[] speedTimeVals;
    private double[] speedVals;
    private long[] timeVals;
    private double[] eleVals;
    private int nTracks;
    private int nSegments;
    private int nTrackPoints;
    private int nHrValues;
    private long startTime = Long.MAX_VALUE;
    private long endTime;
    private long startHrTime = Long.MAX_VALUE;
    private long endHrTime;
    private double distance;

    public TcxFileModel(String fileName) {
        this.fileName = fileName;
        try {
            this.tcx = openFile(fileName);
        } catch(Exception ex) {
            ex.printStackTrace();
            Utils.excMsg("Error reading " + fileName, ex);
        }

        ArrayList<Long> timeValsArray = new ArrayList<Long>();
        ArrayList<Long> speedTimeValsArray = new ArrayList<Long>();
        ArrayList<Double> speedValsArray = new ArrayList<Double>();
        ArrayList<Double> eleValsArray = new ArrayList<Double>();
        ArrayList<Long> hrTimeValsArray = new ArrayList<Long>();
        ArrayList<Double> hrValsArray = new ArrayList<Double>();

        // TCX types
        ActivityListT activities;
        List<ActivityT> activityList;
        List<ActivityLapT> lapList;
        List<TrackT> trackList;
        List<TrackpointT> trackpointList;
        PositionT position;
        HeartRateInBeatsPerMinuteT hrBpm;
        Short hr;
        XMLGregorianCalendar xgcal;
        GregorianCalendar gcal;

        // Get the tracks
        long time;
        long lastTimeValue = -1;
        nTracks = 0;
        nSegments = 0;
        nTrackPoints = 0;
        nHrValues = 0;

        // Activities (Correspond to a track)
        activities = tcx.getActivities();
        // Loop over activities
        activityList = activities.getActivity();
        for(ActivityT activity : activityList) {
            nTracks++;
            // Loop over laps (Correspond to a track segment(s))
            lapList = activity.getLap();
            for(ActivityLapT lap : lapList) {
                nSegments++;
                if(nSegments > 1) {
                    // Use NaN to make a break between segments but don't count
                    // as a HR value
                    hrValsArray.add(Double.NaN);
                    hrTimeValsArray.add(lastTimeValue);
                    speedValsArray.add(Double.NaN);
                    speedTimeValsArray.add(lastTimeValue);
                    eleValsArray.add(Double.NaN);
                    timeValsArray.add(lastTimeValue);
                }
                double deltaLength, speed;
                double prevTime = -1;
                double deltaTime;
                double lat = 0, lon = 0, prevLat = 0, prevLon = 0;
                Double ele;
                // Loop over tracks
                trackList = lap.getTrack();
                for(TrackT track : trackList) {
                    trackpointList = track.getTrackpoint();
                    for(TrackpointT trackPoint : trackpointList) {
                        nTrackPoints++;
                        xgcal = trackPoint.getTime();
                        gcal = xgcal.toGregorianCalendar(
                            TimeZone.getTimeZone("GMT"), null, null);
                        // Consider gcal.getTimeInMillis()
                        time = gcal.getTime().getTime();
                        if(time < startTime) {
                            startTime = time;
                        }
                        if(time > endTime) {
                            endTime = time;
                        }
                        timeValsArray.add(time);
                        // Speed
                        position = trackPoint.getPosition();
                        if(position != null) {
                            lat = position.getLatitudeDegrees();
                            lon = position.getLongitudeDegrees();
                            if(prevTime != -1) {
                                // Should be the second track point
                                deltaLength = GpxUtils.greatCircleDistance(
                                    prevLat, prevLon, lat, lon);
                                distance += deltaLength;
                                deltaTime = time - prevTime;
                                speed = deltaTime > 0
                                    ? 1000. * deltaLength / deltaTime
                                    : 0;
                                // Convert from m/sec to mi/hr
                                speedValsArray.add(
                                    speed * GpxUtils.M2MI / GpxUtils.SEC2HR);
                                speedTimeValsArray
                                    .add(time - Math.round(.5 * deltaTime));
                            }
                            prevTime = time;
                            prevLat = lat;
                            prevLon = lon;
                        }
                        ele = trackPoint.getAltitudeMeters();
                        if(ele != null) {
                            eleValsArray.add(ele * GpxUtils.M2FT);
                        } else {
                            eleValsArray.add(0.0);
                        }
                        hrBpm = trackPoint.getHeartRateBpm();
                        if(hrBpm != null) {
                            hr = hrBpm.getValue();
                            hrValsArray.add((double)hr);
                            hrTimeValsArray.add(time);
                            lastTimeValue = time;
                            if(time < startHrTime) {
                                startHrTime = time;
                            }
                            if(time > endHrTime) {
                                endHrTime = time;
                            }
                            nHrValues++;
                        }
                    }
                }
            }
        }
        hrVals = new double[hrValsArray.size()];

        int index = 0;
        for(Double dVal : hrValsArray) {
            hrVals[index++] = dVal.doubleValue();
        }
        hrTimeVals = new long[hrTimeValsArray.size()];
        index = 0;
        for(Long lVal : hrTimeValsArray) {
            hrTimeVals[index++] = lVal.longValue();
        }
        // Speed
        speedVals = new double[speedValsArray.size()];
        index = 0;
        for(Double dVal : speedValsArray) {
            speedVals[index++] = dVal.doubleValue();
        }
        speedTimeVals = new long[speedTimeValsArray.size()];
        index = 0;
        for(Long lVal : speedTimeValsArray) {
            speedTimeVals[index++] = lVal.longValue();
        }
        // Ele
        eleVals = new double[eleValsArray.size()];
        index = 0;
        for(Double dVal : eleValsArray) {
            eleVals[index++] = dVal.doubleValue();
        }
        timeVals = new long[timeValsArray.size()];
        index = 0;
        for(Long lVal : timeValsArray) {
            timeVals[index++] = lVal.longValue();
        }
    }

    /**
     * Reads the file and returns a TrainingCenterDatabaseT.
     * 
     * @param fileName
     * @return
     * @throws IOException
     * @throws JAXBException
     */
    public TrainingCenterDatabaseT openFile(String fileName)
        throws IOException, JAXBException {
        File file = new File(fileName);
        return TCXParser.parse(file);
    }

    /**
     * Writes the file using the given bytes.
     * 
     * @param file
     * @param saveData
     * @throws IOException
     */
    public static void saveFile(File file, byte[] saveData) throws IOException {
    }

    public static String sysInfo() {
        String info = "";
        String[] properties = {"user.dir", "java.version", "java.home",
            "java.vm.version", "java.vm.vendor", "java.ext.dirs"};
        String property;
        for(int i = 0; i < properties.length; i++) {
            property = properties[i];
            info += property + ": "
                + System.getProperty(property, "<not found>") + LS;
        }
        info += getClassPath("  ");
        return info;
    }

    public static String getClassPath(String tabs) {
        String info = "";
        String classPath = System.getProperty("java.class.path", "<not found>");
        String[] paths = classPath.split(File.pathSeparator);
        for(int i = 0; i < paths.length; i++) {
            info += tabs + i + " " + paths[i] + LS;
        }
        return info;
    }

    /**
     * Gets info about this file.
     * 
     * @return
     */
    public String getInfo() {
        String info = "";
        info += getFileName() + LS + LS;
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);
        Date startHrDate = new Date(startHrTime);
        Date endHrDate = new Date(endHrTime);
        double duration = (double)endTime - (double)startTime;
        double hrDuration = (double)endHrTime - (double)startHrTime;
        int durationHours = (int)(duration / 3600000.);
        int durationMin = (int)(duration / 60000.) - durationHours * 60;
        int durationSec = (int)(duration / 1000.) - durationHours * 3600
            - durationMin * 60;
        int hrDurationHours = (int)(duration / 3600000.);
        int hrDurationMin = (int)(hrDuration / 60000.) - hrDurationHours * 60;
        int hrDurationSec = (int)(hrDuration / 1000.) - hrDurationHours * 3600
            - hrDurationMin * 60;
        info += "Tracks: " + startDate + " to " + endDate + LS;
        info += String.format("Duration: %d hr %d min %d sec (%.0f sec)",
            durationHours, durationMin, durationSec, duration / 1000.) + LS;
        info += String.format("Distance: %.2f mi", distance * GpxUtils.M2MI)
            + LS;
        info += String.format("Simple Avg Speed: %.2f mi/hr",
            distance / duration * 1000 * GpxUtils.M2MI / GpxUtils.SEC2HR) + LS;
        double[] stats = null, stats1 = null;
        if(nHrValues != 0) {
            info += "HR: " + startHrDate + " to " + endHrDate + LS;
            info += String.format("HR Duration: %d hr %d min %d sec",
                hrDurationHours, hrDurationMin, hrDurationSec) + LS;
            stats = getTimeAverageStats(hrVals, hrTimeVals, -Double.MIN_VALUE);
            if(stats != null) {
                info += String.format("HR Min=%.0f HR Max=%.0f HR Avg=%.1f",
                    stats[0], stats[1], stats[2]) + LS;
            } else {
                // Get simple average
                stats = getSimpleStats(hrVals, hrTimeVals, -Double.MIN_VALUE);
                if(stats != null) {
                    info += String.format("HR Min=%.0f HR Max=%.0f HR Avg=%.1f"
                        + " (Simple Average)", stats[0], stats[1], stats[2])
                        + LS;
                }
            }
        }
        if(speedVals.length != 0) {
            stats = getTimeAverageStats(speedVals, speedTimeVals,
                -Double.MIN_VALUE);
            if(stats != null) {
                info += String.format(
                    "Speed Min=%.1f Speed Max=%.1f Speed Avg=%.1f mi/hr",
                    stats[0], stats[1], stats[2]) + LS;
            } else {
                // Get simple average
                stats = getSimpleStats(speedVals, speedTimeVals,
                    -Double.MIN_VALUE);
                if(stats != null) {
                    info += String
                        .format(
                            "Speed Min=%.1f Speed Max=%.1f Speed Avg=%.1f mi/hr"
                                + " (Simple Average)",
                            stats[0], stats[1], stats[2])
                        + LS;
                }
            }
            // Moving average
            // Convert from m/sec to mi/hr
            double noMoveSpeed = D_SPEED_NOT_MOVING * GpxUtils.M2MI
                / GpxUtils.SEC2HR;
            stats = getTimeAverageStats(speedVals, speedTimeVals, noMoveSpeed);
            if(stats != null) {
                info += String.format("  Moving Speed Avg=%.1f mi/hr", stats[2])
                    + LS;
            } else {
                // Get simple average
                stats = getSimpleStats(speedVals, speedTimeVals, noMoveSpeed);
                if(stats != null) {
                    info += String.format(
                        "  Moving Speed Avg=%.1f mi/hr" + " (Simple Average)",
                        stats[2]) + LS;
                }
            }

        }
        if(eleVals.length != 0) {
            stats = getTimeAverageStats(eleVals, timeVals, -Double.MIN_VALUE);
            stats1 = getEleStats(eleVals, timeVals);
            if(stats != null) {
                info += String.format("Ele Min=%.0f Ele Max=%.0f Ele Avg=%.0f ",
                    stats[0], stats[1], stats[2]);
            }
            if(stats1 != null) {
                info += String.format("Ele Gain=%.0f Ele Loss=%.0f ft",
                    stats1[0], stats1[1]) + LS;
            } else {
                info += LS;
            }
        } else {
            // Get simple average
            stats = getSimpleStats(eleVals, timeVals, -Double.MIN_VALUE);
            if(stats != null) {
                info += String
                    .format("Ele Min=%.0f Ele Max=%.0f Ele Avg=%.1f ft"
                        + " (Simple Average)", stats[0], stats[1], stats[2])
                    + LS;
            }
        }
        info += nTracks + " Avtivities" + "        " + nSegments + " Laps:"
            + LS;
        info += nTrackPoints + " Track Points" + "        " + nHrValues
            + " HR Values:" + LS;
        String metadata = TcxGpxUtils.getMetaData(tcx);
        if(metadata != null && !metadata.isEmpty()) {
            info += metadata;
        }
        return info;
    }

    /**
     * Gets the statistics from the given values and time values by averaging
     * over the values, not over the actual time.
     * 
     * @param vals
     * @param timeVals
     * @param omitBelow Do not include values below this one.
     * @return {min, max, avg} or null on error.
     */
    public static double[] getSimpleStats(double[] vals, long[] timeVals,
        double omitBelow) {
        // System.out.println("vals: " + vals.length + ", timeVals: "
        // + timeVals.length);
        if(vals.length != timeVals.length) {
            Utils.errMsg("getSimpleStats: Array sizes (vals: " + vals.length
                + ", timeVals: " + timeVals.length + ") do not match");
            return null;
        }
        int len = vals.length;
        if(len == 0) {
            return new double[] {0, 0, 0};
        }
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        double sum = 0;
        double val;
        int nVals = 0;
        for(int i = 0; i < len; i++) {
            val = vals[i];
            if(Double.isNaN(val)) continue;
            if(val < omitBelow) continue;
            nVals++;
            sum += val;
            if(val > max) {
                max = val;
            }
            if(val < min) {
                min = val;
            }
        }
        if(nVals == 0) {
            return null;
        }
        sum /= nVals;
        return new double[] {min, max, sum};
    }

    /**
     * Gets the statistics from the given values and time values by averaging
     * over the values weighted by the time.
     * 
     * @param vals
     * @param timeVals
     * @param omitBelow Do not include values below this one.
     * @return {min, max, avg} or null on error.
     */
    public static double[] getTimeAverageStats(double[] vals, long[] timeVals,
        double omitBelow) {
        // System.out.println("vals: " + vals.length + ", timeVals: "
        // + timeVals.length);
        if(vals.length != timeVals.length) {
            Utils
                .errMsg("getTimeAverageStats: Array sizes (vals: " + vals.length
                    + ", timeVals: " + timeVals.length + ") do not match");
            return null;
        }
        int len = vals.length;
        if(len == 0) {
            return new double[] {0, 0, 0};
        }
        if(len < 2) {
            return new double[] {vals[0], vals[0], vals[0]};
        }
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        double sum = 0;
        double val;
        // Check for NaN
        for(int i = 0; i < len; i++) {
            val = vals[i];
            if(Double.isNaN(val)) {
                return null;
            }
        }

        // Loop over values.
        double totalWeight = 0;
        double weight;
        for(int i = 0; i < len; i++) {
            val = vals[i];
            if(Double.isNaN(val)) continue;
            if(val < omitBelow) continue;
            if(i == 0) {
                weight = .5 * (timeVals[i + 1] - timeVals[i]);
            } else if(i == len - 1) {
                weight = .5 * (timeVals[i] - timeVals[i - 1]);
            } else {
                weight = .5 * (timeVals[i] - timeVals[i - 1]);
            }
            totalWeight += weight;
            // Shoudn't happen
            sum += val * weight;
            if(val > max) {
                max = val;
            }
            if(val < min) {
                min = val;
            }
        }
        if(totalWeight == 0) {
            return null;
        }
        sum /= (totalWeight);
        return new double[] {min, max, sum};
    }

    /**
     * Gets the elevation statistics from the given values and time values.
     * These include elevation gain and loss.
     * 
     * @param vals
     * @param timeVals
     * @param omitBelow Do not include values below this one.
     * @return {gain, loss} or null on error.
     */
    public static double[] getEleStats(double[] vals, long[] timeVals) {
        // System.out.println("vals: " + vals.length + ", timeVals: "
        // + timeVals.length);
        if(vals.length != timeVals.length) {
            Utils.errMsg("getSimpleStats: Array sizes (vals: " + vals.length
                + ", timeVals: " + timeVals.length + ") do not match");
            return null;
        }
        int len = vals.length;
        if(len == 0) {
            return new double[] {0, 0};
        }
        double gain = 0;
        double loss = 0;
        double val;
        int nVals = 0;
        for(int i = 1; i < len; i++) {
            val = vals[i] - vals[i - 1];
            if(Double.isNaN(val)) continue;
            nVals++;
            if(val > 0) {
                gain += val;
            } else if(val < 0) {
                loss += -val;
            }
        }
        if(nVals == 0) {
            return null;
        }
        return new double[] {gain, loss};
    }

    /**
     * @return The value of fileName.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return The value of tcx.
     */
    public TrainingCenterDatabaseT getTcx() {
        return tcx;
    }

    /**
     * @return The value of hrTimeVals.
     */
    public long[] getHrTimeVals() {
        return hrTimeVals;
    }

    /**
     * @return The value of hrVals.
     */
    public double[] getHrVals() {
        return hrVals;
    }

    /**
     * @return The value of speedTimeVals.
     */
    public long[] getSpeedTimeVals() {
        return speedTimeVals;
    }

    /**
     * @return The value of speedVals.
     */
    public double[] getSpeedVals() {
        return speedVals;
    }

    /**
     * @return The value of timeVals.
     */
    public long[] getTimeVals() {
        return timeVals;
    }

    /**
     * @return The value of timeVals.
     */
    public long[] getEleTimeVals() {
        return timeVals;
    }

    /**
     * @return The value of eleVals.
     */
    public double[] getEleVals() {
        return eleVals;
    }

    /**
     * @return The value of startTime.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return The value of endTime.
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * @return The value of startHrTime.
     */
    public long getStartHrTime() {
        return startHrTime;
    }

    /**
     * @return The value of endHrTime.
     */
    public long getEndHrTime() {
        return endHrTime;
    }

    /**
     * @return The value of nTracks.
     */
    public int getnTracks() {
        return nTracks;
    }

    /**
     * @return The value of nSegments.
     */
    public int getnSegments() {
        return nSegments;
    }

    /**
     * @return The value of nTrackPoints.
     */
    public int getnTrackPoints() {
        return nTrackPoints;
    }

    /**
     * @return The value of nHrValues.
     */
    public int getnHrValues() {
        return nHrValues;
    }

    /**
     * @return The value of distance.
     */
    public double getDistance() {
        return distance;
    }

    // /**
    // * @param args
    // */
    // public static void main(String[] args) {
    // System.out.println("Starting " + GpxFileModel.class.getName());
    // System.out.println(FILE_PATH);
    // GpxFileModel app = new GpxFileModel(FILE_PATH);
    // // System.out.println(app.getInfo());
    // // DEBUG
    // // System.out.println();
    // // System.out.println("Classpath");
    // // System.out.println(getClassPath(" "));
    // // System.out.println();
    // // DEBUG
    // System.out.println();
    // app.printTracks();
    //
    // System.out.println();
    // System.out.println("All Done");
    // }

}
