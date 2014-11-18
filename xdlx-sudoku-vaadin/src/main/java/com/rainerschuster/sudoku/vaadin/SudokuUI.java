package com.rainerschuster.sudoku.vaadin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.rainerschuster.sudoku.Sudoku;
import com.rainerschuster.sudoku.SudokuField;
import com.rainerschuster.sudoku.SudokuProperties;
import com.rainerschuster.sudoku.SudokuSolutionListener;
import com.rainerschuster.sudoku.SudokuValue;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("mytheme")
@SuppressWarnings("serial")
public class SudokuUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SudokuUI.class, widgetset = "com.rainerschuster.sudoku.vaadin.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        layout.addComponent(initMenuBar());
        layout.addComponent(initSudokuField());
    }


    private static final long serialVersionUID = 1L;
    
    private VerticalLayout layout;

    private SudokuFieldVaadin sudokuField = null;

    private MenuBar menuBar = null;

    private MenuItem sudokuMenu = null;

    private MenuItem helpMenu = null;

    @SuppressWarnings("unused")
	private MenuItem aboutMenuItem = null;

    @SuppressWarnings("unused")
	private MenuItem classicSudokuPuzzleMenuItem = null;

    @SuppressWarnings("unused")
	private MenuItem colorSudokuPuzzleMenuItem = null;

    @SuppressWarnings("unused")
	private MenuItem xSudokuPuzzleMenuItem = null;

    @SuppressWarnings("unused")
	private MenuItem classicSudokuSolverMenuItem = null;

    @SuppressWarnings("unused")
	private MenuItem colorSudokuSolverMenuItem = null;

    @SuppressWarnings("unused")
	private MenuItem xSudokuSolverMenuItem = null;

    private MenuItem solveMenuItem = null;

    @SuppressWarnings("unused")
	private MenuItem clearMenuItem = null;

    /**
     * This method initializes menuBar.
     */
    private MenuBar initMenuBar() {
      if (menuBar == null) {
        menuBar = new MenuBar();
        sudokuMenu = menuBar.addItem ("Sudoku", null);
        classicSudokuPuzzleMenuItem = sudokuMenu.addItem("Classic Sudoku Puzzle", new MenuBar.Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				newStandardClassicSudoku(true);
			}
		});
        colorSudokuPuzzleMenuItem = sudokuMenu.addItem("Color Sudoku Puzzle", new MenuBar.Command() {
        	@Override
        	public void menuSelected(final MenuItem selectedItem) {
        		newStandardColorSudoku(true);
        	}
        });
        xSudokuPuzzleMenuItem = sudokuMenu.addItem("X Sudoku Puzzle", new MenuBar.Command() {
        	@Override
        	public void menuSelected(final MenuItem selectedItem) {
        		newStandardXSudoku(true);
        	}
        });
        sudokuMenu.addSeparator();
        classicSudokuSolverMenuItem = sudokuMenu.addItem("Classic Sudoku Solver", new MenuBar.Command() {
        	@Override
        	public void menuSelected(final MenuItem selectedItem) {
        		newStandardClassicSudoku(false);
        	}
        });
        colorSudokuSolverMenuItem = sudokuMenu.addItem("Color Sudoku Solver", new MenuBar.Command() {
        	@Override
        	public void menuSelected(final MenuItem selectedItem) {
        		newStandardColorSudoku(false);
        	}
        });
        xSudokuSolverMenuItem = sudokuMenu.addItem("X Sudoku Solver", new MenuBar.Command() {
        	@Override
        	public void menuSelected(final MenuItem selectedItem) {
        		newStandardXSudoku(false);
        	}
        });
        sudokuMenu.addSeparator();
        solveMenuItem = sudokuMenu.addItem("Solve", new MenuBar.Command() {
        	@Override
        	public void menuSelected(final MenuItem selectedItem) {
                // FIXME differentiate between generated and user-entered Sudoku!
                solve();
        	}
        });
        clearMenuItem = sudokuMenu.addItem("Clear", new MenuBar.Command() {
        	@Override
        	public void menuSelected(final MenuItem selectedItem) {
                clear();
        	}
        });

        helpMenu = menuBar.addItem("Help", null);
        aboutMenuItem = helpMenu.addItem("About", new MenuBar.Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				final Window aboutWindow = new Window("About");
				aboutWindow.setModal(true);
				aboutWindow.setResizable(false);
				final VerticalLayout aboutContent = new VerticalLayout();
				aboutContent.setMargin(true);
				aboutContent.setSpacing(true);
				aboutWindow.setContent(aboutContent);
				aboutContent.addComponent(new Label("&copy; 2007-2014 by Dipl.-Ing. Rainer Schuster", ContentMode.HTML));
				final Button okButton = new Button("OK");
				okButton.addClickListener(new ClickListener() {
					@Override
                    public void buttonClick(final ClickEvent event) {
						aboutWindow.close();
					}
				});
				aboutContent.addComponent(okButton);
				aboutContent.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
				aboutWindow.center();
				addWindow(aboutWindow);
			}
		});
      }
      return menuBar;
    }

    /**
     * This method initializes sudokuField.
     */
    private SudokuFieldVaadin initSudokuField() {
        if (sudokuField == null) {
            newStandardClassicSudoku(true);
        }
        return sudokuField;
    }

    private void newStandardClassicSudoku(final boolean generateGivens) {
        final SudokuProperties properties = new SudokuProperties();
        properties.setDimensions(2);
        properties.setNumbers(9);
        final List<Integer> dimension = new ArrayList<Integer>(2);
        dimension.add(3);
        dimension.add(3);
        properties.setRegion(properties.generateDefaultRegions(dimension));

        newSudoku(new Sudoku(properties), generateGivens);
    }

    private void newStandardColorSudoku(final boolean generateGivens) {
        final SudokuProperties properties = new SudokuProperties();
        properties.setDimensions(2);
        properties.setNumbers(9);
        final List<Integer> dimension = new ArrayList<Integer>(2);
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
        final List<Integer> dimension = new ArrayList<Integer>(2);
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
//      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      sudokuMenu.setEnabled(false);

      // TODO if no important properties have changed, empty() could be used (for performance)
      clear();

      if (sudokuField != null) {
    	  layout.removeComponent(sudokuField);
        sudokuField = null;
      }

      sudokuField = new SudokuFieldVaadin(sudoku.getProperties());

      SudokuField field = null;
      if (generateGivens) {
        field = sudoku.generate();
      } else {
        field = sudoku.generateField();
      }

      sudokuField.importData(field, false);

      layout.addComponent(initSudokuField()/*, BorderLayout.CENTER*/);

      sudokuMenu.setEnabled(true);
      /*TODO if (cheatMenuItem != null) {
        cheatMenuItem.setEnabled(true);
      }
      if (validateMenuItem != null) {
        validateMenuItem.setEnabled(true);
      }*/
      solveMenuItem.setEnabled(true);
      sudokuField.setEnabled(true);
//      getContentPane().validate();
//      setCursor(Cursor.getDefaultCursor());
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
            final long[] maxcount = new long[1]; // workaround to get maximum
                                                 // count
            sudoku.addSolutionListener(new SudokuSolutionListener() {

                @Override
                public void onSolution(final long count, final int level, final SudokuField field) {
                    // FIXME handle multi solution Sudokus!
                    solve(field);
                    maxcount[0] = count;
                    System.out.println(count);
                }

            });
            switch (sudoku.quickSolutions(givens)) {
            case 0:
                new Notification("There is no solution for your input!",
                        Notification.Type.WARNING_MESSAGE).show(Page.getCurrent());
                break;
            case 1:
                sudoku.solve(givens);
                break;
            case 2:
                new Notification("There are too many solutions for your input!",
                        Notification.Type.WARNING_MESSAGE).show(Page.getCurrent());
                break;
            default:
                new Notification("Invalid state (quickSolutions fault)!",
                        "Not unique solution",
                        Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
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
