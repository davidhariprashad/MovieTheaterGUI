public class TableManager {

	static void sortTable(String[][] table, int columnIndex) {
		
		if (table == null) {
			return;
		}
		
		if (columnIndex < 0 || columnIndex > table[0].length) {
			return;
		}
		
		String temp;
		int rows = table.length;
		int columns = table[0].length;
		int minIndex;
		
		for (int r=0; r<rows; r++) {
			minIndex = r;
			for (int k=r+1; k<rows; k++) {
				if (table[k][columnIndex].compareToIgnoreCase(table[minIndex][columnIndex]) < 0) {
					minIndex = k;
				}
			}
			for (int c=0; c<columns; c++) {
				temp = table[minIndex][c];
				table[minIndex][c] = table[r][c];
				table[r][c] = temp;
			}
		}
		
	} /* void sortTable(String[][] table, int columnIndex) */
	
}