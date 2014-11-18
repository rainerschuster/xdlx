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

import com.rainerschuster.dlx.Node;

public class SudokuSolutionConverter {

  //private SudokuValueConverter converter;

  private SudokuProperties properties;

  /*public SudokuSolutionConverter(SudokuValueConverter converter,
      SudokuProperties properties) {
    super();
    this.converter = converter;
    this.properties = properties;
  }*/

  public SudokuSolutionConverter(SudokuProperties properties) {
    super();
    this.properties = properties;
  }

  /*public SudokuValueConverter getConverter() {
    return converter;
  }

  public void setConverter(SudokuValueConverter converter) {
    this.converter = converter;
  }*/

  public SudokuProperties getProperties() {
    return properties;
  }

  public void setProperties(SudokuProperties properties) {
    this.properties = properties;
  }

  public SudokuField convertSolution(final List<Node<SudokuColumnValue, SudokuValue>> solution) {
    if (solution == null) {
      // TODO Exception? return empty array?
      System.err.println("No solution to convert!");
      return null;
    }
    final SudokuField field = new SudokuField(properties);
    for (Node<SudokuColumnValue, SudokuValue> node : solution) {
      //SudokuValue value = converter.convertRow(node);
      field.put(node.getValue());
    }
    /*int[][] field = new int[properties.getEdgeLength()][properties.getEdgeLength()];
    for (Node<SudokuColumnValue> node : solution) {
      SudokuValue value = converter.convertRow(node);
      field[value.getCoordinates().get(0)][value.getCoordinates().get(1)] = value.getNumber();
    }*/
    return field;
  }
}
