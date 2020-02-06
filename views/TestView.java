package com.hal0160.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.hal0160.Application;
import com.hal0160.util.Constants;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

public class TestView extends View {

	// Tracking selected tests
	private final Map<String, Boolean> selection = new HashMap<>();
	
	// Constructor
    public TestView() {
    	// List view
        ListView<String> listView = new ListView<String>();
        listView.setItems(FXCollections.observableArrayList(Application.TestProvider.asList()));
        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
        	@Override
        	public ObservableValue<Boolean> call(String item) {
        		BooleanProperty property = new SimpleBooleanProperty();
        		property.addListener((observable, wasChecked, isChecked) -> {
        			selection.put(item, isChecked);
        		});
        		
        		return property;
        	}
        }));
        
        this.setCenter(listView);    
    }

    @Override
    protected void updateAppBar(AppBar bar) {
        bar.setTitleText(Constants.VIEWLABEL_2);
        bar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getApplication().getDrawer().open()));
        
        Button button = new Button();
        button.setText("Run");
        button.setOnAction(event -> {
        	ProgressView v = (ProgressView) getApplication().switchView(Application.VIEW_3).get();
        	
        	List<String> tests = new ArrayList<>();
        	for (String name : Application.TestProvider.asList()) {
        		if (this.selection.getOrDefault(name, false)) {
        			tests.add(name);
        		}
        	}
        	
        	v.runTests(tests);
        });
        
        bar.getActionItems().add(button);
    }
    
}
