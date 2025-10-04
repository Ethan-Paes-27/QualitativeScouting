package Reefscape2025;

import java.util.ArrayList;
import java.util.List;


public class ReefscapeTeam {
    private int teamNumber;

    private ArrayList<ReefscapeScoringTypesAuto> scoringTypesAuto;
    private ArrayList<ReefscapeScoringTypes> scoringTypes;
    private int qualitativeScore;
    private int gamesPlayed;

    public ReefscapeTeam(int teamNumber,  ArrayList<ReefscapeScoringTypesAuto> scoringTypesAuto, ArrayList<ReefscapeScoringTypes> scoringTypes, int qualitativeScore, int gamesPlayed) {
        this.teamNumber = teamNumber;
        this.scoringTypesAuto = new ArrayList<>(scoringTypesAuto);
        this.scoringTypes = new ArrayList<>(scoringTypes);
        this.qualitativeScore = qualitativeScore;
        this.gamesPlayed = gamesPlayed;
    }

    public void addTypesAuto(List<ReefscapeScoringTypesAuto> scoringTypesAuto) {
    for (ReefscapeScoringTypesAuto type : scoringTypesAuto) {
        if (!this.scoringTypesAuto.contains(type)) {
            this.scoringTypesAuto.add(type);
        }
    }
    sortScoringTypesAuto();
}

public void addTypes(List<ReefscapeScoringTypes> scoringTypes) {
    for (ReefscapeScoringTypes type : scoringTypes) {
        if (!this.scoringTypes.contains(type)) {
            this.scoringTypes.add(type);
        }
    }
    sortScoringTypes();
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
    
    public ArrayList<ReefscapeScoringTypesAuto> getTypesAuto() {
        return scoringTypesAuto;
    }

    public int getQualitativeScore() {
        return qualitativeScore;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public double getAverageQualitativeScore() {
        if (gamesPlayed == 0) return 0;
        return (double)qualitativeScore / gamesPlayed;
    }

    public void sortScoringTypesAuto() {
        scoringTypesAuto.sort((a, b) -> a.ordinal() - b.ordinal());
    }

    public void sortScoringTypes() {
        scoringTypes.sort((a, b) -> a.ordinal() - b.ordinal());
    }
}