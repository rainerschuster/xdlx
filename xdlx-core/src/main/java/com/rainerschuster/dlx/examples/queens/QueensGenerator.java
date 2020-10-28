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
package com.rainerschuster.dlx.examples.queens;

import java.util.ArrayList;
import java.util.List;

import com.rainerschuster.dlx.DataGenerator;

public class QueensGenerator extends
        DataGenerator<QueensColumnValue, QueensValue> {

    private QueensProperties properties;

    public QueensGenerator(final QueensProperties properties) {
        super();
        this.properties = properties;
    }

    /**
     * Maps numbers to hex-encoded chars.
     *
     * @param x
     *            The number to encode.
     * @return The hex-encoded char.
     */
    public static char encode(final int x) {
        // 0..9a..z
        if (x < 10) {
            return (char) ('0' + x);
        }
        return (char) ('a' - 10 + x);
    }

    @Override
    public List<QueensColumnValue> generatePrimaryColumnValues() {
        final int n = properties.getN();
        final List<QueensColumnValue> primaryColumnValues = new ArrayList<>(n + n);
        for (int j = 0; j < n; j++) {
            // final int t = j; // primitive column order
            final int t = ((j % 2 == 1) ? n - 1 - j : n + j) >> 1; // optimized column order
            QueensColumnValue columnValue = new QueensColumnValue(QueensEnum.class);
            columnValue.put(QueensEnum.ROW, t);
            primaryColumnValues.add(columnValue);
            columnValue = new QueensColumnValue(QueensEnum.class);
            columnValue.put(QueensEnum.COLUMN, t);
            primaryColumnValues.add(columnValue);

            // System.out.print("r" + encode(t) + " c" + encode(t) + " ");
        }
        return primaryColumnValues;
    }

    @Override
    public List<QueensColumnValue> generateSecondaryColumnValues() {
        int nn = properties.getNn();

        if (nn <= 0) {
            return null;
        }

        final List<QueensColumnValue> secondaryColumnValues = new ArrayList<>(nn - 1);
        for (int j = 1; j < nn; j++) {
            QueensColumnValue columnValue = new QueensColumnValue(QueensEnum.class);
            columnValue.put(QueensEnum.DIAGONAL_A, j);
            secondaryColumnValues.add(columnValue);
            columnValue = new QueensColumnValue(QueensEnum.class);
            columnValue.put(QueensEnum.DIAGONAL_B, j);
            secondaryColumnValues.add(columnValue);

            // System.out.print(" a" + encode(j) + " b" + encode(j));
        }
        return secondaryColumnValues;
    }

    @Override
    public List<QueensValue> generateValues() {
        final int n = properties.getN();
        final List<QueensValue> values = new ArrayList<>(n * n);
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                values.add(new QueensValue(properties, j, k));
            }
        }
        return values;
    }

    public QueensProperties getProperties() {
        return properties;
    }

    public void setProperties(QueensProperties properties) {
        this.properties = properties;
    }

}
