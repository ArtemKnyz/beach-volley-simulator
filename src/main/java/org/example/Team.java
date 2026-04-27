package org.example;

import java.util.Objects;

public class Team {

    Player man;
    Player woman;

    Team(Player man, Player woman) {
        this.man = man;
        this.woman = woman;
    }

    // Средний скилл команды (как вероятность выиграть розыгрыш против другой команды)
    double avgSkill() {
        return (man.skill + woman.skill) / 2.0;
    }

    @Override
    public String toString() {
        return man.name + "+" + woman.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return man.equals(team.man) && woman.equals(team.woman);
    }

    @Override
    public int hashCode() {
        return Objects.hash(man, woman);
    }
}


