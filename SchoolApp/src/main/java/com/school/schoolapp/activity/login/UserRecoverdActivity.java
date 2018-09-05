package com.school.schoolapp.activity.login;

import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.tools.MD5paser;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.classes.users.VerifyCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRecoverdActivity extends BaseActivity {

	private String verifyCode="";
	private int countdown = 120;
	private Button verifyBtn;
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_recoverd);
		
		this.titleTextView.setText("忘记密码");
		
		Button recoveredBtn = (Button)findViewById(R.id.recoveredBtn);
		recoveredBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText phone = (EditText)findViewById(R.id.phoneEditText);
				EditText pwd = (EditText)findViewById(R.id.pwdEditText);
				EditText verify = (EditText)findViewById(R.id.verifyEditText);
				
				if(verify.getText().toString().equals("") || verifyCode.equals("")){
					Toast toast = Toast.makeText(UserRecoverdActivity.this, "请输入验证码", Toast.LENGTH_SHORT);
					toast.show();
					return;
				}else if(verify.getText().toString().equals(verifyCode)){
					networkAccess(phone.getText().toString(),MD5paser.getMd5UTF8(pwd.getText().toString()) ,verify.getText().toString());
				}else {
					Toast toast = Toast.makeText(UserRecoverdActivity.this, "验证码错误，请重新输入。", Toast.LENGTH_SHORT);
					toast.show();
				}
				//需要加入判空等处理
				
			}
		});
		
		verifyBtn = (Button)findViewById(R.id.verifyBtn);
		verifyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText phone = (EditText)findViewById(R.id.phoneEditText);
				
			    getVerifyCode(phone.getText().toString());
					
			}
		});
	}
	
	public void getVerifyCode(String phonenum){
        String url = getString(R.string.base_url) + getString(R.string.user_verify_url);
		
		RequestParams params = new RequestParams();
		params.put("username", phonenum);
		params.put("type", 3);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					setupTimer();
					
					VerifyCallback verifyCB = gson.fromJson(new String(arg2), VerifyCallback.class);
					verifyCode = verifyCB.getCode();
				}else{
					Toast toast = Toast.makeText(UserRecoverdActivity.this, callback.getMsg(), Toast.LENGTH_SHORT);
					toast.show();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void setupTimer(){
		//此处加入设置倒计时
	    timer = new Timer();
	    timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						verifyBtn.setText(""+countdown+"秒");
						verifyBtn.setBackgroundResource(R.drawable.background_register_verify_btn_timer);
						countdown--;
						if(countdown <0){
							timer.cancel();
							verifyBtn.setText("重发");
							verifyBtn.setBackgroundResource(R.drawable.background_register_verify_btn);
							countdown =60;
						}
					}
				});
			}
		},1000, 1000);
	}
	
	public void networkAccess(String phone,String pwd,String verify) {
		String url = getString(R.string.base_url) + getString(R.string.user_recovered_url);
		
		RequestParams params = new RequestParams();
		params.put("username", phone);
		params.put("password", pwd);
		params.put("phonecode", verify);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("0")){
					Toast toast = Toast.makeText(UserRecoverdActivity.this,callback.getMsg(), Toast.LENGTH_SHORT);
					toast.show();
				}else{
					Toast toast = Toast.makeText(UserRecoverdActivity.this,callback.getMsg(), Toast.LENGTH_SHORT);
					toast.show();
					
					UserRecoverdActivity.this.finish();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_recoverd, menu);
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
