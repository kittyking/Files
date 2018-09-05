package com.school.schoolapp.activity.mine.seller;

import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.mine.MineStatusAdapter;
import com.school.schoolapp.adapter.mine.MineServiceSoldAdapter;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MineServiceSoldActivity extends MineServiceBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_service_sold);
		this.titleTextView.setText("已售服务");
		
		statusLinear =(LinearLayout)findViewById(R.id.statusLinear);
		statusRelative=(RelativeLayout)findViewById(R.id.statusRelative);
		statusRelative.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(statusLinear.getVisibility()==View.GONE)
					statusLinear.setVisibility(View.VISIBLE);
				else
					statusLinear.setVisibility(View.GONE);
			}
		});

		statusList =(ListView)findViewById(R.id.statusList);
		statusList.setAdapter(new MineStatusAdapter(this,status));
		statusList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//选中或者取消
				if(isChecked[position]==false){
					isChecked[position] = true;
					RadioButton check = (RadioButton)view.findViewById(R.id.checked);
					check.setChecked(true);
				}
				else{
					isChecked[position] = false;
					RadioButton check = (RadioButton)view.findViewById(R.id.checked);
					check.setChecked(false);
				}
				
				
				
			}
		});
		
		infoList=(ListView)findViewById(R.id.soldList);
		infoList.setAdapter(new MineServiceSoldAdapter(MineServiceSoldActivity.this));
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_service_sold, menu);
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
