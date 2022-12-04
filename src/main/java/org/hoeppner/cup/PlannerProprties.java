package org.hoeppner.cup;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PlannerProprties {

	private Properties properties = null;

	public PlannerProprties(String language) throws IOException {
		this.properties = loadProperties(language);
	}
	
	public String getPlannerWindowTitle() {
		return properties.getProperty("planner.title");
	}
	
	public String getPlannerTabSetup() {
		return properties.getProperty("planner.setup");
	}
	
	public String getPlannerTabRound() {
		return properties.getProperty("planner.round");
	}
	
	public String getPlannerTabResult() {
		return properties.getProperty("planner.result");
	}
	
	public String getSetupCourtCount() {
		return properties.getProperty("setup.courtCount");
	}
	
	public String getSetupRoundCount() {
		return properties.getProperty("setup.roundCount");
	}
	
	public String getSetupMinimumOpponentCount() {
		return properties.getProperty("setup.minimumOpponentCount");
	}
	
	public String getSetupTrials() {
		return properties.getProperty("setup.trials");
	}
	
	public String getSetupMaxGames() {
		return properties.getProperty("setup.maxGames");
	}
	
	public String getSetupName() {
		return properties.getProperty("setup.name");
	}
	
	public String getSetupAdd() {
		return properties.getProperty("setup.add");
	}
	
	public String getSetupDelete() {
		return properties.getProperty("setup.delete");
	}

	public String getSetupGenerateGamePlan() {
		return properties.getProperty("setup.generateGamePlan");
	}

	public String getRoundCourt() {
		return properties.getProperty("round.court");
	}

	public String getResultName() {
		return properties.getProperty("result.name");
	}

	public String getResultGameCount() {
		return properties.getProperty("result.gameCount");
	}

	public String getResultPoints() {
		return properties.getProperty("result.points");
	}

	public String getResultWeightPoints() {
		return properties.getProperty("result.weightPoints");
	}

	public String getResultNumberOpponents() {
		return properties.getProperty("result.numberOpponents");
	}

	public String getWarningTitle() {
		return properties.getProperty("planner.warning.title");
	}

	public String getWarningPlayerAlreadyExists() {
		return properties.getProperty("planner.warning.playerAlreadyExists");
	}

	public String getWarningNoGamePlanFound() {
		return properties.getProperty("planner.warning.noGameplanFound");
	}

	private Properties loadProperties(String language) throws IOException {
		String propertiesPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesPath + "/" + language + ".properties"));
		return properties;
	}

}
