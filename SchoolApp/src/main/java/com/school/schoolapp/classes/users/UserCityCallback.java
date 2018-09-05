package com.school.schoolapp.classes.users;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.user.UserCityVO;

public class UserCityCallback extends BaseCallback {

	private List<UserCityVO> data;

	public List<UserCityVO> getData() {
		return data;
	}

	public void setData(List<UserCityVO> data) {
		this.data = data;
	}
	
	
}
