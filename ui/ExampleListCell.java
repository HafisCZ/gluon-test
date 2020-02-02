package com.hal0160.ui;

import com.hal0160.examples.ExampleProvider.Example;

import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ExampleListCell extends ListCell<Example> {

	private final VBox box = new VBox();
	private final Text name = new Text();
	private final Text description = new Text();
	
	public ExampleListCell() {
		this.name.setFont(Font.font(15));
		this.description.setFont(Font.font(11));
		
		this.box.getChildren().addAll(this.name, this.description);
	}
	
	@Override
	public void updateItem(Example example, boolean empty) {
		super.updateItem(example, empty);
		if (empty) {
			this.clear();
		} else {
			this.set(example);
		}
	}
	
	private void clear() {
		this.setText(null);
		this.setGraphic(null);
	}
	
	private void set(Example example) {
		this.setText(null);
		
		this.name.setText(example.Name);
		this.description.setText(example.Description);
		
		this.setGraphic(this.box);
	}
	
}
