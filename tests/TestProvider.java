package com.hal0160.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.hal0160.util.Tuple;

public class TestProvider {
	
	private final Map<String, Tuple<Object[], Class<? extends Test>>> tests = new HashMap<>();
	private final List<String> insertionOrder = new ArrayList<>();
	
	public TestProvider() {

	}
	
	public <T extends Test> void register(String name, Class<T> target, Object[] parameters) {
		this.tests.put(name, new Tuple<>(parameters, target));
		this.insertionOrder.add(name);
	}
	
	public List<String> asList() {
		return this.insertionOrder;
	}

	public Test getTest(String name) throws Exception {
		Tuple<Object[], Class<? extends Test>> test = this.tests.getOrDefault(name, null);
		if (Objects.nonNull(test)) {
			return (Test) test.second().getConstructors()[0].newInstance(test.first());
		}
		return null;
	}
	
}
