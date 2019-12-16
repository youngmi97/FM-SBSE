package fama_test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.GenericFeatureModel;
import es.us.isa.FAMA.models.variabilityModel.GenericProduct;
import es.us.isa.FAMA.models.variabilityModel.VariabilityElement;
import es.us.isa.FAMA.Exceptions.FAMAException;
import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.ProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeatureModel;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;
import mutate.Mutation;

public class Initiate {
	
	
	
	public static void main(String[] args) throws IOException{
		String file_name = "Models/James.xml";
		String txt_name = "products.txt";
    	String output_dir = "Mutants";
    	String prod_bin = "11110000000000000000"
		    			+ "00000000000000000000"
		    			+ "00000000000000000000"
		    			+ "00000000000000000000";
    	initiate(file_name, output_dir);
    	System.out.println(calculateScore(txt_name, prod_bin, output_dir));
	}
	
	public static void initiate(String file_name, String output_dir) throws IOException{
		
		//create mutants for the chosen model

        Mutation.mutation(file_name, output_dir);
        
    	FileOutputStream file_write_100 = new FileOutputStream("MutNum.txt");
    	file_write_100.write(Arrays.toString(Mutation.getmutnum()).getBytes());
    	file_write_100.close();
        
        QuestionTrader qt_org = new QuestionTrader();
        VariabilityModel fm_org = qt_org.openFile(file_name);
        qt_org.setVariabilityModel(fm_org);
        
        ValidQuestion vq_org = (ValidQuestion) qt_org.createQuestion("Valid");
    	qt_org.ask(vq_org);
  		if (vq_org.isValid()) {
			NumberOfProductsQuestion npq = (NumberOfProductsQuestion) qt_org
					.createQuestion("#Products");
			qt_org.ask(npq);
				
				
				
  			ProductsQuestion npq2 = (ProductsQuestion) qt_org
  					.createQuestion("Products");
  			qt_org.ask(npq2);
  			
  			
  			
  			GenericFeature[] foos = 
  					((GenericFeatureModel) fm_org).getFeatures().toArray(new GenericFeature[((GenericFeatureModel) fm_org).getFeatures().size()]);
  			
  			HashMap<String, Integer> featureMap = new HashMap<String, Integer>();
  			for (int k = 0; k < foos.length; k++) {
  				featureMap.put(String.valueOf(foos[k]), k);
  			}
  			
  			
  			
  			GenericProduct[] bar =
					npq2.getAllProducts().toArray(new GenericProduct[npq2.getAllProducts().size()]);
  			String result = "";
  			for (int k=0; k < bar.length; k++) {
  				String tmp_str = StringUtils.repeat("0", foos.length);
  				StringBuilder tmp_builder = new StringBuilder(tmp_str);
				// empty array of length equal to number of total features        				
				Collection<VariabilityElement> ex = bar[k].getElements();
				Object[] ex_array = ex.toArray();
				for (int l=0; l < ex_array.length; l++) {
					tmp_builder.setCharAt(featureMap.get(ex_array[l].toString()), '1');
				}
				result = result + tmp_builder.toString();
				result = result + "\n";
			}
  			
	    	FileOutputStream file_write = new FileOutputStream("products.txt");
	    	file_write.write(Integer.toString((int)npq.getNumberOfProducts()).getBytes());
	    	file_write.write("\n".getBytes());
	    	file_write.write(Arrays.deepToString(foos).getBytes());
	    	file_write.write("\n".getBytes());
	    	file_write.write(Arrays.deepToString(bar).getBytes());
	    	file_write.write("\n".getBytes());
	    	file_write.write(result.getBytes());
  			//Print all products
	    	file_write.close();
  		} else {
  			System.out.println("Your feature model is not valid");
  		}
        for (int i = 0; i < Mutation.getmutnum().length; i++) {
        	String tmp_output = "";
    		if (i == 0){
    			tmp_output = output_dir + "/DFL";
    		}
    		if (i == 1){
    			tmp_output = output_dir + "/IFL";
    		}
    		if (i == 2){
    			tmp_output = output_dir + "/DFU";
    		}
    		if (i == 3){
    			tmp_output = output_dir + "/IFU";
    		}
    		if (i == 4){
    			tmp_output = output_dir + "/RSR";
    		}
    		if (i == 5){
    			tmp_output = output_dir + "/DRL";
    		}
    		if (i == 6){
    			tmp_output = output_dir + "/IRL";
    		}
    		if (i == 7){
    			tmp_output = output_dir + "/DRU";
    		}
    		if (i == 8){
    			tmp_output = output_dir + "/IRU";
    		}
    		if (i == 9){
    			tmp_output = output_dir + "/CHD";
    		}
    		if (i == 10){
    			tmp_output = output_dir + "/RDC";
    		}
    		if (i == 11){
    			tmp_output = output_dir + "/REC";
    		}
        	for (int j = 0; j < Mutation.getmutnum()[i]; j++) {
                QuestionTrader qt = new QuestionTrader();
                VariabilityModel fm = qt.openFile(tmp_output + "/output" + Integer.toString(j) + ".xml");
                qt.setVariabilityModel(fm);
                
                ValidQuestion vq = (ValidQuestion) qt.createQuestion("Valid");
            	qt.ask(vq);
          		if (vq.isValid()) {
      				NumberOfProductsQuestion npq = (NumberOfProductsQuestion) qt
          						.createQuestion("#Products");
      				qt.ask(npq);
      				
      				
      				
          			ProductsQuestion npq2 = (ProductsQuestion) qt
          					.createQuestion("Products");
          			qt.ask(npq2);
          			
          			
          			
          			GenericFeature[] foos = 
          					((GenericFeatureModel) fm).getFeatures().toArray(new GenericFeature[((GenericFeatureModel) fm).getFeatures().size()]);
          			
          			HashMap<String, Integer> featureMap = new HashMap<String, Integer>();
          			for (int k = 0; k < foos.length; k++) {
          				featureMap.put(String.valueOf(foos[k]), k);
          			}
          			
          			
          			
          			GenericProduct[] bar =
        					npq2.getAllProducts().toArray(new GenericProduct[npq2.getAllProducts().size()]);
          			String result = "";
          			for (int k=0; k < bar.length; k++) {
          				String tmp_str = StringUtils.repeat("0", foos.length);
          				StringBuilder tmp_builder = new StringBuilder(tmp_str);
        				// empty array of length equal to number of total features        				
        				Collection<VariabilityElement> ex = bar[k].getElements();
        				Object[] ex_array = ex.toArray();
        				for (int l=0; l < ex_array.length; l++) {
        					tmp_builder.setCharAt(featureMap.get(ex_array[l].toString()), '1');
        				}
        				result = result + tmp_builder.toString();
        				result = result + "\n";
        			}
          			
        	    	FileOutputStream file_write = new FileOutputStream(tmp_output + "/products"  + Integer.toString(j) + ".txt");
        	    	if(Integer.toString((int)npq.getNumberOfProducts()) == "80") {
        	    		System.out.println("hi");
        	    	}
        	    	file_write.write(Integer.toString((int)npq.getNumberOfProducts()).getBytes());
        	    	file_write.write("\n".getBytes());
        	    	file_write.write(Arrays.deepToString(foos).getBytes());
        	    	file_write.write("\n".getBytes());
        	    	file_write.write(Arrays.deepToString(bar).getBytes());
        	    	file_write.write("\n".getBytes());
        	    	file_write.write(result.getBytes());
          			//Print all products
        	    	file_write.close();
          		} else {
          			System.out.println("Your feature model is not valid");
          		}
                
        	}
        }
	}
	
	public static double calculateScore(String file_name, String product_bin, String mutants_dir) throws IOException {
		int num_file = 0;
    	BufferedReader file_read_3 = new BufferedReader(new FileReader("MutNum.txt"));
    	String line_3 = file_read_3.readLine();
    	file_read_3.close();
		String[] mut_num = line_3.substring(1, line_3.length() - 1).split(",");
		int[] mut_int_num = new int[mut_num.length];
		for(int i = 0; i < mut_num.length; i++) {
			mut_int_num[i] = Integer.parseInt(StringUtils.strip(mut_num[i]));
		}

    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
		String[] mut_name = Mutation.getMutName();
    	for (int i = 0; i < 3; i++) {
    		file_read.readLine();
    	}
    	String product_string = "";
    	int line_cnt = 0;
		while (true) {
    		String line = file_read.readLine();
    		if(line == null) break;
    		if(product_bin.charAt(line_cnt) == '1') { 
    			product_string = product_string + line; //tmp_product = product_gene EX) A, B, C
    			product_string += ",";
    		}
    		line_cnt ++;
		}
		file_read.close();
		String[] product_array = product_string.split(",");
		double score = 0;

		
    	for(int i = 0; i < mut_name.length; i++) { //mutant directory(ex: CHD, DFL)
    		String dir_name = mutants_dir + "/" + mut_name[i];
			for(int j = 0; j < mut_int_num[i]; j++) { //mutant txt
				num_file++;
				for (int k = 0; k < product_array.length; k++) {
					boolean contain = false;
					BufferedReader tmp_file_read = new BufferedReader(new FileReader(dir_name + "/products" + Integer.toString(j) + ".txt"));
					//txt read
					tmp_file_read.readLine();
					tmp_file_read.readLine();
					tmp_file_read.readLine();
					String tmp_product = product_array[k];
					while(true) { // mutants find gene
						String tmp_line = tmp_file_read.readLine();
						if (tmp_line == null) {
							break;
						}
						if(tmp_line.equals(tmp_product)) {
							contain = true;
							break;
						}
					}
					tmp_file_read.close();
					if (!contain) {
						score++;
						break;
					}
				}
			}
    	}		
    	return (num_file - score)/num_file;
	}

}