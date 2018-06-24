package com.shun.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shun.common.pojo.ylfResult;
import com.shun.common.redis.JedisClient;
import com.shun.mapper.TbOrderItemMapper;
import com.shun.mapper.TbOrderMapper;
import com.shun.mapper.TbOrderShippingMapper;
import com.shun.order.pojo.OrderInfo;
import com.shun.order.service.OrderService;
import com.shun.pojo.TbOrderItem;
import com.shun.pojo.TbOrderShipping;

/**
* @author czs
* @version 创建时间：2018年6月16日 下午12:12:34 <br>
* 用来
*/
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_ID_GEN_KEY}")// 订单id
	private String ORDER_ID_GEN_KEY;
	
	@Value(value="${ORDER_ID_GEN_KEY_INIT}")// 订单初始化id
	private String ORDER_ID_GEN_KEY_INIT;
	
	@Value(value="${ORDER_DETAIL_ID_GEN_KEY}")// 订单明细生成的key
	private String ORDER_DETAIL_ID_GEN_KEY;

	@Override
	public ylfResult craeteOrder(OrderInfo orderInfo) {
		if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			// 如果不存在订单号key，就创建一个
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_GEN_KEY_INIT);
		}
		// 使用redis的incr生成订单号
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		// 补全orderInfo属性
		orderInfo.setOrderId(orderId);
		// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭  
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		// 插入订单表
		orderMapper.insert(orderInfo);
		// 插入订单明细表
		List<TbOrderItem> items = orderInfo.getOrderItems();
		for (TbOrderItem orderItem : items) {
			String mxId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			// 补全pojo属性
			orderItem.setId(mxId);
			orderItem.setOrderId(orderId);
			// 向明细表插入数据
			orderItemMapper.insert(orderItem);
		}
		// 插入订单物流表
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		// 返回待订单号的返回值
		return ylfResult.ok(orderId);
	}

}
