package com.school.schoolapp.callback.store;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entitys.adVO;
import com.school.schoolapp.entitys.shopVO;

public class StoreMainCallback extends BaseCallback {
	
	private StoreMainVO data;
	
	public void setData(StoreMainVO data){
		this.data = data;
	}
	
	public StoreMainVO getData() {
		return this.data;
	}
	
	public class StoreMainVO{
		private List<adVO> adlist;
		
		private List<shopVO> shoplist;
		
		public void setAds(List<adVO> adlist){
			this.adlist = adlist;
		}
		
		public List<adVO> getAds(){
			return this.adlist;
		}
		
		public void setShops(List<shopVO> shoplist){
			this.shoplist = shoplist;
		}
		
		public List<shopVO> getShops(){
			return this.shoplist;
		}
	}

}
