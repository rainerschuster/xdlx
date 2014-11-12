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

/**
 * A helper class for implementers of the
 * {@link com.rainerschuster.sudoku.SourcesSudokuSolutionEvents} interface. This subclass of
 * {@link ArrayList} assumes that all objects added to it will be of type
 * {@link com.rainerschuster.sudoku.SudokuSolutionListener}.
 */
public class SudokuSolutionListenerCollection extends
    ArrayList<SudokuSolutionListener> {

  private static final long serialVersionUID = 1L;

  /**
   * Fires a solution event to all listeners.
   *
   * @param count
   *          The (consecutive) number of the solution.
   * @param level
   *          The level at which the solution was found.
   * @param field
   *          The solution itself.
   */
  public void fireSolution(long count, int level, SudokuField field) {
    for (SudokuSolutionListener listener : this) {
      listener.onSolution(count, level, field);
    }
  }

}
