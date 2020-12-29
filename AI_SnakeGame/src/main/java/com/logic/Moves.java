package com.logic;
import java.awt.Point;

public enum Moves {
	
	RIGHT(new Point(0,1)), LEFT(new Point(0, -1)), UP(new Point(-1,0)), DOWN(new Point(1,0));
	
	private Point value;
	
	private Moves(Point value) {
		this.value = value;
	}
	
	public Point getValue() {
		return value;
	}
	
}
