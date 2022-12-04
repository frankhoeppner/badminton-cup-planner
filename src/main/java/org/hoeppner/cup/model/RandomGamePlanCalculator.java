package org.hoeppner.cup.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RandomGamePlanCalculator extends GamePlanCalculator {

	@Override
	public Map<Integer, List<Game>> generateGamePlan(List<Player> players,  int numberOfRounds, int numberOfCourts, int minimumOpponentCount, int maxTrials) {
		Map<Integer, List<Game>> gameRounds = new HashMap<Integer, List<Game>>();
		for (Player player : players) {
			player.reset();
		}
		int round = 0;
		int failedTrials = 0;
		System.out.println("Calculating " + numberOfRounds + " Rounds!");
		while (round < numberOfRounds && failedTrials < maxTrials) {
			System.out.println("Number of Players: " + players.size());
			List<Player> availablePlayers = clonePlayers(players);
			List<Game> games = new ArrayList<Game>();
			int court = 0;
			boolean calculationOK = true;
			while (court < numberOfCourts && calculationOK) {
				try {
					Player player1 = selectPlayer(availablePlayers);
					Player player2 = selectPartner(player1, availablePlayers);
					Player player3 = selectPlayer(player1, player2, availablePlayers);
					Player player4 = selectPartner(player1, player2, player3, availablePlayers);
					court++;
					availablePlayers.remove(player1);
					availablePlayers.remove(player2);
					availablePlayers.remove(player3);
					availablePlayers.remove(player4);
					games.add(new Game(Arrays.asList(player1, player2, player3, player4)));
				} catch (GameFailedException gfe) {
					calculationOK = false;
				}
			}
			if (calculationOK) {
				round++;
				for (Game game : games) {
					game.play();
				}
				gameRounds.put(round, games);
				System.out.println("Calculation of Round " + (round - 1) + " succeeded with " + failedTrials + " failures!");
			} else {
				failedTrials++;
			}
		}
		System.out.println("Finished Calculation of GamePlan after " + failedTrials + " failures with " + round + " successful calculated rounds!");
		for (Player player : players) {
			System.out.println("GamePlan, Player=" + player);
		}
		return gameRounds;
	}

	private Player selectPlayer(List<Player> players) {
		return getPlayerWithMinMatchCount(players);
	}

	private Player selectPlayer(Player firstPlayer, Player firstPartner, List<Player> players) throws GameFailedException {
		List<Player> possiblePlayers = getPossiblePlayers(players, firstPlayer, firstPartner);
		return getMinimumOpponent(firstPlayer, firstPartner, possiblePlayers);
	}

	private Player selectPartner(Player firstPlayer, List<Player> players) throws GameFailedException {
		List<Player> possiblePlayers = getPossiblePlayers(players, firstPlayer);
		List<Player> possiblePartners = getPossiblePartners(possiblePlayers, firstPlayer);
		int playerIndex = ThreadLocalRandom.current().nextInt(0, possiblePartners.size());
		return possiblePartners.get(playerIndex);
	}

	private Player selectPartner(Player firstPlayer, Player firstPartner, Player secondPlayer, List<Player> players) throws GameFailedException {
		List<Player> possiblePlayers = getPossiblePlayers(players, firstPlayer, firstPartner, secondPlayer);
		List<Player> possiblePartners = getPossiblePartners(possiblePlayers, secondPlayer);
		return getMinimumOpponent(firstPlayer, firstPartner, possiblePartners);
	}

	private List<Player> getPossiblePlayers(List<Player> players, Player... notPosiblePlayers) throws GameFailedException {
		List<Player> possiblePlayers = new ArrayList<>();
		for (Player player : players) {
			boolean playerPossible = true;
			for (int i = 0; i < notPosiblePlayers.length; i++) {
				if (player.getName().equalsIgnoreCase(notPosiblePlayers[i].getName())) {
					playerPossible = false;
				}
			}
			if (playerPossible) {
				possiblePlayers.add(player);
			}
		}
		if (possiblePlayers.isEmpty()) {
			throw new GameFailedException();
		}
		return possiblePlayers;
	}

	private List<Player> getPossiblePartners(List<Player> players, Player partner) throws GameFailedException {
		List<Player> possiblePartners = new ArrayList<>();
		for (Player player : players) {
			if (!player.hasPlayedWith(partner)) {
				possiblePartners.add(player);
			}
		}
		if (possiblePartners.isEmpty()) {
			throw new GameFailedException();
		}
		return possiblePartners;
	}

	private Player getMinimumOpponent(Player opponent1, Player opponent2, List<Player> possiblePartners) throws GameFailedException {
		int minGameCount = 100;
		Map<Integer, List<Player>> partners = new HashMap<>();
		for (Player possiblePartner : possiblePartners) {
			int countOpponent = Math.max(possiblePartner.playedAgainst(opponent1), possiblePartner.playedAgainst(opponent2));
			if (countOpponent < 2) {
				if (!partners.containsKey(countOpponent)) {
					partners.put(countOpponent, new ArrayList<>());
				}
				partners.get(countOpponent).add(possiblePartner);
				if (countOpponent < minGameCount) {
					minGameCount = countOpponent;
				}
			}
		}
		if (minGameCount == 100) {
			throw new GameFailedException();
		}
		int playerIndex = ThreadLocalRandom.current().nextInt(0, partners.get(minGameCount).size());
		return partners.get(minGameCount).get(playerIndex);
	}

}
