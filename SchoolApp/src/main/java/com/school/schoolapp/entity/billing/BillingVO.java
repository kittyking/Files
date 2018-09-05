package com.school.schoolapp.entity.billing;

import java.util.List;

import com.school.schoolapp.entity.store.StoreGoodsVO;

public class BillingVO {

	private String userName;
	
	private String mobile;
	
	private String shopID;
	
	private String shopName;

	

	private String orderID;
	
	private String orderSn;
	
	private String orderStatus;
	
	private String shippingStatus;
	
	private int payStatus;
	
	private String timing;
	
	private int payment;
	
	private String money;
	
	private String goodsNum;
	
	private List<BillingGoodsVO> goods;
	
	
	
	public String getShippingStatus() {
		return shippingStatus;
	}



	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public String getOrderID() {
		return orderID;
	}



	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}



	public String getOrderSn() {
		return orderSn;
	}



	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}



	public String getOrderStatus() {
		return orderStatus;
	}



	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}



	public int getPayStatus() {
		return payStatus;
	}



	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}



	public String getTiming() {
		return timing;
	}



	public void setTiming(String timing) {
		this.timing = timing;
	}



	public int getPayment() {
		return payment;
	}



	public void setPayment(int payment) {
		this.payment = payment;
	}



	public String getMoney() {
		return money;
	}



	public void setMoney(String money) {
		this.money = money;
	}



	public String getGoodsNum() {
		return goodsNum;
	}



	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}



	public List<BillingGoodsVO> getGoods() {
		return goods;
	}



	public void setGoods(List<BillingGoodsVO> goods) {
		this.goods = goods;
	}

	public String getShopID() {
		return shopID;
	}



	public void setShopId(String shopID) {
		this.shopID = shopID;
	}



	public String getShopName() {
		return shopName;
	}



	public void setShopName(String shopName) {
		this.shopName = shopName;
	}



	public class BillingGoodsVO{
		private String goodsID;
		
		private String goodsName;
		
		private String goodsNum;
		
		private String goodsPrice;
		
		private String goodsImg;

		public String getGoodsID() {
			return goodsID;
		}

		public void setGoodsID(String goodsID) {
			this.goodsID = goodsID;
		}

		public String getGoodsName() {
			return goodsName;
		}

		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}

		public String getGoodsNum() {
			return goodsNum;
		}

		public void setGoodsNum(String goodsNum) {
			this.goodsNum = goodsNum;
		}

		public String getGoodsPrice() {
			return goodsPrice;
		}

		public void setGoodsPrice(String goodsPrice) {
			this.goodsPrice = goodsPrice;
		}

		public String getGoodsImg() {
			return goodsImg;
		}

		public void setGoodsImg(String goodsImg) {
			this.goodsImg = goodsImg;
		}
		
		
		
	}
}
