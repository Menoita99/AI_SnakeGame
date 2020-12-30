package com.nn.train;

import com.gui.Gui;
import com.gui.Launch;

public class GeneticEngine {

	

	public static void main(String[] args) throws InterruptedException {
		System.out.println("comecei");
		Population pop = new Population(2000);
		int gens = 100;
		int i = 0;
		int bestScore = 0;
		while ( i < gens) {
			if(pop.done()) {
				bestScore = pop.getBestSnake().getScore();
				pop.calculateFitness();
				pop.naturalSelection();
				System.out.println("Gen : "+i+" Score: "+bestScore);
				i++;
			} else {
				pop.update();
			}
		}
		
		new Thread(() -> Launch.main(args)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(pop.getBestSnake());
	}

}