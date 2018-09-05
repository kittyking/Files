package com.school.schoolapp.classes.workspace;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.store.StoreGoodsVO;

public class WorkspaceReplenishCallback extends BaseCallback {

	private String jixu;
	
	private String xubuhuo;
	
	private ReplenishVO data;
	
	public String getJixu(){
		return this.jixu;
	}
	public void setJixu(String jixu){
		this.jixu = jixu;
	} 
	
	public String getXubuhuo(){
		return this.xubuhuo;
	}
	public void setXubuhuo(String xubuhuo){
		this.xubuhuo = xubuhuo;
	} 
	public ReplenishVO getData(){
		return this.data;
	}
	public void setData(ReplenishVO data){
		this.data = data;
	} 
	
	public class ReplenishVO{
		private List<StoreGoodsVO> goodsList;
		
		public List<StoreGoodsVO> getGoodsList(){
			return this.goodsList;
		}
		public void setGoodsList(List<StoreGoodsVO> goodsList){
			this.goodsList = goodsList;
		}
	}
}
