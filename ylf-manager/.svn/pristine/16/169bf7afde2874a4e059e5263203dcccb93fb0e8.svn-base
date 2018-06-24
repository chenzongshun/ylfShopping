package com.shun.test;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shun.mapper.TbItemMapper;
import com.shun.pojo.TbItem;
import com.shun.pojo.TbItemExample;

/**
* @author czs
* @version 创建时间：2018年5月10日 上午9:45:55 
*/
public class PageHelperTest {
	public static void main(String[] args) {
		// 获取spring容器
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
				
		// 从容器中获得mapper代理对象
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
		
		// 在执行查询之前获得设置分页的条件
		PageHelper.startPage(1, 20);
		
		// 执行查询
		List<TbItem> items = itemMapper.selectByExample(new TbItemExample());
		
		// 获取分页信息PageInfo  总记录数  总页数  当前页码等待
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(items);
		
		System.out.println("总记录数："+pageInfo.getTotal());
		System.out.println("总页数："+pageInfo.getPages());
		System.out.println("当前页码："+pageInfo.getPageNum());
	}
}
