package Reefscape2025;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ReefscapeViewerGUI extends JFrame {
    private JTextArea displayArea;
    private File mainFile = new File("Reefscape2025\\MainFile.txt");
    private ArrayList<ReefscapeTeam> teams = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReefscapeViewerGUI().setVisible(true));
    }

    public ReefscapeViewerGUI() {
        setupGUI();
        readMainFile();
        showAllTeams();
    }

    private void setupGUI() {
        setTitle("Reefscape Data Viewer");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(25, 25, 25));

        JLabel header = new JLabel("Reefscape Scouting Data Viewer", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(35, 35, 35));
        displayArea.setForeground(Color.WHITE);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(25, 25, 25));

        JButton showAllTeamsButton = new JButton("Show All Teams");
        styleButton(showAllTeamsButton, new Color(0, 153, 0));
        showAllTeamsButton.addActionListener(e -> showAllTeams());

        JButton filterTypeButton = new JButton("Filter by Type");
        styleButton(filterTypeButton, new Color(200, 150, 50));
        filterTypeButton.addActionListener(e -> showTeamsByType());

        buttonPanel.add(showAllTeamsButton);
        buttonPanel.add(filterTypeButton);
    
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void readMainFile() {
        teams.clear();

        try (Scanner sc = new Scanner(mainFile)) {

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                String teamNumberString = line.substring(0, line.indexOf(":"));
                int teamNumber = Integer.parseInt(teamNumberString);

                line = line.substring(line.indexOf(":") + 1);

                String qualitativeScoreString = line.substring(0, line.indexOf(","));
                int qualitativeScore = Integer.parseInt(qualitativeScoreString);

                line = line.substring(line.indexOf(",") + 1);

                String gamesPlayedString = line.substring(0, line.indexOf(","));
                int gamesPlayed = Integer.parseInt(gamesPlayedString);

                line = line.substring(line.indexOf(",") + 2);
                int closeBracketPos = line.indexOf("]");

                String typesString = line.substring(0, closeBracketPos);

                Set<ReefscapeScoringTypes> typesSet = new HashSet<>();

                for (String typeString : typesString.split(", ")) {
                    typesSet.add(ReefscapeScoringTypes.valueOf(typeString));
                }

                ArrayList<ReefscapeScoringTypes> types = new ArrayList<>(typesSet);

                line = line.substring(closeBracketPos + 3, line.length() - 1);

                Set<ReefscapeScoringTypesAuto> typesAutoSet = new HashSet<>();

                for (String typeString : line.split(", ")) {
                    typesAutoSet.add(ReefscapeScoringTypesAuto.valueOf(typeString));
                }

                ArrayList<ReefscapeScoringTypesAuto> typesAuto = new ArrayList<>(typesAutoSet);

                ReefscapeTeam team = new ReefscapeTeam(teamNumber, typesAuto, types, qualitativeScore, gamesPlayed);
                teams.add(team);
            }

            System.out.println(teams.size() + " teams loaded from main file.");
            sc.close();
        } catch (NumberFormatException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showAllTeams() {
        StringBuilder sb = new StringBuilder("=== ALL TEAMS ===\n\n");

        for (ReefscapeTeam team : teams) {
            sb.append("Team ").append(team.getTeamNumber()).append(" -> ")
              .append("  Qualitative Score: ").append(team.getQualitativeScore())
              .append(",  Games Played: ").append(team.getGamesPlayed())
              .append(",  Types (Auto): ").append(team.getTypesAuto())
              .append(",  Types (Teleop): ").append(team.getTypes()).append("\n\n");
        }
        displayArea.setText(sb.toString());
    }

    private void showTeamsByType() {
        readMainFile();

        String inputType = JOptionPane.showInputDialog(this,
                "Enter what type of info you want to display:");

        if (inputType == null || inputType.trim().isEmpty()) return;
        inputType = inputType.trim();

        StringBuilder sb = new StringBuilder("=== TEAMS WITH TYPE: " + inputType + " ===\n\n");

        boolean isAutoType = false;

        @SuppressWarnings("rawtypes")
        Enum exactType;
        if (inputType.charAt(inputType.length() - 1) != 'A') {
            exactType = ReefscapeScoringTypes.valueOf(inputType);
        } else {
            exactType = ReefscapeScoringTypesAuto.valueOf(inputType);
            isAutoType = true;
        }

        for (ReefscapeTeam team : teams) {
            boolean found = false;

            if (isAutoType) {
                for (ReefscapeScoringTypesAuto type : team.getTypesAuto()) {
                    if (type == exactType) {
                        found = true;
                        break;
                    }
                }
            } else {
                for (ReefscapeScoringTypes type : team.getTypes()) {
                    if (type.name().equals(inputType)) {
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                sb.append("Team ").append(team.getTeamNumber()).append("\n\n");
            }
        }

        if (sb.toString().equals("=== TEAMS WITH TYPE: " + inputType + " ===\n\n")) {
            sb.append("No teams found with the specified type.");
        }

        displayArea.setText(sb.toString());
    }
}
