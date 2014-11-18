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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

// TODO QUEST moegliche Condition ist, dass mindestens eine PrimaryColumn vorkommen muss, damit es als Inhalt zaehlt
public class ColumnCollection<C, V extends Value<C>> implements
        Collection<Column<C, V>> {

    private List<PrimaryColumn<C, V>> primaryColumns; // eigtl nur ntig um (derzeit) gelschte Elemente aufzufinden

    private List<SecondaryColumn<C, V>> secondaryColumns;

    private PrimaryColumn<C, V> root;

    private PrimaryColumn<C, V> lastColumn;

    @Override
    public boolean add(Column<C, V> column) {
        if (column instanceof PrimaryColumn) {
            final PrimaryColumn<C, V> myColumn = (PrimaryColumn<C, V>) column;
            if (root == null) {
                root = myColumn;
            } else {
                lastColumn.setNext(myColumn);
                myColumn.setPrev(lastColumn);
            }
            return true;
        }
        if (column instanceof SecondaryColumn) {
            secondaryColumns.add((SecondaryColumn<C, V>) column);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Column<C, V>> columns) {
        for (Column<C, V> column : columns) {
            if (column instanceof PrimaryColumn) {
                final PrimaryColumn<C, V> myColumn = (PrimaryColumn<C, V>) column;
                myColumn.setPrev(lastColumn);
                lastColumn.setNext(myColumn);
                lastColumn = myColumn;
            }
            if (column instanceof SecondaryColumn) {
                secondaryColumns.add((SecondaryColumn<C, V>) column);
            }
        }
        root.setPrev(lastColumn);
        lastColumn.setNext(root);
        return true;
    }

    @Override
    public void clear() {
        root = null;
        primaryColumns.clear();
        secondaryColumns.clear();
    }

    @Override
    public boolean contains(Object arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public int indexOf(Object o) {
        int index = 0;
        for (Iterator<PrimaryColumn<C, V>> iter = primaryColumnIterator(); iter.hasNext();) {
            if (iter.next().equals(o)) {
                return index;
            }
            index++;
        }
        int myIndex = secondaryColumns.indexOf(o);
        if (myIndex > -1) {
            return index + myIndex;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return (root == null || primaryColumns.isEmpty())
                && secondaryColumns.isEmpty();
    }

    @Override
    public Iterator<Column<C, V>> iterator() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean remove(Object o) {
        for (Iterator<PrimaryColumn<C, V>> iter = primaryColumnIterator(); iter.hasNext();) {
            final PrimaryColumn<C, V> column = iter.next();
            if (o.equals(column.getValue())) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int size() {
        int count = 0;
        for (Iterator<PrimaryColumn<C, V>> iter = primaryColumnIterator(); iter.hasNext();) {
            count++;
        }
        count += secondaryColumns.size();
        return count;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public PrimaryColumn<C, V> getRoot() {
        return root;
    }

    public void setRoot(PrimaryColumn<C, V> root) {
        this.root = root;
    }

    protected class PrimaryColumnIterator implements Iterator<PrimaryColumn<C, V>> {
        protected PrimaryColumn<C, V> column = root;

        @Override
        public boolean hasNext() {
            if (root == null) {
                return false;
            }

            return column.getNext() != root;
        }

        @Override
        public PrimaryColumn<C, V> next() {
            if (root == null) {
                return null;
            }

            column = column.getNext();

            return column;
        }

        @Override
        public void remove() {
            // TODO Auto-generated method stub
            if (root != null) {

            }
        }
    }

    public Iterator<PrimaryColumn<C, V>> primaryColumnIterator() {
        return new PrimaryColumnIterator();
    }

}
