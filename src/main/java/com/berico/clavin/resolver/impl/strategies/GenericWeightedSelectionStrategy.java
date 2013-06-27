package com.berico.clavin.resolver.impl.strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
 * GenericWeightedSelectionStrategy.java
 * 
 *###################################################################*/


/**
 * Really simple, reusable weighting strategy for evaluating items.
 *
 * @param <ITEM> Item to weigh
 * @param <CONTEXT> Context to help in the weighing
 */
public class GenericWeightedSelectionStrategy<ITEM, CONTEXT> {

	ArrayList<Weigher<ITEM, CONTEXT>> weighers = new ArrayList<Weigher<ITEM, CONTEXT>>();

	/**
	 * Please ensure you add the weighers you would like to use.
	 */
	public GenericWeightedSelectionStrategy(){}
	
	/**
	 * Initialize the weighted selection strategy with the desired
	 * weighers to use when evaluating ITEM's.
	 * @param weighers Collection of weighers.
	 */
	public GenericWeightedSelectionStrategy(
		Collection<Weigher<ITEM, CONTEXT>> weighers){
		
		this.setWeighers(weighers);
	}
	
	/**
	 * Set the weighers used by the weighted selection strategy.
	 * @param weighers Weighers to set.
	 */
	public void setWeighers(Collection<Weigher<ITEM, CONTEXT>> weighers){
		
		if (weighers != null && weighers.size() > 0){
			
			this.weighers.addAll(weighers);
		}
	}
	
	/**
	 * Select the best candidate from each list of candidates that represent
	 * the best selection for that collection.
	 * @param candidatesList List of Candidate Lists
	 * @param context Context to help weighers.
	 * @param options Options for configuring the weighers
	 * @return List of the best selections.
	 * @throws Exception
	 */
	public List<ITEM> select(List<List<ITEM>> candidatesList, CONTEXT context, Options options) 
			throws Exception {
		
		ArrayList<ITEM> bestSelections = new ArrayList<ITEM>();
		
		for (List<ITEM> candidates : candidatesList){
			
			ITEM bestSelection = 
				makeSelection(candidates, context, options);
			
			if (bestSelection != null){
				
				bestSelections.add(bestSelection);
			}
		}
		
		return bestSelections;
	}
	
	/**
	 * Given a list of Candidates, select the best item.
	 * @param candidates Candidates to choose from.
	 * @param context Context to help coach the weighers.
	 * @param options Options to help configure the weighers.
	 * @return Best Selection or null.
	 */
	protected ITEM makeSelection(List<ITEM> candidates, CONTEXT context, Options options){
		
		ITEM bestSelection = null;
		double bestWeight = -1;
		
		if (candidates.size() > 0){
			
			for (ITEM candidate : candidates){
				
				double weight = getWeight(candidate, context, options);
				
				if (weight > bestWeight){
					
					bestWeight = weight;
					
					bestSelection = candidate;
					
					System.out.println("New Best: " + weight + ", Loc: " + candidate);
				}
			}
		}
		
		
		
		return bestSelection;
	}
	
	/**
	 * Get the overall weight of the candidate ITEM by
	 * taking the sum of weights provided by our weighers.
	 * @param candidate Candidate item to weigh
	 * @param context Addition context for the weighing algorithms.
	 * @param options Options to help configure the weighers.
	 * @return The numeric score of the selection based on the
	 * weight provided by the weighing algorithms.
	 */
	protected double getWeight(ITEM candidate, CONTEXT context, Options options){
		
		double weight = 0;
		
		for (Weigher<ITEM, CONTEXT> weigher : this.weighers){
			
			weight += weigher.weigh(candidate, context, options);
		}
		
		return weight;
	}
}
