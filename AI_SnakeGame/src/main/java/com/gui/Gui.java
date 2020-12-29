package com.gui;

import java.io.File;
import java.io.IOException;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import com.logic.GameLogic;
import com.logic.Moves;
import com.neural.NeuralNetwork;

import javafx.application.Application;
import javafx.application.Platform;
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
	private int width = 10;
	private int height = 10;
	private GraphicsContext gc;
	private Text label;
	private static final int BLOCK_SIZE = 20;	
	
	@Override
	public void start(Stage window) throws Exception {
		gl = new GameLogic(width, height);
		window.setTitle("Snake Game");
		label = new Text("Score: "+ gl.getScore());
		Button b = new Button("Play with nn");
		b.setOnMouseClicked(event ->{
			try {
				File f = new File("C:\\Users\\Rui Menoita\\Desktop\\NeuralNetwork.nn");
				NeuralNetwork nn = new NeuralNetwork();
				MultiLayerNetwork loaded = MultiLayerNetwork.load(f, true);
				nn.setMultiLayerNetwork(loaded);
				playWithNeuralNetwork(nn);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		VBox root = new VBox();
		Scene s = new Scene(root);
		Canvas canvas = new Canvas(width * BLOCK_SIZE, height * BLOCK_SIZE);
		gc = canvas.getGraphicsContext2D();
		
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
				showErrorDialog("Game Over", "Final score: " + (gl.getFoodPoints() + gl.getSurvivingPoints()) + " point.");
			label.setText("Score: "+ gl.getScore());
		});
		root.getChildren().addAll(b,label,canvas);
        window.setScene(s);
        window.show();
        
	}
	
	
	private void drawCanvas() {
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
	
    
    public void playWithNeuralNetwork(NeuralNetwork nn) {
    	while(!gl.isGameOver()) {
	    	nn.setGl(gl);
	    	gl.getSnake().forEach(System.out::println);
	    	gl.moveSnake(nn.feedNetwork(nn.getInput()));
	    	Platform.runLater(this::drawCanvas);
	    	
			label.setText("Score: "+ gl.getScore());
	    	gl.getSnake().forEach(System.out::println);
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	showErrorDialog("Game Over", "Final score: " + (gl.getFoodPoints() + gl.getSurvivingPoints()) + " point.");
    }
    
	
	public static void main(String[] args) {
		launch(args);
	}


}
