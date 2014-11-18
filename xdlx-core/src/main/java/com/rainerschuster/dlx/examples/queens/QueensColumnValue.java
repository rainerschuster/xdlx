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
package com.rainerschuster.dlx.examples.queens;

import java.util.EnumMap;

public class QueensColumnValue extends EnumMap<QueensEnum, Integer> {

    private static final long serialVersionUID = 1L;

    public QueensColumnValue(Class<QueensEnum> keyType) {
        super(keyType);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        for (QueensEnum qe : this.keySet()) {
            String s = "";
            switch (qe) {
            case ROW:
                s = "r";
                break;
            case COLUMN:
                s = "c";
                break;
            case DIAGONAL_A:
                s = "a";
                break;
            case DIAGONAL_B:
                s = "b";
                break;
            default:
                // TODO Exception
                System.err.println("Unrecognized column type!");
                break;
            }
            return s + this.get(qe);
        }
        return null;
    }

}
