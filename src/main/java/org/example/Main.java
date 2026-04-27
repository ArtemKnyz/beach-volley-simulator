package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Player> players = List.of(
                new Player("M1", Gender.MALE, 0.80),
                new Player("M2", Gender.MALE, 0.70),
                new Player("M3", Gender.MALE, 0.60),
                new Player("F1", Gender.FEMALE, 0.78),
                new Player("F2", Gender.FEMALE, 0.68),
                new Player("F3", Gender.FEMALE, 0.58)
        );

        List<Team> teams = TournamentService.createAllTeams(players);
        List<Match> matches = TournamentService.generateUniqueMatches(teams);

        System.out.println("Всего команд: " + teams.size());
        System.out.println("Всего уникальных матчей: " + matches.size());

        TournamentService.playAll(matches);

        System.out.println("\nРезультаты матчей:");
        matches.forEach(System.out::println);

        System.out.println("\nРейтинг игроков:");
        TournamentService.ranking(players).forEach(System.out::println);
    }
}
