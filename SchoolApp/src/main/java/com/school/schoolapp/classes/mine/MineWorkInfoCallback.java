package com.school.schoolapp.classes.mine;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.users.FloorChooseCallback.FloorVO;
import com.school.schoolapp.entity.user.UserFloorVO;

public class MineWorkInfoCallback extends BaseCallback {
	
	private WorkInfoVO data;
	
	public void setData( WorkInfoVO data){
		this.data = data;
	}
	public  WorkInfoVO getData(){
		return this.data;
	}

	
	public class WorkInfoVO{
		private String mobile;
		
		private String schoolName;
		
		private List<UserFloorVO> wanFloor;
		
		private List<UserFloorVO> womanFloor;
		
		public void setMobile(String mobile){
			this.mobile = mobile;
		}
		public String getMobile(){
			return this.mobile;
		}
		public void setSchoolName(String schoolName){
			this.schoolName = schoolName;
		}
		public String getSchoolName(){
			return this.schoolName;
		}
		public void setWanFloor(List<UserFloorVO> wanFloor){
			this.wanFloor = wanFloor;
		}
		public List<UserFloorVO> getWanFloor(){
			return this.wanFloor;
		}
		public void setWomanFloor(List<UserFloorVO> womanFloor){
			this.womanFloor = womanFloor;
		}
		public List<UserFloorVO> getWomanFloor(){
			return this.womanFloor;
		}
	}

}
