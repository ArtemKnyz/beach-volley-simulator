package org.example;

public class Player {

    private final String name;
    private final Gender gender;
    private final double skill;

    private int wins;
    private int losses;
    private int pointsScored;
    private int pointsConceded;

    public Player(String name, Gender gender, double skill) {
        this.name = name;
        this.gender = gender;
        this.skill = skill;
    }

    public String getName() {
        return name;
    }

    public Gender gender() {
        return gender;
    }

    public double skill() {
        return skill;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    public void addPointsScored(int p) {
        pointsScored += p;
    }

    public void addPointsConceded(int p) {
        pointsConceded += p;
    }

    public double winRate() {
        int total = wins + losses;
        return total == 0 ? 0 : (double) wins / total;
    }

    public double pointRate() {
        int total = pointsScored + pointsConceded;
        return total == 0 ? 0 : (double) pointsScored / total;
    }

    public double rating() {
        return 0.5 * winRate() + 0.5 * pointRate();
    }

    @Override
    public String toString() {
        return name + " rating=" + rating();
    }
}