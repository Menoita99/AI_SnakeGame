package com.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.neural.NeuralNetwork;
import com.sun.javafx.geom.Point2D;

import lombok.Data;

@Data
public class Snake {
	
	private int score = 1;
	private int lifeLeft = 200;	//quantidade de movimentos até morrer
	private int lifeTime = 0;	//quantidade de movimentos que fez antes de morrer
	
	private double fitness = 0;
	
	private boolean dead = false;
	
	private float [] vision;	//visão da snake
	private float [] decision;	//dicisões da snake
	
	private Point2D head;
	
	private List<Point2D> body;
	private List<Food> foodList;
	
	private Food food;
	private NeuralNetwork brain;
	
	public Snake(int layers, GameLogic gl) {
		head = new Point2D(gl.getWidth()/2, gl.getHeight()/2);
		food = new Food(gl.getWidth(), gl.getHeight());
		body  = new LinkedList<>();
		vision = new float[24];
		decision = new float[4];
		foodList  = new ArrayList<>();
		foodList.add(food.clone());
	}
	
	

}
