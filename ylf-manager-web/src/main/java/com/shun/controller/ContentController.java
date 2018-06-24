package com.shun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.shun.common.pojo.EasyUiDataGridResult;
import com.shun.common.pojo.ylfResult;
import com.shun.pojo.TbContent;
import com.shun.spContent.service.ContentService;

/**
* @author czs
* @version 创建时间：2018年5月19日 下午9:46:01 
* 网站内容的控制层
*/
@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;

	/**
	 * 网站内容管理的tree，点击内容的时候会发送请求到这里来查询内容的详情展示到datagrid
	 * @param categoryId
	 * @param page
	 * @param rows
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUiDataGridResult getCategoryContentByCategoryId(long categoryId, long page, long rows) {
		return contentService.getCategoryContentByCategoryId(categoryId, page, rows);
	}
	
	
	/**
	 * 编辑修改网站首页的内容
	 * @param contentId
	 * @return
	 */
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public ylfResult editContentByContentId(TbContent content){
		return contentService.editContentByContentId(content);
	}
	
	/**
	 * 根据ids删除网站内容
	 * @param ids
	 * @return
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public ylfResult deleteContentByContentIds(String[] ids){
		return contentService.deleteContentByContentIds(ids);
	}
	
	/**
	 * jsp会传一个表单对象封装到content对象，通过这个对象来保存一个网站内容
	 * @param ids
	 * @return
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public ylfResult saveContent(TbContent content){
		return contentService.saveContent(content);
	}
}