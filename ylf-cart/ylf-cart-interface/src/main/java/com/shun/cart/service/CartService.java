package com.shun.cart.service;

import java.util.List;

import com.shun.common.pojo.ylfResult;
import com.shun.pojo.TbItem;

/**
* @author czs
* @version 创建时间：2018年6月13日 下午3:23:55 
*/
public interface CartService {
	
	/**
	 * 添加商品到redis
	 * @param userId 用户id
	 * @param itemId 商品id
	 * @param num 本次添加商品的个数
	 * @return 
	 */
	ylfResult addCartToRedis(long userId, long itemId, int num);
	
	/**
	 * 合并购物车，一个是redis中的购物车，另一个是cookie中的购物车  merge：合并
	 * @param userId 用户id
	 * @param cookieCart cookie中的购物车
	 * @return 
	 */
	ylfResult mergeCart(long userId, List<TbItem> cookieCart);
	
	/**
	 * 根据用户id取出redis中的购物车
	 * @param userId
	 * @return
	 */
	List<TbItem> getRedisCart(long userId);
	
	/**
	 * 修改redis购物车中的商品数量
	 * @param userId 用户id
	 * @param itemId 商品id
	 * @param num 商品数量
	 * @return 
	 */
	ylfResult updateRedisCartItemNum(long userId, long itemId, int num);
	
	/**
	 * 从redis购物车中删除一个商品
	 * @param userId 用户id
	 * @param itemId 商品id
	 * @return
	 */
	ylfResult deleteRedisCartItem(long userId, long itemId);
	
	/**
	 * 根据用户id来清空购物车
	 * @param userId 用户的id
	 * @return 
	 */
	ylfResult cleanCartByUserId(long userId);
}
