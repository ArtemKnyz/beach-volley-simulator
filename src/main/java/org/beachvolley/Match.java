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
        log.debug("Создан матч: {} vs {} [{}]", team1, team2, gameMode.getDescription());
    }

    public Team getTeam1()    { return team1; }
    public Team getTeam2()    { return team2; }
    public int getScore1()    { return score1; }
    public int getScore2()    { return score2; }
    public boolean isPlayed() { return played; }

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
        log.debug("Ручной ввод счёта: {}:{} для {} vs {}", s1, s2, team1, team2);

        int target = gameMode.getTargetScore();
        if (s1 == s2)
            throw new IllegalArgumentException("Ничья недопустима.");
        if (Math.max(s1, s2) < target)
            throw new IllegalArgumentException(
                    "Победитель должен набрать минимум " + target + " очков.");

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
        if (score1 > score2) {
            team1.players().forEach(Player::addWin);
            team2.players().forEach(Player::addLoss);
            log.debug("Победитель: {}", team1);
        } else {
            team2.players().forEach(Player::addWin);
            team1.players().forEach(Player::addLoss);
            log.debug("Победитель: {}", team2);
        }
    }

    public String getTeamsString() {
        return String.format("%s  vs  %s  [до %d]", team1, team2, gameMode.getTargetScore());
    }

    @Override
    public String toString() {
        String status = played ? String.format("%d:%d", score1, score2) : "не сыгран";
        return String.format("%-18s vs %-18s  %s  [%s]",
                team1, team2, status, gameMode.getDescription());
    }
}