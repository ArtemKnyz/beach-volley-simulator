package org.beachvolley;

import org.beachvolley.model.Gender;
import org.beachvolley.model.Player;
import org.beachvolley.model.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    @DisplayName("Сила команды = среднее навыков игроков")
    void strengthIsAverage() {
        Player man   = new Player("М", Gender.MALE,   0.80);
        Player woman = new Player("Ж", Gender.FEMALE, 0.60);
        Team team = new Team(man, woman);
        assertEquals(0.70, team.strength(), 0.001);
    }

    @Test
    @DisplayName("players() возвращает обоих игроков")
    void playersContainsBoth() {
        Player man   = new Player("М", Gender.MALE,   0.80);
        Player woman = new Player("Ж", Gender.FEMALE, 0.60);
        Team team = new Team(man, woman);
        assertTrue(team.players().contains(man));
        assertTrue(team.players().contains(woman));
        assertEquals(2, team.players().size());
    }
}