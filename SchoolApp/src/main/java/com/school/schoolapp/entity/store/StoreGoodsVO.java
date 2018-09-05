package com.school.schoolapp.entity.store;

import java.io.Serializable;

public class StoreGoodsVO implements Serializable{
	
	private String goods_id;
	
	private String goodsName;

	private String goodsTitle;
	
	private String goodsImg;
	
	private String marketPrice;
	
	private String shopPrice;
	
	private String stock;
	
	private String top;
	
	private String hot;
	
	private String sales_count;
	
	
	
	public String getSales_count() {
		return sales_count;
	}
	public void setSales_count(String sales_count) {
		this.sales_count = sales_count;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public String getHot() {
		return hot;
	}
	public void setHot(String hot) {
		this.hot = hot;
	}
	public void setGoods_id(String goods_id){
		this.goods_id = goods_id;
	}
	public String getGoods_id(){
		return this.goods_id;
	}
	
	public void setGoodsName(String goodsName){
		this.goodsName = goodsName;
	}
	public String getGoodsName(){
		return this.goodsName;
	}
	
	public void setGoodsTitle(String goodsTitle){
		this.goodsTitle = goodsTitle;
	}
	public String getGoodsTitle(){
		return this.goodsTitle;
	}
	
	public void setGoodsImg(String goodsImg){
		this.goodsImg = goodsImg;
	}
	public String getGoodsImg(){
		return this.goodsImg;
	}
	
	public void setMarketPrice(String marketPrice){
		this.marketPrice = marketPrice;
	}
	public String getMarketPrice(){
		return this.marketPrice;
	}
	
	public void setShopPrice(String shopPrice){
		this.shopPrice = shopPrice;
	}
	public String getShopPrice(){
		return this.shopPrice;
	}
	
	public void setStock(String stock){
		this.stock = stock;
	}
	public String getStock(){
		return this.stock;
	}
	
//	"goods_id": "1",//商品ID
//    "goodsName": "康师傅红烧牛肉面",//商品名称
//    "goodsTitle": "难以忘怀这样的味道",//商品描述
//    "goodsImg": "img",//商品图片
//    "marketPrice": "6.6",//商品原价
//    "shopPrice": "5.0",//商品店铺价
//    "stock": "10"//库存


}
