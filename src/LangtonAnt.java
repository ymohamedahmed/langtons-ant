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
    private int width = 0;
    private int height = 0;

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
	width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	JFrame frame = new JFrame();
	frame.setSize(width, height);
	frame.setUndecorated(true);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	System.out.println("Input size of square");
	Scanner scan = new Scanner(System.in);
	int cellSize = scan.nextInt();
	scan.close();
	Pane pane = new Pane(cellSize, width, height);
	pane.genCells();
	frame.add(pane);
	frame.setVisible(true);
    }

    public class Pane extends JPanel {
	private int squareSize = 10;
	ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();
	Ant ant;
	int width = 0;
	int height = 0;

	public Pane(int squareSize, int width, int height) {
	    this.squareSize = squareSize;
	    this.width = width;
	    this.height = height;
	}

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
	    ant.row = cells.size() / 2;
	    ant.column = cells.get(0).size() / 2;
	    ant.dir = Direction.NORTH;
	    return cells;

	}

	@Override
	public void paint(Graphics g) {
	    super.paintComponent(g);

	    Graphics2D g2d = (Graphics2D) g.create();

	    for (ArrayList<Cell> cell : cells) {
		for (Cell c : cell) {
		    g2d.setColor(c.color);
		    g2d.fill(c.rect);
		    g2d.setColor(Color.BLACK);
		    g2d.draw(c.rect);
		}
	    }
	    g2d.setColor(Color.RED);
	    g2d.fill(cells.get(ant.row).get(ant.column).rect);
	    g2d.setColor(Color.BLACK);
	    g2d.draw(cells.get(ant.row).get(ant.column).rect);

	    moveAnt(ant, cells);
	    repaint();
	}

	public void moveAnt(Ant ant, ArrayList<ArrayList<Cell>> cells) {
	    if (cells.get(ant.row).get(ant.column).color == Color.WHITE) {
		cells.get(ant.row).get(ant.column).color = Color.BLACK;
		rotateAnt(ant, true);
		moveForward(ant, cells);

	    } else if (cells.get(ant.row).get(ant.column).color == Color.BLACK) {
		cells.get(ant.row).get(ant.column).color = Color.WHITE;
		rotateAnt(ant, false);
		moveForward(ant, cells);

	    }

	}

	public void rotateAnt(Ant ant, boolean right) {
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

	public void checkOffGrid(Ant ant, ArrayList<ArrayList<Cell>> cells) {
	    if (ant.row > cells.size() - 1) {
		ant.row = 0;
	    } else if (ant.row < 0) {
		ant.row = cells.size() - 1;
	    } else if (ant.column > cells.get(0).size() - 1) {
		System.out.println("CELLS SIZE: " + cells.get(0).size());
		ant.column = 0;
	    } else if (ant.column < 0) {
		ant.column = cells.get(0).size() - 1;
	    }
	}

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
