package org.hoeppner.cup.model;

import java.util.Comparator;

public class PlayerScoreComparator implements Comparator<Player> {

	@Override
	public int compare(Player player1, Player player2) {
		int result = player1.getWeightedScore().compareTo(player2.getWeightedScore());
		if (result != 0) {
			return -result;
		}
		return player1.getScore() - player2.getScore();
	}

}
