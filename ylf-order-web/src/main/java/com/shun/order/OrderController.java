package com.shun.order;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.shun.cart.service.CartService;
import com.shun.common.pojo.ylfResult;
import com.shun.order.pojo.Item;
import com.shun.order.pojo.OrderInfo;
import com.shun.order.service.OrderService;
import com.shun.pojo.TbItem;
import com.shun.pojo.TbUser;

/**
* @author czs
* @version 创建时间：2018年6月14日 下午8:10:47<br>
* 订单管理 
*/
@Controller
public class OrderController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 显示订单确认页面
	 * @return
	 */
	@RequestMapping(value="/order/order-cart")
	       //localhost:8094/order/order-cart.html
	public String showOrderCart(HttpServletRequest request) {
		// 拦截器那边已经判断过用户是否已经登陆过了
		TbUser user = (TbUser) request.getAttribute("user");
		// 根据用户id设置收货地址列表
		// 使用静态数据
		// 取支付方式列表
		// 获得redis中的购物车
		List<TbItem> cart = cartService.getRedisCart(user.getId());
		// 由于cart里面没有images属性，所以这里包装一下
		List<Item> newCart = new ArrayList<Item>();
		for (TbItem itemOld : cart) {
			Item itemNew = new Item(itemOld);
			newCart.add(itemNew);
		}
		// 返回给jsp
		request.setAttribute("cartList", newCart);
		// 返回逻辑视图
		return "order-cart";
	}
	
	/**
	 * 创建一个新的订单
	 * @param orderInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
		// 取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		// 把用户信息添加到OrderInfo中
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		// 调用服务生成订单
		ylfResult order = orderService.craeteOrder(orderInfo);
		// 如果订单生成成功了就要清空redis中的购物车
		if (order.getStatus() == 200) {
			cartService.cleanCartByUserId(user.getId());
		}
		// 取到订单号
		String orderId = order.getData().toString();
		// 返回逻辑视图
		request.setAttribute("orderId", orderId);
		request.setAttribute("payment", orderInfo.getPayment());
		return "success";
	}
	
}
 