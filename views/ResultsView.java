package com.hal0160.views;

import java.util.ArrayList;
import java.util.List;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.hal0160.tests.Result;
import com.hal0160.util.Constants;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class ResultsView extends View {
	
	private final GridPane grid = new GridPane();
	private final Label timeMin = new Label();
	private final Label timeMax = new Label();
	
	private final VBox linesBox = new VBox();
	private final List<Rectangle> lines = new ArrayList<>(); 
	
    public ResultsView() {
		this.grid.setPadding(new Insets(5, 10, 5, 10));

		this.grid.add(this.timeMin, 0, 1);
		this.grid.add(this.timeMax, 1, 1);
		
		ColumnConstraints constraints = new ColumnConstraints();
		constraints.setFillWidth(true);
		constraints.setPercentWidth(50);
		this.grid.getColumnConstraints().addAll(constraints, constraints);
		
		GridPane.setHalignment(this.timeMin, HPos.LEFT);
		GridPane.setHalignment(this.timeMax, HPos.RIGHT);
		
		this.linesBox.setSpacing(1);
		this.linesBox.setPadding(new Insets(0, 3, 0, 3));
		
		// Add to layout
		setTop(this.grid);
		setCenter(this.linesBox);
		
		for (int i = 0; i < 100; i++) {
			Rectangle rect = new Rectangle();
			rect.setFill(Constants.FILL);
			
			this.lines.add(rect);
		}
		
		this.linesBox.getChildren().addAll(this.lines);
    }
    
    @Override
    protected void updateAppBar(AppBar bar) {
        bar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> getApplication().switchToPreviousView()));
    }
    
    public void showResult(Result r) {
     	MobileApplication.getInstance().getAppBar().setTitleText(r.Test);

		this.timeMin.setText(String.format("%.3f", r.Min));
		this.timeMax.setText(String.format("%.3f", r.Max));

		for (int i = 0; i < 100; i++) {
			double width = r.Values.get(i) / r.Max;
			this.lines.get(i).setWidth(width * this.linesBox.getWidth() - 6);
			this.lines.get(i).setHeight(this.linesBox.getHeight() / 130.0);
		}
    }
}