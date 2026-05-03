package org.beachvolley.service;

import org.beachvolley.model.Match;
import org.beachvolley.model.GameMode;
import org.beachvolley.model.Player;
import org.beachvolley.model.Team;

import java.util.List;

public class OutputService {

    public void printHeader() {
        System.out.println("\n🏐 Beach Volley Simulator\n");
    }

    public void printGameMode(GameMode mode) {
        System.out.println("Режим: " + mode.getDescription()
                + " до " + mode.getTargetScore());
    }

    public void printTournamentInfo(List<Team> teams, List<Match> matches) {
        System.out.println("Команд: " + teams.size());
        System.out.println("Матчей: " + matches.size());
    }

    public void printResults(List<Match> matches) {
        System.out.println("\nРезультаты:");
        for (Match m : matches) {
            System.out.println(m);
        }
    }

    public void printRanking(List<Player> players) {
        TournamentService.printRanking(players);
    }

    public void printPlayerStats(List<Player> players) {
        System.out.println("\nСтатистика:");
        players.forEach(p ->
                System.out.println(p.getName() +
                        " W:" + p.getWins() +
                        " L:" + p.getLosses()));
    }
}