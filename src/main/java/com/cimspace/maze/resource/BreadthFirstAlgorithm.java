package com.cimspace.maze.resource;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * Name: Ikechukwu Chukwu
 * StudentID: 201123022
 * School: Nile University, Abuja
 * Breadth-First algorithm class implementation
 */
public class BreadthFirstAlgorithm {
	private static final Logger LOGGER = Logger.getLogger(BreadthFirstAlgorithm.class.getName());
	private int searchTime = 100;

	public void breadthFirstAlgorithm(Node start, Node end, int graphWidth, int graphHeight) {

		Queue<Node> queue = new LinkedList<Node>();
		Node[][] previous = new Node[graphWidth][graphHeight];

		LOGGER.info("Add the start node to the queue");
		queue.add(start);
		LOGGER.info("check if the queue is empty");
		while (!queue.isEmpty()) {
			LOGGER.info("perform operations below if the queue is not empty");
			Node currentNode = queue.poll();
			if (currentNode.isEnd()) {
				LOGGER.info("If the exit node is found, change node color");
				currentNode.setColor(Color.MAGENTA);
				break;
			}
			if (!currentNode.isSearched()) {
				LOGGER.info("Set new colors of the searched node aside that of the current node");
				currentNode.setColor(Color.ORANGE);
				try {
					LOGGER.info("run search time per node asynchronously");
					Thread.sleep(searchTime);
				} catch (Exception e) {
					LOGGER.severe("Error message is " + e.getLocalizedMessage());
					e.printStackTrace();
				}
				LOGGER.info("set all searched node to color blue");
				currentNode.setColor(Color.BLUE);
				for (Node adjacent : currentNode.getNeighbours()) {
					LOGGER.info("Add adjacent nodes to the queue");
					queue.add(adjacent);
					previous[adjacent.getX()][adjacent.getY()] = currentNode;
				}
			}
		}

		shortPath(previous, end);
	}

	/**
	 * Method to output the shortest path from the entry node to the exit node
	 * @param previous
	 * @param end
	 */
	private void shortPath(Node[][] previous, Node end) {
		LOGGER.info("Initializing the path to the end node which is the exit node");
		Node pathConstructor = end;
		while(pathConstructor != null) {
			LOGGER.info("While path is null, add the previous node to the array");
			pathConstructor = previous[pathConstructor.getX()][pathConstructor.getY()];
			if(pathConstructor != null) {
			pathConstructor.setColor(Color.ORANGE);
			}
			try {
				LOGGER.info("run search time per node asynchronously");
				Thread.sleep(searchTime);
			} catch (Exception e) {
				LOGGER.severe("Error message is " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

}
