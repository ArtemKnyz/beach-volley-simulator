package org.beachvolley.service;

import org.beachvolley.model.Match;
import org.beachvolley.model.GameMode;
import org.beachvolley.model.Gender;
import org.beachvolley.model.Player;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class InputService {

    private final Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

    public List<Player> inputPlayers() {
        List<Player> players = new ArrayList<>();

        System.out.println("Введите 3 мужчин:");
        for (int i = 0; i < 3; i++) {
            String name = readValidName("  Мужчина " + (i + 1) + ": ");
            players.add(new Player(name, Gender.MALE, 0.8 - i * 0.1));
        }

        System.out.println("Введите 3 Женщин:");
        for (int i = 0; i < 3; i++) {
            String name = readValidName("  Женщин " + (i + 1) + ": ");
            players.add(new Player(name, Gender.FEMALE, 0.8 - i * 0.1));
        }

        return players;
    }

    public GameMode selectGameMode() {
        GameMode[] modes = GameMode.values();

        System.out.println("\nВыберите режим игры:");
        for (int i = 0; i < modes.length; i++) {
            System.out.printf("%d — %s%n", i + 1, modes[i].getDescription());
        }

        int choice = readIntInRange("Ваш выбор: ", 1, modes.length);
        return modes[choice - 1];
    }

    private int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("  ! Введите число");
                continue;
            }

            try {
                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("  ! Число должно быть от %d до %d%n", min, max);
                }

            } catch (NumberFormatException e) {
                System.out.println("  ! Введите корректное число");
            }
        }
    }

    public boolean chooseManualMode() {
        System.out.println("1 — вручную, 2 — авто");
        return "1".equals(sc.nextLine());
    }

    public void enterScore(Match match) {
        System.out.println();
        System.out.println("🏐 Результат встречи:");
        System.out.println("👉 " + match.getTeamsString());
        System.out.println("Введите счёт в формате 11:7 (Enter = авто)");

        while (true) {
            System.out.print("Счёт: ");
            String line = sc.nextLine().trim();

            if (line.isEmpty()) {
                match.play(new Random());
                System.out.printf("✔ Авто: %d:%d%n",
                        match.getScore1(), match.getScore2());
                return;
            }

            String[] parts = line.split(":");
            if (parts.length != 2) {
                System.out.println("❌ Неверный формат. Пример: 11:7");
                continue;
            }

            try {
                int s1 = Integer.parseInt(parts[0].trim());
                int s2 = Integer.parseInt(parts[1].trim());

                match.setScore(s1, s2);

                System.out.printf("✔ Принято: %d:%d%n",
                        match.getScore1(), match.getScore2());
                return;

            } catch (NumberFormatException e) {
                System.out.println("❌ Нужно вводить числа. Пример: 11:7");

            } catch (IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    private String readValidName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("  ! Имя не может быть пустым");
                continue;
            }

            if (input.length() < 2) {
                System.out.println("  ! Имя слишком короткое");
                continue;
            }

            if (!input.matches("^[a-zA-Zа-яА-ЯёЁ ]+$")) {
                System.out.println("  ! Имя должно содержать только буквы");
                continue;
            }

            return input;
        }
    }
}