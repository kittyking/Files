package com.school.schoolapp.callback.technical;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.technical.TechnicalCategoryEntity;

public class TechnicalCategoryCallback extends BaseCallback {

	private TechnicalCategoryList data;

	public TechnicalCategoryList getData() {
		return data;
	}

	public void setData(TechnicalCategoryList data) {
		this.data = data;
	}
	
	public class TechnicalCategoryList{
		private List<TechnicalCategoryEntity> list;

		public List<TechnicalCategoryEntity> getList() {
			return list;
		}

		public void setList(List<TechnicalCategoryEntity> list) {
			this.list = list;
		}
	}
	
	
}
