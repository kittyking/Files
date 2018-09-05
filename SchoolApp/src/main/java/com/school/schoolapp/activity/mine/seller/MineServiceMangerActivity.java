package com.school.schoolapp.activity.mine.seller;

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
import com.school.schoolapp.adapter.mine.MineSillManagerAadapter;
import com.school.schoolapp.callback.technical.SkillManagerCallback;
import com.school.schoolapp.classes.tools.ToastTool;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MineServiceMangerActivity extends BaseActivity {

	private RadioGroup typeRG;
	private ListView serviceList;
	private MineSillManagerAadapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_service_manger);
		this.titleTextView.setText("管理服务");
		
		typeRG =(RadioGroup)findViewById(R.id.typeRG);
		typeRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.all:
					
					break;
				case R.id.already:
					
					break;
				case R.id.not:
	
					break;

				default:
					break;
				}
			}
		});
		
		serviceList=(ListView)findViewById(R.id.services);
		adapter = new  MineSillManagerAadapter(this,this.ticket);
		serviceList.setAdapter(adapter);
		requestSkill();
	}
	private int curpage = 1,onsale=0;
	private void requestSkill(){
		String url = getString(R.string.base_url)+getString(R.string.skill_manager_get_list);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("curpage", curpage);
		params.put("onsale", onsale);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				SkillManagerCallback manager = new Gson().fromJson(new String(arg2), SkillManagerCallback.class);
				
				if(manager.getResult().equals("1")){
					setupSkill(manager);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(MineServiceMangerActivity.this);
			}
		});
	}

	private void setupSkill(SkillManagerCallback manager){
		adapter.addList(manager.getData().getSkillList());
		adapter.notifyDataSetChanged();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_service_manger, menu);
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
