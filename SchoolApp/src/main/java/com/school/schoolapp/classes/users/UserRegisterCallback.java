package com.school.schoolapp.classes.users;

import java.io.Serializable;
import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.user.UserCouponVO;
import com.school.schoolapp.entitys.UserVO;

public class UserRegisterCallback extends BaseCallback implements Serializable{
	private UserVO data;
	
	private List<UserCouponVO> coupon;
	public List<UserCouponVO> getCoupon() {
		return coupon;
	}

	public void setCoupon(List<UserCouponVO> coupon) {
		this.coupon = coupon;
	}

	public UserVO getData() {
		return this.data;
	}

	public void setData(UserVO data) {
		this.data = data;
	}
}
