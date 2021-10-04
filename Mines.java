package mines;

import java.util.Random;

public class Mines {// class that describes the game mine sweeper
	private int height, width, numMines = 0;
	private Place[][] matrix;
	private boolean showAll = false;
	private Random rand = new Random();

	public Mines(int height, int width, int numMines) {// constructor
		this.height = height;
		this.width = width;
		matrix = new Place[height][width];// the board of the game
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				matrix[i][j] = new Place();// every square kind of Place class
		int countMines = numMines;
		while (countMines > 0) {// random the places of the mines in the board
			int rxnum = rand.nextInt(height);
			int rynum = rand.nextInt(width);
			if (addMine(rxnum, rynum))//if added mine
				countMines--;

		}
	}

	private class Place {// inner class
		private int numOfmines = 0;// number of neighbors mines
		private boolean mine = false, open = false, flag = false;

		public void setMine() {
			mine = true;// the place is mine
		}

		public void setOpen() {
			open = true;// the place is open
		}

		public void setFlag(int f) {
			if (f == 0)
				flag = false;// remove flag
			else
				flag = true;// add flag

		}

		public String toString() {
			if (open == false && showAll == false) {// place close
				if (flag == true)
					return "F";// flag
				else
					return ".";
			} else {// place open
				if (mine == true)
					return "X"; // mine
				else {
					if (numOfmines != 0)
						return "" + numOfmines;// number of neighbors mines of specific place
					else
						return " ";// no neighbors with mines
				}
			}

		}
	}

	public boolean addMine(int i, int j) {// set mines for checking
		if (i >= height || j >= width || i < 0 || j < 0 || matrix[i][j].mine == true)
			return false;// check if place exist in matrix and if it is not mine
		matrix[i][j].setMine();
		updateAround(i, j);// increase numOfmines of all specific place (i,j) neighbors
		numMines++;// increase the sum of mines in the board
		return true;
	}

	private void updateAround(int i, int j) {// (i,j) place is mine, so update the neighbors
		if (i - 1 >= 0)
			matrix[i - 1][j].numOfmines++;// up
		if (i + 1 < height)
			matrix[i + 1][j].numOfmines++;// down
		if (j - 1 >= 0)
			matrix[i][j - 1].numOfmines++;// left
		if (j + 1 < width)
			matrix[i][j + 1].numOfmines++;// right
		if (i - 1 >= 0 && j - 1 >= 0)
			matrix[i - 1][j - 1].numOfmines++;// up left
		if (i + 1 < height && j + 1 < width)
			matrix[i + 1][j + 1].numOfmines++;// down right
		if (i + 1 < height && j - 1 >= 0)
			matrix[i + 1][j - 1].numOfmines++;// down left
		if (i - 1 >= 0 && j + 1 < width)
			matrix[i - 1][j + 1].numOfmines++;// up right
	}

	public boolean open(int i, int j) {// recursive function to open neighbors with not mines
		if (i >= height || i < 0 || j < 0 || j >= width)
			return false;
		if (matrix[i][j].open == true)// if already open
			return true;
		if (matrix[i][j].mine == true)
			return false;// if the place is mine
		matrix[i][j].setOpen();
		if (matrix[i][j].numOfmines == 0) {// if the neighbors of the place not mines
			open(i - 1, j);// down
			open(i + 1, j);// up
			open(i, j - 1);// left
			open(i, j + 1);// right
			open(i - 1, j - 1);// up left
			open(i - 1, j + 1);// up right
			open(i + 1, j - 1);// down left
			open(i + 1, j + 1);// down right
		}
		return true;
	}

	public void toggleFlag(int x, int y) {
		if (x >= height || y >= width || x < 0 || y < 0)
			return;
		if (matrix[x][y].flag == true)
			matrix[x][y].setFlag(0); // remove flag
		else
			matrix[x][y].setFlag(1); // add flag
	}

	public boolean isDone() {// return true if all places that not mines are opening
		int cnt = 0;// number of places that not mines and are opening
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (matrix[i][j].mine != true && matrix[i][j].open == true)// if the place is not mine and open
					cnt++;
		if ((height * width) - cnt == numMines)// check if all places that not mines are opening
			return true;
		return false;

	}

	public String get(int i, int j) {// return string of specific square in the board
		return matrix[i][j].toString();
	}

	public void setShowAll(boolean showAll) {// function that determines if relate that all places are opening
		this.showAll = showAll;

	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				b.append(get(i, j));// string of square(i,j)
			b.append("\n");// enter between every line
		}
		return b.toString();

	}

}
