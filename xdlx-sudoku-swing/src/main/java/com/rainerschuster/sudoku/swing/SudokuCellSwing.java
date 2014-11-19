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

import java.awt.Color;

import javax.swing.SwingConstants;

import com.rainerschuster.sudoku.view.ViewProperties;

public class SudokuCellSwing extends JNumberField {

    private static final long serialVersionUID = 1L;

    //public final static CellBorder DEFAULT_BORDER = new CellBorder(Color.BLACK, 1, 3);

    public SudokuCellSwing() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(ViewProperties.defaultFont);
        setSize(ViewProperties.FIELD_SIZE, ViewProperties.FIELD_SIZE);
        setPreferredSize(getSize());
        //setBorder(DEFAULT_BORDER);
        setBorder(new CellBorder(Color.BLACK, ViewProperties.BORDER_THINNESS, ViewProperties.BORDER_THICKNESS));
    }

    // TODO sicherstellen, dass es auch eine Instanz von CellBorder ist!
    /* (non-Javadoc)
     * @see javax.swing.JComponent#getBorder()
     */
    @Override
    public CellBorder getBorder() {
        return (CellBorder) super.getBorder();
    }

}
