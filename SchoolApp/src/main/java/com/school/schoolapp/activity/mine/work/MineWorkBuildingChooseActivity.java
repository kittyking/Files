package com.school.schoolapp.activity.mine.work;

import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.SplashScreenActivity;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.activity.home.HomeMainActivity;
import com.school.schoolapp.activity.login.UserCityActivity;
import com.school.schoolapp.activity.login.UserFastLoginActivity;
import com.school.schoolapp.activity.login.UserSchoolActivity;
import com.school.schoolapp.adapter.user.UserFloorChooseAdapter;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.FloorChooseCallback;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.classes.users.UserRegisterCallback;
import com.school.schoolapp.entity.user.UserFloorVO;

import android.R.bool;
import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MineWorkBuildingChooseActivity extends BaseActivity {
	
	private Button manBtn;
	
	private Button womanBtn;
	
	private ListView floorListView;

	private UserFloorChooseAdapter adapter;
	
	private String from,schoolname,schoolid;
	
	private List<UserFloorVO> floors = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_work_building_choose);
		this.titleTextView.setText("选择楼栋");
		
		from=getIntent().getStringExtra("from");
		schoolname=getIntent().getStringExtra("schoolname");
		schoolid=getIntent().getStringExtra("schoolid");
		
		this.doBtn.setText("城市");
		this.doBtn.setAlpha(1);
		this.doBtn.setTextSize(16);
		this.doBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent(MineWorkBuildingChooseActivity.this,UserCityActivity.class);
				 startActivity(intent);
			}
		});
		
		manBtn = (Button)findViewById(R.id.manBtn);
		manBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				womanBtn.setBackground(null);
				manBtn.setBackgroundColor(Color.WHITE);
				networking(0);
			}
		});
		
		womanBtn = (Button)findViewById(R.id.womanBtn);
		womanBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				manBtn.setBackground(null);
				womanBtn.setBackgroundColor(Color.WHITE);
				networking(1);
			}
		});
		
		floorListView = (ListView)findViewById(R.id.floorListView);
		adapter = new UserFloorChooseAdapter(this, floors);
		floorListView.setAdapter(adapter);
		networking(0);
		
		
		floorListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final int floorIndex = position;
				
				saveUserFloor(position);
				if(floors.get(position).getOpened().equals("0")){
					ToastTool.showWithMessage("该楼层暂未开通", MineWorkBuildingChooseActivity.this);
					return;
				}
			}
		});
	}
	private void saveUserFloor(final int position){
		String url = getString(R.string.base_url)+getString(R.string.user_builling_choice);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(MineWorkBuildingChooseActivity.this).getTicket());
		if(floors == null)
			return;
		params.put("floorid", floors.get(position).getFloorID());
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				UserRegisterCallback callback = new UserRegisterCallback();
				callback = gson.fromJson(new String(arg2), UserRegisterCallback.class);
				if(callback.getResult().equals("1")){
					
					//LocalSharedPreferenceSingleton.getInstance().setUserFloor(MineWorkBuildingChooseActivity.this, floors.get(position).getFloorID(), floors.get(position).getFloorName());
					LocalSharedPreferenceSingleton.getInstance()
					.setUserInfo(MineWorkBuildingChooseActivity.this,
							new String(arg2), callback.getData().getPassword());
					if(from!=null && from.equals("workapply")){
						Intent intent = new Intent();
						intent.putExtra("floorid", floors.get(position).getFloorID());
						intent.putExtra("floorname", floors.get(position).getFloorName());
						
						setResult(RESULT_OK, intent);
						finish();
					}else{
						Intent intent =new Intent(MineWorkBuildingChooseActivity.this,HomeMainActivity.class);
					    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	public void networking(final int sex){
		String url = getString(R.string.base_url)+getString(R.string.user_builling_by_school);
		
		RequestParams params = new RequestParams();
		params.put("ticket", ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				FloorChooseCallback floorCallback = new FloorChooseCallback();
				floorCallback = gson.fromJson(new String(arg2), FloorChooseCallback.class);
				if(sex==0){//sex=0 男 sex=1 女
					adapter.setAdatperList(floorCallback.getData().getWanFloor());
					floors = floorCallback.getData().getWanFloor();
				}else{
					adapter.setAdatperList(floorCallback.getData().getWomanFloor());
					floors = floorCallback.getData().getWomanFloor();
				}
				
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	public void ceoApply(View v){
		startActivity(new Intent(this,MineWorkActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_work_building_choose, menu);
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
