package com.hal0160.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.hal0160.Application;
import com.hal0160.examples.ExampleProvider.Example;
import com.hal0160.ui.ExampleListCell;
import com.hal0160.util.Constants;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class PrimaryView extends View {

    public PrimaryView() {    	
    	// List view
    	ListView<Example> list = new ListView<Example>(FXCollections.observableArrayList(Application.ExampleProvider.asExampleList()));
    	list.setCellFactory(c -> {
    		ListCell<Example> cell = new ExampleListCell();
    		cell.setOnMouseClicked(event -> {
    			if (!cell.isEmpty()) {
    				ExampleView view = (ExampleView) getApplication().switchView(Application.VIEW_5).get();
    				view.showExample(cell.getItem());
    				event.consume();
    			}
    		});
    		
    		return cell;
    	});
    	
        this.setCenter(list);  
    }

    @Override
    protected void updateAppBar(AppBar bar) {
    	bar.setTitleText(Constants.VIEWLABEL_1);
    	bar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getApplication().getDrawer().open()));
    }
    
}
