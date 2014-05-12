package com.bericotech.clavin.resolver;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.gazetteer.Gazetteer;
import com.bericotech.clavin.gazetteer.QueryBuilder;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolves multipart location names from structured data into GeoName objects.
 *
 * Takes multipart location names, such as what's often found in structured data
 * like a spreadsheet or database table (e.g., [Reston][Virginia][United States]),
 * and resolves them into the appropriate geographic entities by identifying the
 * most logical match in a gazetteer, trying to enforce some kind of notional
 * hierarchy of place names (e.g., city --> state/province/etc. --> country).
 */
public class MultipartLocationResolver {
    /**
     * The logger.
     */
    private final static Logger LOG = LoggerFactory.getLogger(MultipartLocationResolver.class);

    /**
     * The hit depth used during searches.
     */
    private static final int MAX_RESULTS = 200;

    /**
     * The gazetteer for searches.
     */
    private final Gazetteer gazetteer;

    public MultipartLocationResolver(final Gazetteer gaz) {
        this.gazetteer = gaz;
    }

    /**
     * Resolves a multipart location name, such as what's often found
     * in structured data like a spreadsheet or database table (e.g.,
     * [Reston][Virginia][United States]), into a {@link ResolvedMultipartLocation}
     * containing {@link com.bericotech.clavin.gazetteer.GeoName} objects.
     *
     * @param location           multipart location name to be resolved
     * @param fuzzy              switch for turning on/off fuzzy matching
     * @return                   resolved multipart location name
     * @throws ClavinException   if an error occurs while resolving locations
     */
    public ResolvedMultipartLocation resolveMultipartLocation(MultipartLocationName location, boolean fuzzy)
            throws ClavinException {
        // find all component locations in the gazetteer
        QueryBuilder queryBuilder = new QueryBuilder()
                .fuzzy(fuzzy)
                .includeHistorical(true)
                .maxResults(MAX_RESULTS);

        // country query should only include country-like feature codes
        queryBuilder.location(location.getCountry()).addCountryCodes();
        List<ResolvedLocation> countries = gazetteer.getClosestLocations(queryBuilder.build());
        // remove all "countries" that are not considered top-level administrative divisions; this
        // filters out territories that do not contain descendant GeoNames
        Iterator<ResolvedLocation> iter = countries.iterator();
        while (iter.hasNext()) {
            if (!iter.next().getGeoname().isTopLevelAdminDivision()) {
                iter.remove();
            }
        }

        Set<CountryCode> foundCountries = EnumSet.noneOf(CountryCode.class);
        // state query should only include admin-level feature codes with ancestors
        // in the list of located countries
        queryBuilder.location(location.getState()).clearFeatureCodes().addAdminCodes();
        for (ResolvedLocation country : countries) {
            queryBuilder.addParentIds(country.getGeoname().getGeonameID());
            foundCountries.add(country.getGeoname().getPrimaryCountryCode());
        }
        List<ResolvedLocation> states = gazetteer.getClosestLocations(queryBuilder.build());

        // city query should only include city-level feature codes; ancestry is restricted
        // to the discovered states or, if no states were found, the discovered countries or,
        // if neither states nor countries were found, no ancestry restrictions are added and
        // the most populated city will be selected
        queryBuilder.location(location.getCity()).clearFeatureCodes().addCityCodes();
        if (!states.isEmpty()) {
            Set<CountryCode> stateCodes = EnumSet.noneOf(CountryCode.class);
            // only clear the parent ID restrictions if states were found; otherwise
            // we will continue our search based on the existing country restrictions, if any
            queryBuilder.clearParentIds();
            for (ResolvedLocation state : states) {
                // only include the first administrative division found for each target
                // country
                if (!stateCodes.contains(state.getGeoname().getPrimaryCountryCode())) {
                    queryBuilder.addParentIds(state.getGeoname().getGeonameID());
                    stateCodes.add(state.getGeoname().getPrimaryCountryCode());
                }
                // since we are only including one "state" per country, short-circuit
                // the loop if we have added one for each unique country code returned
                // by the countries search
                if (!foundCountries.isEmpty() && foundCountries.equals(stateCodes)) {
                    break;
                }
            }
        }
        List<ResolvedLocation> cities = gazetteer.getClosestLocations(queryBuilder.build());

        // initialize return objects components
        ResolvedLocation finalCity = null;
        ResolvedLocation finalState = null;
        ResolvedLocation finalCountry = null;

        // assume the most populous valid city is the correct one return
        // note: this should be a reasonably safe assumption since we've attempted to enforce the
        // notional hierarchy of given place names (e.g., city --> state/province/etc. --> country)
        // and have therefore weeded out all other matches that don't fit this hierarchy
        if (!cities.isEmpty()) {
            finalCity = cities.get(0);
        }

        if (!states.isEmpty()) {
            // if we couldn't find a valid city, just take the most populous valid state/province/etc.
            if (finalCity == null) {
                finalState = states.get(0);
            } else {
                for (ResolvedLocation state : states) {
                    // select the first state that is an ancestor of the selected city
                    if (finalCity.getGeoname().isDescendantOf(state.getGeoname())) {
                        finalState = state;
                        break;
                    }
                }
            }
        }

        if (!countries.isEmpty()) {
            // use the selected city if available and the selected state if not to identify the selected country
            ResolvedLocation best = finalCity != null ? finalCity : finalState;
            // if neither city nor state was resolved, take the most populous valid country
            if (best == null) {
                finalCountry = countries.get(0);
            } else {
                for (ResolvedLocation country : countries) {
                    // select the first country that is an ancestor of the selected city or state
                    if (best.getGeoname().isDescendantOf(country.getGeoname())) {
                        finalCountry = country;
                        break;
                    }
                }
            }
        }

        return new ResolvedMultipartLocation(finalCity, finalState, finalCountry);
    }
}
