package com.school.schoolapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.school.schoolapp.activity.home.HomeMainActivity;
import com.school.schoolapp.application.ActivityApplication;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		
		ActivityApplication.getInstance().addActivity(this);
		Handler handler = new Handler();
		handler.postDelayed(new splashhandler(), 2000);
		

	}

	class splashhandler implements Runnable{
        public void run() {
			if(LocalSharedPreferenceSingleton.getInstance().isShowGuide(SplashScreenActivity.this)){
				
				Intent intent = new Intent(SplashScreenActivity.this,GuideScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}else {
				//String s = LocalSharedPreferenceSingleton.getInstance().getUserInfo(SplashScreenActivity.this);
				//if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(SplashScreenActivity.this)!=null && !LocalSharedPreferenceSingleton.getInstance().getUserInfo(SplashScreenActivity.this).equals("NULL")){
	    			initHX();
	    		//}else{
	    		//	Intent intent = new Intent(SplashScreenActivity.this,UserFastLoginActivity.class);
	    		//	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	    		//	startActivity(intent);
	    		//}
			}
			
        	
        }
	}
	
	
	private void initHX(){
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
	         //return;
	     }else{
	    	 
	    	 EMClient.getInstance().init(this, options);//初始化
	    	 Log.i("EMClient", "My Debug:初始化环信");
	    	 //EMClient.getInstance().setDebugMode(true);//debug模式 在做打包混淆时需关闭debug模式
	    	 //登录用户
	    	 String mobile = LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getMobile();
	    	 String pwd = LocalSharedPreferenceSingleton.getInstance().getUserPwd(this);
	    	 EMClient.getInstance().login(mobile,pwd, new EMCallBack() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();
				    Log.d("main", "My Debug:登录聊天服务器成功！");	
				    //跳转到主页
				    Intent intent = new Intent(SplashScreenActivity.this,HomeMainActivity.class);
				    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				    initJpush(LocalSharedPreferenceSingleton.getInstance().getUserInfo(SplashScreenActivity.this).getTicket());
				    
	    			startActivity(intent);
				}
				
				@Override
				public void onProgress(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Looper.prepare();
					Toast.makeText(SplashScreenActivity.this, "登录聊天器失败", Toast.LENGTH_SHORT).show();
					Log.d("main", "My Debug:登录聊天服务器失败！"+arg1);
					Looper.loop();
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
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
