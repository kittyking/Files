package com.school.schoolapp.classes.billing;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.billing.BillingVO;

public class BillingCallback extends BaseCallback {
	
	private List<BillingVO> data;

	public List<BillingVO> getData() {
		return data;
	}

	public void setData(List<BillingVO> data) {
		this.data = data;
	}
	

}
