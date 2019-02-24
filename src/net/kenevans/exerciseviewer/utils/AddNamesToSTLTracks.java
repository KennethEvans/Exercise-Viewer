package net.kenevans.exerciseviewer.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.datatype.XMLGregorianCalendar;

import net.kenevans.core.utils.Utils;
import net.kenevans.gpxcombined.GpxType;
import net.kenevans.gpxcombined.TrkType;
import net.kenevans.gpxcombined.TrksegType;
import net.kenevans.gpxcombined.WptType;
import net.kenevans.gpxcombined.parser.GPXParser;

/*
 * Created on Sep 1, 2016
 * By Kenneth Evans, Jr.
 */

/**
 * AddNamesToSTLTracks replaces track names in a selected GPX file with names
 * from STL files with the same start time as the track. It fixes the problem
 * that since STL files have no track names, when STL tracks are copied into a
 * combined GPX file there is no way to tell which track is which.
 * 
 * It works by finding the start time for each track. It then attempts to find a
 * STL GPX file with a date that corresponds to the start time. It then uses the
 * useful part of the name, including the date, as the track name, giving a
 * useful identification of the track.
 * 
 * It is highly dependent on the STL way of naming GPX files.
 * 
 * @author Kenneth Evans, Jr.
 */
public class AddNamesToSTLTracks
{
    public static final String LS = System.getProperty("line.separator");

    /** Default starting directory for GPX files. */
    private static String defaultFilePath = "C:/Users/evans/Documents/GPSLink";

    /**
     * STL files start with this string. It will be eliminated in selecting a
     * name for the track.
     */
    private static final String STL_SUFFIX = "track";

    /** Hard-coded directory for STL files */
    private static final String STL_DIRECTORY = "C:/Users/evans/Documents/GPSLink/STL";

    /** Format for the way dates are embedded in STL file names. */
    private static final SimpleDateFormat defaultFormatter = new SimpleDateFormat(
        "yyyy-MM-dd");

    /**
     * Generic method to get a file using a JFileChooser
     * 
     * @param defaultPath
     * @return the File or null if aborted.
     */
    public static File getOpenFile(String defaultPath) {
        File file = null;
        JFileChooser chooser = new JFileChooser();
        if(defaultPath != null) {
            chooser.setCurrentDirectory(new File(defaultPath));
        }
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            // Save the selected path for next time
            defaultPath = chooser.getSelectedFile().getParentFile().getPath();
            // Process the file
            file = chooser.getSelectedFile();
        }
        return file;
    }

    /**
     * Gets the useful part of the filename, eliminating "track" at the
     * beginning and the number identifier at the end.
     * 
     * @param file
     * @return
     */
    public static String extractName(File file) {
        String name = file.getName();
        int len = name.length();
        String ext = Utils.getExtension(file);
        int extLen = ext.length();
        // Eliminate the track in front and the extension
        String newName = name.substring(5, len - extLen - 1);
        // Eliminate the number at the end
        int lastDashIndex = newName.lastIndexOf("-");
        newName = newName.substring(0, lastDashIndex);
        // Check if there is still a - at the end
        while(newName.endsWith("-")) {
            newName = newName.substring(0, newName.length() - 1);
        }
        return newName;
    }

    /**
     * Saves the given file with the extension .bak added (deleting any existing
     * such backup silently), then saves the modifications with the original
     * name.
     * 
     * @param file
     * @param gpxType
     * @return
     */
    public static boolean saveFile(File file, GpxType gpxType) {
        // Create a backup name
        String path = file.getPath() + ".bak";
        File newFile = new File(path);
        // Delete the backup if it exists
        if(newFile.exists()) {
            newFile.delete();
        }
        // Rename the original to the backup name
        boolean ok = file.renameTo(newFile);
        if(!ok) {
            String msg = "Could not rename " + file.getPath();
            Utils.errMsg(msg);
            return false;
        } else {
            System.out.println("Saved backup " + newFile.getPath());
        }

        // Save the changes to the original file
        try {
            GPXParser.save(gpxType.getCreator() + "(Tracks Renamed)", gpxType,
                file);
            System.out.println("Saved " + file.getPath());
        } catch(Exception ex) {
            Utils.excMsg("Error saving " + file.getName(), ex);
            return false;
        }
        return true;
    }

    /**
     * Processes the input file from beginning to end.
     * 
     * @param inputFile
     * @return
     */
    public static boolean process(File inputFile) {
        System.out.println("Processing: " + inputFile);
        GpxType gpx = null;
        try {
            gpx = GPXParser.parse(inputFile);
        } catch(Exception ex) {
            ex.printStackTrace();
            Utils.excMsg("Error reading " + inputFile.getPath(), ex);
        }

        // Get the files in the STL directory
        File dirFile = new File(STL_DIRECTORY);
        File[] files = dirFile.listFiles();
        ArrayList<File> stlFiles = new ArrayList<File>();
        for(File file : files) {
            stlFiles.add(file);
        }
        if(stlFiles.size() == 0) {
            Utils.errMsg("There are no files in " + STL_DIRECTORY);
            return false;
        }

        // Get the tracks and start time
        XMLGregorianCalendar xgcal;
        GregorianCalendar gcal;
        String newName;
        long startTime = Long.MAX_VALUE;
        long endTime = 0;
        long time;
        int nTracks = 0;
        int nMatches;
        boolean changed = false;
        ArrayList<File> matches;
        List<TrkType> tracks = gpx.getTrk();
        for(TrkType track : tracks) {
            nTracks++;
            startTime = Long.MAX_VALUE;
            endTime = 0;
            List<TrksegType> trackSegments = track.getTrkseg();
            for(TrksegType trackSegment : trackSegments) {
                List<WptType> trackPoints = trackSegment.getTrkpt();
                for(WptType tpt : trackPoints) {
                    xgcal = tpt.getTime();
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
                }
            }
            // Find if a STL file name matches the start time
            String pattern = STL_SUFFIX + defaultFormatter.format(startTime);
            nMatches = 0;
            matches = new ArrayList<File>();
            for(File file : stlFiles) {
                if(file.getName().contains(pattern)) {
                    nMatches++;
                    matches.add(file);
                }
            }
            if(nMatches == 0) {
                System.out.println("No match for track " + nTracks);
            } else if(nMatches == 1) {
                System.out.println(
                    "Match for track " + nTracks + " is " + matches.get(0));
                newName = extractName(matches.get(0));
                System.out.println("Old name is " + track.getName());
                System.out.println("New name is " + newName);
                track.setName(newName);
                changed = true;
            } else {
                System.out.println(nMatches + " matches for track " + nTracks);
                for(File match : matches) {
                    System.out.println("    " + match.getName());
                }
            }
        }
        if(nTracks == 0) {
            Utils.errMsg("There are no tracks in " + inputFile.getPath());
            return false;
        }
        System.out.println();
        if(changed) {
            System.out.println("Names have changed");
            int selection = JOptionPane.showConfirmDialog(null,
                inputFile.getName() + LS + "Names have changed" + LS
                    + "(Check the output for details)" + LS + "OK to replace?",
                "Warning", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if(selection != JOptionPane.OK_OPTION) {
                return false;
            }
            saveFile(inputFile, gpx);
        } else {
            System.out.println("No changes");
        }
        return true;
    }

    /**
     * Wrapper that runs the application. It gets the file and runs process().
     */
    public static void run() {
        File file = getOpenFile(defaultFilePath);
        if(file == null) {
            return;
        }
        boolean res = process(file);
        System.out.println();
        if(res) {
            System.out.println("All Done");
        } else {
            System.out.println("Aborting");
        }
    }

    /**
     * The main method.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Set window decorations
            JFrame.setDefaultLookAndFeelDecorated(true);

            // Set the native look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Make the job run in the AWT thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    AddNamesToSTLTracks.run();
                }
            });
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

}
