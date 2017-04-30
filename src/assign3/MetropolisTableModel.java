package assign3;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MetropolisTableModel extends AbstractTableModel {

	private List<String> colNames; // defines the number of cols
	private List<List> data; // one List for each row
	private Connection connection;

	public MetropolisTableModel() {
		colNames = new ArrayList<>();
		data = new ArrayList<>();
		connection = DatabaseAccess.getConnection();
	}

	// Returns the name of each col, numbered 0..columns-1
	public String getColumnName(int col) {
		return colNames.get(col);
	}

	// Returns the number of columns
	public int getColumnCount() {
		return colNames.size();
	}

	// Returns the number of rows
	public int getRowCount() {
		return data.size();
	}

	// Returns the data for each cell, identified by its
	// row, col index.
	public Object getValueAt(int row, int col) {
		List rowList = data.get(row);
		Object result = null;
		if (col < rowList.size()) {
			result = rowList.get(col);
		}

		// _apparently_ it's ok to return null for a "blank" cell
		return (result);
	}

	// Returns true if a cell should be editable in the table
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	// Adds the given column to the right hand side of the model
	public void addColumn(String name) {
		colNames.add(name);
		fireTableStructureChanged();
	}

	// Adds the given row, returns the new row index
	public int addRow(List row) {
		data.add(row);
		fireTableRowsInserted(data.size() - 1, data.size() - 1);
		return (data.size() - 1);
	}

	// Adds an empty row, returns the new row index
	public int addRow() {
		// Create a new row with nothing in it
		List row = new ArrayList();
		return (addRow(row));
	}
}
