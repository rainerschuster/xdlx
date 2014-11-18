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

public class PrimaryColumn<C, V extends Value<C>> extends Column<C, V> {

    private PrimaryColumn<C, V> prev;

    private PrimaryColumn<C, V> next;

    @Override
    public PrimaryColumn<C, V> getPrev() {
        return this.prev;
    }

    public void setPrev(PrimaryColumn<C, V> prev) {
        this.prev = prev;
    }

    @Override
    public PrimaryColumn<C, V> getNext() {
        return this.next;
    }

    public void setNext(PrimaryColumn<C, V> next) {
        this.next = next;
    }

    @Override
    public boolean isRoot() {
        throw new UnsupportedOperationException();
    }

}
