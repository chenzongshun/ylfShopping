package com.shun.search.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.shun.common.pojo.ItemList;
import com.shun.common.pojo.SearchResult;

/**
* @author czs
* @version 创建时间：2018年5月27日 下午2:17:38 <br>
* 没有写接口的原因<br>
* 并没有像redis的数据层一样有多个实现类，如：连接单机版的实现类，连接集群版的实现类。<br>
* 这只是大家写习惯了？也并没有像Service一样Dubbo发布服务<br>
* 所以这里可以怎么快怎么来，这里就不写接口了，证明不写接口也是可以完成开发的
*/
@Repository
public class SearchDao {
	
	@Autowired
	private Builder builder;

	/**
	 * 根据关键字搜索类似关键字的商品
	 * @param solrParams solr的条件
	 * @return 
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public SearchResult SerchItemByKeywords(SolrQuery solrParams) throws SolrServerException, IOException{
		HttpSolrClient client = builder.build();
		
		solrParams.setHighlight(true);										// 打开高亮的开关
		solrParams.addHighlightField("item_title");							// 设置高亮的域
		solrParams.setHighlightSimplePre("<span style = 'color = red'>");	// 设置高亮的前缀
		solrParams.setHighlightSimplePost("</span>");						// 设置高亮的后缀
		
		QueryResponse query = client.query(solrParams);					// 传入条件约束
		SolrDocumentList docList = query.getResults();					// 执行查询获得查询的结果
/*		网页端的结果拷贝下来对着用：  {
        "id":"635906",
        "item_title":"阿尔卡特 (OT-927) 单电版 炭黑 联通3G手机 双卡双待",
        "item_sell_point":"清仓！仅北京，武汉仓有货！",
        "item_price":24900,
        "item_image":"http://image.e3mall.cn/jd/9c1fcdf2bf20450788195c707da00a87.jpg",
        "item_category_name":"手机",
        "_version_":1601588887797366784},*/
		
		Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();	// 获得高亮的结果也可以说是高亮的容器
		// 高亮容器由一个大Map组成的。最外面的  k id:v 又来一个一个大Map			这个大Map k 域名:v List集合		List集合，由于我们此次只有一个值，所以list.get(0);
		
		SearchResult result = new SearchResult();						// 实例化返回结果集封装后准备返回给service 
		result.setRecourdCount(docList.getNumFound());					// 总商品个数
		List<ItemList> itemLists = new ArrayList<ItemList>();			// 实例化商品结果集并封装准备在方法体尾部加入到返回结果集
		
		for (SolrDocument document : docList) {
			
			ItemList item = new ItemList();
			item.setId(document.get("id").toString());				// 商品id
			// 取高亮
			String title = "";
			Map<String, List<String>> map = highlighting.get(document.get("id"));	// 获得id为*的Map
			List<String> list = map.get("item_title");
			if (list != null && list.size() > 0) {
				title = list.get(0);
			}else {
				title = document.get("item_title").toString();
			}
			item.setTitle(title);										// 设置标题
			item.setPrice((long) document.get("item_price"));			// 设置价格
			item.setCategory_name(document.get("item_category_name").toString());	//设置分类名称 
			item.setImage(document.get("item_image").toString());
			itemLists.add(item);
		}
		result.setItemList(itemLists);
		return result;
	}
}
