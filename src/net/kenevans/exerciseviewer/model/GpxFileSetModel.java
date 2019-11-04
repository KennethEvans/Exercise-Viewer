package net.kenevans.exerciseviewer.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import net.kenevans.exerciseviewer.utils.Utils;

public class GpxFileSetModel
{
    private LinkedList<GpxFileModel> gpxFileModels;
    private String name = "GPX Files";

    /**
     * GpxFileSetModel constructor.
     * 
     * @param fileNames
     */
    public GpxFileSetModel() {
        gpxFileModels = new LinkedList<GpxFileModel>();
    }

    /**
     * GpxFileSetModel constructor.
     * 
     * @param fileNames
     */
    public GpxFileSetModel(String[] fileNames) {
        gpxFileModels = new LinkedList<GpxFileModel>();
        String fileInProgress = null;
        try {
            for(String fileName : fileNames) {
                // Trap any blank items
                if(fileName.length() == 0) {
                    continue;
                }
                fileInProgress = fileName;
                gpxFileModels.add(new GpxFileModel(fileName));
            }
        } catch(Throwable t) {
            Utils.excMsg("Error parsing " + fileInProgress, t);
        }
    }

    /**
     * GpxFileSetModel constructor.
     * 
     * @param files
     */
    public GpxFileSetModel(File[] files) {
        gpxFileModels = new LinkedList<GpxFileModel>();
        String fileInProgress = null;
        try {
            for(File file : files) {
                gpxFileModels.add(new GpxFileModel(file.getPath()));
            }
        } catch(Throwable t) {
            Utils.excMsg("Error parsing " + fileInProgress, t);
        }
    }

    /**
     * Removes an element from the gpxFileModel list.
     * 
     * @param model
     * @return true if this list contained the specified element.
     * @see java.util.List#remove
     */
    public boolean remove(GpxFileModel model) {
        return gpxFileModels.remove(model);
    }

    /**
     * Adds an element to the GpxFileModel list.
     * 
     * @param newModel The model to be added.
     * @return true if the add appears to be successful.
     */
    public boolean add(GpxFileModel model) {
        return gpxFileModels.add(model);
    }

    /**
     * @return The value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The new value for name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The value of gpxFileModels.
     */
    public List<GpxFileModel> getGpxFileModels() {
        return gpxFileModels;
    }

}
