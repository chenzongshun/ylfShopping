package com.shun.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.shun.cart.service.CartService;
import com.shun.common.pojo.ylfResult;
import com.shun.common.redis.JedisClient;
import com.shun.common.utils.JackJsonUtils;
import com.shun.mapper.TbItemMapper;
import com.shun.pojo.TbItem;

/**
* @author czs
* @version 创建时间：2018年6月13日 下午3:25:54 
*/
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_CAET_NAME}")
	private String REDIS_CAET_NAME;

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public ylfResult addCartToRedis(long userId, long itemId, int num) {
		// 定义哈希key的名称---->由REDIS_CAET_NAME加上用户的id作为哈希key的键名
		String redisCartHashKey = REDIS_CAET_NAME + ":" + userId;
		// 定义key的域名-------->哈希key中有很多的域名也可以叫做是key，这个key下面存的才是真正的value
		String redisCartHashField = Long.toString(itemId);
		// 向redis中添加购物车
		// 数据类型是hash key：用户id field：商品id value：商品信息
		// 判断商品是否存在
		Boolean hexists = jedisClient.hexists(redisCartHashKey, redisCartHashField);
		// 如果商品存在就把商品取出来加个数
		if (hexists) {
			String itemJson = jedisClient.hget(redisCartHashKey, redisCartHashField);
			// 获得商品对象
			TbItem item = JackJsonUtils.jsonToPojo(itemJson, TbItem.class);
			item.setNum(item.getNum() + num);
			// 写回redis
			jedisClient.hset(redisCartHashKey, redisCartHashField, JackJsonUtils.objectToJson(item));
			return ylfResult.ok();
		}
		// 如果不存在就要根据商品id取得商品对象添加到购物车列表
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		// 设置购物车数量
		item.setNum(num);
		// 取一张图片
		String image = item.getImage();
		if (StringUtils.isNoneBlank(image)) {
			item.setImage(item.getImage().split(",")[0]);
		}
		// 写回redis
		jedisClient.hset(redisCartHashKey, redisCartHashField, JackJsonUtils.objectToJson(item));
		// 返回成功
		return ylfResult.ok();
	}

	@Override
	public ylfResult mergeCart(long userId, List<TbItem> cookieCart) {
		// 遍历商品列表
		// 把列表添加到购物车
		// 如果购物车中有此商品就相加
		// 如果没有商品就添加新的商品
		for (TbItem tbItem : cookieCart) {
			// 直接调用上面的方法
			addCartToRedis(userId, tbItem.getId(), tbItem.getNum());
		}
		// 返回成功
		return ylfResult.ok();
	}

	@Override
	public List<TbItem> getRedisCart(long userId) {
		// 根据用户id查询购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CAET_NAME + ":" + userId);
		List<TbItem> itemList = new ArrayList<TbItem>();
		// 循环jsonList转换成为商品对象并添加到新的List中
		for (String json : jsonList) {
			TbItem item = JackJsonUtils.jsonToPojo(json, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public ylfResult updateRedisCartItemNum(long userId, long itemId, int num) {
		// 定义哈希key的名称---->由REDIS_CAET_NAME加上用户的id作为哈希key的键名
		String redisCartHashKey = REDIS_CAET_NAME + ":" + userId;
		// 定义key的域名-------->哈希key中有很多的域名也可以叫做是key，这个key下面存的才是真正的value
		String redisCartHashField = Long.toString(itemId);
		// 通过用户上面定义的key和域名拿到购物车中的某个商品对象
		String json = jedisClient.hget(redisCartHashKey, redisCartHashField);
		TbItem item = JackJsonUtils.jsonToPojo(json, TbItem.class);
		// 设置商品对象的数量为Controller层给的数量
		item.setNum(num);
		// 写回redis
		jedisClient.hset(redisCartHashKey, redisCartHashField, JackJsonUtils.objectToJson(item));
		return ylfResult.ok();
	}

	@Override
	public ylfResult deleteRedisCartItem(long userId, long itemId) {
		// 定义哈希key的名称---->由REDIS_CAET_NAME加上用户的id作为哈希key的键名
		String redisCartHashKey = REDIS_CAET_NAME + ":" + userId;
		// 定义key的域名-------->哈希key中有很多的域名也可以叫做是key，这个key下面存的才是真正的value
		String redisCartHashField = Long.toString(itemId);
		// 通过用户上面定义的key和域名删除购物车中的某个商品对象
		jedisClient.hdel(redisCartHashKey, redisCartHashField);
		return ylfResult.ok();
	}

	@Override
	public ylfResult cleanCartByUserId(long userId) {
		// 定义哈希key的名称---->由REDIS_CAET_NAME加上用户的id作为哈希key的键名
		String redisCartHashKey = REDIS_CAET_NAME + ":" + userId;
		jedisClient.del(redisCartHashKey);
		return ylfResult.ok();
	}

}
