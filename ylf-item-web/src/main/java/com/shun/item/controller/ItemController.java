package com.shun.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shun.item.pojo.Item;
import com.shun.pojo.TbItem;
import com.shun.pojo.TbItemDesc;
import com.shun.service.ItemService;

/**
* @author czs
* @version 创建时间：2018年6月4日 下午1:36:12 
*/
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value="/item/{itemId}")
	public String searchItemById(@PathVariable long itemId, Model model){
		TbItem tbItem = itemService.getItemById(itemId);
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		Item item = new Item(tbItem);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
