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
package com.rainerschuster.sudoku.view;

import java.awt.Color;
import java.awt.Font;

public class ViewProperties {

    // TODO as bean!
    // TODO colors from color-sudoku
    public final static int GAP_SIZE = 8;

    public final static int FIELD_SIZE = 32;

    public final static int BORDER_THINNESS = 1;

    public final static int BORDER_THICKNESS = 3;

    public final static Font defaultFont = new Font("Dialog", Font.PLAIN, 24); // @jve:decl-index=0:

    public final static Font givenFont = new Font("Dialog", Font.BOLD, 24); // @jve:decl-index=0:

    public final static Color defaultColor = Color.BLACK; // TODO change to standard text color e.g., 51, 51, 51!

    public final static Color givenColor = Color.BLUE;

    public final static Color rightColor = Color.GREEN; // @jve:decl-index=0:

    public final static Color wrongColor = Color.RED;

    private Color[] colors = new Color[] {
            Color.ORANGE, Color.PINK, Color.MAGENTA,
            Color.CYAN, Color.BLUE, Color.LIGHT_GRAY,
            Color.GREEN, Color.RED, Color.YELLOW
    };

    public Color getColor(int index) {
        return colors[index];
    }

    public Color getXBorderColor() {
        return Color.GRAY;
    }

    public Color getXCellColor() {
        return Color.LIGHT_GRAY;
    }

}
