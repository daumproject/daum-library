/*
 * Copyright 20010-2012 the original author or authors.
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
package org.daum.library.javase.tileServer;
import java.io.IOException;

/**
 *  ITileCache interface. Implement to create new tile caches.
 *  
 * @author Jose Luis Martin
 */

public interface ITileCache {
	/**
	 * Create a Tile from URI
	 * @param uri the URI to get the tile
	 * @return a new Tile
	 */
	Tile getTile(String uri);
	
	/**
	 * Cache the tile
	 * @param tile Tile to cache
	 * @throws java.io.IOException on store errors
	 */
	void storeTile(Tile tile) throws IOException;


	/**
	 * Gets the server url that it is caching
	 */
	String getServerUrl();

	/**
	 * Gets server url that it is caching for this query string
	 * @param query request part after the configured cachePath including query string
	 */
	String getServerUrl(String query);

    void setPathCache(String pathCache);
}
