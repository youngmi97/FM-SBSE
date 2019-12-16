package jmetal.operators.mutation;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ProductMutation extends Mutation {
  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(IntSolutionType.class) ;

  private Double mutationProbability_ = null ;
  
   /**
    * Constructor
    * Creates a new instance of the Bit Flip mutation operator
    */
   public ProductMutation(HashMap<String, Object> parameters) {
      super(parameters) ;
     if (parameters.get("probability") != null)
        mutationProbability_ = (Double) parameters.get("probability") ;        
   } // ProductMutation

   public void doMutation(double probability, Solution solution) throws JMException {
       int n = solution.numberOfVariables();
       Variable[] var = solution.getDecisionVariables();
       
       ArrayList<Integer> zeros = new ArrayList<Integer>();
       ArrayList<Integer> ones = new ArrayList<Integer>(); 
       
       for(int i=0;i<n;i++) {
       	if (var[i].getValue()==0) {
       		zeros.add(i);
       	}
       	else {
       		ones.add(1);
       	}
       }
       

       if (PseudoRandom.randDouble() < probability){
           double rand = PseudoRandom.randDouble();
           if (rand < 1/3){ // addition
           	solution.getDecisionVariables()[zeros.get(PseudoRandom.randInt(0,zeros.size()-1))].setValue(1);
           }
           else if (rand < 2/3){ // removal
           		
           	solution.getDecisionVariables()[ones.get(PseudoRandom.randInt(0,ones.size()-1))].setValue(0);
               
           }
           else { // swap
        	   
        	   if(ones.size()>0) {
		           	solution.getDecisionVariables()[zeros.get(PseudoRandom.randInt(0,zeros.size()-1))].setValue(1);
		           	solution.getDecisionVariables()[ones.get(PseudoRandom.randInt(0,ones.size()-1))].setValue(0);
		           }
        	   }
           
       }
 } // doMutation

   public Object execute(Object object) throws JMException {
      Solution solution = (Solution) object;

      if (!VALID_TYPES.contains(solution.getType().getClass())) {
         Configuration.logger_.severe("ProductMutation.execute: the solution " +
               "is not of the right type. The type should be 'Int', but " + solution.getType() + " is obtained");

         Class cls = java.lang.String.class;
         String name = cls.getName();
         throw new JMException("Exception in " + name + ".execute()");
      } // if 

      doMutation(mutationProbability_, solution);
      return solution;
   } // execute
} // ProductMutation