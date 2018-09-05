package com.school.schoolapp.entity.user;

public class UserFloorVO {
	
	private String floorID;
	
	private String floorName;
	
	private String sex;
	
	private String schoolID;
	
	private String opened;

	public void setFloorID(String floorID){
		this.floorID = floorID;
	}
	public String getFloorID(){
		return this.floorID;
	}
	public void setFloorName(String floorName){
		this.floorName = floorName;
	}
	public String getFloorName(){
		return this.floorName;
	}
	public void setSex(String sex){
		this.sex = sex;
	}
	public String getSex(){
		return this.sex;
	}
	public void setSchoolID(String schoolID){
		this.schoolID = schoolID;
	}
	public String getSchoolID(){
		return this.schoolID;
	}
	public void setOpened(String opened){
		this.opened = opened;
	}
	public String getOpened(){
		return this.opened;
	}
//	 "floorID": "1000001",
//     "floorName": "1号楼",
//     "sex": "1",
//     "schoolID": "100001",
//     "opened": 1//是否开通0、未开通1、已开通

}
