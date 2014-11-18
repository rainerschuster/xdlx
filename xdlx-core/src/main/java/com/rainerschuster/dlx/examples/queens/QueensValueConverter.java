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

import java.util.List;

import com.rainerschuster.dlx.Column;
import com.rainerschuster.dlx.ValueConverter;

/** @deprecated Superclass deprecated {@link com.rainerschuster.dlx.ValueConverter}. */
@Deprecated
public class QueensValueConverter extends
    ValueConverter<QueensColumnValue, QueensValue> {

  private QueensProperties properties;

  public QueensValueConverter(QueensProperties properties) {
    super();
    this.properties = properties;
  }

  @Override
  public QueensValue convertRow(List<Column<QueensColumnValue, QueensValue>> columns) {
    int row = 0;
    int col = 0;
    for (Column<QueensColumnValue, QueensValue> column : columns) {
      if (column.getValue().containsKey(QueensEnum.ROW)) {
        row = column.getValue().get(QueensEnum.ROW);
      }
      if (column.getValue().containsKey(QueensEnum.COLUMN)) {
        col = column.getValue().get(QueensEnum.COLUMN);
      }
    }
    return new QueensValue(properties, row, col);
  }

  public QueensProperties getProperties() {
    return properties;
  }

  public void setProperties(QueensProperties properties) {
    this.properties = properties;
  }

}
