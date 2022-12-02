package org.hoeppner.cup.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

	public static int MAX_PLAY = 2;
	
	private List<Player> team1 = new ArrayList<Player>();
	private List<Player> team2 = new ArrayList<Player>();
	private int team1Points = 0;
	private int team2Points = 0;

	public Game() {
	}
	
	public Game(List<Player> players) {
		team1.add(players.get(0));
		team1.add(players.get(1));
		team2.add(players.get(2));
		team2.add(players.get(3));
	}

	public boolean addPlayer(Player player) {
		if (team1.isEmpty()) {
			team1.add(player);
			return true;
		} else if (team1.size() == 1 && !team1.get(0).hasPlayedWith(player) && maxOpponentCount(player, team2) < MAX_PLAY) {
			team1.add(player);
			return true;
		} else if (team2.size() == 0 && maxOpponentCount(player, team1) < MAX_PLAY) {
			team2.add(player);
			return true;
		} else if (team2.size() == 1 && !team2.get(0).hasPlayedWith(player) && maxOpponentCount(player, team1) < MAX_PLAY){
			team2.add(player);
			return true;
		}
		return false;
	}
	
	public boolean isComplete() {
		return team1.size() == 2 && team2.size() == 2;
	}
	
	private int maxOpponentCount(Player player, List<Player> team) {
		int maxOpponentCount = 0;
		for (Player teamPlayer : team) {
			if (player.playedAgainst(teamPlayer) > maxOpponentCount) {
				maxOpponentCount = player.playedAgainst(teamPlayer);
			}
		}
		return maxOpponentCount;
	}
	
	public String getTeam1() {
		return team1.get(0).getName() + " && " + team1.get(1).getName();
	}

	public List<Player> getTeam1Members() {
		return team1;
	}

	public String getTeam2() {
		return team2.get(0).getName() + " && " + team2.get(1).getName();
	}

	public List<Player> getTeam2Members() {
		return team2;
	}

	public int getTeam1Points() {
		return team1Points;
	}

	public void setTeam1Points(int team1Points) {
		this.team1Points = team1Points;
	}

	public int getTeam2Points() {
		return team2Points;
	}

	public void setTeam2Points(int team2Points) {
		this.team2Points = team2Points;
	}
	
	public void play() {
		team1.get(0).playsWith(team1.get(1));
		team1.get(0).playsAgainst(team2.get(0), team2.get(1));
		team1.get(1).playsAgainst(team2.get(0), team2.get(1));
		team2.get(0).playsWith(team2.get(1));
		team2.get(0).playsAgainst(team1.get(0), team1.get(1));
		team2.get(1).playsAgainst(team1.get(0), team1.get(1));
	}
	
	public void updatePlayerScore() {
		team1.get(0).setScore(team1.get(0).getScore() + team1Points);
		team1.get(1).setScore(team1.get(1).getScore() + team1Points);
		team2.get(0).setScore(team2.get(0).getScore() + team2Points);
		team2.get(1).setScore(team2.get(1).getScore() + team2Points);
	}
	
	public String toString() {
		return getTeam1() + " vs " + getTeam2();
	}
}
