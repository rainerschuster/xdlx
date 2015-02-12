package com.rainerschuster.dlx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Given a solved {@link DancingLinks} instance with a <strong>unique</strong> solution,
 * the reducer tries to leave out choice nodes so that the solution remains unique.
 * For example this can be used for finding "givens" for a Sudoku.
 */
public class Reducer<C, V extends Value<C>> {

    private final DancingLinks<C, V> dancingLinks;

    public Reducer(DancingLinks<C, V> dancingLinks) {
        this.dancingLinks = dancingLinks;
    }

    // TODO reduce while solving (e.g., at call in onSolution)
    /**
     * Strategy: Cover all solution-nodes an try to uncover as much as possible.
     * 
     * @return A subset of the solution that produces the same solution when the
     *         nodes are covered and solved (e.g., that would be the givens of a
     *         sudoku-solution).
     */
    public List<Node<C, V>> reduce() {
        // Date beginDate = new Date();

        int last = 0;
        last = dancingLinks.choice.size() - 1;
        Collections.shuffle(dancingLinks.choice); // An alternative would be to shuffle the order of columns when data-structure is built
        List<Node<C, V>> givens = new ArrayList<Node<C, V>>();
        /*
         * for (int i = 0; i <= last; i++) { coverAllColumns(choice.get(i)); }
         */
        while (last >= 0) {
            // fireOnReduceStep(nodes - last);
            last = reduceEnd(last);
            // recover it (cause it is needed) and add it to the given-set
            dancingLinks.coverAllColumns(dancingLinks.choice.get(last));
            givens.add(dancingLinks.choice.get(last));
            last--;
        }

        if (dancingLinks.verbosity > 0 && 1 + last != givens.size()) {
            System.out.println("New rest: " + givens.size());
        }

        for (int i = givens.size() - 1; i >= 0; i--) {
            dancingLinks.uncoverAllColumns(givens.get(i));
        }

        return givens;
    }

    /**
     * Tries to eliminate the latter solution nodes so that there is still a
     * unique solution.
     * 
     * @param previousLast
     *            Index of the previously found reduction.
     * @return Lowest possible index to fulfill uniquity requirement.
     */
    private int reduceEnd(final int previousLast) {
        for (int i = 0; i <= previousLast; i++) {
            // coverAllColumns(solution1[i]);
            dancingLinks.coverAllColumns(dancingLinks.choice.get(i));
        }
        dancingLinks.done = false;
        int last = 0;
        for (int i = previousLast; i >= 0; i--) {
            // uncoverAllColumns(solution1[i]);
            dancingLinks.uncoverAllColumns(dancingLinks.choice.get(i));
            if (!dancingLinks.done && dancingLinks.quickSolutions() != 1) {
                // now there is no unique solution
                last = i; // up to last necessary to be unique!
                dancingLinks.done = true;
                // return i;
            }
        }
        return last;
    }

}
