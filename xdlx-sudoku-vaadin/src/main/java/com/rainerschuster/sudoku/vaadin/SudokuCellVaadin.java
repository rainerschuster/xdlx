package com.rainerschuster.sudoku.vaadin;

import com.rainerschuster.sudoku.view.ViewProperties;

public class SudokuCellVaadin extends NumberField {

	private static final long serialVersionUID = 1L;

	public SudokuCellVaadin() {
		super();
		addStyleName("numerical");
//		setHorizontalAlignment(SwingConstants.CENTER);
		setWidth(ViewProperties.FIELD_SIZE, Unit.PIXELS);
		setHeight(ViewProperties.FIELD_SIZE, Unit.PIXELS);
//		setFont(ViewProperties.defaultFont);
//		setBorder(new CellBorder(Color.BLACK, ViewProperties.BORDER_THINNESS, ViewProperties.BORDER_THICKNESS));
	}

}
