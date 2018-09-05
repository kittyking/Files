package com.school.schoolapp.tool.pay;

import java.net.URLEncoder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.school.schoolapp.activity.billing.BillingPayActivity;
import com.school.schoolapp.activity.store.StoreCreateBillingActivity;
import com.school.schoolapp.activity.store.StorePaySuccessActivity;
import com.school.schoolapp.classes.alipay.PayResult;

public class AliTools {

	private static AliTools aliTools;
	
	private static final int SDK_PAY_FLAG = 1;
	
	public static AliTools getInstance(){
		if(aliTools==null)
			aliTools=new AliTools();
		
		return aliTools;
	}
	

	public static String BRODECAST_NAME = "ALIPAY"; 
	public void aliPay(String sign,String code,final Activity aliActivity){
		try {
			String signs = URLEncoder.encode(sign, "UTF-8");
			final String payInfo = code + "&sign=\"" + signs + "\"&sign_type=\"RSA\"";
		    Log.i("payinfo", payInfo);
			Runnable payRunnable = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					PayTask alipay = new PayTask(aliActivity);
					String result = alipay.pay(payInfo, true);
					
					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
	                msg.obj = result;
	                
	                //通知付款成功
	                Intent mIntent = new Intent(BRODECAST_NAME);  
	                mIntent.putExtra("flag", SDK_PAY_FLAG);
	                mIntent.putExtra("result", result);  
	                aliActivity.sendBroadcast(mIntent);
				}
			};
			// 必须异步调用
	        Thread payThread = new Thread(payRunnable);
	        payThread.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private Context context;
	public void registResultReceiver(Context context,BroadcastReceiver mBroadcastReceiver){
		IntentFilter myIntentFilter = new IntentFilter();  
	    myIntentFilter.addAction(AliTools.BRODECAST_NAME);  
	     //注册广播        
        context.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	public String getResultInfo(String result){
		PayResult payResult = new PayResult(result);
		/**
		 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
		 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
		 * docType=1) 建议商户依赖异步通知
		 */
		String resultInfo = payResult.getResult();// 同步返回需要验证的信息
		Log.i("", "school===="+resultInfo);
		return payResult.getResultStatus();
	}
}
