package Reefscape2025;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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

    // public void sortTeamsByAverageQualitativeScore() {
    //     ArrayList<ReefscapeTeam> newTeams = new ArrayList<>();

    //     for (ReefscapeTeam team : teams) {
    //         newTeams.add(team);
    //     }
        
    //     teams.sort((b, a) -> a.getAverageQualitativeScore() - b.getAverageQualitativeScore());

    //     try {
    //         new PrintWriter("Reefscape2025\\Files\\AvgQualitative.txt").close(); // Clear the file

    //         FileWriter writer = null;

    //         writer = new FileWriter("Reefscape2025\\Files\\AvgQualitative.txt");

    //         for (int i = 0; i < newTeams.size(); i++) {
    //             ReefscapeTeam team = newTeams.get(i);
    //             team.sortScoringTypes();

    //             writer.write(team.getTeamNumber() + ": " + team.getQualitativeScore() + ", " + team.getGamesPlayed() + ", "
    //                     + team.getTypes() + "\n");
    //         }

    //         writer.close();
    //     } catch (Exception e) {
    //     }
    // }

    private void loadMainFile() {
        try (Scanner sc = new Scanner(new File("Reefscape2025\\Files\\MainFile.txt"))) {

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println("currline = " + line);

                String teamNumberString = line.substring(0, line.indexOf(":"));
                int teamNumber = Integer.parseInt(teamNumberString);

                line = line.substring(line.indexOf(":") + 1);

                String qualitativeScoreString = line.substring(0, line.indexOf(","));
                int qualitativeScore = Integer.parseInt(qualitativeScoreString);

                line = line.substring(line.indexOf(",") + 1);

                String gamesPlayedString = line.substring(0, line.indexOf(","));
                int gamesPlayed = Integer.parseInt(gamesPlayedString);

                line = line.substring(line.indexOf(",") + 2);
                line = line.substring(0, line.length() - 1);

                ArrayList<ReefscapeScoringTypes> types = new ArrayList<ReefscapeScoringTypes>();

                for (String typeString : line.split(", ")) {
                    ReefscapeScoringTypes type = ReefscapeScoringTypes.valueOf(typeString);
                    types.add(type);
                }

                ReefscapeTeam team = new ReefscapeTeam(teamNumber, types, qualitativeScore, gamesPlayed);
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
                        + team.getTypes() + "\n");
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

        ArrayList<ReefscapeScoringTypes> types = new ArrayList<ReefscapeScoringTypes>();
        types.add(ReefscapeScoringTypes.Barge);
        types.add(ReefscapeScoringTypes.L3);
        types.add(ReefscapeScoringTypes.Deep);

        ReefscapeTeam team1 = new ReefscapeTeam(1234, types, 10, 5);
        //manager.addTeam(team1);

        ArrayList<ReefscapeScoringTypes> types2 = new ArrayList<ReefscapeScoringTypes>();
        types2.add(ReefscapeScoringTypes.AlgaeRemove);
        types2.add(ReefscapeScoringTypes.BadDef);
        types2.add(ReefscapeScoringTypes.L2);

        ReefscapeTeam team2 = new ReefscapeTeam(5678, types2, 20, 15);
        //manager.addTeam(team2);

        ReefscapeTeam team3 = new ReefscapeTeam(3101, types2, 5, 2);
        //manager.addTeam(team3);

        manager.sortTeamsByTeamNumber();

        ReefscapeTeam team4 = manager.getTeam(1234);
        team4.addQualitativeScore(3);
        manager.updateTeam(team4);

        manager.updateMainFile();

        //manager.sortTeamsByAverageQualitativeScore();
    }
}
