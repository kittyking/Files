package com.school.schoolapp.wxapi;


import com.school.schoolapp.activity.store.StorePaySuccessActivity;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.tool.pay.WXTools;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, WXTools.APP_ID);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		 Log.d("onPayFinish", "onPayFinish, errStr = " + resp.errStr);
		 if(resp.errCode==0){
			 Toast.makeText(this,"支付成功!",Toast.LENGTH_SHORT).show();
		     Intent intent = new Intent(WXPayEntryActivity.this,StorePaySuccessActivity.class);
		     if(WXTools.getInstance().getType()==1)
		    	 intent.putExtra("type", "recharge");
		     startActivity(intent);

			 noticeCartInit();
		 }else if(resp.errCode==-1){
		  Toast.makeText(this,"支付失败!",Toast.LENGTH_SHORT).show();
		 }else if(resp.errCode==-2){
		  Toast.makeText(this,"取消支付!",Toast.LENGTH_SHORT).show();
		 }
		 WXTools.getInstance().setType(-1);
		 finish();
	}
	

	private void noticeCartInit(){
		StoreCartEntity.initCart();
		
		Intent intent = new Intent();
		intent.setAction("Pay");
	    sendBroadcast(intent);
	}
}