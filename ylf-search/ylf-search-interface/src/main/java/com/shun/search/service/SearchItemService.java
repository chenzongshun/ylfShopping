package com.shun.search.service;

import com.shun.common.pojo.SearchResult;
import com.shun.common.pojo.ylfResult;

/**
* @author czs
* @version 创建时间：2018年5月26日 下午6:50:45 
*/
public interface SearchItemService {

	/**
	 * 导入所有的商品数据到索引库
	 * @return 如果成功就返回这个类的ok()
	 * @throws Exception 
	 */
	ylfResult importAllItems();

	/**
	 * 根据关键字查询商品
	 * @param keywords 关键字
	 * @param currentPage 当前页，需要看哪一页
	 * @param rows 每页条数
	 * @return 返回一个查询的结果集类，它包含了总共页数、总共商品条数、商品的结果集
	 * @throws Exception
	 */
	SearchResult SerchItemByKeywords(String keywords, long currentPage, long rows) throws Exception;
}
