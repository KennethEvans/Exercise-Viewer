package net.kenevans.exerciseviewer.preferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.kenevans.core.utils.Utils;
import net.kenevans.exerciseviewer.model.IConstants;
import net.kenevans.exerciseviewer.ui.ExerciseViewer;

/**
 * FileLocations manages a list of FileLocation's.
 * 
 * @author Kenneth Evans, Jr.
 */
public class FileLocations implements IConstants
{
    public static enum FilterMode {
        GPX, TCX, GPXTCX
    };

    private ArrayList<FileLocation> fileLocations = new ArrayList<>();

    public FileLocations(ArrayList<FileLocation> fileLocations) {
        this.fileLocations = fileLocations;
    }

    /**
     * FileLocations copy constructor that sets the fileLocations from the
     * input, making an independent copy.
     * 
     * @param repositoryLocations
     */
    public FileLocations(FileLocations oldFileLocations) {
        for(FileLocation fileLocation : oldFileLocations.getFileLocations()) {
            fileLocations.add(fileLocation);
        }
    }

    /**
     * FileLocations constructor that has an empty list for the fileLocations.
     */
    public FileLocations() {
    }

    /**
     * Loads the repository locations from the preferences.
     */
    public void loadFromPreferences() {
        Preferences prefs = ExerciseViewer.getUserPreferences();
        String jsonFileLocations = prefs.get(P_FILE_LOCATIONS,
            D_FILE_LOCATIONS);
        loadFromJsonString(jsonFileLocations);
    }

    /**
     * Save the current values to the preferences.
     * 
     * @param showErrors Use Utils.errMsg() to show the errors.
     * @return
     */
    public boolean saveToPreferences(boolean showErrors) {
        boolean retVal = true;
        try {
            String jsonFileLocations = getJsonString();
            Preferences prefs = ExerciseViewer.getUserPreferences();
            // // DEBUG
            // System.out.println(
            // "Save: jsonRepositoryLocations=" + jsonRepositoryLocations);
            prefs.put(P_FILE_LOCATIONS, jsonFileLocations);
        } catch(Exception ex) {
            retVal = false;
            if(showErrors) {
                Utils.excMsg("Error storing fileLocatrions", ex);
            }
        }
        return retVal;
    }

    /**
     * Loads the repository locations from a JSON string.
     */
    public void loadFromJsonString(String jsonFileLocations) {
        // // DEBUG
        // System.out.println(
        // "Load: jsonRepositoryLocations=" + jsonRepositoryLocations);
        Gson gson = new Gson();
        FileLocations newLocations = gson.fromJson(jsonFileLocations,
            new TypeToken<FileLocations>() {
            }.getType());
        if(fileLocations != null) {
            this.fileLocations = newLocations.fileLocations;
        }
    }

    /**
     * Loads the repository locations from a JSON string.
     */
    public void loadFromJsonFile(File file) {
        String jsonString;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null) {
                sb.append(line);
                line = br.readLine();
            }
            jsonString = sb.toString();
            loadFromJsonString(jsonString);
        } catch(Exception ex) {
            Utils.excMsg("Error parsing JSON file", ex);
            return;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch(Exception ex) {
                    // Do nothing
                }
            }
        }
    }

    /**
     * get the JSON string corresponding to this RepositoryLocations
     * 
     * @return
     */

    public String getJsonString() {
        Gson gson = new Gson();
        String jsonFileLocations = gson.toJson(this,
            new TypeToken<FileLocations>() {
            }.getType());
        return jsonFileLocations;
    }

    /**
     * @return The value of fileLocations.
     */
    public ArrayList<FileLocation> getFileLocations() {
        return fileLocations;
    }

    /**
     * FileLocation is a class to hold a file location plus parameters.
     * 
     * @author Kenneth Evans, Jr.
     */
    public static class FileLocation
    {
        public String fileName;
        public FilterMode filterMode;

        public FileLocation(String fileName, FilterMode filterMode) {
            this.fileName = fileName;
            this.filterMode = filterMode;
        }

    }

}
