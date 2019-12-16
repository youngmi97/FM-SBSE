package jmetal.problems;

import jmetal.core.Problem;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import fama_test.Initiate;

/** 
 * Class representing problem two_obj
 */

public class two_obj extends Problem {   
     public two_obj(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
       this(solutionType, numberOfVariables, 2); // 2 objectives
     } // two_obj  

  public two_obj(String solutionType, Integer numberOfVariables, Integer numberOfObjectives) {
    numberOfVariables_ = numberOfVariables;
    numberOfObjectives_ = numberOfObjectives;
    numberOfConstraints_ = 0;
    problemName_ = "two_obj";

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
    // padding with 0

	String txt_name = "products.txt";
	String output_dir = "Mutants";
    
                
    double [] x = new double[numberOfVariables_];
    double [] f = new double[numberOfObjectives_];
        
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = gen[i].getValue(); // get variables(products)
    int n = solution.count();
    
    String a = new String();
    
    
    //mutation score
    for (int i=0; i< x.length; ++i)
        a = a + Integer.toString((int) x[i]);
   
	try {
		f[0] = Initiate.calculateScore(txt_name, a , output_dir);
		//System.out.println(f[0]);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	BufferedReader file_read;
	   int line_cnt = 0;
	   try {
	      BufferedWriter tmp_bw = new BufferedWriter(new FileWriter("IndSize.txt", true));
	      tmp_bw.close();
	   } catch (IOException e1) {
	      // TODO Auto-generated catch block
	      e1.printStackTrace();
	   }
	   try {
	      file_read = new BufferedReader(new FileReader("IndSize.txt"));
	      while(true) {
	         String line = file_read.readLine();
	         if(line == null) break;
	         line_cnt++;
	      }
	      file_read.close();
	      BufferedWriter bw = new BufferedWriter(new FileWriter("IndSize.txt", true));
	      PrintWriter pw = new PrintWriter(bw, true);
	      pw.write(Integer.toString(line_cnt) + " " + Integer.toString(n) + "\n");
	      bw.close();
	   } catch (FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
    f[1] = (double) n/ (double) numberOfVariables_; // size score
    
    //f[0] = x[0]+ 2*x[1]+ 3*x[2]+ 4*x[3]+ 5*x[4]+ 4*x[5]+ 3*x[6]+ 2*x[7]+ x[8]+ x[9];
    //f[1] = 3*x[0]+ 4*x[1]- 6*x[2]+ 3*x[3]+ x[4]+ x[5]- x[6]+ 2*x[7]- 3*x[8]- x[9];

    for (int i = 0; i < numberOfObjectives_; i++)
      solution.setObjective(i,f[i]); // objective_[i] = f[i];
  } // evaluate

} // end

