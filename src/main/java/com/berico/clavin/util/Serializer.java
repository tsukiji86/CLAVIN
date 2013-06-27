package com.berico.clavin.util;

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
 * Serializer.java
 * 
 *###################################################################*/

/**
 * Defines a really simple contract for serializing and deserializing objects.
 */
public interface Serializer {

	/**
	 * Default serializer used by CLAVIN.
	 */
	static Serializer Default = new JsonSerializer();
	
	/**
	 * Serialize an object to a string.
	 * @param object Object to serialize.
	 * @return String representation of that object.
	 */
	<T> String serialize(T object);
	
	/**
	 * Deserialize an object from a string.
	 * @param content String to deserialize to an object.
	 * @param returnType Type of object that should return.
	 * @return A deserialized object.
	 */
	<T> T deserialize(String content, Class<T> returnType);
	
}
