package com.shun.spContent.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shun.common.pojo.EasyUiTreeNode;
import com.shun.common.pojo.ylfResult;
import com.shun.mapper.TbContentCategoryMapper;
import com.shun.pojo.TbContentCategory;
import com.shun.pojo.TbContentCategoryExample;
import com.shun.pojo.TbContentCategoryExample.Criteria;
import com.shun.spContent.service.ContentCategoryService;

/**
* @author czs
* @version 创建时间：2018年5月19日 上午11:44:21 
*/
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUiTreeNode> getContentCatTreeNodes(long parentCategoryId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		// 创建查询条件
		Criteria createCriteria = example.createCriteria();
		createCriteria.andParentIdEqualTo(parentCategoryId);
		// 执行查询
		List<TbContentCategory> selectByExample = contentCategoryMapper.selectByExample(example);
		List<EasyUiTreeNode> result = new ArrayList<EasyUiTreeNode>();
		for (TbContentCategory tbContentCategory : selectByExample) {
			EasyUiTreeNode temp = new EasyUiTreeNode();
			temp.setId(tbContentCategory.getId());
			temp.setText(tbContentCategory.getName());
			temp.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			result.add(temp);
		}
		return result;
	}

	@Override
	public ylfResult addContentCategory(long parentId, String name) {
		// 创建一个对象并设置好属性存入到数据库
		TbContentCategory newCategory = new TbContentCategory();
		newCategory.setCreated(new Date());
		newCategory.setIsParent(false);
		newCategory.setName(name);
		newCategory.setParentId(parentId);
		// 默认设置排序就是1
		newCategory.setSortOrder(1);
		// 状态。可选值:1(正常),2(删除)
		newCategory.setStatus(1);
		newCategory.setUpdated(new Date());
		// 插入对象到数据库
		contentCategoryMapper.insert(newCategory);

		// 根据父id查到对象，并把对象的是否为父节点改为true，因为新建的对象在之前这个父对象时可能还是个叶子节点
		TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		// 如果不是父类才更新到数据库
		if (!parentCategory.getIsParent()) {
			parentCategory.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		return ylfResult.ok(newCategory);
	}

	@Override
	public void updateCategoryNameById(long id, String name) {
		// 拿到节点对象
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 直接修改节点名称
		contentCategory.setName(name);
		// 通过id更新节点信息
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
	}

	@Override
	public ylfResult delete(long id) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		// 执行查询，获得该节点下面的所有子节点数
		int countByExample = contentCategoryMapper.countByExample(example);
		if (countByExample > 1) { 
			// 说明该节点下面有多个子节点就不让删
			return ylfResult.build(0, "该节点分类下有多个分类，请逐个删除！");
		} 
		
		// 删除之前拿到父id
		TbContentCategory loddingDelete = contentCategoryMapper.selectByPrimaryKey(id);
		Long parentId = loddingDelete.getParentId();
		
		// 说明选择的是单节点，删除
		contentCategoryMapper.deleteByPrimaryKey(id);
		
		// 删除后把查询当前父分类下面是否还有子分类，如果子分类为0的话那么就要设置isParent为false
		TbContentCategoryExample example3 = new TbContentCategoryExample();
		Criteria createCriteria2 = example3.createCriteria();
		createCriteria2.andParentIdEqualTo(parentId);
		int countByExample2 = contentCategoryMapper.countByExample(example3);
		if (countByExample2 == 0) {
			TbContentCategory selectByPrimaryKey = contentCategoryMapper.selectByPrimaryKey(parentId);
			selectByPrimaryKey.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(selectByPrimaryKey);
		}
		
		return ylfResult.build(1, null);
	}
}
