package com.logic;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import com.neural.NeuralNetwork;
import com.nn.train.GeneticEngine;

import lombok.Data;

@Data
public class Snake implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int maxLife = 75;// 75;

	public static final int FIELD = 6;

	private static final int eatLife = 50;// 50

	private GameLogic gl;

	private int score = 1;
	private int lifeLeft = 25; // quantidade de movimentos at� morrer
	private int lifetime = 0; // quantidade de movimentos que fez antes de morrer

	private float[] vision;
	private float[] decision = new float[4];

	private LinkedList<Point> body = new LinkedList<>();

	private NeuralNetwork brain;

	private int width;
	private int height;
	private LinkedList<Food> foodPos = new LinkedList<>();

	private boolean isCostum;

	public Snake(boolean isCostum) {
		this.isCostum = isCostum;
		gl = new GameLogic(FIELD, FIELD);
		this.width = gl.getWidth();
		this.height = gl.getHeight();
		body.add(new Point(gl.getWidth() / 2, gl.getHeight() / 2));
		brain = isCostum ? new NeuralNetwork(9, 6, 4, 1) : new NeuralNetwork(24, 18, 4, 2);
		gl.setSnake(this);
	}

	public Snake() {
		gl = new GameLogic(FIELD, FIELD);
		this.width = gl.getWidth();
		this.height = gl.getHeight();
		body.add(new Point(gl.getWidth() / 2, gl.getHeight() / 2));
		brain = new NeuralNetwork(24, 18, 4, 2);
		gl.setSnake(this);
	}

	public Snake(NeuralNetwork brain) {
		gl = new GameLogic(FIELD, FIELD);
		this.width = gl.getWidth();
		this.height = gl.getHeight();
		body.add(new Point(gl.getWidth() / 2, gl.getHeight() / 2));
		this.brain = brain;
		gl.setSnake(this);
	}

	public boolean bodyCollide(float x, float y) { // check if a position collides with the snakes body
		for (int i = 0; i < body.size(); i++)
			if (x == body.get(i).x && y == body.get(i).y)
				return true;
		return false;
	}

	public boolean foodCollide(float x, float y) { // check if a position collides with the food
		Point food = gl.getFoodPos();
		if (x == food.x && y == food.y)
			return true;
		return false;
	}

	public boolean wallCollide(float x, float y) { // check if a position collides with the wall
		if (y < 0 || y > height - 1 || x < 0 || x > width - 1)
			return true;
		return false;
	}

	public void eat() { // eat food
		// System.out.println("Eat "+lifeLeft);
		lifetime++;
		score++;
		lifeLeft = Math.min(lifeLeft + eatLife, maxLife);
	}

	public Snake clone() { // clone the snake
		Snake clone = new Snake();
		clone.setBrain(brain);
		clone.setCostum(this.isCostum);
		return clone;
	}

	public Snake crossover(Snake parent) { // crossover the snake with another snake
		Snake child = new Snake();
		child.brain = brain.crossover(parent.brain);
		child.setCostum(parent.isCostum);
		return child;
	}

	public void mutate() { // mutate the snakes brain
		brain.mutate(GeneticEngine.motationRate);
	}

	public float calculateFitness() {
		return gl.getScore();
	}

	public void look() { // look in all 8 directions and check for food, body and wall
		if (isCostum) {
			senInput();
		} else {
			standartLook();
			//starInput();
		}
	}

	public void standartLook() {
		vision = new float[24];
		float[] temp = lookInDirection(new Point(-1, 0));
		vision[0] = temp[0];
		vision[1] = temp[1];
		vision[2] = temp[2];
		temp = lookInDirection(new Point(-1, -1));
		vision[3] = temp[0];
		vision[4] = temp[1];
		vision[5] = temp[2];
		temp = lookInDirection(new Point(0, -1));
		vision[6] = temp[0];
		vision[7] = temp[1];
		vision[8] = temp[2];
		temp = lookInDirection(new Point(1, -1));
		vision[9] = temp[0];
		vision[10] = temp[1];
		vision[11] = temp[2];
		temp = lookInDirection(new Point(1, 0));
		vision[12] = temp[0];
		vision[13] = temp[1];
		vision[14] = temp[2];
		temp = lookInDirection(new Point(1, 1));
		vision[15] = temp[0];
		vision[16] = temp[1];
		vision[17] = temp[2];
		temp = lookInDirection(new Point(0, 1));
		vision[18] = temp[0];
		vision[19] = temp[1];
		vision[20] = temp[2];
		temp = lookInDirection(new Point(-1, 1));
		vision[21] = temp[0];
		vision[22] = temp[1];
		vision[23] = temp[2];
	}

	public void starInput() {
		vision = new float[24];
		Point head = getHead();

		int count = 0;
		Moves[] moves = { Moves.UP, Moves.DOWN, Moves.LEFT, Moves.RIGHT };
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Point point = new Point(head.x - i, head.y - j);
				int valueAt = gl.getValueAt(point);
				if (!getHead().equals(point)) {
					vision[count * 2] = valueAt == 2 ? 1 : 0;
					vision[count * 2 + 1] = valueAt == 1 ? 1 : 0;
					count++;
				}
			}
		}
		for (int i = 0; i < moves.length; i++) {
			Moves m = moves[i];
			int valueAt = gl.getValueAt(new Point(head.x - m.getValue().x * 2, head.y - m.getValue().y * 2));
			vision[count * 2] = valueAt == 2 ? 1 : 0;
			vision[count * 2 + 1] = valueAt == 1 ? 1 : 0;
			count++;
		}
	}

	public void senInput() {
		vision = new float[9];
		Point head = getHead();
		Point food = gl.getFoodPos();
		float co = head.y - food.y;
		float ca = food.x - head.y;
		float h = (float) Math.sqrt(Math.pow(co, 2) + Math.pow(ca, 2));
		vision[8] = co / h;

		Moves[] moves = { Moves.UP, Moves.DOWN, Moves.LEFT, Moves.RIGHT };
		for (int i = 0; i < moves.length; i++) {
			Moves m = moves[i];
			int valueAt = gl.getValueAt(new Point(head.x + m.getValue().x, head.y + m.getValue().y));
			vision[i * 2] = valueAt == 2 ? 1 : 0;
			vision[i * 2 + 1] = valueAt == 1 ? 1 : 0;
		}
	}

	public float[] lookInDirection(Point direction) { // look in a direction and check for food, body and wall
		float look[] = new float[3];
		Point pos = getHead();
		float distance = 0;
		boolean foodFound = false;
		boolean bodyFound = false;
		pos = new Point(pos.x + direction.x, pos.y + direction.y);
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
			pos = new Point(pos.x + direction.x, pos.y + direction.y);
			distance += 1;
		}
		look[2] = 1 / distance;
		return look;
	}

	public void thinkAndMove() { // think about what direction to move
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
			gl.moveSnake(Moves.UP);
			return;
		case 1:
			gl.moveSnake(Moves.DOWN);
			return;
		case 2:
			gl.moveSnake(Moves.LEFT);
			return;
		case 3:
			gl.moveSnake(Moves.RIGHT);
			return;
		}
		System.err.println("???????????????");
	}

	public Point getHead() {
		return body.getLast();
	}

	public boolean isDead() {
		return gl.isGameOver();
	}

	public void reduceLife() {
		lifeLeft--;
		lifetime++;
	}

	public void save() {
		File f = new File("NewBestSnake.snake");
		f.delete();
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f))) {
			out.writeObject(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Snake load(String path) {
		File f = new File(path);
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
			return (Snake) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
