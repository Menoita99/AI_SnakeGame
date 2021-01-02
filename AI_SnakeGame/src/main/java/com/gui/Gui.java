package com.gui;


import com.logic.GameLogic;
import com.logic.Moves;
import com.logic.Snake;
import com.neural.NeuralNetwork;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Gui extends Application{

	private GameLogic gl;
	private int width = Snake.FIELD;
	private int height = Snake.FIELD;
	private GraphicsContext gc;
	private Text label;
	private static final int BLOCK_SIZE = 30;
	private static Gui INSTANCE;
	private AnimationTimer loop;
	private Snake sn = new Snake();
	private boolean loopStarted;
	private Button nnButton;

	@Override 
	public void start(Stage window) throws Exception {
		INSTANCE = this;
		gl = new GameLogic(width, height);
		sn.setGl(gl);
		gl.setSnake(sn);
		window.setTitle("Snake Game");
		label = new Text("Score: "+ sn.getScore());

		nnButton = new Button("Play with nn");
		nnButton.setOnMouseClicked(event ->{
			Snake s = Snake.load("NewBestSnake3.snake");
			sn.setBrain(s.getBrain());
			if(loopStarted) { 
				loop.stop();
				loopStarted = false;
				nnButton.setText("Play with nn");
			}else {
				loop.start();
				loopStarted = true;
				nnButton.setText("Stop nn");
			}

		});
		VBox root = new VBox();
		Scene s = new Scene(root);
		Canvas canvas = new Canvas(width * BLOCK_SIZE, height * BLOCK_SIZE);
		gc = canvas.getGraphicsContext2D();
		initLoop();
		drawCanvas();
		s.setOnKeyPressed(key->{
			if(key.getCode() == KeyCode.W || key.getCode() == KeyCode.UP) {
				gl.moveSnake(Moves.UP);
			}else if(key.getCode() == KeyCode.S || key.getCode() == KeyCode.DOWN) {
				gl.moveSnake(Moves.DOWN);
			}else if(key.getCode() == KeyCode.A || key.getCode() == KeyCode.LEFT) {
				gl.moveSnake(Moves.LEFT);
			}else if(key.getCode() == KeyCode.D || key.getCode() == KeyCode.RIGHT) {
				gl.moveSnake(Moves.RIGHT);
			}
			drawCanvas();
			if(gl.isGameOver())
				showErrorDialog("Game Over", "Final score: " + sn.getScore() + " point.");
		});
		root.getChildren().addAll(nnButton,label,canvas);
		window.setScene(s);
		window.show();

	}


	private void initLoop() {
		loop = new AnimationTimer() {

			public int frame = 0;

			@Override
			public void handle(long now) {
				if(frame % 5 == 0) {
					if(!gl.isGameOver()) {
						sn.look();
						sn.thinkAndMove();
					}else {
						System.out.println("Game Over"+ "Final score: " + sn.getScore() + " point.");
						this.stop();
					}
					System.out.println(gl.getLastMove());
					drawCanvas();
					frame = 0;
				}
				frame++;
			}
		};  
	}


	private void drawCanvas() {
		int [][] auxMatrix = gl.getGameMatrix();
		for(int i = 0; i < auxMatrix.length; i++) {
			for(int j = 0; j < auxMatrix[i].length; j++) {
				if(auxMatrix[i][j] == 0) {
					gc.setFill(Color.WHITE);
				}else if(auxMatrix[i][j] == 1) {
					gc.setFill(Color.RED);
				}else if(auxMatrix[i][j] == 2) {
					gc.setFill(Color.GREEN);
				}
				gc.fillRect(j * BLOCK_SIZE,i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
			}
		}
		label.setText("Score: "+ sn.getScore() + "\nSteps left: " + sn.getLifeLeft());
	}

	/*
	 * Display's an error dialog with the title and message given
	 * @param title dialog title
	 * @param Message dialog message
	 */
	public void showErrorDialog(String title,String Message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(Message);
		alert.showAndWait();
		System.exit(0);
	}


	public void playWithNeuralNetwork(NeuralNetwork n) {
		sn = new Snake();		
		sn.setBrain(n);
		sn.setGl(gl);
		gl.setSnake(sn);
		sn.setBrain(n);
		loopStarted = true;
		nnButton.setText("Stop nn");
		loop.start();
	}


	/**
	 * @return the iNSTANCE
	 */
	public static Gui getINSTANCE() {
		return INSTANCE;
	}


	public static void main(String[] args) {
		launch(args);
	}


}
