package com.school.schoolapp.entitys;

import java.util.List;

public class shopVO {
//	"wid": "5",
//    "shopName": "店铺名称",
//    "lowest": "3.00",
//    "opened": "1",
//    "goodsNum": "34",
//    "shopHead": "http://shopapp.chunchennet.cn/img",
//    "letter": [
	
	private String shopID;
	private String shopName;
	private String lowest;
	private String opened;
	private String goodsNum;
	private String shopHead;
	private List<LetterVO> letter;
	
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}
	public String getShopID(){
		return this.shopID;
	}
	
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopName(){
		return this.shopName;
	}
	public void setShopLowest(String lowest) {
		this.lowest = lowest;
	}
	public String getShopLowest(){
		return this.lowest;
	}
	public void setShopOpened(String opened) {
		this.opened = opened;
	}
	public String getShopOpened(){
		return this.opened;
	}
	public void setShopGoodsnum(String goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getShopGoodsnum(){
		return this.goodsNum;
	}
	public void setShopHead(String shopHead) {
		this.shopHead = shopHead;
	}
	public String getShopHead(){
		return this.shopHead;
	}
	public void setShopLetter(List<LetterVO> letter) {
		this.letter = letter;
	}
	public List<LetterVO> getShopLetter(){
		return this.letter;
	}
	
	public class LetterVO{
		private String title;
		
		public String getTitle(){
			return this.title;
		}
        public void setTitle(String title){
        	this.title =title;
        }
	}

}
