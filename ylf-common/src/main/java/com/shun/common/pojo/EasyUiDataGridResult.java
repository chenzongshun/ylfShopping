package com.shun.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author czs
 * @version 创建时间：2018年5月11日 上午9:42:59 如类名，专门为EasyUi的Datagrid准备数据
 */
public class EasyUiDataGridResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long total;
	
	private List<?> rows;

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}