package edu.ustb.sei.mde.compare.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestW {
	
	public static void main(String[] args) {
		
	}

	public static float[][] loadW(int rows, int cols) {
		
		long start = System.currentTimeMillis();
		
		String base = "/Users/lesley/Documents/modelset/modelset/W/word2vec/W_EClassImpl";
		
		String suffix = "_" + cols + ".txt";
		String filename = base + suffix;
		
        float[][] W = new float[rows][cols];
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                for (int col = 0; col < values.length; col++) {
                    W[row][col] = Float.parseFloat(values[col]);
                }
                row++;
            }
            
            long end = System.currentTimeMillis();
            
            System.out.println("Load W: " + (end - start) + "ms. ");
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return W;
	}
}
