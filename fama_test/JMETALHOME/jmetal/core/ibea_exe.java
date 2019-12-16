package jmetal.core;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.ibea.IBEA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.BinaryTournament;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.problems.two_obj;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.comparators.FitnessComparator;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ibea_exe {
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
    fileHandler_ = new FileHandler("ibea_exe.log"); 
    logger_.addHandler(fileHandler_) ;
    
    indicators = null ;

    if (args.length == 1) {
      problem = new two_obj("Int",Integer.parseInt(args[0]));
    } 
    else { // Default problem
      problem = new two_obj("Int", 20); 
    } // else

    algorithm = new IBEA(problem);

    // parameters
    int pop = 50;
    int archive = 50;
    int maxEval = 10000;
    double crossover_probability = 0.1;
    double mutation_probability = 0.5; 

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

    /* Selection Operator */
    parameters = new HashMap() ; 
    parameters.put("comparator", new FitnessComparator()) ;
    selection = new BinaryTournament(parameters);
    
    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);

    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

    // Print the results
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Variables values have been writen to file VAR_IBEA");
    population.printVariablesToFile("VAR_IBEA");    
    logger_.info("Objectives values have been writen to file FUN_IBEA");
    population.printObjectivesToFile("FUN_IBEA");
  
    if (indicators != null) {
      logger_.info("Quality indicators") ;
      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
      logger_.info("GD         : " + indicators.getGD(population)) ;
      logger_.info("IGD        : " + indicators.getIGD(population)) ;
      logger_.info("Spread     : " + indicators.getSpread(population)) ;
      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;  
    } // if
  } //main
} // ibea_exe.java
