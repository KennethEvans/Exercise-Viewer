package net.kenevans.stlviewer.preferences;

import java.io.File;
import java.util.prefs.Preferences;

import net.kenevans.core.utils.Utils;
import net.kenevans.stlviewer.model.IConstants;
import net.kenevans.stlviewer.ui.STLViewer;

/*
 * Created on Aug 7, 2014
 * By Kenneth Evans, Jr.
 */

/**
 * Settings stores the settings for the STLViewer.
 * 
 * @author Kenneth Evans, Jr.
 */
public class Settings implements IConstants
{
    private String defaultDirectory = D_DEFAULT_DIR;
    private String database = D_DB;

    private boolean hrVisible = D_HR_VISIBILITY;
    private boolean hrZonesVisible = D_HR_ZONES_VISIBILITY;
    private boolean speedVisible = D_SPEED_VISIBILITY;
    private boolean eleVisible = D_ELE_VISIBILITY;

    private int hrRollingAvgCount = D_HR_ROLLING_AVG_COUNT;
    private int speedRollingAvgCount = D_SPEED_ROLLING_AVG_COUNT;
    private int eleRollingAvgCount = D_ELE_ROLLING_AVG_COUNT;

    private int zone1Val = D_ZONE_1_VAL;
    private int zone2Val = D_ZONE_2_VAL;
    private int zone3Val = D_ZONE_3_VAL;
    private int zone4Val = D_ZONE_4_VAL;
    private int zone5Val = D_ZONE_5_VAL;
    private int zone6Val = D_ZONE_6_VAL;

    private String zone1Color = D_ZONE_1_COLOR;
    private String zone2Color = D_ZONE_2_COLOR;
    private String zone3Color = D_ZONE_3_COLOR;
    private String zone4Color = D_ZONE_4_COLOR;
    private String zone5Color = D_ZONE_5_COLOR;
    private String zone6Color = D_ZONE_6_COLOR;

    private int maxHr = D_MAX_HR;
    private int restHr = D_REST_HR;
    private int age = D_AGE;
    private boolean useKorvonen = D_USE_KORVONEN;

    /**
     * Loads the settings from the preferences
     */
    public void loadFromPreferences() {
        Preferences prefs = STLViewer.getUserPreferences();
        defaultDirectory = prefs.get(P_DEFAULT_DIR, D_DEFAULT_DIR);
        database = prefs.get(P_DB, D_DB);

        hrVisible = prefs.getBoolean(P_HR_VISIBILITY, D_HR_VISIBILITY);
        hrZonesVisible = prefs.getBoolean(P_HR_ZONES_VISIBILITY,
            D_HR_ZONES_VISIBILITY);
        speedVisible = prefs.getBoolean(P_SPEED_VISIBILITY, D_SPEED_VISIBILITY);
        eleVisible = prefs.getBoolean(P_ELE_VISIBILITY, D_ELE_VISIBILITY);

        hrRollingAvgCount = prefs.getInt(P_HR_ROLLING_AVG_COUNT,
            D_HR_ROLLING_AVG_COUNT);
        speedRollingAvgCount = prefs.getInt(P_SPEED_ROLLING_AVG_COUNT,
            D_SPEED_ROLLING_AVG_COUNT);
        eleRollingAvgCount = prefs.getInt(P_ELE_ROLLING_AVG_COUNT,
            D_ELE_ROLLING_AVG_COUNT);

        zone1Val = prefs.getInt(P_ZONE_1_VAL, D_ZONE_1_VAL);
        zone2Val = prefs.getInt(P_ZONE_2_VAL, D_ZONE_2_VAL);
        zone3Val = prefs.getInt(P_ZONE_3_VAL, D_ZONE_3_VAL);
        zone4Val = prefs.getInt(P_ZONE_4_VAL, D_ZONE_4_VAL);
        zone5Val = prefs.getInt(P_ZONE_5_VAL, D_ZONE_5_VAL);
        zone6Val = prefs.getInt(P_ZONE_6_VAL, D_ZONE_6_VAL);

        zone1Color = prefs.get(P_ZONE_1_COLOR, D_ZONE_1_COLOR);
        zone2Color = prefs.get(P_ZONE_2_COLOR, D_ZONE_2_COLOR);
        zone3Color = prefs.get(P_ZONE_3_COLOR, D_ZONE_3_COLOR);
        zone4Color = prefs.get(P_ZONE_4_COLOR, D_ZONE_4_COLOR);
        zone5Color = prefs.get(P_ZONE_5_COLOR, D_ZONE_5_COLOR);
        zone6Color = prefs.get(P_ZONE_6_COLOR, D_ZONE_6_COLOR);

        maxHr = prefs.getInt(P_MAX_HR, D_MAX_HR);
        restHr = prefs.getInt(P_REST_HR, D_REST_HR);
        age = prefs.getInt(P_AGE, D_AGE);
        useKorvonen = prefs.getBoolean(P_USE_KORVONEN, D_USE_KORVONEN);
    }

    /**
     * Save the current values to the preferences.
     * 
     * @param showErrors Use Utils.errMsg() to show the errors.
     * @return
     */
    public boolean saveToPreferences(boolean showErrors) {
        boolean retVal = checkValues(showErrors);
        if(!retVal) {
            return retVal;
        }
        try {
            Preferences prefs = STLViewer.getUserPreferences();

            prefs.put(P_DEFAULT_DIR, defaultDirectory);
            prefs.put(P_DB, database);

            prefs.putBoolean(P_HR_VISIBILITY, hrVisible);
            prefs.putBoolean(P_HR_ZONES_VISIBILITY, hrZonesVisible);
            prefs.putBoolean(P_SPEED_VISIBILITY, speedVisible);
            prefs.putBoolean(P_ELE_VISIBILITY, eleVisible);

            prefs.putInt(P_HR_ROLLING_AVG_COUNT, hrRollingAvgCount);
            prefs.putInt(P_SPEED_ROLLING_AVG_COUNT, speedRollingAvgCount);
            prefs.putInt(P_ELE_ROLLING_AVG_COUNT, eleRollingAvgCount);

            prefs.putInt(P_ZONE_1_VAL, zone1Val);
            prefs.putInt(P_ZONE_2_VAL, zone2Val);
            prefs.putInt(P_ZONE_3_VAL, zone3Val);
            prefs.putInt(P_ZONE_4_VAL, zone4Val);
            prefs.putInt(P_ZONE_5_VAL, zone5Val);
            prefs.putInt(P_ZONE_6_VAL, zone6Val);

            prefs.put(P_ZONE_1_COLOR, zone1Color);
            prefs.put(P_ZONE_2_COLOR, zone2Color);
            prefs.put(P_ZONE_3_COLOR, zone3Color);
            prefs.put(P_ZONE_4_COLOR, zone4Color);
            prefs.put(P_ZONE_5_COLOR, zone5Color);
            prefs.put(P_ZONE_6_COLOR, zone6Color);

            prefs.putInt(P_MAX_HR, maxHr);
            prefs.putInt(P_REST_HR, restHr);
            prefs.putInt(P_AGE, age);
            prefs.putBoolean(P_USE_KORVONEN, useKorvonen);
        } catch(Exception ex) {
            retVal = false;
            if(showErrors) {
                Utils.excMsg("Error saving preferences", ex);
            }
        }
        return retVal;
    }

    /**
     * Returns if the parameters are valid
     * 
     * @param showErrors Use Utils.errMsg() to show the errors.
     * @return
     */
    public boolean checkValues(boolean showErrors) {
        boolean retVal = true;

        // Default directory
        if(defaultDirectory == null) {
            if(showErrors) {
                Utils.errMsg("Value for the default directory is null");
            }
            retVal = false;
        } else {
            File file = new File(defaultDirectory);
            if(file == null) {
                if(showErrors) {
                    Utils.errMsg("The default directory is invalid");
                }
                retVal = false;
            } else {
                if(!file.exists()) {
                    if(showErrors) {
                        Utils.errMsg("The default directory does not exist");
                    }
                    retVal = false;
                } else {
                    if(!file.isDirectory()) {
                        if(showErrors) {
                            Utils
                                .errMsg("The default directory is not a directory");
                        }
                        retVal = false;
                    }
                }
            }
        }
        
        // Database
        if(database == null) {
            if(showErrors) {
                Utils.errMsg("Value for the database is null");
            }
            retVal = false;
        } else {
            File file = new File(database);
            if(file == null) {
                if(showErrors) {
                    Utils.errMsg("The database is invalid");
                }
                retVal = false;
            } else {
                if(!file.exists()) {
                    if(showErrors) {
                        Utils.errMsg("The database does not exist");
                    }
                    retVal = false;
                }
            }
        }

        return retVal;
    }

    /**
     * Copies the values in the given settings to this settings.
     * 
     * @param settings
     */
    public void copyFrom(Settings settings) {
        this.defaultDirectory = settings.defaultDirectory;
        this.database = settings.database;

        this.hrVisible = settings.hrVisible;
        this.hrZonesVisible = settings.hrZonesVisible;
        this.speedVisible = settings.speedVisible;
        this.eleVisible = settings.eleVisible;

        this.hrRollingAvgCount = settings.hrRollingAvgCount;
        this.speedRollingAvgCount = settings.speedRollingAvgCount;
        this.eleRollingAvgCount = settings.eleRollingAvgCount;

        this.zone1Val = settings.zone1Val;
        this.zone2Val = settings.zone2Val;
        this.zone3Val = settings.zone3Val;
        this.zone4Val = settings.zone4Val;
        this.zone5Val = settings.zone5Val;
        this.zone6Val = settings.zone6Val;

        this.zone1Color = settings.zone1Color;
        this.zone2Color = settings.zone2Color;
        this.zone3Color = settings.zone3Color;
        this.zone4Color = settings.zone4Color;
        this.zone5Color = settings.zone5Color;
        this.zone6Color = settings.zone6Color;

        this.maxHr = settings.maxHr;
        this.restHr = settings.restHr;
        this.age = settings.age;
        this.useKorvonen = settings.useKorvonen;
    }

    /**
     * @return The value of defaultDirectory.
     */
    public String getDefaultDirectory() {
        return defaultDirectory;
    }

    /**
     * @param defaultDirectory The new value for defaultDirectory.
     */
    public void setDefaultDirectory(String defaultDirectory) {
        this.defaultDirectory = defaultDirectory;
    }

    /**
     * @return The value of database.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @param database The new value for database.
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * @return The value of hrVisible.
     */
    public boolean getHrVisible() {
        return hrVisible;
    }

    /**
     * @param hrVisible The new value for hrVisible.
     */
    public void setHrVisible(boolean hrVisible) {
        this.hrVisible = hrVisible;
    }

    /**
     * @return The value of hrZonesVisible.
     */
    public boolean getHrZonesVisible() {
        return hrZonesVisible;
    }

    /**
     * @param hrZonesVisible The new value for hrZonesVisible.
     */
    public void setHrZonesVisible(boolean hrZonesVisible) {
        this.hrZonesVisible = hrZonesVisible;
    }

    /**
     * @return The value of speedVisible.
     */
    public boolean getSpeedVisible() {
        return speedVisible;
    }

    /**
     * @param speedVisible The new value for speedVisible.
     */
    public void setSpeedVisible(boolean speedVisible) {
        this.speedVisible = speedVisible;
    }

    /**
     * @return The value of eleVisible.
     */
    public boolean getEleVisible() {
        return eleVisible;
    }

    /**
     * @param eleVisible The new value for eleVisible.
     */
    public void setEleVisible(boolean eleVisible) {
        this.eleVisible = eleVisible;
    }

    /**
     * @return The value of hrRollingAvgCount.
     */
    public int getHrRollingAvgCount() {
        return hrRollingAvgCount;
    }

    /**
     * @param hrRollingAvgCount The new value for hrRollingAvgCount.
     */
    public void setHrRollingAvgCount(int hrRollingAvgCount) {
        this.hrRollingAvgCount = hrRollingAvgCount;
    }

    /**
     * @return The value of speedRollingAvgCount.
     */
    public int getSpeedRollingAvgCount() {
        return speedRollingAvgCount;
    }

    /**
     * @param speedRollingAvgCount The new value for speedRollingAvgCount.
     */
    public void setSpeedRollingAvgCount(int speedRollingAvgCount) {
        this.speedRollingAvgCount = speedRollingAvgCount;
    }

    /**
     * @return The value of eleRollingAvgCount.
     */
    public int getEleRollingAvgCount() {
        return eleRollingAvgCount;
    }

    /**
     * @param eleRollingAvgCount The new value for eleRollingAvgCount.
     */
    public void setEleRollingAvgCount(int eleRollingAvgCount) {
        this.eleRollingAvgCount = eleRollingAvgCount;
    }

    /**
     * @return The value of zone1Val.
     */
    public int getZone1Val() {
        return zone1Val;
    }

    /**
     * @param zone1Val The new value for zone1Val.
     */
    public void setZone1Val(int zone1Val) {
        this.zone1Val = zone1Val;
    }

    /**
     * @return The value of zone2Val.
     */
    public int getZone2Val() {
        return zone2Val;
    }

    /**
     * @param zone2Val The new value for zone2Val.
     */
    public void setZone2Val(int zone2Val) {
        this.zone2Val = zone2Val;
    }

    /**
     * @return The value of zone3Val.
     */
    public int getZone3Val() {
        return zone3Val;
    }

    /**
     * @param zone3Val The new value for zone3Val.
     */
    public void setZone3Val(int zone3Val) {
        this.zone3Val = zone3Val;
    }

    /**
     * @return The value of zone4Val.
     */
    public int getZone4Val() {
        return zone4Val;
    }

    /**
     * @param zone4Val The new value for zone4Val.
     */
    public void setZone4Val(int zone4Val) {
        this.zone4Val = zone4Val;
    }

    /**
     * @return The value of zone5Val.
     */
    public int getZone5Val() {
        return zone5Val;
    }

    /**
     * @param zone5Val The new value for zone5Val.
     */
    public void setZone5Val(int zone5Val) {
        this.zone5Val = zone5Val;
    }

    /**
     * @return The value of zone6Val.
     */
    public int getZone6Val() {
        return zone6Val;
    }

    /**
     * @param zone6Val The new value for zone6Val.
     */
    public void setZone6Val(int zone6Val) {
        this.zone6Val = zone6Val;
    }

    /**
     * @return The value of zone1Color.
     */
    public String getZone1Color() {
        return zone1Color;
    }

    /**
     * @param zone1Color The new value for zone1Color.
     */
    public void setZone1Color(String zone1Color) {
        this.zone1Color = zone1Color;
    }

    /**
     * @return The value of zone2Color.
     */
    public String getZone2Color() {
        return zone2Color;
    }

    /**
     * @param zone2Color The new value for zone2Color.
     */
    public void setZone2Color(String zone2Color) {
        this.zone2Color = zone2Color;
    }

    /**
     * @return The value of zone3Color.
     */
    public String getZone3Color() {
        return zone3Color;
    }

    /**
     * @param zone3Color The new value for zone3Color.
     */
    public void setZone3Color(String zone3Color) {
        this.zone3Color = zone3Color;
    }

    /**
     * @return The value of zone4Color.
     */
    public String getZone4Color() {
        return zone4Color;
    }

    /**
     * @param zone4Color The new value for zone4Color.
     */
    public void setZone4Color(String zone4Color) {
        this.zone4Color = zone4Color;
    }

    /**
     * @return The value of zone5Color.
     */
    public String getZone5Color() {
        return zone5Color;
    }

    /**
     * @param zone5Color The new value for zone5Color.
     */
    public void setZone5Color(String zone5Color) {
        this.zone5Color = zone5Color;
    }

    /**
     * @return The value of zone6Color.
     */
    public String getZone6Color() {
        return zone6Color;
    }

    /**
     * @param zone6Color The new value for zone6Color.
     */
    public void setZone6Color(String zone6Color) {
        this.zone6Color = zone6Color;
    }

    /**
     * @return The value of maxHr.
     */
    public int getMaxHr() {
        return maxHr;
    }

    /**
     * @param maxHr The new value for maxHr.
     */
    public void setMaxHr(int maxHr) {
        this.maxHr = maxHr;
    }

    /**
     * @return The value of restHr.
     */
    public int getRestHr() {
        return restHr;
    }

    /**
     * @param restHr The new value for restHr.
     */
    public void setRestHr(int restHr) {
        this.restHr = restHr;
    }

    /**
     * @return The value of age.
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age The new value for age.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return The value of useKorvonen.
     */
    public boolean getUseKorvonen() {
        return useKorvonen;
    }

    /**
     * @param useKorvonen The new value for useKorvonen.
     */
    public void setUseKorvonen(boolean useKorvonen) {
        this.useKorvonen = useKorvonen;
    }

}
