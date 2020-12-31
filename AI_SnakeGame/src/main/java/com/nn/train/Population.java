package com.nn.train;

import java.util.Random;

import com.logic.Snake;

import lombok.Data;

@Data
public class Population {

	private int populationSize;
	private Snake[] snakes;
	private Snake bestSnake;
	private Snake genBestSnake;

//	private int bestSnakeScore = 0;
	private int gen = 0;
//	private int samebest = 0;

	private float fitnessSum = 0;

//	ArrayList<Integer> evolution = new ArrayList<>();

	public Population(int size) {
		populationSize = size;
		snakes = new Snake[size];
		for (int i = 0; i < snakes.length; i++)
			snakes[i] = new Snake();
		bestSnake = new Snake(snakes[0].getBrain());
	}

	public boolean done() { // check if all the snakes in the population are dead
		for (int i = 0; i < snakes.length; i++) {
			if (!snakes[i].isDead())
				return false;
		}
//		if (!bestSnake.isDead())
//			return false;
		return true;
	}

	public void update() { // update all the snakes in the generation
//		if (!bestSnake.isDead()) { // if the best snake is not dead update it, this snake is a replay of the best
//									// from the past generation
//			bestSnake.look();
//			bestSnake.think();
//			bestSnake.move();
//		}
		for (int i = 0; i < snakes.length; i++) {
			if (!snakes[i].isDead()) {
				snakes[i].look();
				snakes[i].thinkAndMove();
//				System.out.println("--------------------");
//				snakes[i].getBody().forEach(System.out::println);//			snakes[i].move();
			}
		}
	}

	public void setBestSnake() { // set the best snake of the generation
//		float max = 0;
//		int maxIndex = 0;
//		if(bestSnake == null)
//			bestSnake = snakes[0];
		genBestSnake = snakes[0];
		for (int i = 0; i < snakes.length; i++) {
			if (snakes[i].calculateFitness() > genBestSnake.calculateFitness()) {
				genBestSnake = snakes[i];
			}
			if (snakes[i].calculateFitness() > bestSnake.calculateFitness()) {
				bestSnake = snakes[i];
				System.out.println("Setted best Snake "+bestSnake.getScore());
			}
		}
//		if (max > bestFitness) {
//			bestFitness = max;
//			bestSnakeScore = snakes[maxIndex].getScore();
			// samebest = 0;
			// mutationRate = defaultMutation;
//		} else {
			/*
			 * samebest++; if(samebest > 2) { //if the best snake has remained the same for
			 * more than 3 generations, raise the mutation rate mutationRate *= 2; samebest
			 * = 0; }
			 */
//		}
	}

	public Snake selectParent() { // selects a random number in range of the fitnesssum and if a snake falls in
									// that range then select it
//		float rand = new Random().nextFloat() * fitnessSum;
//		float summation = 0;
//		for (int i = 0; i < snakes.length; i++) {
//			summation += snakes[i].calculateFitness();
//			if (summation > rand) {
//				return snakes[i];
//			}
//		}
//		return snakes[0];
		//Tournament selection size 5
		Snake winner = null;
		for (int i = 0; i < 5 && i < snakes.length; i++) {
			Snake snake = snakes[new Random().nextInt(snakes.length)];
			if(winner == null || snake.calculateFitness() > winner.calculateFitness())
				winner = snake;
		}
		return winner;
	}

	public void naturalSelection() {
		Snake[] newSnakes = new Snake[snakes.length];

		setBestSnake();
		calculateFitnessSum();
		mutate();
		
		newSnakes[0] = new Snake(bestSnake.getBrain()); // add the best snake of the prior generation into the new generation
		newSnakes[1] = new Snake(genBestSnake.getBrain()); // add the best snake of the prior generation into the new generation
		for (int i = 2; i < snakes.length; i++) {
			Snake child = selectParent().crossover(selectParent());
			child.mutate();
			newSnakes[i] = child;
		}
		snakes = newSnakes.clone();
//		evolution.add(bestSnakeScore);
		gen += 1;
	}

	public void mutate() {
		for (int i = 1; i < snakes.length; i++) // start from 1 as to not override the best snake placed in index 0
			snakes[i].mutate();
	}

	public void calculateFitness() { // calculate the fitnesses for each snake
		for (int i = 0; i < snakes.length; i++)
			snakes[i].calculateFitness();
	}

	public void calculateFitnessSum() { // calculate the sum of all the snakes fitnesses
		fitnessSum = 0;
		for (int i = 0; i < snakes.length; i++)
			fitnessSum += snakes[i].calculateFitness();
	}
	
	public float calculateAverageFitness() { // calculate the sum of all the snakes fitnesses
		return fitnessSum/populationSize;
	}
}