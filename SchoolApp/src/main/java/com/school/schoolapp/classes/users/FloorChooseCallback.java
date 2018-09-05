package com.school.schoolapp.classes.users;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.user.UserFloorVO;

public class FloorChooseCallback extends BaseCallback {
	
	private FloorVO data;
	
	public void setData( FloorVO data){
		this.data = data;
	}
	public  FloorVO getData(){
		return this.data;
	}

	public class FloorVO{
		private List<UserFloorVO> wanFloor;
		
		private List<UserFloorVO> wowanFloor;
		
		public void setWanFloor(List<UserFloorVO> wanFloor){
			this.wanFloor = wanFloor;
		}
		public List<UserFloorVO> getWanFloor(){
			return this.wanFloor;
		}
		public void setWomanFloor(List<UserFloorVO> wowanFloor){
			this.wowanFloor = wowanFloor;
		}
		public List<UserFloorVO> getWomanFloor(){
			return this.wowanFloor;
		}
	}
}
