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
package com.rainerschuster.sudoku.swing;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.rainerschuster.sudoku.Sudoku;
import com.rainerschuster.sudoku.SudokuField;
import com.rainerschuster.sudoku.SudokuProperties;
import com.rainerschuster.sudoku.SudokuValue;

// TODO Currently there is a lot of copy&paste in here => cleanup
public class SudokuApplet extends JApplet {

    private static final long serialVersionUID = 1L;

    private SudokuFieldSwing sudokuField = null;

    private JPanel jContentPane = null;

    private JMenuBar menuBar = null;

    private JMenu sudokuMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem classicSudokuPuzzleMenuItem = null;

    private JMenuItem colorSudokuPuzzleMenuItem = null;

    private JMenuItem xSudokuPuzzleMenuItem = null;

    private JMenuItem classicSudokuSolverMenuItem = null;

    private JMenuItem colorSudokuSolverMenuItem = null;

    private JMenuItem xSudokuSolverMenuItem = null;

    private JMenuItem solveMenuItem = null;

    private JMenuItem clearMenuItem = null;

    /**
     * This is the default constructor.
     */
    public SudokuApplet() {
        super();
    }

    /**
     * This method initializes this.
     */
    @Override
    public void init() {
        this.setJMenuBar(getMenuBar());
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setSize(sudokuField.getPreferredSize().width, sudokuField.getPreferredSize().height + 36);
        // System.out.println(this.getSize());
    }

    /**
     * This method initializes jContentPane.
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getSudokuField(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes menuBar.
     *
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            menuBar.add(getSudokuMenu());
            menuBar.add(getHelpMenu());
        }
        return menuBar;
    }

    /**
     * This method initializes sudokuMenu.
     *
     * @return javax.swing.JMenu
     */
    private JMenu getSudokuMenu() {
        if (sudokuMenu == null) {
            sudokuMenu = new JMenu();
            sudokuMenu.setText("Sudoku");
            sudokuMenu.add(getClassicSudokuPuzzleMenuItem());
            sudokuMenu.add(getColorSudokuPuzzleMenuItem());
            sudokuMenu.add(getXSudokuPuzzleMenuItem());
            sudokuMenu.addSeparator();
            sudokuMenu.add(getClassicSudokuSolverMenuItem());
            sudokuMenu.add(getColorSudokuSolverMenuItem());
            sudokuMenu.add(getXSudokuSolverMenuItem());
            sudokuMenu.addSeparator();
            sudokuMenu.add(getSolveMenuItem());
            sudokuMenu.add(getClearMenuItem());
        }
        return sudokuMenu;
    }

    /**
     * This method initializes helpMenu.
     *
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText("Help");
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    /**
     * This method initializes aboutMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText("About");
        }
        return aboutMenuItem;
    }

    /**
     * This method initializes classicSudokuPuzzleMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getClassicSudokuPuzzleMenuItem() {
        if (classicSudokuPuzzleMenuItem == null) {
            classicSudokuPuzzleMenuItem = new JMenuItem();
            classicSudokuPuzzleMenuItem.setText("Classic Sudoku Puzzle");
            classicSudokuPuzzleMenuItem.addActionListener(e -> newStandardClassicSudoku(true));
        }
        return classicSudokuPuzzleMenuItem;
    }

    /**
     * This method initializes colorSudokuPuzzleMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getColorSudokuPuzzleMenuItem() {
        if (colorSudokuPuzzleMenuItem == null) {
            colorSudokuPuzzleMenuItem = new JMenuItem();
            colorSudokuPuzzleMenuItem.setText("Color Sudoku Puzzle");
            colorSudokuPuzzleMenuItem.addActionListener(e -> newStandardColorSudoku(true));
        }
        return colorSudokuPuzzleMenuItem;
    }

    /**
     * This method initializes xSudokuPuzzleMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getXSudokuPuzzleMenuItem() {
        if (xSudokuPuzzleMenuItem == null) {
            xSudokuPuzzleMenuItem = new JMenuItem();
            xSudokuPuzzleMenuItem.setText("X Sudoku Puzzle");
            xSudokuPuzzleMenuItem.addActionListener(e -> newStandardXSudoku(true));
        }
        return xSudokuPuzzleMenuItem;
    }

    /**
     * This method initializes classicSudokuSolverMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getClassicSudokuSolverMenuItem() {
        if (classicSudokuSolverMenuItem == null) {
            classicSudokuSolverMenuItem = new JMenuItem();
            classicSudokuSolverMenuItem.setText("Classic Sudoku Solver");
            classicSudokuSolverMenuItem.addActionListener(e -> newStandardClassicSudoku(false));
        }
        return classicSudokuSolverMenuItem;
    }

    /**
     * This method initializes colorSudokuSolverMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getColorSudokuSolverMenuItem() {
        if (colorSudokuSolverMenuItem == null) {
            colorSudokuSolverMenuItem = new JMenuItem();
            colorSudokuSolverMenuItem.setText("Color Sudoku Solver");
            colorSudokuSolverMenuItem.addActionListener(e -> newStandardColorSudoku(false));
        }
        return colorSudokuSolverMenuItem;
    }

    /**
     * This method initializes xSudokuSolverMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getXSudokuSolverMenuItem() {
        if (xSudokuSolverMenuItem == null) {
            xSudokuSolverMenuItem = new JMenuItem();
            xSudokuSolverMenuItem.setText("X Sudoku Solver");
            xSudokuSolverMenuItem.addActionListener(e -> newStandardXSudoku(false));
        }
        return xSudokuSolverMenuItem;
    }

    /**
     * This method initializes solveMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSolveMenuItem() {
        if (solveMenuItem == null) {
            solveMenuItem = new JMenuItem();
            solveMenuItem.setText("Solve");
            solveMenuItem.addActionListener(e -> solve());
        }
        return solveMenuItem;
    }

    /**
     * This method initializes clearMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getClearMenuItem() {
        if (clearMenuItem == null) {
            clearMenuItem = new JMenuItem();
            clearMenuItem.setText("Clear");
            clearMenuItem.addActionListener(e -> clear());
        }
        return clearMenuItem;
    }

    /**
     * This method initializes sudokuField.
     *
     * @return sudoku.view.SudokuFieldSwing
     */
    private SudokuFieldSwing getSudokuField() {
        if (sudokuField == null) {
            newStandardClassicSudoku(true);
        }
        return sudokuField;
    }

    private void newStandardClassicSudoku(final boolean generateGivens) {
        final SudokuProperties properties = new SudokuProperties();
        properties.setDimensions(2);
        properties.setNumbers(9);
        final List<Integer> dimension = new ArrayList<>(2);
        dimension.add(3);
        dimension.add(3);
        properties.setRegion(properties.generateDefaultRegions(dimension));

        newSudoku(new Sudoku(properties), generateGivens);
    }

    private void newStandardColorSudoku(final boolean generateGivens) {
        final SudokuProperties properties = new SudokuProperties();
        properties.setDimensions(2);
        properties.setNumbers(9);
        final List<Integer> dimension = new ArrayList<>(2);
        dimension.add(3);
        dimension.add(3);
        properties.setRegion(properties.generateDefaultRegions(dimension));
        properties.setColor(properties.generateDefaultColors(dimension));

        newSudoku(new Sudoku(properties), generateGivens);
    }

    private void newStandardXSudoku(final boolean generateGivens) {
        final SudokuProperties properties = new SudokuProperties();
        properties.setDimensions(2);
        properties.setNumbers(9);
        final List<Integer> dimension = new ArrayList<>(2);
        dimension.add(3);
        dimension.add(3);
        properties.setRegion(properties.generateDefaultRegions(dimension));
        properties.setXSudoku(true);

        newSudoku(new Sudoku(properties), generateGivens);
    }

    /**
     * Generates the new Sudoku.
     */
    private void newSudoku(final Sudoku sudoku, final boolean generateGivens) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        sudokuMenu.setEnabled(false);

        // TODO if no important properties have changed, empty() could be used (for performance)
        clear();

        if (sudokuField != null) {
            jContentPane.remove(sudokuField);
            sudokuField = null;
        }

        sudokuField = new SudokuFieldSwing(sudoku.getProperties());

        SudokuField field = null;
        if (generateGivens) {
            field = sudoku.generate();
        } else {
            field = sudoku.generateField();
        }

        sudokuField.importData(field, false);

        jContentPane.add(getSudokuField(), BorderLayout.CENTER);

        sudokuMenu.setEnabled(true);
        /*TODO if (cheatMenuItem != null) {
            cheatMenuItem.setEnabled(true);
        }
        if (validateMenuItem != null) {
            validateMenuItem.setEnabled(true);
        }*/
        solveMenuItem.setEnabled(true);
        sudokuField.setEnabled(true);
        getContentPane().validate();
        setCursor(Cursor.getDefaultCursor());
    }

    private void clear() {
        /*if (cheatMenuItem != null) {
            cheatMenuItem.setEnabled(false);
        }
        if (validateMenuItem != null) {
            validateMenuItem.setEnabled(false);
        }
        solveMenuItem.setEnabled(true);*/
        if (sudokuField != null) {
            sudokuField.clear();
        }
        /*if (sudokuField != null) {
            jContentPane.remove(sudokuField);
        }
        sudokuField = null;
        jContentPane.revalidate();*/
    }

    @SuppressWarnings("unused")
    private void empty() {
        // TODO implement!
        // sudokuField.empty();
    }

    @SuppressWarnings("unused")
    private void cheat() {
        // TODO implement!
    }

    /**
     * Solves and displays a Sudoku entered by the user.
     */
    private void solve() {
        if (sudokuField != null) {
            final List<SudokuValue> givens = sudokuField.exportValues();
            final Sudoku sudoku = new Sudoku(sudokuField.getProperties());
            final long[] maxcount = new long[1]; // workaround to get maximum count
            sudoku.addSolutionListener((count, level, field) -> {
			    // FIXME handle multi solution Sudokus!
			    solve(field);
			    maxcount[0] = count;
			    System.out.println(count);
			});
            switch (sudoku.quickSolutions(givens)) {
            case 0:
                JOptionPane.showMessageDialog(this, "There is no solution for your input!");
                break;
            case 1:
                sudoku.solve(givens);
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "There are too many solutions for your input!");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid state (quickSolutions fault)!",
                        "Not unique solution", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }

    /**
     * Displays the solution of a generated Sudoku.
     */
    private void solve(final SudokuField solution) {
        sudokuField.importData(solution, true);
    }

}
