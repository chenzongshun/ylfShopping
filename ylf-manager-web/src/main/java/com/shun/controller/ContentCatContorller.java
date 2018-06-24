package com.shun.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.shun.common.pojo.EasyUiTreeNode;
import com.shun.common.pojo.ylfResult;
import com.shun.spContent.service.ContentCategoryService;

/**
* @author czs
* @version 创建时间：2018年5月19日 下午2:25:35 
* 网站内容分类的控制层 
*/
@Controller
public class ContentCatContorller {
	
	@Autowired
	private ContentCategoryService contentCategoryService;

	/**
	 * 用ztree展示所有的网站分类
	 * @param parentCategoryId
	 * @return
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUiTreeNode> contentCatTreeNodes(@RequestParam(name = "id", defaultValue = "0") long parentCategoryId) {
		return contentCategoryService.getContentCatTreeNodes(parentCategoryId);
	}
	
	/**
	 * 添加一个网站分类节点
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/content/category/create")
	@ResponseBody
	public ylfResult createCategory(long parentId, String name) {
		return contentCategoryService.addContentCategory(parentId, name);
	}
	
	/**
	 * 修改网站分类节点名称
	 * @param id
	 * @param name
	 */
	@RequestMapping("/content/category/update")
	public void updateCategoryNameById(long id, String name){
		contentCategoryService.updateCategoryNameById(id, name);
	}
	
	/**
	 * 通过节点id来删除节点
	 * @param id
	 * @return 
	 */
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public ylfResult delete(long id){
		return contentCategoryService.delete(id);
	}
}
