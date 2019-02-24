package net.kenevans.exerciseviewer.model;

import net.kenevans.exerciseviewer.utils.GpxUtils;

/*
 * Created on Jul 9, 2012
 * By Kenneth Evans, Jr.
 */

/**
 * Provides constants for classes related to Exercise Viewer files.
 * 
 * @author Kenneth Evans, Jr.
 */
/**
 * IConstants
 * 
 * @author Kenneth Evans, Jr.
 */
public interface IConstants
{
    public static final String LS = System.getProperty("line.separator");

    // public static final String FILE_NAME =
    // "track2014-06-30-Workout-Rehab-1475016.gpx";
    // public static final String FILE_NAME = "../CM2013.gpx";
    public static final String FILE_NAME = "track2014-06-30-Workout-Rehab-1475018-Combined.gpx";

    /** The title for the viewer. */
    public static final String TITLE = "Exercise Viewer";
    /** The version */
    public static final String VERSION = "2";
    /** The title for the plot. */
    public static final String PLOT_TITLE = "Exercise Data";
    /** The frame width for the viewer. */
    public static final int FRAME_WIDTH = 1200;
    /** The frame height for the viewer. */
    public static final int FRAME_HEIGHT = 750;
    /** The divider location for the main split pane. */
    public static final int MAIN_PANE_DIVIDER_LOCATION = 55 * FRAME_HEIGHT / 100;
    /** The divider location for the lower split pane. */
    public static final int LOWER_PANE_DIVIDER_LOCATION = FRAME_WIDTH / 2;

    /***
     * The name of the preference node for accessing preferences for this
     * application. On Windows these are found in the registry under
     * HKCU/JavaSoft/Prefs.
     */
    public static final String P_PREFERENCE_NODE = "net/kenevans/exerciseviewer/preferences";

    /*** The preference name for the default directory for finding GPX files. */
    public static final String P_DEFAULT_DIR = "defaultDir";
    /*** The default value for the default directory for finding GPX files. */
    public static final String D_DEFAULT_DIR = "C:/Users/evans/Documents/GPSLink/Polar";
    
    /** The number of data types. */
    public static int N_DATA_TYPES = 1;

    // HR
    /** Index for HR */
    public static int HR_INDEX = 0;
    /*** The preference name for the HR name. */
    public static final String P_HR_NAME = "hrName";
    /*** The default value for the HR name. */
    public static final String D_HR_NAME = "HR";
    /*** The preference name for the HR color. */
    public static final String P_HR_COLOR = "hrColor";
    /*** The default value for the HR color. */
    public static final String D_HR_COLOR = "0xC00000";
    /*** The preference name for the HR visibility. */
    public static final String P_HR_VISIBILITY = "hrVisibility";
    /*** The default value for the HR visibility. */
    public static final boolean D_HR_VISIBILITY = true;
    /*** The preference name for the HR range axis. */
    public static final String P_HR_RANGE_AXIS = "hrRangeAxis";
    /*** The default value for the HR range axis. */
    public static final String D_HR_RANGE_AXIS = Integer.toString(HR_INDEX);
    /*** The preference name for the HR rolling average count. */
    public static final String P_HR_ROLLING_AVG_COUNT = "hrRollingAvgCount";
    /*** The default value for the HR rolling average count. */
    public static final int D_HR_ROLLING_AVG_COUNT = 0;

    // HR Zones
    /** Index for HR */
    public static int HR_ZONES_INDEX = 4;
    /*** The preference name for the HR zones name. */
    public static final String P_HR_ZONES_NAME = "hrZonesName";
    /*** The default value for the HR zones name. */
    public static final String D_HR_ZONES_NAME = "HR Zones";
    /*** The preference name for the HR color. */
    public static final String P_HR_ZONES_COLOR = "hrZonesColor";
    /*** The default value for the HR zones color. */
    public static final String D_HR_ZONES_COLOR = "0x000000";
    /*** The preference name for the HR zones visibility. */
    public static final String P_HR_ZONES_VISIBILITY = "hrZonesVisibility";
    /*** The default value for the HR zones visibility. */
    public static final boolean D_HR_ZONES_VISIBILITY = true;
    /*** The preference name for the HR zones range axis. */
    public static final String P_HR_RANGE_ZONES_AXIS = "hrZonesRangeAxis";
    /*** The default value for the HR zones range axis. */
    public static final String D_HR_RANGE_ZONES_AXIS = Integer
        .toString(HR_INDEX);

    // Speed
    /** Index for SPEED */
    public static int SPEED_INDEX = 1;
    /*** The preference name for the Speed name. */
    public static final String P_SPEED_NAME = "speedName";
    /*** The default value for the Speed name. */
    public static final String D_SPEED_NAME = "Speed";
    /*** The preference name for the Speed color. */
    public static final String P_SPEED_COLOR = "speedColor";
    /*** The default value for the Speed color. */
    public static final String D_SPEED_COLOR = "0x2288FF";
    /*** The preference name for the Speed visibility. */
    public static final String P_SPEED_VISIBILITY = "speedVisibility";
    /*** The default value for the Speed visibility. */
    public static final boolean D_SPEED_VISIBILITY = false;
    /*** The preference name for the Speed range axis. */
    public static final String P_SPEED_RANGE_AXIS = "speedRangeAxis";
    /*** The default value for the Speed range axis. */
    public static final String D_SPEED_RANGE_AXIS = Integer
        .toString(SPEED_INDEX);
    /*** The preference name for the not moving speed. */
    public static final String P_SPEED_NOT_MOVING = "speedNotMoving";
    /*** The default value in m/sec for the the not moving speed. */
    public static final double D_SPEED_NOT_MOVING = GpxUtils.NO_MOVE_SPEED;
    /*** The preference name for the speed rolling average count. */
    public static final String P_SPEED_ROLLING_AVG_COUNT = "speedRollingAvgCount";
    /*** The default value for the speed rolling average count. */
    public static final int D_SPEED_ROLLING_AVG_COUNT = 5;

    // Elevation
    /** Index for ELE */
    public static int ELE_INDEX = 2;
    /*** The preference name for the Elevation name. */
    public static final String P_ELE_NAME = "ElevationName";
    /*** The default value for the Elevation name. */
    public static final String D_ELE_NAME = "Elevation";
    /*** The preference name for the Elevation color. */
    public static final String P_ELE_COLOR = "ElevationColor";
    /*** The default value for the Elevation color. */
    public static final String D_ELE_COLOR = "0x000AA";
    /*** The preference name for the Elevation visibility. */
    public static final String P_ELE_VISIBILITY = "ElevationVisibility";
    /*** The default value for the Elevation visibility. */
    public static final boolean D_ELE_VISIBILITY = false;
    /*** The preference name for the Elevation range axis. */
    public static final String P_ELE_RANGE_AXIS = "ElevationRangeAxis";
    /*** The default value for the Elevation range axis. */
    public static final String D_ELE_RANGE_AXIS = Integer.toString(ELE_INDEX);
    /*** The preference name for the elevation rolling average count. */
    public static final String P_ELE_ROLLING_AVG_COUNT = "elevationRollingAvgCount";
    /*** The default value for the elevation rolling average count. */
    public static final int D_ELE_ROLLING_AVG_COUNT = 5;

    // Zones
    /*** The preference name for zone 1 value. */
    public static final String P_ZONE_1_VAL = "Zone1Value";
    /*** The default value for zone 1 value. */
    public static final int D_ZONE_1_VAL = 79;
    /*** The preference name for the zone 1 color. */
    public static final String P_ZONE_1_COLOR = "Zone1Color";
    /*** The default value for zone 1 color. */
    public static final String D_ZONE_1_COLOR = "0x99CCFF";
    /*** The preference name for zone 1 value. */
    public static final String P_ZONE_2_VAL = "Zone2Value";
    /*** The default value for zone 1 value. */
    public static final int D_ZONE_2_VAL = 94;
    /*** The preference name for the zone 1 color. */
    public static final String P_ZONE_2_COLOR = "Zone2Color";
    /*** The default value for zone 1 color. */
    public static final String D_ZONE_2_COLOR = "0x66FF00";
    /*** The preference name for zone 1 value. */
    public static final String P_ZONE_3_VAL = "Zone3Value";
    /*** The default value for zone 1 value. */
    public static final int D_ZONE_3_VAL = 110;
    /*** The preference name for the zone 1 color. */
    public static final String P_ZONE_3_COLOR = "Zone3Color";
    /*** The default value for zone 1 color. */
    public static final String D_ZONE_3_COLOR = "0xFFFF00";
    /*** The preference name for zone 1 value. */
    public static final String P_ZONE_4_VAL = "Zone4Value";
    /*** The default value for zone 1 value. */
    public static final int D_ZONE_4_VAL = 126;
    /*** The preference name for the zone 1 color. */
    public static final String P_ZONE_4_COLOR = "Zone4Color";
    /*** The default value for zone 1 color. */
    public static final String D_ZONE_4_COLOR = "0xFFC800";
    /*** The preference name for zone 1 value. */
    public static final String P_ZONE_5_VAL = "Zone5Value";
    /*** The default value for zone 1 value. */
    public static final int D_ZONE_5_VAL = 141;
    /*** The preference name for the zone 1 color. */
    public static final String P_ZONE_5_COLOR = "Zone5Color";
    /*** The default value for zone 1 color. */
    public static final String D_ZONE_5_COLOR = "0xFf9000";
    /*** The preference name for zone 1 value. */
    public static final String P_ZONE_6_VAL = "Zone6Value";
    /*** The default value for zone 1 value. */
    public static final int D_ZONE_6_VAL = 157;
    /*** The preference name for the zone 1 color. */
    public static final String P_ZONE_6_COLOR = "Zone6Color";
    /*** The default value for zone 1 color. */
    public static final String D_ZONE_6_COLOR = "0xFF0000";

    /*** The preference name for the max HR. */
    public static final String P_MAX_HR = "MaxHeartRate";
    /*** The default value for the max HR. */
    public static final int D_MAX_HR = 157;
    /*** The preference name for the resting HR. */
    public static final String P_REST_HR = "RestingHeartRate";
    /*** The default value for the resting HR. */
    public static final int D_REST_HR = 60;
    /*** The preference name for the age. */
    public static final String P_AGE = "Age";
    /*** The default value for the resting HR. */
    public static final int D_AGE = 73;
    /*** The preference name for using Korvonen. */
    public static final String P_USE_KORVONEN = "UseKorvonen";
    /*** The default value for using Korvonen. */
    public static final boolean D_USE_KORVONEN = false;

    /** The prefix used to represent a boundary in the series name. */
    public static final String BOUNDARY_SERIES_NAME_PREFIX = "HR=";

    public static final String FILE_PATH = D_DEFAULT_DIR + "/" + FILE_NAME;

}
