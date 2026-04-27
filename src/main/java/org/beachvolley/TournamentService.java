package org.beachvolley;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;

public class TournamentService {

    private static final Logger log = LoggerFactory.getLogger(TournamentService.class);

    public static List<Team> createAllTeams(List<Player> players) {
        List<Player> men = players.stream()
                .filter(p -> p.getGender() == Gender.MALE).toList();
        List<Player> women = players.stream()
                .filter(p -> p.getGender() == Gender.FEMALE).toList();

        List<Team> teams = new ArrayList<>();
        for (Player m : men)
            for (Player w : women)
                teams.add(new Team(m, w));

        log.info("Сформировано команд: {} ({} муж × {} жен)", teams.size(), men.size(), women.size());
        return teams;
    }

    public static List<Match> generateUniqueMatches(List<Team> teams, GameMode gameMode) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++)
            for (int j = i + 1; j < teams.size(); j++) {
                Team a = teams.get(i), b = teams.get(j);
                if (disjoint(a, b))
                    matches.add(new Match(gameMode, a, b));
            }

        log.info("Сгенерировано матчей: {} [режим: {}]", matches.size(), gameMode.getDescription());
        return matches;
    }

    private static boolean disjoint(Team a, Team b) {
        return a.man() != b.man() && a.woman() != b.woman();
    }

    public static void playAll(List<Match> matches) {
        log.info("Запуск авто-симуляции {} матчей", matches.size());
        Random random = new Random();
        for (Match m : matches)
            if (!m.isPlayed()) m.play(random);
        log.info("Все матчи завершены");
    }

    public static List<Player> ranking(List<Player> players) {
        return players.stream()
                .sorted(Comparator.comparingInt(Player::rating).reversed())
                .collect(Collectors.toList());
    }

    public static void printRanking(List<Player> players) {
        List<Player> ranked = ranking(players);
        log.info("Формирование рейтинга {} игроков", ranked.size());

        System.out.println("\n" + "=".repeat(85));
        System.out.println("  РЕЙТИНГ ИГРОКОВ");
        System.out.println("=".repeat(85));
        System.out.printf("%-4s %-18s %-6s %-10s %-10s %-12s %-12s %-10s%n",
                "№", "Игрок", "Пол", "Выиграл", "Проиграл", "Мячи за", "Мячи пр", "Рейтинг");
        System.out.println("-".repeat(85));

        for (int i = 0; i < ranked.size(); i++) {
            Player p = ranked.get(i);
            System.out.printf("%-4d %-18s %-6s %-10d %-10d %-12d %-12d %-10d%n",
                    i + 1, p.getName(),
                    p.getGender() == Gender.MALE ? "М" : "Ж",
                    p.getWins(), p.getLosses(),
                    p.getPointsScored(), p.getPointsConceded(),
                    p.rating());
            log.debug("Рейтинг #{}: {} — wins={} losses={} pts={}/{} rating={}",
                    i + 1, p.getName(), p.getWins(), p.getLosses(),
                    p.getPointsScored(), p.getPointsConceded(), p.rating());
        }
        System.out.println("=".repeat(85));
    }
}