package com.shun.search.mapper;

import java.util.List;
import com.shun.common.pojo.ItemList;

/**
* @author czs
* @version 创建时间：2018年5月26日 上午10:05:55 
*/
public interface ItemMapper {
	
	/**
	 * 查询数据库的所有商品数据
	 * @return 返回数据库中所有的商品集合
	 */
	public List<ItemList> getItemList();
	
	/**
	 * 根据商品的id查找出商品对象
	 * @param itemId 要查询的商品id
	 * @return 一个双表查询的结果，它包含了商品的id、标题名字、卖点、价格、图片以及商品的分类名称<br>
	 * 如果只返回一个TbItem对象的话，就没有商品的分类
	 */
	public ItemList getItemById(long itemId);
}