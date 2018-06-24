package com.shun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* @author czs
* @version 创建时间：2018年5月9日 下午9:37:08 
*/
@Controller
public class PageController {
	
	/**
	 * 如果直接访问公网那就返回首页
	 * @return
	 */
	@RequestMapping("/")
	public String showIndxx() {
		return "index";
	}
	
	/**
	 * 如果访问的页面路径就直接放回相应的jsp
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
	
}
