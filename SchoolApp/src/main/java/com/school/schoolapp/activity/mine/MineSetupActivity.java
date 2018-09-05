package com.school.schoolapp.activity.mine;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.activity.login.UserFastLoginActivity;
import com.school.schoolapp.activity.login.UserLoginActivity;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MineSetupActivity extends BaseActivity {

	private ToggleButton mTogBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_setup);
		this.titleTextView.setText("设置");
		
		mTogBtn = (ToggleButton) findViewById(R.id.mTogBtn); // 获取到控件
		mTogBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					//选中
				}else{
					//未选中
				}
			}
		});
		
		
	}
	
	public void logout(View v){
		EMClient.getInstance().logout(true, new EMCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				//设置极光别名为空，不再收到推送
				JPushInterface.setAlias(MineSetupActivity.this, "", new TagAliasCallback() {
					
					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2) {
						// TODO Auto-generated method stub
					    if(arg0 == 0){//设置成功

							LocalSharedPreferenceSingleton.getInstance().clearUserInfo(MineSetupActivity.this);
							Intent intent = new Intent(MineSetupActivity.this,UserFastLoginActivity.class);
			    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
					    }	
					}
				});
			}
			
			@Override
			public void onProgress(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ToastTool.showWithMessage("退出失败，请检查网络连接", MineSetupActivity.this);
			}
		});
		
	}
	
	public void informationSetup(View v){
		startActivity(new Intent(this,MineInfomationSetupActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_setup, menu);
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
