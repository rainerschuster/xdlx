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

public class Node<C, V extends Value<C>> implements Iterable<Node<C, V>> {

  private Node<C, V> left;

  private Node<C, V> right;

  private Node<C, V> up;

  private Node<C, V> down;

  private Column<C, V> column;

  private V value;

  private int color;

  private boolean marked;

  public Node<C, V> getLeft() {
    return this.left;
  }

  public void setLeft(Node<C, V> left) {
    this.left = left;
  }

  public Node<C, V> getRight() {
    return this.right;
  }

  public void setRight(Node<C, V> right) {
    this.right = right;
  }

  public Node<C, V> getUp() {
    return this.up;
  }

  public void setUp(Node<C, V> up) {
    this.up = up;
  }

  public Node<C, V> getDown() {
    return this.down;
  }

  public void setDown(Node<C, V> down) {
    this.down = down;
  }

  public Column<C, V> getColumn() {
    return this.column;
  }

  public void setColumn(Column<C, V> column) {
    this.column = column;
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }

  public int getColor() {
    return this.color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public boolean isMarked() {
    return marked;
  }

  public void setMarked(boolean marked) {
    this.marked = marked;
  }

  // FIXME Alternative zu diesem Workaround suchen
  private Node<C, V> myThis() {
    return this;
  }

  protected class RowIterator implements Iterator<Node<C, V>> {
    private Node<C, V> node = myThis();

    @SuppressWarnings("unused")
    private boolean justRemoved = false;

    private boolean firstOutputted = false;

    @Override
	public boolean hasNext() {
      return node != null && (!firstOutputted || node.getRight() != myThis());
    }

    @Override
	public Node<C, V> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      justRemoved = false;

      if (firstOutputted) {
        node = node.getRight();
      } else {
        firstOutputted = true;
      }

      return node;
    }

    @Override
	public void remove() {
        throw new UnsupportedOperationException();

      /*if (node == null || node == myThis() || justRemoved) {
        throw new IllegalStateException();
      }
      // TODO to delete single nodes probably doesn't make much sense -> What should be deleted (whole column)?)
      node.getLeft().setRight(node.getRight());
      node.getRight().setLeft(node.getLeft());
      node.getUp().setDown(node.getDown());
      node.getDown().setUp(node.getUp());

      justRemoved = true;*/
    }
  }

  @Override
public Iterator<Node<C, V>> iterator() {
    return new RowIterator();
  }

}
