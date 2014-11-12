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
package com.rainerschuster.dlx;

import java.util.ArrayList;
import java.util.List;

public abstract class Value<C> {

  public abstract boolean inRelation(C columnValue);

  /**
   * This method can be overridden to determine the color(ing) of a relation.
   * Per default every relation has the coloring 0.
   *
   * @param columnValue
   *          the column to which the relation should be determined.
   * @return The coloring of the relation to the column.
   */
  public int relationColor(C columnValue) {
    return 0;
  }

  /**
   * @param columns
   *          List of columns that should be tested on relation.
   * @return List of columns that are in relation to this value.
   */
  public <V extends Value<C>> List<Column<C, V>> inRelations(List<Column<C, V>> columns) {
    // TODO QUEST use DancingLinksData as parameter (instead of column list)?
    List<Column<C, V>> list = new ArrayList<Column<C, V>>();
    for (Column<C, V> column : columns) {
      if (inRelation(column.getValue())) {
        list.add(column);
      }
    }
    return list;
  }

  /**
   * @param dlData
   *          The <code>DancingLinksData</code> object that holds the data
   *          (this value is part of it).
   * @return List of columns that are in relation to this value, even if they
   *         are already covered!
   */
  /*public List<Column<C, V>> inRelations(DancingLinksData<C, Value<C>> dlData) {
    return inRelations(dlData.getAllColumns());
  }*/

}
