package jmetal.core;

import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemFactory;
import jmetal.problems.ZDT.ZDT3;
import jmetal.problems.two_obj;
import jmetal.problems.Water;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


import fama_test.Initiate;

public class nsgaII_exe {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  public static void main(String [] args) throws 
                                  JMException, 
                                  SecurityException, 
                                  IOException, 
                                  ClassNotFoundException {
    Problem   problem   ; // The problem to solve
    Algorithm algorithm ; // The algorithm to use
    Operator  crossover ; // Crossover operator
    Operator  mutation  ; // Mutation operator
    Operator  selection ; // Selection operator
    
    HashMap  parameters ; // Operator parameters
    
    QualityIndicator indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("nsgaII_exe.log"); 
    logger_.addHandler(fileHandler_) ;
    
    
    //Initialize Mutants
    String file_name = "Models/James.xml";
	String txt_name = "products.txt";
	String output_dir = "Mutants";
	File file = new File("IndSize.txt");
	if (file.exists()) {
		file.delete();
	}
	BufferedReader file_read = new BufferedReader(new FileReader(txt_name));
	int num_org_prod = Integer.parseInt(file_read.readLine());
	
	Initiate.initiate(file_name, output_dir);
    
    
    
    
        
   
    //indicators = null;
    
    if(args.length==1) {
    	problem = new two_obj("Int", Integer.parseInt(args[0]));
    }
    else problem = new two_obj("Int", num_org_prod); //second argument project number
    

    //indicators = new QualityIndicator(problem, "VAR_total") ; // file containing pareto front
    indicators = null;
    
    algorithm = new NSGAII(problem);
    
    // parameters
    int pop = 250;
    int maxEval = 1500;
    double crossover_probability = 0.5;
    double mutation_probability = 0.5; 
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize",pop);
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

    // Selection Operator 
    parameters = null ;
    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;                           

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);

    // Add the indicator object to the algorithm
    algorithm.setInputParameter("indicators", indicators) ;
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Variables values have been writen to file VAR_NSGAII");
    population.printVariablesToFile("VAR_NSGAII");    
    logger_.info("Objectives values have been writen to file FUN_NSGAII");
    population.printObjectivesToFile("FUN_NSGAII");
  
    if (indicators != null) {
      logger_.info("Quality indicators") ;
      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
      logger_.info("GD         : " + indicators.getGD(population)) ;
      logger_.info("IGD        : " + indicators.getIGD(population)) ;
      logger_.info("Spread     : " + indicators.getSpread(population)) ;
      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;  
     
      int evaluations = ((Integer)algorithm.getOutputParameter("evaluations")).intValue();
      logger_.info("Speed      : " + evaluations + " evaluations") ;      
    } // if
  } //main
} // nsgaII_exe
