package jmetal.core;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.problems.two_obj;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Class for configuring and running the SPEA2 algorithm
 */
public class spea2_exe {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  public static void main(String [] args) throws JMException, IOException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator
        
    QualityIndicator indicators ; // Object to get quality indicators

    HashMap  parameters ; // Operator parameters

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("spea2_exe.log"); 
    logger_.addHandler(fileHandler_) ;
    
    indicators = null ;

    if (args.length == 1) {
      problem = new two_obj("Int",Integer.parseInt(args[0]));
    }
    else { // Default problem
      problem = new two_obj("Int", 20); 
    }

    algorithm = new SPEA2(problem);
    
    // parameters
    int pop =50;
    int archive = 50;
    int maxEval = 10000;
    double crossover_probability = 0.9;
    double mutation_probability = 0.1; 
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize",pop);
    algorithm.setInputParameter("archiveSize",archive);
    algorithm.setInputParameter("maxEvaluations",maxEval);
    
    // Mutation and Crossover for Real codification 
    parameters = new HashMap() ;
    parameters.put("probability", crossover_probability) ;
    parameters.put("distributionIndex", 20.0) ;
    crossover = CrossoverFactory.getCrossoverOperator("ProductCrossover", parameters);                   

    parameters = new HashMap() ;
    parameters.put("probability", mutation_probability) ;
    parameters.put("distributionIndex", 20.0) ;
    mutation = MutationFactory.getMutationOperator("ProductMutation", parameters);                    
        
    // Selection operator 
    parameters = null ;
    selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters) ;                           
    
    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);
    
    // Execute the algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Objectives values have been writen to file FUN_SPEA2");
    population.printObjectivesToFile("FUN_SPEA2");
    logger_.info("Variables values have been writen to file VAR_SPEA2");
    population.printVariablesToFile("VAR_SPEA2");      
    
    if (indicators != null) {
      logger_.info("Quality indicators") ;
      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
      logger_.info("GD         : " + indicators.getGD(population)) ;
      logger_.info("IGD        : " + indicators.getIGD(population)) ;
      logger_.info("Spread     : " + indicators.getSpread(population)) ;
      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;
    } // if 
  }//main
} // spea2_exe.java
