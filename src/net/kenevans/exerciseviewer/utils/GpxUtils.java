package net.kenevans.exerciseviewer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/*
 * Created on Sep 6, 2010
 * By Kenneth Evans, Jr.
 */

public class GpxUtils
{
    /**
     * Nominal radius of the earth in miles. The radius actually varies from
     * 3937 to 3976 mi.
     */
    public static final double REARTH = 3956;
    /** Multiplier to convert miles to nautical miles. */
    public static final double MI2NMI = 1.852; // Exact
    /** Multiplier to convert degrees to radians. */
    public static final double DEG2RAD = Math.PI / 180.;
    /** Multiplier to convert feet to miles. */
    public static final double FT2MI = 1. / 5280.;
    /** Multiplier to convert meters to miles. */
    public static final double M2MI = .00062137119224;
    /** Multiplier to convert kilometers to miles. */
    public static final double KM2MI = .001 * M2MI;
    /** Multiplier to convert meters to feet. */
    public static final double M2FT = 3.280839895;
    /** Multiplier to convert sec to hours. */
    public static final double SEC2HR = 1. / 3600.;
    /** Multiplier to convert millisec to hours. */
    public static final double MS2HR = .001 * SEC2HR;

    /**
     * The speed in m/sec below which there is considered to be no movement for
     * the purposes of calculating Moving Time. This is, of course, arbitrary.
     * Note that 1 mi/hr is 0.44704 m/sec. This is expected to be set from
     * preferences.
     */
    public static double NO_MOVE_SPEED = .5;

    /**
     * Returns great circle distance in meters. assuming a spherical earth. Uses
     * Haversine formula.
     * 
     * @param lat1 Start latitude in deg.
     * @param lon1 Start longitude in deg.
     * @param lat2 End latitude in deg.
     * @param lon2 End longitude in deg.
     * @return
     */
    public static double greatCircleDistance(double lat1, double lon1,
        double lat2, double lon2) {
        double slon, slat, a, c, d;

        // Convert to radians
        lat1 *= DEG2RAD;
        lon1 *= DEG2RAD;
        lat2 *= DEG2RAD;
        lon2 *= DEG2RAD;

        // Haversine formula
        slon = Math.sin((lon2 - lon1) / 2.);
        slat = Math.sin((lat2 - lat1) / 2.);
        a = slat * slat + Math.cos(lat1) * Math.cos(lat2) * slon * slon;
        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        d = REARTH / M2MI * c;

        return (d);
    }

    public static String timeString(double time) {
        if(Double.isNaN(time)) {
            return "NA";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        long longTime = Math.round(time);
        Date date = new Date(longTime);
        return formatter.format(date);
    }

}
