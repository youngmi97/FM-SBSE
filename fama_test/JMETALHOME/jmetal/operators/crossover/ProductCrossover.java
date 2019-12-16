package jmetal.operators.crossover;

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

/**
 * This class allows to apply a ProductCrossover operator using two parent
 */
public class ProductCrossover extends Crossover {
	
	private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class,
              BinaryRealSolutionType.class,
              IntSolutionType.class) ;
	
	 private Double crossoverProbability_ = null;

    public ProductCrossover(HashMap<String, Object> parameters) {
        super(parameters) ;
        if (parameters.get("probability") != null)
            crossoverProbability_ = (Double) parameters.get("probability") ;  		
    } // ProductCrossover

    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {
        Solution[] offSpring = new Solution[2];
        offSpring[0] = new Solution(parent1);
        offSpring[1] = new Solution(parent2);
        if (PseudoRandom.randDouble() < probability) {

            int n1 = parent1.count();
            int n2 = parent2.count();
            int n = parent1.numberOfVariables();

            Integer[] ind1 = parent1.order();
            Integer[] ind2 = parent2.order();
            
            List<Integer> off1 = new ArrayList<>();
            List<Integer> off2 = new ArrayList<>();
            
            for(int i=0; i<n1/2+(n2+1)/2; i++) {
            	if(i<n1/2) off1.add(ind1[i]);
            	else off1.add(ind2[i-n1/2]);
            }
            for(int i=0; i<n2/2+(n1+1)/2; i++) {
            	if(i<n2/2) off1.add(ind2[i]);
            	else off1.add(ind1[i-n2/2]);
            }
            
            for (int i=0; i<n; i++) {
            	if (off1.contains(i)) offSpring[0].getDecisionVariables()[i].setValue(1);
            	else offSpring[0].getDecisionVariables()[i].setValue(0);
            }
            for (int i=0; i<n; i++) {
            	if (off2.contains(i)) offSpring[1].getDecisionVariables()[i].setValue(1);
            	else offSpring[1].getDecisionVariables()[i].setValue(0);
            }
            
        }
        return offSpring;
    } // doCrossover

    public Object execute(Object object) throws JMException {
        Solution[] parents = (Solution[]) object;
    
        /*if (!(VALID_TYPES.contains(parents[0].getType().getClass())  &&
            VALID_TYPES.contains(parents[1].getType().getClass())) ) {
    
          Configuration.logger_.severe("ProductCrossover.execute: the solutions " +
                  "are not of the right type. The type should be 'Binary' or 'Int', but " +
                  parents[0].getType() + " and " +
                  parents[1].getType() + " are obtained");
    
          Class cls = java.lang.String.class;
          String name = cls.getName();
          throw new JMException("Exception in " + name + ".execute()");
        } // if
    
        if (parents.length < 2) {
          Configuration.logger_.severe("ProductCrossover.execute: operator " +
                  "needs two parents");
          Class cls = java.lang.String.class;
          String name = cls.getName();
          throw new JMException("Exception in " + name + ".execute()");
        } */
        
        Solution[] offSpring;
        offSpring = doCrossover(crossoverProbability_,
                parents[0],
                parents[1]);
        return offSpring;
      } // execute
}