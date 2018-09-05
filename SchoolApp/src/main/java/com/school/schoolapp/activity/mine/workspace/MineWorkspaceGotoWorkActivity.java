package com.school.schoolapp.activity.mine.workspace;

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
import com.school.schoolapp.activity.billing.BillingBusinessActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.workspace.WorkspaceWorkStatusCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MineWorkspaceGotoWorkActivity extends BaseActivity {
	
	private Button workBtn;
	private Button restBtn;
	private Button recordBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_workspace_goto_work);
		this.titleTextView.setText("我要工作");
	
		//初始化上班状态
		String url = getString(R.string.base_url)+getString(R.string.workspace_work_status);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				WorkspaceWorkStatusCallback status = gson.fromJson(new String(arg2),WorkspaceWorkStatusCallback.class);
				if(status.getResult().equals("1")){
					//设置上班状态
					if(status.getData().getOpened().equals("1")){
						restBtn.setAlpha(1);
						workBtn.setBackground(null);
						workBtn.setTextColor(Color.BLACK);
						workBtn.setText("工作中");
					}
					//设置时间
					TextView dateTV = (TextView)findViewById(R.id.dateTV);
					dateTV.setText(status.getData().getDate()+"   "+status.getData().getWeek());
					TextView onlineTV= (TextView)findViewById(R.id.onlineTV);
					onlineTV.setText(status.getData().getDiff());
					
					((RelativeLayout)findViewById(R.id.status)).setAlpha(1);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		workBtn = (Button)findViewById(R.id.workButton);
		workBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				restBtn.setAlpha(1);
				workBtn.setBackground(null);
				workBtn.setTextColor(Color.BLACK);
				workBtn.setText("工作中");
				setShopState(getString(R.string.workspace_shop_open));
			}
		});
		
		restBtn = (Button)findViewById(R.id.restButton);
		restBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				restBtn.setAlpha(0);
				workBtn.setBackground(getResources().getDrawable(R.drawable.background_circle_blue));
				workBtn.setTextColor(Color.WHITE);
				workBtn.setText("上班");
				setShopState(getString(R.string.workspace_shop_close));
			}
		});
		
		recordBtn = (Button)findViewById(R.id.recordButton);
		recordBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MineWorkspaceGotoWorkActivity.this,BillingBusinessActivity.class);
				startActivity(intent);
			}
		});
		
	}
	public void setShopState(String subUrl){
		String url = getString(R.string.base_url) + subUrl;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = new BaseCallback();
				callback = gson.fromJson(new String(arg2), BaseCallback.class);
				Toast.makeText(MineWorkspaceGotoWorkActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.mine_workspace_goto_wor, menu);
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
