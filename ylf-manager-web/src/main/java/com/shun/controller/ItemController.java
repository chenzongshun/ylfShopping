package com.shun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shun.common.pojo.EasyUiDataGridResult;
import com.shun.common.pojo.ylfResult;
import com.shun.pojo.TbItem;
import com.shun.search.service.SearchItemService;
import com.shun.service.ItemService;

/**
* @author czs
* @version 创建时间：2018年5月3日 上午9:43:56
* 商品的控制层 
*/
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private SearchItemService searchItemService;
	
	/**
	 * 根据商品的id获取商品对象
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/{itemId}")		// 映射路径
	@ResponseBody/*
	一般在异步获取数据时使用，在使用@RequestMapping后，返回值通常解析为跳转路径
	加上@responsebody后返回结果不会被解析为跳转路径
	而是直接写入HTTP response body中。比如异步获取json数据
	加上@responsebody后，会直接返回json数据。*/
	public TbItem getItemById(@PathVariable Long itemId){
		System.out.println(itemId);
		TbItem item = itemService.getItemById(itemId);
		return item;
	}
	
	/**
	 * 分页商品列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUiDataGridResult getItems(Integer page, Integer rows) {
		return itemService.getItemList(page, rows);
	}
	
	/**
	 * 添加一个商品
	 * @param item 
	 * @param desc 
	 * @return
	 */
	@RequestMapping(value = "/item/save", method = RequestMethod.POST)
	@ResponseBody
	public ylfResult saveItem(TbItem item, String desc) {
		return itemService.addItem(item, desc);
	}
	
	@RequestMapping("/rest/item/param/item/query/{itemId}")
	@ResponseBody
	public TbItem editItem(Long itemId) {
		return getItemById(itemId);
	}
	
	/**
	 * 根据id们下架对应的商品们
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public ylfResult 下架(String ids) {
		return itemService.下架(ids);
	}
	
	/**
	 * 根据id们上架对应的商品们
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public ylfResult 上架(String ids) {
		return itemService.上架(ids);
	}
	
	/**
	 * 根据id们上架对应的商品们
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public ylfResult 删除商品(String ids) {
		return itemService.删除商品(ids);
	}
	
	/**
	 * 编辑商品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/page/item-edit")
	@ResponseBody
	public void 编辑() {
		System.out.println("进来");
	}
	
	/**
	 * 导入所有的商品数据到索引库
	 * @return
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public ylfResult importItemsAsIndexs() {
		return searchItemService.importAllItems();
	}
	
	
}
