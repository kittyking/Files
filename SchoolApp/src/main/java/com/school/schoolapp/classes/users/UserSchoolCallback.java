package com.school.schoolapp.classes.users;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.user.UserSchoolVO;

public class UserSchoolCallback extends BaseCallback {

	private List<UserSchoolVO> data;

	public List<UserSchoolVO> getData() {
		return data;
	}

	public void setData(List<UserSchoolVO> data) {
		this.data = data;
	}
	
	
}
