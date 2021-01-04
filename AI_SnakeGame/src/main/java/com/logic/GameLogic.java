package com.logic;
import java.awt.Point;
import java.io.Serializable;
import java.util.Random;
import lombok.Data;


@Data
public class GameLogic implements Serializable{

	private static final long serialVersionUID = 1L;

	private Random random = new Random();
	private int width, height;
	private int[][] gameMatrix;

	private Snake snake;
	private Point foodPos = new Point();
	private boolean gameOver = false;
	private float survivingPoints;
	private int foodPoints;
	private Moves lastMove = Moves.UP;


	public GameLogic(int width, int height) {
		this.width = width;
		this.height = height;
		gameMatrix = new int[width][height];
	}



	public GameLogic(int width, int height, long seed) {
		this.width = width;
		this.height = height;
		gameMatrix = new int[width][height];
		random = new Random(seed);
	}



	private void gameUpdate() {
		if(!gameOver) {
			gameMatrix = new int[width][height];
			gameMatrix[foodPos.y][foodPos.x] = 2;
			for(Point p : snake.getBody())
				gameMatrix[p.y][p.x] = 1;
		}
	}



	public void moveSnake(Moves move) {
		if(snake.getLifeLeft()<=0) {
			gameOver = true;
			return;
		}

		for(Point p : snake.getBody()) {
			if(new Point(snake.getHead().x + move.getValue().x, snake.getHead().y + move.getValue().y).equals(p) ||
					snake.getHead().y + move.getValue().y < 0 ||
					snake.getHead().y + move.getValue().y > height -1 ||
					snake.getHead().x + move.getValue().x < 0 ||
					snake.getHead().x + move.getValue().x > width - 1
					) {
				gameOver = true;
			}
		}
		if(!gameOver) {
			snake.getBody().add(new Point(snake.getHead().x + move.getValue().x, snake.getHead().y + move.getValue().y));
			survivingPoints +=1;
			lastMove = move;
			if(gameMatrix[snake.getHead().y][snake.getHead().x] != 2) {
				snake.getBody().removeFirst();
				snake.reduceLife();
			}else {
				snake.eat();
				foodPoints += 50;
				generateFood();
			}
		}
		gameUpdate();
	}

	private void generateFood() {
		if(snake.getBody().size() == width*height-1) {
			foodPos = null;
			gameOver = true;
			return;
		}
		Point foodPosAux;
		do {
			foodPosAux = new Point(random.nextInt(width), random.nextInt(height));
		}while(snake.getBody().contains(foodPosAux));
		foodPos = foodPosAux;
		//System.out.println("Food generated at: (" + foodPos.x + ", " + foodPos.y + ")");
		gameMatrix[foodPosAux.y][foodPosAux.x] = 2;
	}


	public int getValueAt(Point point) {
		if(!(point.x < 0 || point.x >= width || point.y < 0 || point.y >= height)) {
			return gameMatrix[point.y][point.x];
		}
		return 1;
	}


	public void setSnake(Snake s) {
		snake = s;
		generateFood();
		gameUpdate();
	}



	public float getScore() {
		return foodPoints + survivingPoints;
	}
}
