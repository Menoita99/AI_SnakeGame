package com.neural;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import com.logic.GameLogic;

import lombok.Data;

@Data
public class NeuralNetwork {
	private int inputNum = 8;
	private int outputNum = 3;
	private List<INDArray> list;
	private MultiLayerConfiguration conf;
	private MultiLayerNetwork multiLayerNetwork;

	private GameLogic gl;

	public NeuralNetwork(List<INDArray> list) {
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
		setNetworkParams();
	}

	private void createBrain() {
		conf = new NeuralNetConfiguration.Builder().activation(Activation.SIGMOID).weightInit(WeightInit.XAVIER).list()
				.layer(0, new DenseLayer.Builder().nIn(inputNum).nOut(5).build())
				.layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
						.activation(Activation.SOFTMAX).nIn(5).nOut(outputNum).build())
				.build();
	}

	public void startNeuralNetwork(MultiLayerConfiguration conf) {
		multiLayerNetwork = new MultiLayerNetwork(conf);
		multiLayerNetwork.init();

	}

	public void setNetworkParams() {
		int itAux = 0;
		Map<String, INDArray> paramTable = multiLayerNetwork.paramTable();
		Set<String> keys = paramTable.keySet();
		Iterator<String> it = keys.iterator();

		for (int i = 0; it.hasNext() && i < list.size(); i++) {
			String key = it.next();
			multiLayerNetwork.setParam(key, list.get(i));
			itAux++;
		}
	}

	public double play() {
		while (!gl.isGameOver()) {

		}
		return gl.getFoodPoints() + gl.getSurvivingPoints();
	}

	public static void main(String[] args) {
		new NeuralNetwork();
	}

}
