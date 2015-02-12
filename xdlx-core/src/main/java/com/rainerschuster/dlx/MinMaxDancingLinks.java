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

/**
 * Generalized version of {@link DancingLinks} with "minimum" and "maximum" constraints.
 */
public class MinMaxDancingLinks<C, V extends Value<C>> extends DancingLinks<C, V> {

    public MinMaxDancingLinks(DancingLinksData<C, V> dlData) {
        super(dlData);
    }

//    @Override
    public void cover(final MaxColumn<C, V> c) {
        // TODO updates k
//        super.cover(c);
        final Node<C, V> currentNode = c.getHead().getDown();
        c.getChoice().add(currentNode);
        if (c.getChoice().size() >= c.getMax()) {
            super.cover(c);
        } else {
            // block this row only
            blockRow(currentNode, BigInteger.ZERO);
        }
    }

//    @Override
    public void uncover(final MaxColumn<C, V> c) {
//        super.uncover(c);
        final Node<C, V> currentNode = c.getHead().getDown();
        if (c.getChoice().size() >= c.getMax()) {
            // TODO all except those in choice!
//            super.uncover(c);
            for (Node<C, V> rr = c.getHead().getUp(); rr != c.getHead(); rr = rr.getUp()) {
                if (rr == currentNode || !c.getChoice().contains(rr)) {
                    unblockRow(rr);
                }
            }
//            unblockColumn(c);
        } else {
            unblockRow(currentNode);
        }
        c.getChoice().remove(currentNode);
    }

//  @Override
  public void cover(final MinColumn<C, V> c) {
      // TODO updates k
//      super.cover(c);
      final Node<C, V> currentNode = c.getHead().getDown();
      c.getChoice().add(currentNode);
      if (c.getChoice().size() >= c.getMin()) {
          blockColumn(c);
      }
      // block this row only
      blockRow(currentNode, BigInteger.ZERO);
  }

//    @Override
    public void uncover(final MinColumn<C, V> c) {
    //    super.uncover(c);
        final Node<C, V> currentNode = c.getHead().getDown();
        unblockRow(currentNode);
        c.getChoice().remove(currentNode);
        if (c.getChoice().size() < c.getMin()) {
            unblockColumn(c);
        }
    }

//    @Override
//    public void cover(final Column<C, V> c) {
//        super.cover(c);
//    }
//
//    @Override
//    public void uncover(final Column<C, V> c) {
//        super.uncover(c);
//    }

    

}
