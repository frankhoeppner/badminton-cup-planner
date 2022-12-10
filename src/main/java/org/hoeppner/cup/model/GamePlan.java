package org.hoeppner.cup.model;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class GamePlan {

	private static final String GAME_PLAN_FILE = "C:\\badminton\\game-plan.json";

	private List<Player> players = new ArrayList<Player>();
	private int numberOfCourts = 0;
	private int numberOfRounds = 0;
	private int minimumOpponentCount = 0;
	private int trials = 1000;
	private Map<Integer, List<Game>> gameRounds = new HashMap<Integer, List<Game>>();
//	private transient GamePlanCalculator gamePlanCalculator = new RandomGamePlanCalculator();
	private transient GamePlanCalculator gamePlanCalculator = new OptimizingGamePlanCalculator();
	
	public GamePlan() {
	}

	public Player getPlayer(Player search) {
		for (Player player : players) {
			if (player.equals(search)) {
				return player;
			}
		}
		return null;
	}

	public List<Player> getPlayers() {
		Collections.sort(players, Comparator.comparing(Player::getName));
		List<Player> clonedPlayers = players.stream().collect(toList());
		return clonedPlayers;
	}

	public void updatePlayers() {
		for (Player player : players) {
			player.setNumberOfRounds(numberOfRounds);
		}
	}

	public List<Player> getPlayersByScore() {
		Collections.sort(players, Comparator.comparing(Player::getScore).reversed());
		List<Player> clonedPlayers = players.stream().collect(toList());
		return clonedPlayers;
	}

	public List<Player> getPlayersByWeightedScore() {
		Collections.sort(players, new PlayerScoreComparator());
		List<Player> clonedPlayers = players.stream().collect(toList());
		return clonedPlayers;
	}

	public boolean addPlayer(String name) {
		gameRounds = new HashMap<Integer, List<Game>>();
		boolean isNewPlayer = true;
		for (Player player : players) {
			if (player.getName().equals(name)) {
				isNewPlayer = false;
				System.out.println("Spieler " + name + " existiert bereits!");
			}
		}
		if (isNewPlayer) {
			System.out.println("Spieler " + name + " hinzugefügt!");
			players.add(new Player(name));
		}
		return isNewPlayer;
	}

	public void removePlayer(String name) {
		gameRounds = new HashMap<Integer, List<Game>>();
		for (Player player : players) {
			if (player.getName().equals(name)) {
				players.remove(player);
				System.out.println("Spieler " + name + " entfernt!");
				break;
			}
		}
	}

	public void generateGamePlan() throws GameFailedException {
		this.gameRounds = gamePlanCalculator.generateGamePlan(players, numberOfRounds, numberOfCourts, minimumOpponentCount, trials);
	}
	
	public int getNumberOfCourts() {
		return numberOfCourts;
	}

	public void setNumberOfCourts(int numberOfCourts) {
		this.numberOfCourts = numberOfCourts;
	}

	public int getNumberOfRounds() {
		return numberOfRounds;
	}

	public void setNumberOfRounds(int numberOfRounds) {
		this.numberOfRounds = numberOfRounds;
	}

	public int getMinimumOpponentCount() {
		return minimumOpponentCount;
	}

	public void setMinimumOpponentCount(int minimumOpponentCount) {
		this.minimumOpponentCount = minimumOpponentCount;
	}

	public int getTrials() {
		return trials;
	}

	public void setTrials(int trials) {
		this.trials = trials;
	}

	public List<Game> getRound(int round) {
		return gameRounds.get(round);
	}

	public List<Game> getAllGames() {
		List<Game> games = new ArrayList<>();
		for (int round = 1; round <= numberOfRounds; round++) {
			List<Game> gamesInRound = getRound(round);
			if (gamesInRound != null) {
				for (Game game : gamesInRound) {
					games.add(game);
				}
			}
		}
		return games;
	}

	public void calculateResults() {
		for (Player player : players) {
			player.setScore(0);
		}
		if (!gameRounds.isEmpty()) {
			for (Game game : getAllGames()) {
				getPlayer(game.getTeam1Members().get(0)).addScore(game.getTeam1Points());
				getPlayer(game.getTeam1Members().get(1)).addScore(game.getTeam1Points());
				getPlayer(game.getTeam2Members().get(0)).addScore(game.getTeam2Points());
				getPlayer(game.getTeam2Members().get(1)).addScore(game.getTeam2Points());
			}
		}
	}
	
	public static GamePlan load() throws IOException {
		File gamePlanFile = new File(GAME_PLAN_FILE);
		if (gamePlanFile.exists()) {
			try (Reader reader = new FileReader(gamePlanFile)) {
				System.out.println("Lade Spielplan...");
				GamePlan gamePlan = new Gson().fromJson(reader, GamePlan.class);
				gamePlan.updatePlayers();
				return gamePlan;
			}
		} else {
			System.out.println("Kein Spielplan vorhanden, erzeuge neuen Spielplan...");
		}
		return new GamePlan();
	}

	public void save() throws IOException {
		File gamePlanFile = new File(GAME_PLAN_FILE);
		if (!gamePlanFile.exists()) {
			gamePlanFile.getParentFile().mkdirs();
			gamePlanFile.createNewFile();
		}
		try (Writer writer = new FileWriter(gamePlanFile)) {
			new Gson().toJson(this, writer);
			System.out.println("Spielplan gesichert!");
		}
	}
	
	public void dump() {
		System.out.println("===================================================================================================");
		System.out.println("Gameplan:");
		System.out.println("---------------------------------------------------------------------------------------------------");
		for (Player player : players) {
			player.dump();
		}
		System.out.println("===================================================================================================");
	}
}
