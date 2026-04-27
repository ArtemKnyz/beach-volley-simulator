package org.example;


/**
 * Турнир по пляжному волейболу (микст 3x3)
 * Все 18 уникальных матчей между непересекающимися командами
 * Расчёт уровня игроков на основе набранных очков и побед
 */
public class BeachVolleyStats {

    // ------------------- Игроки -------------------
    static class Player {
        String name;
        double skill; // 0..1 - вероятность выиграть очко при подаче/розыгрыше (условно)
        int pointsScored;   // очков набрано его командами
        int pointsConceded; // очков пропущено
        int wins;   // выигранные матчи (в которых участвовал)
        int losses; // проигранные матчи

        Player(String name, double skill) {
            this.name = name;
            this.skill = skill;
        }

        // процент выигранных очков
        double pointsPercent() {
            int total = pointsScored + pointsConceded;
            return total == 0 ? 0.5 : (double) pointsScored / total;
        }

        // процент побед
        double winsPercent() {
            int total = wins + losses;
            return total == 0 ? 0.5 : (double) wins / total;
        }

        // итоговый уровень (вес 0.5 + 0.5)
        double overallLevel() {
            return 0.5 * pointsPercent() + 0.5 * winsPercent();
        }
    }
}