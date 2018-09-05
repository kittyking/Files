package com.school.schoolapp.classes.users;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;

public class AddressDefaultCallback extends BaseCallback {

	private String schoolName;
	
	private DefalutVO data;
	
	
	
	public String getSchoolName() {
		return schoolName;
	}



	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}



	public DefalutVO getData() {
		return data;
	}



	public void setData(DefalutVO data) {
		this.data = data;
	}



	public class DefalutVO{
		private List<ShoolFloorVO> floor;
		
		
		
		public List<ShoolFloorVO> getFloor() {
			return floor;
		}



		public void setFloor(List<ShoolFloorVO> floor) {
			this.floor = floor;
		}



		public class ShoolFloorVO{
			private String floorID;
			
			private String floorName;

			public String getFloorID() {
				return floorID;
			}

			public void setFloorID(String floorID) {
				this.floorID = floorID;
			}

			public String getFloorName() {
				return floorName;
			}

			public void setFloorName(String floorName) {
				this.floorName = floorName;
			}
			
			
		}
	}
}
