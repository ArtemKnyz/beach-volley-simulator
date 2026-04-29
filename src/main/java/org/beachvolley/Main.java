package org.beachvolley;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

    private static Scanner sc;

    public static void main(String[] args) {

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        sc = new Scanner(System.in, StandardCharsets.UTF_8);

        int width = 44;
        String title = "ПЛЯЖНЫЙ ВОЛЕЙБОЛ - МИКСТ 2x2";
        String subtitle = "Турнир смешанных пар";

        String topBot = "+" + "=".repeat(width) + "+";
        String empty = "|" + " ".repeat(width) + "|";

        System.out.println(topBot);
        System.out.println(empty);
        System.out.println("|" + center(title, width) + "|");
        System.out.println("|" + center(subtitle, width) + "|");
        System.out.println(empty);
        System.out.println(topBot);
        System.out.println();


        List<Player> players = inputPlayers();

        GameMode gameMode = selectGameMode();
        System.out.println("✓ Режим: " + gameMode.getDescription()
                + " (до " + gameMode.getTargetScore() + " очков)\n");

        List<Team> teams = TournamentService.createAllTeams(players);
        List<Match> matches = TournamentService.generateUniqueMatches(teams, gameMode);

        matches = TournamentService.scheduleMatches(matches, 3);

        System.out.println("Всего команд:  " + teams.size());
        System.out.println("Всего матчей:  " + matches.size());

        System.out.println("\nКак проводить матчи?");
        System.out.println("  1 — Ручной ввод счёта");
        System.out.println("  2 — Автоматическая симуляция");
        System.out.print("Ваш выбор: ");
        String modeInput = sc.nextLine().trim();

        if ("1".equals(modeInput)) {
            playManual(matches, players);

        } else {
            Random random = new Random();
            for (Match m : matches) {
                m.play(random);
                System.out.println("  " + m);
                printPlayerStats(players);
            }
        }

        System.out.println("\nРезультаты матчей:");
        System.out.println("─".repeat(55));
        for (int i = 0; i < matches.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, matches.get(i));
        }

        TournamentService.printRanking(players);

        System.out.println("\nПодробный рейтинг:");
        List<Player> ranked = TournamentService.ranking(players);
        for (int i = 0; i < ranked.size(); i++) {
            Player p = ranked.get(i);
            System.out.printf(
                    "  %d. %s — выиграл %d, проиграл %d. Мячи: %d забил / %d пропустил. Рейтинг: %d%n",
                    i + 1,
                    p.getName(),
                    p.getWins(),
                    p.getLosses(),
                    p.getPointsScored(),
                    p.getPointsConceded(),
                    p.rating());
        }
    }

    private static String center(String text, int width) {
        int padding = (width - text.length()) / 2;
        int extra = (width - text.length()) % 2;
        return " ".repeat(padding) + text + " ".repeat(padding + extra);
    }

    private static GameMode selectGameMode() {
        GameMode[] modes = GameMode.values();

        System.out.println("Выберите режим игры:");
        for (int i = 0; i < modes.length; i++) {
            System.out.printf("  %d — до %2d очков  (%s)%n",
                    i + 1,
                    modes[i].getTargetScore(),
                    modes[i].getDescription());
        }

        while (true) {
            System.out.print("Ваш выбор (1-" + modes.length + "): ");
            String input = sc.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= modes.length) {
                    return modes[choice - 1];
                }
            } catch (NumberFormatException ignored) {
            }

            System.out.println("  ! Введите число от 1 до " + modes.length);
        }
    }

    private static List<Player> inputPlayers() {
        List<Player> players = new ArrayList<>();

        System.out.println("Введите имена 3 мужчин:");
        for (int i = 1; i <= 3; i++) {
            System.out.print("  Мужчина " + i + ": ");
            String name = readNonBlank();
            double skill = 0.80 - (i - 1) * 0.10;
            players.add(new Player(name, Gender.MALE, skill));
        }

        System.out.println("Введите имена 3 женщин:");
        for (int i = 1; i <= 3; i++) {
            System.out.print("  Женщина " + i + ": ");
            String name = readNonBlank();
            double skill = 0.78 - (i - 1) * 0.10;
            players.add(new Player(name, Gender.FEMALE, skill));
        }

        return players;
    }

    private static void playManual(List<Match> matches, List<Player> players) {
        System.out.println("\nВводите счёт в формате «11:15»");
        System.out.println("(пустой Enter = автосимуляция этого матча)\n");
        Random random = new Random();

        for (int i = 0; i < matches.size(); i++) {
            Match m = matches.get(i);
            System.out.printf("Матч %2d: Игра #%-3d %s%n",
                    i + 1, i + 1, m.getTeamsString());

            while (true) {
                System.out.print("  Счёт: ");
                String line = sc.nextLine().trim();

                if (line.isEmpty()) {
                    m.play(random);
                    System.out.printf("  → авто: %d:%d%n%n",
                            m.getScore1(), m.getScore2());
                    break;
                }

                int[] scores = parseScore(line);
                if (scores == null) {
                    System.out.println("  ! Неверный формат. Введите счёт в виде «11:15» или нажмите Enter для авто.");
                    continue;
                }

                try {
                    m.setScore(scores[0], scores[1]);
                    System.out.printf("  → принято: %d:%d%n%n",
                            m.getScore1(), m.getScore2());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.printf("  ! %s Попробуйте ещё раз.%n", e.getMessage());
                }
            }

            printPlayerStats(players);

        }
    }

    private static int[] parseScore(String input) {
        String[] parts = input.split(":");
        if (parts.length != 2) return null;
        try {
            int s1 = Integer.parseInt(parts[0].trim());
            int s2 = Integer.parseInt(parts[1].trim());
            if (s1 < 0 || s2 < 0) return null;
            return new int[]{s1, s2};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String readNonBlank() {
        String line;
        do {
            line = sc.nextLine().trim();
        }
        while (line.isEmpty());
        return line;
    }


    private static void printPlayerStats(List<Player> players) {

        List<Player> sorted = players.stream()
                .sorted(Comparator.comparingInt(Player::getWins).reversed()
                        .thenComparingInt(Player::getLosses))
                .toList();

        System.out.println("  +-----------------+----------+----------+--------+");
        System.out.println("  | Статистика игроков:                             |");
        System.out.println("  +-----------------+----------+----------+--------+");
        System.out.printf("  | %-15s | %-8s | %-8s | %-6s |%n",
                "Игрок", "Сыграно", "Победы", "Пор-я");
        System.out.println("  +-----------------+----------+----------+--------+");

        for (Player p : sorted) {
            int total = p.getWins() + p.getLosses();
            System.out.printf("  | %-15s | %-8d | %-8d | %-6d |%n",
                    p.getName(), total, p.getWins(), p.getLosses());
        }
        System.out.println("  +-----------------+----------+----------+--------+");
        System.out.println();
    }

}