package com.school.schoolapp.entity.user;

import java.util.List;

import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.store.StoreCartGoods;
import com.school.schoolapp.entitys.UserVO;


public class UserInfoEntity{
	
private static UserInfoEntity user = null;

	
	private UserVO userInfo;
	
	
	
	public static UserInfoEntity getInstance(){
		if(user == null){
			user = new UserInfoEntity();
		}
		
		return user;
	}



	public UserVO getUserInfo() {
		return userInfo;
	}



	public void setUserInfo(UserVO userInfo) {
		this.userInfo = userInfo;
	}
	
	
}
