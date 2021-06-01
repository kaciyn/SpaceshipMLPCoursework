//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package coursework;

import coursework.LunarLanderModified;
import model.Individual;
import model.LunarParameters;
import model.LunarParameters.DataSet;
import model.NeuralNetworkModified;

public class FitnessModified
{
    public FitnessModified() {
    }
    
    public static double evaluate(Individual individual, model.NeuralNetworkModified neuralNetwork) {
        ++neuralNetwork.evaluations;
        neuralNetwork.setWeights(individual.chromosome);
        individual.fitness = 0.0D;
        
        for(int i = 0; i < 8; ++i) {
            LunarLanderModified lunarLander = new LunarLanderModified(neuralNetwork);
            if (LunarParameters.getDataSet() == DataSet.Training) {
                lunarLander.setTrainingStartParameters(i);
            } else if (LunarParameters.getDataSet() == DataSet.Test) {
                lunarLander.setTestStartParameters(i);
            }
            
            lunarLander.run();
            individual.fitness += lunarLander.fitness;
        }
        
        individual.fitness /= 8.0D;
        return individual.fitness;
    }
    
    public static double evaluate(NeuralNetworkModified neuralNetwork) {
        return evaluate(neuralNetwork.best, neuralNetwork);
    }
}
