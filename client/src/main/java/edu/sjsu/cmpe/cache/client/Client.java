package edu.sjsu.cmpe.cache.client;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class Client {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting Cache Client...");
		String val; 
		CacheServiceInterface cachetry; 
		ArrayList<CacheServiceInterface> servers = new ArrayList<CacheServiceInterface>();
		Charset charset = Charset.forName("UTF-8");

		CacheServiceInterface cacheServ1 = new DistributedCacheService(
				"http://localhost:3000");
		CacheServiceInterface cacheServ2 = new DistributedCacheService(
				"http://localhost:3001");
		CacheServiceInterface cacheServ3 = new DistributedCacheService(
				"http://localhost:3002");
		servers.add(cacheServ1);
		servers.add(cacheServ2);
		servers.add(cacheServ3);
		//Using google guava lib 
		//		for(int i = 1;i<=10;i++)
		//		{
		//			
		//			int cacheBucket = Hashing.consistentHash(Hashing.md5().hashString(i+"", charset), servers.size());
		//			System.out.println(cacheBucket);
		//			cachetry = servers.get(cacheBucket);
		//			String val = Character.toString((char)(64+i)); 
		//			
		//			cachetry.put(i,val);
		//			System.out.println("bucket of i:"+i+" "+cachetry.get(i));
		//		}
		ConsistentHashing consistentHash = new ConsistentHashing(servers);
		for(CacheServiceInterface cacheServ : servers)
			consistentHash.add(cacheServ);

		for(int i = 1;i<=10;i++)
		{
			val = Character.toString((char)(96+i)); 
			cachetry = consistentHash.get(i);
			cachetry.put(i, val);
		}
		System.out.println("Fetching cached values using keys");
		for(int j=1; j<=10;j++){
			cachetry = consistentHash.get(j);
			System.out.println(j+" => "+cachetry.get(j));
		}
		System.out.println("Fetching all key values from server:");
		for(CacheServiceInterface cs : servers)
			System.out.println(cs.getAll());

		System.out.println("Existing Cache Client...");
	}

}
