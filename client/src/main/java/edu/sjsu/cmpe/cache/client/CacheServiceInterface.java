package edu.sjsu.cmpe.cache.client;

import org.json.JSONArray;

/**
 * Cache Service Interface
 * 
 */
public interface CacheServiceInterface {
    public String get(long key);
    public JSONArray getAll();
    public void put(long key, String value);
    
    
}
