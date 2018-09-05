package com.school.schoolapp.classes.users;

import java.io.Serializable;
import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.user.UserAdressVO;

public class UserAddressCallback extends BaseCallback implements Serializable{

	private AddressVO data;
	
	public AddressVO getData() {
		return data;
	}

	public void setData(AddressVO data) {
		this.data = data;
	}

	public class AddressVO implements Serializable{
		List<UserAdressVO> addressList;

		public List<UserAdressVO> getAddressList() {
			return addressList;
		}

		public void setAddressList(List<UserAdressVO> addressList) {
			this.addressList = addressList;
		}
		
		
	}
}
