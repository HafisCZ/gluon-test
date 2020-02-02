package com.hal0160.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.hal0160.tests.Result;

public class ResultsView extends View {

    public ResultsView() {

    }
    
    @Override
    protected void updateAppBar(AppBar bar) {
        bar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> getApplication().switchToPreviousView()));
    }
    
    public void showResult(Result r) {
     	MobileApplication.getInstance().getAppBar().setTitleText(r.Test);
    }
}