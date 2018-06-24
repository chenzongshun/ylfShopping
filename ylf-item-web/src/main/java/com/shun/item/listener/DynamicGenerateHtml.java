package com.shun.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.shun.item.pojo.Item;
import com.shun.pojo.TbItem;
import com.shun.pojo.TbItemDesc;
import com.shun.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
* @author czs
* @version 创建时间：2018年6月4日 下午10:55:17<br> 
* 每添加一个商品就需要生成一个静态页面
*/
public class DynamicGenerateHtml implements MessageListener {

	@Value("${GENERATE_HEML_PATH}")
	private String GENERATE_HEML_PATH;

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freemarker;
	
	@Override
	public void onMessage(Message message) {
//		try {
//			// 休眠线程1秒钟，等待添加商品的service层提交完添加商品的事务
//			Thread.sleep(1000);
//			// 取出消息中的商品id
//			TextMessage textMessage = (TextMessage) message;
//			long itemId = Long.parseLong(textMessage.getText());
//			// 根据id去数据库查找商品和商品的描述对象
//			TbItem TbItem = itemService.getItemById(itemId);
//			Item item = new Item(TbItem);	// 由于父类没有扩展images方法，所以用子类进行包装扩展
//			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
//			// 加载模板对象
//			Configuration configuration = freemarker.getConfiguration();
//			// 指定编码格式
//			configuration.setDefaultEncoding("utf-8");
//			// 指定模板文件
//			Template template = configuration.getTemplate("item.jsp");
//			// 创建输出流，指定输出的文件目录以及文件名
//			Writer out = new FileWriter(new File(GENERATE_HEML_PATH + itemId + ".html"));
//			// 创建数据集，封装商品数据，等待插入模板
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("item", item);
//			map.put("itemDesc", itemDesc);
//			// 生成静态页面
//			template.process(map, out);
//			// 关闭流
//			out.close();			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
