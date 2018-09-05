package com.school.schoolapp;

import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.adapter.user.UserFloorChooseAdapter;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.FloorChooseCallback;
import com.school.schoolapp.entity.user.UserFloorVO;

import android.app.Activity;
import android.content.Intent;
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

public class FloorChooseActivity extends BaseActivity {
	private Button manBtn,womanBtn;
	
	private String schoolid;
	
	private ListView floorListView;

	private UserFloorChooseAdapter adapter;

	private List<UserFloorVO> floors = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_floor_choose);
		
		this.titleTextView.setText("选择楼栋");
		
		schoolid=getIntent().getStringExtra("schoolid");
		manBtn = (Button)findViewById(R.id.manBtn);
		manBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				floorChanged(0);
			}
		});
		womanBtn = (Button)findViewById(R.id.womanBtn);
		womanBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				floorChanged(1);
			}
		});
		
		
		floorListView = (ListView)findViewById(R.id.floorListView);
		adapter = new UserFloorChooseAdapter(this, floors);
		floorListView.setAdapter(adapter);
		floorChanged(0);
		floorListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("Floor", floors.get(position).getFloorName());
				intent.putExtra("FloorID", floors.get(position).getFloorID());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
	}
	private void floorChanged(int sex){//0男 1女
		if(sex == 0){
			womanBtn.setBackground(null);
			manBtn.setBackgroundColor(Color.WHITE);
		}else{
			manBtn.setBackground(null);
			womanBtn.setBackgroundColor(Color.WHITE);
		}
		networking(sex);
	}
	
	public void networking(final int sex){
		String url = getString(R.string.base_url)+getString(R.string.user_builling_by_schoolid);
		
		RequestParams params = new RequestParams();
		params.put("ticket", ticket);
		params.put("schoolid", schoolid);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.floor_choose, menu);
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
