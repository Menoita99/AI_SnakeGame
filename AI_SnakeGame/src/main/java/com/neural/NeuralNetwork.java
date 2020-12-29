package com.neural;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;

import com.logic.GameLogic;
import com.logic.Moves;

import lombok.Data;

@Data
public class NeuralNetwork {
	private int inputNum = 8;
	private int hiddenNum = 5;
	private int outputNum = 4;
	private List<NDArray> list;
	private MultiLayerConfiguration conf;
	private MultiLayerNetwork multiLayerNetwork;

	private GameLogic gl;

	public NeuralNetwork(List<NDArray> list) {
		this.list = list;
		gl = new GameLogic(4, 4);
		createBrain();
		startNeuralNetwork(conf);
		setNetworkParams();
	}

	public NeuralNetwork() {
		// this.list = list;
		gl = new GameLogic(4, 4);
		createBrain();
		startNeuralNetwork(conf);
		//setNetworkParams();
	}

	private void createBrain() {
		conf = new NeuralNetConfiguration.Builder().activation(Activation.SIGMOID).list()
				.layer(0, new DenseLayer.Builder().activation(Activation.SIGMOID).nIn(inputNum).nOut(hiddenNum).build())
				.layer(1,
						new OutputLayer.Builder().activation(Activation.SOFTMAX).nIn(hiddenNum).nOut(outputNum).build())
				.build();
	}

	public void startNeuralNetwork(MultiLayerConfiguration conf) {
		multiLayerNetwork = new MultiLayerNetwork(conf);
		multiLayerNetwork.init();

	}

	public void setNetworkParams() {
		Map<String, INDArray> paramTable = multiLayerNetwork.paramTable();
		Set<String> keys = paramTable.keySet();
		Iterator<String> it = keys.iterator();
//		System.out.println(paramTable);
//		System.out.println("--------------------------------------");
		for (int i = 0; it.hasNext() && i < list.size(); i++) {
			String key = it.next();
			multiLayerNetwork.setParam(key, list.get(i));
		}
//		System.out.println(paramTable);
	}

	public Moves feedNetwork(INDArray input) {
		INDArray output = multiLayerNetwork.output(input);
		int highestIndex = 0;
		for (int i = 0; i < output.length(); i++) {
			if(output.getDouble(i) > output.getDouble(highestIndex)) {
				highestIndex = i;
			}
		}

		if (highestIndex == 1) {
			return Moves.UP;
		} else if (highestIndex == 3) {
			return Moves.DOWN;
		} else if (highestIndex == 2) {
			return Moves.RIGHT;
		} else if (highestIndex == 0) {
			return Moves.LEFT;
		}
		
		throw new IllegalStateException();
	}

	public NDArray getInput() {
		double[][] networkInputs = new double[1][8];
		Point matrixCenter;
		int count = 0;
		if (gl.getLastMove() == Moves.UP) {
			matrixCenter = new Point(gl.getSnake().getLast().x + gl.getLastMove().getValue().x,
					gl.getSnake().getLast().y + gl.getLastMove().getValue().y);
			for (int j = -1; j <= 1; j++) {
				for (int i = -1; i <= 1; i++) {
					if (!new Point(matrixCenter.x + i, matrixCenter.y + j).equals(gl.getSnake().getLast())) {
						networkInputs[0][count] = (double) gl
								.getValueAt(new Point(matrixCenter.x + i, matrixCenter.y + j));
						count++;
					}
				}
			}
		} else if (gl.getLastMove() == Moves.RIGHT) {
			matrixCenter = new Point(gl.getSnake().getLast().x + gl.getLastMove().getValue().x,
					gl.getSnake().getLast().y + gl.getLastMove().getValue().y);
			for (int i = 1; i >= -1; i--) {
				for (int j = -1; j <= 1; j++) {
					if (!new Point(matrixCenter.x + i, matrixCenter.y + j).equals(gl.getSnake().getLast())) {
						networkInputs[0][count] = (double) gl
								.getValueAt(new Point(matrixCenter.x + i, matrixCenter.y + j));
						count++;
					}
				}
			}
		} else if (gl.getLastMove() == Moves.LEFT) {
			matrixCenter = new Point(gl.getSnake().getLast().x + gl.getLastMove().getValue().x,
					gl.getSnake().getLast().y + gl.getLastMove().getValue().y);
			for (int i = -1; i <= 1; i++) {
				for (int j = 1; j >= -1; j--) {
					if (!new Point(matrixCenter.x + i, matrixCenter.y + j).equals(gl.getSnake().getLast())) {
						networkInputs[0][count] = (double) gl
								.getValueAt(new Point(matrixCenter.x + i, matrixCenter.y + j));
						count++;
					}
				}
			}
		} else if (gl.getLastMove() == Moves.DOWN) {
			matrixCenter = new Point(gl.getSnake().getLast().x + gl.getLastMove().getValue().x,
					gl.getSnake().getLast().y + gl.getLastMove().getValue().y);
			for (int j = 1; j >= -1; j--) {
				for (int i = 1; i >= -1; i--) {
					if (!new Point(matrixCenter.x + i, matrixCenter.y + j).equals(gl.getSnake().getLast())) {
						networkInputs[0][count] = (double) gl
								.getValueAt(new Point(matrixCenter.x + i, matrixCenter.y + j));
						count++;
					}
				}
			}
		}

		NDArray input = new NDArray(networkInputs);
		return input;
	}

	public double play() {
		INDArray input;
		System.out.println(Thread.currentThread()+" Start playing");
		while (!gl.isGameOver()) {
			input = getInput();
			gl.moveSnake(feedNetwork(input));
		}
		System.out.println(Thread.currentThread()+" Lost with "+ (gl.getFoodPoints() + gl.getSurvivingPoints()));
		return gl.getFoodPoints() + gl.getSurvivingPoints();
	}

	public static void main(String[] args) {
		List<NDArray> list = new ArrayList<>();
		list.add(new NDArray(new double [][] {
			{0,0,0,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0},
		}));
		list.add(new NDArray(new double [][] {
			{0,0,0,0,0},
		}));
		list.add(new NDArray(new double [][] {
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},

		}));
		list.add(new NDArray(new double [][] {
			{0,0,0,0},
		}));
		
		new NeuralNetwork(list);
	
	}

}
