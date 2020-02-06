package com.hal0160.tests;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hal0160.util.Utils;

public class Result {

	public final List<Double> Values;
	public final int Count;
	public String Test;
	
	public final Double Min;
	public final Double Max;
	public final Double Average;
	public final Double Median;
	
	public Result (List<Double> measurements) {
		this.Values = measurements.stream().map(x -> x / 1E3).collect(Collectors.toList());
		this.Count = measurements.size();
		
		this.Min = Collections.min(measurements) / 1E3;
		this.Max = Collections.max(measurements) / 1E3;

		double sum = measurements.stream().reduce(0D, (a, b) -> a + b);
		this.Average = sum / (double) measurements.size() / 1E3;
		
		this.Median = Utils.median(measurements) / 1E3;
	}
	
	public Result withName(String name) {
		this.Test = name;
		return this;
	}
	
}
