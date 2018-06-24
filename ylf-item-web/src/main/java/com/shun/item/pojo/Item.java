package com.shun.item.pojo;

import com.shun.pojo.TbItem;

/**
* @author czs
* @version 创建时间：2018年6月3日 下午11:37:39<br>
* 商品类TbItem的包装类，为什么要包装？因为要对这个父类进行扩展一个方法<br>
* 本来是想直接在元pojo类里面扩展的，但是pojo生成类的这个项目负责小组如果发现数据库发生变动<br>
* 势必pojo将要重新生成，那么原来的pojo将会被覆盖，那么我们写的方法就不存在了，所以这里就通过继承来实现扩展方法
*/
public class Item extends TbItem {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 传入一个原始的商品对象
	 * @param t
	 */
	public Item(TbItem t){
		this.setId(t.getId());
		this.setTitle(t.getTitle());
		this.setSellPoint(t.getSellPoint());
		this.setPrice(t.getPrice());
		this.setNum(t.getNum());
		this.setBarcode(t.getBarcode());
		this.setImage(t.getImage());
		this.setCid(t.getCid());
		this.setStatus(t.getStatus());
		this.setCreated(t.getCreated());
		this.setUpdated(t.getUpdated());
	}

	public String[] getImages(){
		if (super.getImage() != null && !super.getImage().equals("")) {
			return super.getImage().split(",");
		}
		return null;
	}

	@Override
	public String toString() {
		return "Item [getId()=" + getId() + ", getTitle()=" + getTitle() + ", getSellPoint()=" + getSellPoint()
				+ ", getPrice()=" + getPrice() + ", getNum()=" + getNum() + ", getBarcode()=" + getBarcode()
				+ ", getImage()=" + getImage() + ", getCid()=" + getCid() + ", getStatus()=" + getStatus()
				+ ", getCreated()=" + getCreated() + ", getUpdated()=" + getUpdated() + ", getClass()=" + getClass()
				+ "]";
	}
	
	public static void main(String[] args) {
		TbItem tb = new TbItem();
		tb.setCid(123l);
		Item item  = new Item(tb);
		System.out.println(item);
	}
}
