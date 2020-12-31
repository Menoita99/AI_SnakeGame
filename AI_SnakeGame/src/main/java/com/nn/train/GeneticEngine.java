package com.nn.train;

import com.gui.Gui;
import com.gui.Launch;

public class GeneticEngine {

	

	public static void main(String[] args) throws InterruptedException {
		System.out.println("comecei");
		Population pop = new Population(2000);
		int gens = 100;
		int i = 0;
		long start = System.currentTimeMillis();
		while (i < gens) {
			if(pop.done()) {
				pop.calculateFitness();
				pop.naturalSelection();
				System.out.println("----------------------------");
				System.out.println("Gen : "+i+" Score: "+pop.getGenBestSnake().getScore()+" fitness "+pop.getGenBestSnake().calculateFitness());
				System.out.println("best of best : "+i+" Score: "+pop.getBestSnake().getScore()+" fitness "+pop.getBestSnake().calculateFitness());
				System.out.println("Time "+(System.currentTimeMillis()-start));
				start = System.currentTimeMillis();
				i++;
			} else {
				pop.update();
			}
		}
		
		new Thread(() -> Launch.main(args)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(pop.getBestSnake().getBrain());
	}

}