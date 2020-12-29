package com.logic;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import lombok.Data;


@Data
public class GameLogic{
	
	private int width, height;
	private int[][] gameMatrix;
	
	private LinkedList<Point> snake = new LinkedList<Point>();
	private Point foodPos = new Point();
	private boolean gameOver = false;
	private int survivingPoints;
	private int foodPoints;
	private Moves lastMove = Moves.UP;
	
	
	public GameLogic(int width, int height) {
		this.width = width;
		this.height = height;
		snake.add(new Point(width/2, height/2));
		gameMatrix = new int[width][height];
		generateFood();
		gameUpdate();
	}
	
	private void gameUpdate() {
		gameMatrix = new int[width][height];
		gameMatrix[foodPos.y][foodPos.x] = 2;
		for(Point p : snake)
			gameMatrix[p.y][p.x] = 1;
//		printGame();
	}
	
	private void gameLoop() {
		Scanner scanner = new Scanner(System.in);
		//gameUpdate();
		while(!gameOver && scanner.hasNextLine()) {
			String key = scanner.nextLine();
			System.out.println(key.length());
			keyPressed(key);
			//gameUpdate();
		}
		scanner.close();
		printScore();
	}
	
	public void moveSnake(Moves move) {
		for(Point p : snake) {
			if(new Point(snake.getLast().x + move.getValue().x, snake.getLast().y + move.getValue().y).equals(p) || 
					snake.getLast().y + move.getValue().y < 0 ||
					snake.getLast().y + move.getValue().y > height -1 ||
					snake.getLast().x + move.getValue().x < 0 ||
					snake.getLast().x + move.getValue().x > width - 1
					) {
				gameOver = true;
			}
		}
		if(!gameOver) {
			snake.add(new Point(snake.getLast().x + move.getValue().x, snake.getLast().y + move.getValue().y));
			survivingPoints +=1;
			lastMove = move;
			if(gameMatrix[snake.getLast().y][snake.getLast().x] != 2) {
				snake.removeFirst();
			}else {
				foodPoints += 5;
				generateFood();
			}
		}
		gameUpdate();
	}
	
	private void generateFood() {
		Point foodPosAux;
		do {
			foodPosAux = new Point(new Random().nextInt(width), new Random().nextInt(height));
		}while(snake.contains(foodPosAux));
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
	
	
	
	
	
	private void printGame() {
		String rowAux = "";
		 for (int row = 0; row < gameMatrix.length; row++) {
			    for (int col = 0; col < gameMatrix[row].length; col++) {
			    	rowAux += Integer.toString(gameMatrix[row][col]) + " ";
			    }
			    System.out.println(rowAux);
			    rowAux ="";
			 }
	}
	
	private void printScore() {
		System.out.println("Final score is: " + (foodPoints + survivingPoints) + " points.");
	}
	
	public void keyPressed(String key) {
		if(key.equals("w")) {
			moveSnake(Moves.UP);
		}else if(key.equals("s")) {
			moveSnake(Moves.DOWN);
		}else if(key.equals("a")) {
			moveSnake(Moves.LEFT);
		}else if(key.equals("d")) {
			moveSnake(Moves.RIGHT);
		}
	}

	public static void main(String[] args) {
		GameLogic gl = new GameLogic(10, 10);
		gl.gameLoop();
	}
}
