package com.hal0160.util;

import java.util.Collections;
import java.util.List;

import com.hal0160.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Text;

public class Utils {

	public interface Func {
		void accept();
	}
	
	public interface Func1 <T> {
		void accept(T t);
	}
	
	public interface Func2 <T, U> {
		void accept(T t, U u);
	}
	
	public static int ackermann (int m, int n) {
		return m == 0 ? n + 1 : ackermann(m - 1, n == 0 ? 1 : ackermann(m , n - 1));
	}
	
	public static String TEXT_16B = "ABCDEFGHIJKLMNOP"; 
	public static String TEXT_1KB;
	
	static {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 64; i++) {
			builder.append("ABCDEFGHIJKLMNOP");
		}
		
		TEXT_1KB = builder.toString();
	}

	public static double median (List<Double> values) {
		Collections.sort(values);
		if (values.size() % 2 == 0) {
			return ((double) values.get(values.size() / 2 - 1) + (double) values.get(values.size() / 2)) / 2;
		} else {
			return (double) values.get(values.size() / 2);
		}
	}
	
	public static String format(String label, double value) {
		return String.format("%s: %.3f", label, value);
	}
	
	public static Node fromFXML(String resource) {
		try {
			return FXMLLoader.load(Application.class.getResource("examples/" + resource));
		} catch (Exception e) {
			return new Text("Resource failed to load!");
		}
	}
	
}