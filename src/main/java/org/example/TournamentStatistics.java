package org.example;

import java.util.List;

public class TournamentStatistics {
    private final List<Player> players;
    private final List<Match> matches;

    public TournamentStatistics(List<Player> players, List<Match> matches) {
        this.players = players;
        this.matches = matches;
        calculateStats();
    }

    private void calculateStats() {}
}
