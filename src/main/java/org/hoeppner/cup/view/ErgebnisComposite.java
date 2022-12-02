package org.hoeppner.cup.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.hoeppner.cup.PlannerController;
import org.hoeppner.cup.PlannerProprties;
import org.hoeppner.cup.model.Player;

public class ErgebnisComposite extends Composite {

	private PlannerController plannerController;
	private final Table table;
	private List<TableColumn> tableColumns = new ArrayList<>();
	
	public ErgebnisComposite(Composite parent, PlannerProprties plannerProprties, PlannerController plannerController) {
		super(parent, SWT.None);
		
		this.plannerController = plannerController;
		
		FillLayout fillLayout = new FillLayout();
		this.setLayout(fillLayout);

		table = new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		tableColumns.add(createColumn(table, plannerProprties.getResultName()));
		tableColumns.add(createColumn(table, plannerProprties.getResultGameCount()));
		tableColumns.add(createColumn(table, plannerProprties.getResultPoints()));
		tableColumns.add(createColumn(table, plannerProprties.getResultWeightPoints()));
		tableColumns.add(createColumn(table, plannerProprties.getResultNumberOpponents()));

		updateView();
	}
	
	private TableColumn createColumn(Table table, String title) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText(title);
		return column;
	}
	
	public void updateView() {
		table.removeAll();
		for (Player player : plannerController.getGamePlan().getPlayersByScore()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, player.getName());
			item.setText(1, String.valueOf(player.getGameCount()));
			item.setText(2, String.valueOf(player.getScore()));
			item.setText(3, String.valueOf(player.getScore() / Math.max(player.getGameCount(), 1)));
			item.setText(4, String.valueOf(player.getOpponentCount()));
		}
		for (TableColumn tableColumn : tableColumns) {
			tableColumn.pack();
		}
	}

}
