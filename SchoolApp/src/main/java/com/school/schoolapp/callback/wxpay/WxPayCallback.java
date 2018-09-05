package com.school.schoolapp.callback.wxpay;

import com.school.schoolapp.classes.BaseCallback;

public class WxPayCallback extends BaseCallback{
    private String prepay;
    private String mch;
    private String sign;
    private String nonce;
	public String getPrepay() {
		return prepay;
	}
	public void setPrepay(String prepay) {
		this.prepay = prepay;
	}
	public String getMch() {
		return mch;
	}
	public void setMch(String mch) {
		this.mch = mch;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
    
    
}
