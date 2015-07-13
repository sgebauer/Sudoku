package de.gymnew.sudoku.model;

public class Column extends Cluster {

	public Column(int column, Sudoku sudoku){
		super();
		for(int i = 0; i<9; i++){
			fields.add(sudoku.getField(column, i));
		}
	}
}
