package org.beachvolley.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Getter
public class Match {

    private final GameMode gameMode;
    private final Team team1;
    private final Team team2;

    private int score1;
    private int score2;
    private boolean played = false;

    public Match(GameMode gameMode, Team team1, Team team2) {
        this.gameMode = gameMode;
        this.team1 = team1;
        this.team2 = team2;
    }

    public String getTeamsString() {
        return String.format("%s vs %s (до %d)",
                team1,
                team2,
                gameMode.getTargetScore());
    }

    public void play(Random random) {
        log.debug("Авто-симуляция: {} vs {}", team1, team2);

        int target = gameMode.getTargetScore();

        while (score1 < target && score2 < target) {
            double p = team1.strength() / (team1.strength() + team2.strength());
            if (random.nextDouble() < p) score1++;
            else score2++;

            if ((score1 >= target || score2 >= target)
                    && Math.abs(score1 - score2) >= 2) break;
        }

        played = true;
        applyStats();

        log.info("Матч сыгран: {} vs {} {}:{}", team1, team2, score1, score2);
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

        Team winner = score1 > score2 ? team1 : team2;
        Team loser  = score1 > score2 ? team2 : team1;

        winner.players().forEach(Player::addWin);
        loser.players().forEach(Player::addLoss);
    }

    public void setScore(int s1, int s2) {

        int target = gameMode.getTargetScore();

        if (s1 < 0 || s2 < 0)
            throw new IllegalArgumentException("Счёт не может быть отрицательным");

        if (s1 == s2)
            throw new IllegalArgumentException("Ничья невозможна");

        int winner = Math.max(s1, s2);
        int loser  = Math.min(s1, s2);

        if (winner < target)
            throw new IllegalArgumentException("Победитель не достиг target");

        if (winner > target && winner - loser != 2)
            throw new IllegalArgumentException("Нужно преимущество в 2 очка");

        this.score1 = s1;
        this.score2 = s2;
        this.played = true;

        applyStats();
    }
}