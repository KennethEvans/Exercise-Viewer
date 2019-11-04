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
    public static final String VERSION = "2.1.0";
    /** The title for the plot. */
    public static final String PLOT_TITLE = "Exercise Data";
    /** The frame width for the viewer. */
    public static final int FRAME_WIDTH = 1200;
    /** The frame height for the viewer. */
    public static final int FRAME_HEIGHT = 750;
    /** The divider location for the main split pane. */
    public static final int MAIN_PANE_DIVIDER_LOCATION = 55 * FRAME_HEIGHT
        / 100;
    /** The divider location for the lower split pane. */
    public static final int LOWER_PANE_DIVIDER_LOCATION = FRAME_WIDTH / 2;

    /***
     * The name of the preference node for accessing preferences for this
     * application. On Windows these are found in the registry under
     * HKCU/JavaSoft/Prefs.
     */
    public static final String P_PREFERENCE_NODE = "net/kenevans/exerciseviewer/preferences";

    /*** The preference name for the locations for finding files. */
    public static final String P_FILE_LOCATIONS = "fileLocations";
    /*** The default value for the locations for finding files. */
    public static final String D_FILE_LOCATIONS = "{}";

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

    // KML
    public static final int KML_COLOR_MODE_COLOR = 0;
    public static final int KML_COLOR_MODE_COLORSET = 1;
    public static final int KML_COLOR_MODE_RAINBOW = 2;
    public static final String[][] kmlColorModes = {
        {"Color", Integer.toString(KML_COLOR_MODE_COLOR)},
        {"Color Set", Integer.toString(KML_COLOR_MODE_COLORSET)},
        {"Rainbow", Integer.toString(KML_COLOR_MODE_RAINBOW)},};
    
    public static final String P_KML_FILENAME = "kmlFileName";
    public static final String D_KML_FILENAME = "";

    /** The track color */
    public static final String P_TRK_COLOR = "trkColor";
    public static final String D_TRK_COLOR = "ff0000";
    /** The track alpha */
    public static final String P_TRK_ALPHA = "trkAlpha";
    public static final String D_TRK_ALPHA = "ff";
    /** The track line width. */
    public static final String P_TRK_LINEWIDTH = "trkLineWidth";
    public static final String D_TRK_LINEWIDTH = "2.0";
    /** The track color mode */
    public static final String P_TRK_COLOR_MODE = "trkColorMode";
    public static final int D_TRK_COLOR_MODE = KML_COLOR_MODE_RAINBOW;

    /** The route line width. */
    public static final String P_RTE_LINEWIDTH = "rteLineWidth";
    public static final String D_RTE_LINEWIDTH = "2.0";
    /** The route color mode */
    public static final String P_RTE_COLOR_MODE = "rteColorMode";
    public static final int D_RTE_COLOR_MODE = KML_COLOR_MODE_COLOR;
    /** The routepoint icon color, actually a mask to & the image with */
    public static final String P_RTE_COLOR = "rteIconColor";
    public static final String D_RTE_COLOR = "0000ff";
    /** The waypoint alpha */
    public static final String P_RTE_ALPHA = "rteAlpha";
    public static final String D_RTE_ALPHA = "ff";

    /** The waypoint icon color, actually a mask to & the image with */
    public static final String P_WPT_COLOR = "wptIconColor";
    public static final String D_WPT_COLOR = "ffcc66";
    /** The waypoint alpha */
    public static final String P_WPT_ALPHA = "wptAlpha";
    public static final String D_WPT_ALPHA = "ff";
    /** The track color mode */
    public static final String P_WPT_COLOR_MODE = "wptColorMode";
    public static final int D_WPT_COLOR_MODE = KML_COLOR_MODE_COLOR;

    /** The icon scale, Use 1 for normal and 0 for label only (no icon) */
    public static final String P_ICON_SCALE = "iconScale";
    public static final String D_ICON_SCALE = "1.0";
    /** Determines if icons are shown at the start of a track */
    public static final String P_USE_TRK_ICON = "useTrkIcon";
    public static final Boolean D_USE_TRK_ICON = true;
    /** Determines if a Track with TimeStamps is shown. */
    public static final String P_USE_TRK_TRACK = "useTrkTrack";
    public static final Boolean D_USE_TRK_TRACK = false;
    /** Determines if icons are shown at the start of a route */
    public static final String P_USE_RTE_ICON = "useRteIcon";
    public static final Boolean D_USE_RTE_ICON = true;
    /**
     * The URL for the home icon. Using white will allow the mask to be more
     * effective. (The default icon is Yellow = #ff00ffff).
     */
    public static final String P_TRK_ICON_URL = "homeIconUrl";
    public static final String D_TRK_ICON_URL = "http://maps.google.com/mapfiles/kml/pushpin/wht-pushpin.png";
    /**
     * The URL for the routepoint icon. Using white will allow the mask to be
     * more effective. (The default icon is Yellow = #ff00ffff).
     */
    public static final String P_RTE_ICON_URL = "rteIconUrl";
    public static final String D_RTE_ICON_URL = "http://maps.google.com/mapfiles/kml/paddle/wht-circle.png";
    /**
     * The URL for the waypoint icon. Using white will allow the mask to be more
     * effective. (The default icon is Yellow = #ff00ffff).
     */
    public static final String P_WPT_ICON_URL = "wptIconUrl";
    public static final String D_WPT_ICON_URL = "http://maps.google.com/mapfiles/kml/paddle/wht-circle.png";

    /** Whether to prompt before overwriting the KML file. */
    public static final String P_KML_PROMPT_TO_OVERWRITE = "kmlPromptToOverwrite";
    public static final Boolean D_KML_PROMPT_TO_OVERWRITE = false;
    /** Whether to send the KML file to Google Earth. */
    public static final String P_KML_SEND_TO_GOOGLE_EARTH = "kmlSendToGoogle";
    public static final Boolean D_KML_SEND_TO_GOOGLE_EARTH = true;

}
