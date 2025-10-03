package Reefscape2025;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ReefscapeManager {
    public ArrayList<ReefscapeTeam> teams;

    public ReefscapeManager() {
        teams = new ArrayList<ReefscapeTeam>();
    }

    public boolean containsTeam(int teamNumber) {
        for (ReefscapeTeam team : teams) {
            if (teamNumber == team.getTeamNumber()) {
                return true;
            }
        }
        return false;
    }

    public void addTeam(ReefscapeTeam team) {
        if (containsTeam(team.getTeamNumber())) {
            System.out.println("Team " + team.getTeamNumber() + " already exists. Not adding.");
            return;
        }
        teams.add(team);
    }

    public void updateTeam(ReefscapeTeam updatedTeam) {
        if (containsTeam(updatedTeam.getTeamNumber()) == false) {
            System.out.println("Team " + updatedTeam.getTeamNumber() + " does not exist. Not updating.");
            return;
        }

        for (int i = 0; i < teams.size(); i++) {
            ReefscapeTeam team = teams.get(i);
            if (updatedTeam.getTeamNumber() == team.getTeamNumber()) {
                teams.set(i, updatedTeam);
                return;
            }
        }
    }

    public ReefscapeTeam getTeam(int teamNumber) {
        for (ReefscapeTeam team : teams) {
            if (teamNumber == team.getTeamNumber()) {
                return team;
            }
        }
        return null;
    }

    public void sortTeamsByTeamNumber() {
        teams.sort((b, a) -> b.getTeamNumber() - a.getTeamNumber());
    }

    public void sortTeamsByAverageQualitativeScore() {
        ArrayList<ReefscapeTeam> newTeams = new ArrayList<>();

        for (ReefscapeTeam team : teams) {
            newTeams.add(team);
        }

        newTeams.sort((a, b) -> Double.compare(b.getAverageQualitativeScore(), a.getAverageQualitativeScore()));

        try {
            new PrintWriter("Reefscape2025\\Files\\AvgQualitative.txt").close(); // Clear the file

            FileWriter writer = null;

            writer = new FileWriter("Reefscape2025\\Files\\AvgQualitative.txt");

            for (int i = 0; i < newTeams.size(); i++) {
                ReefscapeTeam team = newTeams.get(i);
                team.sortScoringTypes();

                double avgQS = Math.floor(team.getAverageQualitativeScore() * 100) / 100;

                writer.write(i + 1 + ". Team " + team.getTeamNumber() + "-> AvgQS: " +
                        avgQS + ", Auto Types: " + team.getTypesAuto() + ", Types: "
                        + team.getTypes() + "\n\n");
            }

            writer.close();
        } catch (Exception e) {
        }
    }

    private void loadMainFile() {
        try (Scanner sc = new Scanner(new File("Reefscape2025\\Files\\MainFile.txt"))) {

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

    public void updateMainFile() throws FileNotFoundException {
        new PrintWriter("Reefscape2025\\Files\\MainFile.txt").close(); // Clear the file

        FileWriter writer = null;

        try {
            writer = new FileWriter("Reefscape2025\\Files\\MainFile.txt");

            for (int i = 0; i < teams.size(); i++) {
                ReefscapeTeam team = teams.get(i);
                team.sortScoringTypes();

                writer.write(team.getTeamNumber() + ":" + team.getQualitativeScore() + "," + team.getGamesPlayed() + ","
                        + team.getTypes() + "," + team.getTypesAuto() + "\n");
            }

            writer.close();
        } catch (Exception e) {
        }
    }

    public void clearTeamsFORDEBUGONLY() {
        teams.clear();
    }

    public static void main(String[] args) throws FileNotFoundException {
        ReefscapeManager manager = new ReefscapeManager();

        manager.loadMainFile();

        // ArrayList<ReefscapeScoringTypesAuto> typesAuto = new ArrayList<>();
        // typesAuto.add(ReefscapeScoringTypesAuto.L1Auto);

        // ArrayList<ReefscapeScoringTypes> types = new ArrayList<>();
        // types.add(ReefscapeScoringTypes.Barge);
        // types.add(ReefscapeScoringTypes.L3);
        // types.add(ReefscapeScoringTypes.Deep);

        // ReefscapeTeam team1 = new ReefscapeTeam(1234, typesAuto, types, 10, 5);
        // manager.addTeam(team1);

        // ArrayList<ReefscapeScoringTypes> types2 = new ArrayList<>();
        // types2.add(ReefscapeScoringTypes.AlgaeRemove);
        // types2.add(ReefscapeScoringTypes.BadDef);
        // types2.add(ReefscapeScoringTypes.L2);

        // ReefscapeTeam team2 = new ReefscapeTeam(5678, typesAuto, types2, 20, 15);
        // manager.addTeam(team2);

        // ReefscapeTeam team3 = new ReefscapeTeam(3101, typesAuto, types2, 5, 2);
        // manager.addTeam(team3);

        ReefscapeTeam team4 = manager.getTeam(1234);
        team4.addQualitativeScore(3);
        ArrayList<ReefscapeScoringTypes> newTypes = new ArrayList<>();
        newTypes.add(ReefscapeScoringTypes.L4);
        team4.addTypes(newTypes);
        ArrayList<ReefscapeScoringTypesAuto> newTypesAuto = new ArrayList<>();
        newTypesAuto.add(ReefscapeScoringTypesAuto.BargeAuto);
        team4.addTypesAuto(newTypesAuto);
        
        team4.addTypesAuto(newTypesAuto);
        
        team4.addTypesAuto(newTypesAuto);
        
        team4.addTypesAuto(newTypesAuto);
        
        team4.addTypesAuto(newTypesAuto);
        
        team4.addTypesAuto(newTypesAuto);
        manager.updateTeam(team4);

        manager.sortTeamsByTeamNumber();

        manager.sortTeamsByAverageQualitativeScore();

        manager.updateMainFile();
    }
}
