package com.school.schoolapp.activity.store;

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
import com.school.schoolapp.R.string;
import com.school.schoolapp.activity.login.UserSchoolActivity;
import com.school.schoolapp.activity.mine.work.MineWorkBuildingChooseActivity;
import com.school.schoolapp.adapter.user.UserSchoolAdapter;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserSchoolCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class StoreSchoolActivity extends BaseActivity {

	private ListView schoolLV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_school);
		this.titleTextView.setText("选择学校");
		schoolLV = (ListView)findViewById(R.id.schoolList);
		
		Intent intent = getIntent();
		String cityId = intent.getStringExtra("CityId");
		String url = getString(R.string.base_url) + getString(R.string.user_school_get);
		RequestParams params = new RequestParams();
		params.put("cityid", cityId);
		final AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				final Gson gson = new Gson();
				final UserSchoolCallback schools = gson.fromJson(new String(arg2), UserSchoolCallback.class);
				if(schools.getResult().equals("1")){
					UserSchoolAdapter adapter = new UserSchoolAdapter(StoreSchoolActivity.this, schools.getData());
					schoolLV.setAdapter(adapter);
					schoolLV.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(StoreSchoolActivity.this, StoreBuildingActivity.class);
							intent.putExtra("schoolid", schools.getData().get(position).getSchoolID());
							startActivity(intent);
						}
					});
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(StoreSchoolActivity.this);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_school, menu);
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
