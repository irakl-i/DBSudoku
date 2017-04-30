package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;


public class SudokuFrame extends JFrame {
	private JTextArea puzzleArea;
	private JTextArea solutionArea;
	private JPanel panel;
	private JButton checkButton;
	private JCheckBox checkBox;


	public SudokuFrame() {
		super("Sudoku Solver");

		// Set border layout.
		setLayout(new BorderLayout(4, 4));

		// Add components.
		addTextAreas();
		addPanel();
		addButton();
		addCheckBox();
		addListeners();

		// Default stuff.
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}

		SudokuFrame frame = new SudokuFrame();
	}

	/**
	 * Adds listeners to Check button and Puzzle text area.
	 */
	private void addListeners() {
		// Add button listener using lambda functions.
		checkButton.addActionListener(e -> printSolution());

		// Can't use lambdas here.
		puzzleArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (checkBox.isSelected()) printSolution();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (checkBox.isSelected()) printSolution();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (checkBox.isSelected()) printSolution();
			}
		});
	}

	/**
	 * Adds check box.
	 */
	private void addCheckBox() {
		checkBox = new JCheckBox("Auto Check");
		checkBox.setSelected(true);
		panel.add(checkBox);
	}

	/**
	 * Adds button.
	 */
	private void addButton() {
		checkButton = new JButton("Check");
		panel.add(checkButton);
	}

	/**
	 * Adds panel.
	 */
	private void addPanel() {
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(Box.createVerticalStrut(25));
		add(panel, BorderLayout.SOUTH);
	}

	/**
	 * Adds text areas.
	 */
	private void addTextAreas() {
		// Add puzzle text area.
		puzzleArea = new JTextArea(15, 20);
		puzzleArea.setBorder(new TitledBorder("Puzzle"));
		add(puzzleArea, BorderLayout.CENTER);

		// Add solution text area.
		solutionArea = new JTextArea(15, 20);
		solutionArea.setBorder(new TitledBorder("Solution"));
		add(solutionArea, BorderLayout.EAST);
	}

	private void printSolution() {
		// Solves the puzzle and prints the solution if the input is valid.
		try {
			Sudoku sudoku = new Sudoku(puzzleArea.getText());
			int solutions = sudoku.solve();
			solutionArea.setText(sudoku.getSolutionText() + "\nsolutions: " + solutions + "\nelapsed: " + sudoku.getElapsed());
		} catch (Exception ex) {
			solutionArea.setText("Parsing problem");
		}
	}

}
