package org.beachvolley;

import java.util.List;

public record Team(Player man, Player woman) {

    public double strength() {
        return (man.getSkill() + woman.getSkill()) / 2;
    }

    public List<Player> players() {
        return List.of(man, woman);
    }

    @Override
    public String toString() {
        return man.getName() + " / " + woman.getName();
    }
}