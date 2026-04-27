package org.example;

public class Match {
    private final Team team1;
    private final Team team2;
    private final int score1;
    private final int score2;

    public Match(Team team1, Team team2, int score1, int score2) {
        this.team1 = team1;
        this.team2 = team2;
        this.score1 = score1;
        this.score2 = score2;
    }
}
