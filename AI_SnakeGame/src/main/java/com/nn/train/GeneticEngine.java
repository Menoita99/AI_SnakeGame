package com.nn.train;


import com.gui.Gui;
import com.gui.Launch;
import com.logic.Snake;
import com.neural.NeuralNetwork;


public class GeneticEngine {



	public static void main(String[] args) throws InterruptedException {
		train(args);
//		load();
	}



	public static void load() throws InterruptedException {
		Snake s = Snake.load("BestSnake(5).snake");
		new Thread(() -> Launch.main(null)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(s.getBrain());
	}




	public static void train(String[] args) throws InterruptedException {
		System.out.println("comecei");
		Population pop = new Population(1000,true);
//		pop.getSnakes()[0] = Snake.load("BestSnake(4).snake");
//		pop.getSnakes()[1] = Snake.load("BestSnake(1).snake");
//		pop.getSnakes()[2] = Snake.load("BestSnake(2).snake");
//		pop.getSnakes()[3] = Snake.load("BestSnake(3).snake");
		int gens = 5000;
		int i = 0;
		long start = System.currentTimeMillis();
		while (i < gens) {
			if(pop.done()) {
				pop.calculateFitness();
				pop.naturalSelection();
				System.out.println("----------------------------");
				System.out.println("Gen : "+i+" Score: "+pop.getGenBestSnake().getScore()+" fitness "+pop.getGenBestSnake().calculateFitness());
				System.out.println("Gen: " + i + "|Average Score: " + pop.calculateAverageScore() +"|Average fitness:" + pop.calculateAverageFitness());
				System.out.println("best of best Score: "+pop.getBestSnake().getScore()+" fitness "+pop.getBestSnake().calculateFitness());
				System.out.println("Time "+(System.currentTimeMillis()-start));
				start = System.currentTimeMillis();
				i++;
			} else {
				pop.update();
			}
			if(i!= 0 && i%200 == 0) {
				pop.getBestSnake().save();
				System.out.println("Checkpoint");
			}
		}
		System.out.println("Saving best sanke");
		pop.getBestSnake().save();
		System.out.println("TEST BEST");
		for (int j = 0; j < 10; j++) {
			NeuralNetwork brain = pop.getBestSnake().getBrain().clone();
			Snake s = new Snake(brain);
			while(!s.isDead()) {
				s.look();
				s.thinkAndMove();
			}
			System.out.println("Test "+j+" score "+s.getScore()+" fitness "+s.calculateFitness());
		}
		new Thread(() -> Launch.main(args)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(pop.getBestSnake().getBrain().clone());
	}

}
