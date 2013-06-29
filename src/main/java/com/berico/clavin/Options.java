package com.berico.clavin;

import java.util.HashMap;
import java.util.Map;

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
 * Options.java
 * 
 *###################################################################*/

/**
 * As the number of extensions to CLAVIN grows, it's going to be very difficult to 
 * maintain the core interfaces without providing a generic way to coach underlying
 * implementations with Configuration Options.  This mechanism, inspired by the
 * way Hadoop configures jobs, should provide the flexibility in providing configuration
 * while allowing backing implementations to become increasingly more complex.
 */
public class Options extends HashMap<String, String> {
	
	private static final long serialVersionUID = -2651858281391010640L;
	
	/**
	 * Get the value of a Long integer property.
	 * @param key Name of the property.
	 * @param defaultValue Default value if it does not exist.
	 * @return Value or default if the property doesn't exist.
	 */
	public long getLong(String key, long defaultValue){
		
		String value = this.get(key);
		
		if (value == null) return defaultValue;
		
		return Long.parseLong(value);
	}
	
	/**
	 * Get the value of an integer property.
	 * @param key Name of the property.
	 * @param defaultValue Default value if it does not exist.
	 * @return Value or default if the property doesn't exist.
	 */
	public int getInt(String key, int defaultValue){
		
		String value = this.get(key);
		
		if (value == null) return defaultValue;
		
		return Integer.parseInt(value);
	}
	
	/**
	 * Get the value of an boolean property.
	 * @param key Name of the property.
	 * @param defaultValue Default value if it does not exist.
	 * @return Value or default if the property doesn't exist.
	 */
	public boolean getBoolean(String key, boolean defaultValue){
		
		String value = this.get(key);
		
		if (value == null) return defaultValue;
		
		return Boolean.parseBoolean(value);
	}
	
	/**
	 * Instantiate the Options object with the default settings.
	 */
	public Options(){ super(); }
	
	/**
	 * Instantiate the options from a Map of <String, String>.
	 * @param settings Settings to start with.
	 */
	public Options(Map<String, String> settings){
		
		this(); // Set defaults.
		this.putAll(settings); // Possibly override them.
	}
	
	/**
	 * Extend one Options object (defaults) with another (overrides).
	 * @param defaults Default set of options.
	 * @param overrides Options to override the defaults.
	 * @return New options object with the correct (overridden) values.
	 */
	public static Options extend(Options defaults, Options overrides){
		
		Options ops = new Options(defaults);
		
		ops.putAll(overrides);
		
		return ops;
	}
}
