package de.gymnew.sudoku.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Set;

import javax.swing.JPanel;

import de.gymnew.sudoku.model.Sudoku;

@SuppressWarnings("serial")
public class SudokuPanel extends JPanel {

	public static final int FIELD_SIZE = 10;
	public static final int NUMBER_SIZE = 8;
	public static final int NUMBER_OFFSET = 2;
	public static final int NOTE_SIZE = 2;
	public static final int NOTE_OFFSET = 2;

	public static final int BLOCK_SEPARATOR_WIDTH = 2;

	public static final int OFFSET_SIDE = 6;
	public static final int OFFSET_TOP = 6;
	public static final int OFFSET_BOTTOM = 6;

	private Sudoku sudoku;
	private MainFrame frame;

	public SudokuPanel(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (sudoku == null)
			return;

		int field_size = FIELD_SIZE * frame.getScale();
		g.setFont(new Font("Arial", Font.PLAIN, NUMBER_SIZE * frame.getScale()));

		for (int block_x = 0; block_x < 3; block_x++) {
			for (int block_y = 0; block_y < 3; block_y++) {
				int base_x = OFFSET_SIDE + BLOCK_SEPARATOR_WIDTH + ((FIELD_SIZE * 3) + BLOCK_SEPARATOR_WIDTH) * block_x;
				base_x *= frame.getScale();
				int base_y = OFFSET_TOP + BLOCK_SEPARATOR_WIDTH + ((FIELD_SIZE * 3) + BLOCK_SEPARATOR_WIDTH) * block_y;
				base_y *= frame.getScale();

				g.setColor(Color.BLACK);
				g.fillRect(base_x - (BLOCK_SEPARATOR_WIDTH * frame.getScale()),
						base_y - (BLOCK_SEPARATOR_WIDTH * frame.getScale()),
						(BLOCK_SEPARATOR_WIDTH * 2 + FIELD_SIZE * 3) * frame.getScale(),
						(BLOCK_SEPARATOR_WIDTH * 2 + FIELD_SIZE * 3) * frame.getScale());
				g.setColor(Color.WHITE);
				g.fillRect(base_x, base_y, field_size * 3, field_size * 3);
				g.setColor(Color.BLACK);

				for (int field_x = 0; field_x < 3; field_x++) {
					for (int field_y = 0; field_y < 3; field_y++) {
						int x = base_x + (field_size * field_x);
						int y = base_y + (field_size * field_y);
						g.drawRect(x, y, field_size, field_size);

						int value = sudoku.getField(field_x + 3 * block_x, field_y + 3 * block_y).getValue();
						if (value != 0) {
							if (sudoku.getField(field_x + 3 * block_x, field_y + 3 * block_y).isLocked())
								g.setColor(Color.GRAY);
							g.drawChars(new char[] { ("" + value).charAt(0) }, 0, 1, x + NUMBER_OFFSET * frame.getScale(),
									y + field_size - NUMBER_OFFSET * frame.getScale()); // TODO drawstring() ?
							g.setColor(Color.BLACK);
						} else {
							Set<Byte> notes = sudoku.getField(field_x + 3 * block_x, field_y + 3 * block_y).getNotes();
							String s = "";
							for(byte f : notes){
								s = s+f+" ";
							}
							String s1 = "";
							String s2 = "";
							String s3 = "";
							s1 = s.substring(0);
							if(s1.length() > 6){
								s1 = s1.substring(0,5);
								s2 = s.substring(6);
								if(s2.length() > 6){
									s2 = s2.substring(0,5);
									s3 = s.substring(12);									
								}
							}
							g.setFont(new Font("Arial", Font.PLAIN, NOTE_SIZE * frame.getScale()));
							g.drawString(s1, x + NOTE_OFFSET * frame.getScale(), y + field_size - NOTE_OFFSET * 3 * frame.getScale());
							g.drawString(s2, x + NOTE_OFFSET * frame.getScale(), y + field_size - NOTE_OFFSET * 2 * frame.getScale());
							g.drawString(s3, x + NOTE_OFFSET * frame.getScale(), y + field_size - NOTE_OFFSET * frame.getScale());
							g.setFont(new Font("Arial", Font.PLAIN, NUMBER_SIZE * frame.getScale()));
							
						}
					}
				}
			}
		}
		
	}

	public Sudoku getSudoku() {
		return sudoku;
	}

	public void setSudoku(Sudoku sudoku) {
		this.sudoku = sudoku;
	}

}
