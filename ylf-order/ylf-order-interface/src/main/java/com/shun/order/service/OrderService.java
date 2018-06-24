package com.shun.order.service;

import com.shun.common.pojo.ylfResult;
import com.shun.order.pojo.OrderInfo;

/**
* @author czs
* @version 创建时间：2018年6月16日 下午12:06:15 
*/
public interface OrderService {

	/**
	 * 创建一个订单
	 * @param orderInfo 订单对象
	 * @return
	 */
	ylfResult craeteOrder(OrderInfo orderInfo);
}
