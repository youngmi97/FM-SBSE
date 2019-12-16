package EA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import fama_test.Initiate;
public class EA {
	public static void main(String[] args) throws IOException{
		String file_name = "Models/James.xml";
		String txt_name = "products.txt";
    	String output_dir = "Mutants";
    	String prod_bin = "11110000000000000000"
		    			+ "00000000000000000000"
		    			+ "00000000000000000000"
		    			+ "00000000000000000000";
    	BufferedReader file_read = new BufferedReader(new FileReader(txt_name));
    	int num_org_prod = Integer.parseInt(file_read.readLine());
		//Initiate.initiate(file_name, output_dir);
    	
    	
    	
		System.out.println(num_org_prod);
		System.out.println(Initiate.calculateScore(txt_name, prod_bin, output_dir));
	}
}
