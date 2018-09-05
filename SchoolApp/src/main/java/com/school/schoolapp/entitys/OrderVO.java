package com.school.schoolapp.entitys;

public class OrderVO {

	private String orderID;
	private String orderSN;
	private String orderStatus;
	private String shippingStatus;
	private String timing;
	private String payStatus;
	private int payment;
	private String money;
	private String goodsMoney;
	private String goodsNum;
	private String payFinashTime;
	private String remark;
	private String shopid;
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderSN() {
		return orderSN;
	}
	public void setOrderSN(String orderSN) {
		this.orderSN = orderSN;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getShippingStatus() {
		return shippingStatus;
	}
	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	public String getTiming() {
		return timing;
	}
	public void setTiming(String timing) {
		this.timing = timing;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
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
	public String getGoodsMoney() {
		return goodsMoney;
	}
	public void setGoodsMoney(String goodsMoney) {
		this.goodsMoney = goodsMoney;
	}
	public String getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getPayFinashTime() {
		return payFinashTime;
	}
	public void setPayFinashTime(String payFinashTime) {
		this.payFinashTime = payFinashTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
//	"orderID": "8",//订单ID
//    "orderSN": "2016060510255491",//订单编号
//    "orderStatus": "1",//订单状态（1待接单,2待发货,3配送中,4已结单）
//    "shippingStatus": "1",//配送方式（1、急速配送，2、指定时间送达）
//    "timing": "2016-06-04 18:00-19:00",//指定配送时间
//    "payStatus": "0",//支付状态（0未付款;1付款中;2已付款）
//    "payment": "1",//付款方式（1、货到付款，2、在线支付）
//    "money": "183.00",//实付款
//    "goodsMoney": "0.00",//商品总价
//    "goodsNum": "53",//商品数量
//    "payFinashTime": "2016-06-05 21:48:33",//未付款状态到期时间
//    "remark": "快递"//订单备注

}
