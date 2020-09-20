package com.adiops.apigateway.account.web.bo.view;

import java.util.LinkedHashMap;
import java.util.Map;

class LRUCache<K, V> extends LinkedHashMap<K, V> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int CACHE_SIZE;

    //NOTE : LinkedHashMap have already given implementation for LRU, this class has just used those method
    //See http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/LinkedHashMap.java#LinkedHashMap

    // LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)
    // accessOrder - to maintain in order of elements from least-recently accessed to most-recently.
    LRUCache(final int sizeIn) {
        super(sizeIn, 0.75F, true);
        this.CACHE_SIZE = sizeIn;
    }

    @Override 
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > this.CACHE_SIZE; 
        /* Returns true if this map should remove its eldest entry. This method is invoked by put and putAll after 
           inserting a new entry into the map. It provides the implementor with the opportunity to remove the eldest 
           entry each time a new one is added. This is useful if the map represents a cache.
        */
    }
}
