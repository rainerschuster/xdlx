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

import com.rainerschuster.dlx.DancingLinks;
import com.rainerschuster.dlx.DancingLinksData;

public class Queens {

    private QueensProperties properties;

    /**
     * @param n
     *            The number of queens (on a n &times; n field).
     */
    public Queens(int n) {
        super();
        this.properties = new QueensProperties(n);
    }

    /**
     * @param args
     *            Command line arguments. Contains the number of queens.
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                final int param = Integer.parseInt(args[0]);
                final Queens queens = new Queens(param);
                queens.start();
            } catch (NumberFormatException e) {
                usage();
            }
        } else {
            usage();
        }
    }

    public void start() {
        final QueensGenerator generator = new QueensGenerator(properties);

        final DancingLinksData<QueensColumnValue, QueensValue> dlData = generator.generate();
        final DancingLinks<QueensColumnValue, QueensValue> dl = new DancingLinks<QueensColumnValue, QueensValue>(dlData);
        dl.setVerbosity(1);
        dl.setSpacing(10000000);
        /*// final QueensValueConverter valueConver = new QueensValueConverter(properties);
        final QueensSolutionConverter solutionConverter = new QueensSolutionConverter(properties);
        dl.addSolutionListener(new SolutionListener<QueensColumnValue, QueensValue>() {

            public void onSolution(final int count, final int level, final List<Node<QueensColumnValue, QueensValue>> solution) {
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
        dl.solve();
        dl.printStatistics();
    }

    /** Prints the command line synopsis and exits. */
    private static void usage() {
        // FIXME Usage Programmaufruf fehlt in Usage
        System.err.println("Usage: n");
        System.exit(-1);
    }

}
