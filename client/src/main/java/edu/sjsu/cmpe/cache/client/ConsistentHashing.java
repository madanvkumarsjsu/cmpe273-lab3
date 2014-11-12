package edu.sjsu.cmpe.cache.client;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class ConsistentHashing {

	private final HashFunction hashFunc;
	private final SortedMap<Integer, CacheServiceInterface> cacheServerMap = new TreeMap<Integer, CacheServiceInterface>();
	private static int iNumberOfNodes = 0;
	Charset charset = Charset.forName("UTF-8");
	public ConsistentHashing(ArrayList<CacheServiceInterface> nodes){
		this.hashFunc = Hashing.md5();
		for (CacheServiceInterface node : nodes) {
			add(node);
		}
	}
	public void add(CacheServiceInterface node) {
		int key = hashFunc.hashInt(iNumberOfNodes).hashCode();
		cacheServerMap.put(key, node);
		iNumberOfNodes++;
	}

//	public void remove(CacheServiceInterface node) {
//		cacheServerMap.remove(hashFunc.hashInt(iNumberOfNodes));     //Removes last added node
//		iNumberOfNodes--;
////		for (int i = 0; i < iNumberOfNodes; i++) {
////			cacheServerMap.remove(hashFunc.hashString(node.toString()+iNumberOfNodes, charset).hashCode());
////		}
////		iNumberOfNodes--;
//	}

	public CacheServiceInterface get(int key) {
		if (cacheServerMap.isEmpty()) {
			return null;
		}
		int temp = key % iNumberOfNodes;
		int hash = hashFunc.hashInt(temp).hashCode();
		if (!cacheServerMap.containsKey(hash)) {
			SortedMap<Integer, CacheServiceInterface> tailMap =
					cacheServerMap.tailMap(hash);
			hash = tailMap.isEmpty() ?
					cacheServerMap.firstKey() : tailMap.firstKey();
		}
		return cacheServerMap.get(hash);
	}
}
