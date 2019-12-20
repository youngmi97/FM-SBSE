package featurePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class GenerateFeaturePairs
{
    public static void main(String[] args) throws IOException
    {
       String products_txt = "products.txt";
        compute(products_txt);
    }

    private static void compute(String products_txt) throws IOException
    {
      BufferedReader file_read = new BufferedReader(new FileReader(products_txt));
      file_read.readLine();
      String line = file_read.readLine();
      file_read.close();
      String[] feature_list =  line.substring(1, line.length()-1).split(",");
      for (int i = 0; i < feature_list.length; i++) {
         //feature_list[i].strip();
         StringUtils.strip(feature_list[i]);
      }
      
       ArrayList<ArrayList<String>> listOfPairs = new ArrayList<ArrayList<String>>();
       
       
       for (int i=0; i < feature_list.length - 1; i++ ) {
          for (int j=i+1; j < feature_list.length; j++) {
             ArrayList<String> values = new ArrayList<>();
             values.add(feature_list[i]);
             values.add(feature_list[j]);
             listOfPairs.add(values);
          }
       }
       
       FileWriter file_write = new FileWriter("pairs.txt");

       
           
       for(ArrayList<String> single_list: listOfPairs) {
          //System.out.println(single_list);
          String listString = single_list.stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
          file_write.write(listString + "\n");
          
          System.out.println(listString);
       }
       
       file_write.close();
    }
}