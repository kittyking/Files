package com.school.schoolapp.tool.pay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.id.Hex;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.school.schoolapp.R;
import com.school.schoolapp.activity.store.StoreCreateBillingActivity;
import com.school.schoolapp.classes.tools.MD5paser;
import com.school.schoolapp.classes.tools.ToastTool;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXTools {

	public static WXTools wxTool;
	public static String APP_ID = "wx619c35e4b2ddc88c";
	public static String APP_KEY="iwf8393kjf9JDI92JFOW092MOJSFojfs";
	
	private IWXAPI wxapi;
	
	public static WXTools getInstance(){
		if(wxTool==null)
			wxTool=new WXTools();
		
		return wxTool;
	}
	
	//注册应用到微信
	public void regToWX(Context context){
		wxapi = WXAPIFactory.createWXAPI(context,APP_ID);
		wxapi.registerApp(APP_ID);
	}
	public int type;//type标识充值还是付款 0付款 1充值
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void payToWX(Context context, String partnerId,String prepayId){
		
		
		regToWX(context);
		
		PayReq payReq = new PayReq();
		payReq.appId=APP_ID;
		payReq.nonceStr=getNonceStr();
		payReq.partnerId=partnerId;
		payReq.prepayId=prepayId;
		payReq.packageValue="Sign=WXPay";
		payReq.timeStamp=getTimeStamp();
		
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", payReq.appId));
		signParams.add(new BasicNameValuePair("noncestr", payReq.nonceStr));
		signParams.add(new BasicNameValuePair("package", payReq.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", payReq.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", payReq.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", payReq.timeStamp));
		
		payReq.sign = genAppSign(signParams);
		
		sb.append("sign\n"+payReq.sign+"\n\n");

		if(wxapi.sendReq(payReq)){
			ToastTool.showWithMessage("打开微信支付成功", context);
			Log.i("", "成功");
		}else{
			ToastTool.showWithMessage("打开微信支付失败", context);
			Log.i("", "失败");
		}
	}
	StringBuffer sb;
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		this.sb = new StringBuffer();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(APP_KEY);

		this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5paser.getMessageDigest(sb.toString().getBytes());
		Log.e("orion","----"+appSign);
		return appSign;
	}
	public void payToWxByInfo(Context context,String partnerId,String prepayId,String sign,String nonceStr){
		regToWX(context);
		
		PayReq payReq = new PayReq();
		payReq.appId=APP_ID;
		payReq.nonceStr=nonceStr;
		payReq.partnerId=partnerId;
		payReq.prepayId=prepayId;
		payReq.packageValue="Sign=WXPay";
		payReq.timeStamp=getTimeStamp();
		payReq.sign=sign;
		if(wxapi.sendReq(payReq)){
			ToastTool.showWithMessage("打开微信支付成功", context);
			Log.i("", "成功");
		}else{
			ToastTool.showWithMessage("打开微信支付失败", context);
			Log.i("", "失败");
		}
	}
	
	private String getNonceStr(){
		Random random = new Random(); 
		 return MD5paser.getMd5UTF8(String.valueOf(random.nextInt(10000))); 
	}
	private String getTimeStamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		return sdf.format(System.currentTimeMillis());
	}
	public String getUrl(Context context){
		return context.getString(R.string.base_url) + context.getString(R.string.pay_get_sign_wx);
	}
	public RequestParams getParams(String tradeno,String ticket){
		RequestParams params = new RequestParams();
		params.put("ticket", ticket);
		params.put("tradeno",tradeno);
		return params;
	}
	
}
