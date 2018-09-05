package com.school.schoolapp.entity.user;

public class UserMenuEntity {
	private int imageSrc;
	
	private String titleStr;

	public void setImageSrc(int imageSrc){
		this.imageSrc = imageSrc;
	}
	public int getImageSrc(){
		return this.imageSrc;
	}
	public void setTitleStr(String titleStr){
		this.titleStr = titleStr;
	}
	public String getTitleStr(){
		return this.titleStr;
	}
}
