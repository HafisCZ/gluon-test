package com.hal0160;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Header;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.Swatch;
import com.hal0160.examples.ExampleProvider;
import com.hal0160.tests.TestProvider;
import com.hal0160.tests.implementations.Ackermann;
import com.hal0160.tests.implementations.Audio;
import com.hal0160.tests.implementations.ReadFile;
import com.hal0160.tests.implementations.ReadPrefs;
import com.hal0160.tests.implementations.Vibration;
import com.hal0160.tests.implementations.WriteFile;
import com.hal0160.tests.implementations.WritePrefs;
import com.hal0160.util.Constants;
import com.hal0160.util.Utils;
import com.hal0160.views.ExampleView;
import com.hal0160.views.PrimaryView;
import com.hal0160.views.ProgressView;
import com.hal0160.views.ResultsView;
import com.hal0160.views.TestView;

import javafx.scene.Scene;
import javafx.scene.text.Text;

public class Application extends MobileApplication {

    public static final String VIEW_1 = HOME_VIEW;
    public static final String VIEW_2 = "screenTests";
    public static final String VIEW_3 = "screenProgress";
    public static final String VIEW_4 = "screenResults";
    public static final String VIEW_5 = "screenExamples";
    
    public static final TestProvider TestProvider = new TestProvider();
    public static final ExampleProvider ExampleProvider = new ExampleProvider();
    
    @Override
    public void init() {
    	// Add views
        addViewFactory(VIEW_1, PrimaryView::new);
        addViewFactory(VIEW_2, TestView::new);
        addViewFactory(VIEW_3, ProgressView::new);
        addViewFactory(VIEW_4, ResultsView::new);
        addViewFactory(VIEW_5, ExampleView::new);

        // Create and setup drawers
		NavigationDrawer drawer = this.getDrawer();
		drawer.setHeader(new Header(Constants.STR_HEADER, Constants.STR_SUBHEADER));
		drawer.getItems().addAll(
			new ViewItem(Constants.VIEWLABEL_1, null, VIEW_1, ViewStackPolicy.SKIP),
			new ViewItem(Constants.VIEWLABEL_2, null, VIEW_2)
		);
		
		// Register all tests
		TestProvider.register("Audio", Audio.class, new Object[] { 100 });
		TestProvider.register("Vibration", Vibration.class, new Object[] { 100 });
		TestProvider.register("Ackermann 3 9", Ackermann.class, new Object[] { 100, 3, 9 });
		TestProvider.register("Ackermann 3 11", Ackermann.class, new Object[] { 100, 3, 11 });
		TestProvider.register("Write 16B file", WriteFile.class, new Object[] { 100, Utils.TEXT_16B, 1000 });
		TestProvider.register("Write 1KB file", WriteFile.class, new Object[] { 100, Utils.TEXT_1KB, 1000 });
		TestProvider.register("Read 16B file", ReadFile.class, new Object[] { 100, Utils.TEXT_16B, 1000 });
		TestProvider.register("Read 1KB file", ReadFile.class, new Object[] { 100, Utils.TEXT_1KB, 1000 });
		TestProvider.register("Write 16B setting", WritePrefs.class, new Object[] { 100, Utils.TEXT_16B, 1000 });
		TestProvider.register("Write 1KB setting", WritePrefs.class, new Object[] { 100, Utils.TEXT_1KB, 1000 });
		TestProvider.register("Read 16B setting", ReadPrefs.class, new Object[] { 100, Utils.TEXT_16B, 1000 });
		TestProvider.register("Read 1KB setting", ReadPrefs.class, new Object[] { 100, Utils.TEXT_1KB, 1000 });
		
		// Register all examples
		ExampleProvider.register("Hello world", "Show centered text", new Text("Hello world!"));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);
        scene.getStylesheets().add(Application.class.getResource("style.css").toExternalForm());
    }
}