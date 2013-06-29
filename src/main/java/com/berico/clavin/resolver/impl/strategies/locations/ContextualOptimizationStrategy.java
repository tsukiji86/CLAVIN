package com.berico.clavin.resolver.impl.strategies.locations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.berico.clavin.Options;
import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.gazetteer.CountryCode;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.resolver.impl.LocationCandidateSelectionStrategy;
import com.berico.clavin.util.ListUtils;

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
 * ContextualOptimizationStrategy.java
 * 
 *###################################################################*/

/**
 * This is the original optimization strategy used by CLAVIN to resolve locations.  
 * It attempts to find the "best fit" (combined score) of set of resolved locations
 * instead of weighing each coordinate individually.
 * 
 * This strategy will ignore the occurrence of coordinates as a means of narrowing
 * plain-named locations to a geographic position.
 */
public class ContextualOptimizationStrategy implements LocationCandidateSelectionStrategy {
	
	/**
	 * Represents how much content to consider when resolving the set of locations.
	 */
	public static final int DEFAULT_MAX_CONTEXT_WINDOW = 5;
	public static final String KEY_DEFAULT_MAX_CONTEXT_WINDOW = "resolver.selection.maxContextWindow";
	
	/**
	 * For each candidate list, select the best candidate.
	 * @param allPossibilities Set of candidate lists to sort through.
	 * @param cooccurringCoordinates Coordinates that occurred within the document.
	 * @param options Options to help configure the optimization strategy.
	 * @return Set of the best candidate choices.
	 */
	@Override
	public List<ResolvedLocation> select(
			List<List<ResolvedLocation>> allPossibilities,
			Collection<CoordinateOccurrence<?>> cooccurringCoordinates, 
			Options options)
			throws Exception {
		
		options = (options == null)? new Options() : options;
		
		int maxContextWindow = options.getInt(
				KEY_DEFAULT_MAX_CONTEXT_WINDOW, DEFAULT_MAX_CONTEXT_WINDOW);
		
		if (maxContextWindow <= 1) return pickBestCandidates(allPossibilities);
			
		List<ResolvedLocation> bestCandidates = new ArrayList<ResolvedLocation>();
		
		// Chunk the list and process in pieces to reduce the processing load
		for (List<List<ResolvedLocation>> theseCandidates : ListUtils.chunkifyList(allPossibilities, maxContextWindow)) {
			// select the best match for each location name based
			// based on heuristics
			bestCandidates.addAll(pickBestCandidates(theseCandidates));
		}
		
		return bestCandidates;
	}

	/**
  	 * Uses heuristics to select the best match for each location name
  	 * extracted from a document, choosing from among a list of lists
  	 * of candidate matches.
  	 * 
  	 * Although not guaranteeing an optimal solution (enumerating &
  	 * evaluating each possible combination is too costly), it does a
  	 * decent job of cracking the "Springfield Problem" by selecting
  	 * candidates that would make sense to appear together based on
  	 * common country and admin1 codes (i.e., states or provinces).
  	 * 
  	 * For example, if we also see "Boston" mentioned in a document 
  	 * that contains "Springfield," we'd use this as a clue that we
  	 * ought to choose Springfield, MA over Springfield, IL or
  	 * Springfield, MO.
  	 * 
  	 * TODO: consider lat/lon distance in addition to shared
  	 * 		 CountryCodes and Admin1Codes.
  	 * 
  	 * @param allCandidates	list of lists of candidate matches for locations names
  	 * @return				list of best matches for each location name
  	 */
  	private List<ResolvedLocation> pickBestCandidates(List<List<ResolvedLocation>> allCandidates) {
  		
  		// initialize return object
  		List<ResolvedLocation> bestCandidates = new ArrayList<ResolvedLocation>();
  		
  		// variables used in heuristic matching
  		List<CountryCode> countries;
  		List<String> states;
  		float score;
  		
  		// initial values for variables controlling recursion
  		float newMaxScore = 0;
  		float oldMaxScore = 0;
  		
  		// controls window of Lucene hits for each location considered
  		// context-based heuristic matching, initialized as a "magic
  		// number" of *3* based on tests of the "Springfield Problem"
  		int candidateDepth = 3;
  		
  		// keep searching deeper & deeper for better combinations of
  		// candidate matches, as long as the scores are improving
  		do {
  			// reset the threshold for recursion
  			oldMaxScore = newMaxScore;
  			
  			// loop through all combinations up to the specified depth.
  			// first recursive call for each depth starts at index 0
	  		for (List<ResolvedLocation> combo : generateAllCombos(allCandidates, 0, candidateDepth)) {
	  			
	  			// these lists store the country codes & admin1 codes for each candidate
	  			countries = new ArrayList<CountryCode>();
	  			
	  			states = new ArrayList<String>();
	  			
	  			for (ResolvedLocation location: combo) {
	  				countries.add(location.getPlace().getPrimaryCountryCode());
	  				
	  				String a1 = location.getPlace().getAdministrativeParents().get(0).getName();
	  				
	  				states.add(a1);
	  			}
	  			
	  			// unique-ify the lists to look for common country codes & admin1 codes
	  			countries = new ArrayList<CountryCode>(new HashSet<CountryCode>(countries));
	  			
	  			states = new ArrayList<String>(new HashSet<String>(states));
	  			
	  			// calculate a score for this particular combination based on commonality
	  			// of country codes & admin1 codes, and the cost of searching this deep
	  			// TODO: tune this score calculation!
	  			score = ((float)allCandidates.size() / (countries.size() + states.size())) / candidateDepth;
	  			
	  			/* *********************************************************** 
	  			 * "So, at last we meet for the first time for the last time."
	  			 * 
	  			 * The fact that you're interested enough in CLAVIN to be
	  			 * reading this means we're interested in talking with you.
	  			 * 
	  			 * Are you looking for a job, or are you in need of a
	  			 * customized solution built around CLAVIN?
	  			 * 
	  			 * Drop us a line at clavin@bericotechnologies.com
	  			 * 
	  			 * "What's the matter, Colonel Sandurz? CHICKEN?"
	  			 * **********************************************************/
	  			
	  			// if this is the best we've seen during this loop, update the return value
	  			if (score > newMaxScore) {
	  				newMaxScore = score;
	  				bestCandidates = combo;
	  			}
	  		}
	  		
	  		// search one level deeper in the next loop
	  		candidateDepth++;
	  		
  		} while (newMaxScore > oldMaxScore);
  		// keep searching while the scores are monotonically increasing
  		
  		return bestCandidates;
  	}
	
	/**
  	 * Recursive helper function for
  	 * {@link LocationResolver#pickBestCandidates(List<List<ResolvedLocation>>)}.
  	 * 
  	 * Generates all combinations of candidate matches from each
  	 * location, down to the specified depth through the lists.
  	 * 
  	 * Adapted from:
  	 * http://www.daniweb.com/software-development/java/threads/177956/generating-all-possible-combinations-from-list-of-sublists#post882553
  	 * 
  	 * @param allCandidates	list of lists of candidate matches for all location names
  	 * @param index			keeps track of which location we're working on for recursive calls
  	 * @param depth			max depth into list we're searching during this recursion
  	 * @return				all combinations of candidate matches for each location, down to the specified depth
  	 */
  	List<List<ResolvedLocation>> generateAllCombos(List<List<ResolvedLocation>> allCandidates, int index, int depth) {
  		
		// stopping condition
		if(index == allCandidates.size()) {
			// return a list with an empty list
			List<List<ResolvedLocation>> result = new ArrayList<List<ResolvedLocation>>();
			result.add(new ArrayList<ResolvedLocation>());
			return result;
		}
		
		// initialize return object
		List<List<ResolvedLocation>> result = new ArrayList<List<ResolvedLocation>>();
		
		// recursive call
		List<List<ResolvedLocation>> recursive = generateAllCombos(allCandidates, index+1, depth);
		
		// for each element of the first list of input, up to depth or list size
		for(int j = 0; j < Math.min(allCandidates.get(index).size(), depth); j++) {
			// add the element to all combinations obtained for the rest of the lists
			for(int k = 0; k < recursive.size(); k++) {
				List<ResolvedLocation> newList = new ArrayList<ResolvedLocation>();
				// add element of the first list
				newList.add(allCandidates.get(index).get(j));
				// copy a combination from recursive
				for(ResolvedLocation listItem : recursive.get(k))
					newList.add(listItem);
	            // add new combination to result
				result.add(newList);
			}
		}
		
		return result;
	}
  	
  	/**
  	 * Set the max context window on the Options object
  	 * @param options Options to set on.
  	 * @param maxContextWindow Size of the context window.
  	 */
  	public static void configureMaxContextWindow(Options options, int maxContextWindow){
  		
  		options.put(KEY_DEFAULT_MAX_CONTEXT_WINDOW, Integer.toString(maxContextWindow));
  	}
}
