package org.hoeppner.cup.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.hoeppner.cup.PlannerController;
import org.hoeppner.cup.PlannerProprties;
import org.hoeppner.cup.model.Player;

public class PlayerComposite extends Composite {

	final Text numberOfCourtsText;
	final Text numberOfRoundsText;
	final Text playerText;
	final Button playerAddButton;
	final List playerList;
	MenuItem playerRemoveMenuItem;
	final Button generateGameButton;
	
	public PlayerComposite(Composite parent, PlannerProprties plannerProprties, PlannerController plannerController) {
		super(parent, SWT.None);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		this.setLayout(gridLayout);

		final Label courtCountLabel = new Label(this, SWT.HORIZONTAL);
		courtCountLabel.setText(plannerProprties.getSetupCourtCount());

		numberOfCourtsText = new Text(this, SWT.SINGLE | SWT.BORDER);
		numberOfCourtsText.setText(String.valueOf(plannerController.getGamePlan().getNumberOfCourts()));
		numberOfCourtsText.addFocusListener(plannerController);
		
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		numberOfCourtsText.setLayoutData(gridData);

		final Label gameCountLabel = new Label(this, SWT.HORIZONTAL);
		gameCountLabel.setText(plannerProprties.getSetupRoundCount());

		numberOfRoundsText = new Text(this, SWT.SINGLE | SWT.BORDER);
		numberOfRoundsText.setText(String.valueOf(plannerController.getGamePlan().getNumberOfRounds()));
		numberOfRoundsText.addFocusListener(plannerController);
		
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		numberOfRoundsText.setLayoutData(gridData);
		
		final Label spielernameLabel = new Label(this, SWT.HORIZONTAL);
		spielernameLabel.setText(plannerProprties.getSetupName());
		
		playerText = new Text(this, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		playerText.setLayoutData(gridData);

		playerAddButton = new Button(this, SWT.PUSH);
		playerAddButton.setText(plannerProprties.getSetupAdd());
		playerAddButton.addSelectionListener(plannerController);
		
		playerList = new List(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		playerList.setLayoutData(gridData);
		for (Player player : plannerController.getGamePlan().getPlayers()) {
			playerList.add(player.getName());
		}

		generateGameButton = new Button(this, SWT.PUSH);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		generateGameButton.setText(plannerProprties.getSetupGenerateGamePlan());
		generateGameButton.setLayoutData(gridData);
		generateGameButton.addSelectionListener(plannerController);

		final Menu menu = new Menu(playerList);
		playerList.setMenu(menu);
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				int selected = playerList.getSelectionIndex();

				if (selected < 0 || selected >= playerList.getItemCount())
					return;

				MenuItem[] items = menu.getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}
				playerRemoveMenuItem = new MenuItem(menu, SWT.NONE);
				playerRemoveMenuItem.setText(plannerProprties.getSetupDelete());
				playerRemoveMenuItem.addSelectionListener(plannerController);
			}
		});
		
	}

	public Text getNumberOfRoundsText() {
		return numberOfRoundsText;
	}

	public Text getNumberOfCourtsText() {
		return numberOfCourtsText;
	}

	public Text getPlayerText() {
		return playerText;
	}
	
	public Button getPlayerAddButton() {
		return playerAddButton;
	}

	public List getPlayerList() {
		return playerList;
	}
	
	public MenuItem getPlayerRemoveMenuItem() {
		return playerRemoveMenuItem;
	}
	
	public Button getGenerateGameButton() {
		return generateGameButton;
	}
}
