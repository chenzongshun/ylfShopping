package com.shun.test.redis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
* @author czs
* @version 创建时间：2018年5月24日 上午11:23:24 
* 测试jedis的单机和集群的连接使用
*/
public class 纯java代码测试单机和集群连接 {

	/**
	 * 服务器地址
	 */
	private final static String host = "118.89.226.190";

	/**
	 * 单机版
	 * @param stringKey 存入键名
	 * @param stringValue 存入键值
	 */
	public static void 单个redis(String stringKey, String stringValue) {
		Jedis jedis = new Jedis(host, 6379);
		jedis.set(stringKey, stringValue);
		System.out.println("创建了键：" + stringKey + "\t\t存入的值：" + jedis.get(stringKey));
		jedis.close();// 最后不要忘记关闭jedis
	}

	/**
	 * 单机版使用连接池
	 * @param stringKey 存入键名
	 * @param stringValue 存入键值
	 */
	public static void 单个redis使用连接池(String stringKey, String stringValue) {
		JedisPool jedisPool = new JedisPool(host, 6379);// 创建一个连接池对象，两个参数host、port
		Jedis jedis = jedisPool.getResource();// 从连接池获得一个连接，就是一个jedis对象。
		jedis.set(stringKey, stringValue);
		System.out.println("创建了键：" + stringKey + "\t\t存入的值：" + jedis.get(stringKey));
		jedis.close();// 关闭连接，每次使用完毕后关闭连接。连接池回收资源。
		jedisPool.close();
	}

	/**
	 * 集群版
	 * @param stringKey 存入键名
	 * @param stringValue 存入键值
	 * @throws IOException
	 */
	public static void 集群redisCluster(String stringKey, String stringValue) throws IOException {
		// 创建一个JedisCluster对象。有一个参数nodes是一个set类型。set中包含若干个HostAndPort对象。
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort(host, 6001));
		nodes.add(new HostAndPort(host, 6002));
		nodes.add(new HostAndPort(host, 6003));
		nodes.add(new HostAndPort(host, 6004));
		nodes.add(new HostAndPort(host, 6005));
		nodes.add(new HostAndPort(host, 6006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set(stringKey, stringValue);
		System.out.println("创建了键：" + stringKey + "\t\t存入的值：" + jedisCluster.get(stringKey));
		jedisCluster.close();// 关闭JedisCluster对象
	}

	public static void main(String[] args) throws IOException {
		单个redis("name", "chenzongshun");
		单个redis使用连接池("age", "19");
		集群redisCluster("data", "2018年5月24日16:06:26");
	}

}