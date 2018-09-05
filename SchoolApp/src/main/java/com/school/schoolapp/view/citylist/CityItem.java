package com.school.schoolapp.view.citylist;

import com.school.schoolapp.view.citylist.ContactItemInterface;

public class CityItem implements ContactItemInterface
{
	private String nickName;
	private String fullName;
	private String cityid;

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public CityItem(String nickName, String fullName)
	{
		super();
		this.nickName = nickName;
		this.setFullName(fullName);
		this.cityid = cityid;
	}

	@Override
	public String getItemForIndex()
	{
		return fullName;
	}

	@Override
	public String getDisplayInfo()
	{
		return nickName;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

}
