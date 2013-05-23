package com.berico.clavin.resolver.impl.lucene;

import java.util.List;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.resolver.impl.LocationNameIndex;

public class LuceneLocationNameIndex implements LocationNameIndex {

	public static final int DEFAULT_LIMIT = 10;
	
	LuceneComponents lucene;
	
	public LuceneLocationNameIndex(LuceneComponents lucene){
		
		this.lucene = lucene;
	}

	@Override
	public List<ResolvedLocation> search(LocationOccurrence occurrence,
			boolean useFuzzy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResolvedLocation> search(LocationOccurrence occurrence,
			int limit, boolean useFuzzy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
