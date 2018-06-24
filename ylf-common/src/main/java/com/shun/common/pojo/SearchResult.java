package com.shun.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
* @author czs
* @version 创建时间：2018年5月27日 下午2:12:12 
* 商城首页进行搜索的时候，返回的结果集列表就是这，它包含了总共页数、总共商品条数、商品的结果集
*/
public class SearchResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private long totalPages;				// 总共页数
	private long recourdCount; 				// 总共商品条数
	private List<ItemList> itemList; 		// 商品结果集

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public long getRecourdCount() {
		return recourdCount;
	}

	public void setRecourdCount(long recourdCount) {
		this.recourdCount = recourdCount;
	}

	public List<ItemList> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemList> itemList) {
		this.itemList = itemList;
	}
}
