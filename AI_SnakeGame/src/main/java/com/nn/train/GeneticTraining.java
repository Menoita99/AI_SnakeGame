package com.nn.train;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;

import com.neural.NeuralNetwork;

import io.jenetics.Chromosome;
import io.jenetics.DoubleChromosome;
import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Problem;
import io.jenetics.util.Factory;

public class GeneticTraining implements Problem<List<INDArray> , DoubleGene ,Double>{

	@Override
	public Function<List<INDArray>, Double> fitness() {
		return list -> new NeuralNetwork(list).play();
	}

	@Override
	public Codec<List<INDArray>, DoubleGene> codec() {
		return new Codec<List<INDArray>, DoubleGene>() {
			@Override
			public Factory<Genotype<DoubleGene>> encoding() {
				int input = 8;
				int hidden = 5;
				int output = 3;

				LinkedList<DoubleChromosome> chromosomes = new LinkedList<>();
				
				for (int j = 0; j < input; j++) 
					chromosomes.add(DoubleChromosome.of(-1.0, 1.0, hidden));
				
				//bias
				chromosomes.add(DoubleChromosome.of(-1.0, 1.0, hidden));
				
				for (int j = 0; j < hidden; j++) 
					chromosomes.add(DoubleChromosome.of(-1.0, 1.0, output));
				
				//bias
				chromosomes.add(DoubleChromosome.of(-1.0, 1.0, output));
				
				return Genotype.of(chromosomes);
			}

			
			
			@Override
			public Function<Genotype<DoubleGene>, List<INDArray>> decoder() {
				return genotype -> {
					LinkedList<INDArray> list = new LinkedList<>();
					
					int input = 8;
					int hidden = 5;
					int output = 3;
					
					double[][] inputLayer = new double[input][hidden];
					double[][] inputBiasLayer = new double[1][hidden];
					double[][] hiddenLayer = new double[hidden][output];
					double[][] hiddenBiasLayer = new double[1][output];
					
					int i = 0;
					
					Iterator<Chromosome<DoubleGene>> iterator = genotype.iterator();
					while(iterator.hasNext()) {
						double [] array = ((DoubleChromosome) iterator.next()).toArray();
						if(i<input) 
							inputLayer[i] = array;
						else if(i == input)
							inputBiasLayer[0] = array;
						else if(i > input && i < input+hidden+1) 
							hiddenLayer[(input+1)-i] = array;
						else if(i == input+hidden+1)
							hiddenBiasLayer[0] = array;
					}
					
					list.add(new NDArray(inputLayer));
					list.add(new NDArray(inputBiasLayer));
					list.add(new NDArray(hiddenLayer));
					list.add(new NDArray(hiddenBiasLayer));
					
					return list;
				};
			}
		};
	}
}
