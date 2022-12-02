package org.hoeppner.cup.view;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.hoeppner.cup.PlannerController;
import org.hoeppner.cup.PlannerProprties;
import org.hoeppner.cup.model.Game;

public class GameComposite extends Composite {

	private PlannerProprties plannerProprties = null;
	private PlannerController plannerController;
	private int gameNumber;
	
	public GameComposite(Composite parent, PlannerProprties plannerProprties, PlannerController plannerController, int gameNumber) {
		super(parent, SWT.None);
		this.plannerProprties = plannerProprties;
		this.plannerController = plannerController;
		this.gameNumber = gameNumber;
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		this.setLayout(gridLayout);

		int courtNumber = 0;
		
		for (Game game : plannerController.getGamePlan().getRound(gameNumber)) {
			courtNumber++;
			final Group court1 = new Group(this, SWT.SHADOW_ETCHED_IN);
			gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			court1.setLayout(gridLayout);
			court1.setText(plannerProprties.getRoundCourt() + " " + courtNumber);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			court1.setLayoutData(gridData);
			
			final Label team1 = new Label(court1, SWT.HORIZONTAL);
			team1.setText(game.getTeam1());
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			team1.setLayoutData(gridData);
			
			final Text team1Result = new Text(court1, SWT.SINGLE | SWT.BORDER);
			team1Result.setText(String.valueOf(game.getTeam1Points()));
			gridData = new GridData();
			gridData.widthHint = 20;
			team1Result.setLayoutData(gridData);
			team1Result.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent arg0) {
					try {
						game.setTeam1Points(Integer.parseInt(team1Result.getText()));
						plannerController.getGamePlan().save();
					} catch (NumberFormatException | IOException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void focusGained(FocusEvent arg0) {
				}
			});
			
			final Label team2 = new Label(court1, SWT.HORIZONTAL);
			team2.setText(game.getTeam2());
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			team2.setLayoutData(gridData);

			final Text team2Result = new Text(court1, SWT.SINGLE | SWT.BORDER);
			team2Result.setText(String.valueOf(game.getTeam2Points()));
			gridData = new GridData();
			gridData.widthHint = 20;
			team2Result.setLayoutData(gridData);
			team2Result.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent arg0) {
					try {
						game.setTeam2Points(Integer.parseInt(team2Result.getText()));
						plannerController.getGamePlan().save();
					} catch (NumberFormatException | IOException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void focusGained(FocusEvent arg0) {
				}
			});
		}
		
	}

}
