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

import java.util.List;

/**
 * The data produced by this generator (resp. the class implementing it) is the
 * basis for the dancing links data structure.
 */
public abstract class DataGenerator<C, V extends Value<C>> {

  public DancingLinksData<C, V> generate() {
    DancingLinksData<C, V> dlData = new DancingLinksData<C, V>();

    List<C> primaryColumns = generatePrimaryColumnValues();
    List<C> secondaryColumns = generateSecondaryColumnValues();
    List<V> values = generateValues();

    dlData.addAllPrimaryColumns(primaryColumns);
    dlData.addAllSecondaryColumns(secondaryColumns);

    for (V value : values) {
      dlData.addRowContinuous(value);
    }

    dlData.setGeneratedPrimaryColumns(primaryColumns);
    dlData.setGeneratedSecondaryColumns(secondaryColumns);
    dlData.setGeneratedValues(values);

    return dlData;
  }

  // public abstract List<Column<C>> generateColumns();
  public abstract List<C> generatePrimaryColumnValues();

  public abstract List<C> generateSecondaryColumnValues();

  public abstract List<V> generateValues();

}
