package org.beachvolley;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TournamentServiceTest {

    private List<Player> players;

    @BeforeEach
    void setUp() {
        players = List.of(
                new Player("М1", Gender.MALE,   0.80),
                new Player("М2", Gender.MALE,   0.70),
                new Player("М3", Gender.MALE,   0.60),
                new Player("Ж1", Gender.FEMALE, 0.78),
                new Player("Ж2", Gender.FEMALE, 0.68),
                new Player("Ж3", Gender.FEMALE, 0.58)
        );
    }

    @Test
    @DisplayName("3 муж × 3 жен = 9 команд")
    void createAllTeamsCount() {
        List<Team> teams = TournamentService.createAllTeams(players);
        assertEquals(9, teams.size());
    }

    @Test
    @DisplayName("В каждой команде 1 мужчина и 1 женщина")
    void eachTeamHasOneManOneWoman() {
        List<Team> teams = TournamentService.createAllTeams(players);
        for (Team t : teams) {
            assertEquals(Gender.MALE,   t.man().getGender());
            assertEquals(Gender.FEMALE, t.woman().getGender());
        }
    }

    @Test
    @DisplayName("Количество уникальных матчей = 18")
    void generateMatchesCount() {
        List<Team> teams = TournamentService.createAllTeams(players);
        List<Match> matches = TournamentService.generateUniqueMatches(teams, GameMode.POINTS_11);
        assertEquals(18, matches.size());
    }

    @Test
    @DisplayName("В матче не повторяется ни один игрок из двух команд")
    void matchTeamsAreDisjoint() {
        List<Team> teams = TournamentService.createAllTeams(players);
        List<Match> matches = TournamentService.generateUniqueMatches(teams, GameMode.POINTS_11);
        for (Match m : matches) {
            assertNotEquals(m.getTeam1().man(),   m.getTeam2().man());
            assertNotEquals(m.getTeam1().woman(), m.getTeam2().woman());
        }
    }

    @Test
    @DisplayName("После playAll все матчи сыграны")
    void allMatchesPlayedAfterPlayAll() {
        List<Team> teams = TournamentService.createAllTeams(players);
        List<Match> matches = TournamentService.generateUniqueMatches(teams, GameMode.POINTS_11);
        TournamentService.playAll(matches);
        assertTrue(matches.stream().allMatch(Match::isPlayed));
    }

    @Test
    @DisplayName("Рейтинг отсортирован по убыванию")
    void rankingIsSortedDescending() {
        List<Team> teams = TournamentService.createAllTeams(players);
        List<Match> matches = TournamentService.generateUniqueMatches(teams, GameMode.POINTS_11);
        TournamentService.playAll(matches);

        List<Player> ranked = TournamentService.ranking(players);
        for (int i = 0; i < ranked.size() - 1; i++) {
            assertTrue(ranked.get(i).rating() >= ranked.get(i + 1).rating());
        }
    }

    @Test
    @DisplayName("Все матчи используют выбранный GameMode")
    void matchesUseCorrectGameMode() {
        List<Team> teams = TournamentService.createAllTeams(players);
        // Проверяем через счёт: победитель должен набрать >= 21
        List<Match> matches = TournamentService.generateUniqueMatches(teams, GameMode.POINTS_21);
        TournamentService.playAll(matches);
        for (Match m : matches) {
            assertTrue(Math.max(m.getScore1(), m.getScore2()) >= 21);
        }
    }
}
