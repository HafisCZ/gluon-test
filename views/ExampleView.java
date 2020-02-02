package com.hal0160.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.hal0160.examples.ExampleProvider.Example;

public class ExampleView extends View {

    public ExampleView() {
       
    }

    @Override
    protected void updateAppBar(AppBar bar) {
    	bar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> getApplication().switchToPreviousView()));
    }
    
    public void showExample(Example example) {
    	setCenter(example.Content);
    	MobileApplication.getInstance().getAppBar().setTitleText(example.Name);
    }
    
}

