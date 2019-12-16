package mutate;

import java.io.FileOutputStream;
import java.lang.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.Arrays;

public class Mutation {
	
	public static String[] getMutName() {
		String[] arr = {"DFL", "IFL", "DFU", "IFU", "RSR", "DRL", "IRL", "DRU", "IRU", "CHD", "RDC", "REC"};
		return arr;
	}
	public static int[] getmutnum() {
		int[] arr = {DFLcnt, IFLcnt, DFUcnt, IFUcnt, RSRcnt, DRLcnt, IRLcnt, DRUcnt, IRUcnt, CHDcnt, RDCcnt, RECcnt};
		return arr;
	}
	private static int DFLcnt = 0;
	private static int IFLcnt = 0;
	private static int DFUcnt = 0;
	private static int IFUcnt = 0;
	private static int RSRcnt = 0;
	private static int DRLcnt = 0;
	private static int IRLcnt = 0;
	private static int DRUcnt = 0;
	private static int IRUcnt = 0;
	private static int CHDcnt = 0;
	private static int RDCcnt = 0;
	private static int RECcnt = 0;
	
    public static void main(String[] args) throws IOException {
    	String file_name = "c:/Users/junst/OneDrive/HIS.xml";
    	String output_dir = "c:/Users/junst/OneDrive";
        mutation(file_name, output_dir);
    }
    
    public static void change_file(String file_name) throws IOException{
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	String tmp_line = file_read.readLine();
    	String next_line = file_read.readLine();
    	if(next_line == null) {
    		file_read.close();
	    	FileOutputStream file_write = new FileOutputStream(file_name);
	    	String[] tmp_list = tmp_line.split(">");
	    	tmp_line = String.join(">\n", tmp_list);
	    	tmp_line = tmp_line + ">";
	    	file_write.write(tmp_line.getBytes());
	    	file_write.close();
    	}else {
	    	while(true) {
	    		String line = file_read.readLine();
	    		if(line == null) {
	    			break;
	    		}
	    	}
	    	file_read.close();
    	}
    	
    }
    public static void mutation(String file_name, String output_dir) throws IOException{
    	change_file(file_name);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	String[] mutator = new String[] {"/DFL", "/IFL", "/DFU", "/IFU", "/RSR", "/DRL", "/IRL", "/DRU", "/IRU", "/CHD", "/RDC", "/REC"};
    	for (int i = 0; i < mutator.length; i++) {
    		String path = output_dir + mutator[i];
        	File Folder = new File(path);
        	if(Folder.exists()) {
        		Folder.delete();
        	}
        	Folder.mkdir();
    	}
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if (line == null) {
    			break;
    		}
    		if (line.contains("<binaryRelation")){
    			line = file_read.readLine();
    			line_count ++;
    			if(Integer.parseInt(line.split("\"")[3]) > 0) {
    				DFL(file_name, line_count, output_dir + "/DFL/output" + Integer.toString(DFLcnt) + ".xml");
    				DFLcnt ++;
    			}
    			if(Integer.parseInt(line.split("\"")[3]) < Integer.parseInt(line.split("\"")[1])){
    				IFL(file_name, line_count, output_dir + "/IFL/output" + Integer.toString(IFLcnt) + ".xml");
    				IFLcnt ++;
    				DFU(file_name, line_count, output_dir + "/DFU/output" + Integer.toString(DFUcnt) + ".xml");
    				DFUcnt++;
    			}
				IFU(file_name, line_count, output_dir + "/IFU/output" + Integer.toString(IFUcnt) + ".xml");
				IFUcnt++;
    		}else if(line.contains("<setRelation")) {
    			line = file_read.readLine();
    			String tmp_line = line;
    			line_count++;
    			int tmp_count = line_count;
    			int group_count = line_count;
    			int tmp_cnt = 1;
    			do{
					line = file_read.readLine();
					line_count++;
					group_count++;
					if(line.contains("<setRelation")) {
						tmp_cnt++;
					}
					if(line.contains("</setRelation")) {
						tmp_cnt--;
					}
				} while(tmp_cnt > 0);
    			group_count++;
    			RSR(file_name, tmp_count - 1, group_count, output_dir + "/RSR/output" + Integer.toString(RSRcnt) + ".xml");
    			RSRcnt++;
    			if(Integer.parseInt(tmp_line.split("\"")[3]) > 0) {
    				DRL(file_name, tmp_count, output_dir + "/DRL/output" + Integer.toString(DRLcnt) + ".xml");
    				DRLcnt++;
    			}
    			if(Integer.parseInt(tmp_line.split("\"")[3]) < Integer.parseInt(tmp_line.split("\"")[1])){
    				IRL(file_name, tmp_count, output_dir + "/IRL/output" + Integer.toString(IRLcnt) + ".xml");
    				IRLcnt ++;
    				DRU(file_name, tmp_count, output_dir + "/DRU/output" + Integer.toString(DRUcnt) + ".xml");
    				DRUcnt++;
    			}
    			if(Integer.parseInt(tmp_line.split("\"")[1]) < group_count - tmp_count - 3) {
    				IRU(file_name, tmp_count, output_dir + "/IRU/output" + Integer.toString(IRUcnt) + ".xml");
    				IRUcnt++;


    			}
    		}else if(line.contains("<requires")) {
    			CHD(file_name, line_count, output_dir + "/CHD/output" + Integer.toString(CHDcnt)+ ".xml");
    			CHDcnt++;
    			RDC(file_name, line_count, output_dir + "/RDC/output" + Integer.toString(RDCcnt) + ".xml");
    			RDCcnt++;
    		}else if(line.contains("<excludes")) {
    			REC(file_name, line_count, output_dir + "/REC/output" + Integer.toString(RECcnt) + ".xml");
    			RECcnt++;
    		}
    		line_count ++;
    	}
    }
        
    public static void DFL(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null) 
    			break;
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			int new_min = Integer.parseInt(tmp_list[3]) - 1;
    			String new_line = "";
    			for (int i=0; i < tmp_list.length; i++) {
    				if(i != 3) {
    					new_line = new_line + tmp_list[i];
    				}else {
    					new_line = new_line + Integer.toString(new_min);
    				}
    				new_line = new_line + "\"";
    			}
    			file_write.write(new_line.getBytes());
    		
    		}else {
    			file_write.write(line.getBytes());
    		}
    		file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    public static void IFL(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null) 
    			break;
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			int new_min = Integer.parseInt(tmp_list[3]) + 1;
    			String new_line = "";
    			for (int i=0; i < tmp_list.length; i++) {
    				if(i != 3) {
    					new_line = new_line + tmp_list[i];
    				}else {
    					new_line = new_line + Integer.toString(new_min);
    				}
    				new_line = new_line + "\"";
    			}
    			file_write.write(new_line.getBytes());
    		
    		}else {
    			file_write.write(line.getBytes());
    		}
    		file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void DFU(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null) 
    			break;
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			int new_max = Integer.parseInt(tmp_list[1]) - 1;
    			String new_line = "";
    			for (int i=0; i < tmp_list.length; i++) {
    				if(i != 1) {
    					new_line = new_line + tmp_list[i];
    				}else {
    					new_line = new_line + Integer.toString(new_max);
    				}
    				new_line = new_line + "\"";
    			}
    			file_write.write(new_line.getBytes());
    		
    		}else {
    			file_write.write(line.getBytes());
    		}
    		file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    public static void IFU(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null) 
    			break;
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			int new_max = Integer.parseInt(tmp_list[1]) + 1;
    			String new_line = "";
    			for (int i=0; i < tmp_list.length; i++) {
    				if(i != 1) {
    					new_line = new_line + tmp_list[i];
    				}else {
    					new_line = new_line + Integer.toString(new_max);
    				}
    				new_line = new_line + "\"";
    			}
    			file_write.write(new_line.getBytes());
    		
    		}else {
    			file_write.write(line.getBytes());
    		}
    		file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void DRL(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null) 
    			break;
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			int new_min = Integer.parseInt(tmp_list[3]) - 1;
    			String new_line = "";
    			for (int i=0; i < tmp_list.length; i++) {
    				if(i != 3) {
    					new_line = new_line + tmp_list[i];
    				}else {
    					new_line = new_line + Integer.toString(new_min);
    				}
    				new_line = new_line + "\"";
    			}
    			file_write.write(new_line.getBytes());
    		
    		}else {
    			file_write.write(line.getBytes());
    		}
    		file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void IRL(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null) 
    			break;
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			int new_min = Integer.parseInt(tmp_list[3]) + 1;
    			String new_line = "";
    			for (int i=0; i < tmp_list.length; i++) {
    				if(i != 3) {
    					new_line = new_line + tmp_list[i];
    				}else {
    					new_line = new_line + Integer.toString(new_min);
    				}
    				new_line = new_line + "\"";
    			}
    			file_write.write(new_line.getBytes());
    		
    		}else {
    			file_write.write(line.getBytes());
    		}
    		file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void DRU(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null) 
    			break;
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			int new_max = Integer.parseInt(tmp_list[1]) - 1;
    			String new_line = "";
    			for (int i=0; i < tmp_list.length; i++) {
    				if(i != 1) {
    					new_line = new_line + tmp_list[i];
    				}else {
    					new_line = new_line + Integer.toString(new_max);
    				}
    				new_line = new_line + "\"";
    			}
    			file_write.write(new_line.getBytes());
    		
    		}else {
    			file_write.write(line.getBytes());
    		}
    		file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void IRU(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null) 
    			break;
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			int new_max = Integer.parseInt(tmp_list[1]) + 1;
    			String new_line = "";
    			for (int i=0; i < tmp_list.length; i++) {
    				if(i != 1) {
    					new_line = new_line + tmp_list[i];
    				}else {
    					new_line = new_line + Integer.toString(new_max);
    				}
    				new_line = new_line + "\"";
    			}
    			file_write.write(new_line.getBytes());
    		
    		}else {
    			file_write.write(line.getBytes());
    		}
    		file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void RDC(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null)
    			break;
    		if(line_count != line_index) {
    			file_write.write(line.getBytes());
    			file_write.write("\n".getBytes());
    		}
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void REC(String file_name, int line_index, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null)
    			break;
    		if(line_count != line_index) {
    			file_write.write(line.getBytes());
    			file_write.write("\n".getBytes());
    		}
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void RSR(String file_name, int line_start, int line_end, String output) throws IOException{
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null){
    			break;
    		}
    		if(line_count == line_start || line_count == line_end-1) {
    			line_count++;
    			continue;
    		}else if(line_count > line_start && line_count < line_end) {
    			if(line.contains(("<groupedFeature"))){
	    			String name = line.split("\"")[1];
	    			file_write.write("<binaryRelation name=\"\">\n".getBytes());
	    			file_write.write("<cardinality max=\"1\" min=\"0\"/>\n".getBytes());
	    			if(line.contains(("/>"))) {
		    			String tmp_str = "<solitaryFeature name=\"" + name + "\"/>\n";
		    			file_write.write(tmp_str.getBytes());
	    			}
	    			else {
		    			String tmp_str = "<solitaryFeature name=\"" + name + "\">\n";
		    			file_write.write(tmp_str.getBytes());
    					int tmp_cnt = 0;
    					do{
    						line = file_read.readLine();
    						line_count++;
    						file_write.write(line.getBytes());
    		    			file_write.write("\n".getBytes());
	    					if(line.contains("<setRelation")) {
	    						tmp_cnt++;
	    					}
	    					if(line.contains("</setRelation")) {
	    						tmp_cnt--;
	    					}
    					} while(tmp_cnt > 0);
    					line = file_read.readLine();
    					line_count++;
    					file_write.write("</solitaryFeature>\n".getBytes());
    				}
	    			file_write.write("</binaryRelation>\n".getBytes());
    			}
    		}else {
    			file_write.write(line.getBytes());
    			file_write.write("\n".getBytes());
    		}
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
    
    public static void CHD(String file_name, int line_index, String output) throws IOException {
    	FileOutputStream file_write = new FileOutputStream(output);
    	BufferedReader file_read = new BufferedReader(new FileReader(file_name));
    	int line_count = 0;
    	while(true) {
    		String line = file_read.readLine();
    		if(line == null){
    			break;
    		}
    		if(line_count == line_index) {
    			String[] tmp_list = line.split("\"");
    			String tmp_word = tmp_list[1];
    			tmp_list[1] = tmp_list[5];
    			tmp_list[5] = tmp_word;
    			String tmp_line = String.join("\"", tmp_list);
    			file_write.write(tmp_line.getBytes());
    		}else {
    			file_write.write(line.getBytes());
    		}
			file_write.write("\n".getBytes());
    		line_count++;
    	}
    	file_read.close();
    	file_write.close();
    }
}