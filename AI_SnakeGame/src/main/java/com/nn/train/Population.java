package com.nn.train;

import java.io.Serializable;
import java.util.Random;

import com.logic.Snake;

import lombok.Data;

@Data
public class Population implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int populationSize;
	private Snake[] snakes;
	private Snake bestSnake;
	private Snake genBestSnake;

	private int gen = 0;

	private float fitnessSum = 0;
	private int scoreSum = 0;

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
		return true;
	}

	public void update() { 
		for (int i = 0; i < snakes.length; i++) {
			if (!snakes[i].isDead()) {
				snakes[i].look();
				snakes[i].thinkAndMove();
			}
		}
	}

	public void setBestSnake() { // set the best snake of the generation
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
	}
	
	//Tournament selection size 5
	public Snake selectParent() { 
		
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
		calculateScoreSum();
		mutate();
		
//		newSnakes[1] = new Snake(bestSnake.getBrain()); // add the best snake of the prior generation into the new generation
		newSnakes[0] = new Snake(genBestSnake.getBrain()); // add the best snake of the prior generation into the new generation
		for (int i = 1; i < snakes.length; i++) {
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
	
	public void calculateScoreSum() { // calculate the sum of all the snakes fitnesses
		scoreSum = 0;
		for (int i = 0; i < snakes.length; i++)
			scoreSum += snakes[i].getScore();
	}
	
	public float calculateAverageFitness() { // calculate the sum of all the snakes fitnesses
		return fitnessSum/populationSize;
	}
	
	public int calculateAverageScore() {
		return scoreSum/populationSize;
	}
}