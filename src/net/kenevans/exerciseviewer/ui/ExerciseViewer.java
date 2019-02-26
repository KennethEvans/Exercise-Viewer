package net.kenevans.exerciseviewer.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.ui.RefineryUtilities;

import net.kenevans.core.utils.AboutBoxPanel;
import net.kenevans.core.utils.ImageUtils;
import net.kenevans.core.utils.Utils;
import net.kenevans.exerciseviewer.model.GpxFileModel;
import net.kenevans.exerciseviewer.model.IConstants;
import net.kenevans.exerciseviewer.preferences.FileLocations;
import net.kenevans.exerciseviewer.preferences.FileLocations.FileLocation;
import net.kenevans.exerciseviewer.preferences.FileLocations.FilterMode;
import net.kenevans.exerciseviewer.preferences.PreferencesDialog;
import net.kenevans.exerciseviewer.preferences.Settings;

/**
 * ExerciseViewer is a viewer to view ECG fileNames from the MD100A ECG Monitor.
 * 
 * @author Kenneth Evans, Jr.
 */
public class ExerciseViewer extends JFrame implements IConstants
{
    private static final String AUTHOR = "Written by Kenneth Evans, Jr.";
    private static final String COPYRIGHT = "Copyright (c) 2014-2019 Kenneth Evans";
    private static final String COMPANY = "kenevans.net";

    /**
     * Use this to determine if a file is loaded initially. Useful for
     * development. Not so good for deployment.
     */
    private static final long serialVersionUID = 1L;
    public static final String LS = System.getProperty("line.separator");

    private Settings settings;
    private PreferencesDialog preferencesDialog;;
    /** Keeps the last-used path for the file open dialog. */
    public String defaultOpenPath;
    /** Keeps the last-used path for the file save dialog. */
    public String defaultSavePath;

    /** The model for this user interface. */
    private GpxFileModel model;

    /** The dataPlot for this user interface. */
    private DataPlot dataPlot;

    // User interface controls (Many do not need to be global)
    private Container contentPane = this.getContentPane();
    private JPanel listPanel = new JPanel();
    private JPanel lowerPanel = new JPanel();
    private DefaultListModel<String> listModel = new DefaultListModel<String>();
    private JList<String> list = new JList<String>(listModel);
    private JScrollPane listScrollPane;
    private JTextArea infoTextArea;
    private JPanel displayPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        displayPanel, lowerPanel);
    private JMenuBar menuBar;

    /** Array of file names for the viewer. */
    public String[] fileNames = {};
    /** The currently selected file name. */
    private String curFileName;

    /**
     * ExerciseViewer constructor.
     */
    public ExerciseViewer() {
        loadUserPreferences();
        dataPlot = new DataPlot(this);
        uiInit();
        findFileNames(settings.getFileLocations());
    }

    /**
     * Fills the fileNames array with the files in the given FileLocations.
     * 
     * @param fileLocations
     */
    void findFileNames(FileLocations fileLocations) {
        if(fileLocations == null) {
            return;
        }
        if(fileLocations.getFileLocations().isEmpty()) {
            Utils.errMsg("There are no file locations");
            return;
        }
        String dirName = null;
        List<File> fileList = new ArrayList<>();
        for(FileLocation fileLocation : fileLocations.getFileLocations()) {
            dirName = fileLocation.fileName;
            File dir = new File(dirName);
            if(!dir.exists()) {
                Utils.errMsg("Does not exist: " + dirName);
                return;
            }
            if(!dir.isDirectory()) {
                Utils.errMsg("Not a directory: " + dirName);
                return;
            }
            File[] files = dir.listFiles(new java.io.FileFilter() {
                public boolean accept(File file) {
                    if(file.isDirectory()) {
                        return false;
                    }
                    String ext = Utils.getExtension(file);
                    if(ext == null) {
                        return false;
                    }
                    return ext.toLowerCase().equals("gpx");
                }
            });
            for(File file : files) {
                fileList.add(file);
            }
        }

        // Sort in reverse order
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File fa, File fb) {
                if(fa.isDirectory() && !fb.isDirectory()) return -1;
                if(fb.isDirectory() && !fa.isDirectory()) return 1;
                String faName = fa.getName();
                String fbName = fb.getName();
                String regex = "(\\d\\d\\d\\d-\\d\\d-\\d\\d)";
                int index;
                boolean faFind=false, fbFind=false;
                Matcher matcher = Pattern.compile(regex).matcher(faName);
                if(matcher.find()) {
                    index = faName.indexOf(matcher.group(1));
                    if(index != -1) {
                        faFind = true;
                        faName = faName.substring(index, faName.length());
                    }
                    // System.out.println("fa: " + matcher.group(1) + " " +
                    // faName);
                }
                matcher = Pattern.compile(regex).matcher(fbName);
                if(matcher.find()) {
                    index = fbName.indexOf(matcher.group(1));
                    if(index != -1) {
                        fbFind = true;
                        fbName = fbName.substring(index, fbName.length());
                    }
                    // System.out.println("fb: " + matcher.group(1) + " " +
                    // fbName);
                }
                if(faFind && !fbFind) {
                    return -1;
                }
                if(!faFind && fbFind) {
                    return 1;
                }
                return (fbName.compareTo(faName));
            }
        });

        // Make the list of file names
        int nFiles = fileList.size();
        fileNames = new String[nFiles];
        if(nFiles <= 0) {
            curFileName = null;
        } else {
            for(int i = 0; i < nFiles; i++) {
                fileNames[i] = fileList.get(i).getPath();
            }
            curFileName = fileNames[0];
        }

        // Fill in the ListModel
        populateList();
    }

    /**
     * Initializes the user interface.
     */
    void uiInit() {
        this.setLayout(new BorderLayout());

        // Chart panel
        displayPanel.setLayout(new BorderLayout());
        dataPlot.createChart();
        // Not necessary to set this, it will adjust per the divider location
        // and frame size
        // dataPlot.getChartPanel().setPreferredSize(
        // new Dimension(CHART_WIDTH, CHART_HEIGHT));
        dataPlot.getChartPanel().setDomainZoomable(true);
        dataPlot.getChartPanel().setRangeZoomable(true);
        javax.swing.border.CompoundBorder compoundborder = BorderFactory
            .createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createEtchedBorder());
        dataPlot.getChartPanel().setBorder(compoundborder);
        displayPanel.add(dataPlot.getChartPanel());

        // List panel
        listScrollPane = new JScrollPane(list);
        listPanel.setLayout(new BorderLayout());
        listPanel.add(listScrollPane, BorderLayout.CENTER);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent ev) {
                // Internal implementation
                onListItemSelected(ev);
            }
        });

        list.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getListCellRendererComponent(JList<?> list,
                Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);
                String fileName = (String)value;
                label.setText(fileName);
                return label;
            }
        });

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());

        // Info text area
        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setColumns(40);
        JScrollPane infoScrollPane = new JScrollPane(infoTextArea);
        infoPanel.add(infoScrollPane, BorderLayout.CENTER);

        // Lower split pane
        JSplitPane lowerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            listPanel, infoPanel);
        lowerPane.setContinuousLayout(true);
        lowerPane.setDividerLocation(LOWER_PANE_DIVIDER_LOCATION);

        // Main split pane
        mainPane.setContinuousLayout(true);
        mainPane.setDividerLocation(MAIN_PANE_DIVIDER_LOCATION);
        if(false) {
            mainPane.setOneTouchExpandable(true);
        }

        // Lower panel
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.add(lowerPane, BorderLayout.CENTER);

        // Main panel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mainPane, BorderLayout.CENTER);

        // Content pane
        contentPane.setLayout(new BorderLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the menus.
     */
    private void initMenus() {
        // Menu
        menuBar = new JMenuBar();

        // File
        JMenu menu = new JMenu();
        menu.setText("File");
        menuBar.add(menu);

        // File Open
        JMenuItem menuItem = new JMenuItem();
        menuItem.setText("Open...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                open();
            }
        });
        menu.add(menuItem);

        // Set directory
        menuItem = new JMenuItem();
        menuItem.setText("Set Default Directory...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openDirectory();
            }
        });
        menu.add(menuItem);

        // Refresh
        menuItem = new JMenuItem();
        menuItem.setText("Refresh");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                refresh();
            }
        });
        menu.add(menuItem);

        JSeparator separator = new JSeparator();
        menu.add(separator);

        // File Exit
        menuItem = new JMenuItem();
        menuItem.setText("Exit");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                quit();
            }
        });
        menu.add(menuItem);

        // Tools
        menu = new JMenu();
        menu.setText("Tools");
        menuBar.add(menu);

        separator = new JSeparator();
        menu.add(separator);

        menuItem = new JMenuItem();
        menuItem.setText("File Info...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                showInfo();
            }
        });
        menu.add(menuItem);

        separator = new JSeparator();
        menu.add(separator);

        menuItem = new JMenuItem();
        menuItem.setText("Preferences...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setPreferences();
            }
        });
        menu.add(menuItem);

        // Help
        menu = new JMenu();
        menu.setText("Help");
        menuBar.add(menu);

        menuItem = new JMenuItem();
        menuItem.setText("About");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(
                    null, new AboutBoxPanel(TITLE + " " + VERSION, AUTHOR,
                        COMPANY, COPYRIGHT),
                    "About", JOptionPane.PLAIN_MESSAGE);
            }
        });
        menu.add(menuItem);
    }

    /**
     * Puts the panel in a JFrame and runs the JFrame.
     */
    public void run() {
        try {
            // Create and set up the window.
            // JFrame.setDefaultLookAndFeelDecorated(true);
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.setTitle(TITLE);
            // USE EXIT_ON_CLOSE not DISPOSE_ON_CLOSE to close any modeless
            // dialogs
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // frame.setLocationRelativeTo(null);

            // Set the icon
            ImageUtils.setIconImageFromResource(this,
                "/resources/ExerciseViewer32x32.png");

            // Has to be done here. The menus are not part of the JPanel.
            initMenus();
            this.setJMenuBar(menuBar);

            // Display the window
            this.setBounds(20, 20, FRAME_WIDTH, FRAME_HEIGHT);
            RefineryUtilities.centerFrameOnScreen(this);
            this.setVisible(true);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Brings up a JFileChooser to open a file.
     */
    private void open() {
        JFileChooser chooser = new JFileChooser();
        if(defaultOpenPath != null) {
            chooser.setCurrentDirectory(new File(defaultOpenPath));
        }
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            // Save the selected path for next time
            defaultOpenPath = chooser.getSelectedFile().getParentFile()
                .getPath();
            // Process the file
            File file = chooser.getSelectedFile();
            loadFile(file);
        }
    }

    /**
     * Brings up a JFileChooser to open a directory.
     */
    private void openDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(defaultOpenPath != null) {
            FileLocations fileLocations = settings.getFileLocations();
            if(fileLocations.getFileLocations().size() > 0) {
                File dir = new File(
                    fileLocations.getFileLocations().get(0).fileName);
                chooser.setCurrentDirectory(dir);
                chooser.setSelectedFile(dir);
            }
        }
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            // Process the directory
            String dirName = chooser.getSelectedFile().getPath();
            File dir = new File(dirName);
            if(!dir.exists()) {
                Utils.errMsg("Does not exist: " + dirName);
                return;
            }
            if(!dir.isDirectory()) {
                Utils.errMsg("Not a directory: " + dirName);
                return;
            }
            FileLocations newLocations = new FileLocations();
            // TODO Prompt for which FilterMode
            newLocations.getFileLocations()
                .add(new FileLocation(dir.getPath(), FilterMode.GPXTCX));
            settings.setFileLocations(newLocations);
            findFileNames(settings.getFileLocations());
        }
    }

    /**
     * Refreshes the loaded directory.
     */
    private void refresh() {
        findFileNames(settings.getFileLocations());
    }

    /**
     * Loads a new file.
     * 
     * @param fileName
     */
    private void loadFile(final File file) {
        if(file == null) {
            Utils.errMsg("File is null");
            return;
        }

        // Needs to be done this way to allow the text to change before reading
        // the image.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Cursor oldCursor = getCursor();
                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    model = new GpxFileModel(file.getPath());
                    dataPlot.clearPlot();
                    dataPlot.addModelToChart(model);
                    updateInfoText(model);
                } catch(Exception ex) {
                    String msg = "Error loading file: " + file.getPath();
                    Utils.excMsg(msg, ex);
                } catch(Error err) {
                    String msg = "Error loading file: " + file.getPath();
                    Utils.excMsg(msg, err);
                } finally {
                    setCursor(oldCursor);
                }
            }
        });
    }

    /**
     * Populates the list from the list of profiles.
     */
    private void populateList() {
        list.setEnabled(false);
        listModel.removeAllElements();
        for(String fileName : fileNames) {
            listModel.addElement(fileName);
        }
        list.validate();
        mainPane.validate();
        list.setEnabled(true);
    }

    /**
     * Handler for the list. Toggles the checked state.
     * 
     * @param ev
     */
    private void onListItemSelected(ListSelectionEvent ev) {
        if(ev.getValueIsAdjusting()) return;
        String fileName = (String)list.getSelectedValue();
        // DEBUG
        // System.out.println("onListItemSelected" + " fileName=" + fileName);
        if(fileName == null) {
            // Don't put an error message here. Gets called with null after
            // clear selection.
            return;
        }
        File file = new File(fileName);
        if(!file.exists()) {
            Utils.errMsg("Does not exist: " + fileName);
            return;
        }
        curFileName = fileName;

        list.clearSelection();
        loadFile(file);
    }

    /**
     * Shows model information.
     */
    private void showInfo() {
        if(model != null) {
            String info = model.getInfo();
            scrolledTextMsg(null, info, "File Info", 600, 400);
        }
    }

    /**
     * Set viewer fields from the user preferences.
     */
    public void loadUserPreferences() {
        settings = new Settings();
        settings.loadFromPreferences();
    }

    /**
     * Brings up a dialog to set preferences.
     */
    private void setPreferences() {
        if(preferencesDialog == null) {
            preferencesDialog = new PreferencesDialog(this, this);
        }
        // For modal, use this and dialog.showDialog() instead of
        // dialog.setVisible(true)
        // dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        preferencesDialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        URL url = ExerciseViewer.class
            .getResource("/resources/ExerciseViewer32x32.png");
        if(url != null) {
            preferencesDialog.setIconImage(new ImageIcon(url).getImage());
        }
        preferencesDialog.setVisible(true);
        // This only returns on Cancel and always returns true. All actions are
        // done from the dialog.
        // dialog.showDialog();
    }

    /**
     * Copies the given settings to settings and resets the viewer.
     * 
     * @param settings
     */
    public void onPreferenceReset(Settings settings) {
        // Save old values
        FileLocations fileLocationsOld = this.settings.getFileLocations();

        // Copy from the given settings.
        this.settings.copyFrom(settings);
        dataPlot.reset();
        // Check if the file locations have changed
        boolean changed = false;
        if(fileLocationsOld.getFileLocations().size() != this.settings
            .getFileLocations().getFileLocations().size()) {
            changed = true;
        } else {
            List<FileLocation> oldLocations = fileLocationsOld
                .getFileLocations();
            List<FileLocation> newLocations = this.settings.getFileLocations()
                .getFileLocations();
            for(int i = 0; i < oldLocations.size(); i++) {
                if(!oldLocations.get(i).fileName.equals(newLocations.get(i))) {
                    changed = true;
                    break;
                }
                if(oldLocations.get(i).filterMode != newLocations
                    .get(i).filterMode) {
                    changed = true;
                    break;
                }
            }
        }
        if(changed) {
            findFileNames(settings.getFileLocations());
        }
    }

    /**
     * Displays a scrolled text dialog with the given message.
     * 
     * @param message
     */
    public static void scrolledTextMsg(Frame parent, String message,
        String title, int width, int height) {
        final JDialog dialog = new JDialog(parent);

        // Message
        JPanel jPanel = new JPanel();
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        jPanel.add(scrollPane, BorderLayout.CENTER);
        dialog.getContentPane().add(scrollPane);

        // Close button
        jPanel = new JPanel();
        JButton button = new JButton("OK");
        jPanel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }

        });
        dialog.getContentPane().add(jPanel, BorderLayout.SOUTH);

        // Settings
        dialog.setTitle(title);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(width, height);
        // Has to be done after set size
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Updates the info text area.
     * 
     * @param model
     */
    public void updateInfoText(GpxFileModel model) {
        String info = "";
        if(model != null) {
            info += model.getInfo() + LS;
        }
        infoTextArea.setText(info);
        infoTextArea.setCaretPosition(0);
    }

    /**
     * Quits the application
     */
    private void quit() {
        System.exit(0);
    }

    /**
     * @return The value of curFileName.
     */
    public String getCurFleName() {
        return curFileName;
    }

    /**
     * @return The value of model.
     */
    public GpxFileModel getModel() {
        return model;
    }

    /**
     * Returns the user preference store for the viewer.
     * 
     * @return
     */
    public static Preferences getUserPreferences() {
        return Preferences.userRoot().node(P_PREFERENCE_NODE);
    }

    /**
     * @return The value of settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @param settings The new value for settings.
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * Main method.
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
                    ExerciseViewer app = new ExerciseViewer();
                    app.run();
                }
            });
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

}
