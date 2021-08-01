package coursework;

import java.lang.reflect.Field;
import java.util.Random;

import model.LunarParameters;
import model.NeuralNetwork;
import model.LunarParameters.DataSet;

import static java.lang.Math.max;

public class Parameters
{
    //default Parameter values
    private static int numHiddenNodes;
    private static int numGenes = calculateNumGenes();
    public static double minGene; // specifies minimum and maximum weight values
    public static double maxGene;
    
    private static int popSize;
    private static int maxEvaluations;
    
    private static int tournamentSize;
    
    //1 = 1 point
    //2 = 2 point
    //3 = uniform
    //4 = arithmetic
    private static int crossoverType;
    
    //1 = Replace worst
    //2 = Replace random
    //3 = Tournament replacement
    private static int replacementType;
    private static int replacementTournamentSize;
    
    
    private static int childrenPerReproduction;
    
    // Parameters for mutation
    // Rate = probability of changing a gene
    // Change = the maximum +/- adjustment to the gene value
    private static double mutateRate; // mutation rate for mutation operator
    private static double mutateChange ; // delta change for mutation operator, proportional to gene range by a factor of 1/150, ie 0.04 for default -3 - +3 range
    
    //Random number generator used throughout the application
    public static long seed = System.currentTimeMillis();
    public static Random random = new Random(seed);
//
    public static String getResultsFileName() {
        return resultsFileName;
    }
    
    public static void setResultsFileName(String resultsFileName) {
        Parameters.resultsFileName = resultsFileName;
    }
    
    private static String resultsFileName="RunResults.csv";
    
    //set the NeuralNetwork class here to use your code from the GUI
    public static Class neuralNetworkClass = ExampleEvolutionaryAlgorithm.class;
    
    /**
     * Do not change any methods that appear below here.
     */
    
    public static int getNumGenes() {
        return numGenes;
    }
    
    private static int calculateNumGenes() {
        int num = (NeuralNetwork.numInput * getNumHiddenNodes()) + (getNumHiddenNodes() * NeuralNetwork.numOutput) + getNumHiddenNodes() + NeuralNetwork.numOutput;
        return num;
    }
    
    public static int getNumHidden() {
        return getNumHiddenNodes();
    }
    
    public static void setHidden(int nHidden) {
        setNumHiddenNodes(nHidden);
        numGenes = calculateNumGenes();
    }
    
    public static String printParamsToCsv() {
        String str = "";
        
        for (Field field : Parameters.class.getDeclaredFields()) {
            String name = field.getName();
            
            str += name + ",";
            
        }
        str += "\r\n";
        
        for (Field field : Parameters.class.getDeclaredFields()) {
            Object val = null;
            try {
                val = field.get(null);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            str += val + ",";
            
        }
        
        str += "\r\n";
        
        return str;
    }
    
    public static String appendParamsToCsv() {
        String str = "";
        
        for (Field field : Parameters.class.getDeclaredFields()) {
            Object val = null;
            try {
                val = field.get(null);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            str += val + ",";
        }
        
        
        return str;
    }
    
    public static String printParams() {
        String str = "";
        for (Field field : Parameters.class.getDeclaredFields()) {
            String name = field.getName();
            Object val = null;
            try {
                val = field.get(null);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            str += name + " \t" + val + "\r\n";
            
        }
        return str;
    }
    
    public static String printParamFieldsToCsv() {
        String str = "";
        
        for (Field field : Parameters.class.getDeclaredFields()) {
    
            str += field.getName() + ",";
            
        }
        return str;
    }
    
    public static void setDataSet(DataSet dataSet) {
        LunarParameters.setDataSet(dataSet);
    }
    
    public static DataSet getDataSet() {
        return LunarParameters.getDataSet();
    }
    
    public static String getResultsFilename() {
        return resultsFileName;
    }
    
    public static void main(String[] args) {
        printParamsToCsv();
    }
    
    /**
     * These parameter values can be changed
     * You may add other Parameters as required to this class
     */
    public static int getNumHiddenNodes() {
        return numHiddenNodes;
    }
    
    public static void setNumHiddenNodes(int numHiddenNodes) {
        Parameters.numHiddenNodes = numHiddenNodes;
    }
    
    public static int getPopSize() {
        return popSize;
    }
    
    public static void setPopSize(int popSize) {
        Parameters.popSize = popSize;
    }
    
    public static int getMaxEvaluations() {
        return maxEvaluations;
    }
    
    public static void setMaxEvaluations(int maxEvaluations) {
        Parameters.maxEvaluations = maxEvaluations;
    }
    
    public static int getTournamentSize() {
        return tournamentSize;
    }
    
    public static void setTournamentSize(int tournamentSize) {
        Parameters.tournamentSize = tournamentSize;
    }
    
    public static int getCrossoverType() {
        return crossoverType;
    }
    
    public static void setCrossoverType(int crossoverType) {
        Parameters.crossoverType = crossoverType;
    }
    
    public static int getChildrenPerReproduction() {
        return childrenPerReproduction;
    }
    
    public static void setChildrenPerReproduction(int childrenPerReproduction) {
        Parameters.childrenPerReproduction = childrenPerReproduction;
    }
    
    public static double getMutateRate() {
        return mutateRate;
    }
    
    public static void setMutateRate(double mutateRate) {
        Parameters.mutateRate = mutateRate;
    }
    
    public static double getMutateChange() {
        return mutateChange;
    }
    
    public static void setMutateChange(double mutateChange) {
        Parameters.mutateChange = mutateChange;
    }
    
    public static double getMinGene() {
        return minGene;
    }
    
    public static void setMinGene(double minGene) {
        Parameters.minGene = minGene;
    }
    
    public static double getMaxGene() {
        return maxGene;
    }
    
    public static void setMaxGene(double maxGene) {
        Parameters.maxGene = maxGene;
    }
    
    public static int getReplacementType() {
        return replacementType;
    }
    
    public static void setReplacementType(int replacementType) {
        Parameters.replacementType = replacementType;
    }
    
    public static int getReplacementTournamentSize() {
        return replacementTournamentSize;
    }
    
    public static void setReplacementTournamentSize(int replacementTournamentSize) {
        Parameters.replacementTournamentSize = replacementTournamentSize;
    }
    
   public static void defaultParameters() {
        Parameters.setMaxEvaluations(20000);
        
        Parameters.setPopSize(10);
        
        Parameters.setChildrenPerReproduction(1);
        
        //1=1 point
        //2=2 point
        //3=uniform
        //4=arithmetic
        Parameters.setCrossoverType(1);
        
        Parameters.setMinGene(-0.8);
        Parameters.setMaxGene(0.8);
        
        Parameters.setHidden(6);
        Parameters.setMutateChange((Parameters.getMaxGene() - Parameters.getMinGene())); // delta change for mutation operator, proportional
        
        Parameters.setMutateRate((.93 / (double) Parameters.getPopSize())); // mutation rate for mutation operator
        
        var tournament = (int) ((double) Parameters.getPopSize() * 0.01);
        Parameters.setTournamentSize(max(tournament, 2));
        
        //1 = Replace worst
        //2 = Replace random
        //3 = Tournament replacement
        Parameters.setReplacementType(3);
        
        var replaceTournament = (int) ((double) Parameters.getPopSize() * 0.65);
        Parameters.setReplacementTournamentSize(max(replaceTournament, 2));
        
    }
}
