package com.shun.spContent.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shun.common.pojo.EasyUiDataGridResult;
import com.shun.common.pojo.ylfResult;
import com.shun.common.redis.JedisClient;
import com.shun.common.utils.JackJsonUtils;
import com.shun.common.utils.RedisError;
import com.shun.mapper.TbContentMapper;
import com.shun.pojo.TbContent;
import com.shun.pojo.TbContentExample;
import com.shun.pojo.TbContentExample.Criteria;
import com.shun.spContent.service.ContentService;

/**
* @author czs
* @version 创建时间：2018年5月19日 下午9:57:38 
*/
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private JedisClient jedisClient;

	/**
	 * 这个类中要使用的缓存键，用这个来表示‘首页大广告图片和链接’在redis数据库中的哈希键<br>
	 * 它放在/ylf-spContent-service/src/main/resources/conf/resource.properties文件里
	 */
	@Value(value = "${CONTENT_LIST}")
	private String CONTENT_LIST;

	@Override
	public EasyUiDataGridResult getCategoryContentByCategoryId(long categoryId, long page, long rows) {
		TbContentExample example = new TbContentExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andCategoryIdEqualTo(categoryId);

		// 查询分页信息
		List<TbContent> selectByExample = contentMapper.selectByExample(example);
		// 设置分页数据
		PageHelper.startPage((int) page, (int) rows);

		// 分装pageInfo对象
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(selectByExample);

		// 创建返回对象并分装参数
		EasyUiDataGridResult result = new EasyUiDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(selectByExample);
		return result;
	}

	@Override
	public ylfResult editContentByContentId(TbContent content) {
		// 删除redis的缓存
		try {
			jedisClient.hdel(CONTENT_LIST, content.getId()+"");
		} catch (Exception e) {
			System.out.println(RedisError.error);
			e.printStackTrace();
		}
		if (content.getCreated() == null) {
			content.setCreated(new Date());
		}
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		return new ylfResult(201, "成功", null);
	}

	@Override
	public ylfResult deleteContentByContentIds(String[] ids) {
		for (String id : ids) {
			contentMapper.deleteByPrimaryKey(new Long(id));
			// 删除redis的缓存
			try {
				jedisClient.hdel(CONTENT_LIST, id);
			} catch (Exception e) {
				System.out.println(RedisError.error);
				e.printStackTrace();
			}
		}
		return ylfResult.ok();
	}

	@Override
	public ylfResult saveContent(TbContent content) {
		// 删除redis的缓存
		try {
			jedisClient.hdel(CONTENT_LIST, content.getId() + "");
		} catch (Exception e) {
			System.out.println(RedisError.error);
			e.printStackTrace();
		}
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		return ylfResult.ok();
	}

	@Override
	public List<TbContent> get首页大广告图片和链接(long cONTENT_DAGUANGGAO_IMAGE_LIANJIE) {
		// 首先查缓存，如果缓存中有就直接返回-----由于不能影响正常的业务逻辑，所以用try保护起来，万一缓存代码出错。。。
		try {
			String hget = jedisClient.hget(CONTENT_LIST, cONTENT_DAGUANGGAO_IMAGE_LIANJIE + "");
			if (StringUtils.isNotBlank(hget)) {
				return JackJsonUtils.jsonToList(hget, TbContent.class);
			}
		} catch (Exception e) {
			System.err.println("查找数据库失败，请如果多次出现请检查！");
		}
		// 说明缓存中没有，那么再查数据库
		TbContentExample example = new TbContentExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andCategoryIdEqualTo(cONTENT_DAGUANGGAO_IMAGE_LIANJIE);
		List<TbContent> list = contentMapper.selectByExample(example);
		// 将查到的结果存入缓存-----由于不能影响正常的业务逻辑，所以用try保护起来，万一缓存代码出错。。。
		try {
			jedisClient.hset(CONTENT_LIST, cONTENT_DAGUANGGAO_IMAGE_LIANJIE + "", JackJsonUtils.objectToJson(list));
		} catch (Exception e) {
			System.err.println(RedisError.error);
			e.printStackTrace();
		}
		return list;
	}
}
