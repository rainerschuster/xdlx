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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class CellBorder extends AbstractBorder {

    private static final long serialVersionUID = 1L;

    protected int thinness; // :-)
    protected int thickness;
    protected Color lineColor;
    //protected boolean roundedCorners;
    protected boolean north, east, south, west;

    public CellBorder(Color color, int thinness, int thickness/*, boolean roundedCorners*/, boolean north, boolean east, boolean south, boolean west)  {
        if (thinness > thickness) {
            // TODO Exception!
            System.err.println("Thinness must not be larger than thickness!");
        }
        this.lineColor = color;
        this.thinness = thinness;
        this.thickness = thickness;
        //this.roundedCorners = roundedCorners;
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public CellBorder(Color color, int thinness, int thickness/*, boolean roundedCorners*/)  {
        this(color, thinness, thickness, false, false, false, false);
    }

    /* (non-Javadoc)
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component, java.awt.Insets)
     */
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = thickness;
        return insets;
    }

    /* (non-Javadoc)
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    /* (non-Javadoc)
     * @see javax.swing.border.AbstractBorder#isBorderOpaque()
     */
    @Override
    public boolean isBorderOpaque() {
        // TODO Auto-generated method stub
        return super.isBorderOpaque();
    }

    /* (non-Javadoc)
     * @see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
     */
    @Override
    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {
        final Color oldColor = g.getColor();

        g.setColor(lineColor);
        int ax = x, ay = y, awidth = width, aheight = height;
        for (int i = 0; i < thickness; i++) {
            g.drawRect(ax, ay, awidth - 1, aheight - 1);
            if (north || thinness > i + 1) {
                ay++;
                aheight--;
            }
            if (east || thinness > i + 1) {
                awidth--;
            }
            if (south || thinness > i + 1) {
                aheight--;
            }
            if (west || thinness > i + 1) {
                ax++;
                awidth--;
            }
            /*if(!roundedCorners) {
                g.drawRect(x+i, y+i, width-i-i-1, height-i-i-1);
            } else {
                g.drawRoundRect(x+i, y+i, width-i-i-1, height-i-i-1, thickness, thickness);
            }*/
        }

        g.setColor(oldColor);
    }


    /**
     * @return the north
     */
    public boolean isNorth() {
        return north;
    }

    /**
     * @param north the north to set
     */
    public void setNorth(boolean north) {
        this.north = north;
    }

    /**
     * @return the east
     */
    public boolean isEast() {
        return east;
    }

    /**
     * @param east the east to set
     */
    public void setEast(boolean east) {
        this.east = east;
    }

    /**
     * @return the south
     */
    public boolean isSouth() {
        return south;
    }

    /**
     * @param south the south to set
     */
    public void setSouth(boolean south) {
        this.south = south;
    }

    /**
     * @return the west
     */
    public boolean isWest() {
        return west;
    }

    /**
     * @param west the west to set
     */
    public void setWest(boolean west) {
        this.west = west;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

}
