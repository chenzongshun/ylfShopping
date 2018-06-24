package com.shun.spContent.service;

import java.util.List;
import com.shun.common.pojo.EasyUiDataGridResult;
import com.shun.common.pojo.ylfResult;
import com.shun.pojo.TbContent;

/**
* @author czs
* @version 创建时间：2018年5月19日 下午9:58:15 
*/
public interface ContentService {

	/**
	 * 网站内容管理的tree，点击内容的时候会发送请求到这里来查询内容的详情展示到datagrid
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUiDataGridResult getCategoryContentByCategoryId(long categoryId, long page, long rows);

	/**
	 * 编辑修改网站首页的内容
	 * @param content 内容id
	 * @return
	 */
	ylfResult editContentByContentId(TbContent content);

	/**
	 * 根据ids删除网站内容
	 * @param ids
	 * @return
	 */
	ylfResult deleteContentByContentIds(String[] ids);

	/**
	 * jsp会传一个表单对象封装到content对象，通过这个对象来保存一个网站内容
	 * @param content
	 * @return
	 */
	ylfResult saveContent(TbContent content);

	/**
	 * 准备首页中间的大广告图片和链接
	 * @param cONTENT_DAGUANGGAO_IMAGE_LIANJIE
	 * @return
	 */
	List<TbContent> get首页大广告图片和链接(long cONTENT_DAGUANGGAO_IMAGE_LIANJIE);

}
