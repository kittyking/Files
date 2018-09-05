package com.school.schoolapp.entitys;

public class UserVO {

	private String userID;
	
	private String userName;
	
	private String nickName;
	
	private String userRank;
	
	private String recommend;
	
	private String schoolID;
	
	private int waiter;
	
	private String wid;
	
	private String trueName;
	
	private String mobile;
	
	private String floorID;
	
	private String floorName;
	
	private String schoolName;
	private String avatar;

	private String password;
	private String cityName;
	private String cityID;
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getUserID() {
		return this.userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserRank() {
		return this.userRank;
	}

	public void setUserRank(String userRank) {
		this.userRank = userRank;
	}
	public String getRecomment() {
		return this.recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getSchoolID() {
		return this.schoolID;
	}

	public void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}
	public int getWaiter() {
		return this.waiter;
	}

	public void setWaiter(int waiter) {
		this.waiter = waiter;
	}
	public String getTruename() {
		return this.trueName;
	}

	public void setTruename(String trueName) {
		this.trueName = trueName;
	}
	public String getWID() {
		return this.wid;
	}

	public void setWID(String wid) {
		this.wid = wid;
	}
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFloorID() {
		return this.floorID;
	}

	public void setFloorID(String floorID) {
		this.floorID = floorID;
	}
	
//	"schoolID": "1",//所属学校
//	"waiter": 1,//是否为店铺CEO（0、不是1、是）
//	"wid": "1",//店铺ID
//	"trueName": "不知道",//店铺CEO真实姓名
//	"mobile": "139999999999",//店铺CEO手机号
//	"floorID": "3"//店铺所属楼栋
}
