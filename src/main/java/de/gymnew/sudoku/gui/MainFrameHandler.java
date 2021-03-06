package de.gymnew.sudoku.gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.gymnew.sudoku.algorithm.Standard;
import de.gymnew.sudoku.core.Solver;
import de.gymnew.sudoku.core.SolverWatcher;
import de.gymnew.sudoku.model.Field;
import de.gymnew.sudoku.model.Sudoku;
import static de.gymnew.sudoku.gui.SudokuPanel.*;

public class MainFrameHandler extends MouseAdapter implements SolverWatcher {

	private MainFrame frame;

	public MainFrameHandler(MainFrame frame) {
		this.frame = frame;
	}

	/* ================================================== */
	// From DrawPanel
	/* ================================================== */

	@Override
	public void mouseClicked(MouseEvent event) {
		Point click = event.getPoint();
		int x = (int) click.getX();
		int y = (int) click.getY();
		x -= (OFFSET_SIDE + BLOCK_SEPARATOR_WIDTH) * frame.getScale();
		y -= (OFFSET_TOP + BLOCK_SEPARATOR_WIDTH) * frame.getScale();
		if (x <= 0 || y <= 0) {
			return;
		}
		int block_x = (int) x / ((FIELD_SIZE * 3 + BLOCK_SEPARATOR_WIDTH) * frame.getScale());
		int block_y = (int) y / ((FIELD_SIZE * 3 + BLOCK_SEPARATOR_WIDTH) * frame.getScale());
		if (block_x > 2 || block_y > 2) {
			return;
		}
		x -= block_x * (FIELD_SIZE * 3 + BLOCK_SEPARATOR_WIDTH) * frame.getScale();
		y -= block_y * (FIELD_SIZE * 3 + BLOCK_SEPARATOR_WIDTH) * frame.getScale();
		int field_x = (int) x / (FIELD_SIZE * frame.getScale());
		int field_y = (int) y / (FIELD_SIZE * frame.getScale());
		if (field_x > 2 || field_y > 2) {
			return;
		}

		Field field = frame.getSudoku().getField(block_x * 3 + field_x, block_y * 3 + field_y);

		if (event.getButton() == MouseEvent.BUTTON2) {
			field.setLocked((!field.isLocked()) && field.getValue() != 0);
			frame.repaint();
			return;
		}

		if (field.isLocked()) {
			return;
		}

		if (event.getButton() == MouseEvent.BUTTON1) {
			String s = JOptionPane.showInputDialog(frame, "Wert: (0 = leer)", field.getValue());

			if (s == null || "".equals(s)) {
				return;
			}

			byte b;
			try {
				b = Byte.parseByte(s);
			} catch (NumberFormatException e) {
				b = -1;
			}
			if (b < 0 || b > 9) {
				JOptionPane.showMessageDialog(frame, "Ung\u00fcltige Eingabe!", "Eingabe-Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else if (!field.canHaveValue(b)) {
				JOptionPane.showMessageDialog(frame, "Diese Zahl kann hier nicht eingetragen werden!", "Eingabe-Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				field.setValue(b);
			}
		} else if (event.getButton() == MouseEvent.BUTTON3) {
			Set<Byte> notes = field.getNotes();
			String n = "";

			for (byte f : notes) {
				n = n + f;
			}

			String s = JOptionPane.showInputDialog(frame, "Notizen: (Nicht-Ziffern werden ignoriert)", n);
			if (s == null) {
				return;
			}

			Set<Byte> newNotes = new HashSet<Byte>();
			for (char c : s.toCharArray()) {
				try {
					byte b = Byte.parseByte("" + c);
					if (b != 0) {
						newNotes.add(b);
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
			field.clearNotes();
			field.addNotes(newNotes);
		}
		frame.repaint();
	}

	/* ================================================== */
	// From MainFrame
	/* ================================================== */

	public void onMenuQuit() {
		System.exit(0);
	}

	public void onMenuLoad() {
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		File file = chooser.getSelectedFile();

		if (file != null) {
			try {
				frame.setSudoku(Sudoku.load(file));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Sudoku konnte nicht geladen werden!", "Fehler beim Laden!",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(frame, "Keine Datei ausgew\u00E4hlt", "Fehler beim Laden!",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void onMenuSave() {
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(null);
		File file = chooser.getSelectedFile();

		if (file != null) {
			try {
				frame.getSudoku().save(file);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Sudoku konnte nicht gespeichert werden!",
						"Fehler beim Speichern!", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(frame, "Kein Speicherort ausgew\u00E4hlt", "Fehler beim Speichern!",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public void onMenuCredits() {
		JOptionPane.showMessageDialog(frame,
				"Projektleiter: Sven Gebauer, Tobias Gr\u00F6mer\n"
				+ "Betreuende Lehrkraft: StD Horst Dippold\n"
				+ "Developer:  Tobias Bodensteiner, Katharina Hauer, Valentin Kellner, Elena Menzl,\n"
				+ "Jonas Piehler, Alexander Puff, Maximilian Rauch, Catrin Schnupfhagn, Matthias Zetzl",
				"Credits", JOptionPane.INFORMATION_MESSAGE);
	}

	public void onMenuStartSolver() {
		if (frame.getSolver() != null && frame.getSolver().isAlive()) {
			JOptionPane.showMessageDialog(frame, "Solver l\u00e4uft bereits", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}

		frame.setSolver(new Solver(new Standard(frame.getSudoku(), true), this));
		frame.getSolver().start();
	}

	public void onMenuStopSolver() {
		if (frame.getSolver() == null || (!frame.getSolver().isAlive())) {
			JOptionPane.showMessageDialog(frame, "Solver l\u00e4uft nicht", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}

		frame.getSolver().interrupt();
	}
	
	public void onMenuClear() {
		frame.setSudoku(new Sudoku());
	}
	
	public void onMenuReset() {
		Sudoku s =frame.getSudoku();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
					if (!s.getField(i, j).isLocked()) {
					s.getField(i, j).setValue((byte)0);
					s.getField(i, j).clearNotes();
					}
			}
		}
		frame.setSudoku(s);
	}

	public void onMenuCheck() {
		if (frame.getSudoku().isValid()) {
			JOptionPane.showMessageDialog(frame, "Das Sudoku ist g\u00fcltig!");
		} else {
			JOptionPane.showMessageDialog(frame, "Das Sudoku ist ung\u00fcltig!");
		}
	}

	public void onMenuScale() {
		String s = JOptionPane.showInputDialog(frame, "Neuer Skalierungs-Faktor: (Ganzzahl < 0)", frame.getScale());
		try {
			int i = Integer.parseInt(s);
			if (i < 1) {
				JOptionPane.showMessageDialog(frame, "Ung\u00fcltige Eingabe!", "Eingabe-Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
			frame.setScale(i);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Ung\u00fcltige Eingabe!", "Eingabe-Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/* ================================================== */
	// SolverWatcher methods
	/* ================================================== */

	@Override
	public void onUpdate(Solver solver, Sudoku sudoku) {
		frame.setSudoku(sudoku);
		frame.getContentPane().repaint();
		if (frame.isDebugEnabled()) {
			JOptionPane.showMessageDialog(null, "Pause!");
		}
	}

	@Override
	public void onFinished(Solver solver) {
		if (solver.getResult() == null) {
			JOptionPane.showMessageDialog(frame, "Der Solver konnte kein Ergebnis finden!");
		} else {
			frame.setSudoku(solver.getResult());
			frame.getContentPane().repaint();
			JOptionPane.showMessageDialog(frame, "Der Solver hat ein Ergebnis gefunden!");
		}
	}

	@Override
	public void onInterrupted(Solver solver) {
		frame.setSudoku(solver.getAlgorithm().getSudoku());
		frame.getContentPane().repaint();
		JOptionPane.showMessageDialog(frame, "Der Solver wurde unterbrochen!");
	}
}
