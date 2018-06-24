package com.shun.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.shun.common.pojo.SearchResult;
import com.shun.search.service.SearchItemService;

/**
* @author czs
* @version 创建时间：2018年5月27日 下午4:24:13 
*/
@Controller
public class SearchItemController {

	@Value(value="${SEARCH_REQUEST_ROWS}")
	private String SEARCH_REQUEST_ROWS;
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping(value="/search")
	public String searchItemByKeywords(String keyword,@RequestParam(defaultValue="1") long page, Model model) throws Exception{
		// 解决post乱码			tomcat默认编码iso-8859-1，把它转换utf-8
		keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
		
		// 获得结果集
		SearchResult result = searchItemService.SerchItemByKeywords(keyword, page, Long.valueOf(SEARCH_REQUEST_ROWS));
		model.addAttribute("query", keyword);							// 回显搜索关键字
		model.addAttribute("totalPages", result.getRecourdCount());		// 总页数
		model.addAttribute("recourdCount", result.getRecourdCount());	// 符合条件的总记录数
		model.addAttribute("itemList", result.getItemList());			// 结果集
		// 返回逻辑视图

		// 测试全局异常处理类
//		int i = 1/0;
		return "search";
	}
}
