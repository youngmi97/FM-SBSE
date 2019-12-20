package jmetal.problems;

import jmetal.core.Problem;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.Arrays;

import fama_test.Initiate;
import pairwise.pairScore;
// import pairwise coverage file

/** 
 * Class representing problem three_obj
 */

public class three_obj extends Problem {   
     public three_obj(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
       this(solutionType, numberOfVariables, 3); // 3 objectives
     } // three_obj 

  public three_obj(String solutionType, Integer numberOfVariables, Integer numberOfObjectives) {
    numberOfVariables_ = numberOfVariables;
    numberOfObjectives_ = numberOfObjectives;
    numberOfConstraints_ = 0;
    problemName_ = "three_obj";

    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables; var++) {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } // for

    if (solutionType.compareTo("Int") == 0)
      solutionType_ = new IntSolutionType(this);
    else if (solutionType.compareTo("Bin") == 0)
        solutionType_ = new BinarySolutionType(this);
    else {
      System.out.println("Error: solution type " + solutionType + " invalid");
      System.exit(-1);
    }
  }
  /**
   * Evaluates a solution
 * @throws IOException 
   */

  public void evaluate(Solution solution) throws JMException {
    Variable[] gen  = solution.getDecisionVariables();
    // gen is the list of products

	String txt_name = "products.txt";
	String output_dir = "Mutants";
	String pair_dir = "pairs.txt";
    
                
    double [] x = new double[numberOfVariables_];
    double [] f = new double[numberOfObjectives_];
        
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = gen[i].getValue(); // get variables(products)
    int n = solution.count();
    
    String a = new String();
    String b = new String();

    // mutation score
    for (int i=0; i< x.length; ++i)
        a = a + Integer.toString((int) x[i]);
   
	try {
		f[0] = Initiate.calculateScore(txt_name, a , output_dir);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    // size score
	System.out.println(n);
    f[1] = (double) n/ (double) numberOfVariables_;

    // pairwise coverage score
    for (int i=0; i< x.length; ++i)
        b = b + Integer.toString((int) x[i]);
    
    
    
    f[2] = pairScore.pairScore(txt_name, a, pair_dir);
	//f[2] = 0; Insert calculate coverage function // import pairwise coverage file

    //test objectives
    //f[0] = x[0]+ 2*x[1]+ 3*x[2]+ 4*x[3]+ 5*x[4]+ 4*x[5]+ 3*x[6]+ 2*x[7]+ x[8]+ x[9];
    //f[1] = 3*x[0]+ 4*x[1]- 6*x[2]+ 3*x[3]+ x[4]+ x[5]- x[6]+ 2*x[7]- 3*x[8]- x[9];


    // save objective
    for (int i = 0; i < numberOfObjectives_; i++)
      solution.setObjective(i,f[i]); // objective_[i] = f[i];
  } // evaluate

} // end

