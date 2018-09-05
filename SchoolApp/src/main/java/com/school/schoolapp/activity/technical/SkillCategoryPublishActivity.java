package com.school.schoolapp.activity.technical;

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
import com.school.schoolapp.callback.technical.TechnicalCategoryCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SkillCategoryPublishActivity extends BaseActivity {

	private ListView categoryLV;
	private TechnicalCategoryCallback category;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skill_category_publish);
		categoryLV=(ListView)findViewById(R.id.categoryLV);
		requestSkillCategory();
		
	}

	
	//获取技能分类
	public void requestSkillCategory(){
		String url = getString(R.string.base_url) + getString(R.string.technical_get_skill_category);
		RequestParams params = new RequestParams();
		final UserCallback user = LocalSharedPreferenceSingleton.getInstance().getUserInfo(SkillCategoryPublishActivity.this);
		params.put("ticket", user.getTicket());
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				category = new Gson().fromJson(new String(arg2), TechnicalCategoryCallback.class);
				if(category.getResult().equals("1") && category.getData().getList().size()>0){
					categoryLV.setAdapter(new CategoryAdapter());
					categoryLV.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Intent intent = getIntent();
							intent.putExtra("name", category.getData().getList().get(position).getName());
							intent.putExtra("cid", category.getData().getList().get(position).getCid());
							setResult(101,intent);
							finish();
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
	private class CategoryAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return category.getData().getList().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView=LayoutInflater.from(SkillCategoryPublishActivity.this).inflate(android.R.layout.simple_list_item_1, null);
			TextView txt =  (TextView)convertView.findViewById(android.R.id.text1);
			txt.setText(category.getData().getList().get(position).getName());
			return convertView;
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.skill_category_publish, menu);
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
