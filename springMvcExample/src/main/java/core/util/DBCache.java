package core.util;

import java.io.Serializable;

public class DBCache extends RedisCache {
	private String cache_key = "DBCACHE::STUDENT::";
	public static DBCache instance() {
		return new DBCache();
	}
	
	public <T extends Serializable> T get(String key) {
		return super.get(this.cache_key + "KEY:" + key);
	}
	
	public void set(String key, Serializable obj, int seconds) {
		super.set(this.cache_key + "KEY:" + key, obj, seconds);
	}
}
