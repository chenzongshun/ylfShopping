package com.shun.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import com.shun.common.pojo.ItemList;
import com.shun.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;

/**
* @author czs
* @version 创建时间：2018年6月1日 下午4:14:27<br>
* 当成功插入一个商品到数据库的时候就需要产生一个新的document并插入到服务器创建索引
*/
public class ItemAddMessage implements MessageListener{
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private Builder builder;

	@Override
	public void onMessage(Message message) {
		try {
			// 获取插入成功的商品id，这个id会在这个message里面包装这，所以需要取出来
			TextMessage mess = (TextMessage) message;
			String text = mess.getText();
			long itemId = Long.parseLong(text);
//			long itemId = Long.getLong(text);
			
			// 等待XXService插入商品到数据库提交到数据库之前再进行查找，否则将会出现空指针错误！
			Thread.sleep(500);
			
			// 根据商品id到数据库找到该商品对象
			ItemList item = itemMapper.getItemById(itemId);
			// 创建文档对象并提交到solr服务器
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
