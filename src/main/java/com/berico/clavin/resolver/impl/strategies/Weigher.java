package com.berico.clavin.resolver.impl.strategies;

public interface Weigher<ITEM, CONTEXT> {

	double weigh(ITEM item, CONTEXT context);
	
}
