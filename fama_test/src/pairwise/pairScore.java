package pairwise;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

public class pairScore {
	public static void main(String arg[]) {
		
	}

	public static double pairScore(String products_dir, String product_bin, String pairs_dir) {
    	try {
			BufferedReader file_read = new BufferedReader(new FileReader(products_dir));
			HashSet<String> pairs_set = new HashSet<String>();
			file_read.readLine();
			String line = file_read.readLine();
			String[] feature_list =  line.substring(1, line.length()-1).split(",");
			int fullnum_tuple = (int)(feature_list.length * feature_list.length / 2);
			for (int i = 0; i < feature_list.length; i++) {
				//feature_list[i].strip();
				feature_list[i] = StringUtils.strip(feature_list[i]);
			}
			file_read.readLine();
			for (int i=0; i < product_bin.length(); i++) {
				line = file_read.readLine();
				if (product_bin.charAt(i) == '1') {
					BufferedReader tuple_read = new BufferedReader(new FileReader(pairs_dir));
					while(true) {
						String tuple_line = tuple_read.readLine();
						if (tuple_line == null) break;
						String[] tuple_list = tuple_line.split(",");
						//String elem1 = tuple_list[0].strip();
						String elem1 = StringUtils.strip(tuple_list[0]);
						//String elem2 = tuple_list[1].strip();
						String elem2 = StringUtils.strip(tuple_list[1]);
						int[] elem_index = new int[] {-1, -1};
						for (int j = 0; j < feature_list.length; j++) {
							if(feature_list[j].equals(elem1)) {
								elem_index[0] = j;
							}else if(feature_list[j].equals(elem2)) {
								elem_index[1] = j;
							}
						}
						if(line.charAt(elem_index[0]) == '1' && line.charAt(elem_index[1]) == '1') {
							pairs_set.add(tuple_line);
						}
					}
				}
			}
			//System.out.println(1-((double)pairs_set.size() / (double)fullnum_tuple));
			return (1 - ((double)pairs_set.size() / (double)fullnum_tuple));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return 0;

	}
}