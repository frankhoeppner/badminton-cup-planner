package org.hoeppner.cup.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimizingGamePlanCalculator extends GamePlanCalculator {

	private List<Game> initializeGames(int count) {
		List<Game> games = new ArrayList<Game>();
		for (int i = 0; i < count; i++) {
			games.add(new Game());
		}
		return games;
	}

	@Override
	public Map<Integer, List<Game>> generateGamePlan(List<Player> players, int numberOfRounds, int numberOfCourts, int minimumOpponentCount, int maxTrials) throws GameFailedException {
		boolean succeeded = false;
		int trials = 0;
		Map<Integer, List<Game>> gameRounds = new HashMap<Integer, List<Game>>();
		players.stream().forEach(Player::reset);
		int round = 1;
		while (!succeeded && trials < maxTrials) {
			try {
				while (round <= numberOfRounds) {
					List<Game> games = generateRound(players, numberOfCourts);
					for (Game game : games) {
						game.play();
					}
					gameRounds.put(round, games);
					round++;
				}
				if (getMinimumOpponentCount(players) < minimumOpponentCount) {
					throw new GameFailedException();
				}
				succeeded = true;
			} catch (GameFailedException gfe) {
				trials++;
				gameRounds = new HashMap<Integer, List<Game>>();
				players.stream().forEach(Player::reset);
				round = 1;
			}
		}
		System.out.println("getMinimumOpponentCount: " + getMinimumOpponentCount(players));
		if (gameRounds.size() < numberOfRounds) {
			System.out.println(gameRounds.size());
			throw new GameFailedException();
		}
		for (Player player : players) {
			System.out.println(player);
		}
		return gameRounds;
	}

	private List<Game> generateRound(List<Player> players, int numberOfCourts) throws GameFailedException {
		int trials = 0;
		List<Player> availablePlayers = clonePlayers(players);
		List<Game> games = initializeGames(numberOfCourts);
		while (trials < 10000 && !isComplete(games)) {
			Player player = getPlayerWithMinMatchCount(availablePlayers);
			boolean playerIsPlaying = false;
			for (Game game : games) {
				if (game.addPlayer(player)) {
					playerIsPlaying = true;
					break;
				}
			}
			if (playerIsPlaying) {
				availablePlayers.remove(player);
			} else {
				availablePlayers = clonePlayers(players);
				games = initializeGames(numberOfCourts);
				trials++;
			}
		}
		if (!isComplete(games)) {
			throw new GameFailedException();
		}
		return games;
	}

	private int getMinimumOpponentCount(List<Player> players) {
		int minimumOpponentCount = 100;
		for (Player player : players) {
			if (player.getOpponentCount() < minimumOpponentCount) {
				minimumOpponentCount = player.getOpponentCount();
			}
		}
		return minimumOpponentCount;
	}
	
	private boolean isComplete(List<Game> games) {
		boolean complete = true;
		for (Game game : games) {
			complete &= game.isComplete();
		}
		return complete;
	}
}
