package com.shun.common.pojo;

import java.io.Serializable;

/**
 * @author czs 专门为easyUi的tree做的类，类的格式为
 * @version 创建时间：2018年5月12日 上午11:25:00
 */
public class EasyUiTreeNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**		
	 * 树控件内建异步加载模式的支持，用户先创建一个空的树，然后指定一个服务器端，执行检索后动态返回JSON数据来填充树并完成异步请求
	 * 页面需要的格式为：
	 * [{    
	"id": 1,    
	"text": "Node 1",    
	"state": "closed",    
	"children": [{    			这个children可以不需要
	    "id": 11,    
	    "text": "Node 11"   
	},{    
	    "id": 12,    
	    "text": "Node 12"   
	}]    
	},{    
	"id": 2,    
	"text": "Node 2",    
	"state": "closed"   
	}]  
	 */

	// 所以需要以下的属性
	private long id;
	private String text;
	private String state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
