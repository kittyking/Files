package com.school.schoolapp.classes.billing;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.school.schoolapp.entity.user.UserAdressVO;
import com.school.schoolapp.entity.user.UserCouponVO;
import com.school.schoolapp.entitys.OrderVO;

public class BillingPayCallback extends BaseCallback {
	
	private BillingPay data;

	public BillingPay getData() {
		return data;
	}

	public void setData(BillingPay data) {
		this.data = data;
	}

	public class BillingPay{
		private OrderVO order;
		
		private UserAdressVO address;
		
		private UserCouponVO coupon;
		
		private List<GoodsPay> goods;
		
		
		public OrderVO getOrder() {
			return order;
		}


		public void setOrder(OrderVO order) {
			this.order = order;
		}


		public UserAdressVO getAddress() {
			return address;
		}


		public void setAddress(UserAdressVO address) {
			this.address = address;
		}


		public UserCouponVO getCoupon() {
			return coupon;
		}


		public void setCoupon(UserCouponVO coupon) {
			this.coupon = coupon;
		}


		public List<GoodsPay> getGoods() {
			return goods;
		}


		public void setGoods(List<GoodsPay> goods) {
			this.goods = goods;
		}

		public class GoodsPay{
			private  String goodsID;
			private  String goodsName;
			private  String goodsNum;
			private  String goodsPrice;
			private  String goodsImg;
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
			
			
			
			
//			"goodsID": "14",//商品ID
//	        "goodsName": "雪碧600ml",//商品名称
//	        "goodsNum": "41",//商品数量
//	        "goodsPrice": "3.00",//商品单价
//	        "goodsImg": 
		}
	}
}
