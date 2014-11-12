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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SudokuField {
	private SudokuProperties properties = new SudokuProperties();
	// TODO field, givens
	private Map<List<Integer>, Integer> field = new HashMap<List<Integer>, Integer>();
	private List<SudokuValue> givens = new ArrayList<SudokuValue>();

	public SudokuField(SudokuProperties properties) {
		super();
		this.properties = properties;
	}

	public SudokuProperties getProperties() {
		return properties;
	}

	public void setProperties(SudokuProperties properties) {
		this.properties = properties;
	}

	public Integer put(SudokuValue value) {
		return field.put(value.getCoordinates(), value.getNumber());
	}
	
	public boolean isGiven(List<Integer> coordinates) {
		for (SudokuValue given : givens) {
			if (coordinates.equals(given.getCoordinates())) {
				return true;
			}
		}
		return false;
	}

	public Map<List<Integer>, Integer> getField() {
		return field;
	}

	public void setField(Map<List<Integer>, Integer> field) {
		this.field = field;
	}

	public List<SudokuValue> getGivens() {
		return givens;
	}

	public void setGivens(List<SudokuValue> givens) {
		this.givens = givens;
	}

}
