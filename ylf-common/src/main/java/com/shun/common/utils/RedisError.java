package com.shun.common.utils;

/**
* @author czs
* @version 创建时间：2018年5月25日 上午8:35:20 
*/
public class RedisError {

	/**
	 * 将显示redis可能错误的配置信息，以方便日后的维护
	 */
	public static String error = "查询或操作数据库失败，如果出现单次可能没问题，如果多次，请检查项目目录下的applicationContext-redis.xml中redis服务器的地址或者端口，检查服务器端redis的redis.conf文件中bind是否为0.0.0.0";
}
