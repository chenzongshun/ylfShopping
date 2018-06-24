package com.shun.test;

import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
//import org.junit.Test;
 
/**
* @author czs
* @version 创建时间：2018年3月25日 下午3:08:52 
* Solrj的增删改查，值得幸运的是。。。单机版和集群版的使用的方法是完全一模一样的！，注意url的填写。是ip加端口库加solr/库名
*/
public class Solr单机版和集群版 {
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
	
	
	// String solrAddress = "http://localhost:8080/solr/core1";
//	String solrAddress = "http://localhost:8081/solr/core1";	// 为了能够同时开启两个tomcat，所以把solr索引库的端口改为了8081，加了个1
	String solrAddress = "http://118.89.226.190:8983/solr/shun";// 为了能够同时开启两个tomcat，所以把solr索引库的端口改为了8081，加了个1
	
	//@Test
	// 添加
	public void testAddDocument() throws Exception{
//		SolrClient client = new HttpSolrClient.Builder("localhost:8080/solr/index.html/core1").build();
//		SolrClient client = new HttpSolrClient.Builder(solrAddress).build();
		try {
			Builder builder = new Builder(solrAddress);
			
			HttpSolrClient httpSolrClient = builder.build();
			
			SolrClient client = httpSolrClient;
			
			SolrInputDocument document = new SolrInputDocument();
			//向文档中添加域   第一个参数：域的名称，域的名称必须是在schema.xml中定义的   第二个参数：域的值
			document.setField("id", "12345");
			document.setField("name", "shunss");
			//把document对象添加到索引库中
			client.add(document);
			client.commit();//提交修改
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			new Solr单机版和集群版().testAddDocument();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	//@Test
	// 删除
	public void testDeleteDocument() throws Exception{
		SolrClient client = new HttpSolrClient.Builder(solrAddress).build(); 
		client.deleteById("12345");// 通过id删除
//		client.deleteByQuery("id:12345");// 通过id删除
//		client.deleteByQuery("*:*");// 全部删除
		client.commit();//提交修改
	}
	
	//@Test
	// 查询   条件为："product_catalog_name":"幽默杂货","product_price":18.9,
	// 价格排序  分页 开始行  没页数  高亮  默认域   只查询指定域默认域
	public void testSerchDocument() throws Exception{
		SolrClient client = new HttpSolrClient.Builder(solrAddress).build(); 
		SolrQuery solrParams = new SolrQuery();							// 创建条件对象开始添加条件
//		solrParams.set("q", "product_catalog_name:幽默杂货");
//		solrParams.setQuery("product_catalog_name:幽默杂货"); 				// 设置查询这个域的值为幽默杂货     和上面一样效果
		solrParams.set("df", "item_title");					// 设置了默认域之后下面的分类就可以直接写分类名了
		solrParams.setQuery("手机"); 									// 设置查询这个域的值为幽默杂货     和上面一样效果
//		solrParams.set("fq", "product_price:[10 TO 200]");				// 搜价格区间在10到200的，包括10以及200
//		solrParams.setSort("product_price",ORDER.asc);					// 按照价格来排序
//		solrParams.setSort(new SortClause("product_price",ORDER.asc));	// 一样的按照价格来排序
		solrParams.setStart(0);											// 设置起始页
		solrParams.setRows(5);											// 设置每页显示条数
//		solrParams.setFields("product_name");							// 设置只查名字
//		solrParams.set("fl","product_name,product_price,id");			// 查询只查询"product_name,product_price"这两个域
		solrParams.set("item_title");
		
		solrParams.setHighlight(true);										// 打开高亮的开关
		solrParams.addHighlightField("item_title");						// 设置高亮的域
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
		
//		System.out.println("查询到符合条件的有：" + docList.getNumFound() + " 个记录\n");
		Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();	// 获得高亮的结果也可以说是高亮的容器
		// 高亮容器由一个大Map组成的。最外面的  k id:v 又来一个一个大Map			这个大Map k 域名:v List集合		List集合，由于我们此次只有一个值，所以list.get(0);
		
		for (SolrDocument document : docList) {
			System.out.println("商品ID：" + document.get("id"));
			Map<String, List<String>> map = highlighting.get(document.get("id"));	// 获得id为*的Map
			List<String> list = map.get("item_title");
			if (list != null && list.size() > 0) {
				System.out.println("商品名字：" + list.get(0));
			}else {
				System.out.println("商品名字：" + document.get("item_title"));
			}
			System.out.println("商品价格：" + document.get("item_price"));
			System.out.println("商品分类名称：" + document.get("item_category_name"));
			System.out.println("商品焦点：" + document.get("item_price") + "\n");
		}
	}
	

	
	//@Test 		// 复杂查询备份
	// 查询   条件为："product_catalog_name":"幽默杂货","product_price":18.9,
	// 价格排序  分页 开始行  没页数  高亮  默认域   只查询指定域默认域
	public void testSerchDocument2222() throws Exception{
		SolrClient client = new HttpSolrClient.Builder(solrAddress).build(); 
//		SolrParams solrParams = new SolrQuery();
		SolrQuery solrParams = new SolrQuery();							// 创建条件对象开始添加条件
//		solrParams.set("q", "product_catalog_name:幽默杂货");
//		solrParams.setQuery("product_catalog_name:幽默杂货"); 				// 设置查询这个域的值为幽默杂货     和上面一样效果
		
		solrParams.set("df", "product_catalog_name");					// 设置了默认域之后下面的分类就可以直接写分类名了
		
		solrParams.setQuery("幽默杂货"); 									// 设置查询这个域的值为幽默杂货     和上面一样效果
		solrParams.setQuery("product_name:台灯");							// 搜名字为台灯的
		solrParams.set("fq", "product_price:[10 TO 200]");				// 搜价格区间在10到200的，包括10以及200
		solrParams.setSort("product_price",ORDER.asc);					// 按照价格来排序
//		solrParams.setSort(new SortClause("product_price",ORDER.asc));	// 一样的按照价格来排序
		solrParams.setStart(0);											// 设置起始页
		solrParams.setRows(5);											// 设置每页显示条数
//		solrParams.setFields("product_name");							// 设置只查名字
		solrParams.set("fl","product_name,product_price,id");			// 查询只查询"product_name,product_price"这两个域
		
		solrParams.setHighlight(true);										// 打开高亮的开关
		solrParams.addHighlightField("product_name");						// 设置高亮的域
		solrParams.setHighlightSimplePre("<span style = 'color = red'>");	// 设置高亮的前缀
		solrParams.setHighlightSimplePost("</span>");						// 设置高亮的后缀
		
		QueryResponse query = client.query(solrParams);					// 传入条件约束
		SolrDocumentList docList = query.getResults();					// 执行查询获得查询的结果
//		网页端的结果拷贝下来对着用：  "product_catalog_name":"幽默杂货",   "product_price":18.9, "product_name":"花儿朵朵彩色金属门后挂&nbsp;8钩免钉门背挂钩2066", "id":"1",
		System.out.println("查询到符合条件的有：" + docList.getNumFound() + " 个记录\n");
		
		Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();	// 获得高亮的结果也可以说是高亮的容器
		// 高亮容器由一个大Map组成的。最外面的  k id:v 又来一个一个大Map			这个大Map k 域名:v List集合		List集合，由于我们此次只有一个值，所以list.get(0);
		
		for (SolrDocument document : docList) {
			System.out.println("商品ID：" + document.get("id"));
			System.out.println("商品分类名称：" + document.get("product_catalog_name"));
			System.out.println("商品价格：" + document.get("product_price"));
			System.out.println("商品名字：" + document.get("product_name") + "\n");
			System.out.println("————————————————————高亮容器里面的内容————————————————————");
			Map<String, List<String>> map = highlighting.get(document.get("id"));	// 获得id为*的Map
			List<String> list = map.get("product_name");
			System.out.println("map里面的商品名字为：" + list.get(0) + "\n\n");
		}
	}
}
