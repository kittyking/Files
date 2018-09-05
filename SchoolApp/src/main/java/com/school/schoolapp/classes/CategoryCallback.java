package com.school.schoolapp.classes;

import java.util.List;

import com.school.schoolapp.entity.store.StoreCategoryVO;

public class CategoryCallback extends BaseCallback {
	
	private CategoryVO data;
	
	public void setData(CategoryVO data){
		this.data =data;
	}
	public CategoryVO getData(){
		return this.data;
	}

	public class CategoryVO{
		private List<StoreCategoryVO> categoryList;
		
		public void setCategoryList(List<StoreCategoryVO> categoryList) {
			this.categoryList = categoryList;
		}
		public List<StoreCategoryVO> getCategoryList() {
			return this.categoryList;
		}
		
	}
}
