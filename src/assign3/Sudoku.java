package assign3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.

	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
			"1 6 4 0 0 0 0 0 2",
			"2 0 0 4 0 3 9 1 0",
			"0 0 5 0 8 0 4 0 7",
			"0 9 0 0 0 6 5 0 0",
			"5 0 0 1 0 2 0 0 8",
			"0 0 8 9 0 0 0 3 0",
			"8 0 9 0 4 0 2 0 0",
			"0 7 3 5 0 9 0 0 1",
			"4 0 0 0 0 0 6 7 9");

	public static final int[][] zeroGrid = Sudoku.stringsToGrid(
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0 0",
			"0 0 0 0 0 9 0 0 0",
			"0 0 0 0 0 0 6 0 0");

	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
			"530070000",
			"600195000",
			"098000060",
			"800060003",
			"400803001",
			"700020006",
			"060000280",
			"000419005",
			"000080079");

	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
			"3 7 0 0 0 0 0 8 0",
			"0 0 1 0 9 3 0 0 0",
			"0 4 0 7 8 0 0 0 3",
			"0 9 3 8 0 0 0 1 2",
			"0 0 0 0 4 0 0 0 0",
			"5 2 0 0 0 6 7 9 0",
			"6 0 0 0 2 1 0 4 0",
			"0 0 0 5 3 0 9 0 0",
			"0 3 0 0 0 0 0 5 1");


	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	/**
	 * Private copy of the board.
	 */
	private Spot[][] board;

	/**
	 * List of spots that need to be assigned a number.
	 */
	private List<Spot> spots;

	/**
	 * List of strings that store solutions.
	 */
	private List<String> solutions;

	/**
	 * Stores how much time solve() took to solve the board.
	 */
	private long elapsed;

	// Provided various static utility methods to
	// convert data formats to int[][] grid.

	/* Implementation idea: Create inner class 'Square'
		and divide the board into 2-dimensional array of
		Squares each one being 3x3 grid of spots. Don't
		have enough time to implement this unfortunately. */

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		board = new Spot[SIZE][SIZE];
		spots = new ArrayList<>();
		solutions = new ArrayList<>();
		elapsed = 0L;
		copyBoard(ints);
		computeWeights();
		Collections.sort(spots);
	}

	/**
	 * Calls main constructor with correct arguments.
	 *
	 * @param text
	 */
	public Sudoku(String text) {
		this(textToGrid(text));
	}

	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 *
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row < rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}

	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 *
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE * SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}

		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}

	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 *
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i = 0; i < string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i + 1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}

	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);

		int count = sudoku.solve();
		sudoku = new Sudoku(sudoku.getSolutionText());
		System.out.println(sudoku); // print the raw problem
		count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	/**
	 * Computes weights of each spot on the board.
	 */
	private void computeWeights() {
		for (Spot[] i : board) {
			for (Spot j : i) {
				if (j.getValue() == 0) {
					j.computeWeight();
					spots.add(j);
				}
			}
		}
	}

	/**
	 * Copies given board to local instance variable.
	 *
	 * @param ints
	 */
	private void copyBoard(int[][] ints) {
		for (int i = 0; i < ints.length; i++) {
			for (int j = 0; j < ints[i].length; j++) {
				board[i][j] = new Spot(i, j, ints[i][j]);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Spot[] i : board) {
			for (Spot j : i) {
				builder.append(j.getValue() + " ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long start = System.currentTimeMillis();
		solveRecursively(0);
		long end = System.currentTimeMillis();

		elapsed = end - start;
		return solutions.size();
	}

	/**
	 * Recursively solves Sudoku board.
	 *
	 * @param index start
	 */
	private void solveRecursively(int index) {
		// If we've found more than 100 solutions no need to continue anymore.
		if (solutions.size() >= MAX_SOLUTIONS || spots.isEmpty())
			return;

		// If we've successfully assigned a number to every element in the
		// spots list that means we've completed the board and need to save
		// it to the solutions list.
		if (index > spots.size() - 1) {
			solutions.add(this.toString());
			return;
		}

		// Tries to assign every valid number to each spot in the list. After
		// assigning number to a spot lets the recursion do its thing and then
		// removes the number and tries something else.
		Spot s = spots.get(index);
		for (int i = 1; i < 10; i++) {
			if (s.setValue(i)) {
				solveRecursively(index + 1);
				s.setValue(0);
			}
		}
	}

	/**
	 * Returns a solution in a string form.
	 *
	 * @return solution
	 */
	public String getSolutionText() {
		return !solutions.isEmpty() ? solutions.get(0) : "";
	}

	/**
	 * Returns time that was needed to recursively solve Sudoku board.
	 *
	 * @return elapsed time
	 */
	public long getElapsed() {
		return elapsed;
	}


	private class Spot implements Comparable<Spot> {
		/**
		 * Each spot stores its row, column and value.
		 */
		private int row;
		private int col;
		private int value;

		/**
		 * Stores how many different values it can take and if
		 * that value is already computed.
		 */
		private int weight;
		private boolean computed;

		/**
		 * Constructor.
		 *
		 * @param row
		 * @param col
		 * @param value
		 */
		public Spot(int row, int col, int value) {
			this.row = row;
			this.col = col;
			this.value = value;
			this.computed = false;
		}

		/**
		 * Calls default constructor with 0 value.
		 *
		 * @param row
		 * @param col
		 */
		public Spot(int row, int col) {
			this(row, col, 0);
		}

		/**
		 * Gets the value of the spot.
		 *
		 * @return value
		 */
		public int getValue() {
			return value;
		}

		/**
		 * Sets the value of the spot.
		 *
		 * @param value
		 * @return true if successful
		 */
		public boolean setValue(int value) {
			if (value == 0 || (checkSquare(value) && checkRow(value) && checkColumn(value))) {
				this.value = value;
				return true;
			}
			return false;
		}

		/**
		 * Gets the weight of the spot.
		 *
		 * @return weight
		 */
		public int getWeight() {
			// Makes sure weight is computed before returning anything invalid.
			if (!computed) {
				throw new RuntimeException("Weights must be computed before getWeight() is called.");
			}
			return this.weight;
		}

		/**
		 * Computes weight value for this spot.
		 */
		public void computeWeight() {
			// Weight's value is at most 9.
			int weight = 9;

			// Check every assignable number, subtract 1 every time given
			// number is invalid for this spot.
			for (int i = 1; i < 10; i++) {
				if (!checkColumn(i) || !checkRow(i) || !checkSquare(i)) {
					weight--;
				}
			}

			// Update the weight and its state.
			this.weight = weight;
			computed = true;
		}

		/**
		 * Checks if given value is unique in this spot's square.
		 *
		 * @param value
		 * @return true if the value's unique
		 */
		private boolean checkSquare(int value) {
			// Calculate square's start coordinates. Works because of the integer division.
			int x = (row / PART) * PART;
			int y = (col / PART) * PART;

			// Check if the given value is present in this square.
			boolean flag = true;
			for (int i = 0; i < PART; i++) {
				for (int j = 0; j < PART; j++) {
					if (board[x + i][y + j].getValue() == value)
						flag = false;
				}
			}
			return flag;
		}

		/**
		 * Checks if given value is unique in this spot's column.
		 *
		 * @param value
		 * @return true if the value's unique
		 */
		private boolean checkColumn(int value) {
			boolean flag = true;
			for (Spot[] s : board) {
				if (s[col].getValue() == value)
					flag = false;
			}
			return flag;
		}

		/**
		 * Checks if given value is unique in this spot's row.
		 *
		 * @param value
		 * @return true if the value's unique
		 */
		private boolean checkRow(int value) {
			boolean flag = true;
			for (Spot s : board[row]) {
				if (s.getValue() == value)
					flag = false;
			}
			return flag;
		}

		@Override
		public int compareTo(Spot that) {
			if (this.getWeight() < that.getWeight()) return -1;
			if (this.getWeight() > that.getWeight()) return 1;
			return 0;
		}

		@Override
		public String toString() {
			return "[" + col + "]" + "[" + row + "] \t V: " + value + ", W: " + weight;
		}
	}

}
