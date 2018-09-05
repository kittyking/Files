package com.school.schoolapp.activity.billing;

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
import com.school.schoolapp.activity.mine.work.MineWorkBuildingChooseActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.AddressDefaultCallback;
import com.school.schoolapp.classes.users.AddressEditCallback;
import com.school.schoolapp.classes.users.UserCallback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddressAddActivity extends BaseActivity {
	
	private TextView sexTV,schoolTV,buildingTV,floor;
	
	private EditText nameET,phoneET,addressET;
	
	private AddressDefaultCallback defaultCallback;
	
	private Spinner buildingSP;
	
	private String flag,addressId,floorid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_add);
		
		sexTV = (TextView)findViewById(R.id.sexTV);
		schoolTV= (TextView)findViewById(R.id.schoolTV);
		floor= (TextView)findViewById(R.id.floor);
		buildingSP = (Spinner)findViewById(R.id.buildingSP);
		
		nameET = (EditText)findViewById(R.id.nameET);
		phoneET= (EditText)findViewById(R.id.phoneET);
		addressET= (EditText)findViewById(R.id.addressET);
		
		Button saveBtn = (Button)findViewById(R.id.saveBtn);
		Intent intent = getIntent();
		flag = intent.getStringExtra("Flag");
		if(flag.equals("1")){//编辑地址
			this.titleTextView.setText("编辑地址");
			addressId = intent.getStringExtra("AddressID");
			String url = getString(R.string.base_url) + getString(R.string.user_address_edit);
			RequestParams params = new RequestParams();
			params.put("ticket", AddressAddActivity.this.ticket);
			params.put("addressid", addressId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					AddressEditCallback editcallback = gson.fromJson(new String(arg2), AddressEditCallback.class);
					if(editcallback.getResult().equals("1")){
						schoolTV.setText(editcallback.getData().getAddress().getSchoolName());
						nameET.setText(editcallback.getData().getAddress().getName());
						addressET.setText(editcallback.getData().getAddress().getAddress());
						phoneET.setText(editcallback.getData().getAddress().getMobile());
						//性别、楼栋设置
					}
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					ToastTool.showNetworkError(getApplicationContext());
				}
			});
			
			
		}else {//新建地址
			this.titleTextView.setText("新建地址");
			if(!LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getSchoolName().equals(""))
				schoolTV.setText(LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getSchoolName());
			}
		
		saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flag.equals("1"))
					uploadAddressInfo(getString(R.string.user_address_update));
				else
					uploadAddressInfo(getString(R.string.user_address_add));
			}
		});
	}
	
	public void uploadAddressInfo(String subString){
		String url = getString(R.string.base_url) + subString;
		RequestParams params = new RequestParams();
		params.put("ticket", AddressAddActivity.this.ticket);
		params.put("name", nameET.getText().toString());
		params.put("sex", sexTV.getText().toString());
		params.put("mobile", phoneET.getText().toString());
		params.put("address", addressET.getText().toString());
		params.put("floorid", floorid);//需要修改传入参数
		if(flag.equals("1")){
			params.put("addressid",addressId);
		}
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = new BaseCallback();
				callback = gson.fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1"))
					finish();
				else
					Toast.makeText(getApplicationContext(), callback.getMsg(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	public void buildingchoose(View v){
		Intent intent= new Intent(this,MineWorkBuildingChooseActivity.class);
		intent.putExtra("from", "workapply");
		startActivityForResult(intent, 103);
	}

	public void setDefaultInfo(){

		String url = getString(R.string.base_url) + getString(R.string.user_address_default);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				defaultCallback = new AddressDefaultCallback();
				defaultCallback = gson.fromJson(new String(arg2), AddressDefaultCallback.class);
				if(defaultCallback.getResult().equals("1")){
					schoolTV.setText(defaultCallback.getSchoolName());
					if(defaultCallback.getData().getFloor()!=null&&defaultCallback.getData().getFloor().size()>0)
						floorid=defaultCallback.getData().getFloor().get(0).getFloorID();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==RESULT_OK && requestCode==103){
			floor.setText(data.getStringExtra("floorname"));
			floorid = data.getStringExtra("floorid");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.address_add, menu);
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
