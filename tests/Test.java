package com.hal0160.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hal0160.util.Utils.Func1;

public abstract class Test {

	private final int iterations;
	protected int divisions;

	protected Test (int iterations) {
		this.iterations = iterations;
		this.divisions = 1;
	}
	
	public int Count() {
		return this.iterations;
	}
	
	public int Parts() {
		return this.divisions;
	}
	
	public Result execute (Func1<List<Double>> callback, Func1<Exception> exceptionCallback) {
		try {
			double startTime, endTime;
			List<Double> measurements = new ArrayList<Double>(this.iterations);
			
			this.prepare();
			
			for (int i = 0; i < this.iterations; i++) {
				startTime = System.nanoTime();
				
				// Run test
				this.test();
				
				endTime = System.nanoTime();
				measurements.add((endTime - startTime) / (double) this.divisions);
				
				// Progress callback
				if (Objects.nonNull(callback)) {
					callback.accept(measurements);
				}
			}
			
			// Return results
			return new Result(measurements);
		} catch (Exception exception) {
			// Alert app
			if (Objects.nonNull(exceptionCallback)) {
				exceptionCallback.accept(exception);
			}
			
			// Return nothing
			return null;
		}
	}
	
	protected void prepare() throws Exception {
		
	}
	
	protected abstract void test() throws Exception;
	
}
