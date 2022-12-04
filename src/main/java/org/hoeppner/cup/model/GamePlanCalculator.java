package org.hoeppner.cup.model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class GamePlanCalculator {
	
	protected Map<Integer, List<Game>> gameRounds = new HashMap<Integer, List<Game>>();
	
	public abstract Map<Integer, List<Game>> generateGamePlan(List<Player> players,  int numberOfRounds, int numberOfCourts, int minimumOpponentCount, int trials) throws GameFailedException;

	protected void reset() {
		
	}
	protected List<Player> clonePlayers(List<Player> players) {
		return players.stream().collect(toList());
	}

	protected Player getPlayerWithMinMatchCount(List<Player> players) {
		List<Player> playersWithMinMatchCount = getPlayersWithMinMatchCount(players);
		return playersWithMinMatchCount.get(ThreadLocalRandom.current().nextInt(0, playersWithMinMatchCount.size()));
	}

	protected List<Player> getPlayersWithMinMatchCount(List<Player> players) {
		int minMatchCount = getMinMatchCount(players);
		return players.stream().filter(player -> player.getGameCount() == minMatchCount).toList();
	}

	protected List<Player> randomizePlayers(List<Player> players) {
		List<Player> randomizedPlayers = new ArrayList<>();
		while (!players.isEmpty()) {
			int randomIndex = ThreadLocalRandom.current().nextInt(0, players.size());
			randomizedPlayers.add(players.get(randomIndex));
			players.remove(randomIndex);
		}
		return randomizedPlayers;
	}

	protected int getMinMatchCount(List<Player> players) {
		int minGameCount = 100;
		for (Player player : players) {
			if (player.getGameCount() < minGameCount) {
				minGameCount = player.getGameCount();
			}
		}
		return minGameCount;
	}
}
