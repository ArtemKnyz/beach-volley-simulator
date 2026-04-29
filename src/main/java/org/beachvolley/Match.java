package org.beachvolley;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Match {

    private static final Logger log = LoggerFactory.getLogger(Match.class);

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

    public boolean isPlayed() {
        return played;
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
        log.info("Матч сыгран (авто): {} vs {}  {}:{}", team1, team2, score1, score2);
    }

    public void setScore(int s1, int s2) {
        int target = gameMode.getTargetScore();

        if (s1 < 0 || s2 < 0)
            throw new IllegalArgumentException(
                    "Счёт не может быть отрицательным.");

        if (s1 == s2)
            throw new IllegalArgumentException(
                    "Ничья недопустима.");

        int winner = Math.max(s1, s2);
        int loser  = Math.min(s1, s2);

        if (winner < target)
            throw new IllegalArgumentException(
                    "Победитель должен набрать минимум " + target + " очков.");

        if (winner > target) {
            if (loser != target - 1)
                throw new IllegalArgumentException(
                        "При счёте выше " + target +
                                " возможен только дьюс. Например " +
                                (target + 1) + ":" + (target - 1));
            if (winner - loser != 2)
                throw new IllegalArgumentException(
                        "При дьюсе победитель должен вести ровно на 2 очка.");
        }

        log.debug("Ручной ввод счёта: {}:{} для {} vs {}", s1, s2, team1, team2);
        this.score1 = s1;
        this.score2 = s2;
        played = true;
        applyStats();
        log.info("Матч сыгран (вручную): {} vs {}  {}:{}", team1, team2, score1, score2);
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
        Team loser = score1 > score2 ? team2 : team1;

        winner.players().forEach(Player::addWin);
        loser.players().forEach(Player::addLoss);

        log.debug("Победитель {}", winner);
    }

    @Override
    public String toString() {
        String score = played ? String.format("%d:%d", score1, score2) : "не сыгран";
        return String.format("%-18s vs %-18s  %s  [%s]",
                team1, team2, score, gameMode.getDescription());
    }

    public String getTeamsString() {
        return String.format("%-18s vs  %-18s [до %d]",
                team1, team2, gameMode.getTargetScore());
    }


}