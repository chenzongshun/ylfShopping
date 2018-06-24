package com.shun.service.impl;
 
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shun.common.pojo.EasyUiTreeNode;
import com.shun.mapper.TbItemCatMapper;
import com.shun.pojo.TbItemCat;
import com.shun.pojo.TbItemCatExample;
import com.shun.pojo.TbItemCatExample.Criteria;
import com.shun.service.ItemCatService;

/**
* @author czs
* @version 创建时间：2018年5月12日 上午11:41:54 
*/
@Service // 注意一定要导入spring的service，别导错了！
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	TbItemCatMapper itemCatMapper;

	@Override
	public List<EasyUiTreeNode> getItemCatListByNodeParentId(long parentId) {
		// 根据父id查询所有的子节点
		TbItemCatExample example = new TbItemCatExample();
		// 创建查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 查询获得所有的子节点
		List<TbItemCat> itemCats = itemCatMapper.selectByExample(example);
		// 创建一个节点集合对象，待会是要进行封装返回的
		List<EasyUiTreeNode> nodes = new ArrayList<EasyUiTreeNode>();
		// 循环所有的子节点并添加到节点集合对象
		for (TbItemCat itemCat : itemCats) {
			// 创建一个节点对象并进行封装
			EasyUiTreeNode node = new EasyUiTreeNode();
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			node.setState(itemCat.getIsParent() ? "closed" : "open");
			// 别忘了添加到返回结果集里面去
			nodes.add(node);
		}
		// 返回节点集合对象
		return nodes;
	}

}
