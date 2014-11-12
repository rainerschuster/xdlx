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
package com.rainerschuster.dlx.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.io.*;
import java.util.List;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;

import java.awt.Font;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

import com.rainerschuster.dlx.Column;
import com.rainerschuster.dlx.DancingLinks;
import com.rainerschuster.dlx.DancingLinksData;
import com.rainerschuster.dlx.Node;
import com.rainerschuster.dlx.SolveListener;
import com.rainerschuster.dlx.Value;
import com.rainerschuster.dlx.examples.queens.QueensColumnValue;
import com.rainerschuster.dlx.examples.queens.QueensEnum;
import com.rainerschuster.dlx.examples.queens.QueensGenerator;
import com.rainerschuster.dlx.examples.queens.QueensProperties;
import com.rainerschuster.dlx.examples.queens.QueensSolutionConverter;
import com.rainerschuster.dlx.examples.queens.QueensValue;

/**
 * This class was intended to print the steps of the DLX-algorithm for the
 * 4 queens puzzle.
 */
public class SVGPrinter<C, V extends Value<C>> {

  public final static Color GREEN = new Color(0, 128, 0);

  public final static Color CHESS = new Color(210, 105, 30);

  private DancingLinksData<C, V> dlData;

  private boolean[][] previousMatrix;

  private boolean[] previousColumns;

  public SVGPrinter(DancingLinksData<C, V> dlData) {
    super();
    this.dlData = dlData;
  }

  /*public void print() throws IOException {
    print(getMatrix());
  }*/

  // TODO Print deleted links dotted (or other style with lots of white space)
  // TODO QUEST Print arrows at secondary columns?
	public void print(boolean[][] matrix, boolean[] columnArray) throws IOException {
        // Convert data structure to matrix
        /*List<C> list = new Vector<C>();
        for (Column<C, V> column : dlData) {
          list.add(column.getValue());
        }*/
        List<Column<C, V>> list = dlData.getAllColumns();

        // Create an SVG document
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        // Create a converter for this document
        SVGGraphics2D g = new SVGGraphics2D(doc);

        // Draw columns
        g.setPaint(GREEN);
        g.drawRoundRect(48, 48, 32, 48, 10, 10); // root
        g.setPaint(Color.BLACK);
        Font f = new Font("Dialog", Font.BOLD, 14);
        g.setFont(f);
        g.drawString("root", 48, 64);
        int cLastHorizontal = 80;
        for (int column = 0; column < list.size(); column++) {
          Column<C, V> myColumn = list.get(column);
          //Column<C, V> column : list
          int startHorizontal = (column + 2) * 48;
          // Print chess field
          g.setPaint(Color.BLACK);
          g.drawRect(startHorizontal, 0, 32, 32);
          g.setPaint(CHESS);
          g.fillRect(startHorizontal + 8, 0, 8, 8);
          g.fillRect(startHorizontal + 24, 0, 8, 8);
          g.fillRect(startHorizontal + 0, 0 + 8, 8, 8);
          g.fillRect(startHorizontal + 16, 0 + 8, 8, 8);
          g.fillRect(startHorizontal + 8, 0 + 16, 8, 8);
          g.fillRect(startHorizontal + 24, 0 + 16, 8, 8);
          g.fillRect(startHorizontal + 0, 0 + 24, 8, 8);
          g.fillRect(startHorizontal + 16, 0 + 24, 8, 8);
          g.setPaint(Color.BLACK);
          QueensColumnValue cv = (QueensColumnValue) myColumn.getValue();
          if (cv.containsKey(QueensEnum.ROW)) {
            int r = cv.get(QueensEnum.ROW);
            g.drawLine(startHorizontal, (r * 8) + 4, startHorizontal + 32, (r * 8) + 4);
          }
          if (cv.containsKey(QueensEnum.COLUMN)) {
            int c = cv.get(QueensEnum.COLUMN);
            g.drawLine(startHorizontal + (c * 8) + 4, 0, startHorizontal + (c * 8) + 4, 32);
          }
          if (cv.containsKey(QueensEnum.DIAGONAL_A)) {
            int a = cv.get(QueensEnum.DIAGONAL_A);
            int h1 = 0, v1 = 0,  h2 = 0, v2 = 0;
            switch (a) {
            case 1:
              h1 = 0; v1 = 16; h2 = 16; v2 = 0;
              break;
            case 2:
              h1 = 0; v1 = 24; h2 = 24; v2 = 0;
              break;
            case 3:
              h1 = 0; v1 = 32; h2 = 32; v2 = 0;
              break;
            case 4:
              h1 = 8; v1 = 32; h2 = 32; v2 = 8;
              break;
            case 5:
              h1 = 16; v1 = 32; h2 = 32; v2 = 16;
              break;
            default:
              break;
            }
            g.drawLine(startHorizontal + h1, v1, startHorizontal + h2, v2);
          }
          if (cv.containsKey(QueensEnum.DIAGONAL_B)) {
            int b = cv.get(QueensEnum.DIAGONAL_B);
            int h1 = 0, v1 = 0,  h2 = 0, v2 = 0;
            switch (b) {
            case 1:
              h1 = 0; v1 = 16; h2 = 16; v2 = 32;
              break;
            case 2:
              h1 = 0; v1 = 8; h2 = 24; v2 = 32;
              break;
            case 3:
              h1 = 0; v1 = 0; h2 = 32; v2 = 32;
              break;
            case 4:
              h1 = 8; v1 = 0; h2 = 32; v2 = 24;
              break;
            case 5:
              h1 = 16; v1 = 0; h2 = 32; v2 = 16;
              break;
            default:
              break;
            }
            g.drawLine(startHorizontal + h1, v1, startHorizontal + h2, v2);
          }
          // Print column
          // TODO QUEST Print arrows above column-box?
          if (columnArray[column] || (previousColumns == null || previousColumns[column])) {
            if (previousColumns == null || previousColumns[column]) {
              g.setPaint(columnArray[column] ? GREEN : Color.RED);
            } else {
              g.setPaint(Color.GREEN);
            }
            g.drawRoundRect(startHorizontal, 48, 32, 48, 10, 10);
            if (myColumn.isPrimary()) {
              //g.setPaint((myColumn.getPrev().getNext() == myColumn) ? Color.BLUE : Color.RED);
              g.setPaint(Color.BLUE);
              g.drawLine(cLastHorizontal - 8, 48 + 8, startHorizontal, 48 + 8);
              g.fillPolygon(new int[]{startHorizontal - 8, startHorizontal - 8, startHorizontal}, new int[]{4 + 48, 12 + 48, 8 + 48}, 3);
              //g.setPaint((myColumn.getNext().getPrev() == myColumn) ? Color.BLUE : Color.RED);
              g.setPaint(Color.BLUE);
              g.drawLine(cLastHorizontal, 48 + 40, startHorizontal + 8, 48 + 40);
              g.fillPolygon(new int[]{cLastHorizontal + 8, cLastHorizontal + 8, cLastHorizontal}, new int[]{36 + 48, 44 + 48, 40 + 48}, 3);

              cLastHorizontal = startHorizontal + 32;
            }
            g.setPaint(Color.BLUE);
            g.fillPolygon(new int[]{startHorizontal + 4, startHorizontal + 12, startHorizontal + 8}, new int[]{96 + 8, 96 + 8, 96}, 3);

            g.setPaint(Color.BLACK);
            // Draw column value
            g.drawString(myColumn.getValue().toString(), startHorizontal + 6, 48 + 16);
            // Draw node count (of the column)
            g.drawString(Integer.toString(myColumn.getLength()), startHorizontal + 12, 48 + 40);
          }
        }
        // TODO draw first an last links dotted (or similar)
        // Draw nodes and horizontal lines
        for (int row = 0; row < matrix.length; row++) {
        boolean[] myrow = matrix[row];
        int startVertical = ((row + 2) * 48) + 16;
        int lastHorizontal = 48;
        QueensValue value = (QueensValue) dlData.getGeneratedValues().get(row);
        // Print chess field
        g.setPaint(Color.BLACK);
        g.drawRect(0, startVertical, 32, 32);
        g.setPaint(CHESS);
        g.fillRect(8, startVertical, 8, 8);
        g.fillRect(24, startVertical, 8, 8);
        g.fillRect(0, startVertical + 8, 8, 8);
        g.fillRect(16, startVertical + 8, 8, 8);
        g.fillRect(8, startVertical + 16, 8, 8);
        g.fillRect(24, startVertical + 16, 8, 8);
        g.fillRect(0, startVertical + 24, 8, 8);
        g.fillRect(16, startVertical + 24, 8, 8);
        g.setPaint(Color.BLACK);
        int qr = value.getRow();
        int qc = value.getColumn();
        g.fillOval(qc * 8, startVertical + (qr * 8), 8, 8);
        boolean found = false;
        boolean empty = true;
        for (int column = 0; column < myrow.length; column++) {
        if (myrow[column]) {
          empty = false;
          int startHorizontal = (column + 2) * 48;
          // Draw node
          if (previousMatrix == null || previousMatrix[row][column]) {
            g.setPaint(GREEN);
          } else {
            // Recovered node
            g.setPaint(Color.GREEN);
          }
          g.fillRoundRect(startHorizontal, startVertical, 32, 32, 8, 8);
          // Draw arrows
          g.setPaint(Color.BLUE);
          g.drawLine(lastHorizontal, startVertical + 8, startHorizontal, startVertical + 8);
          g.fillPolygon(new int[]{startHorizontal - 8, startHorizontal - 8, startHorizontal}, new int[]{startVertical + 4, startVertical + 12, startVertical + 8}, 3);
          g.drawLine(lastHorizontal + (found ? 8 : 0), startVertical + 24, startHorizontal + 8, startVertical + 24);
          g.fillPolygon(new int[]{startHorizontal + 40, startHorizontal + 40, startHorizontal + 32}, new int[]{startVertical + 20, startVertical + 28, startVertical + 24}, 3);

          lastHorizontal = startHorizontal + 24;
          found = true;
        } else {
          // TODO Visualize deleted links
          if (previousMatrix != null && previousMatrix[row][column]) {
        	empty = false;
        	// Node was deleted
            int startHorizontal = (column + 2) * 48;
            // Draw deleted node
            g.setPaint(Color.RED);
            g.fillRoundRect(startHorizontal, startVertical, 32, 32, 8, 8);
          }
        }
      }
      if (!empty) {
	    g.setPaint(Color.BLUE);
	    g.drawLine(lastHorizontal, startVertical + 8, ((list.size() + 2) * 48) + 32, startVertical + 8);
	    g.drawLine(lastHorizontal + 8, startVertical + 24, ((list.size() + 2) * 48) + 32, startVertical + 24);
      }
    }
    // Draw vertical lines
    g.setPaint(Color.BLUE);
    for (int column = 0; column < matrix[0].length; column++) {
      if (columnArray[column] || (previousColumns == null || previousColumns[column])) {
	  int startHorizontal = (column + 2) * 48;
      int lastVertical = 40 + 48;
      for (int row = 0; row < matrix.length; row++) {
        boolean[] myrow = matrix[row];
        //int startVertical = (row * 48) + 64;
        //int lastHorizontal = 0;
        int startVertical = ((row + 2) * 48) + 16;

        if (myrow[column]) {
          g.drawLine(startHorizontal + 8, lastVertical + 8, startHorizontal + 8, startVertical + 8);
          g.fillPolygon(new int[]{startHorizontal + 4, startHorizontal + 12, startHorizontal + 8}, new int[]{startVertical + 40, startVertical + 40, startVertical + 32}, 3);
          g.drawLine(startHorizontal + 24, lastVertical, startHorizontal + 24, startVertical);
          g.fillPolygon(new int[]{startHorizontal + 20, startHorizontal + 28, startHorizontal + 24}, new int[]{startVertical - 8, startVertical -8, startVertical}, 3);
          lastVertical = startVertical + 24;
        }
      }
      g.drawLine(startHorizontal + 8, lastVertical+8, startHorizontal + 8, (matrix.length + 3) * 48);
      g.drawLine(startHorizontal + 24, lastVertical, startHorizontal + 24, (matrix.length + 3) * 48);
    }
    }

    g.setSVGCanvasSize(new Dimension(1000, 1000));

    // Finally, stream out SVG to the standard output using UTF-8 encoding
    boolean useCSS = true; // we want to use CSS style attributes
    //Writer out = new OutputStreamWriter(System.out, "UTF-8");
    Writer out = new FileWriter("graph" + (count < 10 ? "0" : "") + count + ".svg");
    g.stream(out, useCSS);

    /*// Populate the document root with the generated SVG content
    Element root = doc.getDocumentElement();
    g.getRoot(root);

    // Display the document
    JSVGCanvas canvas = new JSVGCanvas();
    JFrame f = new JFrame();
    f.getContentPane().add(canvas);
    canvas.setSVGDocument(doc);
    f.pack();
    f.setVisible(true);*/
  }

  static int count = 0;

  /**
   * @param args
   */
  public static void main(String[] args) {
    final QueensProperties properties = new QueensProperties(4);

    QueensGenerator generator = new QueensGenerator(properties);

    final DancingLinksData<QueensColumnValue, QueensValue> dlData = generator.generate();

    DancingLinks<QueensColumnValue, QueensValue> dl = new DancingLinks<QueensColumnValue, QueensValue>(dlData);
    // dl.setVerbosity(4);
    //QueensValueConverter valueConver = new QueensValueConverter(properties);
    final QueensSolutionConverter solutionConverter = new QueensSolutionConverter(properties);
    /*dl.addSolutionListener(new SolutionListener<QueensColumnValue, QueensValue>() {

          public void onSolution(int count, int level, List<Node<EnumMap<QueensEnum, Integer>>> solution) {
            boolean[][] field = solutionConverter.convertSolution(solution);
            // Print field
            System.out.println("SOLUTION " + count + " (level " + level + ")");
            System.out.print("--");
            for (int i = 0; i < properties.getN() - 1; i++) {
              System.out.print("--+-");
            }
            System.out.println("---");
            for (boolean[] row : field) {
              for (boolean cell : row) {
                System.out.print("| ");
                System.out.print(cell ? "X" : " ");
                System.out.print(" ");
              }
              System.out.println("|");
              System.out.print("--");
              for (int i = 0; i < properties.getN() - 1; i++) {
                System.out.print("--+-");
              }
              System.out.println("---");
            }
            System.out.println();
          }

    });*/
    final SVGPrinter<QueensColumnValue, QueensValue> svg = new SVGPrinter<QueensColumnValue, QueensValue>(dlData);
    dl.addSolveListener(new SolveListener() {

      @Override
	public void onSolution(int level) {
        try {
          count++;
          boolean[][] matrix = svg.getMatrix();

          List<Column<QueensColumnValue, QueensValue>> allColumns = dlData.getAllColumns();
          boolean[] columnArray = new boolean[allColumns.size()];

          for (int i = 0; i < allColumns.size(); i++) {
            columnArray[i] = !allColumns.get(i).isCovered();
          }

          svg.print(matrix, columnArray);
          svg.setPreviousMatrix(matrix);
          svg.setPreviousColumns(columnArray);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

    });
    count = 0;
    dl.solve();

    //dl.printStatistics();
  }

  /**
   * Transforms the data structure to a two-dimensional (boolean) matrix.
   * NOTE Remind setting the previous matrix!
   */
  public boolean[][] getMatrix() {
    List<Column<C, V>> list = dlData.getAllColumns();
    boolean[][] matrix = new boolean[dlData.getGeneratedValues().size()][list.size()];
    dlData.resetMarks();
    for (Column<C, V> column : dlData) {
      for (Node<C, V> row : column) {
        if (!row.isMarked()) {
          boolean[] myrow = new boolean[list.size()];
          for (Node<C, V> node : row) {
            int index = list.indexOf(node.getColumn());
            myrow[index] = true;
            node.setMarked(true);
          }
          // int index = previousMatrix.indexOf(myrow);
          // int index = find(previousMatrix, myrow);
          int index = dlData.getGeneratedValues().indexOf(row.getValue());
          matrix[index] = myrow;
          System.out.println(index);
          /*for (boolean bb : myrow) {
            System.out.print(bb ? 1 : 0);
          }*/

          /*// Fill with empty rows (if rows were deleted)
          for (int i = matrix.size(); i < index; i++) {
            matrix.add(new boolean[list.size()]);
          }
          matrix.add(myrow);*/
        }
      }
    }
    /*// Fill with empty rows (if rows were deleted at the end)
    if (previousMatrix != null) {
      for (int i = matrix.size(); i < previousMatrix.size(); i++) {
        matrix.add(new boolean[list.size()]);
      }
    }*/
    System.out.println("---");
    return matrix;
  }

  /** @deprecated Now we can use <code>indexOf</code> from the value list. */
  @Deprecated
  private int find(List<boolean[]> list, boolean[] element) {
    if (list != null) {
      for (int i = 0; i < list.size(); i++) {
        boolean[] listItem = list.get(i);
        if (listItem.length == element.length) {
          boolean found = true;
          for (int j = 0; j < listItem.length; j++) {
            if (listItem[j] != element[j]) {
              found = false;
              break;
            }
          }
          if (found) {
            return i;
          }
        }
      }
    }
    return -1;
  }

  public DancingLinksData<C, V> getDancingLinksData() {
    return dlData;
  }

  public void setDancingLinksData(DancingLinksData<C, V> dlData) {
    this.dlData = dlData;
  }

  public boolean[][] getPreviousMatrix() {
    return previousMatrix;
  }

  public void setPreviousMatrix(boolean[][] previousMatrix) {
    this.previousMatrix = previousMatrix;
  }

  public boolean[] getPreviousColumns() {
    return previousColumns;
  }

  public void setPreviousColumns(boolean[] previousColumns) {
    this.previousColumns = previousColumns;
  }

}
