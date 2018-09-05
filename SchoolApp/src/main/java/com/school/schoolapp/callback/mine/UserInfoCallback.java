package com.school.schoolapp.callback.mine;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.user.UserInfoVO;

public class UserInfoCallback extends BaseCallback {

	private UserInfoVO data;

	public UserInfoVO getData() {
		return data;
	}

	public void setData(UserInfoVO data) {
		this.data = data;
	}
	
}
