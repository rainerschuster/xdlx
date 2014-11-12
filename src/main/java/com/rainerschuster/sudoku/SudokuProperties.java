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
package com.rainerschuster.sudoku;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class SudokuProperties {

	private int dimensions;
	private int numbers;
	private Map<List<Integer>, Integer> region;
	private Map<List<Integer>, Integer> color;
	private boolean xSudoku;
	//private boolean polyominoSudoku;
	//private boolean colorSudoku;
	
	public int getEdgeLength() {
		return (int) Math.pow(numbers, 1 / (dimensions - 1));
	}
	
	public int getCellCount() {
		return (int) Math.pow(getEdgeLength(), dimensions);
	}
	
	private List<Integer> regionsPerDimension;
	
	private void calcRegionsPerDimension(List<Integer> regionDimension) {
		regionsPerDimension = new Vector<Integer>(dimensions);
		for (int regdim : regionDimension) {
			regionsPerDimension.add((getEdgeLength() / regdim));
		}
	}
	
	// 2D
	public Map<List<Integer>, Integer> generateDefaultRegions(List<Integer> regionDimension) {
		Map<List<Integer>, Integer> region = new HashMap<List<Integer>, Integer>(getCellCount());
		calcRegionsPerDimension(regionDimension);
		//Integer[] cellDimension = new Integer[dimensions];
		for (int x = 0; x < getEdgeLength(); x++) {
			//cellDimension[0] = x;
			for (int y = 0; y < getEdgeLength(); y++) {
				//cellDimension[1] = y;
				Integer[] cellDimension = new Integer[dimensions];
				cellDimension[0] = x;
				cellDimension[1] = y;
				region.put(Arrays.asList(cellDimension), calcDefaultRegion(cellDimension, regionDimension));
			}
		}
		return region;
	}
	
	// 2D
	private int calcDefaultRegion(Integer[] cellDimension, List<Integer> regionDimension) {
		int x0 = (int) Math.floor(cellDimension[0] / regionDimension.get(0));
		int x1 = (int) Math.floor(cellDimension[1] / regionDimension.get(1));
		
		return x0 + (x1 * regionsPerDimension.get(0));
	}
	
	// 2D
	public Map<List<Integer>, Integer> generateDefaultColors(List<Integer> regionDimension) {
		Map<List<Integer>, Integer> color = new HashMap<List<Integer>, Integer>(getCellCount());
		calcRegionsPerDimension(regionDimension);
		//Integer[] cellDimension = new Integer[dimensions];
		for (int x = 0; x < getEdgeLength(); x++) {
			//cellDimension[0] = x;
			for (int y = 0; y < getEdgeLength(); y++) {
				//cellDimension[1] = y;
				Integer[] cellDimension = new Integer[dimensions];
				cellDimension[0] = x;
				cellDimension[1] = y;
				color.put(Arrays.asList(cellDimension), calcDefaultColor(cellDimension, regionDimension));
			}
		}
		return color;
	}
	
	// 2D
	private int calcDefaultColor(Integer[] cellDimension, List<Integer> regionDimension) {
		//field[row][col] = (col % boxWidth) + ((row % boxHeight) * boxWidth);
		return (cellDimension[0] % regionDimension.get(1)) + ((cellDimension[1] % regionDimension.get(0)) * regionDimension.get(1));
	}

	public Map<List<Integer>, Integer> getColor() {
		return color;
	}

	public void setColor(Map<List<Integer>, Integer> color) {
		this.color = color;
	}

	public int getDimensions() {
		return dimensions;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	public int getNumbers() {
		return numbers;
	}

	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}

	public Map<List<Integer>, Integer> getRegion() {
		return region;
	}

	public void setRegion(Map<List<Integer>, Integer> region) {
		this.region = region;
	}

	public boolean isXSudoku() {
		return xSudoku;
	}

	public void setXSudoku(boolean sudoku) {
		xSudoku = sudoku;
	}

	public boolean isColorSudoku() {
		return color != null && !color.isEmpty();
	}
	
}
