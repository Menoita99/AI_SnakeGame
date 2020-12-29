package com.gui;
import java.awt.Point;

import com.logic.GameLogic;
import com.logic.Moves;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Gui extends Application{
	
	private GameLogic gl;
	private int width = 10;
	private int height = 10;
	private static final int BLOCK_SIZE = 20;	
	
	
	@Override
	public void start(Stage window) throws Exception {
		gl = new GameLogic(width, height);
		window.setTitle("Snake Game");
		Group root = new Group();
		Scene s = new Scene(root, width * BLOCK_SIZE, height * BLOCK_SIZE, Color.BLUE);
		final Canvas canvas = new Canvas(width * BLOCK_SIZE, height * BLOCK_SIZE);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		drawCanvas(gc, gl);
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
			drawCanvas(gc, gl);
			if(gl.isGameOver())
				showErrorDialog("Game Over", "Final score: " + (gl.getFoodPoints() + gl.getSurvivingPoints()) + " point.");
		});
		root.getChildren().add(canvas);
        window.setScene(s);
        window.show();
        
	}
	
	
	private void drawCanvas(GraphicsContext gc, GameLogic gl) {
		gc.setStroke(Color.BLACK);
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
    }
	
	
	public static void main(String[] args) {
		launch(args);
	}


}
