package com.shun.cart.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shun.cart.service.CartService;
import com.shun.common.pojo.ylfResult;
import com.shun.common.utils.CookieUtils;
import com.shun.common.utils.JackJsonUtils;
import com.shun.item.pojo.Item;
import com.shun.pojo.TbItem;
import com.shun.pojo.TbUser;
import com.shun.service.ItemService;

/**
* @author czs
* @version 创建时间：2018年6月8日 下午9:20:20 
*/
@Controller
public class CartController {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CartService cartService;

	@Value("${YLF_CAER_COOKIENAME}")
	private String YLF_CAER_COOKIENAME;

	/**
	 * 添加一个商品
	 * @param itemId 商品id 
	 * @param num 本次添加商品的个数
	 * @param request request
	 * @param response response
	 * @return 逻辑视图
	 */
	@RequestMapping(value = "/cart/add/{itemId}") // http://localhost:8089/cart/add/152809137802196.html?num=1
	public String cartAddItem(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果是登录状态就把购物车写入redis
		if (user != null) {
			// 保存到服务端
			cartService.addCartToRedis(user.getId(), itemId, num);
			// 返回逻辑视图
			return "cartSuccess";
		}
		// 如果没有登录就采用下面的老方案
		
		// 获取cookie中的购物车
		List<TbItem> cart = getCartByCookie(request);
		// 定义一个标志，如果购物车里面有该商品了就给flase
		boolean flg = true;
		// 循环遍历购物车中的商品，看是否存在了
		for (TbItem tbItem : cart) {
			// 购物车中循环的商品的id如果等于传进来的商品id，那么代表购物车中加入过该商品，所以就将商品的数量加num
			if (tbItem.getId().longValue() == itemId.longValue()) {
				flg = false;
				// 说明购物车中有这个商品，那么就要加num
				tbItem.setNum(tbItem.getNum() + num);
				// 既然加了就要提前结束掉不进入就下面的flg
				break;
			}
		}
		// 如果标志还是true的话，说明没有给购物车里面的商品进行加num操作
		if(flg){
			// 购物车中没有该商品就根据itemId查询到商品对象
			TbItem item = itemService.getItemById(itemId);
			// 我的天， 居然忘记给商品在购物车中的数量
			item.setNum(num);
			// 将商品对象对象加入购物车集合
			cart.add(item);
		}
		// 将购物车转换成为json并写给浏览器
		CookieUtils.setCookie(request, response, YLF_CAER_COOKIENAME, JackJsonUtils.objectToJson(cart), true);
		return "cartSuccess";
	}

	/**
	 * 根据HttpServletRequest对象获得浏览器cookie中的Cart购物车
	 * @param request 一个HttpServletRequest对象
	 * @return 一个购物车，其实就是一个商品的集合
	 */
	public List<TbItem> getCartByCookie(HttpServletRequest request) {
		// 根据HttpServletRequest获得cookie中的字符串
		String cart = CookieUtils.getCookieValue(request, YLF_CAER_COOKIENAME, true);
		// 如果是空的说明没有值，直接返回一个ArrayList
		if (StringUtils.isBlank(cart)) {
			return new ArrayList<TbItem>();
		}
		// 将结果通过jsonUtils转换成为List并r返回
		return JackJsonUtils.jsonToList(cart, TbItem.class);
	}
	
	/**
	 * 为购物车页面准备数据，准备数据之后就返回到购物车页面
	 * @param model 
	 * @param request
	 * @param response 
	 * @return 逻辑视图
	 */
	@RequestMapping(value="/cart/cart")
	public String getCart (Model model,HttpServletRequest request, HttpServletResponse response){
		// 取出cookie中购物车中的所有商品
		List<TbItem> cookieCast = getCartByCookie(request);
		// 获取user对象
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果是登录状态
		if (user != null) {
			// 如果不为空，就要和redis中的购物车合并
			cartService.mergeCart(user.getId(), cookieCast);
			// 合并后将cookie中的购物车删除掉
			CookieUtils.deleteCookie(request, response, YLF_CAER_COOKIENAME);
			// 从服务端取购物车列表
			List<TbItem> redisCart = cartService.getRedisCart(user.getId());
			// 覆盖，待会到下面还有操作
			cookieCast = redisCart;
		}
		// 实例化返回的
		List<Item> itemList = new ArrayList<Item>();
		// 一个个去除并添加到itemList
		for (TbItem tbItem : cookieCast) {
			Item item = new Item(tbItem);
			itemList.add(item);
		}
		// 是时候返回了
		model.addAttribute("cartList",itemList);
		return "cart";
	}
	
	/**
	 * 从url中取出需要修改的商品id，和购物车数量，对购物车中的商品进行购买数量的加减操作
	 * @param itemId 商品id
	 * @param num 商品再购物车中的最新数量
	 * @param request request 
	 * @param response response
	 */
	@RequestMapping(value="/cart/update/num/{itemId}/{num}")
	public void ajaxUpdateCartItemNum(@PathVariable Long itemId, @PathVariable Integer num ,HttpServletRequest request, HttpServletResponse response){
		// 判断用户是否登登陆，如果登录就修改redis中的购物车，否则就修改cookie中的购物车
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 调用service修改商品数量
			cartService.updateRedisCartItemNum(user.getId(), itemId, num);
			return;
		}
		// 没有登陆就从cookie中拿到购物车
		List<TbItem> cartByCookie = getCartByCookie(request);
		// 遍历商品修改商品个数
		for (TbItem tbItem : cartByCookie) {
			if (tbItem.getId().longValue() == itemId.longValue()) {
				tbItem.setNum(num);
				break;
			}  
		}
		// 重新将购物车写回cookie
		CookieUtils.setCookie(request, response, YLF_CAER_COOKIENAME, JackJsonUtils.objectToJson(cartByCookie), true);
	}
	
	/**
	 * 从购物车中删除指定的商品
	 * @param itemId 商品id
	 * @param request request
	 * @param response response
	 * @return 逻辑视图
	 */
	@RequestMapping(value="/cart/delete/{itemId}")
	public String deleteCartItemByItemId(@PathVariable long itemId, HttpServletRequest request, HttpServletResponse response){
		// 判断用户是否登录，如果登录就修改redis中的购物车，否则就修改cookie中的购物车
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			// 调用service修改商品数量
			cartService.deleteRedisCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		// 没有登陆从cookie中取出购物车
		List<TbItem> cart = getCartByCookie(request);
		// 遍历删除商品
		for (TbItem tbItem : cart) {
			if (tbItem.getId().longValue() == itemId) {
				cart.remove(tbItem);
				break;
			}
		}
		// 购物车写回cookie
		CookieUtils.setCookie(request, response, YLF_CAER_COOKIENAME, JackJsonUtils.objectToJson(cart), true);
		// 返回逻辑视图
		return "redirect:/cart/cart.html";
		// 加上/就是绝对路径，域名加工程名加...
		// 去掉/就是相对路径
	}
}
