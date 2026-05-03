package org.beachvolley.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Player {

    private String name;
    private Gender gender;
    private double skill;

    private int wins;
    private int losses;
    private int pointsScored;
    private int pointsConceded;

    public Player(String name, Gender gender, double skill) {
        this.name = name;
        this.gender = gender;
        this.skill = skill;
    }

    public void addWin() { wins++; }
    public void addLoss() { losses++; }
    public void addPointsScored(int p) { pointsScored += p; }
    public void addPointsConceded(int p) { pointsConceded += p; }

    public double winRate() {
        int total = wins + losses;
        return total == 0 ? 0.0 : (double) wins / total;
    }

    public int rating() {
        return 100
                + wins * 5
                - losses * 2
                + pointsScored / 10
                - pointsConceded / 10;
    }
}