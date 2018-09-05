package com.school.schoolapp.activity.login;

import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.activity.home.HomeMainActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.MD5paser;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.thuongnh.zprogresshud.ZProgressHUD;

public class UserLoginActivity extends BaseActivity {

	private EditText phoneET, pwdET;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		this.titleTextView.setText("登录");

		setupEditText();

		this.backBtn.setVisibility(View.GONE);
		this.doBtn.setText("忘记密码");
		this.doBtn.setTextSize(14);
		this.doBtn.setAlpha(1);
		this.doBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UserLoginActivity.this,
						UserRecoverdActivity.class);
				startActivity(intent);
			}
		});

		Button loginBtn = (Button) findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 需要加入判空等处理
				networkAccess(phoneET.getText().toString(),
						MD5paser.getMd5UTF8(pwdET.getText().toString()));
			}
		});

		Button registerBtn = (Button) findViewById(R.id.registerBtn);
		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UserLoginActivity.this,
						UserRegisterActivity.class);
				startActivity(intent);
			}
		});

		Button fastLoginBtn = (Button) findViewById(R.id.fastLoginBtn);
		fastLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UserLoginActivity.this,
						UserFastLoginActivity.class);
				startActivity(intent);
			}
		});
	}

	public void setupEditText() {
		phoneET = (EditText) findViewById(R.id.phoneEditText);
		phoneET.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				EditText _v = (EditText) v;
				if (!hasFocus) {// 失去焦点
					_v.setHint(_v.getTag().toString());
				} else {
					String hint = _v.getHint().toString();
					_v.setTag(hint);
					_v.setHint("");
				}
			}
		});

		pwdET = (EditText) findViewById(R.id.pwdEditText);
		pwdET.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				EditText _v = (EditText) v;
				if (!hasFocus) {// 失去焦点
					_v.setHint(_v.getTag().toString());
				} else {
					String hint = _v.getHint().toString();
					_v.setTag(hint);
					_v.setHint("");
				}
			}
		});
	}

	public void networkAccess(String phone, final String pwd) {
		String url = getString(R.string.base_url)
				+ getString(R.string.user_login_url);

		RequestParams params = new RequestParams();
		params.put("username", phone);
		params.put("password", pwd);

		final ZProgressHUD progressHUD = ZProgressHUD.getInstance(this);
		progressHUD.setMessage("正在登录");
		progressHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				progressHUD.dismiss();
				Gson gson = new Gson();
				BaseCallback callback = gson.fromJson(new String(arg2),
						BaseCallback.class);
				if (callback.getResult().equals("0")) {
					Toast toast = Toast.makeText(UserLoginActivity.this,
							callback.getMsg(), Toast.LENGTH_SHORT);
					toast.show();
				} else {
					// 保存返回的用户信息和密码
					UserCallback user = gson.fromJson(new String(arg2),
							UserCallback.class);
					initHX(user, arg2, pwd);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				ToastTool.showNetworkError(getApplicationContext());
				progressHUD.dismiss();
			}
		});
	}

	private void initHX(final UserCallback callback, final byte[] arg2,
			final String pwd) {
		// 初始化环信
		EMOptions options = new EMOptions();
		options.setAcceptInvitationAlways(true); // 设置添加好友时是否需要验证 true不需要验证
												 // false需要验证
		// options.setAutoLogin(false);//设置SDK不自动登录
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果APP启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process
		// name就立即返回

		if (processAppName == null
				|| !processAppName.equalsIgnoreCase(this.getPackageName())) {
			// Log.e(TAG, "enter the service process!");

			// 则此application::onCreate 是被service 调用的，直接返回
			// return;
		} else {

			EMClient.getInstance().init(this, options);// 初始化
			Log.i("EMClient", "My Debug:初始化环信");
			// EMClient.getInstance().setDebugMode(true);//debug模式
			// 在做打包混淆时需关闭debug模式
			// 登录用户
			EMClient.getInstance().login(callback.getData().getMobile(), pwd,
					new EMCallBack() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							EMClient.getInstance().groupManager()
									.loadAllGroups();
							EMClient.getInstance().chatManager()
									.loadAllConversations();
							Log.d("main", "My Debug:登录聊天服务器成功！");
							// 跳转到主页
							LocalSharedPreferenceSingleton.getInstance()
									.setUserInfo(UserLoginActivity.this,
											new String(arg2), pwd);

							if (callback.getData().getSchoolID().equals("0"))
								startActivity(new Intent(
										UserLoginActivity.this,
										UserCityActivity.class));
							else {
								Intent intent = new Intent(
										UserLoginActivity.this,
										HomeMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						}

						@Override
						public void onProgress(int arg0, String arg1) {
						}

						@Override
						public void onError(int arg0, String arg1) {
							Looper.prepare();
							Toast.makeText(UserLoginActivity.this, "登录聊天器失败",
									Toast.LENGTH_SHORT).show();
							Log.d("main", "My Debug:登录聊天服务器失败！" + arg1);
							Looper.loop();
						}
					});
		}
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
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
		getMenuInflater().inflate(R.menu.user_login, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return false;
		}
		return false;
	}
}
