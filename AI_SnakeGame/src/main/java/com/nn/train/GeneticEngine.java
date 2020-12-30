package com.nn.train;

import com.gui.Gui;

public class GeneticEngine {

	

	public static void main(String[] args) {
		System.out.println("comecei");
		Population pop = new Population(20);
		int gens = 100;
		int i = 0;
		int bestScore = 0;
		while ( i < gens) {
			if(pop.done()) {
				bestScore = pop.getBestSnake().getScore();
				pop.calculateFitness();
				pop.naturalSelection();
				System.out.println("Gen : "+i+" Score: "+bestScore);
			} else {
				pop.update();
			}
		}
		
		Gui.launch(args);
//		Gui.getInstance().
	}

}