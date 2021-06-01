package coursework;

import java.util.ArrayList;

import model.*;

import static coursework.Parameters.*;

/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * <p>
 * You Can Use This Class to implement your EA or implement your own class that extends {@link NeuralNetwork}
 */
public class ExampleEvolutionaryAlgorithm extends NeuralNetwork
{
    
   
    
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
        
        //save the trained network + used parameters & fitness to disk
        saveParametersAndFitnessToCsv(saveNeuralNetworkAndReturnFilename());
        
    }
    
    /**
     * Sets the fitness of the individuals passed as parameters (whole population)
     */
    private void evaluateIndividuals(ArrayList<Individual> individuals) {
        for (Individual individual : individuals) {
            individual.fitness = Fitness.evaluate(individual, this);
        }
    }
    
    /**
     * Returns a copy of the best individual in the population
     */
    private Individual getBest() {
        best = null;
        for (Individual individual : population) {
            if (best == null) {
                best = individual.copy();
            }
            else if (individual.fitness < best.fitness) {
                best = individual.copy();
            }
        }
        return best;
    }
    
    /**
     * Generates a randomly initialised population
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
     * <p>
     * NEEDS REPLACED with proper selection this just returns a copy of a random
     * member of the population
     */
    private Individual select() {
        
        //tournament selection
        
        var pickedIndividual = population.get(random.nextInt(population.size()));
        var bestFitness = pickedIndividual.fitness;
        var bestIndividual = pickedIndividual;
        
        for (var i = 0; i < Parameters.tournamentSize - 1; i++) {
            pickedIndividual = population.get(random.nextInt(population.size()));
            
            if (pickedIndividual.fitness > bestFitness) {
                bestFitness = pickedIndividual.fitness;
                bestIndividual = pickedIndividual;
            }
        }
        
        Individual parent = bestIndividual;
        return parent.copy();
    }
    
    /**
     * Crossover / Reproduction
     * <p>
     * NEEDS REPLACED with proper method this code just returns exact copies of the
     * parents.
     */
    private ArrayList<Individual> reproduce(Individual parent1, Individual parent2) {
        ArrayList<Individual> children = new ArrayList<>();
        
        switch (crossoverType) {
            case 1:
                onePointCrossover(children, parent1, parent2);
                break;
            case 2:
                twoPointCrossover(children, parent1, parent2);
            case 3:
                uniformCrossover(children, parent1, parent2);
        }
        
        return children;
    }
    
    // crossover
    public ArrayList<Individual> onePointCrossover(ArrayList<Individual> children, Individual parent1, Individual parent2)
    {
        
        for (int i = 0; i < childrenPerReproduction; i++) {
            var child = new Individual();
            
            // one point
            
            // pick crosspoint at random
            var crossPoint = random.nextInt(getNumGenes());
            
            // first part of child is copied from parent1, and 2nd
            // part from parent2
            
            for (var j = 0; j < crossPoint; j++) {
                child.chromosome[j] = parent1.chromosome[j];
            }
            for (var j = crossPoint; j < (getNumGenes()); j++) {
                child.chromosome[j] = parent2.chromosome[j];
            }
            
            children.add(child);
        }
        
        return (children);
    }
    
    public ArrayList<Individual> twoPointCrossover(ArrayList<Individual> children, Individual parent1, Individual parent2)
    {
        for (int i = 0; i < childrenPerReproduction; i++) {
            
            var child = new Individual();
            
            //TODO MAKE SURE THIS IS ACTUALLY WORKING AS EXPECTED
            // two points
            
            // pick crosspoint1 at random, has to leave enough room for at least one gene after crosspoint 2 (otherwise it'd just be a 1 point crossover)
            var crossPoint1 = random.nextInt(getNumGenes() - 1);
            
            //crosspoint2 -"-, has to be between crosspoint 1 and end of chromosome, again leaving at least one gene between crosspoints
            var crossPoint2 = (crossPoint1 + 1) + random.nextInt(getNumGenes() - (crossPoint1 + 1) + 1);
            
            // up to cp1 is copied from p1, between cp1 and 2 from p2, cp2 to end from p1 again
            for (var j = 0; j < crossPoint1; j++) {
                child.chromosome[j] = parent1.chromosome[j];
            }
            for (var j = crossPoint1; j < crossPoint2; j++) {
                child.chromosome[j] = parent2.chromosome[j];
            }
            
            for (var j = crossPoint2; j < (getNumGenes()); j++) {
                child.chromosome[j] = parent1.chromosome[j];
            }
            children.add(child);
        }
        return (children);
    }
    
    public ArrayList<Individual> uniformCrossover(ArrayList<Individual> children, Individual parent1, Individual parent2)
    {
        int j;
        //TODO DOUBLE CHECK PLS
        for (j = 0; j < getNumGenes(); j++) {
            var child = new Individual();
            
            if (random.nextInt(1) >= 0.5) {
                child.chromosome[j] = parent1.chromosome[j];
            }
            else {
                child.chromosome[j] = parent2.chromosome[j];
            }
        }
        
        return (children);
    }
    
    /**
     * Mutation
     */
    private void mutate(ArrayList<Individual> individuals) {
        for (Individual individual : individuals) {
            for (int i = 0; i < individual.chromosome.length; i++) {
                if (Parameters.random.nextDouble() < Parameters.mutateRate) {
                    if (Parameters.random.nextBoolean()) {
                        individual.chromosome[i] += (Parameters.mutateChange);
                    }
                    else {
                        individual.chromosome[i] -= (Parameters.mutateChange);
                    }
                }
            }
        }
    }
    
    /**
     * Replaces the worst member of the population
     * (regardless of fitness)
     */
    //TODO this seems? implemented? like it doesn't not regard fitness
    private void replace(ArrayList<Individual> individuals) {
        for (Individual individual : individuals) {
            int index = getWorstIndex();
            population.set(index, individual);
        }
    }
    
    /**
     * Returns the index of the worst member of the population
     *
     * @return
     */
    private int getWorstIndex() {
        int index = 0;
        
        var worst = population.get(0);
        
        //why check if worst is null every time in the loop when it's only null before the loop runs
        
        for (int i = 1; i < population.size(); i++) {
            
            Individual individual = population.get(i);
            
            if (individual.fitness > worst.fitness) {
                worst = individual;
                index = i;
            }
        }
        return index;
    }
    
    @Override
    public double activationFunction(double x) {
        if (x < -20.0) {
            return -1.0;
        }
        else if (x > 20.0) {
            return 1.0;
        }
        return Math.tanh(x);
    }
    
    protected void saveParametersAndFitnessToCsv(String filename) {
        if (!filename.isEmpty() ) {
            var output = "";
            output+="Run ID,"+filename;
            output = output + Parameters.printParamsToCsv();
            output = output + "Training Set, " + LunarParameters.getDataSet() + "\r\n";
            output = output + "Fitness, " + this.best.fitness;
            StringIO.writeStringToFile(filename + "-PF.csv", output, false);
            System.out.println(output);
        }
    }
    
    protected String saveNeuralNetworkAndReturnFilename() {
        if (this.best != null && this.best.chromosome != null && this.best.chromosome.length >= 1) {
            String str = "";
            
            for(int i = 0; i < this.best.chromosome.length - 1; ++i) {
                str = str + this.best.chromosome[i] + ",";
            }
            
            str = str + this.best.chromosome[this.best.chromosome.length - 1] + "\r\n";
            str = str + Parameters.printParams();
            str = str + "Training Set " + LunarParameters.getDataSet() + "\r\n";
            str = str + "Fitness " + this.best.fitness;
            String filePrefix = System.currentTimeMillis() + "-" + Parameters.getNumHidden();
            StringIO.writeStringToFile(filePrefix + ".txt", str, false);
            System.out.println(str);
            
            return filePrefix;
        }
        return null;
    }
}
