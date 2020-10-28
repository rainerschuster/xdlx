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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rainerschuster.dlx.RandomDataGenerator;

public class SudokuGenerator extends RandomDataGenerator<SudokuColumnValue, SudokuValue> {

    private SudokuProperties properties;

    /* Constraint-Names
     * x1, x2, ... coordinates
     * n ... number
     * r ... region
     * c ... color
     * d ... diagonals (X-Sudoku)
     */

    public SudokuGenerator(SudokuProperties properties) {
        this.properties = properties;
    }

    @Override
    public List<SudokuColumnValue> generatePrimaryColumnValues() {
        final List<SudokuColumnValue> columns = new ArrayList<>();
        Map<String, Integer> values;
        for (int i = 0; i < properties.getNumbers(); i++) {
            for (int j = 0; j < properties.getNumbers(); j++) {
                values = new HashMap<>(2);
                values.put("x1", i);
                values.put("x2", j);
                columns.add(new SudokuColumnValue(values));
                values = new HashMap<>(2);
                values.put("n", i + 1);
                values.put("x1", j);
                columns.add(new SudokuColumnValue(values));
                values = new HashMap<>(2);
                values.put("n", i + 1);
                values.put("x2", j);
                columns.add(new SudokuColumnValue(values));
                values = new HashMap<>(2);
                values.put("n", i + 1);
                values.put("r", j);
                columns.add(new SudokuColumnValue(values));

                if (properties.isColorSudoku()) {
                    values = new HashMap<>(2);
                    values.put("n", i + 1);
                    values.put("c", j);
                    columns.add(new SudokuColumnValue(values));
                }
            }
            if (properties.isXSudoku()) {
                values = new HashMap<>(1);
                values.put("d1", i+1);
                columns.add(new SudokuColumnValue(values));
                values = new HashMap<>(1);
                values.put("d2", i+1);
                columns.add(new SudokuColumnValue(values));
            }
        }
        return columns;
    }

    @Override
    public List<SudokuColumnValue> generateSecondaryColumnValues() {
        return null;
    }

    /*@Override
    public List<SudokuValue> generateValues() {
        return generateMyValues();
    }*/

    // 2D
    @Override
    public List<SudokuValue> generateMyValues() {
        final List<SudokuValue> values = new ArrayList<>();
        for (int n = 1; n <= properties.getNumbers(); n++) {
            for (int r = 0; r < properties.getNumbers(); r++) {
                for (int c = 0; c < properties.getNumbers(); c++) {
                    final SudokuValue sValue = new SudokuValue(properties, n);
                    final List<Integer> coordinates = new ArrayList<>(2);
                    coordinates.add(r);
                    coordinates.add(c);
                    sValue.setCoordinates(coordinates);
                    values.add(sValue);
                }
            }
        }
        return values;
    }

    public SudokuProperties getProperties() {
        return properties;
    }

    public void setProperties(SudokuProperties properties) {
        this.properties = properties;
    }

}
