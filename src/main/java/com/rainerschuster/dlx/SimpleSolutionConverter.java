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

/**
 * This simple converter converts a list of nodes to a list a values.
 */
public class SimpleSolutionConverter<C, V extends Value<C>> {

  private ValueConverter<C, V> converter;

  public ValueConverter<C, V> getConverter() {
    return this.converter;
  }

  public void setConverter(ValueConverter<C, V> converter) {
    this.converter = converter;
  }

  public SimpleSolutionConverter(ValueConverter<C, V> converter) {
    this.converter = converter;
  }

  /**
   * Converts the solution to a list of values.
   *
   * @param solution
   *          A previously computed solution.
   * @return A list with each node converted to a value.
   */
  public List<V> convertSolution(List<Node<C, V>> solution) {
    if (solution == null) {
      // TODO Exception? return empty list?
      System.err.println("No solution to convert!");
      return null;
    }
    List<V> list = new ArrayList<V>(solution.size());
    for (Node<C, V> node : solution) {
      list.add(converter.convertRow(node));
    }
    return list;
  }

}
