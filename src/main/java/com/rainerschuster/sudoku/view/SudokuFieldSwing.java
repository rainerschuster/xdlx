/*
 * Copyright 2007 Rainer Schuster
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rainerschuster.sudoku.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.rainerschuster.sudoku.SudokuField;
import com.rainerschuster.sudoku.SudokuProperties;
import com.rainerschuster.sudoku.SudokuValue;

public class SudokuFieldSwing extends JPanel {

	private static final long serialVersionUID = 1L;
	private SudokuProperties properties;
	//private int[][] field;
	private SudokuCellSwing[][] cells;
	
	/**
	 * This is the default constructor
	 */
	public SudokuFieldSwing(SudokuProperties properties) {
		super();
		this.properties = properties;
		initialize();
	}
	
	/**
	 * This method initializes this field
	 */
	private void initialize() {
		GridLayout gridLayout = new GridLayout(properties.getEdgeLength(), properties.getEdgeLength(), 0, 0);
		this.setLayout(gridLayout);
		
		cells = new SudokuCellSwing[properties.getEdgeLength()][properties.getEdgeLength()];
		for (int row = 0; row < properties.getEdgeLength(); row++) {
			for (int col = 0; col < properties.getEdgeLength(); col++) {
				cells[row][col] = new SudokuCellSwing();
				this.add(cells[row][col]);
			}
		}
		
		int size = ViewProperties.FIELD_SIZE * properties.getEdgeLength();
		
		Dimension d = new Dimension(size, size);
		this.setSize(d);
		this.setPreferredSize(d);
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
				cells[row][col].setEditable(true);
				cells[row][col].setEnabled(true);
				cells[row][col].setFont(ViewProperties.defaultFont);
			}
		}
	}

	public void importData(SudokuField field, boolean showSolution) {
		ViewProperties vProps = new ViewProperties();
		for (int row = 0; row < properties.getEdgeLength(); row++) {
			for (int col = 0; col < properties.getEdgeLength(); col++) {
				List<Integer> coordinates = new Vector<Integer>(2);
				coordinates.add(row);
				coordinates.add(col);
				
				
				if (showSolution || field.isGiven(coordinates)) {
					cells[row][col].setNumber(field.getField().get(coordinates));
				}
				cells[row][col].setEditable(!field.isGiven(coordinates));
				//cells[row][col].setEnabled(!bean.isGiven());
				cells[row][col].setFont(field.isGiven(coordinates) ? ViewProperties.givenFont : ViewProperties.defaultFont);
				
				
				Color cellColor = Color.WHITE;
				Color borderColor = ViewProperties.defaultColor;
				if (properties.isXSudoku() && (row == col || row + col + 1 == properties.getEdgeLength())) {
					cellColor = vProps.getXCellColor();
					borderColor = vProps.getXBorderColor();
				}
				if (properties.isColorSudoku()) {
					cellColor = vProps.getColor(properties.getColor().get(coordinates));
				}
				
				cells[row][col].setBackground(cellColor);
				
				cells[row][col].setMinNumber(1);
				cells[row][col].setMaxNumber(properties.getNumbers());
				
				List<Integer> neighborCoordinates = new Vector<Integer>(2);
				neighborCoordinates.add(row);
				neighborCoordinates.add(col);
				
				cells[row][col].getBorder().setLineColor(borderColor);
				// north (top)
				neighborCoordinates.set(0, row - 1);
				cells[row][col].getBorder().setNorth(row == 0 || !properties.getRegion().get(coordinates).equals(properties.getRegion().get(neighborCoordinates)));
				// south (bottom)
				neighborCoordinates.set(0, row + 1);
				cells[row][col].getBorder().setSouth(row == properties.getEdgeLength() - 1 || !properties.getRegion().get(coordinates).equals(properties.getRegion().get(neighborCoordinates)));
				// east (right)
				neighborCoordinates.set(0, row);
				neighborCoordinates.set(1, col + 1);
				cells[row][col].getBorder().setEast(col == properties.getEdgeLength() - 1 || !properties.getRegion().get(coordinates).equals(properties.getRegion().get(neighborCoordinates)));
				// west (left)
				neighborCoordinates.set(1, col - 1);
				cells[row][col].getBorder().setWest(col == 0 || !properties.getRegion().get(coordinates).equals(properties.getRegion().get(neighborCoordinates)));
				
				// to reset validation-color
				cells[row][col].addKeyListener(new java.awt.event.KeyAdapter() {
					@Override
          public void keyTyped(java.awt.event.KeyEvent e) {
						((JComponent) e.getSource()).setForeground(ViewProperties.defaultColor);
					}
				});
			}
		}
		//revalidate();
	}
	
	// TODO QUEST replace by exportData (which returns SudokuField)?
	public List<SudokuValue> exportValues() {
		List<SudokuValue> list = new Vector<SudokuValue>();
		
		for (int row = 0; row < properties.getEdgeLength(); row++) {
			for (int col = 0; col < properties.getEdgeLength(); col++) {
				if (cells[row][col].getNumber() > 0) {
					List<Integer> coordinates = new Vector<Integer>(2);
					coordinates.add(row);
					coordinates.add(col);
					
					SudokuValue value = new SudokuValue(properties, cells[row][col].getNumber());
					value.setCoordinates(coordinates);
					
					list.add(value);
				}
			}
		}
		
		return list;
	}

}
