package com.school.schoolapp.activity.technical;

import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TechnicalMenuActivity extends BaseActivity {

	private ListView levelListview;
	private LinearLayout levelLinear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_technical_menu);
		this.titleTextView.setText("选择技能类型");
		
		levelLinear= (LinearLayout)findViewById(R.id.level1);
		setupLevel1();
	}

	private String[] level1s={"生活技能","运动娱乐","IT互联网","设计媒体","兴趣特长","私人定制","授课辅导","职场技能"};
	private int index =0;
	public void setupLevel1(){
		for(int i=0;i<level1s.length;i++){
			final TextView tv =new TextView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			//params.setMargins(0, 20, 0, 20);
			tv.setLayoutParams(params);
			tv.setGravity(Gravity.CENTER);
			tv.setText(level1s[i]);
			tv.setTextSize(16);
			tv.setPadding(0, 30, 0, 30);
			levelLinear.addView(tv);
			if(i==0){
				tv.setBackgroundColor(Color.WHITE);
				tv.setTextColor(Color.YELLOW);
			}
			final int position = i;
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tv.setBackgroundColor(Color.WHITE);
					tv.setTextColor(Color.YELLOW);
					
					TextView lastTV= (TextView)levelLinear.getChildAt(index);
					lastTV.setBackground(null);
					lastTV.setTextColor(Color.BLACK);
					index = position;
				}
			});
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.technical_menu, menu);
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
