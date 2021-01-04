package com.nn.train;


import java.awt.Point;
import java.util.ArrayList;

import com.gui.Gui;
import com.gui.Launch;
import com.logic.GameLogic;
import com.logic.Snake;
import com.neural.NeuralNetwork;
import com.utils.ExcelWritter;


public class GeneticEngine {



	public static final boolean isCostum = false;
	public static final float motationRate = 0.001f;




	public static void main(String[] args) throws InterruptedException {
		train(args);
		//load();
		//getBestSeed();
	}



	public static void load() throws InterruptedException {
		Snake s = Snake.load("NewBestSnake.snake");
		new Thread(() -> Launch.main(null)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(s.getBrain(),isCostum);
	}
	
	public static void getBestSeed() {
		float maxScore = 0;
		long seed = 0;
		long currentTimeMillis = System.currentTimeMillis();
		for (long i = currentTimeMillis - 50000; i < currentTimeMillis ; i++) {
			System.out.println(i);
			GameLogic gl = new GameLogic(Snake.FIELD, Snake.FIELD, i);
			Snake s = Snake.load("NewBestSnake.snake");
			s.getBody().clear();
			s.getBody().add(new Point(Snake.FIELD/2,Snake.FIELD/2));
			gl.setSnake(s);
			s.setGl(gl);
			while(!s.isDead()) {
				s.look();
				s.thinkAndMove();
			}
			if(maxScore < gl.getScore()) {
				maxScore = gl.getScore();
				seed = i;
				System.err.println(gl.getScore());
			}
		}
		System.out.println(seed + "|||" + maxScore);
	}




	public static void train(String[] args) throws InterruptedException {
		Population pop = new Population(100,isCostum);

		//pop.getSnakes()[0]= Snake.load("NewBestSnake.snake");
		
		int gens = 1000;

		ArrayList<String> genarations = new ArrayList<>();
		ArrayList<String> scores = new ArrayList<>();
		ArrayList<String> fitnesses = new ArrayList<>();
		ArrayList<String> bestscorePerGen = new ArrayList<>();
		ArrayList<String> bestFitnessPerGen = new ArrayList<>();

		int i = 0;
		long start = System.currentTimeMillis();

		while (i < gens) {
			if(pop.done()) {
				pop.calculateFitness();
				pop.naturalSelection();

				genarations.add(Integer.toString(i));
				scores.add(Integer.toString(pop.calculateAverageScore()));
				fitnesses.add(Float.toString(pop.calculateAverageFitness()));
				bestscorePerGen.add(Integer.toString(pop.getGenBestSnake().getScore()));
				bestFitnessPerGen.add(Float.toString(pop.getGenBestSnake().calculateFitness()));

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

		ExcelWritter.write(genarations, scores, fitnesses,bestscorePerGen, bestFitnessPerGen ,"Our24_Neurons_"+ motationRate + "MutationRate_" +gens+"Gens_"+pop.getSnakes().length+"Pop_"+Snake.FIELD+"Field");
//
//		for (int j = 0; j < 10; j++) {
//			NeuralNetwork brain = pop.getBestSnake().getBrain().clone();
//			Snake s = new Snake(brain);
//			s.setCostum(isCostum);
//			while(!s.isDead()) {
//				s.look();
//				s.thinkAndMove();
//			}
//			System.out.println("Test "+j+" score "+s.getScore()+" fitness "+s.calculateFitness());
//		}
//		new Thread(() -> Launch.main(args)).start();
//		Thread.sleep(1000);
//		Gui.getINSTANCE().playWithNeuralNetwork(pop.getBestSnake().getBrain().clone(),isCostum);
	}

}
