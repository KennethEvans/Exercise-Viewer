package net.kenevans.exerciseviewer.utils;

/*
 * Created on May 18, 2011
 * By Kenneth Evans, Jr.
 */

/**
 * TrkStat is a class to hold statistics for a track or segment. The statistics
 * include:
 * <ul>
 * <li>The number of points.</li>
 * <li>The length in meters.</li>
 * <li>The elapsed time in millisec.</li>
 * <li>The moving time in millisec.</li>
 * <li>The maximum speed in m/sec.</li>
 * <li>The average speed in m/sec.</li>
 * <li>The average speed while moving in m/sec.</li>
 * <li>The maximum elevation in m.</li>
 * <li>The minimum elevation in m.</li>
 * <li>The average elevation in m.</li>
 * </ul>
 * 
 * @author Kenneth Evans, Jr.
 */
public class TrackStat
{
    /** The number of points. */
    private int nPoints = 0;
    /** The length in meters. */
    private double length = 0;
    /** The start time in millisec. */
    private double startTime;
    /** The elapsed time in millisec. */
    private double elapsedTime = 0;
    /** The moving time in millisec. */
    private double movingTime = 0;
    /** The maximum speed in m/sec. */
    private double maxSpeed = 0;
    /** The time average speed in m/sec. */
    private double avgSpeed = 0;
    /** The time average speed while moving in m/sec. */
    private double avgMovingSpeed = 0;
    /** The maximum elevation in m. */
    private double maxEle = 0;
    /** The minimum elevation in m. */
    private double minEle = 0;
    /** The average elevation in m over track points. */
    private double avgEle = 0;

    /**
     * TrkStat constructor that sets all values to zero.
     */
    public TrackStat() {
    }

    /**
     * TrkStat constructor that sets all values.
     * 
     * @param nPoints
     * @param length
     * @param elapsedTime
     * @param movingTime
     * @param maxSpeed
     * @param avgSpeed
     * @param avgMovingSpeed
     * @param maxEle
     * @param minEle
     * @param avgEle
     */
    public TrackStat(int nPoints, double length, double startTime,
        double elapsedTime, double movingTime, double maxSpeed, double avgSpeed,
        double avgMovingSpeed, double maxEle, double minEle, double avgEle) {
        this.nPoints = nPoints;
        this.startTime = startTime;
        this.elapsedTime = elapsedTime;
        this.movingTime = movingTime;
        this.maxSpeed = maxSpeed;
        this.avgSpeed = avgSpeed;
        this.avgMovingSpeed = avgMovingSpeed;
        this.maxEle = maxEle;
        this.minEle = minEle;
        this.avgEle = avgEle;
    }

    /**
     * @return The value of nPoints.
     */
    public int getNPoints() {
        return nPoints;
    }

    /**
     * @param nPoints The new value for nPoints.
     */
    public void setNPoints(int nPoints) {
        this.nPoints = nPoints;
    }

    /**
     * @return The value of length in m.
     */
    public double getLength() {
        return length;
    }

    /**
     * @param length The new value for length in m.
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * @return The value of startTime.
     */
    public double getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The new value for startTime.
     */
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    /**
     * @return The value of elapsed time in millisec.
     */
    public double getElapsedTime() {
        return elapsedTime;
    }

    /**
     * @param elapsedTime The new value for elapsed time in millisec.
     */
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * @return The value of moving time in millisec.
     */
    public double getMovingTime() {
        return movingTime;
    }

    /**
     * @param movingTime The new value for moving time in millisec.
     */
    public void setMovingTime(double movingTime) {
        this.movingTime = movingTime;
    }

    /**
     * @return The value of maximum speed in m/sec.
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @param maxSpeed The new value for maximum speed in m/sec.
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * @return The value of average speed in m/sec.
     */
    public double getAvgSpeed() {
        return avgSpeed;
    }

    /**
     * @param avgSpeed The new value for average speed in m/sec.
     */
    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    /**
     * @return The value of average moving speed in m/sec.
     */
    public double getAvgMovingSpeed() {
        return avgMovingSpeed;
    }

    /**
     * @param avgMovingSpeed The new value for average moving speed in m/sec.
     */
    public void setAvgMovingSpeed(double avgMovingSpeed) {
        this.avgMovingSpeed = avgMovingSpeed;
    }

    /**
     * @return The value of maximum elevation in meters.
     */
    public double getMaxEle() {
        return maxEle;
    }

    /**
     * @param maxEle The new value for maximum elevation in meters.
     */
    public void setMaxEle(double maxEle) {
        this.maxEle = maxEle;
    }

    /**
     * @return The value of minimum elevation in meters.
     */
    public double getMinEle() {
        return minEle;
    }

    /**
     * @param minEle The new value for minimum elevation in meters.
     */
    public void setMinEle(double minEle) {
        this.minEle = minEle;
    }

    /**
     * @return The value of average elevation.
     */
    public double getAvgEle() {
        return avgEle;
    }

    /**
     * @param avgEle The new value for average elevation in meters.
     */
    public void setAvgEle(double avgEle) {
        this.avgEle = avgEle;
    }

    /**
     * @return The value of the elevation difference between min and max.
     */
    public double getDeltaEle() {
        return maxEle - minEle;

    }

}
