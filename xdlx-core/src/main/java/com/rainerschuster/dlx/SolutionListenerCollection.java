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

import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for implementers of the
 * {@link com.rainerschuster.dlx.SourcesSolutionEvents} interface. This subclass of
 * {@link ArrayList} assumes that all objects added to it will be of type
 * {@link com.rainerschuster.dlx.SolutionListener}.
 */
public class SolutionListenerCollection<C, V extends Value<C>> extends
        ArrayList<SolutionListener<C, V>> {

    private static final long serialVersionUID = 1L;

    /**
     * Fires a solution event to all listeners.
     *
     * @param count
     *            The (consecutive) number of the solution.
     * @param level
     *            The level at which the solution was found.
     * @param solution
     *            The solution itself.
     */
    public void fireSolution(long count, int level, List<Node<C, V>> solution) {
        for (SolutionListener<C, V> listener : this) {
            listener.onSolution(count, level, solution);
        }
    }

}
