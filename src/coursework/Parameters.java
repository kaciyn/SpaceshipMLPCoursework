package coursework;

import java.lang.reflect.Field;
import java.util.Random;

import model.LunarParameters;
import model.NeuralNetwork;
import model.LunarParameters.DataSet;

public class Parameters
{
    
    private static int numHiddenNodes = 5;
    private static int numGenes = calculateNumGenes();
    public static double minGene = -3; // specifies minimum and maximum weight values
    public static double maxGene = +3;
    
    private static int popSize = 200;
    private static int maxEvaluations = 1000;
    
    private static int tournamentSize = 2;
    
    //1=1 point
    //2=2 point
    //3=uniform
    //4=arithmetic
    private static int crossoverType = 4;
    
    private static int childrenPerReproduction = 1;
    
    // Parameters for mutation
    // Rate = probability of changing a gene
    // Change = the maximum +/- adjustment to the gene value
    private static double mutateRate = 0.04; // mutation rate for mutation operator
    private static double mutateChange = 0.1; // delta change for mutation operator
    
    //Random number generator used throughout the application
    public static long seed = System.currentTimeMillis();
    public static Random random = new Random(seed);
 
    private static String resultsFileName="RunResults.csv";
    
    //set the NeuralNetwork class here to use your code from the GUI
//    public static Class neuralNetworkClass = ExampleHillClimber.class;
    
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
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
}
