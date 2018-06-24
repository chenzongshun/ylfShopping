package com.shun.search.service.impl;

import java.util.List;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shun.common.pojo.ItemList;
import com.shun.common.pojo.SearchResult;
import com.shun.common.pojo.ylfResult;
import com.shun.search.dao.SearchDao;
import com.shun.search.mapper.ItemMapper;
import com.shun.search.service.SearchItemService;

/**
* @author czs
* @version 创建时间：2018年5月26日 下午6:52:58<br> 
* 有关于商品索引的
*/
@Service
public class SearchItemServiceImpl implements SearchItemService {

	// @Value(value="{SolrAddress}")
	// private String SolrAddress; 

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private Builder builder;
	
	@Autowired
	private SearchDao searchDao;

	/**
	 * 在vim /shun/software/solr-7.3.1-home/new_core/conf/managed-schema的</schema>体内增加了如下内容
	--------------------------------------------------------------------------------------------------------
	        <!-- IKAnalyze自定义中文分词r-->
	<fieldType name="text_ik" class="solr.TextField">
	<analyzer class="org.wltea.analyzer.lucene.IKAnalyzer"/>
	</fieldType>
	
	<field name="item_title" type="text_ik" indexed="true" stored="true"/>
	<field name="item_sell_point" type="text_ik" indexed="true" stored="true"/>
	<field name="item_price"  type="plong" indexed="true" stored="true"/>
	<field name="item_image" type="string" indexed="false" stored="true" />
	<field name="item_category_name" type="string" indexed="true" stored="true" />
	
	<field name="item_keywords" type="text_ik" indexed="true" stored="false" multiValued="true"/>
	<copyField source="item_title" dest="item_keywords"/>
	<copyField source="item_sell_point" dest="item_keywords"/>
	<copyField source="item_category_name" dest="item_keywords"/>
	--------------------------------------------------------------------------------------------------------
	 */
	
	/**
	 * 首先查询数据库的所有商品数据，然后建立索引，提交到云服务器的solr索引库中建立索引
	 */
	@Override
	public ylfResult importAllItems() {
		try {
			// 查询所有的商品数据
			List<ItemList> itemList = itemMapper.getItemList();
			int i = 0;
			int count = itemList.size();
			// 循环遍历每个商品对象并存入索引库提交
			for (ItemList item : itemList) {
				// Builder builder = new Builder(SolrAddress);
				HttpSolrClient httpSolrClient = builder.build();
				SolrClient client = httpSolrClient;
				SolrInputDocument document = new SolrInputDocument();
				// 向文档中添加域 第一个参数：域的名称，域的名称必须是在schema.xml中定义的 第二个参数：域的值

				// 把document对象添加到索引库中
				document.setField("id", item.getId());
				// <field name="item_title" type="text_ik" indexed="true"
				// stored="true"/>
				document.setField("item_title", item.getTitle());
				// <field name="item_sell_point" type="text_ik" indexed="true"
				// stored="true"/>
				document.setField("item_sell_point", item.getSell_point());
				// <field name="item_price" type="plong" indexed="true"
				// stored="true"/>
				document.setField("item_price", item.getPrice());
				// <field name="item_image" type="string" indexed="false"
				// stored="true" />
				document.setField("item_image", item.getImage());
				// <field name="item_category_name" type="string" indexed="true"
				// stored="true" />
				document.setField("item_category_name", item.getCategory_name());
				// 
				// <field name="item_keywords" type="text_ik" indexed="true"
				// stored="false" multiValued="true"/>
				// <copyField source="item_title" dest="item_keywords"/>
				// <copyField source="item_sell_point" dest="item_keywords"/>
				// <copyField source="item_category_name" dest="item_keywords"/>

				client.add(document);
				// 提交修改
				client.commit();
				System.err.println("-------------总共 " + count + " 个Item，已完成第：" + ++i + " 个Item的索引导入！-------------");
			}
		} catch (Exception e) {
			return ylfResult.build(500, "数据导入异常！");
		}
		return ylfResult.ok();
	}
	
	
	@Override
	public SearchResult SerchItemByKeywords(String keywords, long currentPage, long rows) throws Exception {
		// 创建查询条件
		SolrQuery solrParams = new SolrQuery();
		// 设置默认查询域
		solrParams.set("df", "item_title");
		// 查询符合条件的
		solrParams.setQuery(keywords);
		// 别忘了设置高亮查询
		solrParams.setHighlight(true);
		solrParams.addHighlightField("item_title");
		solrParams.setHighlightSimplePre("<span style=\"color:red\">");
		solrParams.setHighlightSimplePost("</span>");
		// 设置查询起始页
		if (currentPage <= 0) currentPage = 1;// 安全处理
		solrParams.setStart((int) ((currentPage -1) * rows));
		// 设置查询条数
		solrParams.setRows((int) rows);
		
		// 带着查询条件去dao查询符合条件的商品
		SearchResult searchResult = searchDao.SerchItemByKeywords(solrParams);
		// 目前从dao返回的结果里面只有总记录数和商品List结果集，所以还需要计算总页数
		long totolCount = searchResult.getRecourdCount();
		long pageCount = (totolCount + rows - 1 ) / rows;
		if (pageCount % rows > 0) pageCount++;
		searchResult.setTotalPages(pageCount);
		return searchResult;
	}
	
	

}
