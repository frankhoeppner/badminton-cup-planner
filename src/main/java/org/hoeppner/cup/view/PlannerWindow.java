package org.hoeppner.cup.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.hoeppner.cup.PlannerController;
import org.hoeppner.cup.PlannerProprties;

public class PlannerWindow {

	private PlannerController plannerController = null;
	private PlannerProprties plannerProprties = null;
	private PlayerComposite playerComposite = null;
	private ErgebnisComposite ergebnisComposite = null;
	private Display display = new Display();
	private Shell shell = null;
	private final TabFolder tabFolder;
	private final TabItem itemPlayers;

	public PlannerWindow(PlannerController plannerController, PlannerProprties plannerProprties) {
		this.plannerController = plannerController;
		this.plannerProprties = plannerProprties;
		shell = new Shell(display);
		tabFolder = new TabFolder(shell, SWT.BORDER);
		this.playerComposite = new PlayerComposite(tabFolder, this.plannerProprties, this.plannerController);

		shell.setText(plannerProprties.getPlannerWindowTitle());

		shell.setLayout(new FillLayout());

		Rectangle clientArea = shell.getClientArea();
		tabFolder.setLocation(clientArea.x, clientArea.y);
		tabFolder.addSelectionListener(plannerController);
		
		itemPlayers = new TabItem(tabFolder, SWT.NONE);
		itemPlayers.setText(plannerProprties.getPlannerTabSetup());
		itemPlayers.setControl(this.playerComposite);

		updateView();
		
		this.ergebnisComposite = new ErgebnisComposite(tabFolder, plannerProprties, plannerController);
		TabItem itemResult = new TabItem(tabFolder, SWT.NONE);
		itemResult.setText(plannerProprties.getPlannerTabResult());
		itemResult.setControl(this.ergebnisComposite);

		shell.open();
	}

	private void removeGameTabs() {
		for (TabItem tabItem : tabFolder.getItems()) {
			if (tabItem.getText().startsWith(plannerProprties.getPlannerTabRound()) && tabItem.isDisposed() == false) {
				tabItem.dispose();
			}
		}
	}
	
	public void updateView() {
		updateGamesView();
		tabFolder.redraw();
		tabFolder.pack();
		shell.pack();
		shell.redraw();
		shell.setSize(800, 600);
	}
	
	private void updateGamesView() {
		removeGameTabs();
		for (int i = 1; i <= plannerController.getGamePlan().getNumberOfRounds(); i++) {
			if (plannerController.getGamePlan().getRound(i) != null) {
				TabItem round = new TabItem(tabFolder, SWT.NONE, i);
				round.setText(plannerProprties.getPlannerTabRound() + " " + i);
				round.setControl(new GameComposite(tabFolder, plannerProprties, plannerController, i));
			}
		}
	}
	
	public void refreshResultView() {
		TabItem selectedTab = tabFolder.getItem(tabFolder.getSelectionIndex());
		if (selectedTab.getText().equalsIgnoreCase(plannerProprties.getPlannerTabResult())) {
			this.ergebnisComposite.updateView();
		}
	}

	public Shell getActiveShell() {
		return shell;
	}

	public Text getNumberOfCourtsText() {
		return playerComposite.getNumberOfCourtsText();
	}

	public Text getNumberOfRoundsText() {
		return playerComposite.getNumberOfRoundsText();
	}

	public Text getMinimumOpponentCountText() {
		return playerComposite.getMinimumOpponentCountText();
	}

	public Text getTrialsText() {
		return playerComposite.getTrialsText();
	}

	public Text getPlayerText() {
		return playerComposite.getPlayerText();
	}

	public Button getPlayerAddButton() {
		return playerComposite.getPlayerAddButton();
	}

	public List getPlayerList() {
		return playerComposite.getPlayerList();
	}

	public Button getGenerateGameButton() {
		return playerComposite.getGenerateGameButton();
	}

	public MenuItem getPlayerRemoveMenuItem() {
		return playerComposite.getPlayerRemoveMenuItem();
	}

	public TabFolder getTabFolder() {
		return tabFolder;
	}
	
	public void show() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
