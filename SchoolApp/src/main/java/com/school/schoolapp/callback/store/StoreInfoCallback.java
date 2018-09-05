package com.school.schoolapp.callback.store;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.store.StoreCategoryVO;
import com.school.schoolapp.entity.store.StoreLetterVO;
import com.school.schoolapp.entitys.adVO;

public class StoreInfoCallback extends BaseCallback {
	
	private String moblie;
	
	private String shopName;
	
	private String lowest;
	
	private String shopUserName;
	
	
	
	public String getMoblie() {
		return moblie;
	}
	public void setMoblie(String moblie) {
		this.moblie = moblie;
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
	public String getShopUserName() {
		return shopUserName;
	}
	public void setShopUserName(String shopUserName) {
		this.shopUserName = shopUserName;
	}

	private StoreInfoVO data;
	
	public void setData(StoreInfoVO data){
		this.data = data;
	}
	public StoreInfoVO getData(){
		return this.data;
	}
	
	public class StoreInfoVO{
		
		private List<adVO> adlist;
		
		private List<StoreCategoryVO> categoryList;
		
		private List<StoreLetterVO> letter;
		
		public void setAdlist(List<adVO> adlist){
			this.adlist = adlist;
		}
		public List<adVO> getAdlist(){
			return this.adlist;
		}
		public void setCategoryList(List<StoreCategoryVO> categoryList){
			this.categoryList = categoryList;
		}
		public List<StoreCategoryVO> getCategoryList(){
			return this.categoryList;
		}
		public void setLetter(List<StoreLetterVO> letter){
			this.letter = letter;
		}
		public List<StoreLetterVO> getLetter(){
			return this.letter;
		}
		
	}

}
