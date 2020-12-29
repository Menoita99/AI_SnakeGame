package com.neural;

import java.util.Arrays;
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
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class NeuralNetwork {
	private int inputNum = 8;
	private int outputNum = 3;
	private List<NDArray> list;
	
	
	
	public NeuralNetwork() {
		//this.list = list;
	}
	
	private void createBrain() {
		MultiLayerConfiguration conf 
		  = new NeuralNetConfiguration.Builder()
		    .activation(Activation.TANH)
		    .weightInit(WeightInit.XAVIER)
		    .list()
		    .layer(0, new DenseLayer.Builder().nIn(inputNum).nOut(5).build())
		    .layer(1, new OutputLayer.Builder(
		      LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
		        .activation(Activation.SOFTMAX)
		        .nIn(5).nOut(outputNum).build())
		    .build();
		
		
		MultiLayerNetwork multiLayerNetwork = new MultiLayerNetwork(conf);
	    multiLayerNetwork.init();
	    
	    Map<String, INDArray> paramTable = multiLayerNetwork.paramTable();
	    Set<String> keys = paramTable.keySet();
	    Iterator<String> it = keys.iterator();

	    while (it.hasNext()) {
	        String key = it.next();
	        INDArray values = paramTable.get(key);
	        System.out.print(key+" ");//print keys
	        System.out.println(Arrays.toString(values.shape()));//print shape of INDArray
	        System.out.println(values);
	        multiLayerNetwork.setParam(key, Nd4j.rand(values.shape()));//set some random values
	    }
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		new NeuralNetwork().createBrain();
	}

}
