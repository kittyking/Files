package com.school.schoolapp.entity.store;

import java.util.List;

public class GoodDetailVO {

private String goods_id;
	
	private String goodsName;

	private String goodsTitle;
	
	private String market_price;
	
	private String shop_price;
	
	private String unit;
	
	private List<String> img;
	
	private List<String> imglist;
	
	private List<GoodPropertyVO> attr;
	
	
	
	public String getGoods_id() {
		return goods_id;
	}



	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}



	public String getGoodsName() {
		return goodsName;
	}



	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}



	public String getGoodsTitle() {
		return goodsTitle;
	}



	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}



	public String getMarket_price() {
		return market_price;
	}



	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}



	public String getShop_price() {
		return shop_price;
	}



	public void setShop_price(String shop_price) {
		this.shop_price = shop_price;
	}



	public String getUnit() {
		return unit;
	}



	public void setUnit(String unit) {
		this.unit = unit;
	}



	public List<String> getImg() {
		return img;
	}



	public void setImg(List<String> img) {
		this.img = img;
	}



	public List<String> getImglist() {
		return imglist;
	}



	public void setImglist(List<String> imglist) {
		this.imglist = imglist;
	}



	public List<GoodPropertyVO> getAttr() {
		return attr;
	}



	public void setAttr(List<GoodPropertyVO> attr) {
		this.attr = attr;
	}



	public class GoodPropertyVO{
		private String title;
		private String val;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getVal() {
			return val;
		}
		public void setVal(String val) {
			this.val = val;
		}
		
	}
}
