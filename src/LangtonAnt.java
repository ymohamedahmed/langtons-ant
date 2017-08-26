import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LangtonAnt {
    private int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();;

    public class Ant {
	int row;
	int column;
	Direction dir;
    }

    public enum Direction {
	NORTH, EAST, SOUTH, WEST
    }

    public class Cell {
	Rectangle rect;
	Color color;
    }

    public static void main(String[] args) {
	new LangtonAnt().setupFrame();
    }

    public void setupFrame() {
	// Create a fullscreen frame
	JFrame frame = new JFrame();
	frame.setSize(width, height);
	frame.setUndecorated(true);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// Ask user for size of square
	System.out.println("Input size of square");
	Scanner scan = new Scanner(System.in);
	int cellSize = scan.nextInt();
	scan.close();
	// Setup pane
	Pane pane = new Pane(cellSize);
	pane.genCells();
	frame.add(pane);
	// Display the frame
	frame.setVisible(true);
    }

    public class Pane extends JPanel {
	// Default square size (pixels)
	private int squareSize = 10;
	ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();
	Ant ant;

	public Pane(int squareSize) {
	    this.squareSize = squareSize;
	}

	// Return an arraylist with the cells in the entire frame
	public ArrayList<ArrayList<Cell>> genCells() {
	    for (int xPos = 0; xPos <= width - squareSize; xPos += squareSize) {
		ArrayList<Cell> temp = new ArrayList<Cell>();
		for (int yPos = 0; yPos <= height - squareSize; yPos += squareSize) {
		    Rectangle rectangle = new Rectangle(xPos, yPos, squareSize, squareSize);
		    Cell cell = new Cell();
		    cell.rect = rectangle;
		    cell.color = Color.WHITE;
		    temp.add(cell);
		}
		cells.add(temp);
	    }
	    ant = new Ant();
	    // Start the ant in the middle of the grid
	    ant.row = cells.size() / 2;
	    ant.column = cells.get(0).size() / 2;
	    // Starts facing north
	    ant.dir = Direction.NORTH;
	    return cells;

	}

	@Override
	public void paint(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g.create();

	    // Paint each rectangle with a black outline
	    for (ArrayList<Cell> cell : cells) {
		for (Cell c : cell) {
		    g2d.setColor(c.color);
		    g2d.fill(c.rect);
		    g2d.setColor(Color.BLACK);
		    g2d.draw(c.rect);
		}
	    }
	    // Fill the rectangle red with the ant in
	    g2d.setColor(Color.RED);
	    g2d.fill(cells.get(ant.row).get(ant.column).rect);
	    g2d.setColor(Color.BLACK);
	    g2d.draw(cells.get(ant.row).get(ant.column).rect);
	    moveAnt(ant, cells);
	    repaint();
	}

	public void moveAnt(Ant ant, ArrayList<ArrayList<Cell>> cells) {
	    // Rotate right at a white square and invert colour
	    if (cells.get(ant.row).get(ant.column).color == Color.WHITE) {
		cells.get(ant.row).get(ant.column).color = Color.BLACK;
		rotateAnt(ant, true);
		moveForward(ant, cells);
	    }
	    // Rotate left at a black square and invert colour
	    else if (cells.get(ant.row).get(ant.column).color == Color.BLACK) {
		cells.get(ant.row).get(ant.column).color = Color.WHITE;
		rotateAnt(ant, false);
		moveForward(ant, cells);

	    }

	}

	public void rotateAnt(Ant ant, boolean right) {
	    // Rotate ant appropriately
	    if (right) {
		switch (ant.dir) {
		case NORTH:
		    ant.dir = Direction.EAST;
		    break;
		case EAST:
		    ant.dir = Direction.SOUTH;
		    break;
		case SOUTH:
		    ant.dir = Direction.WEST;
		    break;
		case WEST:
		    ant.dir = Direction.NORTH;
		    break;
		}
	    } else if (!right) {
		switch (ant.dir) {
		case NORTH:
		    ant.dir = Direction.WEST;
		    break;
		case EAST:
		    ant.dir = Direction.NORTH;
		    break;
		case SOUTH:
		    ant.dir = Direction.EAST;
		    break;
		case WEST:
		    ant.dir = Direction.SOUTH;
		    break;
		}
	    }
	}
	// Test if the ant has moved off the grid and if so wrap it back round
	public void checkOffGrid(Ant ant, ArrayList<ArrayList<Cell>> cells) {
	    if (ant.row > cells.size() - 1) {
		ant.row = 0;
	    } else if (ant.row < 0) {
		ant.row = cells.size() - 1;
	    } else if (ant.column > cells.get(0).size() - 1) {
		ant.column = 0;
	    } else if (ant.column < 0) {
		ant.column = cells.get(0).size() - 1;
	    }
	}
	// Move ant based on direction and then check if off grid
	public void moveForward(Ant ant, ArrayList<ArrayList<Cell>> cells) {
	    switch (ant.dir) {
	    case NORTH:
		ant.row--;
		break;
	    case EAST:
		ant.column++;
		break;
	    case SOUTH:
		ant.row++;
		break;
	    case WEST:
		ant.column--;
		break;
	    }
	    checkOffGrid(ant, cells);
	}

    }

}
