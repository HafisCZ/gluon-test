package com.hal0160.views;

import java.util.List;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.ProgressBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.hal0160.Application;
import com.hal0160.tests.Result;
import com.hal0160.tests.Test;
import com.hal0160.ui.ResultListCell;
import com.hal0160.util.Constants;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ProgressView extends View {

	private final ObservableList<Result> items = FXCollections.observableArrayList();
	
	private final DoubleProperty progress = new SimpleDoubleProperty(0);
	private final StringProperty name = new SimpleStringProperty("");
	private final StringProperty remaining = new SimpleStringProperty("");
	private final StringProperty order = new SimpleStringProperty("");
	private final BooleanProperty shown = new SimpleBooleanProperty(true);
	
	private final VBox box = new VBox();
	
    public ProgressView() {
    	// Completed tests
    	ListView<Result> list = new ListView<Result>(items);
    	list.setCellFactory(c -> {
    		ListCell<Result> cell = new ResultListCell();
    		cell.setOnMouseClicked(event -> {
    			if (!cell.isEmpty()) {
    				ResultsView view = (ResultsView) getApplication().switchView(Application.VIEW_4).get();
    				view.showResult(cell.getItem());
    				event.consume();
    			}
    		});
    		
    		return cell;
    	});
    	
    	// Current progress
    	Text orderLabel = new Text();
    	orderLabel.textProperty().bind(this.order);
    	orderLabel.setFont(Font.font(12));
    	
    	Text nameLabel = new Text();
    	nameLabel.textProperty().bind(this.name);
    	nameLabel.setFont(Font.font(15));
    	
    	Text remainingLabel = new Text();
    	remainingLabel.textProperty().bind(this.remaining);
    	remainingLabel.setFont(Font.font(14));
     	
    	ProgressBar progress = new ProgressBar();
    	progress.progressProperty().bind(this.progress);
    	progress.setPrefSize(Double.MAX_VALUE, 10);
    	
    	VBox centered = new VBox();
    	centered.setAlignment(Pos.CENTER);
    	centered.setPadding(new Insets(5, 0, 5, 0));
    	centered.getChildren().addAll(nameLabel, remainingLabel);
    	
    	VBox left = new VBox();
    	left.setAlignment(Pos.BASELINE_LEFT);
    	left.setPadding(new Insets(0, 10, 0, 10));
    	left.getChildren().add(orderLabel);
    	
    	this.box.getChildren().addAll(left, centered, progress);
    	
    	setCenter(list);
    	setBottom(this.box);
    }

    public void runTests(List<String> tests) {
    	Platform.runLater(() -> {
        	this.items.clear();
        	this.progress.set(0);
    	});
    	
    	ThreadGroup threadGroup = new ThreadGroup("tg0");
    	new Thread(threadGroup, () -> {
        	try {
        		Platform.runLater(() -> {
            		setBottom(this.box);
                	this.shown.set(true);
        		});
            	for (String name : tests) {
            		Test test = Application.TestProvider.getTest(name);
            		
            		Platform.runLater(() -> {
            			this.name.set(name);
              			this.order.set((tests.indexOf(name) + 1) + " of " + tests.size());
            		});
            		
            		Result result = test.execute(list -> {
            			Platform.runLater(() -> {
            				double percentage = (double) list.size() / (double) test.Count();
            				
                			this.progress.set(percentage);
                			
                			double time = (double) test.Parts() * list.stream().reduce(Double::sum).get() / 1E9;
                			time /= (double) list.size();
                			time *= (double) (test.Count() - list.size());
                			
                			this.remaining.set("Around " + (int) time + " seconds remaining");
            			});
            		}, e -> {
            			e.printStackTrace();
            		});
            		
            		Platform.runLater(() -> {
                		this.items.add(result.withName(name));
            		});
            	}
        		Platform.runLater(() -> {
            		setBottom(null);
                	this.shown.set(false);
        		});
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
    	}, "th0", 2 * 1024 * 1024).start();
    }
    
    @Override
    protected void updateAppBar(AppBar bar) {
        bar.setTitleText(Constants.VIEWLABEL_3);
        
        Button button = MaterialDesignIcon.ARROW_BACK.button(e -> getApplication().switchToPreviousView());
        button.visibleProperty().bind(this.shown.not());
        bar.setNavIcon(button);
    }
}
