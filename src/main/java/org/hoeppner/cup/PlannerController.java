package org.hoeppner.cup;

import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MessageBox;
import org.hoeppner.cup.model.GameFailedException;
import org.hoeppner.cup.model.GamePlan;
import org.hoeppner.cup.view.PlannerWindow;

public class PlannerController implements SelectionListener, FocusListener {

	private static final String LANGUAGE_DE = "de";
	
	private GamePlan gamePlan = null;
	private PlannerWindow plannerWindow = null;
	private PlannerProprties plannerProprties = null;
	
	PlannerController() throws IOException {
		gamePlan = GamePlan.load();
		this.plannerProprties = new PlannerProprties(LANGUAGE_DE);
		this.plannerWindow = new PlannerWindow(this, plannerProprties);
	}
	
	void start() {
		plannerWindow.show();
	}

	public GamePlan getGamePlan() {
		return gamePlan;
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent event) {
		try {
			if (plannerWindow == null) {
				return;
			}
			if (event.getSource() == plannerWindow.getPlayerAddButton()) {
				if (!plannerWindow.getPlayerText().getText().trim().isEmpty()) {
					if (gamePlan.addPlayer(plannerWindow.getPlayerText().getText())) {
						gamePlan.save();
						plannerWindow.getPlayerList().add(plannerWindow.getPlayerText().getText());
						plannerWindow.getPlayerText().setText("");
						plannerWindow.updateView();
					} else {
						MessageBox box = new MessageBox(plannerWindow.getActiveShell(), SWT.OK);
						box.setText(plannerProprties.getWarningTitle());
						box.setMessage(MessageFormat.format(plannerProprties.getWarningPlayerAlreadyExists(), plannerWindow.getPlayerText().getText()));
						box.open();
					}
				}
			} else if (event.getSource() == plannerWindow.getGenerateGameButton()) {
				try {
					gamePlan.generateGamePlan();
					gamePlan.save();
					plannerWindow.updateView();
				} catch (GameFailedException e) {
					e.printStackTrace();
					MessageBox box = new MessageBox(plannerWindow.getActiveShell(), SWT.OK);
					box.setText(plannerProprties.getWarningTitle());
					box.setMessage(plannerProprties.getWarningNoGamePlanFound());
					box.open();
				}
			} else if (event.getSource() == plannerWindow.getPlayerRemoveMenuItem()) {
				int selectedPlayerIndex = plannerWindow.getPlayerList().getSelectionIndex();
				gamePlan.removePlayer(plannerWindow.getPlayerList().getItem(selectedPlayerIndex));
				gamePlan.save();
				plannerWindow.getPlayerList().remove(selectedPlayerIndex);
				plannerWindow.updateView();
//				plannerWindow.removeGameTabs();
			} else if (event.getSource() == plannerWindow.getTabFolder()) {
				gamePlan.calculateResults();
				plannerWindow.refreshResultView();
			} else {
				System.out.println("No Handler found: " + event.getSource());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void focusGained(FocusEvent event) {
	}

	@Override
	public void focusLost(FocusEvent event) {
		if (event.getSource() == plannerWindow.getNumberOfRoundsText()) {
			try {
				gamePlan.setNumberOfRounds(Integer.parseInt(plannerWindow.getNumberOfRoundsText().getText()));
				gamePlan.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (event.getSource() == plannerWindow.getNumberOfCourtsText()) {
			try {
				gamePlan.setNumberOfCourts(Integer.parseInt(plannerWindow.getNumberOfCourtsText().getText()));
				gamePlan.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (event.getSource() == plannerWindow.getMinimumOpponentCountText()) {
			try {
				gamePlan.setMinimumOpponentCount(Integer.parseInt(plannerWindow.getMinimumOpponentCountText().getText()));
				gamePlan.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (event.getSource() == plannerWindow.getTrialsText()) {
			try {
				gamePlan.setTrials(Integer.parseInt(plannerWindow.getTrialsText().getText()));
				gamePlan.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("No Handler found: " + event.getSource());
		}
	}

	public static void main(String[] args) throws IOException {
		PlannerController controller = new PlannerController();
		controller.start();
	}
}
