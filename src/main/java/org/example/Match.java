package org.example;

import java.util.Random;

public class Match {

    private final GameMode gameMode;
    private final Team team1;
    private final Team team2;

    private int score1;
    private int score2;

    public Match(GameMode gameMode, Team team1, Team team2) {
        this.gameMode = gameMode;
        this.team1 = team1;
        this.team2 = team2;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public void play(Random random) {

        int target = gameMode.getTargetScore();

        while (score1 < target && score2 < target) {

            double p = team1.strength() /
                    (team1.strength() + team2.strength());

            if (random.nextDouble() < p)
                score1++;
            else
                score2++;

            if ((score1 >= target || score2 >= target)
                    && Math.abs(score1 - score2) >= 2) {
                break;
            }
        }

        applyStats();
    }

    private void applyStats() {

        for (Player p : team1.players()) {
            p.addPointsScored(score1);
            p.addPointsConceded(score2);
        }

        for (Player p : team2.players()) {
            p.addPointsScored(score2);
            p.addPointsConceded(score1);
        }

        if (score1 > score2) {
            team1.players().forEach(Player::addWin);
            team2.players().forEach(Player::addLoss);
        } else {
            team2.players().forEach(Player::addWin);
            team1.players().forEach(Player::addLoss);
        }
    }

    @Override
    public String toString() {
        return String.format("%-10s vs %-10s %d:%d",
                team1, team2, score1, score2);
    }
}