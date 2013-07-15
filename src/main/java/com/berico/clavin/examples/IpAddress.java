package com.berico.clavin.examples;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.berico.clavin.gazetteer.LatLon;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.Omni;
import com.maxmind.geoip2.record.LocationRecord;

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
 * GeoHash.java
 * 
 *###################################################################*/

/**
 * Represents a IpAddress.
 */
public class IpAddress {

	private String ipaddressValue;
	private LatLon latlonValue;
	
	/**
	 * For serialization purposes.
	 */
	public IpAddress(){}
	
	/**
	 * Initialize with the IpAddress value.
	 * @param hashValue IpAddress value.
	 */
	public IpAddress(String ipaddress){
		
		this.ipaddressValue = ipaddress;
	}

	/**
	 * Get the IpAddress Value.
	 * @return IpAddress Value.
	 */
	public String getIpAddressValue() {
		return ipaddressValue;
	}
	
	/**
	 * Get the LatLon Value of the IpAddress.
	 * @return LatLon value.
	 * @throws GeoIp2Exception 
	 * @throws IOException 
	 */
	public LatLon getLatLonValue() throws IOException, GeoIp2Exception{
		
		if (latlonValue == null){
			
			 DatabaseReader reader = new DatabaseReader(new File("GeoIP/GeoLite2-City.mmdb"));

		     Omni o = reader.omni(InetAddress.getByName(ipaddressValue));
		     LocationRecord l = o.getLocation();
		     
		
			latlonValue = new LatLon(l.getLatitude(), l.getLongitude());
			reader.close();
		}
		
		return latlonValue;
	}
	
}
