package com.shun.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.shun.common.pojo.EasyUiTreeNode;
import com.shun.service.ItemCatService;

/**
* @author czs 商品分类的控制层
* @version 创建时间：2018年5月12日 下午12:35:40 
*/
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUiTreeNode> itemCatList(@RequestParam(name = "id", defaultValue = "0") Integer parentId) {
		return itemCatService.getItemCatListByNodeParentId(parentId);
	}
}
