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

/**
 * A helper class for implementers of the
 * {@link com.rainerschuster.dlx.SourcesSolveEvents} interface. This subclass of
 * {@link ArrayList} assumes that all objects added to it will be of type
 * {@link com.rainerschuster.dlx.SolveListener}.
 */
public class SolveListenerCollection extends ArrayList<SolveListener> {

    private static final long serialVersionUID = 1L;

    /**
     * Fires a solve event to all listeners.
     * 
     * @param level
     *            The current level.
     */
    public void fireSolution(int level) {
        for (SolveListener listener : this) {
            listener.onSolution(level);
        }
    }

}
