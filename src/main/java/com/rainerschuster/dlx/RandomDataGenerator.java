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

import java.util.Collections;
import java.util.List;

public abstract class RandomDataGenerator<C, V extends Value<C>> extends DataGenerator<C, V> {

	private boolean random = true;

	public boolean isRandom() {
		return this.random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	@Override
	public List<V> generateValues() {
		List<V> myValues = generateMyValues();
		Collections.shuffle(myValues);
		return myValues;
	}
	
	public abstract List<V> generateMyValues();

}
