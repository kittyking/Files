package com.school.schoolapp.classes.users;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.users.AddressDefaultCallback.DefalutVO.ShoolFloorVO;
import com.school.schoolapp.classes.users.UserAddressCallback.AddressVO;
import com.school.schoolapp.entity.user.UserAdressVO;

public class AddressEditCallback extends BaseCallback {
	private AddressEditVO data;
	

	public AddressEditVO getData() {
		return data;
	}


	public void setData(AddressEditVO data) {
		this.data = data;
	}


	public class AddressEditVO{
		private List<ShoolFloorVO> floor;
		
		public List<ShoolFloorVO> getFloor() {
			return floor;
		}

		public void setFloor(List<ShoolFloorVO> floor) {
			this.floor = floor;
		}
		
		private UserAdressVO address;

		public UserAdressVO getAddress() {
			return address;
		}

		public void setAddress(UserAdressVO address) {
			this.address = address;
		}
		
		

	}
}
