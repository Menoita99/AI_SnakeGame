package com.nn.train;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import com.gui.Gui;
import com.gui.Launch;
import com.logic.GameLogic;
import com.logic.Snake;
import com.neural.NeuralNetwork;
import com.utils.ExcelWritter;

public class GeneticEngine {

	public static final int isCostum = 2;
	public static final float motationRate = 0.01f;

	public static void main(String[] args) throws InterruptedException {
		//train(args);
		// load();
		// getBestSeed();
		loadSnakes();
	}

	public static void loadSnakes() throws InterruptedException {
//		Snake s = Snake.load("Snakes//SenInput//NewBestSnake3.snake");
//		new Thread(() -> Launch.main(null)).start();
//		Thread.sleep(1000);
//		Gui.getINSTANCE().playWithNeuralNetwork(s.getBrain(), 0); // Sen input
//		Snake s1 = Snake.load("standardInput.snake");
//		new Thread(() -> Launch.main(null)).start();
//		Thread.sleep(1000);
//		Gui.getINSTANCE().playWithNeuralNetwork(s1.getBrain(), 1);// Standard input
		Snake s2 = Snake.load("Snakes//StarInput//Snake34Score.snake");
		new Thread(() -> Launch.main(null)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(s2.getBrain(), 2); //Buble - Star input

	}

	public static void load() throws InterruptedException {
		Snake s = Snake.load("bubleInput.snake");
		new Thread(() -> Launch.main(null)).start();
		Thread.sleep(1000);
		Gui.getINSTANCE().playWithNeuralNetwork(s.getBrain(), isCostum);
	}

	public static void getBestSeed() {
		float maxScore = 0;
		long seed = 0;
		long currentTimeMillis = System.currentTimeMillis();
		for (long i = currentTimeMillis - 50000; i < currentTimeMillis; i++) {
			System.out.println(i);
			GameLogic gl = new GameLogic(Snake.FIELD, Snake.FIELD, i);
			Snake s = Snake.load("Snake2.snake");
			s.getBody().clear();
			s.getBody().add(new Point(Snake.FIELD / 2, Snake.FIELD / 2));
			gl.setSnake(s);
			s.setGl(gl);
			while (!s.isDead()) {
				s.look();
				s.thinkAndMove();
			}
			if (maxScore < gl.getScore()) {
				maxScore = gl.getScore();
				seed = i;
				System.err.println(gl.getScore());
			}
		}
		System.out.println(seed + "|||" + maxScore);
	}

	public static void train(String[] args) throws InterruptedException {
		Population pop = new Population(150, isCostum);

		 //pop.getSnakes()[0]= Snake.load("bubleInput.snake");
		 pop.getSnakes()[0]= Snake.load("Snakes//StarInput//GoodieSnakie.snake");
		 pop.getSnakes()[1]= Snake.load("Snakes//StarInput//Snake34Score.snake");
		 pop.getSnakes()[2]= Snake.load("Snakes//StarInput//BeastSnake.snake");
		int gens = 1000;

		ArrayList<String> genarations = new ArrayList<>();
		ArrayList<String> scores = new ArrayList<>();
		ArrayList<String> fitnesses = new ArrayList<>();
		ArrayList<String> bestscorePerGen = new ArrayList<>();
		ArrayList<String> bestFitnessPerGen = new ArrayList<>();

		int i = 0;
		long start = System.currentTimeMillis();

		while (i < gens) {
			if (pop.done()) {
				pop.calculateFitness();
				pop.naturalSelection();

				genarations.add(Integer.toString(i));
				scores.add(Integer.toString(pop.calculateAverageScore()));
				fitnesses.add(Float.toString(pop.calculateAverageFitness()));
				bestscorePerGen.add(Integer.toString(pop.getGenBestSnake().getScore()));
				bestFitnessPerGen.add(Float.toString(pop.getGenBestSnake().calculateFitness()));

				System.out.println("----------------------------");
				System.out.println("Gen : " + i + " Score: " + pop.getGenBestSnake().getScore() + " fitness "
						+ pop.getGenBestSnake().calculateFitness());
				System.out.println("Gen: " + i + "|Average Score: " + pop.calculateAverageScore() + "|Average fitness:"
						+ pop.calculateAverageFitness());
				System.out.println("best of best Score: " + pop.getBestSnake().getScore() + " fitness "
						+ pop.getBestSnake().calculateFitness());
				System.out.println("Time " + (System.currentTimeMillis() - start));

				start = System.currentTimeMillis();
				i++;
				if (i != 0 && i % 500 == 0) {
					pop.getBestSnake().save();
					pop.getGenBestSnake().save();
					System.out.println("Checkpoint");
				}
			} else {
				pop.update();
			}
		}
		pop.getBestSnake().save();
//
//		ExcelWritter.write(genarations, scores, fitnesses,bestscorePerGen, bestFitnessPerGen ,"8_Neurons_"+ motationRate + "MutationRate_" +gens+"Gens_"+pop.getSnakes().length+"Pop_"+Snake.FIELD+"Field");
//
		for (int j = 0; j < 10; j++) {
			NeuralNetwork brain = pop.getBestSnake().getBrain().clone();
			Snake s = new Snake(brain);
			s.setCostum(isCostum);
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
