package com.shun.common.pojo;

import java.io.Serializable;

/**
* @author czs
* @version 创建时间：2018年5月26日 上午10:31:20<br> 
* 有关于索引库的类对应的sql语句为<br>
* SELECT  a.id,  a.title,  a.sell_point,  a.price,  a.image,  b.name category_namefrom  tb_item a  LEFT JOIN tb_item_cat b    ON a.cid = b.id<br>
* 它包含了商品的id、标题名字、卖点、价格、图片以及商品的分类名称
*/
public class ItemList implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String sell_point;
	private long price;
	private String image;
	private String category_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSell_point() {
		return sell_point;
	}

	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String[] getImages() {
		if (image !=null && !image.equals("")) {
			String[] split = image.split(",");
			return split;
		}
		return null;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

}
