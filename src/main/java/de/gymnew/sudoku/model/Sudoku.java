package de.gymnew.sudoku.model;

import java.io.File;

public class Sudoku {

	private Field[][] fields;
	private Row[] rows;
	private Column[] columns;
	private Block[][] blocks;

	public Sudoku() {
		fields = new Field[9][9];
		
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				fields[i][j] = new Field();
			}
		}
		// TODO Create Rows, Columns and Blocks
	}

	public Field getField(int column, int row) {
		return fields[column][row];
	}
	
	@Override
	public Sudoku clone() {
		Sudoku sudoku = new Sudoku();
		for(int i = 0; i<9; i++) {
			for(int j = 0; j<9; j++) {
				Field src = fields[i][j];
				Field dst = sudoku.getField(i, j);
				dst.setValue(src.getValue());
				dst.addNotes(src.getNotes());
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				s += fields[i][j].getValue();
			}
			s += "|";
		}
		return s;
	}

	private void load(String string) {
		// TODO Auto-generated method stub
		for(int i = 0;i<9;i++){
			for(int j=0;j<9;j++){
				string.charAt();
			}
		}
	}
	
	public static Sudoku load(File file) {
		// TODO Auto-generated method stub
		return null;
	}

}
