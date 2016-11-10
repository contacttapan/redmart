package com.challenge.redmart;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Find longest skiiing path
 *
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	private static Scanner scan;
	static int  rowSize = 0;
	static int colSize = 0;
	static int[][] input=null;
	static ArrayList<LinkedList<Node>> pathArray= new ArrayList<LinkedList<Node>>();
	static ArrayList<LinkedList<Node>> finalpathArray= new ArrayList<LinkedList<Node>>();
	public static void main(String[] args) {
		try {
			ClassLoader classLoader = App.class.getClassLoader();
			scan = new Scanner(new File(classLoader.getResource("map.txt").getFile()));
		
			
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
			ArrayList<ArrayList<Node>> rows=new ArrayList<ArrayList<Node>>();
	        for(int i=0;i<rowSize;i++){
	        	System.out.println("");
	        	ArrayList<Node> columns=new ArrayList<Node>();
	            for(int j=0;j<colSize;j++){
	                System.out.print(" "+input[i][j]);
	                Node point=new Node(i,j,input[i][j]);
	                point.initNodes(input,rowSize,colSize);
	                columns.add(point);
	            }
	            rows.add(columns); 
	        }
			scan.close();
			logger.info(">>>>>>>>>>>>>>>>");
			for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
				ArrayList<Node> arrayList = (ArrayList<Node>) iterator.next();
				for (Iterator iterator2 = arrayList.iterator(); iterator2.hasNext();) {
					Node node = (Node) iterator2.next();
					logger.info("Node {}", node);
					ArrayList<LinkedList<Node>> result = findMoveMents(node);
					pathArray.addAll(result);
				}
			}
			
			//for each number in the array find the following
			//- find North , south , east , west distance
			LinkedList<Node> list=new LinkedList<Node>();			
			// create tuple of link list with all possible path to and from it 
			// combine the adjacent list
			//find the lengthy and highest value
			 System.out.print(" ");
			for (Iterator<LinkedList<Node>> iterator = pathArray.iterator(); iterator.hasNext();) {
			//	logger.info(" PATH---> ");
				LinkedList<Node> path = (LinkedList<Node>) iterator.next();
				finalpathArray.add(combine(path));
				
			}
			 System.out.print("RESULT--->>>>>>>>>>>>>>>>>>>>>>>>");
			 LinkedList<Node> bestPath=finalpathArray.get(0);
			 int length =bestPath.size();
		     int difference=bestPath.getFirst().value-bestPath.getLast().value;
			for (Iterator<LinkedList<Node>> iterator = finalpathArray.iterator(); iterator.hasNext();) {
				logger.info(" PATH---> ");
				LinkedList<Node> path = (LinkedList<Node>) iterator.next();
				
				/*for (Iterator iterator2 = path.iterator(); iterator2.hasNext();) {
					Node node = (Node) iterator2.next();
					 System.out.print("-->"+node.value);
				}*/
				if(path.size()>=length && (path.getFirst().value-path.getLast().value) > difference)
				{
					bestPath=path;
				    length=bestPath.size();
				    difference=bestPath.getFirst().value-bestPath.getLast().value;
				}
				
				
			}
			//find the lengthy and highest value
			logger.info("Final Path with Difference {} and size {}",difference,length);
			System.out.println("PATH--->");
 			for (Iterator iterator2 = bestPath.iterator(); iterator2.hasNext();) {
				Node node = (Node) iterator2.next();
				 System.out.print("-->"+node.value);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static ArrayList<LinkedList<Node>> findMoveMents(Node node){
		ArrayList<LinkedList<Node>> pathArray= new ArrayList<LinkedList<Node>>();
		LinkedList<Node> listeast=new LinkedList<Node>();
		LinkedList<Node> listwest=new LinkedList<Node>();
		LinkedList<Node> listnorth=new LinkedList<Node>();
		LinkedList<Node> listsouth=new LinkedList<Node>();
		if(node.east!=null){listeast.add(node);listeast.add(node.east);pathArray.add(listeast);findMoveMents(node.east);}
		if(node.west!=null) {listwest.add(node);listwest.add(node.west);pathArray.add(listwest);findMoveMents(node.west);}
		if(node.north!=null) {listnorth.add(node);listnorth.add(node.north);pathArray.add(listnorth);findMoveMents(node.north);}
		if(node.south!=null) {listsouth.add(node);listsouth.add(node.south);pathArray.add(listsouth);findMoveMents(node.south);}
		return pathArray;
	}
	
	public static LinkedList<Node> combine(LinkedList<Node> nodeList){
		//System.out.println("PATH-->");
		/*for (Iterator iterator2 = nodeList.iterator(); iterator2.hasNext();) {
			Node node = (Node) iterator2.next();
			 System.out.print("-->"+node.value);
		}
		*/
		//System.out.println("FOR EACH BELOW PATH");
			for (Iterator iterator = pathArray.iterator(); iterator.hasNext();) {
			//	System.out.println("PATH-->");
			LinkedList<Node> linkedList = (LinkedList<Node>) iterator.next();
			/*for (Iterator iterator2 = linkedList.iterator(); iterator2.hasNext();) {
				Node node = (Node) iterator2.next();
				 System.out.print("-->"+node.value);
			}*/
			if(nodeList.getLast().equals(linkedList.getFirst())){
				nodeList.removeLast();
				nodeList.addAll(linkedList);
				
			}
		}
		return nodeList;
	}
	public static Node findBestPath(Node node){
		Node result=null;
		ArrayList<Integer> values=new ArrayList<Integer>();
		if(node.east!=null)values.add(node.east.value);
		if(node.west!=null)values.add(node.west.value);
		if(node.north!=null)values.add(node.north.value);
		if(node.south!=null)values.add(node.south.value);
		Collections.sort(values);
		Collections.reverse(values);
		if(node.east!=null && values.get(0)==node.east.value) result= node.east;
		if(node.west!=null && values.get(0)==node.west.value)  result= node.west;
		if(node.north!=null && values.get(0)==node.north.value)  result= node.north;
		if(node.south!=null && values.get(0)==node.south.value)  result= node.south;
		
		return result;
		
	}
	
	public static LinkedList<Node> traverse(Node node){
		ArrayList<LinkedList<Node>> pathArray= new ArrayList<LinkedList<Node>>();
		LinkedList<Node> listeast=new LinkedList<Node>();
		Node nextNode=findBestPath(node);
		listeast.add(node);
		if(nextNode!=null){
			listeast.addAll(traverse(nextNode));
		} 
		return listeast;
		
	}
	
}
