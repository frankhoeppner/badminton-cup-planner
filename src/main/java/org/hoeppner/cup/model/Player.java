package org.hoeppner.cup.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Player {

	private String name;
	private int gameCount;
	private int score;
	private List<String> partners = new ArrayList<>();
	private Map<String, Integer> opponents = new HashMap<>();
	
	public Player() {
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getGameCount() {
		return gameCount;
	}

	public int getOpponentCount() {
		return opponents.size();
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public void reset() {
		opponents.clear();
		partners.clear();
		gameCount = 0;
		score = 0;
	}
	
	public void playsWith(Player player) {
		partners.add(player.getName());
		player.partners.add(name);
		gameCount++;
		player.gameCount++;
	}
	
	public void playsAgainst(Player player1, Player player2) {
		if (opponents.get(player1.getName()) == null) {
			opponents.put(player1.getName(), 1);
		} else {
			opponents.put(player1.getName(), opponents.get(player1.getName()) + 1);
		}
		if (opponents.get(player2.getName()) == null) {
			opponents.put(player2.getName(), 1);
		} else {
			opponents.put(player2.getName(), opponents.get(player2.getName()) + 1);
		}
	}
	
	public boolean hasPlayedWith(Player player) {
		return partners.contains(player.getName());
	}
	
	public int playedAgainst(Player player) {
		if (opponents.containsKey(player.getName())) {
			return opponents.get(player.getName());
		}
		return 0;
	}
	
	public boolean equals(Player other) {
		if (other == null) {
			return false;
		}
		return name.equals(other.name);
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
}
