package com.school.schoolapp.callback.mine;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.user.UserCouponVO;

public class UserCouponCallback extends BaseCallback {

	private List<UserCouponVO> data;

	public List<UserCouponVO> getData() {
		return data;
	}

	public void setData(List<UserCouponVO> data) {
		this.data = data;
	}
	
}
