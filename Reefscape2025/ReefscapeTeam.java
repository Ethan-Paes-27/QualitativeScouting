package Reefscape2025;

import java.util.ArrayList;


public class ReefscapeTeam {
    private int teamNumber;
    private String teamName;

    private ArrayList<ReefscapeScoringTypes> scoringTypes;
    private int qualitativeScore;
    private int gamesPlayed;

    public ReefscapeTeam(int teamNumber, ArrayList<ReefscapeScoringTypes> scoringTypes, int qualitativeScore, int gamesPlayed) {
        this.teamNumber = teamNumber;
        this.scoringTypes = scoringTypes;
        this.qualitativeScore = qualitativeScore;
        this.gamesPlayed = gamesPlayed;
    }

    public void addTypes(ArrayList<ReefscapeScoringTypes> scoringTypes) {
        this.scoringTypes.addAll(scoringTypes);
    }
    
    public void addQualitativeScore(int score) {
        this.qualitativeScore += score;
        gamesPlayed++;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public ArrayList<ReefscapeScoringTypes> getTypes() {
        return scoringTypes;
    }

    public int getQualitativeScore() {
        return qualitativeScore;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getAverageQualitativeScore() {
        if (gamesPlayed == 0) return 0;
        return qualitativeScore / gamesPlayed;
    }

    public void sortScoringTypes() {
        scoringTypes.sort((a, b) -> a.ordinal() - b.ordinal());
    }
}