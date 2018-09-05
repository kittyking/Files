package com.school.schoolapp;

import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.classes.users.UserCallback;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseActivity extends Activity {
	
	protected Button doBtn;
	protected ImageButton backBtn;
	protected TextView titleTextView;
	
	protected String ticket;
	
	protected UserCallback user;
	
	protected View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		setActionBarLayout();
		
		
		ticket = getTicket();
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
						BaseActivity.this.finish();
					}
				});
	        }
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(BaseActivity.this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(BaseActivity.this);
	}
}
