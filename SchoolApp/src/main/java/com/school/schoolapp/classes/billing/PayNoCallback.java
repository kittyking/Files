package com.school.schoolapp.classes.billing;

import com.school.schoolapp.classes.BaseCallback;

public class PayNoCallback extends BaseCallback {

	private String payNo;

	private String sign;
	
	private String code;
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	
}
