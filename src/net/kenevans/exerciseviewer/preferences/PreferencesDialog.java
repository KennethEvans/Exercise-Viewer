package net.kenevans.exerciseviewer.preferences;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.kenevans.core.utils.Utils;
import net.kenevans.exerciseviewer.model.IConstants;
import net.kenevans.exerciseviewer.preferences.FileLocations.FileLocation;
import net.kenevans.exerciseviewer.preferences.FileLocations.FilterMode;
import net.kenevans.exerciseviewer.ui.ExerciseViewer;

/**
 * PreferencesDialog is a dialog to set the Preferences for ExerciseViewer. It only
 * returns after Done. It can save the values to the preference store or set
 * them in the viewer. In either case it remains visible.
 * 
 * @author Kenneth Evans, Jr.
 */
/**
 * PreferencesDialog
 * 
 * @author Kenneth Evans, Jr.
 */
public class PreferencesDialog extends JDialog implements IConstants
{
    private static final long serialVersionUID = 1L;
    private ExerciseViewer viewer;
    /**
     * The return value. It is always true.
     */
    private boolean ok = true;

    private FileLocations fileLocations;
    private String initialFileDir;

    JList<String> fileLocationsList;
    DefaultListModel<String> fileLocationsModel;

    JCheckBox hrVisibileCheck;
    JCheckBox hrZonesVisibileCheck;
    JCheckBox speedVisibileCheck;
    JCheckBox eleVisibileCheck;
    JTextField hrRavCountText;
    JTextField speedRavCountText;
    JTextField eleRavCountText;

    JTextField hrMaxText;
    JTextField hrMinText;
    JTextField speedMaxText;
    JTextField speedMinText;
    JTextField eleMaxText;
    JTextField eleMinText;

    JTextField zone1ValText;
    JTextField zone2ValText;
    JTextField zone3ValText;
    JTextField zone4ValText;
    JTextField zone5ValText;
    JTextField zone6ValText;

    JTextField zone1ColorText;
    JTextField zone2ColorText;
    JTextField zone3ColorText;
    JTextField zone4ColorText;
    JTextField zone5ColorText;
    JTextField zone6ColorText;

    JTextField maxHrText;
    JTextField restHrText;
    JTextField ageText;
    JCheckBox useKorvonenCheck;

    /**
     * Constructor
     */
    public PreferencesDialog(JFrame parent, ExerciseViewer viewer) {
        super(parent);
        this.viewer = viewer;
        if(viewer == null) {
            Utils.errMsg("Viewer is null");
            return;
        }
        init();
        Settings settings = new Settings();
        settings.loadFromPreferences();
        setValues(settings);
        // Locate it on the screen
        this.setLocationRelativeTo(parent);
    }

    /**
     * This method initializes this dialog
     * 
     * @return void
     */
    private void init() {
        this.setTitle("Preferences");
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbcDefault = new GridBagConstraints();
        gbcDefault.insets = new Insets(2, 2, 2, 2);
        gbcDefault.anchor = GridBagConstraints.WEST;
        gbcDefault.fill = GridBagConstraints.NONE;
        GridBagConstraints gbc = null;
        int gridy = -1;
        int gridPanel = -1;

        JLabel label;
        JButton button;
        JPanel panel;

        // File Group //////////////////////////////////////////////////////
        JPanel fileGroup = new JPanel();
        fileGroup.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("File"),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        gridy++;
        fileGroup.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        contentPane.add(fileGroup, gbc);

        gridPanel++;
        label = new JLabel("File Locations:");
        label.setToolTipText("Directories to search for GPX and/or TCX files.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = gridPanel;
        fileGroup.add(label, gbc);

        fileLocationsList = new JList<String>();
        fileLocationsList.setToolTipText(label.getText());
        fileLocationsList.setVisibleRowCount(4);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane scrollPane = new JScrollPane(fileLocationsList);
        fileGroup.add(scrollPane, gbc);

        // Create a JPanel for the rest
        gridPanel++;
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = gridPanel;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        fileGroup.add(panel, gbc);

        // Add
        button = new JButton("Add");
        button.setToolTipText("Add a new filter:path file location.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                add();
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.weightx = 100;
        panel.add(button, gbc);

        // Remove
        button = new JButton("Remove");
        button.setToolTipText("Remove the selected file location.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                delete();
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.weightx = 100;
        panel.add(button, gbc);

        // Edit
        button = new JButton("Edit");
        button.setToolTipText("Edit the selected file location.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                edit();
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        gbc.weightx = 100;
        panel.add(button, gbc);

        // HR Group /////////////////////////////////////////////////////////
        JPanel hrGroup = new JPanel();
        hrGroup.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Heart Rate"),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        gridy++;
        hrGroup.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        contentPane.add(hrGroup, gbc);

        // Visible
        hrVisibileCheck = new JCheckBox("Visible");
        hrVisibileCheck.setToolTipText("Whether HR data is visible.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        hrGroup.add(hrVisibileCheck, gbc);

        // HR Min
        String toolTip = "Minimum HR value or blank for none.";
        label = new JLabel("Min:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        hrGroup.add(label, gbc);

        hrMinText = new JTextField(5);
        hrMinText.setToolTipText(label.getText());
        hrMinText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        hrGroup.add(hrMinText, gbc);

        // HR Max
        toolTip = "Maximum HR value or blank for none.";
        label = new JLabel("Max:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        hrGroup.add(label, gbc);

        hrMaxText = new JTextField(5);
        hrMaxText.setToolTipText(label.getText());
        hrMaxText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        hrGroup.add(hrMaxText, gbc);

        // Running average
        toolTip = "Number of data points to average over.  "
            + "0->Don't average.  " + "Negative->Omit raw values.";
        label = new JLabel("Average Over:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 5;
        hrGroup.add(label, gbc);

        hrRavCountText = new JTextField(5);
        hrRavCountText.setToolTipText(label.getText());
        hrRavCountText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 6;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        // gbc.weightx = 100;
        hrGroup.add(hrRavCountText, gbc);

        // Zones visible
        hrZonesVisibileCheck = new JCheckBox("Zones Visible");
        hrZonesVisibileCheck.setToolTipText("Whether HR zones are visible.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 7;
        gbc.weightx = 100;
        hrGroup.add(hrZonesVisibileCheck, gbc);

        // Speed Group //////////////////////////////////////////////////////
        JPanel speedGroup = new JPanel();
        speedGroup.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Speed"),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        gridy++;
        speedGroup.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        contentPane.add(speedGroup, gbc);

        // Visible
        speedVisibileCheck = new JCheckBox("Visible");
        speedVisibileCheck.setToolTipText("Whether speed data is visible.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        speedGroup.add(speedVisibileCheck, gbc);

        // Speed Min
        toolTip = "Minimum speed value or blank for none.";
        label = new JLabel("Min:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        speedGroup.add(label, gbc);

        speedMinText = new JTextField(5);
        speedMinText.setToolTipText(label.getText());
        speedMinText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        speedGroup.add(speedMinText, gbc);

        // Speed Max
        toolTip = "Maximum speed value or blank for none.";
        label = new JLabel("Max:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        speedGroup.add(label, gbc);

        speedMaxText = new JTextField(5);
        speedMaxText.setToolTipText(label.getText());
        speedMaxText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        speedGroup.add(speedMaxText, gbc);

        // Running average
        toolTip = "Number of data points to average over.  "
            + "0->Don't average.  " + "Negative->Omit raw values.";
        label = new JLabel("Average Over:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 5;
        speedGroup.add(label, gbc);

        speedRavCountText = new JTextField(5);
        speedRavCountText.setToolTipText(label.getText());
        speedRavCountText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 6;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        speedGroup.add(speedRavCountText, gbc);

        // Elevation Group //////////////////////////////////////////////////
        JPanel eleGroup = new JPanel();
        eleGroup.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Elevation"),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        gridy++;
        eleGroup.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        contentPane.add(eleGroup, gbc);

        // Visible
        eleVisibileCheck = new JCheckBox("Visible");
        eleVisibileCheck.setToolTipText("Whether elevation data is visible.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        eleGroup.add(eleVisibileCheck, gbc);

        // Elevation Min
        toolTip = "Minimum elevation value or blank for none.";
        label = new JLabel("Min:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        eleGroup.add(label, gbc);

        eleMinText = new JTextField(5);
        eleMinText.setToolTipText(label.getText());
        eleMinText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        eleGroup.add(eleMinText, gbc);

        // Elevation Max
        toolTip = "Maximum elevation value or blank for none.";
        label = new JLabel("Max:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        eleGroup.add(label, gbc);

        eleMaxText = new JTextField(5);
        eleMaxText.setToolTipText(label.getText());
        eleMaxText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        eleGroup.add(eleMaxText, gbc);

        // Running average
        toolTip = "Number of data points to average over.  "
            + "0->Don't average.  " + "Negative->Omit raw values.";
        label = new JLabel("Average Over:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 5;
        eleGroup.add(label, gbc);

        eleRavCountText = new JTextField(5);
        eleRavCountText.setToolTipText(label.getText());
        eleRavCountText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 6;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        eleGroup.add(eleRavCountText, gbc);

        // Zone Group ///////////////////////////////////////////////////////
        JPanel zoneGroup = new JPanel();
        zoneGroup.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Zones"),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        gridy++;
        zoneGroup.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        contentPane.add(zoneGroup, gbc);
        int zoneGridy = -1;

        // Zone 1
        zoneGridy++;
        toolTip = "Value for zone 1";
        label = new JLabel("Zone 1 Value:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone1ValText = new JTextField(5);
        zone1ValText.setToolTipText(label.getText());
        zone1ValText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridy = zoneGridy;
        gbc.gridx = 1;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone1ValText, gbc);

        toolTip = "Color for zone 1";
        label = new JLabel("Zone 1 Color:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone1ColorText = new JTextField(10);
        zone1ColorText.setToolTipText(label.getText());
        zone1ColorText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone1ColorText, gbc);

        button = new JButton();
        button.setText("Pick Color");
        button.setToolTipText("Pick the zone color.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                chooseZoneColor(ev, zone1ColorText);
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        gbc.gridy = zoneGridy;
        zoneGroup.add(button, gbc);

        // Zone 2
        zoneGridy++;
        toolTip = "Value for zone 2";
        label = new JLabel("Zone 2 Value:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone2ValText = new JTextField(5);
        zone2ValText.setToolTipText(label.getText());
        zone2ValText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone2ValText, gbc);

        toolTip = "Color for zone 2";
        label = new JLabel("Zone 2 Color:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone2ColorText = new JTextField(10);
        zone2ColorText.setToolTipText(label.getText());
        zone2ColorText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone2ColorText, gbc);

        button = new JButton();
        button.setText("Pick Color");
        button.setToolTipText("Pick the zone color.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                chooseZoneColor(ev, zone2ColorText);
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        gbc.gridy = zoneGridy;
        zoneGroup.add(button, gbc);

        // Zone 3
        zoneGridy++;
        toolTip = "Value for zone 3";
        label = new JLabel("Zone 3 Value:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone3ValText = new JTextField(5);
        zone3ValText.setToolTipText(label.getText());
        zone3ValText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone3ValText, gbc);

        toolTip = "Color for zone 3";
        label = new JLabel("Zone 3 Color:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone3ColorText = new JTextField(10);
        zone3ColorText.setToolTipText(label.getText());
        zone3ColorText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone3ColorText, gbc);

        button = new JButton();
        button.setText("Pick Color");
        button.setToolTipText("Pick the zone color.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                chooseZoneColor(ev, zone3ColorText);
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        gbc.gridy = zoneGridy;
        zoneGroup.add(button, gbc);

        // Zone 4
        zoneGridy++;
        toolTip = "Value for zone 4";
        label = new JLabel("Zone 4 Value:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone4ValText = new JTextField(5);
        zone4ValText.setToolTipText(label.getText());
        zone4ValText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone4ValText, gbc);

        toolTip = "Color for zone 4";
        label = new JLabel("Zone 4 Color:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone4ColorText = new JTextField(10);
        zone4ColorText.setToolTipText(label.getText());
        zone4ColorText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone4ColorText, gbc);

        button = new JButton();
        button.setText("Pick Color");
        button.setToolTipText("Pick the zone color.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                chooseZoneColor(ev, zone4ColorText);
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        gbc.gridy = zoneGridy;
        zoneGroup.add(button, gbc);

        // Zone 5
        zoneGridy++;
        toolTip = "Value for zone 5";
        label = new JLabel("Zone 5 Value:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone5ValText = new JTextField(5);
        zone5ValText.setToolTipText(label.getText());
        zone5ValText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone5ValText, gbc);

        toolTip = "Color for zone 5";
        label = new JLabel("Zone 5 Color:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone5ColorText = new JTextField(10);
        zone5ColorText.setToolTipText(label.getText());
        zone5ColorText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone5ColorText, gbc);

        button = new JButton();
        button.setText("Pick Color");
        button.setToolTipText("Pick the zone color.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                chooseZoneColor(ev, zone5ColorText);
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        gbc.gridy = zoneGridy;
        zoneGroup.add(button, gbc);

        // Zone 6
        zoneGridy++;
        toolTip = "Value for zone 6";
        label = new JLabel("Zone 6 Value:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone6ValText = new JTextField(5);
        zone6ValText.setToolTipText(label.getText());
        zone6ValText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone6ValText, gbc);

        toolTip = "Color for zone 6";
        label = new JLabel("Zone 6 Color:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        zone6ColorText = new JTextField(10);
        zone6ColorText.setToolTipText(label.getText());
        zone6ColorText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(zone6ColorText, gbc);

        button = new JButton();
        button.setText("Pick Color");
        button.setToolTipText("Pick the zone color.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                chooseZoneColor(ev, zone6ColorText);
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        gbc.gridy = zoneGridy;
        zoneGroup.add(button, gbc);

        // Max HR
        zoneGridy++;
        toolTip = "Maximum HR";
        label = new JLabel("Max HR:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        maxHrText = new JTextField(5);
        maxHrText.setToolTipText(label.getText());
        maxHrText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(maxHrText, gbc);

        toolTip = "Age";
        label = new JLabel("Age:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        ageText = new JTextField(5);
        ageText.setToolTipText(label.getText());
        ageText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 3;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(ageText, gbc);

        button = new JButton();
        button.setText("Calculate Max HR");
        button.setToolTipText("Calculate the max HR for this age.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                calculateMaxHr();
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        gbc.gridy = zoneGridy;
        zoneGroup.add(button, gbc);

        // Rest HR
        zoneGridy++;
        toolTip = "Resting HR";
        label = new JLabel("Resting HR:");
        label.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = zoneGridy;
        zoneGroup.add(label, gbc);

        restHrText = new JTextField(5);
        restHrText.setToolTipText(label.getText());
        restHrText.setToolTipText(toolTip);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = zoneGridy;
        gbc.weightx = 100;
        zoneGroup.add(restHrText, gbc);

        // Use Korvonen
        useKorvonenCheck = new JCheckBox("Use Korvonen");
        useKorvonenCheck.setToolTipText("Whether to use the Korvonen formula.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        hrGroup.add(useKorvonenCheck, gbc);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 2;
        gbc.gridy = zoneGridy;
        zoneGroup.add(useKorvonenCheck, gbc);

        button = new JButton();
        button.setText("Calculate Zone Values");
        button.setToolTipText("Calculate the zone values for this age.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                calculateZoneValues();
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 4;
        gbc.gridy = zoneGridy;
        zoneGroup.add(button, gbc);

        // // Dummy Group
        // JPanel dummyGroup = new JPanel();
        // dummyGroup.setBorder(BorderFactory.createCompoundBorder(
        // BorderFactory.createTitledBorder("Dummy"),
        // BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        // gridy++;
        // dummyGroup.setLayout(new GridBagLayout());
        // gbc = (GridBagConstraints)gbcDefault.clone();
        // gbc.gridx = 0;
        // gbc.gridy = gridy;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        // gbc.weightx = 100;
        // contentPane.add(dummyGroup, gbc);
        //
        // // Dummy
        // label = new JLabel("Dummy:");
        // label.setToolTipText("Dummy.");
        // gbc = (GridBagConstraints)gbcDefault.clone();
        // gbc.gridx = 0;
        // dummyGroup.add(label, gbc);
        //
        // JTextField dummyText = new JTextField(30);
        // dummyText.setToolTipText(label.getText());
        // gbc = (GridBagConstraints)gbcDefault.clone();
        // gbc.gridx = 1;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        // gbc.weightx = 100;
        // dummyGroup.add(dummyText, gbc);

        // Button panel /////////////////////////////////////////////////////
        gridy++;
        JPanel buttonPanel = new JPanel();
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridy = gridy;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(buttonPanel, gbc);

        button = new JButton();
        button.setText("Use Current");
        button.setToolTipText("Set to the current viewer values.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                Settings settings = viewer.getSettings();
                if(settings == null) {
                    Utils.errMsg("Settings in the viewer do not exist");
                    return;
                }
                setValues(settings);
            }
        });
        buttonPanel.add(button);

        button = new JButton();
        button.setText("Use Defaults");
        button.setToolTipText("Set to the ExerciseViewer default values.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                Settings settings = new Settings();
                if(settings == null) {
                    Utils.errMsg("Default settings do not exist");
                    return;
                }
                setValues(settings);
            }
        });
        buttonPanel.add(button);

        button = new JButton();
        button.setText("Use Stored");
        button.setToolTipText("Reset to the current stored preferences.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                Settings settings = new Settings();
                settings.loadFromPreferences();
                if(settings == null) {
                    Utils.errMsg("Cannot load preferences");
                    return;
                }
                setValues(settings);
            }
        });
        buttonPanel.add(button);

        button = new JButton();
        button.setText("Save");
        button.setToolTipText("Save the changes as preferences.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                save();
            }
        });
        buttonPanel.add(button);

        button = new JButton();
        button.setText("Set Current");
        button.setToolTipText("Set the current values in the viewer.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                setToViewer();
            }
        });
        buttonPanel.add(button);

        button = new JButton();
        button.setText("Done");
        button.setToolTipText("Close the dialog and do nothing.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                PreferencesDialog.this.setVisible(false);
            }
        });
        buttonPanel.add(button);

        pack();
    }

    /**
     * Brings up a JColorChooser to get a new color starting with the color in
     * the colorText and sets the new color in the colorText unless cancelled.
     * 
     * @param ev Not used
     * @param colorText
     */
    void chooseZoneColor(java.awt.event.ActionEvent ev, JTextField colorText) {
        Color initialColor = Color.BLACK;
        try {
            initialColor = Color.decode(colorText.getText());
        } catch(NumberFormatException ex) {
            Utils.excMsg("Cannot parse color", ex);
            return;
        }
        Color newColor = JColorChooser.showDialog(PreferencesDialog.this,
            "Choose Zone Color", initialColor);
        if(newColor != null) {
            int rgb = newColor.getRGB() & 0x00FFFFFF;
            String colorString = String.format("0x%06X", rgb);
            colorText.setText(colorString);
        }
    }

    /**
     * Calculates the max HR from the age using teh formula by Tanaka, Monahan,
     * & Seals and sets the max HR.
     */
    void calculateMaxHr() {
        int maxHr = 0, age = 0;
        try {
            age = Integer.parseInt(ageText.getText());
            // Tanaka, Monahan, & Seals
            maxHr = (int)Math.round(208 - .7 * age);
            maxHrText.setText(Integer.toString(maxHr));
        } catch(NumberFormatException ex) {
            Utils.excMsg("Invalid age", ex);
            return;
        }
    }

    void calculateZoneValues() {
        int maxHr = 0, restHr = 0;
        boolean useKorvonen = false;
        int[] zoneVals = new int[6];
        try {
            maxHr = Integer.parseInt(maxHrText.getText());
            restHr = Integer.parseInt(restHrText.getText());
            useKorvonen = useKorvonenCheck.isSelected();
            // Tanaka, Monahan, & Seals
            double intensity;
            for(int i = 0; i < 6; i++) {
                intensity = .1 * i + .5;
                if(useKorvonen) {
                    zoneVals[i] = (int)Math
                        .round(intensity * (maxHr - restHr) + restHr);
                } else {
                    zoneVals[i] = (int)Math.round(intensity * maxHr);
                }
            }
            zone1ValText.setText(Integer.toString(zoneVals[0]));
            zone2ValText.setText(Integer.toString(zoneVals[1]));
            zone3ValText.setText(Integer.toString(zoneVals[2]));
            zone4ValText.setText(Integer.toString(zoneVals[3]));
            zone5ValText.setText(Integer.toString(zoneVals[4]));
            zone6ValText.setText(Integer.toString(zoneVals[5]));
        } catch(NumberFormatException ex) {
            Utils.excMsg("Invalid value for max Hr or resting HR", ex);
            return;
        }
    }

    /**
     * Add a directory to the list.
     * 
     * @param ev
     */
    private void add() {
        if(initialFileDir == null) {
            if(fileLocations != null
                && !fileLocations.getFileLocations().isEmpty()) {
                initialFileDir = fileLocations.getFileLocations()
                    .get(0).fileName;
            }
        }
        String filePath = browse(initialFileDir);
        if(filePath == null) {
            return;
        }
        // Prompt for FilterType
        Object selection = JOptionPane.showInputDialog(this,
            "Choose a filter mode", "Filter Mode", JOptionPane.QUESTION_MESSAGE,
            null, FilterMode.values(), FilterMode.values()[0]);
        if(selection == null) {
            return;
        }
        FileLocation newLocation = new FileLocation(filePath,
            (FilterMode)selection);
        fileLocations.getFileLocations().add(newLocation);
        resetFileLocationsModel();
    }

    /**
     * Remove a directory from the list.
     * 
     * @param ev
     */
    private void delete() {
        int selectedIndex = fileLocationsList.getSelectedIndex();
        if(selectedIndex < 0) {
            Utils.errMsg("Nothing selected");
            return;
        }
        fileLocations.getFileLocations().remove(selectedIndex);
        resetFileLocationsModel();
    }

    private void edit() {
        int selectedIndex = fileLocationsList.getSelectedIndex();
        if(selectedIndex < 0) {
            Utils.errMsg("Nothing selected");
            return;
        }
        String val = fileLocationsList.getSelectedValue();
        if(val == null || val.isEmpty()) {
            Utils.errMsg("Cannot determine selected value");
            return;
        }
        String[] tokens = val.split(", ");
        if(tokens.length != 2) {
            Utils.errMsg("Cannot determine filter mode and path");
            return;
        }
        FileLocation fileLocation = fileLocations.getFileLocations()
            .get(selectedIndex);
        FilterMode filterMode = fileLocation.filterMode;
        String path = fileLocation.fileName;

        JComboBox<FilterMode> filterModeCombo = new JComboBox<>(
            FilterMode.values());
        filterModeCombo.setSelectedItem(filterMode);
        JTextField pathText = new JTextField();
        if(path != null && !path.isEmpty()) {
            pathText.setText(path);
        }
        Object[] message = {"FilterMode:", filterModeCombo, "Path:", pathText};

        int option = JOptionPane.showConfirmDialog(null, message, "Edit",
            JOptionPane.OK_CANCEL_OPTION);
        if(option != JOptionPane.OK_OPTION) {
            return;
        }
        fileLocation.filterMode = (FilterMode)filterModeCombo.getSelectedItem();
        fileLocation.fileName = pathText.getText();
        resetFileLocationsModel();
    }

    /**
     * Convenience method to reset the fileLocationsModel.
     */
    private void resetFileLocationsModel() {
        fileLocationsModel = new DefaultListModel<String>();
        if(fileLocations != null
            && !fileLocations.getFileLocations().isEmpty()) {
            List<FileLocation> fileLocationsList = fileLocations
                .getFileLocations();
            for(FileLocation fileLocation : fileLocationsList) {
                fileLocationsModel.addElement(
                    fileLocation.filterMode + ", " + fileLocation.fileName);
            }
        }
        fileLocationsList.setModel(fileLocationsModel);
    }

    /**
     * Brings up a JFileChooser to choose a directory.
     */
    private String browse(String initialDirName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(initialDirName != null) {
            File dir = new File(initialDirName);
            chooser.setCurrentDirectory(dir);
            chooser.setSelectedFile(dir);
        }
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            // Process the directory
            String dirName = chooser.getSelectedFile().getPath();
            File dir = new File(dirName);
            if(!dir.exists()) {
                Utils.errMsg("Does not exist: " + dirName);
                return null;
            }
            if(!dir.isDirectory()) {
                Utils.errMsg("Not a diretory: " + dirName);
                return null;
            }
            return dirName;
        } else {
            return null;
        }
    }

    /**
     * Set the Controls from the given Settings. Can also be used to initialize
     * the dialog.
     * 
     * @param settings
     */
    public void setValues(Settings settings) {
        if(viewer == null) {
            return;
        }
        if(fileLocationsList != null) {
            fileLocations = settings.getFileLocations();
            resetFileLocationsModel();
        }
        if(hrVisibileCheck != null) {
            hrVisibileCheck.setSelected(settings.getHrVisible());
        }
        if(hrZonesVisibileCheck != null) {
            hrZonesVisibileCheck.setSelected(settings.getHrZonesVisible());
        }
        if(speedVisibileCheck != null) {
            speedVisibileCheck.setSelected(settings.getSpeedVisible());
        }
        if(eleVisibileCheck != null) {
            eleVisibileCheck.setSelected(settings.getEleVisible());
        }

        if(hrRavCountText != null) {
            hrRavCountText
                .setText(Integer.toString(settings.getHrRollingAvgCount()));
        }
        if(speedRavCountText != null) {
            speedRavCountText
                .setText(Integer.toString(settings.getSpeedRollingAvgCount()));
        }
        if(eleRavCountText != null) {
            eleRavCountText
                .setText(Integer.toString(settings.getEleRollingAvgCount()));
        }

        double val = settings.getHrMin();
        if(hrMinText != null) {
            if(Double.isNaN(val)) {
                hrMinText.setText("");
            } else {
                hrMinText.setText(Double.toString(val));
            }
        }
        val = settings.getSpeedMin();
        if(speedMinText != null) {
            if(Double.isNaN(val)) {
                speedMinText.setText("");
            } else {
                speedMinText.setText(Double.toString(val));
            }
        }
        val = settings.getEleMin();
        if(eleMinText != null) {
            if(Double.isNaN(val)) {
                eleMinText.setText("");
            } else {
                eleMinText.setText(Double.toString(val));
            }
        }
        val = settings.getHrMax();
        if(hrMaxText != null) {
            if(Double.isNaN(val)) {
                hrMaxText.setText("");
            } else {
                hrMaxText.setText(Double.toString(val));
            }
        }
        val = settings.getSpeedMax();
        if(speedMaxText != null) {
            if(Double.isNaN(val)) {
                speedMaxText.setText("");
            } else {
                speedMaxText.setText(Double.toString(val));
            }
        }
        val = settings.getEleMax();
        if(eleMaxText != null) {
            if(Double.isNaN(val)) {
                eleMaxText.setText("");
            } else {
                eleMaxText.setText(Double.toString(val));
            }
        }

        if(zone1ValText != null) {
            zone1ValText.setText(Integer.toString(settings.getZone1Val()));
        }
        if(zone2ValText != null) {
            zone2ValText.setText(Integer.toString(settings.getZone2Val()));
        }
        if(zone3ValText != null) {
            zone3ValText.setText(Integer.toString(settings.getZone3Val()));
        }
        if(zone4ValText != null) {
            zone4ValText.setText(Integer.toString(settings.getZone4Val()));
        }
        if(zone5ValText != null) {
            zone5ValText.setText(Integer.toString(settings.getZone5Val()));
        }
        if(zone6ValText != null) {
            zone6ValText.setText(Integer.toString(settings.getZone6Val()));
        }

        if(zone1ColorText != null) {
            zone1ColorText.setText(settings.getZone1Color());
        }
        if(zone2ColorText != null) {
            zone2ColorText.setText(settings.getZone2Color());
        }
        if(zone3ColorText != null) {
            zone3ColorText.setText(settings.getZone3Color());
        }
        if(zone4ColorText != null) {
            zone4ColorText.setText(settings.getZone4Color());
        }
        if(zone5ColorText != null) {
            zone5ColorText.setText(settings.getZone5Color());
        }
        if(zone6ColorText != null) {
            zone6ColorText.setText(settings.getZone6Color());
        }

        if(ageText != null) {
            ageText.setText(Integer.toString(settings.getAge()));
        }
        if(maxHrText != null) {
            maxHrText.setText(Integer.toString(settings.getMaxHr()));
        }
        if(restHrText != null) {
            restHrText.setText(Integer.toString(settings.getRestHr()));
        }
        if(useKorvonenCheck != null) {
            useKorvonenCheck.setSelected(settings.getUseKorvonen());
        }
    }

    /**
     * Sets the current values in the given Settings then checks if they are
     * valid.
     * 
     * @param settings
     * @return True if they are valid, else false.
     */
    public boolean setSettingsFromValues(Settings settings) {
        if(settings == null) {
            Utils.errMsg("Input settings is null");
            return false;
        }
        try {
            settings.setFileLocations(new FileLocations(fileLocations));

            settings.setHrVisible(hrVisibileCheck.isSelected());
            settings.setHrZonesVisible(hrZonesVisibileCheck.isSelected());
            settings.setSpeedVisible(speedVisibileCheck.isSelected());
            settings.setEleVisible(eleVisibileCheck.isSelected());

            settings.setHrRollingAvgCount(
                Integer.parseInt(hrRavCountText.getText()));
            settings.setSpeedRollingAvgCount(
                Integer.parseInt(speedRavCountText.getText()));
            settings.setEleRollingAvgCount(
                Integer.parseInt(eleRavCountText.getText()));

            String text = hrMinText.getText();
            if (text.isEmpty()) {
                settings.setHrMin(Double.NaN);
            } else {
                settings.setHrMin(Double.parseDouble(text));
            }
            text = speedMinText.getText();
            if (text.isEmpty()) {
                settings.setSpeedMin(Double.NaN);
            } else {
                settings.setSpeedMin(Double.parseDouble(text));
            }
            text = eleMinText.getText();
            if (text.isEmpty()) {
                settings.setEleMin(Double.NaN);
            } else {
                settings.setEleMin(Double.parseDouble(text));
            }

            text = hrMaxText.getText();
            if (text.isEmpty()) {
                settings.setHrMax(Double.NaN);
            } else {
                settings.setHrMax(Double.parseDouble(text));
            }
            text = speedMaxText.getText();
            if (text.isEmpty()) {
                settings.setSpeedMax(Double.NaN);
            } else {
                settings.setSpeedMax(Double.parseDouble(text));
            }
            text = eleMaxText.getText();
            if (text.isEmpty()) {
                settings.setEleMax(Double.NaN);
            } else {
                settings.setEleMax(Double.parseDouble(text));
            }

            settings.setZone1Val(Integer.parseInt(zone1ValText.getText()));
            settings.setZone2Val(Integer.parseInt(zone2ValText.getText()));
            settings.setZone3Val(Integer.parseInt(zone3ValText.getText()));
            settings.setZone4Val(Integer.parseInt(zone4ValText.getText()));
            settings.setZone5Val(Integer.parseInt(zone5ValText.getText()));
            settings.setZone6Val(Integer.parseInt(zone6ValText.getText()));

            settings.setZone1Color(zone1ColorText.getText());
            settings.setZone2Color(zone2ColorText.getText());
            settings.setZone3Color(zone3ColorText.getText());
            settings.setZone4Color(zone4ColorText.getText());
            settings.setZone5Color(zone5ColorText.getText());
            settings.setZone6Color(zone6ColorText.getText());

            settings.setAge(Integer.parseInt(ageText.getText()));
            settings.setMaxHr(Integer.parseInt(maxHrText.getText()));
            settings.setRestHr(Integer.parseInt(restHrText.getText()));
            settings.setUseKorvonen(useKorvonenCheck.isSelected());
        } catch(Exception ex) {
            Utils.excMsg("Error reading values", ex);
            return false;
        }

        // Check if the values are valid
        boolean res = settings.checkValues(true);
        if(!res) {
            Utils.errMsg("Some values are invalid");
        }
        return res;
    }

    /**
     * Saves the current values to the preference store if they are valid.
     */
    public void save() {
        Settings settings = new Settings();
        boolean res = setSettingsFromValues(settings);
        if(!res) {
            Utils.errMsg("Aborting");
            return;
        }
        // Save to the preference store
        try {
            res = settings.saveToPreferences(true);
        } catch(Exception ex) {
            Utils.excMsg("Error saving preferences", ex);
            return;
        }
        if(res) {
            // Utils.errMsg("Preferences saved successfully");
        } else {
            Utils.errMsg("Error saving preferences");
        }
    }

    /**
     * Sets the current values to the viewer if they are valid.
     */
    public void setToViewer() {
        Settings settings = new Settings();
        boolean res = setSettingsFromValues(settings);
        if(!res) {
            Utils.errMsg("Aborting");
            return;
        }
        // Copy to the viewer settings
        try {
            viewer.onPreferenceReset(settings);
        } catch(Exception ex) {
            Utils.excMsg("Error setting viewer settings", ex);
            return;
        }
        if(res) {
            // Utils.errMsg("Viewer settings set successfully");
        } else {
            Utils.errMsg("Error setting viewer settings");
        }
    }

    /**
     * Shows the dialog and returns whether it was successful or not. However
     * currently it is always successful and returns only on Done.
     * 
     * @return
     */
    public boolean showDialog() {
        setVisible(true);
        dispose();
        return ok;
    }

}
