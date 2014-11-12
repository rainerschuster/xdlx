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

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.rainerschuster.dlx.Value;

public class SudokuValue extends Value<SudokuColumnValue> {

	private SudokuProperties properties;
	private int number;
	private List<Integer> coordinates;
	
	public SudokuValue(SudokuProperties properties, int number) {
		super();
		this.properties = properties;
		this.number = number;
		this.coordinates = new Vector<Integer>();
	}

	@Override
	public boolean inRelation(SudokuColumnValue columnValue) {
		// TODO Auto-generated method stub
		Map<String, Integer> values = columnValue.getValues();
		for (String valueName : values.keySet()) {
			if (valueName.equals("n")) {
				if (!values.get("n").equals(number)) {
					return false;
				}
			} else if (valueName.equals("x1")) {
				if (!values.get("x1").equals(coordinates.get(0))) {
					return false;
				}
			} else if (valueName.equals("x2")) {
				if (!values.get("x2").equals(coordinates.get(1))) {
					return false;
				}
			} else if (valueName.equals("r")) {
				if (!values.get("r").equals(properties.getRegion().get(coordinates))) {
					return false;
				}
			} else if (valueName.equals("c")) {
				if (!values.get("c").equals(properties.getColor().get(coordinates))) {
					return false;
				}
			} else if (valueName.equals("d1")) {
				// trick to test if on diagonal
				if (!coordinates.get(0).equals(coordinates.get(1)) || !values.get("d1").equals(number)) {
					return false;
				}
			} else if (valueName.equals("d2")) {
				// trick to test if on diagonal
				if (properties.getEdgeLength() != (coordinates.get(0) + coordinates.get(1) + 1) || !values.get("d2").equals(number)) {
					return false;
				}
			}
		}
		return true;
	}
	

	public List<Integer> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Integer> coordinates) {
		this.coordinates = coordinates;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public SudokuProperties getProperties() {
		return properties;
	}

	public void setProperties(SudokuProperties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "(" + number + ", " + coordinates.toString() + ")";
	}
}
