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
import java.util.Vector;

/**
 * This class solves the exact cover problem provided as a
 * {@link com.rainerschuster.dlx.DancingLinksData} datastructure with the Dancing Links
 * algorithm.
 */
public class DancingLinks<C, V extends Value<C>> implements SourcesSolutionEvents<C, V>, SourcesSolveEvents {

  private SolutionListenerCollection<C, V> solutionListeners = new SolutionListenerCollection<C, V>();

  private SolveListenerCollection solveListeners = new SolveListenerCollection();

  // Global variables
  /** > 0 to show solutions,
   *  > 1 to show partial ones too,
   *  > 2 to show more gory details. */
  private int verbosity = 0;

  /** Number of solutions found so far. */
  private long count = 0;

  /** Number of times we deleted a list element. */
  private BigInteger updates = BigInteger.ZERO;

  /** Number of times we purified a list element. */
  private BigInteger purifs = BigInteger.ZERO;

  /** If verbose, we output solutions when count % spacing == 0. */
  private int spacing = 1;

  /** Tree nodes of given level and degree. */
  private List<List<BigInteger>> profile = new ArrayList<List<BigInteger>>();

  /** Updates at a given level. */
  private List<BigInteger> updProfile = new ArrayList<BigInteger>();

  /** Purifications at a given level. */
  private List<BigInteger> purProfile = new ArrayList<BigInteger>();

  /** Maximum branching factor actually needed. */
  private int maxb = 0;

  /** Maximum level actually reached. */
  private int maxl = 0;

  /** Number of choices in current partial solution. */
  private int level;

  /** Smallest color allowable when extending a solution. */
  private int cthresh;

  /** Set true if a conflict arises while covering. */
  private boolean conflict;

  /** The row and column chosen on each level. */
  private List<Node<C, V>> choice = new ArrayList<Node<C, V>>();


  private DancingLinksData<C, V> dlData;
  private boolean firstSolution = false;
  private boolean quickSolution = false;
  private boolean done = false;


  public DancingLinks(DancingLinksData<C, V> dlData) {
    super();
    this.dlData = dlData;
  }

  /** Starts the solving algorithm. */
  public void solve() {
    count = 0;
    updates = BigInteger.ZERO;
    purifs = BigInteger.ZERO;
    solve(0);
    // TODO cthresh = 'a';
  }

  // TODO check done
  /** DFS/DLX at specified level. */
  private void solve(int level) {
    // TODO QUEST when should it be fired (really at the beginning?)?
    //solveListeners.fireSolution(level);
    this.level = level;
    // Set bestColumn to the best column for branching
    //int minlen = MAX_NODES;
    int minlen = -1;
    Column<C, V> bestColumn = null;
    if (verbosity > 2) {
      System.out.print("Level " + level + ":");
    }
    for (Column<C, V> curCol : dlData) {
      if (verbosity > 2) {
        System.out.print(" " + curCol.getValue() + "(" + curCol.getLength() + ")");
      }
      if (curCol.getLength() < minlen || minlen == -1) {
        bestColumn = curCol;
        minlen = curCol.getLength();
      }
    }
    if (verbosity > 0) {
      if (level > maxl) {
        /*if (level >= MAX_LEVEL) {
          panic("Too many levels");
        }*/
        maxl = level;
      }
      if (minlen > maxb) {
        /*if (minlen >= MAX_DEGREE) {
          panic("Too many branches");
        }*/
        maxb = minlen;
      }
      // profile[level][minlen]++;
      if (level >= profile.size()) {
        profile.add(new ArrayList<BigInteger>());
      }
      if (minlen >= profile.get(level).size()) {
        profile.get(level).add(BigInteger.ONE);
      } else {
        profile.get(level).set(minlen, profile.get(level).get(minlen).add(BigInteger.ONE));
      }
      if (verbosity > 2) {
        System.out.println(" branching on " + bestColumn.getValue() + "(" + minlen + ")");
      }
    }

    if (dlData.getRoot().getNext() != dlData.getRoot()) {
      cover(bestColumn);
      Node<C, V> currentNode = bestColumn.getHead().getDown();
      //choice[level] = bestColumn.getHead().getDown();
      if (level >= choice.size()) {
        choice.add(level, bestColumn.getHead().getDown());
      } else {
        choice.set(level, bestColumn.getHead().getDown());
      }
      while (!done && currentNode != bestColumn.getHead()) {
        //solveListeners.fireSolution(level);
        if (verbosity > 1) {
          System.out.print("L" + level + ":");
          DancingLinksData.printRow(choice.get(level));
        }
        conflict = false;
        coverAllOtherColumns(currentNode);
        if (!conflict) {
          if (dlData.getRoot().getNext() == dlData.getRoot()) {
            // solution found!
            count++;

            if (verbosity > 0) {
              // profile[level + 1][0]++;
              if (level + 1 >= profile.size()) {
                profile.add(new ArrayList<BigInteger>());
              }
              if (minlen >= profile.get(level + 1).size()) {
                profile.get(level + 1).add(BigInteger.ONE);
              } else {
                profile.get(level + 1).set(0, profile.get(level + 1).get(0).add(BigInteger.ONE));
              }

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
        }
        uncoverAllOtherColumns(currentNode);
        currentNode = currentNode.getDown();
        if (!done) {
          choice.set(level, currentNode);
        }
      }
      uncover(bestColumn);
      this.level--;
      //currentNode = choice.get(level);
      //bestColumn = currentNode.getColumn();
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
          //j += profile.get(level).get(k);
          j = j.add(profile.get(level).get(k));
        } else {
          System.out.print("0\t");
        }
      }
      System.out.println(j + " nodes, "
          + updProfile.get(level - 1) + " updates, "
          + purProfile.get(level - 1) + " cleansings");
      //x += j;
      x = x.add(j);
    }
    System.out.println("Total " + x + " nodes.");
  }

  /**
   * When a row is blocked, it leaves all lists except the list of the column
   * that is being covered. Thus a node is never removed from a list twice.
   */
  public void cover(Column<C, V> c) {
    //solveListeners.fireSolution(level);
    //if (c != null) {
    Column<C, V> l, r;
    Node<C, V> uu, dd;
    //long k = 1; // updates
    BigInteger k = BigInteger.ONE; //updates
    l = c.getPrev();
    r = c.getNext();
    l.setNext(r);
    r.setPrev(l);

    for (Node<C, V> rr : c) {
      for (Node<C, V> nn = rr.getRight(); nn != rr; nn = nn.getRight()) {
      //for (Node<C, V> nn : rr) {
        uu = nn.getUp();
        dd = nn.getDown();
        uu.setDown(dd);
        dd.setUp(uu);
        //k++;
        k = k.add(BigInteger.ONE);
        //nn.column.length--;
        nn.getColumn().setLength(nn.getColumn().getLength() - 1);
      }
    }

    c.setCovered(true);

    updates = updates.add(k);
    if (level >= updProfile.size()) {
      updProfile.add(k);
      purProfile.add(BigInteger.ZERO);
    } else {
      if (level >= 0) // FIXME correct this!
      updProfile.set(level, updProfile.get(level).add(k));
    }
    /*} else {
      System.err.println("Cover-Error: Column is null.");
    }*/
  }

  /**
   * Uncovering is done in precisely the reverse order. The pointers thereby
   * execute an exquisitely choreographed dance which returns them almost
   * magically to their former state.
   */
  public void uncover(Column<C, V> c) {
    Column<C, V> l, r;
    Node<C, V> uu, dd;
    for (Node<C, V> rr = c.getHead().getUp(); rr != c.getHead(); rr = rr.getUp()) {
      for (Node<C, V> nn = rr.getLeft(); nn != rr; nn = nn.getLeft()) {
        uu = nn.getUp();
        dd = nn.getDown();
        //uu.down = dd.up = nn;
        uu.setDown(nn);
        dd.setUp(nn);
        //nn.column.length++;
        nn.getColumn().setLength(nn.getColumn().getLength() + 1);
      }
    }

    c.setCovered(false);

    l = c.getPrev();
    r = c.getNext();
    //l.next = r.prev = c;
    l.setNext(c);
    r.setPrev(c);
  }

  /**
   * When we choose a row that specifies colors in one or more columns, we
   * "purify" those columns by removing all incompatible rows. All rows that
   * want the same color in a purified column will now be given the color code
   * -1 so that we need not purify the column again.
   */
  public void purify(Node<C, V> p) {
    Column<C, V> c = p.getColumn();
    int x = p.getColor();
    Node<C, V> /*rr, nn, */uu, dd;
    long k = 0, kk = 1; /* updates */
    c.getHead().setColor(x); /* this is used only to help printRow */
    c.setColorThresh(cthresh);
    if (cthresh >= x) {
      cthresh++;
    }
    for (Node<C, V> rr : c) {
      if (rr.getColor() != x) {
        for (Node<C, V> nn : rr) {
          uu = nn.getUp();
          dd = nn.getDown();
          uu.setDown(dd);
          dd.setUp(uu);
          k++;
          //nn.column.length--;
          nn.getColumn().setLength(nn.getColumn().getLength() - 1);
        }
      } else if (rr != p) {
        kk++;
        rr.setColor(-1);
      }
      updates = updates.add(BigInteger.valueOf(k));
      purifs = purifs.add(BigInteger.valueOf(kk));
      if (level >= updProfile.size()) {
        updProfile.add(BigInteger.valueOf(k));
      } else {
        updProfile.set(level, updProfile.get(level).add(BigInteger.valueOf(k)));
      }
      if (level >= purProfile.size()) {
        purProfile.add(BigInteger.valueOf(kk));
      } else {
        purProfile.set(level, purProfile.get(level).add(BigInteger.valueOf(kk)));
      }
    }
  }

  /**
   * Just as purify is analogous to cover, the inverse process is analogous to uncover.
   */
  public void unpurify(Node<C, V> p) {
    Column<C, V> c = p.getColumn();
    int x = p.getColor();
    Node<C, V> rr, nn, uu, dd;
    for (rr = c.getHead().getUp(); rr != c.getHead(); rr = rr.getUp()) {
      if (rr.getColor() < 0) {
        rr.setColor(x);
      } else if (rr != p) {
        for (nn = rr.getLeft(); nn != rr; nn = nn.getLeft()) {
          uu = nn.getUp();
          dd = nn.getDown();
          //uu.down = dd.up = nn;
          uu.setDown(nn);
          dd.setUp(nn);
          //nn.column.length++;
          nn.getColumn().setLength(nn.getColumn().getLength() + 1);
        }
      }
    }
    c.getHead().setColor(0);
    cthresh = c.getColorThresh();
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
   * @param curNode The node that represents the row.
   */
  public void coverAllColumns(Node<C, V> curNode) {
    cover(curNode.getColumn());
    coverAllOtherColumns(curNode);
  }

  /**
   * Uncovers all columns in the row (that is represented by the node).
   * @param curNode The node that represents the row.
   */
  public void uncoverAllColumns(Node<C, V> curNode) {
    uncoverAllOtherColumns(curNode);
    uncover(curNode.getColumn());
  }

  public void cover(V value) {
    List<Column<C, V>> list = value.inRelations(dlData.getAllColumns());
    for (Column<C, V> column : list) {
      cover(column);
    }
  }

  public void uncover(V value) {
    List<Column<C, V>> list = value.inRelations(dlData.getAllColumns());
    // uncover in reversed order
    Collections.reverse(list);
    for (Column<C, V> column : list) {
      uncover(column);
    }
  }

  // TODO conflict als return-Wert oder Exception (statt globaler Variable)
  /** Covers all colums which are in the same row as <code>curNode</code> (the column <code>curNode</code> itself is not covered). */
  public void coverAllOtherColumns(Node<C, V> curNode) {
    // Cover all other columns of curNode
    for (Node<C, V> pp = curNode.getRight(); pp != curNode; pp = pp.getRight()) {
      if (pp.getColor() == 0) {
        cover(pp.getColumn());
      } else if (pp.getColor() > 0) {
        /*TODO if (pp.getColor() > cthresh) {
          conflict = true;
        } else {*/
          purify(pp);
        /*}*/
      }
    }
  }

  /** Uncovers all colums which are in the same row as <code>curNode</code> (the column <code>curNode</code> itself is not uncovered). */
  public void uncoverAllOtherColumns(Node<C, V> curNode) {
    for (Node<C, V> pp = curNode.getLeft(); pp != curNode; pp = pp.getLeft()) {
      if (pp.getColor() == 0) {
        uncover(pp.getColumn());
      } else if (pp.getColor() > 0/* TODO && pp.getColor() > cthresh*/) {
        unpurify(pp);
      }
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
  /** @return 0 if no solution, 1 if unique solution and 2 if two or more solutions */
  public long quickSolutions() {
    // Backup of the solution is necessary (if reducing)
    List<Node<C, V>> backup = new Vector<Node<C, V>>(choice.size());
    //Collections.copy(backup, choice);
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

  /** Trys to eliminate the latter solution nodes so that there is still a unique solution.
   * @param previousLast Index of the previously found reduction.
   * @return Lowest possible index to fulfill uniquity requirement.
   */
  private int reduceEnd(int previousLast) {
    for (int i = 0; i <= previousLast; i++) {
      //coverAllColumns(solution1[i]);
      coverAllColumns(choice.get(i));
    }
    done = false;
    int last = 0;
    for (int i = previousLast; i >= 0; i--) {
      //uncoverAllColumns(solution1[i]);
      uncoverAllColumns(choice.get(i));
      if (!done && quickSolutions() != 1) {
        // now there is no unique solution
        last = i; // up to last necessary to be unique!
        done = true;
        //return i;
      }
    }
    return last;
  }

  // TODO reduce while solving (e.g. at call in onSolution)
  /** Strategy: Cover all solution-nodes an try to uncover as much as possible.
   * @return A subset of the solution that produces the same solution when the nodes are covered and solved (e.g. that would be the givens of a sudoku-solution). */
  public List<Node<C, V>> reduce() {
    //Date beginDate = new Date();

    int last = 0;
    last = choice.size() - 1;
    Collections.shuffle(choice); // An alternative would be to shuffle the order of columns when data-structure is built
    List<Node<C, V>> givens = new Vector<Node<C, V>>();
    /*for (int i = 0; i <= last; i++) {
      coverAllColumns(choice.get(i));
    }*/
    while (last >= 0) {
      //fireOnReduceStep(nodes - last);
      last = reduceEnd(last);
      // recover it (cause it is needed) and add it to the given-set
      coverAllColumns(choice.get(last));
      givens.add(choice.get(last));
      last--;
    }

    if (verbosity > 0 &&  1 + last != givens.size()) {
      System.out.println("New rest: " + givens.size());
    }

    for (int i = givens.size() - 1; i >= 0; i--) {
      uncoverAllColumns(givens.get(i));
    }

    return givens;
  }

  // TODO count may be incorrect (2) if reducing!
  public void printStatistics() {
    System.out.println("Altogether "
        + count + " solutions, after "
        + updates + " updates and "
        + purifs + " cleansings.");
    if (verbosity > 0) {
      printProfile();
    }
  }

  @Override
public void addSolutionListener(SolutionListener<C, V> listener) {
    if (solutionListeners == null) {
      solutionListeners = new SolutionListenerCollection<C, V>();
    }
    solutionListeners.add(listener);
  }

  @Override
public void removeSolutionListener(SolutionListener<C, V> listener) {
    if (solutionListeners != null) {
      solutionListeners.remove(listener);
    }
  }

  @Override
public void addSolveListener(SolveListener listener) {
    if (solveListeners == null) {
      solveListeners = new SolveListenerCollection();
    }
    solveListeners.add(listener);
  }

  @Override
public void removeSolveListener(SolveListener listener) {
    if (solveListeners != null) {
      solveListeners.remove(listener);
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

}
