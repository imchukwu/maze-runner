package com.cimspace.maze;

import com.cimspace.maze.resource.BreadthFirstAlgorithm;
import com.cimspace.maze.resource.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.nio.file.LinkOption;

public class MazeApplication extends Canvas implements Runnable, MouseListener {

	private static Node startNode = null;
	private static Node targetNode = null;
	private static JFrame frame;

	private Node[][] nodeList;
	private static MazeApplication runTimeMazeApplication;
	private static BreadthFirstAlgorithm breadthFirstAlgorithm;

	private final static int WIDTH = 768;
	private final static int HEIGHT = 768;

	private final static int NODES_WIDTH = 19;
	private final static int NODES_HEIGHT = 19;

	public static void main(String[] args) {
		frame = new JFrame("Ikechukwu Chukwu - 201123022 Maze Runner");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setLayout(null);
		MazeApplication m = new MazeApplication();
		breadthFirstAlgorithm =  new BreadthFirstAlgorithm();

		m.setBounds(0, 25, WIDTH, HEIGHT);
		SetupMenu(frame);
		runTimeMazeApplication = m;

		frame.add(m);
		frame.setVisible(true);
		m.start();

	}

	public static void SetupMenu(JFrame frame) {
		JMenuBar bar = new JMenuBar();
		bar.setBounds(0, 0, WIDTH, 25);
		frame.add(bar);
		JMenu fileMenu = new JMenu("File");
		bar.add(fileMenu);
		JMenu boardMenu = new JMenu("Board");
		bar.add(boardMenu);
		JMenu algorithmsMenu = new JMenu("Algorithm");
		bar.add(algorithmsMenu);

		JMenuItem saveMaze = new JMenuItem("Save Maze");
		JMenuItem openMaze = new JMenuItem("Open Maze");
		JMenuItem exit = new JMenuItem("Exit Maze");

		JMenuItem newGrid = new JMenuItem("New Board");

		JMenuItem bfsItem = new JMenuItem("Breadth-First Search");

		openMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					runTimeMazeApplication.openMaze();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		});
		saveMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runTimeMazeApplication.clearSearchResults();
				try {
					runTimeMazeApplication.saveMaze();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		newGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runTimeMazeApplication.createNodes(true);
			}
		});
		bfsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (runTimeMazeApplication.isMazeValid()) {
					breadthFirstAlgorithm.breadthFirstAlgorithm(runTimeMazeApplication.startNode, runTimeMazeApplication.targetNode, runTimeMazeApplication.NODES_WIDTH,
							runTimeMazeApplication.NODES_HEIGHT);
				} else {
					System.out.println("DIDN'T LAUNCH");
				}

			}

		});

		fileMenu.add(exit);
		fileMenu.add(saveMaze);
		fileMenu.add(openMaze);
		boardMenu.add(newGrid);
		algorithmsMenu.add(bfsItem);

	}

	public void run() {
		init();
		while (true) {
			// check
			BufferStrategy bs = getBufferStrategy(); // check
			if (bs == null) {
				// check
				createBufferStrategy(2);
				continue;
			}
			// check
			Graphics2D grap = (Graphics2D) bs.getDrawGraphics(); // check
			render(grap);
			bs.show();
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void init() {
		requestFocus();
		addMouseListener(this);
		nodeList = new Node[NODES_WIDTH][NODES_HEIGHT];
		createNodes(false);
		setMazeDirections();
	}

	/**
	 * Sets maze direction
	 */
	public void setMazeDirections() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				Node up = null,down = null,left = null,right = null;
				int u = j - 1;
				int d = j + 1;
				int l = i - 1;
				int r = i + 1;
				
				if(u >= 0) up = nodeList[i][u];
				if(d < NODES_HEIGHT) down =  nodeList[i][d];
				if(l >= 0) left = nodeList[l][j];
				if(r < NODES_WIDTH) right =  nodeList[r][j];
				
				nodeList[i][j].setDirections(left, right, up, down);
			}	
		}
	}

	/**
	 * Creates a node on the board
	 * @param ref
	 */
	public void createNodes(boolean ref) {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if(!ref) nodeList[i][j] = new Node(i, j).setX(15 + i * 35).setY(15 + j * 35);
				nodeList[i][j].clearNode();
			}
		}
	}

	/**
	 * This method is used to save a maze current in the board as a .maze file
	 * @throws IOException
	 */
	public void saveMaze() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(frame);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String ext = file.getAbsolutePath().endsWith(".maze") ? "" : ".maze";
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath() + ext));
			for (int i = 0; i < nodeList.length; i++) {
				for (int j = 0; j < nodeList[i].length; j++) {
					if (nodeList[i][j].isWall()) {
						outputWriter.write("1");
					} else if (nodeList[i][j].isStart()) {
						outputWriter.write("2");
					} else if (nodeList[i][j].isEnd()) {
						outputWriter.write("3");
					} else {
						outputWriter.write("0");
					}
				}
				outputWriter.newLine();
			}
			outputWriter.flush();
			outputWriter.close();
		}

	}

	/**
	 * Opens a saved maze and use it
	 * @throws IOException
	 */
	public void openMaze() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(frame);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = null;
			for (int i = 0; i < NODES_WIDTH; i++) {
				line = reader.readLine();
				for (int j = 0; j < NODES_HEIGHT; j++) {

					int nodeType = Character.getNumericValue(line.charAt(j));
					System.out.println("node is " + nodeType);
					switch (nodeType) {
					case 0:
						nodeList[i][j].setColor(Color.LIGHT_GRAY);
						break;
					case 1:
						nodeList[i][j].setColor(Color.BLACK);
						break;
					case 2:
						nodeList[i][j].setColor(Color.GREEN);
						startNode = nodeList[i][j];
						break;
					case 3:
						nodeList[i][j].setColor(Color.RED);
						targetNode = nodeList[i][j];
						break;
					}
				}

			}
			reader.close();

		}
	}

	/**
	 * Clears search result
	 */
	public void clearSearchResults() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if (nodeList[i][j].isSearched()) {
					nodeList[i][j].clearNode();
				}
			}
		}
		if (isMazeValid()) {
			targetNode.setColor(Color.RED);
			startNode.setColor(Color.GREEN);
		}
	}

	/**
	 * Renders graphics
	 * @param g
	 */
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				nodeList[i][j].render(g);
			}
		}
	}

	public void start() {
		new Thread(this).start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		Node clickedNode = getNodeAt(e.getX(), e.getY());
		if (clickedNode == null)
			return;

		if (clickedNode.isWall()) {
			clickedNode.clearNode();
			return;
		}

		clickedNode.Clicked(e.getButton());

		if (clickedNode.isEnd()) {
			if (targetNode != null) {
				targetNode.clearNode();
			}
			targetNode = clickedNode;
		} else if (clickedNode.isStart()) {

			if (startNode != null) {
				startNode.clearNode();
			}
			startNode = clickedNode;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public boolean isMazeValid() {
		return targetNode == null ? false : true && startNode == null ? false : true;
	}

	private Node getStart() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if (nodeList[i][j].isStart()) {
					return nodeList[i][j];
				}
			}
		}
		return null;
	}

	public Node getNodeAt(int x, int y) {
		x -= 15;
		x /= 35;
		y -= 15;
		y /= 35;

		System.out.println(x + ":" + y);
		if (x >= 0 && y >= 0 && x < nodeList.length && y < nodeList[x].length) {
			return nodeList[x][y];
		}
		return null;
	}
}
