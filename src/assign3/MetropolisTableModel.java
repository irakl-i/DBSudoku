package assign3;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


public class MetropolisTableModel extends AbstractTableModel {

	private List<String> columnNames; // defines the number of cols
	private List<List> data; // one List for each row
	private List<Entry> entries;
	private DatabaseAccess access;

	/**
	 * Constructor.
	 */
	public MetropolisTableModel() {
		columnNames = new ArrayList<>();
		data = new ArrayList<>();
		access = new DatabaseAccess();
		columnNames = access.getColumnNames();
	}

	/**
	 * Adds entries to the table.
	 */
	private void addEntries() {
		// Now this is some cool-ass code right here if I do say so myself.
		for (Entry e : entries) {
			addRow(new ArrayList<String>() {{
				add(e.getMetropolis());
				add(e.getContinent());
				add(Integer.toString(e.getPopulation()));
			}});
		}
	}

	/**
	 * Returns the name of each col, numbered 0..columns-1
	 *
	 * @param col column
	 * @return column name
	 */
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	/**
	 * Returns the number of columns
	 *
	 * @return number of columns
	 */
	public int getColumnCount() {
		return columnNames.size();
	}

	/**
	 * Returns the number of rows
	 *
	 * @return number of rows
	 */
	public int getRowCount() {
		return data.size();
	}

	/**
	 * Returns the data for each cell, identified by its row, col index.
	 *
	 * @param row
	 * @param col
	 * @return data
	 */
	public Object getValueAt(int row, int col) {
		List rowList = data.get(row);
		Object result = null;
		if (col < rowList.size()) {
			result = rowList.get(col);
		}

		// _apparently_ it's ok to return null for a "blank" cell
		return (result);
	}

	/**
	 * Returns true if a cell should be editable in the table
	 *
	 * @param row
	 * @param col
	 * @return true if a cell should be editable
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * Changes the value of a cell
	 *
	 * @param value
	 * @param row
	 * @param col
	 */
	@SuppressWarnings("unchecked")
	public void setValueAt(Object value, int row, int col) {
		List rowList = data.get(row);

		// make this row long enough
		if (col >= rowList.size()) {
			while (col >= rowList.size()) {
				rowList.add(null);
			}
		}

		// install the data
		rowList.set(col, value);

		// notify model listeners of cell change
		fireTableCellUpdated(row, col);
	}

	/**
	 * Adds entry to the table and the database
	 *
	 * @param entry
	 */
	public void addEntry(Entry entry) {
		resetTable();
		addRow(new ArrayList<String>() {{
			add(entry.getMetropolis());
			add(entry.getContinent());
			add(Integer.toString(entry.getPopulation()));
		}});
		access.addEntry(entry);
	}

	/**
	 * Searches entry based on search modifiers
	 *
	 * @param entry  value
	 * @param larger modifier
	 * @param exact  modifier
	 */
	public void searchEntry(Entry entry, boolean larger, boolean exact) {
		resetTable();
		entries = access.searchEntry(entry, larger, exact);
		addEntries();
	}

	/**
	 * Resets the table
	 */
	private void resetTable() {
		data.clear();
		fireTableDataChanged();
	}

	/**
	 * Adds the given row, returns the new row index
	 *
	 * @param row
	 * @return
	 */
	public int addRow(List row) {
		data.add(row);
		fireTableRowsInserted(data.size() - 1, data.size() - 1);
		return (data.size() - 1);
	}

	/**
	 * Tells access to close the connection to the database.
	 */
	public void closeConnection() {
		access.closeConnection();
	}
}
