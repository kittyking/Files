package com.school.schoolapp.activity.login;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.RegistAgreementActivity;
import com.school.schoolapp.activity.home.HomeMainActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.classes.users.UserRegisterCallback;
import com.school.schoolapp.classes.users.VerifyCallback;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserFastLoginActivity extends Activity {
	
	private String verifyCode="";
	private int countdown = 120;
	private Button verifyBtn;
	private Timer timer;
	private EditText phone,code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_fast_login);
		
		((Button)findViewById(R.id.explainBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(UserFastLoginActivity.this,RegistAgreementActivity.class));
			}
		});
		
		verifyBtn = (Button)findViewById(R.id.verifyBtn);
		verifyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method
				hideKeyboard(v);
			    getVerifyCode(phone.getText().toString());
			}
		});
		
		code = (EditText)findViewById(R.id.verifyEditText);
		phone = (EditText)findViewById(R.id.phoneEditText);
		
		Button loginBtn = (Button)findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				networkAccess(phone.getText().toString(), code.getText().toString());
			}
		});
		
		
	}
	public void networkAccess(String phone,String verify) {
		String url = getString(R.string.base_url) + getString(R.string.user_login_fast);
		
		RequestParams params = new RequestParams();
		params.put("username", phone);
		params.put("phonecode", verify);
		
		final ZProgressHUD progressHUD = ZProgressHUD.getInstance(this);
		progressHUD.setMessage("正在登录");
		progressHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				Gson gson = new Gson();
				BaseCallback callback = gson.fromJson(new String(arg2),
						BaseCallback.class);
				if (callback.getResult().equals("0")) {
					Toast toast = Toast.makeText(UserFastLoginActivity.this,
							callback.getMsg(), Toast.LENGTH_SHORT);
					toast.show();
				} else {
					// 保存返回的用户信息和密码
					UserRegisterCallback user = gson.fromJson(new String(arg2),
							UserRegisterCallback.class);
					initHX(user,arg2);
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				ToastTool.showWithMessage("登录失败，请检查网络环境", UserFastLoginActivity.this);
			}
		});
		
	}
	
	
	private void initHX(final UserRegisterCallback callback,final byte[] arg2){
		//初始化环信
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(true); //设置添加好友时是否需要验证 true不需要验证 false需要验证
        //options.setAutoLogin(false);//设置SDK不自动登录
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
	     // 如果APP启用了远程的service，此application:onCreate会被调用2次
	     // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
	     // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
	      
	     if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
	         //Log.e(TAG, "enter the service process!");
	      
	         // 则此application::onCreate 是被service 调用的，直接返回
	         return;
	     }else{
	    	 
	    	 EMClient.getInstance().init(this, options);//初始化
	    	 Log.i("EMClient", "My Debug:初始化环信");
	    	 //EMClient.getInstance().setDebugMode(true);//debug模式 在做打包混淆时需关闭debug模式
	    	 //登录用户
	    	 EMClient.getInstance().login(callback.getData().getMobile(),callback.getData().getPassword(), new EMCallBack() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();
					
					// 跳转到主页
					LocalSharedPreferenceSingleton.getInstance()
							.setUserInfo(UserFastLoginActivity.this,
									new String(arg2), callback.getData().getPassword());
					LocalSharedPreferenceSingleton.getInstance()
					.setUsername(UserFastLoginActivity.this,callback.getData().getUserName());
					Intent intent = new Intent(
							UserFastLoginActivity.this,
							HomeMainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					if(callback.getCoupon()!=null && callback.getCoupon().size()>0)
				    	intent.putExtra("coupon",(Serializable)callback.getCoupon());
					initJpush(callback.getTicket());
					startActivity(intent);
				}
				
				@Override
				public void onProgress(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Log.d("main", "My Debug:登录聊天服务器失败！"+arg1);
				}
			});
	     }
	}
	private void initJpush(String username){
		//设置Jpush
		//JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setAlias(this, username, null);

	}
	
	public void hideKeyboard(View v){
		
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	
	public void getVerifyCode(String phonenum){
        String url = getString(R.string.base_url) + getString(R.string.user_verify_url);
		
		RequestParams params = new RequestParams();
		params.put("username", phonenum);
		params.put("type",2);
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
					Log.i("","验证码"+verifyCode);
					Toast.makeText(UserFastLoginActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();;
				}else{
					Toast toast = Toast.makeText(UserFastLoginActivity.this, callback.getMsg(), Toast.LENGTH_SHORT);
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
						//verifyBtn.setBackgroundResource(R.drawable.background_register_verify_btn_timer);
						countdown--;
						if(countdown <0){
							timer.cancel();
							verifyBtn.setText("重发");
							verifyBtn.setBackgroundResource(R.drawable.background_register_verify_btn);
							countdown =120;
						}
					}
				});
			}
		},1000, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_fast_login, menu);
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
