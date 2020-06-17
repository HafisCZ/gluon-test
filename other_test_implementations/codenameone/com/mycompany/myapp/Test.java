package com.mycompany.myapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
    Java base for performance experiments
    mar21 (c) 2020
*/

public class Test {

    public interface ZeroParamFunction {
        void accept();
    }

    public interface OneParamFunction <T> {
        void accept(T t);
    }

    public interface TwoParamFunction <T, U> {
        void accept(T t, U u);
    }

    public static int Ackermann(int m, int n)
    {
        return m == 0 ? n + 1 : Ackermann(m - 1, n == 0 ? 1 : Ackermann(m, n - 1));
    }

    public class TestResults {

        public final List<Double> values;
        public final Double min, max, median, average;

        public TestResults (List<Double> values) {
            this.values = values;
            this.min = Collections.min(values);
            this.max = Collections.max(values);
            this.average = sum(values) / ((double) values.size());
            this.median = median(values);
        }

    }

    public static double median (List<Double> values) {
        Collections.sort(values);
        return values.size() % 2 == 1 ? values.get(values.size() / 2) : ((values.get(values.size() / 2) + values.get(values.size() / 2 - 1)) / 2);
    }

    public static double sum (List<Double> values) {
    	double x = 0;
    	for (int i = 0; i < values.size(); i++) {
    		x += values.get(i);
    	}
        return x;
    }

    private List<Double> values;
    private int repeats;

    private ZeroParamFunction exit;
    private OneParamFunction<TestResults> finished;
    private OneParamFunction<OneParamFunction<Object>> test;
    private OneParamFunction<Exception> error;
    private TwoParamFunction<List<Double>, Integer> progress;

    public Test (OneParamFunction<OneParamFunction<Object>> test) {
        this.test = test;
        this.values = new ArrayList<Double>();
        this.exit = () -> {};
        this.error = (Exception e) -> { e.printStackTrace(); };
        this.progress = (List<Double> values, Integer size) -> { };
        this.repeats = 1;
        this.finished = (TestResults tr) -> { };
    }

    public Test onFinished(OneParamFunction<TestResults> func) {
        this.finished = func;
        return this;
    }

    public Test onError (OneParamFunction<Exception> func) {
        this.error = func;
        return this;
    }

    public Test onProgress (TwoParamFunction<List<Double>, Integer> func) {
        this.progress = func;
        return this;
    }

    public Test onExit (ZeroParamFunction exit) {
        this.exit = exit;
        return this;
    }

    public Test repeat(int repeats) {
        this.repeats = repeats;
        return this;
    }

    public void execute() {
     
        if (this.values.size() < this.repeats) {
            this.test.accept((Object value) -> {
                if (value instanceof String) {
                    this.error.accept(new Exception((String) value));
                } else {
                    this.values.add((double) ((long) value));
                    this.progress.accept(this.values, this.repeats);
                    this.execute();
                }
            });
        } else {
            this.exit.accept();
            this.finished.accept(new TestResults(this.values));
        }
    }

}
