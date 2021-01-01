package com.nn.train;


import java.util.ArrayList;

import com.gui.Gui;
import com.gui.Launch;
import com.logic.Snake;
import com.neural.NeuralNetwork;
import com.utils.ExcelWritter;


public class GeneticEngine {



	public static void main(String[] args) throws InterruptedException {
		train(args);
//		load();
		System.out.println();
	}



	public static void load() throws InterruptedException {
		Snake s = Snake.load("ConsistentSnake.snake");
		new Thread(() -> Launch.main(null)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(s.getBrain());
	}




	public static void train(String[] args) throws InterruptedException {
		System.out.println("comecei");
		Population pop = new Population(150);
//		pop.getSnakes()[0] = Snake.load("ConsistentSnake.snake");
//		pop.getSnakes()[1] = Snake.load("ConsistentInvertedSnake.snake");
		for(int i = 0; i< 20;i++)
			pop.getSnakes()[i] = Snake.load("ConsistentSnake.snake").crossover(Snake.load("ConsistentInvertedSnake.snake"));
		int gens = 1500;
		ArrayList<String> genarations = new ArrayList<>();
		ArrayList<String> scores = new ArrayList<>();
		ArrayList<String> fitnesses = new ArrayList<>();

		pop.getSnakes()[0] = Snake.load("ConsistentSnake.snake");
		pop.getSnakes()[1] = Snake.load("BestSnakeBest.snake");
		
		int gens = 500;
		int i = 0;
		long start = System.currentTimeMillis();
		while (i < gens) {
			if(pop.done()) {
				pop.calculateFitness();
				pop.naturalSelection();
				genarations.add(Integer.toString(i));
				scores.add(Integer.toString(pop.calculateAverageScore()));
				fitnesses.add(Float.toString(pop.calculateAverageFitness()));
				System.out.println("----------------------------");
				System.out.println("Gen : "+i+" Score: "+pop.getGenBestSnake().getScore()+" fitness "+pop.getGenBestSnake().calculateFitness());
				System.out.println("Gen: " + i + "|Average Score: " + pop.calculateAverageScore() +"|Average fitness:" + pop.calculateAverageFitness());
				System.out.println("best of best Score: "+pop.getBestSnake().getScore()+" fitness "+pop.getBestSnake().calculateFitness());
				System.out.println("Time "+(System.currentTimeMillis()-start));
				start = System.currentTimeMillis();
				i++;
				if(i!= 0 && i%500 == 0) {
					pop.getBestSnake().save();
					System.out.println("Checkpoint");
				}
			} else {
				pop.update();
				if(i!= 0 && i%1000 == 0) {
					pop.getBestSnake().save();
				}
			}
		}
		pop.getBestSnake().save();
		ExcelWritter.write(genarations, scores, fitnesses);
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
