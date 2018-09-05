package com.school.schoolapp.callback.store;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.store.StoreGoodsVO;

public class StoreGoodsCallback extends BaseCallback {

	private GoodsVO data;
	
	public void setData(GoodsVO data){
		this.data = data;
	}
	public GoodsVO getData(){
		return this.data;
	}
	
	
	public class GoodsVO{
		private List<StoreGoodsVO> goodsList;
		
		public void setGoodList(List<StoreGoodsVO> goodsList){
			this.goodsList = goodsList;
		}
		public List<StoreGoodsVO> getGoodList(){
			return this.goodsList;
		}
	}
}
