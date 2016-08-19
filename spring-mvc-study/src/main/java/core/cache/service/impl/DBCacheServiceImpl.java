package core.cache.service.impl;

import core.cache.service.RedisCacheService;

public class DBCacheServiceImpl extends RedisCacheService {
	public static DBCacheServiceImpl instance() {
		return new DBCacheServiceImpl();
	}
}
