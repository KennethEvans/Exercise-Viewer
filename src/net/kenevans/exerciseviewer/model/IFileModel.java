package net.kenevans.exerciseviewer.model;

/*
 * Created on Feb 26, 2019
 * By Kenneth Evans, Jr.
 */

public interface IFileModel
{
    /**
     * Gets info about this file.
     * 
     * @return
     */
    public String getInfo();

    /**
     * @return The value of fileName.
     */
    public String getFileName();

    /**
     * @return The value of hrTimeVals.
     */
    public long[] getHrTimeVals();

    /**
     * @return The value of hrVals.
     */
    public double[] getHrVals();

    /**
     * @return The value of speedTimeVals.
     */
    public long[] getSpeedTimeVals();

    /**
     * @return The value of speedVals.
     */
    public double[] getSpeedVals();

    /**
     * @return The value of timeVals.
     */
    public long[] getTimeVals();

    /**
     * @return The value of timeVals.
     */
    public long[] getEleTimeVals();

    /**
     * @return The value of eleVals.
     */
    public double[] getEleVals();

    /**
     * @return The value of startTime.
     */
    public long getStartTime();

    /**
     * @return The value of endTime.
     */
    public long getEndTime();

    /**
     * @return The value of startHrTime.
     */
    public long getStartHrTime();

    /**
     * @return The value of endHrTime.
     */
    public long getEndHrTime();

    /**
     * @return The value of nTracks.
     */
    public int getnTracks();

    /**
     * @return The value of nSegments.
     */
    public int getnSegments();

    /**
     * @return The value of nTrackPoints.
     */
    public int getnTrackPoints();

    /**
     * @return The value of nHrValues.
     */
    public int getnHrValues();

    /**
     * @return The value of distance.
     */
    public double getDistance();
}
