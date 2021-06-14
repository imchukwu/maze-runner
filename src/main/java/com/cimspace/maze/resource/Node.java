package com.cimspace.maze.resource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Node class
 */
public class Node {
	private static final Logger LOGGER = Logger.getLogger(Node.class.getName());
	private int xPosition;
	private int yPosition;
	private Color nodeColor = Color.LIGHT_GRAY;
	private final int WIDTH = 35;
	private final int HEIGHT = 35;
	private Node left, right, up, down;

	private double fcost;

	/**
	 * <p>Constructs and Initializes Node object</p>
	 *
	 * @param x X axis of the coordinate
	 * @param y Y axis of the coordinate
	 */
	public Node(int x, int y) {
		xPosition = x;
		yPosition = y;
	}

	/**
	 * Constructs and Initializes Node object with empty parameter
	 */
	public Node() {
	}

	/**
	 * Draw graphics
	 * @param graphics Graphics2D object to draw images
	 */
	public void render(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.drawRect(xPosition, yPosition, WIDTH, HEIGHT);
		graphics.setColor(nodeColor);
		graphics.fillRect(xPosition + 1, yPosition + 1, WIDTH - 1, HEIGHT - 1);
	}

	/**
	 * Clicked method tells which path of the mouse is clicked to decide which type of node the user intend to assign.
	 * @param buttonCode Mouse click button position
	 */
	public void Clicked(int buttonCode) {
		LOGGER.info("Button clicked");
		if (buttonCode == 1) {
			LOGGER.info("Drawing a wall node");
			nodeColor = Color.BLACK;
		}
		if (buttonCode == 2) {
			LOGGER.info("Drawing the start node");
			nodeColor = Color.GREEN;
		}
		if (buttonCode == 3) {
			LOGGER.info("Drawing the exit or end node");
			nodeColor = Color.RED;
		}
		if (buttonCode == 4) {
			LOGGER.info("Drawing or clearing a path node");
			clearNode();
		}
	}

	public void setColor(Color c) {
		nodeColor = c;
	}

	/**
	 *
	 * @return list of neighbour nodes from a particular node
	 */
	public List<Node> getNeighbours() {
		List<Node> neighbours = new ArrayList<>();
		if (left != null && left.isPath())
			neighbours.add(left);
		if (down != null && down.isPath())
			neighbours.add(down);
		if (right != null && right.isPath())
			neighbours.add(right);
		if (up != null && up.isPath())
			neighbours.add(up);

		return neighbours;
	}

	public void setDirections(Node l, Node r, Node u, Node d) {
		left = l;
		right = r;
		up = u;
		down = d;
	}

	public void clearNode() {
		nodeColor = Color.LIGHT_GRAY;
	}

	public int getX() {
		return (xPosition - 15) / WIDTH;
	}

	public int getY() {
		return (yPosition - 15) / HEIGHT;
	}

	public Node setX(int x) {
		xPosition = x;
		return this;
	}

	public Node setY(int y) {
		yPosition = y;
		return this;
	}

	public boolean isWall() {
		return (nodeColor == Color.BLACK);
	}

	public boolean isStart() {
		return (nodeColor == Color.GREEN);
	}

	public boolean isEnd() {
		return (nodeColor == Color.RED);
	}

	public boolean isPath() {
		return (nodeColor == Color.LIGHT_GRAY || nodeColor == Color.RED);
	}

	public boolean isSearched() {
		return (nodeColor == Color.BLUE || nodeColor == Color.ORANGE);
	}

}
