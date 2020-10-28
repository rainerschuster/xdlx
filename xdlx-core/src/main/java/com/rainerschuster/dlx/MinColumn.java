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

import java.util.ArrayList;
import java.util.List;

public class MinColumn<C, V extends Value<C>> extends PrimaryColumn<C, V> {

    private final int min;

    private int currentMax = 0;

    protected List<Node<C, V>> choice = new ArrayList<>();

    public MinColumn(int min) {
        super();
        this.min = min;
    }

    public List<Node<C, V>> getChoice() {
        return choice;
    }

    public int getCurrentMax() {
        return currentMax;
    }

    public void setCurrentMax(int currentMax) {
        this.currentMax = currentMax;
    }

    public int getMin() {
        return min;
    }

}
