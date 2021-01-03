package com.nn.train;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

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

	private boolean useCostumInput;

	public Population(int size) {
		populationSize = size;
		snakes = new Snake[size];
		for (int i = 0; i < snakes.length; i++)
			snakes[i] = new Snake();
		bestSnake = new Snake(snakes[0].getBrain());
	}
	
	public Population(Snake[] population) {
		populationSize = population.length + 1;
		snakes = population;
		bestSnake = new Snake(snakes[0].getBrain());
	}

	public Population(int size, boolean useCostumInput) {
		populationSize = size;
		this.useCostumInput = useCostumInput;
		snakes = new Snake[size];
		for (int i = 0; i < snakes.length; i++)
			snakes[i] = new Snake(useCostumInput);
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
				System.out.println("SET BEST SNAKE");
				bestSnake = snakes[i];
			}
		}
	}

	//Tournament selection size 5
	public Snake selectParent() { 
		Snake winner = null;
		for (int i = 0; i < 7 && i < snakes.length; i++) {
			Snake snake = snakes[new Random().nextInt(snakes.length)];
			if(winner == null || snake.calculateFitness() > winner.calculateFitness())
				winner = snake;
		}
		return winner;
	}

	public void naturalSelection() {
		setBestSnake();
		calculateFitnessSum();
		calculateScoreSum();
		mating();	
		mutate();
	}

	private void mating() {
		snakes = Arrays.stream(snakes).sorted((s1,s2)-> (int)(s1.calculateFitness()-s2.calculateFitness())).collect(Collectors.toList()).toArray(snakes);
		//		newSnakes[snakes.length-1] = new Snake(genBestSnake.getBrain()); // add the best snake of the prior generation into the new generation
		for (int i = 1; i < snakes.length; i++) {
			if(i < snakes.length*0.9) {
				Snake child = snakes[i].crossover(selectParent());
				child.mutate();
				snakes[i] = child;
			}else {
				snakes[i] = new Snake(snakes[i].getBrain());
			}
		}
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