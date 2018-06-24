package com.shun.service;

import com.shun.common.pojo.EasyUiDataGridResult;
import com.shun.common.pojo.ylfResult;
import com.shun.pojo.TbItem;
import com.shun.pojo.TbItemDesc;

/**
* @author czs
* @version 创建时间：2018年5月3日 上午9:31:11 
* 这个类对应了数据库的商品表
*/
public interface ItemService {

	/**
	 * 获得一个商品对象
	 * @param itemId
	 * @return
	 */
	TbItem getItemById(long itemId);

	/**
	 * 为EasyUi的表格准备数据
	 * @param currentPage 当前页
	 * @param rows 数据集
	 * @return
	 */
	EasyUiDataGridResult getItemList(int currentPage, int rows);

	/**
	 * 添加一个商品
	 * @param item 商品的实体类
	 * @param desc 商品的详细描述，详细描述奇葩的不是在一个表里面，而是拆分开的
	 * @return 一个表示类
	 */
	ylfResult addItem(TbItem item, String desc);

	/**
	 * 根据id们下架对应的商品们
	 * @param ids
	 * @return
	 */
	ylfResult 下架(String ids);

	/**
	 * 根据id们上架对应的商品们
	 * @param ids
	 * @return
	 */
	ylfResult 上架(String ids);

	/**
	 * 根据id们删除对应的商品们
	 * @param ids
	 * @return
	 */
	ylfResult 删除商品(String ids);
	
	/**
	 * 根据商品的id查找对应的商品描述
	 * @param itemId 商品的id
	 * @return 商品描述的实例
	 */
	TbItemDesc getItemDescById(long itemId);
}
