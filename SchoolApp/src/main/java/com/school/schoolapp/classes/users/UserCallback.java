package com.school.schoolapp.classes.users;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entitys.UserVO;

public class UserCallback extends BaseCallback {
	
	private UserVO data;
	
	public UserVO getData() {
		return this.data;
	}

	public void setData(UserVO data) {
		this.data = data;
	}

}
