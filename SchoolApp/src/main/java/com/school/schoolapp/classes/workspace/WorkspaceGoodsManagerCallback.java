package com.school.schoolapp.classes.workspace;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.store.StoreGoodsVO;

public class WorkspaceGoodsManagerCallback extends BaseCallback {
	private ManagerVO data;
	
	public ManagerVO getData(){
		return this.data;
	}
	public void setData(ManagerVO data){
		this.data = data;
	} 
	
	public class ManagerVO{
		private List<StoreGoodsVO> goodsList;
		
		public List<StoreGoodsVO> getGoodsList(){
			return this.goodsList;
		}
		public void setGoodsList(List<StoreGoodsVO> goodsList){
			this.goodsList = goodsList;
		}
	}

}
