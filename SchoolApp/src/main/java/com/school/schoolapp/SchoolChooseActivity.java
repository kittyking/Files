package com.school.schoolapp;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.activity.login.UserSchoolActivity;
import com.school.schoolapp.adapter.user.UserSchoolAdapter;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.users.UserSchoolCallback;
import com.school.schoolapp.entity.user.UserCityVO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SchoolChooseActivity extends BaseActivity {

	private ListView schoolLV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_choose);
		schoolLV=(ListView)findViewById(R.id.schoolLV);
		String url = getString(R.string.base_url) + getString(R.string.user_school_by_city);
		RequestParams params = new RequestParams();
		params.put("ticket",this.ticket);
		params.put("cid", getIntent().getStringExtra("cityid"));
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				final UserSchoolCallback schools = new Gson().fromJson(new String(arg2), UserSchoolCallback.class);
				if(schools.getResult().equals("1")){
					UserSchoolAdapter adapter = new UserSchoolAdapter(SchoolChooseActivity.this, schools.getData());
					schoolLV.setAdapter(adapter);
					schoolLV.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent();
							intent.putExtra("schoolid", schools.getData().get(position).getSchoolID());
							intent.putExtra("schoolname", schools.getData().get(position).getSchoolName());setResult(RESULT_OK, intent);
							finish();
							
						}
					});
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.school_choose, menu);
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
