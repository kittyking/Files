package com.school.schoolapp.activity.store;

import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class StorePaySuccessActivity extends BaseActivity {
	
	private int way=0;//0零食货到付款

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_pay_success);
		
		ImageView img = (ImageView)findViewById(R.id.img);
		
		String type = getIntent().getStringExtra("type");
		if(type !=null && type.equals("recharge")){
			this.titleTextView.setText("充值成功");
			ImageLoaderTool.getInstance().displayImage("drawable://"+R.drawable.background_recharge_success, img, this);
		}else{
			this.titleTextView.setText("下单成功");
			ImageLoaderTool.getInstance().displayImage("drawable://"+R.drawable.background_pay_success, img, this);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_pay_success, menu);
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
