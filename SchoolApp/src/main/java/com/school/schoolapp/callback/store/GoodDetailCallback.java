package com.school.schoolapp.callback.store;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.store.GoodDetailVO;

public class GoodDetailCallback extends BaseCallback {

	private GoodDetailVO data;

	public GoodDetailVO getData() {
		return data;
	}

	public void setData(GoodDetailVO data) {
		this.data = data;
	}
	
}
