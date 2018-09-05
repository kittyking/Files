package com.school.schoolapp.activity.login;

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
import com.school.schoolapp.activity.home.HomeMainActivity;
import com.school.schoolapp.activity.mine.work.MineWorkBuildingChooseActivity;
import com.school.schoolapp.adapter.user.UserSchoolAdapter;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.AddressDefaultCallback.DefalutVO.ShoolFloorVO;
import com.school.schoolapp.classes.users.UserSchoolCallback;
import com.school.schoolapp.entity.user.UserSchoolVO;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserSchoolActivity extends BaseActivity {
	
	private ListView schoolLV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_school);
		this.titleTextView.setText("选择学校");
		
		schoolLV = (ListView)findViewById(R.id.schoolList);
		
		String url = getString(R.string.base_url) + getString(R.string.user_school_get);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		final AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				final Gson gson = new Gson();
				final UserSchoolCallback schools = gson.fromJson(new String(arg2), UserSchoolCallback.class);
				if(schools.getResult().equals("1")){
					UserSchoolAdapter adapter = new UserSchoolAdapter(UserSchoolActivity.this, schools.getData());
					schoolLV.setAdapter(adapter);
					schoolLV.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// 选中地址后进行绑定
							String url = getString(R.string.base_url) + getString(R.string.user_school_set);
							RequestParams params = new RequestParams();
							params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(UserSchoolActivity.this).getTicket());
							params.put("schoolid", schools.getData().get(position).getSchoolID());
							final String schoolid=schools.getData().get(position).getSchoolID();
							final String schoolname=schools.getData().get(position).getSchoolName();
							client.post(url, params, new AsyncHttpResponseHandler() {
								
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									// TODO Auto-generated method stub
									BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
									if(callback.getResult().equals("1")){
										
										Intent intent = new Intent();
										intent.setClass(UserSchoolActivity.this, MineWorkBuildingChooseActivity.class);
										intent.putExtra("schoolname", schoolname);
										intent.putExtra("schoolid", schoolid);
										startActivity(intent);
										
										//保存用户选择的学校
										
									}else{
										Toast.makeText(UserSchoolActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
									}
								}
								
								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									// TODO Auto-generated method stub
									ToastTool.showNetworkError(getApplicationContext());
								}
							});
						}
					});
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
		getMenuInflater().inflate(R.menu.user_school, menu);
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
