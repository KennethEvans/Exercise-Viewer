package net.kenevans.exerciseviewer.kml;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import de.micromata.opengis.kml.v_2_2_0.ColorMode;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.gx.MultiTrack;
import de.micromata.opengis.kml.v_2_2_0.gx.Track;
import net.kenevans.exerciseviewer.model.GpxFileModel;
import net.kenevans.exerciseviewer.model.GpxFileSetModel;
import net.kenevans.exerciseviewer.model.IConstants;
import net.kenevans.exerciseviewer.utils.GpxUtils;
import net.kenevans.exerciseviewer.utils.RainbowColorScheme;
import net.kenevans.exerciseviewer.utils.Utils;
import net.kenevans.gpxcombined.RteType;
import net.kenevans.gpxcombined.TrkType;
import net.kenevans.gpxcombined.TrksegType;
import net.kenevans.gpxcombined.WptType;

public class KmlUtils implements IConstants
{
    private static boolean VERBOSE = false;
    /** The line separator for this platform. */
    public static final String LS = System.getProperty("line.separator");
    /** The number of points for the find circle copied to the clipboard. */
    private static final int N_CIRCLE_POINTS = 101;
    /** The delta used to determine d(lat)/d(radius) and d(lon)/d(radius). */
    private static final double DELTA_LATLON = .0001;
    /** Set of hard-coded line colorSetColors. it will cycle through these. */
    private static final String[] colorSetColors = {"0000ff", "00ff00",
        "ff0000", "ffff00", "ff00ff", "00ffff", "0077ff", "ff0077"};
    /** Array to hold the track colors, values depend on the mode. */
    private static String[] trkColors;
    /** Array to hold the route colors, values depend on the mode. */
    private static String[] rteColors;
    /** Array to hold the waypoint, values depend on the mode. */
    private static String[] wptColors;

    /**
     * @param args
     * @throws IOException
     */
    public static void createKml(String name, GpxFileSetModel fileSetModel,
        final KmlOptions kmlOptions) throws IOException {
        // Generate the KML
        final Kml kml = KmlFactory.createKml();

        // Create the Document for this file
        Document document = kml.createAndSetDocument().withName(name)
            .withOpen(true);

        // Make the Styles for this Document
        Style style;
        IconStyle iconStyle;
        // Trk Colors
        switch(kmlOptions.getTrkColorMode()) {
        case KML_COLOR_MODE_COLOR:
            createTrkColorColors(kmlOptions);
            break;
        case KML_COLOR_MODE_COLORSET:
            createTrkColorSetColors(kmlOptions);
            break;
        case KML_COLOR_MODE_RAINBOW:
            createTrkRainbowColors(kmlOptions, fileSetModel);
            break;
        }
        // Rte Colors
        switch(kmlOptions.getRteColorMode()) {
        case KML_COLOR_MODE_COLOR:
            createRteColorColors(kmlOptions);
            break;
        case KML_COLOR_MODE_COLORSET:
            createRteColorSetColors(kmlOptions);
            break;
        case KML_COLOR_MODE_RAINBOW:
            createRteRainbowColors(kmlOptions, fileSetModel);
            break;
        }
        // Wpt Colors
        switch(kmlOptions.getWptColorMode()) {
        case KML_COLOR_MODE_COLOR:
            createWptColorColors(kmlOptions);
            break;
        case KML_COLOR_MODE_COLORSET:
            createWptColorSetColors(kmlOptions);
            break;
        case KML_COLOR_MODE_RAINBOW:
            createWptRainbowColors(kmlOptions, fileSetModel);
            break;
        }
        // Create the color styles
        int nTrkColors = trkColors.length;
        for(String color : trkColors) {
            style = document.createAndAddStyle().withId("trk" + color);
            style.createAndSetLineStyle().withColor(color)
                .withWidth(kmlOptions.getTrkLineWidth());
            iconStyle = style.createAndSetIconStyle().withColor(color)
                .withColorMode(ColorMode.NORMAL)
                .withScale(kmlOptions.getIconScale());
            iconStyle.createAndSetIcon().withHref(kmlOptions.getTrkIconUrl());
        }
        int nRteColors = rteColors.length;
        for(String color : rteColors) {
            style = document.createAndAddStyle().withId("rte" + color);
            style.createAndSetLineStyle().withColor(color)
                .withWidth(kmlOptions.getTrkLineWidth());
            iconStyle = style.createAndSetIconStyle().withColor(color)
                .withColorMode(ColorMode.NORMAL)
                .withScale(kmlOptions.getIconScale());
            iconStyle.createAndSetIcon().withHref(kmlOptions.getRteIconUrl());
        }
        int nWptColors = wptColors.length;
        for(String color : wptColors) {
            style = document.createAndAddStyle().withId("wpt" + color);
            style.createAndSetLineStyle().withColor(color)
                .withWidth(kmlOptions.getTrkLineWidth());
            iconStyle = style.createAndSetIconStyle().withColor(color)
                .withColorMode(ColorMode.NORMAL)
                .withScale(kmlOptions.getIconScale());
            iconStyle.createAndSetIcon().withHref(kmlOptions.getWptIconUrl());
        }

        // // Routepoint icon
        // style = document.createAndAddStyle().withId(ROUTEPOINT_ICON_ID);
        // style.createAndSetLineStyle()
        // .withColor(kmlOptions.getRteAlpha() + kmlOptions.getRteColor())
        // .withWidth(kmlOptions.getRteLineWidth());
        // iconStyle = style.createAndSetIconStyle()
        // .withColor(kmlOptions.getRteAlpha() + kmlOptions.getRteColor())
        // .withColorMode(ColorMode.NORMAL)
        // .withScale(kmlOptions.getIconScale());
        // iconStyle.createAndSetIcon().withHref(kmlOptions.getRteIconUrl());

        // Loop over GPX files
        int nTrack = 0;
        int nRoute = 0;
        int nWaypoint = 0;
        List<WptType> wpt;
        Folder fileFolder;
        Folder folder;
        Folder routeFolder;
        MultiGeometry mg;
        Placemark placemark;
        Placemark trackPlacemark;
        MultiTrack mt;
        Track track;
        LineString ls = null;
        double lat, lon, alt;
        String fileName;
        String when;
        List<GpxFileModel> fileModels = fileSetModel.getGpxFileModels();
        for(GpxFileModel fileModel : fileModels) {
            File file = new File(fileModel.getFileName());
            fileName = file.getPath();
            if(VERBOSE) {
                System.out.println(file.getPath());
            }
            if(!file.exists()) {
                Utils.errMsg("File does not exist: " + fileName);
                continue;
            }
            // Create the Folder for this file
            fileFolder = document.createAndAddFolder().withName(file.getName())
                .withOpen(true);

            // Loop over waypoints
            wpt = fileModel.getGpx().getWpt();
            if(wpt.size() > 0) {
                folder = fileFolder.createAndAddFolder().withName("Waypoints")
                    .withOpen(true);
            } else {
                folder = null;
            }
            for(WptType waypoint : wpt) {
                if(VERBOSE) {
                    System.out.println(waypoint.getName());
                }
                if(waypoint.getLat() != null) {
                    lat = waypoint.getLat().doubleValue();
                } else {
                    lat = 0;
                }
                if(waypoint.getLon() != null) {
                    lon = waypoint.getLon().doubleValue();
                } else {
                    lon = 0;
                }
                if(waypoint.getEle() != null) {
                    alt = waypoint.getEle().doubleValue();
                } else {
                    alt = 0;
                }
                // Make a Placemark
                placemark = folder.createAndAddPlacemark()
                    .withName(waypoint.getName())
                    .withStyleUrl("#wpt" + wptColors[nWaypoint % nWptColors]);
                placemark.createAndSetPoint().addToCoordinates(lon, lat, alt);
                nWaypoint++;
            }

            // Loop over tracks
            List<TrkType> tracks;
            List<TrksegType> trackSegments;
            List<WptType> trackPoints;
            tracks = fileModel.getGpx().getTrk();
            boolean useTrackIconFirst;
            boolean useTrackTrackFirst;
            if(tracks.size() > 0) {
                folder = fileFolder.createAndAddFolder().withName("Tracks")
                    .withOpen(true);
            } else {
                folder = null;
            }
            trackPlacemark = null;
            mt = null;
            track = null;
            for(TrkType trk : tracks) {
                if(VERBOSE) {
                    System.out.println(trk.getName());
                }
                // Make a Placemark for the track
                if(kmlOptions.getUseTrkTrack()) {
                    trackPlacemark = folder.createAndAddPlacemark()
                        .withName(trk.getName() + " Track")
                        .withStyleUrl("#trk" + trkColors[nTrack % nTrkColors])
                        .withSnippetd("");
                    mt = trackPlacemark.createAndSetMultiTrack();
                }
                // Make a Placemark with MultiGeometry
                placemark = folder.createAndAddPlacemark()
                    .withName(trk.getName() + " Lines")
                    .withStyleUrl("#trk" + trkColors[nTrack % nTrkColors]);
                // Need MultiGeometry to handle non-connected segments
                mg = placemark.createAndSetMultiGeometry();
                trackSegments = trk.getTrkseg();
                useTrackIconFirst = kmlOptions.getUseTrkIcon() ? true : false;
                useTrackTrackFirst = kmlOptions.getUseTrkTrack() ? true : false;
                for(TrksegType trackSegment : trackSegments) {
                    // Add a LineString to the MultiGeometry
                    ls = mg.createAndAddLineString().withExtrude(false)
                        .withTessellate(true);
                    if(mt != null) {
                        track = mt.createAndAddTrack();
                    }
                    trackPoints = trackSegment.getTrkpt();
                    for(WptType trackPoint : trackPoints) {
                        if(trackPoint.getLat() != null) {
                            lat = trackPoint.getLat().doubleValue();
                        } else {
                            lat = 0;
                        }
                        if(trackPoint.getLon() != null) {
                            lon = trackPoint.getLon().doubleValue();
                        } else {
                            lon = 0;
                        }
                        if(trackPoint.getEle() != null) {
                            alt = trackPoint.getEle().doubleValue();
                        } else {
                            alt = 0;
                        }
                        if(trackPoint.getTime() != null) {
                            when = GpxUtils.getGmtTimeFromXMLGregorianCalendar(
                                trackPoint.getTime());
                        } else {
                            when = null;
                        }
                        // Add coordinates to the LineString
                        ls.addToCoordinates(lon, lat, alt);
                        if(track != null) {
                            // Add coordinates and when to the track
                            track.addToCoord(lon + " " + lat + " " + alt);
                            track.addToWhen(when);
                        }
                        // Make a Placemark with an icon at the first point
                        // on the track
                        if(useTrackIconFirst) {
                            useTrackIconFirst = false;
                            folder.createAndAddPlacemark()
                                .withName(trk.getName())
                                .withVisibility(!kmlOptions.getUseTrkTrack())
                                .withStyleUrl(
                                    "#trk" + trkColors[nTrack % nTrkColors])
                                .createAndSetPoint().addToCoordinates(lon, lat);
                        }
                        if(trackPlacemark != null && useTrackTrackFirst) {
                            useTrackTrackFirst = false;
                            trackPlacemark.setDescription(
                                GpxUtils.getLocalTimeFromXMLGregorianCalendar(
                                    trackPoint.getTime()));
                        }
                    }
                }
                nTrack++;
            }

            // Loop over routes
            List<RteType> routes;
            List<WptType> routePoints;
            routes = fileModel.getGpx().getRte();
            boolean useRteIconFirst;
            if(routes.size() > 0) {
                folder = fileFolder.createAndAddFolder().withName("Routes")
                    .withOpen(true);
            } else {
                folder = null;
            }
            for(RteType route : routes) {
                if(VERBOSE) {
                    System.out.println(route.getName());
                }
                routeFolder = folder.createAndAddFolder()
                    .withName(route.getName()).withOpen(true);
                // Make a Placemark with MultiGeometry
                placemark = routeFolder.createAndAddPlacemark()
                    .withName(route.getName() + " Lines")
                    .withStyleUrl("#rte" + rteColors[nRoute % nRteColors]);
                // Need MultiGeometry to handle non-connected segments
                mg = placemark.createAndSetMultiGeometry();
                routePoints = route.getRtept();
                useRteIconFirst = kmlOptions.getUseRteIcon() ? true : false;
                // Add a LineString to the MultiGeometry
                ls = mg.createAndAddLineString().withExtrude(false)
                    .withTessellate(true);
                for(WptType rtePoint : routePoints) {
                    if(rtePoint.getLat() != null) {
                        lat = rtePoint.getLat().doubleValue();
                    } else {
                        lat = 0;
                    }
                    if(rtePoint.getLon() != null) {
                        lon = rtePoint.getLon().doubleValue();
                    } else {
                        lon = 0;
                    }
                    if(rtePoint.getEle() != null) {
                        alt = rtePoint.getEle().doubleValue();
                    } else {
                        alt = 0;
                    }
                    if(useRteIconFirst) {
                        // Make a Placemark with an Icon at the first point
                        // on the track
                        useRteIconFirst = false;
                        routeFolder.createAndAddPlacemark()
                            .withName(route.getName())
                            .withStyleUrl(
                                "#rte" + rteColors[nRoute % nRteColors])
                            .createAndSetPoint().addToCoordinates(lon, lat);
                    }
                    // Make a Placemark
                    placemark = routeFolder.createAndAddPlacemark()
                        .withName(rtePoint.getName())
                        .withStyleUrl("#rte" + rteColors[nRoute % nRteColors]);
                    placemark.createAndSetPoint().addToCoordinates(lon, lat,
                        alt);
                    ls.addToCoordinates(lon, lat, alt);
                }
            }
            nRoute++;
        }

        // Create the file
        final File outFile;
        String kmlFileName = kmlOptions.getKmlFileName();
        if(kmlFileName == null || kmlFileName.isEmpty()) {
            outFile = File.createTempFile(name, ".kml");
        } else {
            outFile = new File(kmlFileName);
        }
        if(kmlOptions.getPromptToOverwrite() && outFile.exists()) {
            int result = JOptionPane.showConfirmDialog(null,
                "File exists:" + LS + outFile.getPath() + LS
                    + "OK to overwrite?",
                "File Exists", JOptionPane.OK_CANCEL_OPTION);
            if(result != JOptionPane.OK_OPTION) {
                return;
            }
        }
        outFile.createNewFile();

        try {
            kml.marshal(outFile);
            // Send it to Google Earth
            if(kmlOptions.getSendToGoogle()) {
                if(VERBOSE) {
                    System.out.println(
                        "Sending " + outFile.getPath() + " to Google Earth");
                }
                if(Desktop.isDesktopSupported()) {
                    final Desktop dt = Desktop.getDesktop();
                    if(dt.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            dt.open(outFile);
                        } catch(IOException ex) {
                            Utils.excMsg("Could not send to Google Earth", ex);
                        }
                    }
                } else {
                    Utils.errMsg("Could not send to Google Earth\n"
                        + "Desktop is not supported");
                }
            }
        } catch(FileNotFoundException ex) {
            Utils.excMsg("Could not marshal file: " + outFile, ex);
            ex.printStackTrace();
        } catch(Throwable t) {
            Utils.excMsg(
                "Error marshaling KML file: " + kmlOptions.getKmlFileName(), t);
            t.printStackTrace();
        }
    }

    /**
     * Create the trkColors array when the mode is KML_COLOR_MODE_COLOR.
     * 
     * @param kmlOptions
     */
    private static void createTrkColorColors(KmlOptions kmlOptions) {
        // Make a single element with the options track color
        trkColors = new String[1];
        trkColors[0] = kmlOptions.getTrkAlpha() + kmlOptions.getTrkColor();
    }

    /**
     * Create the trkColors array when the mode is KML_COLOR_MODE_COLORSET.
     * 
     * @param kmlOptions
     */
    private static void createTrkColorSetColors(KmlOptions kmlOptions) {
        // Use the hard-coded colorset colors with alpha prepended
        int nColors = colorSetColors.length;
        trkColors = new String[nColors];
        String alpha = kmlOptions.getTrkAlpha();
        // Insure alpha has two characters
        if(alpha.length() == 0) {
            alpha = "00";
        } else if(alpha.length() == 1) {
            alpha = "0" + alpha;
        } else if(alpha.length() > 2) {
            alpha = alpha.substring(0, 2);
        }
        for(int i = 0; i < nColors; i++) {
            trkColors[i] = alpha + colorSetColors[i];
        }
    }

    /**
     * Create the trkColors array when the mode is KML_COLOR_MODE_RAINBOW.
     * 
     * @param kmlOptions
     */
    private static void createTrkRainbowColors(KmlOptions kmlOptions,
        GpxFileSetModel fileSetModel) {
        // Make a color for each track
        int nColors = 0;
        List<GpxFileModel> fileModels = fileSetModel.getGpxFileModels();
        for(GpxFileModel fileModel : fileModels) {
            List<TrkType> tracks = fileModel.getGpx().getTrk();
            for(int i = 0; i < tracks.size(); i++) {
                nColors++;
            }
        }
        trkColors = new String[nColors];

        // Calculate the trkColors
        Color color;
        int red, green, blue;
        String alpha = kmlOptions.getTrkAlpha();
        // Insure alpha has two characters
        if(alpha.length() == 0) {
            alpha = "00";
        } else if(alpha.length() == 1) {
            alpha = "0" + alpha;
        } else if(alpha.length() > 2) {
            alpha = alpha.substring(0, 2);
        }
        for(int i = 0; i < nColors; i++) {
            color = RainbowColorScheme.defineColor(i, nColors);
            red = color.getRed();
            green = color.getGreen();
            blue = color.getBlue();
            trkColors[i] = String.format("%s%02x%02x%02x", alpha, blue, green,
                red);
            // DEBUG
            // System.out.println(trkColors[i]);
        }
        // DEBUG
        // System.out.println("nColors=" + nColors);
    }

    /**
     * Create the rteColors array when the mode is KML_COLOR_MODE_COLOR.
     * 
     * @param kmlOptions
     */
    private static void createRteColorColors(KmlOptions kmlOptions) {
        // Make a single element with the options route color
        rteColors = new String[1];
        rteColors[0] = kmlOptions.getRteAlpha() + kmlOptions.getRteColor();
    }

    /**
     * Create the rteColors array when the mode is KML_COLOR_MODE_COLORSET.
     * 
     * @param kmlOptions
     */
    private static void createRteColorSetColors(KmlOptions kmlOptions) {
        // Use the hard-coded colorset colors with alpha prepended
        int nColors = colorSetColors.length;
        rteColors = new String[nColors];
        String alpha = kmlOptions.getRteAlpha();
        // Insure alpha has two characters
        if(alpha.length() == 0) {
            alpha = "00";
        } else if(alpha.length() == 1) {
            alpha = "0" + alpha;
        } else if(alpha.length() > 2) {
            alpha = alpha.substring(0, 2);
        }
        for(int i = 0; i < nColors; i++) {
            rteColors[i] = alpha + colorSetColors[i];
        }
    }

    /**
     * Create the rteColors array when the mode is KML_COLOR_MODE_RAINBOW.
     * 
     * @param kmlOptions
     */
    private static void createRteRainbowColors(KmlOptions kmlOptions,
        GpxFileSetModel fileSetModel) {
        // Make a color for each route
        int nColors = 0;
        List<GpxFileModel> fileModels = fileSetModel.getGpxFileModels();
        for(GpxFileModel fileModel : fileModels) {
            List<RteType> routes = fileModel.getGpx().getRte();
            for(int i = 0; i < routes.size(); i++) {
                nColors++;
            }
        }
        rteColors = new String[nColors];

        // Calculate the rteColors
        Color color;
        int red, green, blue;
        String alpha = kmlOptions.getRteAlpha();
        // Insure alpha has two characters
        if(alpha.length() == 0) {
            alpha = "00";
        } else if(alpha.length() == 1) {
            alpha = "0" + alpha;
        } else if(alpha.length() > 2) {
            alpha = alpha.substring(0, 2);
        }
        for(int i = 0; i < nColors; i++) {
            color = RainbowColorScheme.defineColor(i, nColors);
            red = color.getRed();
            green = color.getGreen();
            blue = color.getBlue();
            rteColors[i] = String.format("%s%02x%02x%02x", alpha, blue, green,
                red);
            // DEBUG
            // System.out.println(rteColors[i]);
        }
        // DEBUG
        // System.out.println("nColors=" + nColors);
    }

    /**
     * Create the wptColors array when the mode is KML_COLOR_MODE_COLOR.
     * 
     * @param kmlOptions
     */
    private static void createWptColorColors(KmlOptions kmlOptions) {
        // Make a single element with the options waypoint color
        wptColors = new String[1];
        wptColors[0] = kmlOptions.getWptAlpha() + kmlOptions.getWptColor();
    }

    /**
     * Create the wptColors array when the mode is KML_COLOR_MODE_COLORSET.
     * 
     * @param kmlOptions
     */
    private static void createWptColorSetColors(KmlOptions kmlOptions) {
        // Use the hard-coded colorset colors with alpha prepended
        int nColors = colorSetColors.length;
        wptColors = new String[nColors];
        String alpha = kmlOptions.getWptAlpha();
        // Insure alpha has two characters
        if(alpha.length() == 0) {
            alpha = "00";
        } else if(alpha.length() == 1) {
            alpha = "0" + alpha;
        } else if(alpha.length() > 2) {
            alpha = alpha.substring(0, 2);
        }
        for(int i = 0; i < nColors; i++) {
            wptColors[i] = alpha + colorSetColors[i];
        }
    }

    /**
     * Create the wptColors array when the mode is KML_COLOR_MODE_RAINBOW.
     * 
     * @param kmlOptions
     */
    private static void createWptRainbowColors(KmlOptions kmlOptions,
        GpxFileSetModel fileSetModel) {
        // Make a color for each waypoint
        int nColors = 0;
        List<GpxFileModel> fileModels = fileSetModel.getGpxFileModels();
        for(GpxFileModel fileModel : fileModels) {
            List<WptType> waypoints = fileModel.getGpx().getWpt();
            for(int i = 0; i < waypoints.size(); i++) {
                nColors++;
            }
        }
        wptColors = new String[nColors];

        // Calculate the wptColors
        Color color;
        int red, green, blue;
        String alpha = kmlOptions.getWptAlpha();
        // Insure alpha has two characters
        if(alpha.length() == 0) {
            alpha = "00";
        } else if(alpha.length() == 1) {
            alpha = "0" + alpha;
        } else if(alpha.length() > 2) {
            alpha = alpha.substring(0, 2);
        }
        for(int i = 0; i < nColors; i++) {
            color = RainbowColorScheme.defineColor(i, nColors);
            red = color.getRed();
            green = color.getGreen();
            blue = color.getBlue();
            wptColors[i] = String.format("%s%02x%02x%02x", alpha, blue, green,
                red);
            // DEBUG
            // System.out.println(wptColors[i]);
        }
        // DEBUG
        // System.out.println("nColors=" + nColors);
    }

    /**
     * Get the coordinates of a placemark placed in the system clipboard.
     * 
     * @return { latitude, longitude, elevation [meters] } or null on failure.
     */
    public static double[] coordinatesFromClipboardPlacemark() {
        // Get the placemark from the clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor flavor = DataFlavor.stringFlavor;
        Transferable contents = clipboard.getContents(null);
        if(contents == null || !contents.isDataFlavorSupported(flavor)) {
            return null;
        }
        String text = null;
        try {
            text = (String)contents.getTransferData(flavor);
        } catch(UnsupportedFlavorException ex) {
            Utils.excMsg("Clipboard does not contain a placemark", ex);
            return null;
        } catch(IOException ex) {
            Utils.excMsg("Clipboard does not contain a placemark", ex);
            return null;
        }
        if(text == null || text.length() == 0) {
            return null;
        }
        // Parse the coordinates
        double[] coords = {Double.NaN, Double.NaN, Double.NaN};
        String regex = "(<coordinates>.*</coordinates>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()) {
            String match = matcher.group();
            int len = match.length();
            if(match.startsWith("<coordinates>")
                && match.endsWith("</coordinates>")) {
                match = match.substring(13, len - 14);
                String[] tokens = match.split(",");
                for(int i = 0; i < 3; i++) {
                    try {
                        coords[i] = Double.parseDouble(tokens[i]);
                    } catch(NumberFormatException ex) {
                        // Do nothing
                    }
                }
            } else {
                Utils.errMsg(
                    "Could not find coordinates from " + "clipboard placemark");
                return null;
            }
        } else {
            Utils.errMsg("Could not parse placemark from Clipboard. "
                + "May not be a placemark.");
            return null;
        }
        return coords;
    }

    /**
     * Copies a Google Earth Placemark to the system clipboard as well as an
     * optional circle about the Placemark denoting the given radius. The
     * clipboard contents can be pasted into Google Earth.
     * 
     * @param documentName The name of the KML Document or null to not make a
     *            Document. There does not have to be a document if only a
     *            Placemark is used.
     * @param lat The latitude.
     * @param lon The longitude.
     * @param ele The elevation.
     */
    public static void copyPlacemarkToClipboard(String documentName,
        String name, String lat, String lon, String ele) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        text += "<kml xmlns=\"http://www.opengis.net/kml/2.2\" "
            + "xmlns:gx=\"http://www.google.com/kml/ext/2.2\" "
            + "xmlns:kml=\"http://www.opengis.net/kml/2.2\" " + ""
            + "xmlns:atom=\"http://www.w3.org/2005/Atom\" "
            + "xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">\n";
        if(documentName != null) {
            text += "<Document>\n";
            text += "  <name>" + documentName + "</name>\n";
        }

        // Point
        text += "  <Placemark>\n";
        text += "    <name>" + name + "</name>\n";
        text += "    <Point>\n";
        text += "      <coordinates>" + lon + "," + lat + "," + ele
            + "</coordinates>\n";
        text += "    </Point>\n";
        text += "  </Placemark>\n";

        if(documentName != null) {
            text += "</Document>\n";
        }
        text += "</kml>\n";
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, null);
    }

    /**
     * Copies a Google Earth Placemark to the system clipboard with a circle
     * about the coordinates with the given radius. The clipboard contents can
     * be pasted into Google Earth.
     * 
     * @param documentName The name of the KML Document or null to not make a
     *            Document. There does not have to be a document if only a
     *            Placemark is used.
     * @param lat The latitude.
     * @param lon The longitude.
     * @param ele The elevation.
     * @param radius The radius in meters of a circle to be drawn about the
     *            center or Double.NaN to not draw one.
     */
    public static void copyPlacemarkCircleToClipboard(String documentName,
        String name, String lat, String lon, String ele, double radius) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        text += "<kml xmlns=\"http://www.opengis.net/kml/2.2\" "
            + "xmlns:gx=\"http://www.google.com/kml/ext/2.2\" "
            + "xmlns:kml=\"http://www.opengis.net/kml/2.2\" " + ""
            + "xmlns:atom=\"http://www.w3.org/2005/Atom\" "
            + "xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">\n";
        if(documentName != null) {
            text += "<Document>\n";
            text += "  <name>" + documentName + "</name>\n";
        }

        // Circle
        if(radius != Double.NaN && radius > 0) {
            double lat0 = 0, lon0 = 0;
            try {
                lat0 = Double.parseDouble(lat);
            } catch(NumberFormatException ex) {
                // Do nothing
            }
            try {
                lon0 = Double.parseDouble(lon);
            } catch(NumberFormatException ex) {
                // Do nothing
            }
            double rlat = GpxUtils.greatCircleDistance(lat0, lon0,
                lat0 + DELTA_LATLON, lon0);
            double rlon = GpxUtils.greatCircleDistance(lat0, lon0, lat0,
                lon0 + DELTA_LATLON);
            double a = DELTA_LATLON / rlon * radius;
            double b = DELTA_LATLON / rlat * radius;
            if(!Double.isNaN(a) && !Double.isInfinite(a) && !Double.isNaN(b)
                && !Double.isInfinite(b)) {
                text += "  <Placemark>\n";
                text += "    <name>" + name + "</name>\n";
                text += "    <MultiGeometry>\n";
                text += "      <LineString>\n";
                text += "        <coordinates>";
                double delta = 2 * Math.PI / N_CIRCLE_POINTS;
                double lat1, lon1;
                for(int i = 0; i <= N_CIRCLE_POINTS; i++) {
                    lat1 = lat0 + b * Math.sin(i * delta);
                    lon1 = lon0 + a * Math.cos(i * delta);
                    if(i != 0) {
                        text += ",";
                    }
                    text += String.format("%.6f", lon1) + ","
                        + String.format("%.6f", lat1) + ",0";
                }
                text += "</coordinates>\n";
                text += "      </LineString>\n";
                text += "    </MultiGeometry>\n";
            }
            text += "  </Placemark>\n";
        }
        if(documentName != null) {
            text += "</Document>\n";
        }
        text += "</kml>\n";
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, null);
    }

}
