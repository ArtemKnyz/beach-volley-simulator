package org.beachvolley;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Тест", Gender.MALE, 0.75);
    }

    @Test
    @DisplayName("Базовый рейтинг без матчей = 100")
    void ratingDefaultIs100() {
        Assertions.assertEquals(100, player.rating());
    }

    @Test
    @DisplayName("Победа прибавляет +5 к рейтингу")
    void winAddsToRating() {
        player.addWin();
        Assertions.assertEquals(105, player.rating());
    }

    @Test
    @DisplayName("Поражение снимает 2 очка рейтинга")
    void lossSubtractsFromRating() {
        player.addLoss();
        Assertions.assertEquals(98, player.rating());
    }

    @Test
    @DisplayName("Рейтинг учитывает мячи: +1 за каждые 10 забитых")
    void pointsScoredAffectRating() {
        player.addPointsScored(30);
        Assertions.assertEquals(103, player.rating());
    }

    @Test
    @DisplayName("Рейтинг учитывает мячи: -1 за каждые 10 пропущенных")
    void pointsConcededAffectRating() {
        player.addPointsConceded(20);
        Assertions.assertEquals(98, player.rating());
    }

    @Test
    @DisplayName("winRate = 0 без матчей")
    void winRateZeroWithNoMatches() {
        Assertions.assertEquals(0.0, player.winRate());
    }

    @Test
    @DisplayName("winRate корректен: 2 победы из 3 матчей")
    void winRateCalculation() {
        player.addWin();
        player.addWin();
        player.addLoss();
        Assertions.assertEquals(2.0 / 3.0, player.winRate(), 0.001);
    }

    @Test
    @DisplayName("Комплексный рейтинг: 5 побед, 2 поражения, 80 забитых, 40 пропущенных")
    void complexRating() {
        for (int i = 0; i < 5; i++) player.addWin();
        for (int i = 0; i < 2; i++) player.addLoss();
        player.addPointsScored(80);
        player.addPointsConceded(40);
        Assertions.assertEquals(125, player.rating());
    }
}