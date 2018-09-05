package com.school.schoolapp.activity.mine.wallet;

import java.net.URLEncoder;

import org.apache.http.Header;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.activity.billing.BillingPayActivity;
import com.school.schoolapp.activity.store.StoreCreateBillingActivity;
import com.school.schoolapp.activity.store.StorePaySuccessActivity;
import com.school.schoolapp.callback.wxpay.WxPayCallback;
import com.school.schoolapp.classes.alipay.PayResult;
import com.school.schoolapp.classes.billing.PayNoCallback;
import com.school.schoolapp.classes.mine.MineWalletBalanceCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.tool.pay.AliTools;
import com.school.schoolapp.tool.pay.WXTools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MineWalletRechargeActivity extends BaseActivity {

	private TextView balanceTV;
	private EditText moneyET;
	private RadioButton zfbBtn,wxBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_wallet_recharge);
		this.titleTextView.setText("充值");
		this.view.setBackgroundColor(Color.WHITE);
		
		balanceTV = (TextView)findViewById(R.id.balance);
		zfbBtn = (RadioButton)findViewById(R.id.zfbRB);
		wxBtn = (RadioButton)findViewById(R.id.wxRB);
		moneyET=(EditText)findViewById(R.id.moneyET);
		
	    RadioGroup rg = new RadioGroup(this);
	    rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.zfbRB:
					
					break;
				case R.id.wxRB:
					break;

				default:
					break;
				}
			}
		});
		getRemindMoney();
		
	}
	
	public void getRemindMoney(){
		String url = getString(R.string.base_url)+getString(R.string.wallet_get_balace);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params= new RequestParams();
		params.put("ticket", this.ticket);
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				MineWalletBalanceCallback balance = gson.fromJson(new String(arg2), MineWalletBalanceCallback.class);
				if(balance.getResult().equals("1"))
					balanceTV.setText(balance.getBalance());
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	private int type=0;
	public void recharge(View v){
		String url = getString(R.string.base_url)+getString(R.string.wallet_recharge);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params= new RequestParams();
		params.put("ticket", this.ticket);
		if(zfbBtn.isChecked())
			type=1;
		else
			type=2;
		
		params.put("type",type);//1支付宝 2微信
		params.put("money", moneyET.getText().toString());
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				PayNoCallback payno = new Gson().fromJson(new String(arg2), PayNoCallback.class);
				if(payno.getResult().equals("1")){
					if(type==1)
						payByZFB(payno.getPayNo());
					else
						payByWX(payno.getPayNo());
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				ToastTool.showNetworkError(getApplicationContext());
				
			}
		});
	}

	public void payByZFB(String payno){
		Log.i("", "支付宝");
		registerBroadCast();
		payByAli(payno);
	}
	public void payByWX(String payno){
		Log.i("", "微信");
		payByWx(payno);
	}
	//注册广播
	private void registerBroadCast(){
		 IntentFilter myIntentFilter = new IntentFilter();  
	     myIntentFilter.addAction(AliTools.BRODECAST_NAME);  
	     //注册广播        
         registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	private  BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String action = arg1.getAction();  
            if(action.equals(AliTools.BRODECAST_NAME)){
            	int flag = arg1.getIntExtra("flag",0);
            	String result = arg1.getStringExtra("result");
            	switch (flag) {
    			case SDK_PAY_FLAG: {
    				String resultStatus = AliTools.getInstance().getResultInfo(result);
    				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
    				if (TextUtils.equals(resultStatus, "9000")) {//这里需要加入更高级的判断方式在resultStatus=9000，并且success=“true”以及sign=“xxx”校验通过的情况下，证明支付成功
    					Intent intent = new Intent(MineWalletRechargeActivity.this,StorePaySuccessActivity.class);
    					intent.putExtra("type", "recharge");
    					startActivity(intent);
    					finish();
    				} else {
    					// 判断resultStatus 为非"9000"则代表可能支付失败
    					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
    					if (TextUtils.equals(resultStatus, "8000")) {
    						finish();
    						Toast.makeText(MineWalletRechargeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
    					} else {
    						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
    						finish();
    						Toast.makeText(MineWalletRechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
    					}
    				}
    				break;
    			}
    			default:
    				break;
    			}
            }  
		}
	};
	
	private static final int SDK_PAY_FLAG = 1;
	
	private void payByAli(String tradeno){
		String url = getString(R.string.base_url) + getString(R.string.pay_get_sign);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("tradeno",tradeno);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				PayNoCallback payNo = gson.fromJson(new String(arg2), PayNoCallback.class);
				if(payNo.getResult().equals("1")){
					AliTools.getInstance().aliPay(payNo.getSign(), payNo.getCode(), MineWalletRechargeActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	private void payByWx(String tradeno){
		new AsyncHttpClient().post(WXTools.getInstance().getUrl(this),WXTools.getInstance().getParams(tradeno, this.ticket), new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				WxPayCallback wxpay = gson.fromJson(new String(arg2), WxPayCallback.class);
				if(wxpay.getResult().equals("1")){
					WXTools.getInstance().setType(1);//设置充值标识
					WXTools.getInstance().payToWX(MineWalletRechargeActivity.this,wxpay.getMch(), wxpay.getPrepay()); 
					finish();
					//WXTools.getInstance().payToWxByInfo(StoreCreateBillingActivity.this, wxpay.getMch(), wxpay.getPrepay(), wxpay.getSign(), wxpay.getNonce());
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_wallet_recharge, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
