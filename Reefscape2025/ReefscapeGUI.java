package Reefscape2025;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ReefscapeGUI extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReefscapeGUI().setVisible(true));
    }

    private ReefscapeManager manager;

    private int teamsAdded;
    private ArrayList<Integer> addedTeamNumbers = new ArrayList<>();
    private int redTeams;
    private int blueTeams;

    // GUI components
    private JTextField teamNumberField;
    private JTextField qualitativePointsField;
    private JTextField autoScoringField;
    private JTextField teleopScoringField;
    private JButton addButton;
    private JTextArea outputArea;
    private JToggleButton teamColorToggle; // Red/Blue

    public ReefscapeGUI() {
        manager = new ReefscapeManager();
        manager.loadMainFile();

        teamsAdded = 0;
        redTeams = 0;
        blueTeams = 0;

        setUndecorated(true); // Remove OS title bar
        setupGUI();

        // Save on close
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    manager.sortTeamsByTeamNumber();
                    manager.sortTeamsByAverageQualitativeScore();
                    manager.updateMainFile();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setupGUI() {
        setTitle("Reefscape Scouting GUI");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Header ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("REEFSCAPE SCOUTING GUI");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonsPanel.setOpaque(false);

        // Header buttons: minimize -> maximize -> close
        JButton minimizeButton = new JButton("_");
        styleButton(minimizeButton, Color.DARK_GRAY);
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton maxButton = new JButton("⬜");
        styleButton(maxButton, Color.DARK_GRAY);
        maxButton.addActionListener(e -> {
            if (getExtendedState() != JFrame.MAXIMIZED_BOTH)
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            else
                setExtendedState(JFrame.NORMAL);
        });

        JButton closeButton = new JButton("X");
        styleButton(closeButton, Color.RED);
        closeButton.addActionListener(e -> {
            try {
                manager.sortTeamsByTeamNumber();
                manager.sortTeamsByAverageQualitativeScore();
                manager.updateMainFile();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            dispose(); // close the frame
            System.exit(0); // terminate the app
        });

        buttonsPanel.add(minimizeButton);
        buttonsPanel.add(maxButton);
        buttonsPanel.add(closeButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        // Toggle for Red/Blue
        teamColorToggle = new JToggleButton("RED TEAM"); // Default: red
        styleButton(teamColorToggle, new Color(255, 102, 102));
        teamColorToggle.addActionListener(e -> {
            if (teamColorToggle.isSelected()) {
                teamColorToggle.setText("BLUE TEAM");
            } else {
                teamColorToggle.setText("RED TEAM");
            }
        });
        headerPanel.add(teamColorToggle, BorderLayout.CENTER);

        // Make header draggable
        final Point clickPoint = new Point();
        headerPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clickPoint.x = e.getX();
                clickPoint.y = e.getY();
            }
        });
        headerPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - clickPoint.x, p.y + e.getY() - clickPoint.y);
            }
        });

        // === Input Panel ===
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBackground(new Color(30, 30, 30));
        Color labelColor = Color.WHITE;

        JLabel teamNumLabel = new JLabel("Team Number:");
        teamNumLabel.setForeground(labelColor);
        inputPanel.add(teamNumLabel);

        teamNumberField = new JTextField();
        styleTextField(teamNumberField);
        inputPanel.add(teamNumberField);

        JLabel qPointsLabel = new JLabel("Qualitative Points to Add:");
        qPointsLabel.setForeground(labelColor);
        inputPanel.add(qPointsLabel);

        qualitativePointsField = new JTextField();
        styleTextField(qualitativePointsField);
        inputPanel.add(qualitativePointsField);

        JLabel autoLabel = new JLabel("Auto Scoring Types (comma separated):");
        autoLabel.setForeground(labelColor);
        inputPanel.add(autoLabel);

        autoScoringField = new JTextField();
        styleTextField(autoScoringField);
        inputPanel.add(autoScoringField);

        JLabel teleopLabel = new JLabel("Teleop Scoring Types (comma separated):");
        teleopLabel.setForeground(labelColor);
        inputPanel.add(teleopLabel);

        teleopScoringField = new JTextField();
        styleTextField(teleopScoringField);
        inputPanel.add(teleopScoringField);

        addButton = new JButton("Add Team");
        styleButton(addButton, new Color(70, 130, 180));
        inputPanel.add(addButton);

        // Combine header + input panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // EXIT WITHOUT SAVING
        JButton exitWithoutSaveButton = new JButton("EXIT WITHOUT SAVING (ONLY PRESS IF MISTAKE)");
        styleButton(exitWithoutSaveButton, Color.RED);
        exitWithoutSaveButton.setFont(exitWithoutSaveButton.getFont().deriveFont(Font.BOLD, 16f));
        exitWithoutSaveButton.addActionListener(e -> {
            dispose();
            System.exit(0);
        });
        add(exitWithoutSaveButton, BorderLayout.SOUTH);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(20, 20, 20));
        outputArea.setForeground(Color.WHITE);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.getViewport().setBackground(new Color(20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> handleAddTeam());
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(50, 50, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
    }

    private void styleButton(AbstractButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void handleAddTeam() {
        if (teamsAdded >= 6) {
            JOptionPane.showMessageDialog(this, "You can only add 6 teams!", "Limit Reached",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int teamNumber = Integer.parseInt(teamNumberField.getText().trim());
            int qualitativePoints = Integer.parseInt(qualitativePointsField.getText().trim());

            ArrayList<ReefscapeScoringTypesAuto> autoTypes = new ArrayList<>();
            for (String s : autoScoringField.getText().split(",")) {
                s = s.trim();
                if (!s.isEmpty())
                    autoTypes.add(ReefscapeScoringTypesAuto.valueOf(s));
            }

            ArrayList<ReefscapeScoringTypes> teleopTypes = new ArrayList<>();
            for (String s : teleopScoringField.getText().split(",")) {
                s = s.trim();
                if (!s.isEmpty())
                    teleopTypes.add(ReefscapeScoringTypes.valueOf(s));
            }

            if (addedTeamNumbers.contains(teamNumber)) {
                JOptionPane.showMessageDialog(this, "You have already added team " + teamNumber + "!", "Duplicate Team",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (autoTypes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Empty Auto Types!", "No Auto Types",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (teleopTypes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Empty Teleop Types!", "No Teleop Types",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (qualitativePoints <= 0) {
                JOptionPane.showMessageDialog(this, "Qualitative points cannot be 0 or less!", "Invalid QS",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (qualitativePoints > 3) {
                JOptionPane.showMessageDialog(this, "Qualitative points cannot be more than 3!", "Invalid QS",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            addTeam(teamNumber, autoTypes, teleopTypes, qualitativePoints);

            // Determine color prefix
            String colorPrefix;
            if (teamColorToggle.isSelected()) { // Blue
                blueTeams++;
                colorPrefix = "Blue " + blueTeams + ". ";
            } else { // Red
                redTeams++;
                colorPrefix = "Red " + redTeams + ". ";
            }

            outputArea.append(colorPrefix + "Team " + teamNumber + " -> QS: " +
                    qualitativePoints + ", Auto: " + autoTypes + ", Teleop: " + teleopTypes + "\n\n");

            outputArea.setCaretPosition(outputArea.getDocument().getLength());

            teamNumberField.setText("");
            qualitativePointsField.setText("");
            autoScoringField.setText("");
            teleopScoringField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTeam(int teamNumber, ArrayList<ReefscapeScoringTypesAuto> autoTypes,
            ArrayList<ReefscapeScoringTypes> teleopTypes, int qualitativePoints) {
        if (!manager.containsTeam(teamNumber)) {
            manager.addTeam(new ReefscapeTeam(teamNumber, autoTypes, teleopTypes, qualitativePoints, 1));
        } else {
            ReefscapeTeam oldTeam = manager.getTeam(teamNumber);
            oldTeam.addTypesAuto(autoTypes);
            oldTeam.addTypes(teleopTypes);
            oldTeam.addQualitativeScore(qualitativePoints);
            manager.updateTeam(oldTeam);
        }
        teamsAdded++;
    }
}
