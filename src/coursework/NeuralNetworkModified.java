//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package model;

import coursework.ExampleEvolutionaryAlgorithm;
import coursework.Parameters;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import javax.swing.JFileChooser;

//just copied over because I wanted to make it output to csv

public abstract class NeuralNetworkModified extends Observable implements Runnable {
    public Individual best = null;
    public ArrayList<Individual> population;
    public int evaluations = 0;
    public static final int numInput = 5;
    private int numHidden = Parameters.getNumHidden();
    public static final int numOutput = 3;
    private double[] inputs = new double[5];
    private double[][] ihWeights;
    private double[] hBiases;
    private double[] hOutputs;
    private double[][] hoWeights;
    private double[] oBiases;
    private double[] outputs;
    
    public NeuralNetworkModified() {
        this.ihWeights = makeMatrix(5, this.numHidden);
        this.hBiases = new double[this.numHidden];
        this.hOutputs = new double[this.numHidden];
        this.hoWeights = makeMatrix(this.numHidden, 3);
        this.oBiases = new double[3];
        this.outputs = new double[3];
    }
    
    private static double[][] makeMatrix(int rows, int cols) {
        double[][] result = new double[rows][];
        
        for(int r = 0; r < result.length; ++r) {
            result[r] = new double[cols];
        }
        
        return result;
    }
    
    public void setWeights(double[] weights) {
        int numWeights = 5 * this.numHidden + this.numHidden * 3 + this.numHidden + 3;
        if (weights.length != numWeights) {
            System.out.println("Bad weights array length: ");
            System.exit(0);
        }
        
        int k = 0;
        
        int i;
        int j;
        for(i = 0; i < 5; ++i) {
            for(j = 0; j < this.numHidden; ++j) {
                this.ihWeights[i][j] = weights[k++];
            }
        }
        
        for(i = 0; i < this.numHidden; ++i) {
            this.hBiases[i] = weights[k++];
        }
        
        for(i = 0; i < this.numHidden; ++i) {
            for(j = 0; j < 3; ++j) {
                this.hoWeights[i][j] = weights[k++];
            }
        }
        
        for(i = 0; i < 3; ++i) {
            this.oBiases[i] = weights[k++];
        }
        
    }
    
    public double[] getWeights() {
        int numWeights = 5 * this.numHidden + this.numHidden * 3 + this.numHidden + 3;
        double[] result = new double[numWeights];
        int k = 0;
        
        int i;
        int j;
        for(i = 0; i < this.ihWeights.length; ++i) {
            for(j = 0; j < this.ihWeights[0].length; ++j) {
                result[k++] = this.ihWeights[i][j];
            }
        }
        
        for(i = 0; i < this.hBiases.length; ++i) {
            result[k++] = this.hBiases[i];
        }
        
        for(i = 0; i < this.hoWeights.length; ++i) {
            for(j = 0; j < this.hoWeights[0].length; ++j) {
                result[k++] = this.hoWeights[i][j];
            }
        }
        
        for(i = 0; i < this.oBiases.length; ++i) {
            result[k++] = this.oBiases[i];
        }
        
        return result;
    }
    
    public double[] computeOutputs() {
        double[] hSums = new double[this.numHidden];
        double[] oSums = new double[3];
        
        int i;
        int i;
        for(i = 0; i < this.numHidden; ++i) {
            for(i = 0; i < 5; ++i) {
                hSums[i] += this.inputs[i] * this.ihWeights[i][i];
            }
        }
        
        for(i = 0; i < this.numHidden; ++i) {
            hSums[i] += this.hBiases[i];
        }
        
        for(i = 0; i < this.numHidden; ++i) {
            this.hOutputs[i] = this.activationFunction(hSums[i]);
        }
        
        for(i = 0; i < 3; ++i) {
            for(i = 0; i < this.numHidden; ++i) {
                oSums[i] += this.hOutputs[i] * this.hoWeights[i][i];
            }
        }
        
        for(i = 0; i < 3; ++i) {
            oSums[i] += this.oBiases[i];
        }
        
        double[] finalOutputs = new double[3];
        
        for(i = 0; i < 3; ++i) {
            finalOutputs[i] = this.activationFunction(oSums[i]);
        }
        
        this.outputs = Arrays.copyOf(finalOutputs, finalOutputs.length);
        double[] retResult = new double[3];
        retResult = Arrays.copyOf(this.outputs, this.outputs.length);
        return retResult;
    }
    
    public abstract double activationFunction(double var1);
    
    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }
    
    public static ExampleEvolutionaryAlgorithm loadNeuralNetwork(String filename) {
        if (filename == null || !(new File(filename)).exists()) {
            JFileChooser fileChooser = new JFileChooser(".");
            if (fileChooser.showOpenDialog((Component)null) != 0) {
                return null;
            }
            
            filename = fileChooser.getSelectedFile().getName();
        }
        
        String[] lines = StringIO.readStringsFromFile(filename);
        String[] data = lines[0].split(",");
        double[] weights = new double[data.length];
        
        int hidden;
        for(hidden = 0; hidden < data.length; ++hidden) {
            weights[hidden] = Double.parseDouble(data[hidden]);
        }
        
        hidden = Integer.parseInt(filename.split("-")[1].replaceAll(".txt", ""));
        Parameters.setHidden(hidden);
        ExampleEvolutionaryAlgorithm ea = new ExampleEvolutionaryAlgorithm();
        ea.setWeights(weights);
        ea.best = new Individual();
        ea.best.chromosome = weights;
        return ea;
    }
    
    protected void saveNeuralNetwork() {
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
        }
    }
    
    protected void outputStats() {
        this.setChanged();
        this.notifyObservers(this.best.copy());
        System.out.println(this.evaluations + "\t" + this.best);
    }
}
