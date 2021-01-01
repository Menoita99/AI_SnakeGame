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

import lombok.Data;

@Data
public class Snake implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int maxLife = 500;

	private static final int FIELD = 5;

	private static final int eatLife = 150;

	private GameLogic gl;

	private int score = 1;
	private int lifeLeft = 30; // quantidade de movimentos at� morrer
	private int lifetime = 0; // quantidade de movimentos que fez antes de morrer

	private float[] vision;
	private float[] decision = new float[4];

	private LinkedList<Point> body= new LinkedList<>();

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
		brain = isCostum ? new NeuralNetwork(25, 18, 4, 2) : new NeuralNetwork(24, 18, 4, 2) ;
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
		//		System.out.println("Eat "+lifeLeft);
		lifetime++;
		score ++;
		lifeLeft = Math.min(lifeLeft+eatLife, maxLife);
	}

	public Snake clone() { // clone the snake
		Snake clone = new Snake();
		clone.setBrain(brain);
		return clone;
	}

	public Snake crossover(Snake parent) { // crossover the snake with another snake
		Snake child = new Snake();
		child.brain = brain.crossover(parent.brain);
		return child;
	}

	public void mutate() { // mutate the snakes brain
		brain.mutate(0.05f); // threshhold for now
	}

	public float calculateFitness() { 
		return gl.getScore();
	}

	public void look() { // look in all 8 directions and check for food, body and wall
		if(isCostum)
			costumLook();
		else
			standartLook();
	}
	
	
	
	private void standartLook() {
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

	
	public void costumLook() {
		vision = new float[25];
		Point head = getHead();
		Point food = gl.getFoodPos();
		float co = head.y - food.y;
		float ca = food.x - head.y;
		float h = (float) Math.sqrt(Math.pow(co, 2)+Math.pow(ca, 2));
		vision[24] = co/h;
		
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
		
		
		
//		float[] temp = lookInDirection(Moves.UP.getValue());
//		vision[0] = temp[0];
//		vision[1] = temp[1];
//		vision[2] = temp[2];
//		temp = lookInDirection(Moves.DOWN.getValue());
//		vision[3] = temp[0];
//		vision[4] = temp[1];
//		vision[5] = temp[2];
//		temp = lookInDirection(Moves.LEFT.getValue());
//		vision[6] = temp[0];
//		vision[7] = temp[1];
//		vision[8] = temp[2];
//		temp = lookInDirection(Moves.RIGHT.getValue());
//		vision[9] = temp[0];
//		vision[10] = temp[1];
//		vision[11] = temp[2];	
		
//		Moves[] moves = {Moves.UP,Moves.DOWN,Moves.LEFT,Moves.RIGHT};
//		for (int i = 0; i < moves.length; i++) {
//			Moves m = moves[i];
//			int valueAt = gl.getValueAt(new Point(head.x+ m.getValue().x,head.y + m.getValue().y));
//			if(valueAt == 2) {
//				vision[i*2] = 0;
//				vision[i*2+1] = 1; //FOOD
//			}else {
//				vision[i*2] = valueAt;
//				vision[i*2+1] = 0;
//			}
//		}
	}
	
	
//	public void costumLook() {
//		vision = new float[8];
//		Point matrixCenter;
//		int count = 0;
//		if (gl.getLastMove() == Moves.UP) {
//			matrixCenter = new Point(getHead().x + gl.getLastMove().getValue().x, getHead().y + gl.getLastMove().getValue().y);
//			for (int j = -1; j <= 1; j++) {
//				for (int i = -1; i <= 1; i++) {
//					if (!new Point(matrixCenter.x + i, matrixCenter.y + j).equals(getHead())) {
//						vision[count] = (float) gl.getValueAt(new Point(matrixCenter.x + i, matrixCenter.y + j))-1;
//						count++;
//					}
//				}
//			}
//		} else if (gl.getLastMove() == Moves.RIGHT) {
//			matrixCenter = new Point(getHead().x + gl.getLastMove().getValue(). x,getHead().y + gl.getLastMove().getValue().y);
//			for (int i = 1; i >= -1; i--) {
//				for (int j = -1; j <= 1; j++) {
//					if (!new Point(matrixCenter.x + i, matrixCenter.y + j).equals(getHead())) {
//						vision[count] = (float) gl.getValueAt(new Point(matrixCenter.x + i, matrixCenter.y + j))-1;
//						count++;
//					}
//				}
//			}
//		} else if (gl.getLastMove() == Moves.LEFT) {
//			matrixCenter = new Point(getHead().x + gl.getLastMove().getValue().x,getHead().y + gl.getLastMove().getValue().y);
//			for (int i = -1; i <= 1; i++) {
//				for (int j = 1; j >= -1; j--) {
//					if (!new Point(matrixCenter.x + i, matrixCenter.y + j).equals(getHead())) {
//						vision[count] = (float) gl.getValueAt(new Point(matrixCenter.x + i, matrixCenter.y + j))-1;
//						count++;
//					}
//				}
//			}
//		} else if (gl.getLastMove() == Moves.DOWN) {
//			matrixCenter = new Point(getHead().x + gl.getLastMove().getValue().x, getHead().y + gl.getLastMove().getValue().y);
//			for (int j = 1; j >= -1; j--) {
//				for (int i = 1; i >= -1; i--) {
//					if (!new Point(matrixCenter.x + i, matrixCenter.y + j).equals(getHead())) {
//						vision[count] = (float)	 gl.getValueAt(new Point(matrixCenter.x + i, matrixCenter.y + j))-1;
//						count++;
//					}
//				}
//			}
//		}
//	}
	
	

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
		System.out.println("???????????????''");
	}


	public Point getHead(){
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
		File f = new File("BestSnake.snake");
		f.delete();
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f))) {
			out.writeObject(this);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Snake load(String path) {
		File f = new File(path);
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
			return (Snake) in.readObject();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
