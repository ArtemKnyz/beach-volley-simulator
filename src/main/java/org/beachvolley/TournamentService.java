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

        log.info("Сформировано команд: {}", teams.size());
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

    public static List<Match> scheduleMatches(List<Match> matches, int maxConsecutive) {
        log.info("Составлено расписание: {} матчей, макс. подряд = {}",
                matches.size(), maxConsecutive);

        Set<Player> allPlayers = new HashSet<>();
        for (Match m : matches) {
            allPlayers.addAll(m.getTeam1().players());
            allPlayers.addAll(m.getTeam2().players());
        }

        List<Match> remaining = new ArrayList<>(matches);
        List<Match> scheduled = new ArrayList<>();
        Map<Player, Integer> consecutive = new HashMap<>();
        allPlayers.forEach(p -> consecutive.put(p, 0));

        Random rng = new Random();

        while (!remaining.isEmpty()) {

            List<Match> candidates = remaining.stream()
                    .filter(m -> canPlay(m, consecutive, maxConsecutive))
                    .collect(Collectors.toList());

            if (candidates.isEmpty()) {
                log.warn("Сброс счётчиков на шаге {}", scheduled.size() + 1);
                allPlayers.forEach(p -> consecutive.put(p, 0));
                candidates = new ArrayList<>(remaining);
            }

            final Map<Player, Integer> consecutiveFinal = consecutive;
            int minScore = candidates.stream()
                    .mapToInt(m -> matchConsecutiveScore(m, consecutiveFinal))
                    .min()
                    .orElse(0);

            List<Match> bestCandidates = candidates.stream()
                    .filter(m -> matchConsecutiveScore(m, consecutiveFinal) == minScore)
                    .toList();

            Match chosen = bestCandidates.get(rng.nextInt(bestCandidates.size()));

            scheduled.add(chosen);
            remaining.remove(chosen);

            Set<Player> playingNow = new HashSet<>();
            playingNow.addAll(chosen.getTeam1().players());
            playingNow.addAll(chosen.getTeam2().players());

            for (Player p : allPlayers) {
                if (playingNow.contains(p)) {
                    consecutive.merge(p, 1, Integer::sum);
                } else {
                    consecutive.put(p, 0);
                }
            }

            for (Player p : playingNow) {
                if (consecutive.get(p) > maxConsecutive) {
                    log.error("НАРУШЕНИЕ: {} играет {} раз подряд!", p.getName(), consecutive.get(p));
                }
            }

            log.debug("Матч {}: {} vs {} | Счётчики: {}",
                    scheduled.size(),
                    chosen.getTeam1(),
                    chosen.getTeam2(),
                    consecutive.entrySet().stream()
                            .filter(e -> e.getValue() > 0)
                            .map(e -> e.getKey().getName() + "=" + e.getValue())
                            .collect(Collectors.joining(", ")));
        }

        log.info("Расписание составлено: {} матчей", scheduled.size());
        return scheduled;
    }


    private static int matchConsecutiveScore(Match m, Map<Player, Integer> consecutive) {
        return m.getTeam1().players().stream()
                .mapToInt(p -> consecutive.getOrDefault(p, 0)).sum()
                + m.getTeam2().players().stream()
                .mapToInt(p -> consecutive.getOrDefault(p, 0)).sum();
    }

    private static boolean canPlay(Match match,
                                   Map<Player, Integer> consecutive,
                                   int maxConsecutive) {
        for (Player p : match.getTeam1().players())
            if (consecutive.getOrDefault(p, 0) >= maxConsecutive) return false;
        for (Player p : match.getTeam2().players())
            if (consecutive.getOrDefault(p, 0) >= maxConsecutive) return false;
        return true;
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
        }
        System.out.println("=".repeat(85));
    }
}