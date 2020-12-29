package com.nn.train;

import java.util.List;
import java.util.function.Function;

import org.nd4j.linalg.api.ndarray.INDArray;

import com.neural.NeuralNetwork;

import io.jenetics.DoubleGene;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Problem;

public class GeneticTraining implements Problem<List<INDArray> , DoubleGene ,Double>{

	@Override
	public Function<List<INDArray>, Double> fitness() {
		return list -> new NeuralNetwork(list).play();
	}

	@Override
	public Codec<List<INDArray>, DoubleGene> codec() {
		// TODO Auto-generated method stub
		return null;
	}


	
	
}
