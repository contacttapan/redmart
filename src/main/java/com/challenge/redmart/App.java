package com.challenge.redmart;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Find longest skiing path
 *for each number in the array find the following
			// - find North , south , east , west distance
			// create tuple of link list with all possible path to and from it
			// combine the adjacent list
			// find the lengthy and highest value
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	private static Scanner scan;
	static int rowSize = 0;
	static int colSize = 0;
	static int[][] input = null;
	static ArrayList<LinkedList<Node>> pathArray = new ArrayList<LinkedList<Node>>();
	static ArrayList<LinkedList<Node>> finalpathArray = new ArrayList<LinkedList<Node>>();

	public static void main(String[] args) {
		try {
			ClassLoader classLoader = App.class.getClassLoader();
			scan = new Scanner(new File(classLoader.getResource("test1.txt").getFile()));

			String line = scan.nextLine();
			StringTokenizer st = new StringTokenizer(line);
			rowSize = Integer.parseInt(st.nextToken());
			if (st.hasMoreTokens())
				colSize = Integer.parseInt(st.nextToken());
			input = new int[rowSize][colSize];
			int row = 0;
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				logger.trace(" line " + line);
				st = new StringTokenizer(line);
				int col = 0;
				while (st.hasMoreTokens()) {
					logger.trace("row {} col {}", row, col);
					input[row][col] = Integer.parseInt(st.nextToken());
					logger.trace("input[row][col] {} {} {}", input[row][col], row, col);
					col++;
				}
				row++;
			}

			logger.info("Strating Node initialization");
			logger.info(" rowsize {}  colsize {} ", rowSize, colSize);
			logger.info("The input sorted matrix is : ");
			// for each number in the array find the following
			// - find North , south , east , west distance
			ArrayList<Node> nodes =new  ArrayList<Node>();
			for (int i = 0; i < rowSize; i++) {
				for (int j = 0; j < colSize; j++) {
					Node point = new Node(i, j, input[i][j]);
					point.initNodes(input, rowSize, colSize);
					nodes.add(point);
					if (j % 100 == 0)
						logger.info("Completed 100th set of J {}", j);
				}
				
			}
			scan.close();
			logger.info("End Node initialization");
			logger.info("Total Nodes {}",+nodes.size());
			logger.info("Strating Node node Visits");
			// create tuple of link list with all possible path to and from it
			int counter = 0;
		
				for (Iterator<Node> iterator2 = nodes.iterator(); iterator2.hasNext();) {
					Node node = (Node) iterator2.next();
					ArrayList<LinkedList<Node>> result = findMoveMents(node);
					counter++;
					pathArray.addAll(result);
					if (counter % 100 == 0)
						logger.info("Completed 100th set {}", counter);
				}
			
			logger.info("Ending Node node Visits");
			// combine the adjacent list
			// System.out.print(" ");
			ThreadPoolExecutor combinerpoolExecutor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>());
			Iterator<LinkedList<Node>> piterator = pathArray.iterator();
			int worksize=pathArray.size();
			int submitedjob=0;
			while (piterator.hasNext()) {
				
				if (combinerpoolExecutor.getActiveCount() < combinerpoolExecutor.getMaximumPoolSize())
					{
					LinkedList<Node> path = (LinkedList<Node>) piterator.next();
					combinerpoolExecutor.submit(new App().new Combiner(path));
					submitedjob++;
					}
				else
					logger.info("combinerpoolExecutor.getActiveCount() ={}", combinerpoolExecutor.getActiveCount());

			}
			while (submitedjob!=worksize) {
				Thread.sleep(3000);
				logger.info("combinerpoolExecutor total task completed  {}",combinerpoolExecutor.getCompletedTaskCount());
			}
			while(combinerpoolExecutor.getActiveCount()!=0){
				Thread.sleep(3000);
				logger.info("waiting for job to finish",combinerpoolExecutor.getActiveCount());
			}
			combinerpoolExecutor.shutdown();
			logger.info("RESULT--->>>>>>>>>>>>>>>>>>>>>>>>");
			LinkedList<Node> bestPath = finalpathArray.get(0);
			int length = bestPath.size();
			int difference = bestPath.getFirst().value - bestPath.getLast().value;
			for (Iterator<LinkedList<Node>> iterator = finalpathArray.iterator(); iterator.hasNext();) {
				logger.info("PATH--->");
				String temp="";
				for (Iterator<Node> iterator2 = bestPath.iterator(); iterator2.hasNext();) {
					Node node = (Node) iterator2.next();
					temp=temp+("-->" + node.value);
				}
				logger.info("{}",temp);
				LinkedList<Node> path = (LinkedList<Node>) iterator.next();
				if (path.size() >= length)
					 {
					bestPath = path;
					length = bestPath.size();
					difference = bestPath.getFirst().value - bestPath.getLast().value;
					logger.info("BEST PATH length {} & diff{}",length,difference);
				}else if(path.size() == length && (path.getFirst().value - path.getLast().value) > difference) {
					bestPath = path;
					length = bestPath.size();
					difference = bestPath.getFirst().value - bestPath.getLast().value;
				}

			}
			// find the lengthy and highest value
			logger.info("Final Path with Difference {} and size {} and Node List {}", difference, length,bestPath);
			logger.info("PATH--->");
			String temp="";
			for (Iterator<Node> iterator2 = bestPath.iterator(); iterator2.hasNext();) {
				Node node = (Node) iterator2.next();
				temp=temp+("-->" + node.value);
				
			}
			logger.info("{}" ,temp);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Recursive method to find all allowed move for a given node
	 * @param node
	 * @return
	 */
	public static ArrayList<LinkedList<Node>> findMoveMents(Node node) {
		ArrayList<LinkedList<Node>> pathArray = new ArrayList<LinkedList<Node>>();
		LinkedList<Node> listeast = new LinkedList<Node>();
		LinkedList<Node> listwest = new LinkedList<Node>();
		LinkedList<Node> listnorth = new LinkedList<Node>();
		LinkedList<Node> listsouth = new LinkedList<Node>();
		if (node.east != null) {
			listeast.add(node);
			listeast.add(node.east);
			logger.info("PATH--->");
			String temp="";
			for (Iterator<Node> iterator2 = listeast.iterator(); iterator2.hasNext();) {
				Node n1 = (Node) iterator2.next();
				temp=temp+("-->" + n1.value);
			}
			logger.info("{}",temp);
			pathArray.add(listeast);
			
		}
		if (node.west != null) {
			listwest.add(node);
			listwest.add(node.west);
			logger.info("PATH--->");
			String temp="";
			for (Iterator<Node> iterator2 = listwest.iterator(); iterator2.hasNext();) {
				Node n1 = (Node) iterator2.next();
				temp=temp+("-->" + n1.value);
			}
			logger.info("{}",temp);
			pathArray.add(listwest);
			
		}
		if (node.north != null) {
			listnorth.add(node);
			listnorth.add(node.north);
			String temp="";
			for (Iterator<Node> iterator2 = listnorth.iterator(); iterator2.hasNext();) {
				Node n1 = (Node) iterator2.next();
				temp=temp+("-->" + n1.value);
			}
			logger.info("{}",temp);
			pathArray.add(listnorth);
			
		}
		if (node.south != null) {
			listsouth.add(node);
			listsouth.add(node.south);
			String temp="";
			for (Iterator<Node> iterator2 = listsouth.iterator(); iterator2.hasNext();) {
				Node n1 = (Node) iterator2.next();
				temp=temp+("-->" + n1.value);
			}
			logger.info("{}",temp);
			pathArray.add(listsouth);
			
		}
		return pathArray;
	}
/**
 * A runnable class to faciliate recursive combiner for a list of nodes 
 * @author Tapan
 *
 */
	class Combiner implements Runnable {
		LinkedList<Node> nodeList;

		public Combiner(LinkedList<Node> nodeList) {
			this.nodeList = nodeList;
		}

		@Override
		public void run() {
			LinkedList<Node> result = this.combine();
			logger.info("PATH--->");
			String temp="";
			for (Iterator<Node> iterator2 = result.iterator(); iterator2.hasNext();) {
				Node node = (Node) iterator2.next();
				temp=temp+("-->" + node.value);
			}
			logger.info("{}" ,temp);
			synchronized (finalpathArray) {
				logger.info("Completed for Node length {} size of FinalArray{}", result.size(), finalpathArray.size());
				finalpathArray.add(result);
			}
		}
		/**
		 * Join two node list if traversal is allowed
		 * @return
		 */
		public LinkedList<Node> combine() {
			for (Iterator<LinkedList<Node>> iterator = pathArray.iterator(); iterator.hasNext();) {
				LinkedList<Node> linkedList = (LinkedList<Node>) iterator.next();
				if (nodeList.getLast().equals(linkedList.getFirst())) {
					nodeList.removeLast();
					nodeList.addAll(linkedList);

				}
			}
			return nodeList;
		}

	}

}
