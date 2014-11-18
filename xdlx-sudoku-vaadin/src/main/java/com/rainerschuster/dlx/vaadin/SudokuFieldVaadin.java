package com.rainerschuster.dlx.vaadin;

import java.awt.Color;
//import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import com.rainerschuster.sudoku.SudokuField;
import com.rainerschuster.sudoku.SudokuProperties;
import com.rainerschuster.sudoku.SudokuValue;
import com.rainerschuster.sudoku.view.ViewProperties;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;

public class SudokuFieldVaadin extends CustomComponent {
	private static final long serialVersionUID = 1L;

	private GridLayout mainLayout;
	private SudokuProperties properties;
	//private int[][] field;
	private SudokuCellVaadin[][] cells;

	/**
	 * This is the default constructor
	 */
	public SudokuFieldVaadin(SudokuProperties properties) {
		super();
		this.properties = properties;
		initialize();
	}
	
	/**
	 * This method initializes this field
	 */
	private void initialize() {
		mainLayout = new GridLayout(properties.getEdgeLength(), properties.getEdgeLength());

		cells = new SudokuCellVaadin[properties.getEdgeLength()][properties.getEdgeLength()];
		for (int row = 0; row < properties.getEdgeLength(); row++) {
			for (int col = 0; col < properties.getEdgeLength(); col++) {
				cells[row][col] = new SudokuCellVaadin();
				mainLayout.addComponent(cells[row][col], col, row);
			}
		}

		final int size = ViewProperties.FIELD_SIZE * properties.getEdgeLength();
		mainLayout.setWidth(size, Unit.PIXELS);
		mainLayout.setHeight(size, Unit.PIXELS);
		setWidth(size, Unit.PIXELS);
		setHeight(size, Unit.PIXELS);

		setCompositionRoot(mainLayout);
	}

	/*public int[][] getField() {
		return field;
	}

	public void setField(int[][] field) {
		this.field = field;
	}*/

	public SudokuProperties getProperties() {
		return properties;
	}

	public void setProperties(SudokuProperties properties) {
		this.properties = properties;
	}
	
	public void clear() {
		for (int row = 0; row < properties.getEdgeLength(); row++) {
			for (int col = 0; col < properties.getEdgeLength(); col++) {
				cells[row][col].setNumber(null);
//				cells[row][col].setEditable(true);
				cells[row][col].setEnabled(true);
//				cells[row][col].setFont(ViewProperties.defaultFont);
				cells[row][col].removeStyleName("given");
				cells[row][col].removeStyleName("north");
				cells[row][col].removeStyleName("east");
				cells[row][col].removeStyleName("south");
				cells[row][col].removeStyleName("west");
			}
		}
	}

	public void importData(final SudokuField field, final boolean showSolution) {
		final ViewProperties vProps = new ViewProperties();
		for (int row = 0; row < properties.getEdgeLength(); row++) {
			for (int col = 0; col < properties.getEdgeLength(); col++) {
				final List<Integer> coordinates = new ArrayList<Integer>(2);
				coordinates.add(row);
				coordinates.add(col);

				if (showSolution || field.isGiven(coordinates)) {
					cells[row][col].setNumber(field.getField().get(coordinates));
				}
//				cells[row][col].setEditable(!field.isGiven(coordinates));
//				//cells[row][col].setEnabled(!bean.isGiven());
				cells[row][col].setEnabled(!field.isGiven(coordinates));
				if (field.isGiven(coordinates)) {
					cells[row][col].addStyleName("given");
				} else {
					cells[row][col].removeStyleName("given");
				}

				Color cellColor = Color.WHITE;
				Color borderColor = ViewProperties.defaultColor;
				if (properties.isXSudoku()) {
					if (row == col || row + col + 1 == properties.getEdgeLength()) {
						cellColor = vProps.getXCellColor();
						borderColor = vProps.getXBorderColor();
						cells[row][col].addStyleName("colorX");
					} else {
						cells[row][col].removeStyleName("colorX");
					}
				}
				if (properties.isColorSudoku()) {
					final int colorIndex = properties.getColor().get(coordinates);
					cellColor = vProps.getColor(colorIndex);
					for (int i = 0; i < properties.getEdgeLength(); i++) {
						final String cellColorString = "color" + (i + 1);
						if (i == colorIndex) {
							cells[row][col].addStyleName(cellColorString);
						} else {
							cells[row][col].removeStyleName(cellColorString);
						}
					}
				}
//				cells[row][col].setBackground(cellColor);
				
				cells[row][col].setMinNumber(1);
				cells[row][col].setMaxNumber(properties.getNumbers());
				
				final List<Integer> neighborCoordinates = new ArrayList<Integer>(2);
				neighborCoordinates.add(row);
				neighborCoordinates.add(col);
				
//				cells[row][col].getBorder().setLineColor(borderColor);
				// north (top)
				neighborCoordinates.set(0, row - 1);
				if (row == 0 || !properties.getRegion().get(coordinates).equals(properties.getRegion().get(neighborCoordinates))) {
					cells[row][col].addStyleName("north");
				} else {
					cells[row][col].removeStyleName("north");
				}
				// south (bottom)
				neighborCoordinates.set(0, row + 1);
				if (row == properties.getEdgeLength() - 1 || !properties.getRegion().get(coordinates).equals(properties.getRegion().get(neighborCoordinates))) {
					cells[row][col].addStyleName("south");
				} else {
					cells[row][col].removeStyleName("south");
				}
				// east (right)
				neighborCoordinates.set(0, row);
				neighborCoordinates.set(1, col + 1);
				if (col == properties.getEdgeLength() - 1 || !properties.getRegion().get(coordinates).equals(properties.getRegion().get(neighborCoordinates))) {
					cells[row][col].addStyleName("east");
				} else {
					cells[row][col].removeStyleName("east");
				}
				// west (left)
				neighborCoordinates.set(1, col - 1);
				if (col == 0 || !properties.getRegion().get(coordinates).equals(properties.getRegion().get(neighborCoordinates))) {
					cells[row][col].addStyleName("west");
				} else {
					cells[row][col].removeStyleName("west");
				}
//				
//				// to reset validation-color
//				cells[row][col].addKeyListener(new java.awt.event.KeyAdapter() {
//					@Override
//          public void keyTyped(java.awt.event.KeyEvent e) {
//						((JComponent) e.getSource()).setForeground(ViewProperties.defaultColor);
//					}
//				});
			}
		}
		//revalidate();
	}
	
	// TODO QUEST replace by exportData (which returns SudokuField)?
	public List<SudokuValue> exportValues() {
		final List<SudokuValue> list = new ArrayList<SudokuValue>();
		
		for (int row = 0; row < properties.getEdgeLength(); row++) {
			for (int col = 0; col < properties.getEdgeLength(); col++) {
				if (cells[row][col].getNumber() > 0) {
					final List<Integer> coordinates = new ArrayList<Integer>(2);
					coordinates.add(row);
					coordinates.add(col);
					
					final SudokuValue value = new SudokuValue(properties, cells[row][col].getNumber());
					value.setCoordinates(coordinates);
					
					list.add(value);
				}
			}
		}
		
		return list;
	}

}
