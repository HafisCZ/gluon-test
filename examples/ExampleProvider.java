package com.hal0160.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Node;

public class ExampleProvider {

	private final Map<String, Example> examples = new HashMap<>();
	private final List<Example> insertionOrder = new ArrayList<>();
	
	public class Example {
		
		public final String Name;
		public final String Description;
		public final Node Content;
		
		private Example (String name, String description, Node content) {
			this.Name = name;
			this.Description = description;
			this.Content = content;
		}
		
	}
	
	public ExampleProvider() {
		
	}
	
	public void register(String name, String description, Node node) {
		Example example = new Example(name, description, node);
		
		this.examples.put(name, example);
		this.insertionOrder.add(example);
	}
	
	public List<Example> asExampleList() {
		return this.insertionOrder;
	}
	
	public Example getExample(String name) {
		return this.examples.getOrDefault(name, null);
	}
	
}