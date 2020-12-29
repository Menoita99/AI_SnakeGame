package com.logic;
import java.awt.Point;

public enum Moves {
	
	RIGHT(new Point(1,0)), LEFT(new Point(-1, 0)), UP(new Point(0,-1)), DOWN(new Point(0,1));
	
	private Point value;
	
	private Moves(Point value) {
		this.value = value;
	}
	
	public Point getValue() {
		return value;
	}
	
}
