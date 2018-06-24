package com.shun.order.pojo;

import java.io.Serializable;
import java.util.List;

import com.shun.pojo.TbOrder;
import com.shun.pojo.TbOrderItem;
import com.shun.pojo.TbOrderShipping;

/** 订单对象
* @author czs
* @version 创建时间：2018年6月16日 上午11:59:28<br>
* 这个类是TbOrder、TbOrderItem、TbOrderShipping这三个类的包装类<br>
* 它继承自Order订单表<br>
* 新增了TbOrderItem的集合，因为订单里面有更多个商品，新增了TbOrderShipping物流表这个属性<br>
*/
public class OrderInfo extends TbOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TbOrderItem> orderItems;
	private TbOrderShipping orderShipping;

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}
