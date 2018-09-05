package com.school.schoolapp;

import com.google.gson.Gson;
import com.school.schoolapp.application.ActivityApplication;
import com.school.schoolapp.classes.users.UserCallback;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseFragmentActivity extends FragmentActivity {
	protected Button doBtn;
	protected ImageButton backBtn;
	protected TextView titleTextView;
	protected ActionBar actionBar;
	protected UserCallback user;

	protected View view;
	protected String ticket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_fragment);
		ActivityApplication.getInstance().addActivity(this);
		setActionBarLayout();
		getTicket();
	}
	public String getTicket(){

		SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
		String userStr = sp.getString("USER_KEY", "NULL");
		Gson gson = new Gson();
		user = gson.fromJson(userStr, UserCallback.class);
		if(user!=null)
		    return user.getTicket();
		return null;
	}
	public void setActionBarLayout() {
		 ActionBar actionbar = getActionBar();
	        if(null != actionbar){
	        	actionbar.setDisplayShowHomeEnabled(false);
	        	actionbar.setDisplayShowCustomEnabled(true);
	        	LayoutInflater mInflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        	View v = mInflator.inflate(R.layout.actionbar_custom,null);
	        	view =v;
	            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	            actionbar.setCustomView(v,layout);
	            
	            this.titleTextView = (TextView)v.findViewById(R.id.titleTextView);
	            this.doBtn = (Button)v.findViewById(R.id.doButton);
	            this.backBtn = (ImageButton)v.findViewById(R.id.backButton);
	            this.backBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseFragmentActivity.this.finish();
					}
				});
	            
	            this.actionBar =actionbar;
	        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
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
