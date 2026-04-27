package org.example;

import java.util.*;
import java.util.stream.Collectors;

class TournamentService {

    public static List<Team> createAllTeams(List<Player> players) {

        List<Player> men = players.stream()
                .filter(p -> p.gender() == Gender.MALE)
                .toList();

        List<Player> women = players.stream()
                .filter(p -> p.gender() == Gender.FEMALE)
                .toList();

        List<Team> teams = new ArrayList<>();

        for (Player m : men)
            for (Player w : women)
                teams.add(new Team(m, w));

        return teams;
    }

    public static List<Match> generateUniqueMatches(List<Team> teams) {

        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {

                Team a = teams.get(i);
                Team b = teams.get(j);

                if (disjoint(a, b))
                    matches.add(new Match(GameMode.POINTS_15, a, b));
            }
        }

        return matches;
    }

    private static boolean disjoint(Team a, Team b) {
        return a.man() != b.man()
                && a.woman() != b.woman();
    }

    public static List<Player> ranking(List<Player> players) {
        return players.stream()
                .sorted(Comparator.comparingDouble(Player::rating).reversed())
                .collect(Collectors.toList());
    }



    public static void playAll(List<Match> matches) {
        Random random = new Random();

        for (Match match : matches) {
            match.play(random);  // Не забудьте передать random!
        }
    }
}