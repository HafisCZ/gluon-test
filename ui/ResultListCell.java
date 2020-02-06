package com.hal0160.ui;

import com.hal0160.tests.Result;
import com.hal0160.util.Utils;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class ResultListCell extends ListCell<Result> {

	private final GridPane grid = new GridPane();
	private final Label name = new Label();
	
	private final Label timeMin = new Label();
	private final Label timeMax = new Label();
	private final Label timeAverage = new Label();
	private final Label timeMedian = new Label();
	
	public ResultListCell() {
		this.grid.setHgap(5);
		this.grid.setVgap(5);
		this.grid.setPadding(new Insets(0, 10, 0, 10));
		
		this.grid.add(this.name, 0, 0, 2, 1);
		this.grid.add(this.timeMin, 0, 1);
		this.grid.add(this.timeMax, 1, 1);
		this.grid.add(this.timeAverage, 0, 2);
		this.grid.add(this.timeMedian, 1, 2);
		
		ColumnConstraints constraints = new ColumnConstraints();
		constraints.setFillWidth(true);
		constraints.setPercentWidth(50);

		this.grid.getColumnConstraints().addAll(constraints, constraints);
		
		this.name.setFont(Font.font(15));
	}
	
	@Override
	public void updateItem(Result result, boolean empty) {
		super.updateItem(result, empty);
		if (empty) {
			this.clear();
		} else {
			this.set(result);
		}
	}
	
	private void clear() {
		this.setText(null);
		this.setGraphic(null);
	}
	
	private void set(Result result) {
		this.setText(null);
		
		this.name.setText(result.Test);
		
		this.timeMin.setText(Utils.format("Min", result.Min));
		this.timeMax.setText(Utils.format("Max", result.Max));
		
		this.timeAverage.setText(Utils.format("Avg", result.Average));
		this.timeMedian.setText(Utils.format("Med", result.Median));
		
		this.setGraphic(this.grid);
	}
	
}
