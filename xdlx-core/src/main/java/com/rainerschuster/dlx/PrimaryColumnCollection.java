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

import java.util.AbstractSequentialList;
import java.util.ListIterator;

public class PrimaryColumnCollection<C, V extends Value<C>> extends
    AbstractSequentialList<PrimaryColumn<C, V>> {

  private PrimaryColumn<C, V> root;

  // private int length = 0;

  protected class PrimaryColumnListIterator implements
      ListIterator<PrimaryColumn<C, V>> {
    int index = -1;

    PrimaryColumn<C, V> column = root;

    public PrimaryColumnListIterator(int index) {
      this.index = -1;
      while (this.index < index) { // TODO QUEST +-1?
        if (!hasNext()) {
          // TODO OutOfBoundException
        }
        next();
      }
    }

    @Override
	public void add(PrimaryColumn<C, V> column) {
      // TODO Auto-generated method stub

    }

    @Override
	public boolean hasNext() {
      return column.getNext() != root;
    }

    @Override
	public boolean hasPrevious() {
      return index > 0;
    }

    @Override
	public PrimaryColumn<C, V> next() {
      column = column.getNext();
      index++;
      return column;
    }

    @Override
	public int nextIndex() {
      return index + 1;
    }

    @Override
	public PrimaryColumn<C, V> previous() {
      column = column.getPrev();
      index--;
      return column;
    }

    @Override
	public int previousIndex() {
      return index - 1;
    }

    @Override
	public void remove() {
      // TODO Auto-generated method stub

    }

    @Override
	public void set(PrimaryColumn<C, V> column) {
      // TODO Auto-generated method stub

    }

  }

  @Override
  public ListIterator<PrimaryColumn<C, V>> listIterator(int index) {
    return new PrimaryColumnListIterator(index);
  }

  @Override
  public int size() {
    // return length;
    return 0;
  }

}
