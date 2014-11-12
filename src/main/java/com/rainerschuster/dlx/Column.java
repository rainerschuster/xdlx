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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Column<C, V extends Value<C>> implements Iterable<Node<C, V>> {

  /** The list header. */
  private Node<C, V> head;

  /** The number of non-header items currently in this column's list. */
  private int length;

  /** Identification of the column. */
  private C value;

  private boolean covered;

  public Node<C, V> getHead() {
    return this.head;
  }

  public void setHead(Node<C, V> head) {
    this.head = head;
  }

  public int getLength() {
    return this.length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public C getValue() {
    return this.value;
  }

  public void setValue(C value) {
    this.value = value;
  }

  public boolean isEmpty() {
    assert length >= 0 : "length must not be negative";
    return length == 0;
  }

  public int size() {
    return length;
  }

  protected class NodeIterator implements Iterator<Node<C, V>> {
    private Node<C, V> node = head;

    @SuppressWarnings("unused")
    private boolean justRemoved = false;

    @Override
	public boolean hasNext() {
      return node != null && node.getDown() != head;
    }

    @Override
	public Node<C, V> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      justRemoved = false;

      node = node.getDown();

      return node;
    }

    @Override
	public void remove() {
        throw new UnsupportedOperationException();

        /*if (node == null || node == head || justRemoved) {
          throw new IllegalStateException();
        }
        // TODO to delete single nodes probably doesn't make much sense -> What should be deleted (whole row)?)
        node.getLeft().setRight(node.getRight());
        node.getRight().setLeft(node.getLeft());
        node.getUp().setDown(node.getDown());
        node.getDown().setUp(node.getUp());

        justRemoved = true;*/
    }
  }

  @Override
public Iterator<Node<C, V>> iterator() {
    return new NodeIterator();
  }

  public boolean isPrimary() {
    return !isSecondary();
  }

  public boolean isSecondary() {
    return prev == this && next == this;
  }

  // Special Primary Column

  private Column<C, V> prev; // left neighbor of this column

  private Column<C, V> next; // right neighbor of this column

  public Column<C, V> getPrev() {
    return this.prev;
  }

  public void setPrev(Column<C, V> prev) {
    this.prev = prev;
  }

  public Column<C, V> getNext() {
    return this.next;
  }

  public void setNext(Column<C, V> next) {
    this.next = next;
  }

  public boolean isRoot() {
    return value == null;
  }

  // CONDITION It only works if not more than 2 colums can be removed at once (larger removed parts of connected columns are not recognized)!
  public boolean isRemoved() {
    return prev.getNext() != this || next.getPrev() != this;
  }

  // Special Secondary Column

  private int colorThresh; // used for backing up

  public int getColorThresh() {
    return this.colorThresh;
  }

  public void setColorThresh(int colorThresh) {
    this.colorThresh = colorThresh;
  }

  public boolean isCovered() {
    return covered;
  }

  public void setCovered(boolean covered) {
    this.covered = covered;
  }

}
