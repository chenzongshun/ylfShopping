package com.shun.common.redis.old;

/**
 * 将常用的redis操作封装了，它拥有两个实现类，分别是</br>
 * 单机版：com.shun.common.redis.JedisClientPool</br>
 * 集群版：com.shun.common.redis.JedisClientCluster</br>
 * @author 疙瘩陈
 *
 */
public interface OldJedisClient {

	String set(String key, String value);

	String get(String key);

	Boolean exists(String key);

	Long expire(String key, int seconds);

	Long ttl(String key);

	Long incr(String key);

	Long hset(String key, String field, String value);

	String hget(String key, String field);

	Long hdel(String key, String... field);
}
