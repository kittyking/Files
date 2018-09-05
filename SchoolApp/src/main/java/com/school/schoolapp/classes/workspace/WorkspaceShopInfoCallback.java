package com.school.schoolapp.classes.workspace;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entitys.shopVO;
import com.school.schoolapp.entitys.shopVO.LetterVO;

public class WorkspaceShopInfoCallback extends BaseCallback {

	private ShopInfoVO data;
	
	public ShopInfoVO getData() {
		return data;
	}



	public void setData(ShopInfoVO data) {
		this.data = data;
	}



	public class ShopInfoVO{
		
		private ShopInfo shop;
		
		private List<LetterVO> letter;

		public ShopInfo getShop() {
			return shop;
		}

		public void setShop(ShopInfo shop) {
			this.shop = shop;
		}

		public List<LetterVO> getLetter() {
			return letter;
		}

		public void setLetter(List<LetterVO> letter) {
			this.letter = letter;
		}
		
		public class ShopInfo{
			private String headImg;
			
			private String shopName;
			
			private String lowest;

			public String getHeadImg() {
				return headImg;
			}

			public void setHeadImg(String headImg) {
				this.headImg = headImg;
			}

			public String getShopName() {
				return shopName;
			}

			public void setShopName(String shopName) {
				this.shopName = shopName;
			}

			public String getLowest() {
				return lowest;
			}

			public void setLowest(String lowest) {
				this.lowest = lowest;
			}
			
		}
		
	}
}
