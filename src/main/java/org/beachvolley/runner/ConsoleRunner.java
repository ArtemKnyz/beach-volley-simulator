package org.beachvolley.runner;

import lombok.RequiredArgsConstructor;
import org.beachvolley.model.Match;
import org.beachvolley.service.TournamentService;
import org.beachvolley.model.GameMode;
import org.beachvolley.model.Player;
import org.beachvolley.model.Team;
import org.beachvolley.service.InputService;
import org.beachvolley.service.OutputService;

import java.util.List;

@RequiredArgsConstructor
public class ConsoleRunner {

    private final InputService input;
    private final OutputService output;

    public void run() {
        output.printHeader();

        List<Player> players = input.inputPlayers();
        GameMode mode = input.selectGameMode();
        boolean manual = input.chooseManualMode();

        output.printGameMode(mode);

        List<Team> teams = TournamentService.createAllTeams(players);
        List<Match> matches = TournamentService.generateUniqueMatches(teams, mode);
        matches = TournamentService.scheduleMatches(matches, 3);

        output.printTournamentInfo(teams, matches);

        if (manual) {
            runManual(matches, players);
        } else {
            TournamentService.playAll(matches);
        }

        output.printResults(matches);
        output.printRanking(players);
    }

    private void runManual(List<Match> matches, List<Player> players) {
        int i = 1;
        for (Match match : matches) {
            System.out.println("\nМатч #" + i++);
            input.enterScore(match);
        }
    }

}