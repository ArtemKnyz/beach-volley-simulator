package org.beachvolley;

import org.beachvolley.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private Team team1;
    private Team team2;

    @BeforeEach
    void setUp() {
        team1 = new Team(
                new Player("М1", Gender.MALE,   0.80),
                new Player("Ж1", Gender.FEMALE, 0.78));
        team2 = new Team(
                new Player("М2", Gender.MALE,   0.60),
                new Player("Ж2", Gender.FEMALE, 0.58));
    }

    @Test
    @DisplayName("После авто-игры матч помечен как сыгранный")
    void afterPlayIsPlayed() {
        Match match = new Match(GameMode.POINTS_11, team1, team2);
        match.play(new Random(42));
        assertTrue(match.isPlayed());
    }

    @Test
    @DisplayName("Победитель набирает >= targetScore")
    void winnerReachesTarget() {
        Match match = new Match(GameMode.POINTS_11, team1, team2);
        match.play(new Random(42));
        int target = GameMode.POINTS_11.getTargetScore();
        assertTrue(Math.max(match.getScore1(), match.getScore2()) >= target);
    }

    @Test
    @DisplayName("Ничья после setScore — исключение")
    void drawThrowsException() {
        Match match = new Match(GameMode.POINTS_11, team1, team2);
        assertThrows(IllegalArgumentException.class, () -> match.setScore(11, 11));
    }

    @Test
    @DisplayName("Победитель ниже target после setScore — исключение")
    void scoreBelowTargetThrows() {
        Match match = new Match(GameMode.POINTS_11, team1, team2);
        assertThrows(IllegalArgumentException.class, () -> match.setScore(5, 8));
    }

    @Test
    @DisplayName("Корректный ручной ввод 11:7 принимается")
    void validManualScore() {
        Match match = new Match(GameMode.POINTS_11, team1, team2);
        match.setScore(11, 7);
        assertEquals(11, match.getScore1());
        assertEquals(7,  match.getScore2());
        assertTrue(match.isPlayed());
    }

    @Test
    @DisplayName("После матча победитель получает +1 win, проигравший +1 loss")
    void winsAndLossesApplied() {
        Match match = new Match(GameMode.POINTS_11, team1, team2);
        match.setScore(11, 5);
        assertEquals(1, team1.man().getWins());
        assertEquals(1, team1.woman().getWins());
        assertEquals(1, team2.man().getLosses());
        assertEquals(1, team2.woman().getLosses());
    }

    @Test
    @DisplayName("Очки корректно записываются игрокам")
    void pointsApplied() {
        Match match = new Match(GameMode.POINTS_11, team1, team2);
        match.setScore(11, 7);
        assertEquals(11, team1.man().getPointsScored());
        assertEquals(7,  team1.man().getPointsConceded());
        assertEquals(7,  team2.man().getPointsScored());
        assertEquals(11, team2.man().getPointsConceded());
    }
}