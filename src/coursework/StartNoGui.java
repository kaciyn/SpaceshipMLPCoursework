package coursework;

import model.Fitness;
import model.LunarParameters;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;
import model.StringIO;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static java.lang.Double.valueOf;

/**
 * Example of how to to run the {@link ExampleEvolutionaryAlgorithm} without the need for the GUI
 * This allows you to conduct multiple runs programmatically
 * The code runs faster when not required to update a user interface
 */
public class StartNoGui
{
    
    public static void main(String[] args) {
        /**
         * Train the Neural Network using our Evolutionary Algorithm
         *
         */
        
        createResultsFileIfNotExtant(Parameters.getResultsFilename());
        
        //Set the data set for training
        Parameters.setDataSet(DataSet.Training);
        
        String resultsSuffix;
        int numberOfRunsPerParameterConfiguration = 1;
        
        //loop testing config
//        Parameters.setMaxEvaluations(20);
//         numberOfRunsPerParameterConfiguration = 1;
        
        //actual run config
//        Parameters.setMaxEvaluations(20000);
//        var numberOfRunsPerParameterConfiguration = 10;
        
        //initial exploration config
//        Parameters.setMaxEvaluations(10000);
//        resultsSuffix = "_Exploration-1.csv";
    
        //exploration config 1.2
        Parameters.setMaxEvaluations(20000);
        numberOfRunsPerParameterConfiguration = 10;
        resultsSuffix = "_Exploration-2.csv";
    
        //exploration config 1.3
        Parameters.setMaxEvaluations(20000);
        numberOfRunsPerParameterConfiguration = 10;
        resultsSuffix = "_Exploration-3.csv";
    
        //exploration config 1.4
        Parameters.setMaxEvaluations(20000);
        numberOfRunsPerParameterConfiguration = 20;
        resultsSuffix = "_Exploration-4.csv";
        
        //Sets parameters to default configuration
        defaultParameters();
        
//        for (int i = 1; i <= 2; i++) {
//            Parameters.setResultsFileName("Crossover" + resultsSuffix);
//            createResultsFileIfNotExtant(Parameters.getResultsFilename());
//            Parameters.setCrossoverType(i);
//            runNeuralNet(numberOfRunsPerParameterConfiguration);
//            defaultParameters();
//        }
    
        for (int i = 1; i <= 3; i++) {
            Parameters.setResultsFileName("Replacement" + resultsSuffix);
            createResultsFileIfNotExtant(Parameters.getResultsFilename());
            Parameters.setCrossoverType(i);
            runNeuralNet(numberOfRunsPerParameterConfiguration);
            defaultParameters();
        }
//
//        for (int j = 1; j <= 32; j *= 2) {
//            Parameters.setResultsFileName("ChildrenPerReproduction" + resultsSuffix);
//            createResultsFileIfNotExtant(Parameters.getResultsFilename());
//            Parameters.setChildrenPerReproduction(j);
//            runNeuralNet(numberOfRunsPerParameterConfiguration);
//            defaultParameters();
//        }
        
        for (int k = 1; k <= 3; k++) {
            Parameters.setResultsFileName("GeneRange" + resultsSuffix);
            createResultsFileIfNotExtant(Parameters.getResultsFilename());
            
            Parameters.setMaxGene(k);
            Parameters.setMinGene(-k);
            Parameters.setMutateChange((double)(Parameters.getMaxGene() - Parameters.getMinGene()) / 150);
            
            runNeuralNet(numberOfRunsPerParameterConfiguration);
            defaultParameters();
        }
        
        for (int l = 5; l <= 7; l++) {
            Parameters.setResultsFileName("HiddenLayers" + resultsSuffix);
            createResultsFileIfNotExtant(Parameters.getResultsFilename());

            Parameters.setHidden(l);

            runNeuralNet(numberOfRunsPerParameterConfiguration);
            defaultParameters();
        }
        
        //just for funsies
        Parameters.setResultsFileName("HiddenLayers" + resultsSuffix);
        createResultsFileIfNotExtant(Parameters.getResultsFilename());
    
        Parameters.setHidden(20);
    
        runNeuralNet(numberOfRunsPerParameterConfiguration);
        defaultParameters();
        
//        for (int m = 300; m <= 700; m += 100) {
//            Parameters.setResultsFileName("PopulationSize" + resultsSuffix);
//            createResultsFileIfNotExtant(Parameters.getResultsFilename());
//            Parameters.setPopSize(m);
//            Parameters.setMutateRate(1 / (double) Parameters.getPopSize()); // mutation rate for mutation operator
//                runNeuralNet(numberOfRunsPerParameterConfiguration);
//            defaultParameters();
//            }
        
        //tournament size scaled to population since what we're varying is the selection pressure-
        //although that's a question, is there a point to varying both pop and tourney size since they'll work in opposite ways wrt selection pressure?
        //who knows! we'll see i guess
        for (double n = 0.05; n <= .8; n += .05) {
            Parameters.setResultsFileName("TournamentSize" + resultsSuffix);
            createResultsFileIfNotExtant(Parameters.getResultsFilename());
            var tournamentSize = (int) (Parameters.getPopSize() * n);
            Parameters.setTournamentSize(tournamentSize);
            runNeuralNet(numberOfRunsPerParameterConfiguration);
            defaultParameters();
        }

        //centred around the approx. ideal mutation rate of 1/population size
        for (double o = .8; o <= 1; o +=.01 ) {
            Parameters.setResultsFileName("MutationRate" + resultsSuffix);
            createResultsFileIfNotExtant(Parameters.getResultsFilename());
            var mutationToPopulationRatio = o * (1 / (double) Parameters.getPopSize());

            Parameters.setMutateRate(mutationToPopulationRatio);

            runNeuralNet(numberOfRunsPerParameterConfiguration);
            defaultParameters();
        }
        
//        for (double p = 0.01; p <= .1; p += 0.01) {
//            Parameters.setResultsFileName("MutationChange" + resultsSuffix);
//            createResultsFileIfNotExtant(Parameters.getResultsFilename());
//            Parameters.setMutateChange(p);
//            runNeuralNet(numberOfRunsPerParameterConfiguration);
//            defaultParameters();
//        }
    }
    
    static void runNeuralNet(int numberOfRunsPerParameterConfiguration) {
        for (int q = 0; q < numberOfRunsPerParameterConfiguration; q++) {
            //Create a new Neural Network Trainer Using the above parameters
            NeuralNetwork nn = new ExampleEvolutionaryAlgorithm();
            
            //train the neural net (Go and have a coffee)
            nn.run();
            
            /* Print out the best weights found
             * (these will have been saved to disk in the project default directory using
             * the saveWeights method in EvolutionaryTrainer)
             */
            System.out.println(nn.best);
        }
    }
    
    static void defaultParameters() {
        Parameters.setHidden(5);
        Parameters.setMinGene(-3);
        Parameters.setMaxGene(3);
        
        Parameters.setPopSize(300);
        
        Parameters.setTournamentSize(2);
        
        //1=1 point
        //2=2 point
        //3=uniform
        //4=arithmetic
        Parameters.setCrossoverType(1);
        
        Parameters.setChildrenPerReproduction(1);
        
        Parameters.setReplacementType(1);
                Parameters.setReplacementTournamentSize(2);
    
    
        Parameters.setMutateRate(1 / (double) Parameters.getPopSize()); // mutation rate for mutation operator
        Parameters.setMutateChange((double)(Parameters.getMaxGene() - Parameters.getMinGene()) / 150); // delta change for mutation operator, proportional
    }
    /**
     * The last File Saved to the Output Directory will contain the best weights /
     * Parameters and Fitness on the Training Set
     *
     * We can used the trained NN to Test on the test Set
     */
//        Parameters.setDataSet(DataSet.Test);
//        double fitness = Fitness.evaluate(nn);
//        System.out.println("Fitness on " + Parameters.getDataSet() + " " + fitness);
    
    /**
     * Or We can reload the NN from the file generated during training and test it on a data set
     * We can supply a filename or null to open a file dialog
     * Note that files must be in the project root and must be named *-n.txt
     * where "n" is the number of hidden nodes
     * ie  1518461386696-5.txt was saved at timestamp 1518461386696 and has 5 hidden nodes
     * Files are saved automatically at the end of training
     */

//        ExampleEvolutionaryAlgorithm nn2 = ExampleEvolutionaryAlgorithm.loadNeuralNetwork("1518446327913-5.txt");
//        Parameters.setDataSet(DataSet.Random);
//        double fitness2 = Fitness.evaluate(nn2);
//        System.out.println("Fitness on " + Parameters.getDataSet() + " " + fitness2);
    static void createResultsFileIfNotExtant(String filename) {
        if (filename == null || !(new File(filename)).exists()) {
            
            var parameterFields = "Run ID," + Parameters.printParamFieldsToCsv() + "Training Set,Fitness" + "\r\n";
            
            StringIO.writeStringToFile(filename, parameterFields, false);
        }
    }
}
