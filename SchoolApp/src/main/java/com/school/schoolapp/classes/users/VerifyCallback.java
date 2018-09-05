package com.school.schoolapp.classes.users;

import com.school.schoolapp.classes.BaseCallback;

public class VerifyCallback extends BaseCallback {

	private String code;
	
	public void setCode(String code){
		this.code = code;
	}
	public String getCode() {
		return this.code;
	}
}
