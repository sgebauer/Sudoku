package de.gymnew.sudoku.model;

import java.util.Set;

public class Field {

	private byte value; // 0 = undefined
	private Set<Byte> notes;
	private Row row;
	private Column col;
	private Block block;
	
	public byte getValue() {
		return value;
	}
	public void setValue(byte value) {
		this.value = value;
	}
	public Set<Byte> getNotes() {
		return notes;
	}
	public void addNote(byte b) {
		notes.add(b);
	}
	public boolean hasNote(byte b){
		return notes.contains(b);
	}
	public boolean deleteNote(byte b){
		return notes.remove(b);
	}
	public Row getRow() {
		return row;
	}
	public Column getCol() {
		return col;
	}
	public Block getBlock() {
		return block;
	}

}