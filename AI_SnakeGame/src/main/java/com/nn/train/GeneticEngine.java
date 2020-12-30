package com.nn.train;

import java.io.File;
import java.io.IOException;

import com.neural.NeuralNetwork;

import io.jenetics.DoubleGene;
import io.jenetics.Mutator;
import io.jenetics.Optimize;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.SinglePointCrossover;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;

public class GeneticEngine {

	private Engine<DoubleGene, Double> engine;


	public void setUpEngine() {
//		engine = Engine.builder(new GeneticTraining())
//				.optimize(Optimize.MAXIMUM)
//				.populationSize(50)
//				.offspringFraction(0.6)
//				.survivorsSelector (new TournamentSelector <>(5)) 
//				.offspringSelector (new RouletteWheelSelector <>() ) 
//				.alterers(new Mutator<>(0.02), new SinglePointCrossover<>(0.8))
//				.build();
		
	}


	public EvolutionResult<DoubleGene, Double> evaluate(EvolutionStatistics<Double,?> statistics) {
		EvolutionResult<DoubleGene, Double> result = engine.stream()
				.limit(100)
				.peek(er -> System.out.println("Fitness: "+er.bestPhenotype().fitness()))
				.peek(statistics)
				.collect(EvolutionResult.toBestEvolutionResult());
		System.out.println(statistics);
		return result;
	}
	

	public static void main(String[] args) {
//		EvolutionStatistics<Double,?> statistics = EvolutionStatistics.ofNumber();
		GeneticEngine gan = new GeneticEngine();
		gan.setUpEngine();
		File f = new File("C:\\Users\\car31\\OneDrive\\Desktop\\NeuralNetwork.nn");
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}