package com.logic;

import java.util.Random;

import com.sun.javafx.geom.Point2D;

public class Food {
	Point2D pos;
	
	public Food(int mapWidth,int mapHeight) {
		pos = new Point2D(new Random().nextInt(mapWidth), new Random().nextInt(mapHeight));
	}
	
	public Food(Point2D pos) {
		this.pos = pos;
	}
	
	public Food clone() {
		return this;
	}
	
	
	

}
