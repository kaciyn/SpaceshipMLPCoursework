package coursework;

import java.util.ArrayList;
import java.util.Random;

import model.Fitness;
import model.Individual;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;

/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * 
 * You Can Use This Class to implement your EA or implement your own class that extends {@link NeuralNetwork} 
 * 
 */
public class  ExampleEvolutionaryAlgorithm extends NeuralNetwork {
	
	//TODO move down if not used everywhere
	Random random;
	
	/**
	 * The Main Evolutionary Loop
	 */
	@Override
	public void run() {		
		//Initialise a population of Individuals with random weights
		population = initialise();

		//Record a copy of the best Individual in the population
		best = getBest();
		System.out.println("Best From Initialisation " + best);

		/**
		 * main EA processing loop
		 */		
		
		while (evaluations < Parameters.maxEvaluations) {

			/**
			 * this is a skeleton EA - you need to add the methods.
			 * You can also change the EA if you want 
			 * You must set the best Individual at the end of a run
			 * 
			 */

			// Select 2 Individuals from the current population. Currently returns random Individual
			Individual parent1 = select(); 
			Individual parent2 = select();

			// Generate a child by crossover. Not Implemented			
			ArrayList<Individual> children = reproduce(parent1, parent2);			
			
			//mutate the offspring
			mutate(children);
			
			// Evaluate the children
			evaluateIndividuals(children);			

			// Replace children in population
			replace(children);

			// check to see if the best has improved
			best = getBest();
			
			// Implemented in NN class. 
			outputStats();
			
			//Increment number of completed generations			
		}

		//save the trained network to disk
		saveNeuralNetwork();
	}

	

	/**
	 * Sets the fitness of the individuals passed as parameters (whole population)
	 * 
	 */
	private void evaluateIndividuals(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}


	/**
	 * Returns a copy of the best individual in the population
	 * 
	 */
	private Individual getBest() {
		best = null;
		for (Individual individual : population) {
			if (best == null) {
				best = individual.copy();
			} else if (individual.fitness < best.fitness) {
				best = individual.copy();
			}
		}
		return best;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			//chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}

	/**
	 * Selection --
	 * 
	 * NEEDS REPLACED with proper selection this just returns a copy of a random
	 * member of the population
	 */
	private Individual select() {
		
		//tournament selection
		
		var pickedIndividual=population.get(random.nextInt(population.size()));
		var bestFitness = pickedIndividual.fitness;
		var bestIndividual = pickedIndividual;
		
		for (var i = 0; i < Parameters.tournamentSize - 1; i++)
		{
			pickedIndividual=population.get(random.nextInt(population.size()));
			
			if (pickedIndividual.fitness> bestFitness)
			{
				bestFitness = pickedIndividual.fitness;
				bestIndividual = pickedIndividual;
			}
		}
		
		Individual parent = bestIndividual;
		return parent.copy();
	}

	/**
	 * Crossover / Reproduction
	 * 
	 * NEEDS REPLACED with proper method this code just returns exact copies of the
	 * parents. 
	 */
	private ArrayList<Individual> reproduce(Individual parent1, Individual parent2) {
		var chromosomeLength =parent1.chromosome.length;
		ArrayList<Individual> children = new ArrayList<>();
		children.add(parent1.copy());
		children.add(parent2.copy());			
		return children;
	}
	
	// crossover
	public int[] onePointCrossover(int chromosomeLength,Individual parent1, Individual parent2)
	{
		// one point
		
		// pick crosspoint at random
		var crossPoint = random.nextInt(chromosomeLength);
		
		var child= new Individual();
		
		// first part of child is copied from parent1, and 2nd
		// part from parent2
		child=parent1.copy();
		
		for (var j = 0; j < crossPoint; j++) {
			child.chromosome[j] = parent1.chromosome[j];
		}
		for (j = crossPoint; j < (length); j++){
			child[j] = population[parent2, j];}
		return (child);
	}
	
	public int[] twoPointCrossover(Individual parent1, Individual parent2)
	{
		//TODO MAKE SURE THIS IS ACTUALLY WORKING AS EXPECTED
		// two points
		int crossPoint1, crossPoint2, j;
		
		// pick crosspoint1 at random, has to leave enough room for at least one gene after crosspoint 2 (otherwise it'd just be a 1 point crossover)
		crossPoint1 = random.Next(length - 1);
		
		//crosspoint2 -"-, has to be between crosspoint 1 and end of chromosome, again leaving at least one gene between crosspoints
		crossPoint2 = random.Next(crossPoint1 + 1, length);
		
		// up to cp1 is copied from p1, between cp1 and 2 from p2, cp2 to end from p1 again
		for (j = 0; j < crossPoint1; j++)
			child[j] = population[p1, j];
		
		for (j = 0; j < crossPoint2; j++)
			child[j] = population[p2, j];
		
		for (j = crossPoint2; j < (length); j++)
			child[j] = population[p1, j];
		return (child);
	}
	
	public int[] uniformCrossover(Individual parent1, Individual parent2)
	{
		int j;
		//TODO DOUBLE CHECK PLS
		for (j = 0; j < length; j++)
			if (random.Next() >= 0.5)
			{
				child[j] = population[p1, j];
			}
			else
			{
				child[j] = population[p2, j];
			}
		
		return (child);
	}
	/**
	 * Mutation
	 * 
	 * 
	 */
	private void mutate(ArrayList<Individual> individuals) {		
		for(Individual individual : individuals) {
			for (int i = 0; i < individual.chromosome.length; i++) {
				if (Parameters.random.nextDouble() < Parameters.mutateRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[i] += (Parameters.mutateChange);
					} else {
						individual.chromosome[i] -= (Parameters.mutateChange);
					}
				}
			}
		}		
	}

	/**
	 * 
	 * Replaces the worst member of the population 
	 * (regardless of fitness)
	 * 
	 */
	private void replace(ArrayList<Individual> individuals) {
		for(Individual individual : individuals) {
			int idx = getWorstIndex();		
			population.set(idx, individual);
		}		
	}

	

	/**
	 * Returns the index of the worst member of the population
	 * @return
	 */
	private int getWorstIndex() {
		Individual worst = null;
		int idx = -1;
		for (int i = 0; i < population.size(); i++) {
			Individual individual = population.get(i);
			if (worst == null) {
				worst = individual;
				idx = i;
			} else if (individual.fitness > worst.fitness) {
				worst = individual;
				idx = i; 
			}
		}
		return idx;
	}	

	@Override
	public double activationFunction(double x) {
		if (x < -20.0) {
			return -1.0;
		} else if (x > 20.0) {
			return 1.0;
		}
		return Math.tanh(x);
	}
}
