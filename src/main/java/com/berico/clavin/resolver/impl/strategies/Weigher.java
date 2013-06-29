package com.berico.clavin.resolver.impl.strategies;

import com.berico.clavin.Options;

/*#####################################################################
 * 
 * CLAVIN (Cartographic Location And Vicinity INdexer)
 * ---------------------------------------------------
 * 
 * Copyright (C) 2012-2013 Berico Technologies
 * http://clavin.bericotechnologies.com
 * 
 * ====================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * ====================================================================
 * 
 * Weigher.java
 * 
 *###################################################################*/

/**
 * Given an ITEM and some CONTEXT, provide a numeric weight.
 * 
 * This can be used to reward (positive values) or punish (negative)
 * items.  Keep in mind the value of the weight should be considered in
 * the context of the overall weighting system.
 *
 * @param <ITEM>
 * @param <CONTEXT>
 */
public interface Weigher<ITEM, CONTEXT> {

	/**
	 * Given an item and some context, provide a weight for the value.
	 * @param item Item to weigh
	 * @param context Extra context to help in weighing
	 * @param options Options to help configure the weigher
	 * @return Weight for the item
	 */
	double weigh(ITEM item, CONTEXT context, Options options);
	
}
