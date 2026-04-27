package org.example;

import java.util.List;

public record Team(Player man, Player woman) {

    public double strength() {
        return (man.skill() + woman.skill()) / 2;
    }
    public List<Player> players() {
        return List.of(man, woman);
    }
}