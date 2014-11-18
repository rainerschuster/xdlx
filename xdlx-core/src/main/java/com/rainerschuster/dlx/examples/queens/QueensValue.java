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
package com.rainerschuster.dlx.examples.queens;

import com.rainerschuster.dlx.Value;

public class QueensValue extends Value<QueensColumnValue> {

  private QueensProperties properties;

  private int row;

  private int column;

  public QueensValue(QueensProperties properties, int row, int column) {
    super();
    this.properties = properties;
    this.row = row;
    this.column = column;
  }

  public boolean isDiagonalA() {
    int t = row + column;
    return (t > 0) && (t < properties.getNn());
  }

  public boolean isDiagonalB() {
    int t = properties.getN() - 1 - row + column;
    return (t > 0) && (t < properties.getNn());
  }

  @Override
  public boolean inRelation(QueensColumnValue columnValue) {
    assert columnValue.size() == 1 : "invalid column value";
    for (QueensEnum qe : columnValue.keySet()) {
      switch (qe) {
      case ROW:
        return row == columnValue.get(qe);
      case COLUMN:
        return column == columnValue.get(qe);
      case DIAGONAL_A:
        return row + column == columnValue.get(qe);
      case DIAGONAL_B:
        return properties.getN() - 1 - row + column == columnValue.get(qe);
      default:
        return false;
      }
    }
    return false;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

}
