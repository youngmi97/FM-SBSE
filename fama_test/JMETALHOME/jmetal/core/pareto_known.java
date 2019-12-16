package jmetal.core;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.ibea.IBEA;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.BinaryTournament;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemFactory;
import jmetal.problems.two_obj;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.FitnessComparator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import fama_test.Initiate;

public class pareto_known {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  public static void main(String [] args) throws 
                                  JMException, 
                                  SecurityException, 
                                  IOException, 
                                  ClassNotFoundException {
    Problem   problem   ; // The problem to solve
    
    Algorithm algorithm_nsgaII ; // The algorithm to use
    Algorithm algorithm_spea2 ; // The algorithm to use
    Algorithm algorithm_ibea ; // The algorithm to use
    
    Operator  crossover_nsgaII ; // Crossover operator
    Operator  crossover_spea2 ; // Crossover operator
    Operator  crossover_ibea ; // Crossover operator
    
    Operator  mutation_nsgaII  ; // Mutation operator
    Operator  mutation_spea2  ; // Mutation operator
    Operator  mutation_ibea  ; // Mutation operator
    Operator  selection ; // Selection operator
    
    HashMap  parameters ; // Operator parameters
    
    QualityIndicator indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("nsgaII_exe.log"); 
    logger_.addHandler(fileHandler_) ;
    
    //Initialize Mutants
    //String file_name = "Models/model_20120130_333619036.xml";
    String file_name = "Models/JAMES.xml";
	String txt_name = "products.txt";
	String output_dir = "Mutants";
	
	File file = new File("IndSize.txt");
	   if (file.exists()) {
	      file.delete();
	   }
	Initiate.initiate(file_name, output_dir);
	
	BufferedReader file_read = new BufferedReader(new FileReader(txt_name));
	int num_org_prod = Integer.parseInt(file_read.readLine());
	
    // Define problem
    problem = new two_obj("Int", num_org_prod); //second argument project number
    
    //System.out.println(num_org_prod);
   
    // indicators = new QualityIndicator(problem, "FUN_total") ;
    indicators = null;
    
    // parameter
    int pop = 250;
    int archive = 50;
    int maxEval = 1500;
    double cp_nsgaII = 0.8;
    double cp_spea2 = 0.8;
    double cp_ibea = 0.5;
    double mp_nsgaII = 0.8;
    double mp_spea2 = 0.8;
    double mp_ibea = 0.5;
    int num = 1;
    
    algorithm_nsgaII = new NSGAII(problem);
    algorithm_spea2 = new SPEA2(problem);
    algorithm_ibea = new IBEA(problem);

    // NSGA_II parameters setting
    algorithm_nsgaII.setInputParameter("populationSize",pop);
    algorithm_nsgaII.setInputParameter("maxEvaluations",maxEval);
    // SPEA2 parameters setting
    algorithm_spea2.setInputParameter("populationSize",pop);
    algorithm_spea2.setInputParameter("archiveSize",archive);
    algorithm_spea2.setInputParameter("maxEvaluations",maxEval);
    // IBEA parameters setting
    algorithm_ibea.setInputParameter("populationSize",pop);
    algorithm_ibea.setInputParameter("archiveSize",archive);
    algorithm_ibea.setInputParameter("maxEvaluations",maxEval);

    // Mutation and Crossover for Real codification 
    parameters = new HashMap() ;
    parameters.put("probability", cp_nsgaII) ;
    parameters.put("distributionIndex", 20.0) ;
    crossover_nsgaII = CrossoverFactory.getCrossoverOperator("ProductCrossover", parameters);  
    
    parameters = new HashMap() ;
    parameters.put("probability", cp_spea2) ;
    parameters.put("distributionIndex", 20.0) ;
    crossover_spea2 = CrossoverFactory.getCrossoverOperator("ProductCrossover", parameters);
    
    parameters = new HashMap() ;
    parameters.put("probability", cp_ibea) ;
    parameters.put("distributionIndex", 20.0) ;
    crossover_ibea = CrossoverFactory.getCrossoverOperator("ProductCrossover", parameters);

    parameters = new HashMap() ;
    parameters.put("probability", mp_nsgaII) ;
    parameters.put("distributionIndex", 20.0) ;
    mutation_nsgaII = MutationFactory.getMutationOperator("ProductMutation", parameters);  
    
    parameters = new HashMap() ;
    parameters.put("probability", mp_spea2) ;
    parameters.put("distributionIndex", 20.0) ;
    mutation_spea2 = MutationFactory.getMutationOperator("ProductMutation", parameters);
    
    parameters = new HashMap() ;
    parameters.put("probability", mp_ibea) ;
    parameters.put("distributionIndex", 20.0) ;
    mutation_ibea = MutationFactory.getMutationOperator("ProductMutation", parameters);                   

    // Selection Operator 
    parameters = null ;
    selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters) ;                           

    // Add the operators to the algorithm NSGII
    algorithm_nsgaII.addOperator("crossover",crossover_nsgaII);
    algorithm_nsgaII.addOperator("mutation",mutation_nsgaII);
    algorithm_nsgaII.addOperator("selection",selection);
    // Add the operators to the algorithm SPEA2
    algorithm_spea2.addOperator("crossover",crossover_spea2);
    algorithm_spea2.addOperator("mutation",mutation_spea2);
    algorithm_spea2.addOperator("selection",selection);
    // Add the operators to the algorithm IBEA
    algorithm_ibea.addOperator("crossover",crossover_ibea);
    algorithm_ibea.addOperator("mutation",mutation_ibea);
    algorithm_ibea.addOperator("selection",selection);

    // Add the indicator object to the algorithm
    algorithm_nsgaII.setInputParameter("indicators", indicators) ;
    algorithm_spea2.setInputParameter("indicators", indicators) ;
    algorithm_ibea.setInputParameter("indicators", indicators) ;
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet[] population = new SolutionSet[3*num];
    for (int i=0; i<3*num; i++) {
    	if (i<num) {population[i] = algorithm_nsgaII.execute(); logger_.info(Integer.toString(i));}
    	else if (i<2*num) {population[i] = algorithm_spea2.execute(); logger_.info(Integer.toString(i));}
    	else {population[i] = algorithm_ibea.execute(); logger_.info(Integer.toString(i));}
    }
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    SolutionSet population_nsgaII = population[0] ;
    SolutionSet population_spea2 = population[num];
    SolutionSet population_ibea = population[2*num];
    SolutionSet population_total = population[0];
    
    for (int i=0; i<num; i++) {
    	population_nsgaII = population_nsgaII.union(population[i]);
    }
    for (int i=num; i<2*num; i++) {
    	population_spea2 = population_spea2.union(population[i]);
    }
    for (int i=2*num; i<3*num; i++) {
    	population_ibea = population_ibea.union(population[i]);
    }
    for (int i=0; i<3*num; i++) {
    	population_total = population_total.union(population[i]);
    }    
    
    Ranking ranking_nsgaII = new Ranking(population_nsgaII);
    Ranking ranking_spea2 = new Ranking(population_spea2);
    Ranking ranking_ibea = new Ranking(population_ibea);
    Ranking ranking_total = new Ranking(population_total);
    
    SolutionSet pareto_nsgaII = ranking_nsgaII.getSubfront(0);
    SolutionSet pareto_spea2 = ranking_spea2.getSubfront(0);
    SolutionSet pareto_ibea = ranking_ibea.getSubfront(0);
    SolutionSet pareto_total = ranking_total.getSubfront(0);
    
    // Result messages 
    logger_.info("Total execution time: "+ estimatedTime + "ms");
    logger_.info("Variables values have been writen to file VAR_*");
    pareto_nsgaII.printVariablesToFile("VAR_NSGAII");
    pareto_spea2.printVariablesToFile("VAR_SPEA2");
    pareto_ibea.printVariablesToFile("VAR_IBEA");
    pareto_total.printVariablesToFile("VAR_total");  
    
    logger_.info("Objectives values have been writen to file FUN_*");
    pareto_nsgaII.printObjectivesToFile("FUN_NSGAII");
    pareto_spea2.printObjectivesToFile("FUN_SPEA2");
    pareto_ibea.printObjectivesToFile("FUN_IBEA");
    pareto_total.printObjectivesToFile("FUN_total");
    
    /*
    BufferedReader in = new BufferedReader(new FileReader("FUN_total"));
    String line;
    while((line = in.readLine()) != null) {
    	System.out.println(line);
    }
    in.close();
    */
    
    indicators = new QualityIndicator(problem, "FUN_total") ; // file containing pareto front
    
    if (indicators != null) {
    	logger_.info("Quality indicators: NSGAII \n" + ""+ "Hypervolume: " + indicators.getHypervolume(pareto_nsgaII)+ 
        		  "\n" + "GD         : " + indicators.getGD(pareto_nsgaII) + "\n" + "IGD        : " + indicators.getIGD(pareto_nsgaII)
        		  + "\n" + "Spread     : " + indicators.getSpread(pareto_nsgaII) + "\n" + "Epsilon    : " + indicators.getEpsilon(pareto_nsgaII) ) ;
    	
    	logger_.info("Quality indicators: SPEA2 \n" + ""+ "Hypervolume: " + indicators.getHypervolume(pareto_spea2)+ 
      		  "\n" + "GD         : " + indicators.getGD(pareto_spea2) + "\n" + "IGD        : " + indicators.getIGD(pareto_spea2)
      		  + "\n" + "Spread     : " + indicators.getSpread(pareto_spea2) + "\n" + "Epsilon    : " + indicators.getEpsilon(pareto_spea2) ) ;
    	
    	logger_.info("Quality indicators: IBEA \n" + ""+ "Hypervolume: " + indicators.getHypervolume(pareto_ibea)+ 
      		  "\n" + "GD         : " + indicators.getGD(pareto_ibea) + "\n" + "IGD        : " + indicators.getIGD(pareto_ibea)
      		  + "\n" + "Spread     : " + indicators.getSpread(pareto_ibea) + "\n" + "Epsilon    : " + indicators.getEpsilon(pareto_ibea) ) ;
     
      //int evaluations = ((Integer)algorithm_nsgaII.getOutputParameter("evaluations")).intValue();
      //logger_.info("Speed      : " + evaluations + " evaluations") ;      
    } // if
    
  } //main
} // pareto_known