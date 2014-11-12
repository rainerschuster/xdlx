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

import java.util.List;
import java.util.Vector;

/**
 * Implementations of this class convert a row of {@link DancingLinksData} back
 * to the original value type.
 *
 * @param <C>
 *          Column value
 * @param <V>
 *          Row value
 *
 * @deprecated Use <code>node.getValue</code> directly.
 */
@Deprecated
public abstract class ValueConverter<C, V extends Value<C>> {

  /**
   * Converts a row back to the original value type.
   *
   * @param node
   *          A node of the row.
   * @return A value that equals the original value (that formed that row).
   */
  public V convertRow(Node<C, V> node) {
    List<Column<C, V>> columns = new Vector<Column<C, V>>();
    for (Node<C, V> n : node) {
      columns.add(n.getColumn());
    }
    return convertRow(columns);
  };

  public abstract V convertRow(List<Column<C, V>> columns);

  // public abstract List<Column<C>> convertValue(V value);

}
