package com.school.schoolapp.classes.billing;

import java.io.Serializable;
import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.school.schoolapp.entity.user.UserAdressVO;
import com.school.schoolapp.entity.user.UserCouponVO;

public class BillingBuyCallback extends BaseCallback implements Serializable{
	
	private BillingBuyVO data;

	public BillingBuyVO getData(){
		return data;
	}

	public void setData(BillingBuyVO data) {
		this.data = data;
	}

	public class BillingBuyVO implements Serializable{
		private List<UserAdressVO> addressList;
		
		private List<UserCouponVO> couponList;
		
		private List<BuyGoodsVO> goodsList;
		

		public List<BuyGoodsVO> getGoodsList() {
			return goodsList;
		}

		public void setGoodsList(List<BuyGoodsVO> goodsList) {
			this.goodsList = goodsList;
		}

		public List<UserAdressVO> getAddressList() {
			return addressList;
		}

		public void setAddressList(List<UserAdressVO> addressList) {
			this.addressList = addressList;
		}

		public List<UserCouponVO> getCouponList() {
			return couponList;
		}

		public void setCouponList(List<UserCouponVO> couponList) {
			this.couponList = couponList;
		}
		
		public class BuyGoodsVO implements Serializable{
			private String goods_id;
			
			private String goodsName;

			private String goodsTitle;
			
			private String goods_img;
			
			private String market_price;
			
			private String shop_price;
			
			private String num;
			
			private String isactivity;
			
			public String getIsactivity() {
				return isactivity;
			}

			public void setIsactivity(String isactivity) {
				this.isactivity = isactivity;
			}

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

			public String getGoods_img() {
				return goods_img;
			}

			public void setGoods_img(String goods_img) {
				this.goods_img = goods_img;
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

			public String getNum() {
				return num;
			}

			public void setNum(String num) {
				this.num = num;
			}
			
			
		}
		
	}
}
