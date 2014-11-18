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
import java.util.ArrayList;

import com.rainerschuster.dlx.DancingLinks;
import com.rainerschuster.dlx.DancingLinksData;
import com.rainerschuster.dlx.Node;
import com.rainerschuster.dlx.SolutionListener;

public class Sudoku implements SourcesSudokuSolutionEvents {

  private SudokuProperties properties = new SudokuProperties();

  private SudokuSolutionListenerCollection sudokuSolutionListeners = new SudokuSolutionListenerCollection();

  public Sudoku(SudokuProperties properties) {
    super();
    this.properties = properties;
  }

  // TODO QUEST what about givens?
  public SudokuField generateField() {
    SudokuField field = new SudokuField(properties);
    /*Map<List<Integer>, Integer> values = new HashMap<List<Integer>, Integer>();
    for (int row = 0; row < properties.getEdgeLength(); row++) {
      for (int col = 0; col < properties.getEdgeLength(); col++) {

      }
    }*/
    //field.setField(new HashMap<List<Integer>, Integer>());
    field.setGivens(new ArrayList<SudokuValue>());
    return field;
  }

  /** @return 0 if no solution, 1 if unique solution and 2 if two or more solutions */
  public int quickSolutions(final List<SudokuValue> givens) {
    SudokuGenerator generator = new SudokuGenerator(properties);
    DancingLinksData<SudokuColumnValue, SudokuValue> dlData = generator.generate();
    DancingLinks<SudokuColumnValue, SudokuValue> dl = new DancingLinks<SudokuColumnValue, SudokuValue>(dlData);
    for (SudokuValue given : givens) {
      dl.cover(given);
    }
    return (int) dl.quickSolutions();
  }

  /** Solves a Sudoku with its givens. */
  public void solve(final List<SudokuValue> givens) {
    SudokuGenerator generator = new SudokuGenerator(properties);
    DancingLinksData<SudokuColumnValue, SudokuValue> dlData = generator.generate();
    DancingLinks<SudokuColumnValue, SudokuValue> dl = new DancingLinks<SudokuColumnValue, SudokuValue>(dlData);
    // dl.setVerbosity(1);
    //SudokuValueConverter valueConverter = new SudokuValueConverter(properties);
    final SudokuSolutionConverter solutionConverter = new SudokuSolutionConverter(properties);

    dl.addSolutionListener(new SolutionListener<SudokuColumnValue, SudokuValue>() {
      @Override
	public void onSolution(long count, int level, List<Node<SudokuColumnValue, SudokuValue>> solution) {
        SudokuField field = solutionConverter.convertSolution(solution);
        /* Givens must be added because they are not contained in the chosens of
         * DancingLinksData */
        for (SudokuValue given : givens) {
          field.getField().put(given.getCoordinates(), given.getNumber());
        }
        field.setGivens(givens);
        sudokuSolutionListeners.fireSolution(count, level, field);
      }
    });
    for (SudokuValue given : givens) {
      dl.cover(given);
    }
    dl.solve();
    // uncover isn't necessary because data structure isn't needed any more
  }

  /**
   * @return A new (random) <code>SudokuField</code> including the givens (and
   *         even the solution, which is a byproduct of the given-generation).
   */
  public SudokuField generate() {
    SudokuGenerator generator = new SudokuGenerator(properties);
    DancingLinksData<SudokuColumnValue, SudokuValue> dlData = generator.generate();
    DancingLinks<SudokuColumnValue, SudokuValue> dl = new DancingLinks<SudokuColumnValue, SudokuValue>(dlData);
    // dl.setVerbosity(1);
    //SudokuValueConverter valueConverter = new SudokuValueConverter(properties);
    final SudokuSolutionConverter solutionConverter = new SudokuSolutionConverter(properties);

    List<Node<SudokuColumnValue, SudokuValue>> solution = dl.getFirstSolution();
    SudokuField myField = solutionConverter.convertSolution(solution);
    List<Node<SudokuColumnValue, SudokuValue>> givenSolution = dl.reduce();
    for (Node<SudokuColumnValue, SudokuValue> n : givenSolution) {
      myField.getGivens().add(n.getValue());
    }

    return myField;
  }



  /**
   * @param args
   */
  public static void main(String[] args) {
    final SudokuProperties properties = new SudokuProperties();

    properties.setDimensions(2);
    properties.setNumbers(9);
    List<Integer> regionDimension = new ArrayList<Integer>(2);
    regionDimension.add(3);
    regionDimension.add(3);
    properties.setRegion(properties.generateDefaultRegions(regionDimension));

    Map<List<Integer>, Integer> region = properties.getRegion();
    System.out.println(region.toString());

    SudokuGenerator generator = new SudokuGenerator(properties);
    DancingLinksData<SudokuColumnValue, SudokuValue> dlData = generator.generate();
    DancingLinks<SudokuColumnValue, SudokuValue> dl = new DancingLinks<SudokuColumnValue, SudokuValue>(dlData);
    // dl.setVerbosity(1);
    //SudokuValueConverter valueConverter = new SudokuValueConverter(properties);
    final SudokuSolutionConverter solutionConverter = new SudokuSolutionConverter(properties);

     /*dl.addSolutionListener(new SolutionListener<SudokuColumnValue>() {

      public void onSolution(int count, int level,
          List<Node<SudokuColumnValue, SudokuValue>> solution) {
      }
    });
    // dl.solve();*/

    List<Node<SudokuColumnValue, SudokuValue>> solution = dl.getFirstSolution();
    SudokuField myField = solutionConverter.convertSolution(solution);
    int[][] field = new int[properties.getEdgeLength()][properties.getEdgeLength()];
    for (List<Integer> coord : myField.getField().keySet()) {
      field[coord.get(0)][coord.get(1)] = myField.getField().get(coord);
    }
    // Print field
    // System.out.println("SOLUTION " + count + " (level " + level + ")");
    System.out.print("--");
    for (int i = 0; i < properties.getEdgeLength() - 1; i++) {
      System.out.print("--+-");
    }
    System.out.println("---");
    for (int[] row : field) {
      for (int cell : row) {
        System.out.print("| ");
        System.out.print(cell > 0 ? cell : " ");
        System.out.print(" ");
      }
      System.out.println("|");
      System.out.print("--");
      for (int i = 0; i < properties.getEdgeLength() - 1; i++) {
        System.out.print("--+-");
      }
      System.out.println("---");
    }
    System.out.println();
    System.out.println("----------------");
    System.out.println("GOT FIRST SOLUTION");
    System.out.println("----------------");

    /*for (Node<SudokuColumnValue, SudokuValue> nl : solution) {
      System.out.println("VALUE: " + nl.getColumn().getValue());
      for (Node<SudokuColumnValue, SudokuValue> nlx : nl) {
        System.out.println(nlx.getColumn().getValue());
      }
    }*/

    List<Node<SudokuColumnValue, SudokuValue>> givenSolution = dl.reduce();
    for (Node<SudokuColumnValue, SudokuValue> n : givenSolution) {
      //myField.getGivens().add(valueConverter.convertRow(n));
      myField.getGivens().add(n.getValue());
    }

    dl.printStatistics();
  }

  public SudokuProperties getProperties() {
    return properties;
  }

  public void setProperties(SudokuProperties properties) {
    this.properties = properties;
  }

  @Override
public void addSolutionListener(SudokuSolutionListener listener) {
    sudokuSolutionListeners.add(listener);
  }

  @Override
public void removeSolutionListener(SudokuSolutionListener listener) {
    sudokuSolutionListeners.remove(listener);
  }

}
