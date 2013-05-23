package com.berico.clavin.resolver.impl.strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	 * @return List of the best selections.
	 * @throws Exception
	 */
	public List<ITEM> select(List<List<ITEM>> candidatesList, CONTEXT context) 
			throws Exception {
		
		ArrayList<ITEM> bestSelections = new ArrayList<ITEM>();
		
		for (List<ITEM> candidates : candidatesList){
			
			ITEM bestSelection = 
				makeSelection(candidates, context);
			
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
	 * @return Best Selection or null.
	 */
	protected ITEM makeSelection(List<ITEM> candidates,CONTEXT context){
		
		ITEM bestSelection = null;
		double bestWeight = -1;
		
		if (candidates.size() > 0){
			
			for (ITEM candidate : candidates){
				
				double weight = getWeight(candidate, context);
				
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
	 * @return The numeric score of the selection based on the
	 * weight provided by the weighing algorithms.
	 */
	protected double getWeight(ITEM candidate, CONTEXT context){
		
		double weight = 0;
		
		for (Weigher<ITEM, CONTEXT> weigher : this.weighers){
			
			weight += weigher.weigh(candidate, context);
		}
		
		return weight;
	}
}
