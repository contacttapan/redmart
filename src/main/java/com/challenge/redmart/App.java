package com.challenge.redmart;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	private static Scanner scan;

	public static void main(String[] args) {
		try {
			ClassLoader classLoader = App.class.getClassLoader();

			scan = new Scanner(new File(classLoader.getResource("map.txt").getFile()));
			boolean matrix_size_set = false;
			int rowSize = 0;
			int colSize = 0;
			int[][] input=null;
				String line = scan.nextLine();
				StringTokenizer st = new StringTokenizer(line);
				rowSize = Integer.parseInt(st.nextToken());
				if (st.hasMoreTokens())
				  colSize = Integer.parseInt(st.nextToken());
				input = new int[rowSize][colSize];
				int row=0;
				while (scan.hasNextLine()) {
					 line = scan.nextLine();
					 logger.info(" line "+line);
					 st = new StringTokenizer(line);
					 int col=0;
					 while(st.hasMoreTokens()){
						 logger.info("row {} col {}",row,col);
						 input[row][col] = Integer.parseInt(st.nextToken());
						 logger.info("input[row][col] {} {} {}",input[row][col],row,col);
						 col++;
					 }
					 row++;
				}

			
			logger.info(" rowsize {}  colsize {} ", rowSize, colSize);
			logger.info("The input sorted matrix is : ");
	        for(int i=0;i<rowSize;i++){
	        	System.out.println("");
	            for(int j=0;j<colSize;j++)
	                System.out.print(" "+input[i][j]);
	        }
			
			scan.close();
			
			//for each number in the array find the following
			//- find North , south , east , west distance
			// combine the adjacent counts 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
