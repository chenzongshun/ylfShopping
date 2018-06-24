package com.shun.portal.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.shun.pojo.TbContent;
import com.shun.spContent.service.ContentService;

/**
* @author czs
* @version 创建时间：2018年5月19日 上午8:02:43 
*/
@Controller
public class IndexController {
	
	@Value("${CONTENT_DAGUANGGAO_IMAGE_LIANJIE}") // 这个在配置文件resource.properties里面
	private long CONTENT_DAGUANGGAO_IMAGE_LIANJIE;
	
	@Autowired
	private ContentService contentService;

	/**
	 * 首页导航，这里准备首页中间的大广告图片和链接
	 * @return
	 */
	@RequestMapping("index") // 省略html后缀
	public String index(Model model) {
		List<TbContent> ad1List = contentService.get首页大广告图片和链接(CONTENT_DAGUANGGAO_IMAGE_LIANJIE);
		model.addAttribute("ad1List",ad1List);
		return "index";
	}
}
