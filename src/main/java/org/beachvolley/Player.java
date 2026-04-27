package org.beachvolley;

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

    public String getName()      { return name; }
    public Gender getGender()    { return gender; }
    public double getSkill()     { return skill; }
    public int getWins()         { return wins; }
    public int getLosses()       { return losses; }
    public int getPointsScored() { return pointsScored; }
    public int getPointsConceded(){ return pointsConceded; }

    public void addWin()                  { wins++; }
    public void addLoss()                 { losses++; }
    public void addPointsScored(int p)    { pointsScored += p; }
    public void addPointsConceded(int p)  { pointsConceded += p; }

    public double winRate() {
        int total = wins + losses;
        return total == 0 ? 0.0 : (double) wins / total;
    }

    public double pointRate() {
        int total = pointsScored + pointsConceded;
        return total == 0 ? 0.0 : (double) pointsScored / total;
    }

    /**
     * Целочисленный рейтинг:
     *   победа = +5 очков рейтинга
     *   поражение = -2 очка рейтинга
     *   каждые 10 набранных очков в матчах = +1 очко рейтинга
     *   каждые 10 пропущенных = -1 очко рейтинга
     * Базовый рейтинг = 100
     */
    public int rating() {
        return 100
                + wins * 5
                - losses * 2
                + pointsScored / 10
                - pointsConceded / 10;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] skill=%.2f wins=%d losses=%d rating=%d",
                name, gender, skill, wins, losses, rating());
    }
}