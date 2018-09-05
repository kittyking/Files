package com.school.schoolapp.activity.technical;

import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class TechnicalCategoryActivity extends BaseActivity {

	private GridLayout categoryGL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_technical_category);
		this.titleTextView.setText("选择分类");
		
		categoryGL = (GridLayout)findViewById(R.id.category);
		categoryGL.setColumnCount(3);
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		
		for(int i=0;i<10;i++){
			RelativeLayout rl = new RelativeLayout(this);
			LayoutParams params = new LayoutParams(width/3,width/3);
			
			TextView tv = new TextView(this);
			tv.setText("分类");
			tv.setGravity(Gravity.CENTER);
			
			Drawable topDrawable = getResources().getDrawable(R.drawable.fxq_bg);    
		    topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
		    tv.setCompoundDrawables(null, topDrawable, null, null);    
		       
			rl.addView(tv);
			rl.setGravity(Gravity.CENTER);
			rl.setLayoutParams(params);
			rl.setBackgroundColor(Color.WHITE);
			rl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(TechnicalCategoryActivity.this,TechnicalListActivity.class));
				}
			});
			categoryGL.addView(rl);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.technical_category, menu);
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
