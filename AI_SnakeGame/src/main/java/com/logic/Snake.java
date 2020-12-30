package com.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.neural.NeuralNetwork;
import com.sun.javafx.geom.Point2D;

import lombok.Data;

@Data
public class Snake {
	private GameLogic gl;

	private int score = 1;
	private int lifeLeft = 200; // quantidade de movimentos até morrer
	private int lifetime = 0; // quantidade de movimentos que fez antes de morrer
	private Moves move;

	private float  fitness = 0;

	private boolean dead = false;

	private float[] vision; // visão da snake
	private float[] decision; // dicisões da snake

	private Point2D head;

	private List<Point2D> body;
	private List<Food> foodList;

	private Food food;
	private NeuralNetwork brain;

	private int width;
	private int height;

	public Snake() {
		gl = new GameLogic(5, 5);
		this.width = gl.getWidth();
		this.height = gl.getHeight();
		head = new Point2D(gl.getWidth() / 2, gl.getHeight() / 2);
		food = new Food(gl.getWidth(), gl.getHeight());
		body = new LinkedList<>();
		vision = new float[24];
		decision = new float[4];
		foodList = new ArrayList<>();
		foodList.add(food.clone());
		brain = new NeuralNetwork(24, 18, 4, 2);
//		body.add(new PVector(800,(height/2)+SIZE));  
//	    body.add(new PVector(800,(height/2)+(2*SIZE)));
		score += 2;
	}

	public boolean bodyCollide(float x, float y) { // check if a position collides with the snakes body
		for (int i = 0; i < body.size(); i++)
			if (x == body.get(i).x && y == body.get(i).y)
				return true;
		return false;
	}

	public boolean foodCollide(float x, float y) { // check if a position collides with the food
		if (x == food.pos.x && y == food.pos.y)
			return true;
		return false;
	}

	public boolean wallCollide(float x, float y) { // check if a position collides with the wall
		if (y < 0 || y > height - 1 || x < 0 || x > width - 1)
			return true;
		return false;
	}

	public void move() { // move the snake
		if (!dead) {
			if (foodCollide(head.x, head.y)) {
				eat();
			}
			shiftBody();
			if (wallCollide(head.x, head.y)) {
				dead = true;
			} else if (bodyCollide(head.x, head.y)) {
				dead = true;
			} else if (lifeLeft <= 0) {
				dead = true;
			}
		}
	}

	public void eat() { // eat food
		int len = body.size() - 1;
		score++;
		if (lifeLeft < 500) {
			if (lifeLeft > 400) {
				lifeLeft = 500;
			} else {
				lifeLeft += 100;
			}
		}
		if (len >= 0) {
			body.add(new Point2D(body.get(len).x, body.get(len).y));
		} else {
			body.add(new Point2D(head.x, head.y));
		}
	}

	public void shiftBody() { // shift the body to follow the head
		float tempx = head.x;
		float tempy = head.y;
		head.x += move.getValue().x;
		head.y += move.getValue().y;
		float temp2x;
		float temp2y;
		for (int i = 0; i < body.size(); i++) {
			temp2x = body.get(i).x;
			temp2y = body.get(i).y;
			body.get(i).x = tempx;
			body.get(i).y = tempy;
			tempx = temp2x;
			tempy = temp2y;
		}
	}

	public Snake clone() { // clone the snake
		return this;
	}

	public Snake crossover(Snake parent) { // crossover the snake with another snake
		Snake child = this;
		child.brain = brain.crossover(parent.brain);
		return child;
	}

	public void mutate() { // mutate the snakes brain
		brain.mutate(0.05f); // threshhold for now
	}

	public void calculateFitness() { // calculate the fitness of the snake
		if (score < 10) {
			fitness = (float) (Math.floor(lifetime * lifetime) * Math.pow(2, score));
		} else {
			fitness = (float) Math.floor(lifetime * lifetime);
			fitness *= Math.pow(2, 10);
			fitness *= (score - 9);
		}
	}

	public void look() { // look in all 8 directions and check for food, body and wall
		vision = new float[24];
		float[] temp = lookInDirection(new Point2D(-1, 0));
		vision[0] = temp[0];
		vision[1] = temp[1];
		vision[2] = temp[2];
		temp = lookInDirection(new Point2D(-1, -1));
		vision[3] = temp[0];
		vision[4] = temp[1];
		vision[5] = temp[2];
		temp = lookInDirection(new Point2D(0, -1));
		vision[6] = temp[0];
		vision[7] = temp[1];
		vision[8] = temp[2];
		temp = lookInDirection(new Point2D(1, -1));
		vision[9] = temp[0];
		vision[10] = temp[1];
		vision[11] = temp[2];
		temp = lookInDirection(new Point2D(1, 0));
		vision[12] = temp[0];
		vision[13] = temp[1];
		vision[14] = temp[2];
		temp = lookInDirection(new Point2D(1, 1));
		vision[15] = temp[0];
		vision[16] = temp[1];
		vision[17] = temp[2];
		temp = lookInDirection(new Point2D(0, 1));
		vision[18] = temp[0];
		vision[19] = temp[1];
		vision[20] = temp[2];
		temp = lookInDirection(new Point2D(-1, 1));
		vision[21] = temp[0];
		vision[22] = temp[1];
		vision[23] = temp[2];
	}

	public float[] lookInDirection(Point2D direction) { // look in a direction and check for food, body and wall
		float look[] = new float[3];
		Point2D pos = new Point2D(head.x, head.y);
		float distance = 0;
		boolean foodFound = false;
		boolean bodyFound = false;
		pos = new Point2D(pos.x + direction.x, pos.y + direction.y);
		distance += 1;
		while (!wallCollide(pos.x, pos.y)) {
			if (!foodFound && foodCollide(pos.x, pos.y)) {
				foodFound = true;
				look[0] = 1;
			}
			if (!bodyFound && bodyCollide(pos.x, pos.y)) {
				bodyFound = true;
				look[1] = 1;
			}
			pos = new Point2D(pos.x + direction.x, pos.y + direction.y);
			distance += 1;
		}
		look[2] = 1 / distance;
		return look;
	}

	public void think() { // think about what direction to move
		decision = brain.output(vision);
		int maxIndex = 0;
		float max = 0;
		for (int i = 0; i < decision.length; i++) {
			if (decision[i] > max) {
				max = decision[i];
				maxIndex = i;
			}
		}

		switch (maxIndex) {
		case 0:
			moveUp();
			break;
		case 1:
			moveDown();
			break;
		case 2:
			moveLeft();
			break;
		case 3:
			moveRight();
			break;
		}
	}

	public void moveUp() {
		move = Moves.UP;
	}

	public void moveDown() {
		move = Moves.DOWN;
	}

	public void moveLeft() {
		move = Moves.LEFT;
	}

	public void moveRight() {
		move = Moves.RIGHT;
	}

}
