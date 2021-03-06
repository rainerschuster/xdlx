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

/**
 * A class that implements this interface sources the events defined by the
 * {@link com.rainerschuster.dlx.SolutionListener} interface.
 */
public interface SourcesSolutionEvents<C, V extends Value<C>> {

    /**
     * Adds a listener interface to receive solution events.
     * 
     * @param listener
     *            The listener interface to add.
     */
    void addSolutionListener(SolutionListener<C, V> listener);

    /**
     * Removes a previously added listener interface.
     * 
     * @param listener
     *            The listener interface to remove.
     */
    void removeSolutionListener(SolutionListener<C, V> listener);

}
