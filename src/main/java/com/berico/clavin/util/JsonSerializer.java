package com.berico.clavin.util;

import com.google.gson.Gson;

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
 * JsonSerializer.java
 * 
 *###################################################################*/

/**
 * A dead simple and fast way to serialize and deserialize objects.
 */
public class JsonSerializer implements Serializer {

	Gson gson = new Gson();
	
	/**
	 * Instantiate!
	 */
	public JsonSerializer(){}
	
	/**
	 * Instantiate with a configured instance of Gson.
	 * @param gson Preconfigured Gson instance.
	 */
	public JsonSerializer(Gson gson){
		this.gson = gson;
	}
	
	/**
	 * Serialize an object to a JSON String.
	 * @param object Object to serialize.
	 * @return Object as JSON String.
	 */
	@Override
	public <T> String serialize(T object) {
		
		return gson.toJson(object);
	}

	/**
	 * Deserialize Json string to an object of desired type (you must know the type
	 * ahead of time!!!!).
	 * @param content Content to deserialize.
	 * @param returnType The object type the JSON should be deserialize to.
	 * @return Deserialized object or null.
	 */
	@Override
	public <T> T deserialize(String content, Class<T> returnType) {
		
		return gson.fromJson(content, returnType);
	}

}
