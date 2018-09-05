package com.school.schoolapp.callback.wallet;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.wallet.WalletDetailVO;

public class WalletDetailCallback extends BaseCallback {

	private List<WalletDetailVO> data;

	public List<WalletDetailVO> getData() {
		return data;
	}

	public void setData(List<WalletDetailVO> data) {
		this.data = data;
	}
	
	
}
