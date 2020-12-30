package com.nn.train;

import java.util.Random;

import com.logic.Snake;

public class Population {

	private Snake[] snakes;
	private Snake bestSnake;

	private int bestSnakeScore = 0;
	private int gen = 0;
	private int samebest = 0;

	private float bestFitness = 0;
	private float fitnessSum = 0;

	public Population(int size) {
		snakes = new Snake[size];
		for (int i = 0; i < snakes.length; i++)
			snakes[i] = new Snake();
		bestSnake = snakes[0].clone();
	}

	public boolean done() { // check if all the snakes in the population are dead
		for (int i = 0; i < snakes.length; i++) {
			if (!snakes[i].isDead())
				return false;
		}
		if (!bestSnake.isDead())
			return false;
		return true;
	}

	public void update() {  //update all the snakes in the generation
	      if(!bestSnake.isDead()) {  //if the best snake is not dead update it, this snake is a replay of the best from the past generation
	          bestSnake.look();
	          bestSnake.think();
	          bestSnake.move();
	       }
	       for(int i = 0; i < snakes.length; i++) {
	         if(!snakes[i].isDead()) {
	            snakes[i].look();
	            snakes[i].think();
	            snakes[i].move(); 
	         }
	       }
	    }
	
	public void setBestSnake() {  //set the best snake of the generation
	       float max = 0;
	       int maxIndex = 0;
	       for(int i = 0; i < snakes.length; i++) {
	          if(snakes[i].getFitness() > max) {
	             max = (float) snakes[i].getFitness();
	             maxIndex = i;
	          }
	       }
	       if(max > bestFitness) {
	         bestFitness = max;
	         bestSnakeScore = snakes[maxIndex].getScore();
	         //samebest = 0;
	         //mutationRate = defaultMutation;
	       } else {
	         /*
	         samebest++;
	         if(samebest > 2) {  //if the best snake has remained the same for more than 3 generations, raise the mutation rate
	            mutationRate *= 2;
	            samebest = 0;
	         }*/
	       }
	   }
	
	public Snake selectParent() {  //selects a random number in range of the fitnesssum and if a snake falls in that range then select it
	      float rand = new Random().nextFloat() * fitnessSum;
	      float summation = 0;
	      for(int i = 0; i < snakes.length; i++) {
	         summation += snakes[i].getFitness();
	         if(summation > rand) {
	           return snakes[i];
	         }
	      }
	      return snakes[0];
	   }
	
	public void naturalSelection() {
	      Snake[] newSnakes = new Snake[snakes.length];
	      
	      setBestSnake();
	      calculateFitnessSum();
	      
	      newSnakes[0] = bestSnake.clone();  //add the best snake of the prior generation into the new generation
	      for(int i = 1; i < snakes.length; i++) {
	         Snake child = selectParent().crossover(selectParent());
	         child.mutate();
	         newSnakes[i] = child;
	      }
	      snakes = newSnakes.clone();
	      evolution.add(bestSnakeScore);
	      gen+=1;
	   }
	   
	  public void mutate() {
	       for(int i = 1; i < snakes.length; i++) {  //start from 1 as to not override the best snake placed in index 0
	          snakes[i].mutate(); 
	       }
	   }
	   
	   public void calculateFitness() {  //calculate the fitnesses for each snake
	      for(int i = 0; i < snakes.length; i++) {
	         snakes[i].calculateFitness(); 
	      }
	   }
	   
	  public void calculateFitnessSum() {  //calculate the sum of all the snakes fitnesses
	       fitnessSum = 0;
	       for(int i = 0; i < snakes.length; i++) {
	         fitnessSum += snakes[i].fitness; 
	      }
	   }
	}
	
	
}
