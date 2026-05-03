package org.beachvolley.controller;

import org.beachvolley.model.Match;
import org.beachvolley.service.TournamentService;
import org.beachvolley.model.GameMode;
import org.beachvolley.model.Player;
import org.beachvolley.model.Team;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tournament")
public class TournamentController {

    @PostMapping("/simulate")
    public List<Match> simulate(@RequestBody List<Player> players) {

        GameMode mode = GameMode.POINTS_21;

        List<Team> teams = TournamentService.createAllTeams(players);
        List<Match> matches = TournamentService.generateUniqueMatches(teams, mode);

        TournamentService.playAll(matches);

        return matches;
    }

    @PostMapping("/ranking")
    public List<Player> ranking(@RequestBody List<Player> players) {
        return TournamentService.ranking(players);
    }
}
