package org.example;

public class Player {
    private final String name;
    private final String gender; // "M" или "F"
    private int pointsScored;
    private int pointsConceded;
    private int wins;
    private int losses;

    public Player(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }


}
