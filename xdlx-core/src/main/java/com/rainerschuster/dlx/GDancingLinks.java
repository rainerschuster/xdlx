/*
 * Copyright 2015 Rainer Schuster
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
import java.util.List;

/**
 * Generalized version of {@link DancingLinks} with "color" support (see GDANCE).
 */
public class GDancingLinks<C, V extends Value<C>> extends DancingLinks<C, V> {

    /** Number of times we purified a list element. */
    private BigInteger purifs = BigInteger.ZERO;

    /** Purifications at a given level. */
    private List<BigInteger> purProfile = new ArrayList<BigInteger>();

    public GDancingLinks(DancingLinksData<C, V> dlData) {
        super(dlData);
    }

    @Override
    public void solve() {
        purifs = BigInteger.ZERO;
        super.solve();
    }

    /**
     * Covers all columns which are in the same row as <code>curNode</code>
     * (the column <code>curNode</code> itself is not covered).
     */
    @Override
    public void coverAllOtherColumns(final Node<C, V> curNode) {
        // Cover all other columns of curNode
        for (Node<C, V> pp = curNode.getRight(); pp != curNode; pp = pp.getRight()) {
            if (pp.getColor() == 0) {
                cover(pp.getColumn());
            } else if (pp.getColor() > 0) {
                purify(pp);
            }
        }
    }

    /**
     * Uncovers all columns which are in the same row as <code>curNode</code>
     * (the column <code>curNode</code> itself is not uncovered).
     */
    @Override
    public void uncoverAllOtherColumns(final Node<C, V> curNode) {
        for (Node<C, V> pp = curNode.getLeft(); pp != curNode; pp = pp.getLeft()) {
            if (pp.getColor() == 0) {
                uncover(pp.getColumn());
            } else if (pp.getColor() > 0) {
                unpurify(pp);
            }
        }
    }

    /**
     * When we choose a row that specifies colors in one or more columns, we
     * "purify" those columns by removing all incompatible rows. All rows that
     * want the same color in a purified column will now be given the color code
     * -1 so that we need not purify the column again.
     */
    public void purify(final Node<C, V> p) {
        Column<C, V> c = p.getColumn();
        int x = p.getColor();
        Node<C, V> /* rr, nn, */uu, dd;
        long k = 0, kk = 1; /* updates */
        c.getHead().setColor(x); /* this is used only to help printRow */
        for (Node<C, V> rr : c) {
            if (rr.getColor() != x) {
                for (Node<C, V> nn : rr) {
                    uu = nn.getUp();
                    dd = nn.getDown();
                    uu.setDown(dd);
                    dd.setUp(uu);
                    k++;
                    // nn.column.length--;
                    nn.getColumn().setLength(nn.getColumn().getLength() - 1);
                }
            } else if (rr != p) {
                kk++;
                rr.setColor(-1);
            }
            updates = updates.add(BigInteger.valueOf(k));
            purifs = purifs.add(BigInteger.valueOf(kk));
            ensureIndexSize(updProfile, level, BigInteger.ZERO);
            updProfile.set(level, updProfile.get(level).add(BigInteger.valueOf(k)));
            ensureIndexSize(purProfile, level, BigInteger.ZERO);
            purProfile.set(level, purProfile.get(level).add(BigInteger.valueOf(kk)));
        }
    }

    /**
     * Just as purify is analogous to cover, the inverse process is analogous to uncover.
     */
    public void unpurify(final Node<C, V> p) {
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
                    // uu.down = dd.up = nn;
                    uu.setDown(nn);
                    dd.setUp(nn);
                    // nn.column.length++;
                    nn.getColumn().setLength(nn.getColumn().getLength() + 1);
                }
            }
        }
        c.getHead().setColor(0);
    }

    @Override
    public void printStatistics() {
        System.out.println("Altogether "
                + count + " solutions, after "
                + updates + " updates and "
                + purifs + " cleansings.");
        if (verbosity > 0) {
            printProfile();
        }
    }

}
