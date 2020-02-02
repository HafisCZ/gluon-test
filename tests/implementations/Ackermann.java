package com.hal0160.tests.implementations;

import com.hal0160.tests.Test;
import com.hal0160.util.Utils;

public class Ackermann extends Test {

	private final int a, b;
	
	public Ackermann(int iterations, int a, int b) {
		super(iterations);
		this.a = a;
		this.b = b;
	}
	
	@Override
	protected void test() {
		Utils.ackermann(this.a, this.b);
	}

}
