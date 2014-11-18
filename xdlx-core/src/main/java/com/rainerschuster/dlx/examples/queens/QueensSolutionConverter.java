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

import java.util.List;

import com.rainerschuster.dlx.Node;

public class QueensSolutionConverter {

    private QueensProperties properties;

    public QueensSolutionConverter(QueensProperties properties) {
        this.properties = properties;
    }

    public QueensProperties getProperties() {
        return properties;
    }

    public void setProperties(QueensProperties properties) {
        this.properties = properties;
    }

    public boolean[][] convertSolution(final List<Node<QueensColumnValue, QueensValue>> solution) {
        if (solution == null) {
            // TODO Exception? return empty array?
            System.err.println("No solution to convert!");
            return null;
        }
        boolean[][] field = new boolean[properties.getN()][properties.getN()];
        for (Node<QueensColumnValue, QueensValue> node : solution) {
            // final QueensValue value = converter.convertRow(node);
            final QueensValue value = node.getValue();
            field[value.getRow()][value.getColumn()] = true;
        }
        return field;
    }

}
