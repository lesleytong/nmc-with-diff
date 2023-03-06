package edu.ustb.sei.mde.compare.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;

import org.bytedeco.opencv.opencv_core.float16_t;

public class TestGensim {
	

	
    private static final String PYTHON_PATH = "/Users/lesley/opt/anaconda3/bin/python3"; // replace with your python interpreter path
    private static final String SCRIPT_PATH = "/Users/lesley/Documents/pythonWorkspace/FirstProject/load_once"; // replace with your python script path
    
    static Gson gson = new Gson();
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		simpleTest();
				
	
	}


	public static float[] generateVector(String str) {
		
		float[] vector = null;
		
		try {
			String pythonPath = "/Users/lesley/opt/anaconda3/bin/python3"; // Or the path to your Python interpreter
			String scriptPath = "/Users/lesley/Documents/pythonWorkspace/FirstProject/load_my_doc2vec.py"; // Replace with the actual path to your script file
			
			String[] command = {pythonPath, scriptPath, str};
		    Process process = Runtime.getRuntime().exec(command);// 执行py文件

		    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String line = in.readLine();
	    	JsonReader reader = new JsonReader(new StringReader(line));
	    	reader.setLenient(true);
	    	vector = gson.fromJson(reader, float[].class);
		   		    
	    	in.close();
		    process.waitFor();
		    
		} catch (IOException | InterruptedException e) {
		    e.printStackTrace();
		} 
		
		return vector;
		
	}

	private static void simpleTest() {
		int a = 18;
		int b = 23;
		
		try {
			String pythonPath = "/Users/lesley/opt/anaconda3/bin/python3"; // Or the path to your Python interpreter
			String scriptPath = "/Users/lesley/Documents/pythonWorkspace/FirstProject/mytest.py"; // Replace with the actual path to your script file
			String arg1 = String.valueOf(a); // Replace with the actual argument
			String arg2 = String.valueOf(b); // Replace with the actual argument
			
			String[] command = {pythonPath, scriptPath, arg1, arg2};
			
		    Process proc = Runtime.getRuntime().exec(command);// 执行py文件

		    BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    String line = null;
		    while ((line = in.readLine()) != null) {
		        System.out.println(line);
		    }
		    in.close();
		    proc.waitFor();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
	
}
