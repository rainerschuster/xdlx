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
import java.util.List;
import java.util.Map;

import com.rainerschuster.dlx.Column;
import com.rainerschuster.dlx.ValueConverter;

/** @deprecated Superclass deprecated {@link com.rainerschuster.dlx.ValueConverter}. */
@Deprecated
public class SudokuValueConverter extends
    ValueConverter<SudokuColumnValue, SudokuValue> {

  private SudokuProperties properties;

  public SudokuValueConverter(SudokuProperties properties) {
    super();
    this.properties = properties;
  }

  @Override
  public SudokuValue convertRow(List<Column<SudokuColumnValue, SudokuValue>> columns) {
    int x1 = 0, x2 = 0;
    int n = 0;
    for (Column<SudokuColumnValue, SudokuValue> colum : columns) {
      Map<String, Integer> values = colum.getValue().getValues();
      if (values.containsKey("x1")) {
        x1 = values.get("x1");
      }
      if (values.containsKey("x2")) {
        x2 = values.get("x2");
      }
      if (values.containsKey("n")) {
        n = values.get("n");
      }
    }
    List<Integer> coordinates = new ArrayList<Integer>(2);
    coordinates.add(x1);
    coordinates.add(x2);
    SudokuValue value = new SudokuValue(properties, n);
    value.setCoordinates(coordinates);
    return value;
  }

  public SudokuProperties getProperties() {
    return properties;
  }

  public void setProperties(SudokuProperties properties) {
    this.properties = properties;
  }

}
