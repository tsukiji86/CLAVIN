/*
 * Copyright 2014 Berico Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bericotech.clavin.gazetteer;

import com.bericotech.clavin.extractor.LocationOccurrence;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration parameters for querying a Gazetteer.  The
 * {@link QueryBuilder} contains convenience methods to make
 * query construction more readable.
 */
public class GazetteerQuery {
    /**
     * The location occurrence to search for.
     */
    private final LocationOccurrence occurrence;

    /**
     * The maximum number of results.
     */
    private final int maxResults;

    /**
     * Should fuzzy matching be used?
     */
    private final boolean fuzzy;

    /**
     * Should historical locations be included?
     */
    private final boolean includeHistorical;

    /**
     * Searches should be restricted to locations found in at least
     * one of this set of parents.
     */
    private final Set<Integer> parentIds;

    /**
     * Searches should be restricted to locations matching one of
     * these feature codes.
     */
    private final Set<FeatureCode> featureCodes;

    /**
     * Create a new GazetteerQuery.
     * @param occurrence the location occurrence
     * @param maxResults the maximum number of results
     * @param fuzzy <code>true</code> for fuzzy matching
     * @param includeHistorical <code>true</code> to include historical locations
     * @param parentIds the set of parent IDs to restrict the search to; these will be OR'ed
     * @param featureCodes the set of feature codes to restrict the search to; these will be OR'ed
     */
    @SuppressWarnings("unchecked")
    public GazetteerQuery(final LocationOccurrence occurrence, final int maxResults, final boolean fuzzy,
            final boolean includeHistorical, final Set<Integer> parentIds, final Set<FeatureCode> featureCodes) {
        this.occurrence = occurrence;
        this.maxResults = maxResults;
        this.fuzzy = fuzzy;
        this.includeHistorical = includeHistorical;
        this.parentIds = parentIds != null ? new HashSet<Integer>(parentIds) : Collections.EMPTY_SET;
        this.featureCodes = featureCodes != null ? EnumSet.copyOf(featureCodes) : EnumSet.noneOf(FeatureCode.class);
    }

    /**
     * Get the LocationOccurrence to search on.
     * @return the LocationOccurrence to search for
     */
    public LocationOccurrence getOccurrence() {
        return occurrence;
    }

    /**
     * Get the maximum number of results that should be returned by this query.
     * @return the maximum number of search results
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * Should fuzzy matching be used?
     * @return <code>true</code> if fuzzy matching should be used
     */
    public boolean isFuzzy() {
        return fuzzy;
    }

    /**
     * Should historical locations be included in the search results?
     * @return <code>true</code> if historical locations should be included
     */
    public boolean isIncludeHistorical() {
        return includeHistorical;
    }

    /**
     * Get the set of parents that should be used to restrict the search.
     * @return the IDs of the parents that should be used to restrict the search
     */
    public Set<Integer> getParentIds() {
        return Collections.unmodifiableSet(parentIds);
    }

    /**
     * Get the set of FeatureCodes that should be used to restrict the search.
     * @return the set of FeatureCodes that should be used to restrict the search
     */
    public Set<FeatureCode> getFeatureCodes() {
        return Collections.unmodifiableSet(featureCodes);
    }
}
