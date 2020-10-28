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
package com.rainerschuster.dlx;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class solves the exact cover problem provided as a
 * {@link com.rainerschuster.dlx.DancingLinksData} datastructure with the Dancing Links
 * algorithm.
 */
public class DancingLinks<C, V extends Value<C>> implements SourcesSolutionEvents<C, V> {

    private SolutionListenerCollection<C, V> solutionListeners = new SolutionListenerCollection<>();

    // Global variables
    /** > 0 to show solutions,
     *  > 1 to show partial ones too,
     *  > 2 to show more gory details. */
    protected int verbosity = 0;

    /** Number of solutions found so far. */
    protected long count = 0;

    /** Number of times we deleted a list element. */
    protected BigInteger updates = BigInteger.ZERO;

    /** If verbose, we output solutions when count % spacing == 0. */
    protected int spacing = 1;

    /** Tree nodes of given level and degree. */
    protected List<List<BigInteger>> profile = new ArrayList<>();

    /** Updates at a given level. */
    protected List<BigInteger> updProfile = new ArrayList<>();

    /** Maximum branching factor actually needed. */
    protected int maxb = 0;

    /** Maximum level actually reached. */
    protected int maxl = 0;

    /** Number of choices in current partial solution. */
    protected int level;

    /** The row and column chosen on each level. */
    protected List<Node<C, V>> choice = new ArrayList<>();

    private DancingLinksData<C, V> dlData;
    private boolean firstSolution = false;
    private boolean quickSolution = false;
    boolean done = false;

    private ColumnChooser<C, V> columnChooser = new MinLengthColumnChooser<>();

    public DancingLinks(DancingLinksData<C, V> dlData) {
        super();
        this.dlData = dlData;
    }

    /** Starts the solving algorithm. */
    public void solve() {
        count = 0;
        updates = BigInteger.ZERO;
        solve(0);
    }

    // TODO check done
    /** DFS/DLX at specified level. */
    private void solve(final int level) {
        this.level = level;
        if (verbosity > 2) {
            System.out.print("Level " + level + ":");
        }
        // Set bestColumn to the best column for branching
        // int minlen = MAX_NODES;
        final Column<C, V> bestColumn = columnChooser.chooseColumn(dlData);
        final int minlen = bestColumn.getLength();
        if (verbosity > 0) {
            if (level > maxl) {
                maxl = level;
            }
            if (minlen > maxb) {
                maxb = minlen;
            }
            // profile[level][minlen]++;
//            ensureIndexSize(profile, level, new ArrayList<BigInteger>());
            if (level >= profile.size()) {
                profile.add(new ArrayList<BigInteger>());
            }
            ensureIndexSize(profile.get(level), minlen, BigInteger.ZERO);
            profile.get(level).set(minlen, profile.get(level).get(minlen).add(BigInteger.ONE));
            if (verbosity > 2) {
                System.out.println(" branching on " + bestColumn.getValue() + "(" + minlen + ")");
            }
        }

        if (dlData.getRoot().getNext() != dlData.getRoot()) {
            cover(bestColumn);
            Node<C, V> currentNode = bestColumn.getHead().getDown();
            // choice[level] = bestColumn.getHead().getDown();
            ensureIndexSize(choice, level, null);
            choice.set(level, bestColumn.getHead().getDown());
            while (!done && currentNode != bestColumn.getHead()) {
                if (verbosity > 1) {
                    System.out.print("L" + level + ":");
                    DancingLinksData.printRow(choice.get(level));
                }
                coverAllOtherColumns(currentNode);
                if (dlData.getRoot().getNext() == dlData.getRoot()) {
                    // solution found!
                    count++;

                    if (verbosity > 0) {
                        // profile[level + 1][0]++;
                        // ensureIndexSize(profile, level + 1, new ArrayList<BigInteger>());
                        if (level + 1 >= profile.size()) {
                            profile.add(new ArrayList<BigInteger>());
                        }
                        ensureIndexSize(profile.get(level + 1), 0, BigInteger.ZERO);
                        profile.get(level + 1).set(0, profile.get(level + 1).get(0).add(BigInteger.ONE));

                        if (count % spacing == 0) {
                            System.out.println(count + ":");
                            for (int k = 0; k <= level; k++) {
                                DancingLinksData.printRow(choice.get(k));
                            }
                        }
                    }

                    if (quickSolution) {
                        if (count > 1) {
                            done = true;
                        }
                    }
                    // TODO check and validate this part!
                    if (!done && !quickSolution) {
                        solutionListeners.fireSolution(count, level, choice);
                    }
                    if (firstSolution) {
                        done = true;
                    }
                    // own extension END
                } else {
                    if (!done) {
                        solve(level + 1);
                    }
                }
                uncoverAllOtherColumns(currentNode);
                currentNode = currentNode.getDown();
                if (!done) {
                    choice.set(level, currentNode);
                }
            }
            uncover(bestColumn);
            this.level--;
            // currentNode = choice.get(level);
            // bestColumn = currentNode.getColumn();
        }
    }

    // TODO an error occurs if this method is invoked before solved
    /** Print a profile of the search tree. */
    public void printProfile() {
        // the root node doesn't show up in the profile
        BigInteger x = BigInteger.ONE;
        for (int level = 1; level <= maxl + 1; level++) {
            BigInteger j = BigInteger.ZERO;
            for (int k = 0; k < maxb; k++) {
                if (level < profile.size() && k < profile.get(level).size()) {
                    System.out.print(profile.get(level).get(k) + "\t");
                    // j += profile.get(level).get(k);
                    j = j.add(profile.get(level).get(k));
                } else {
                    System.out.print("0\t");
                }
            }
//            System.out.println(j + " nodes, " + updProfile.get(level - 1) + " updates, " + purProfile.get(level - 1) + " cleansings");
            System.out.println(j + " nodes, " + updProfile.get(level - 1) + " updates");
            // x += j;
            x = x.add(j);
        }
        System.out.println("Total " + x + " nodes.");
    }

    /**
     * When a row is blocked, it leaves all lists except the list of the column
     * that is being covered. Thus a node is never removed from a list twice.
     */
    public void cover(final Column<C, V> c) {
        // long k = 1; // updates
        BigInteger k = BigInteger.ONE; // updates
        blockColumn(c);

        for (Node<C, V> rr : c) {
            k = k.add(blockRow(rr, k));
        }

        c.setCovered(true);

        updates = updates.add(k);
        if (level < 0) {
        	return; // FIXME
        }
        ensureIndexSize(updProfile, level, BigInteger.ZERO);
        updProfile.set(level, updProfile.get(level).add(k));
    }

    public void blockColumn(final Column<C, V> c) {
        final Column<C, V> l = c.getPrev();
        final Column<C, V> r = c.getNext();
        l.setNext(r);
        r.setPrev(l);
    }

    public BigInteger blockRow(final Node<C, V> rr, BigInteger k) {
        for (Node<C, V> nn = rr.getRight(); nn != rr; nn = nn.getRight()) {
            // for (Node<C, V> nn : rr) {
            k = k.add(blockNode(nn, k));
        }
        return k;
    }

    public BigInteger blockNode(final Node<C, V> nn, BigInteger k) {
        final Node<C, V> uu = nn.getUp();
        final Node<C, V> dd = nn.getDown();
        uu.setDown(dd);
        dd.setUp(uu);
        // nn.column.length--;
        nn.getColumn().setLength(nn.getColumn().getLength() - 1);
        // k++;
        return k.add(BigInteger.ONE);
    }

    /**
     * Uncovering is done in precisely the reverse order. The pointers thereby
     * execute an exquisitely choreographed dance which returns them almost
     * magically to their former state.
     */
    public void uncover(final Column<C, V> c) {
        for (Node<C, V> rr = c.getHead().getUp(); rr != c.getHead(); rr = rr.getUp()) {
            unblockRow(rr);
        }

        c.setCovered(false);

        unblockColumn(c);
    }

    public void unblockColumn(final Column<C, V> c) {
        final Column<C, V> l = c.getPrev();
        final Column<C, V> r = c.getNext();
        // l.next = r.prev = c;
        l.setNext(c);
        r.setPrev(c);
    }

    public void unblockRow(Node<C, V> rr) {
        for (Node<C, V> nn = rr.getLeft(); nn != rr; nn = nn.getLeft()) {
            unblockNode(nn);
        }
    }

    public void unblockNode(Node<C, V> nn) {
        final Node<C, V> uu = nn.getUp();
        final Node<C, V> dd = nn.getDown();
        // uu.down = dd.up = nn;
        uu.setDown(nn);
        dd.setUp(nn);
        // nn.column.length++;
        nn.getColumn().setLength(nn.getColumn().getLength() + 1);
    }

    /**
     * Covers all columns which are in the same row as <code>curNode</code> (the
     * column <code>curNode</code> itself is not covered).
     */
    public void coverAllOtherColumns(final Node<C, V> curNode) {
        // Cover all other columns of curNode
        for (Node<C, V> pp = curNode.getRight(); pp != curNode; pp = pp.getRight()) {
            cover(pp.getColumn());
        }
    }

    /**
     * Uncovers all columns which are in the same row as <code>curNode</code>
     * (the column <code>curNode</code> itself is not uncovered).
     */
    public void uncoverAllOtherColumns(final Node<C, V> curNode) {
        for (Node<C, V> pp = curNode.getLeft(); pp != curNode; pp = pp.getLeft()) {
            uncover(pp.getColumn());
        }
    }

    /**
     * Help for debugging<br />
     * Here's a subroutine for when I'm doing a long run and want to check the
     * current progress.
     */
    public void showState() {
        System.out.println("Current state (level " + level + "):");
        for (int k = 0; k < level; k++) {
            DancingLinksData.printRow(choice.get(k));
        }
        System.out.println("Max level so far: " + maxl);
        System.out.println("Max branching so far: " + maxb);
        System.out.println("Solutions so far: " + count);
    }

    /**
     * Covers all columns in the row (that is represented by the node).
     * 
     * @param curNode
     *            The node that represents the row.
     */
    public void coverAllColumns(final Node<C, V> curNode) {
        cover(curNode.getColumn());
        coverAllOtherColumns(curNode);
    }

    /**
     * Uncovers all columns in the row (that is represented by the node).
     * 
     * @param curNode
     *            The node that represents the row.
     */
    public void uncoverAllColumns(final Node<C, V> curNode) {
        uncoverAllOtherColumns(curNode);
        uncover(curNode.getColumn());
    }

    public void cover(final V value) {
        final List<Column<C, V>> list = value.inRelations(dlData.getAllColumns());
        for (Column<C, V> column : list) {
            cover(column);
        }
    }

    public void uncover(final V value) {
        final List<Column<C, V>> list = value.inRelations(dlData.getAllColumns());
        // uncover in reversed order
        Collections.reverse(list);
        for (Column<C, V> column : list) {
            uncover(column);
        }
    }

    /** @return The first solution. */
    public List<Node<C, V>> getFirstSolution() {
        done = false;
        firstSolution = true;
        solve();
        firstSolution = false;
        return choice;
    }

    // usually used to decide if there is a unique solution
    /**
     * @return 0 if no solution, 1 if unique solution and 2 if two or more solutions
     */
    public long quickSolutions() {
        // Backup of the solution is necessary (if reducing)
        final List<Node<C, V>> backup = new ArrayList<>(choice.size());
        // Collections.copy(backup, choice);
        for (Node<C, V> n : choice) {
            backup.add(n);
        }
        done = false;
        quickSolution = true;
        solve();
        quickSolution = false;
        choice = backup;
        return count;
    }

    // TODO count may be incorrect (2) if reducing!
    public void printStatistics() {
        System.out.println("Altogether "
                + count + " solutions, after "
                + updates + " updates.");
        if (verbosity > 0) {
            printProfile();
        }
    }

    @Override
    public void addSolutionListener(SolutionListener<C, V> listener) {
        if (solutionListeners == null) {
            solutionListeners = new SolutionListenerCollection<>();
        }
        solutionListeners.add(listener);
    }

    @Override
    public void removeSolutionListener(SolutionListener<C, V> listener) {
        if (solutionListeners != null) {
            solutionListeners.remove(listener);
        }
    }

    public int getVerbosity() {
        return verbosity;
    }

    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    // TODO QUEST volatile?
    public List<Node<C, V>> getChoice() {
        return choice;
    }

    public DancingLinksData<C, V> getDancingLinksData() {
        return dlData;
    }

    public void setDancingLinksData(DancingLinksData<C, V> dlData) {
        this.dlData = dlData;
    }

    /**
     * Adds values (specified by <code>defaultValue</code>) until the specified list is large enough to be accessed by the specified index.
     */
    protected <T> void ensureIndexSize(final List<T> list, int index, T defaultValue) {
        if (index >= list.size()) {
            final int diff = index - list.size();
            for (int i = 0; i <= diff; i++) {
                list.add(defaultValue);
            }
        }
    }

}
