package org.beachvolley.runner;

import lombok.RequiredArgsConstructor;
import org.beachvolley.model.GameMode;
import org.beachvolley.model.Match;
import org.beachvolley.model.Player;
import org.beachvolley.model.Team;
import org.beachvolley.service.InputService;
import org.beachvolley.service.OutputService;
import org.beachvolley.service.TournamentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {

    private final InputService input;
    private final OutputService output;

    @Override
    public void run(String... args) {

        output.printHeader();

        List<Player> players = input.inputPlayers();

        GameMode mode = input.selectGameMode();

        boolean manualMode = input.chooseManualMode();

        output.printGameMode(mode);

        List<Team> teams =
                TournamentService.createAllTeams(players);

        List<Match> matches =
                TournamentService.generateUniqueMatches(teams, mode);

        matches =
                TournamentService.scheduleMatches(matches, 3);

        if (manualMode) {
            runManual(matches, players);
        } else {
            TournamentService.playAll(matches);
        }

        output.printResults(matches);

        output.printRanking(players);

        System.exit(0);
    }

    private void runManual(List<Match> matches,
                           List<Player> players) {

        for (Match match : matches) {

            input.enterScore(match);

            output.printPlayerStats(players);
        }
    }
}