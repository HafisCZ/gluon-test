package com.hal0160.util;

public class Tuple <T, U> {
	
	private T t;
	private U u;
	
	public Tuple (T t, U u) {
		this.t = t;
		this.u = u;
	}
	
	public T first() {
		return this.t;
	}
	
	public U second() {
		return this.u;
	}
	
}