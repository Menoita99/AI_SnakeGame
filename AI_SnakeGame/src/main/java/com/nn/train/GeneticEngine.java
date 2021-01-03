package com.nn.train;


import java.util.ArrayList;

import com.gui.Gui;
import com.gui.Launch;
import com.logic.Snake;
import com.neural.NeuralNetwork;
import com.utils.ExcelWritter;


public class GeneticEngine {



	public static final boolean isCostum = false;




	public static void main(String[] args) throws InterruptedException {
		train(args);
//		load();
	}



	public static void load() throws InterruptedException { 
		Snake s = Snake.load("NewBestSnake.snake");
		new Thread(() -> Launch.main(null)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(s.getBrain(),isCostum);
	}




	public static void train(String[] args) throws InterruptedException {
		Population pop = new Population(150,isCostum);

//		for(int i = 0; i< 10;i++) {
//			pop.getSnakes()[i*4] = Snake.load("ConsistentSnake.snake").crossover(Snake.load("NewBestSnake6.snake"));
//			pop.getSnakes()[i*4+1] = Snake.load("VeryverygoodSnakeFrom0with0.9.snake").crossover(Snake.load("NewBestSnake3.snake"));
//			pop.getSnakes()[i*4+2] = Snake.load("NewBestSnake5(Diff).snake").crossover(Snake.load("NewBestSnake4.snake"));
//			pop.getSnakes()[i*4+3] = Snake.load("hibrid.snake").crossover(Snake.load("ConsistentSnake.snake"));
//		}
		
		int gens = 2000;
		
		ArrayList<String> genarations = new ArrayList<>();
		ArrayList<String> scores = new ArrayList<>();
		ArrayList<String> fitnesses = new ArrayList<>();

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
					pop.getGenBestSnake().save();
					System.out.println("Checkpoint");
				}
			} else {
				pop.update();
			}
		}
		pop.getBestSnake().save();

		ExcelWritter.write(genarations, scores, fitnesses,gens+"Gens_"+pop.getSnakes().length+"Pop_"+Snake.FIELD+"Field");

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
		Gui.getINSTANCE().playWithNeuralNetwork(pop.getBestSnake().getBrain().clone(),isCostum);
	}

}
