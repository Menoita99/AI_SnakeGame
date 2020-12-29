package com.nn.train;

import com.neural.NeuralNetwork;

import io.jenetics.DoubleGene;
import io.jenetics.Mutator;
import io.jenetics.Optimize;
import io.jenetics.Phenotype;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.SinglePointCrossover;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;

public class GeneticEngine {

	private Engine<DoubleGene, Double> engine;


	public void setUpEngine() {
		engine = Engine.builder(new GeneticTraining())
				.optimize(Optimize.MAXIMUM)
				.populationSize(50) 
				.offspringFraction(0.75)//0.6 standard
				.survivorsSelector (new TournamentSelector <>(2) )  //standard new TournamentSelector <>(3)
				.offspringSelector (new RouletteWheelSelector <>() ) 
				.alterers(new Mutator<>(0.1), new SinglePointCrossover<>(0.9))
				.build();
		
	}


	public EvolutionResult<DoubleGene, Double> evaluate(EvolutionStatistics<Double,?> statistics) {
		EvolutionResult<DoubleGene, Double> result = engine.stream()
				.limit(50)
				.peek(er -> System.out.println("Fitness: "+er.bestPhenotype().fitness()))
				.peek(statistics)
				.collect(EvolutionResult.toBestEvolutionResult());
		System.out.println(statistics);
		return result;
	}
	

	public static void main(String[] args) {
		EvolutionStatistics<Double,?> statistics = EvolutionStatistics.ofNumber();
		GeneticEngine gan = new GeneticEngine();
		gan.setUpEngine();
		Phenotype<DoubleGene, Double> bestPhenotype = gan.evaluate(statistics).bestPhenotype();
		System.out.println(bestPhenotype);
		new NeuralNetwork(new GeneticTraining().decode(bestPhenotype.genotype()));
	}
}
