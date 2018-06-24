package com.shun.service.impl;

import java.util.Date;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shun.common.pojo.EasyUiDataGridResult;
import com.shun.common.pojo.ylfResult;
import com.shun.common.redis.JedisClient;
import com.shun.common.redis.JedisClientPool;
import com.shun.common.utils.JackJsonUtils;
import com.shun.common.utils.RedisError;
import com.shun.common.pojo.my生成ID;
import com.shun.mapper.TbItemDescMapper;
import com.shun.mapper.TbItemMapper;
import com.shun.pojo.TbItem;
import com.shun.pojo.TbItemDesc;
import com.shun.pojo.TbItemExample;
import com.shun.pojo.TbItemExample.Criteria;
import com.shun.service.ItemService;

/**
 * @author czs
 * @version 创建时间：2018年5月3日 上午9:33:32
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ActiveMQTopic activeMQTopic;
	
	@Autowired
	private JedisClient jedisClient;

	@Value(value="${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;// 商品内容redis的key前缀
	
	@Value(value="${REDIS_ITEM_TIMEOUT}")
	private Integer REDIS_ITEM_TIMEOUT;

	@Override
	public TbItem getItemById(long itemId) {
		// 先查缓存，如果有直接返回
		try {
			String itemKey = REDIS_ITEM_PRE + ":" + itemId + ":BASE";
			String string = jedisClient.get(itemKey);
			if (StringUtils.isNotBlank(string)) {
				return JackJsonUtils.jsonToPojo(string, TbItem.class);
			}
		} catch (Exception e) {
			System.out.println(RedisError.error);
			e.printStackTrace();
		}
		// 没有查到就找数据库
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		if(item != null){
			// 如果不为空就做redis缓存
			try {
				String itemKey = REDIS_ITEM_PRE + ":" + itemId + ":BASE";
				jedisClient.set(itemKey, JackJsonUtils.objectToJson(item));
				jedisClient.expire(itemKey, REDIS_ITEM_TIMEOUT);
			} catch (Exception e) {
				System.out.println(RedisError.error);
				e.printStackTrace();
			}
			return item;
		}
		return null;
		// 还可以设置查询条件
//		TbItemExample example = new TbItemExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andIdEqualTo(itemId);
//		List<TbItem> resultList = null;
//		try {
//			resultList = itemMapper.selectByExample(example);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (resultList != null && resultList.size() > 0) {
//			return resultList.get(0);
//		}
//		return null;
	}

	@Override
	public EasyUiDataGridResult getItemList(int currentPage, int rows) {
		// 设置分页数据
		PageHelper.startPage(currentPage, rows);
		
		// 查询分页信息
		List<TbItem> items = itemMapper.selectByExample(new TbItemExample());
		
		// 分装pageInfo对象
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(items);
		
		// 创建返回对象并分装参数
		EasyUiDataGridResult result = new EasyUiDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(items);
		return result;
	}

	@Override
	public ylfResult addItem(TbItem item, String desc) {
		
		// 开始封装商品的实体
		final long itemId = my生成ID.genItemId();
		item.setId(itemId);
		// '商品状态，1-正常，2-下架，3-删除',
		item.setStatus((byte) 1);		
		item.setUpdated(new Date());
		item.setCreated(new Date());
		// 保存到数据库
		itemMapper.insert(item);
		
		// 开始封装商品详细表的实体并保存到数据库
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDesc.setItemId(itemId);	// 这个id一定要是商品的id 
		itemDesc.setItemDesc(desc);
		itemDescMapper.insert(itemDesc);
		
		// 发送信息“商品的id”告诉索引库工程，叫它去数据库查找商品并添加索引信息到solr服务器
		jmsTemplate.send(activeMQTopic, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId + "");
			}
		});
		return ylfResult.ok();
	}

	@Override
	public ylfResult 下架(String ids) {
		String[] split = ids.split(",");
		for (String itemId : split) {
			// 根据id拿到商品并设置状态为下架
			TbItem item = getItemById(Long.valueOf(itemId));
			item.setStatus((byte)2);//设置为下架
			
			// 创建条件
			TbItemExample example = new TbItemExample();
			Criteria createCriteria = example.createCriteria();
			createCriteria.andIdEqualTo(Long.valueOf(itemId));
			
			// 执行更新操作
			itemMapper.updateByExample(item, example);
		}
		return ylfResult.ok();
	}

	@Override
	public ylfResult 上架(String ids) {
		String[] split = ids.split(",");
		for (String itemId : split) {
			// 根据id拿到商品并设置状态为上架
			TbItem item = getItemById(Long.valueOf(itemId));
			item.setStatus((byte)1);//设置为上架
			
			// 创建条件
			TbItemExample example = new TbItemExample();
			Criteria createCriteria = example.createCriteria();
			createCriteria.andIdEqualTo(Long.valueOf(itemId));
			
			// 执行更新操作
			itemMapper.updateByExample(item, example);
		}
		return ylfResult.ok();
	}

	@Override
	public ylfResult 删除商品(String ids) {
		String[] split = ids.split(",");
		for (String itemId : split) {
			// 根据id删除对应的商品
			itemMapper.deleteByPrimaryKey(Long.valueOf(itemId));
		}
		return ylfResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		// 先查缓存，如果有直接返回
		try {
			String itemKey = REDIS_ITEM_PRE + ":" + itemId + ":DESC";
			String string = jedisClient.get(itemKey);
			if (StringUtils.isNotBlank(string)) {
				return JackJsonUtils.jsonToPojo(string, TbItemDesc.class);
			}
		} catch (Exception e) {
			System.out.println(RedisError.error);
			e.printStackTrace();
		}
		
		// 没有查到就找数据库
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		// 如果不为空就做redis缓存
		try {
			String itemDescKey = REDIS_ITEM_PRE + ":" + itemId + ":DESC";
			jedisClient.set(itemDescKey, JackJsonUtils.objectToJson(itemDesc));
			jedisClient.expire(itemDescKey, REDIS_ITEM_TIMEOUT);
		} catch (Exception e) {
			System.out.println(RedisError.error);
			e.printStackTrace();
		}
		return itemDesc;
	}
}