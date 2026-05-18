package org.beachvolley.model;

import lombok.Getter;

@Getter
public enum GameMode {

    POINTS_11(11, "Beach Express"),
    POINTS_15(15, "Beach Standard"),
    POINTS_21(21, "Classic"),
    POINTS_25(25, "Training");

    private final int targetScore;
    private final String description;

    GameMode(int targetScore, String description) {
        this.targetScore = targetScore;
        this.description = description;
    }

}