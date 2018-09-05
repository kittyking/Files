package com.school.schoolapp.activity.mine.workspace;

import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.activity.chat.ChatDialogListActivity;
import com.school.schoolapp.activity.chat.MessageDialogListActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.view.View.OnClickListener;
import android.widget.TabHost.TabSpec;

public class MineWorkspaceMainActivity extends  TabActivity{

	TabHost mTabHost ;
	int[] unselectedArray = {R.drawable.icon_workspace_unselected,R.drawable.icon_mail_unselected,R.drawable.icon_chat_unselected};
	int[] selectedArray = {R.drawable.icon_workspace_selected,R.drawable.icon_mail_selected,R.drawable.icon_chat_selected};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_workspace_main);
		
		mTabHost = getTabHost();
		mTabHost.getTabWidget().setDividerDrawable(null);
        TabSpec spec;
        
        spec = mTabHost.newTabSpec("工作台");
        spec.setIndicator(composeLayout("工作台", 0));
        spec.setContent(new Intent(this, MineWorkspaceActivity.class));
        mTabHost.addTab(spec);
        
        spec = mTabHost.newTabSpec("消息");    
        spec.setIndicator(composeLayout("消息",1));
        spec.setContent(new Intent(this, MessageDialogListActivity.class));
        mTabHost.addTab(spec);
        
        spec = mTabHost.newTabSpec("聊天");    
        spec.setIndicator(composeLayout("聊天", 2));
        spec.setContent(new Intent(this, ChatDialogListActivity.class));
        mTabHost.addTab(spec);
        
        mTabHost.setCurrentTab(4);   
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				setActionBarLayout(tabId);
				updateTab(mTabHost);
			}
		});
        
        updateTab(mTabHost);
        setActionBarLayout("工作台");
	}
	
	//设置tabhost图片和文字
		public View composeLayout(String s, int position) {

			// 定义一个LinearLayout布局

			LinearLayout layout = new LinearLayout(this);

			// 设置布局垂直显示

			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setGravity(Gravity.CENTER);

			ImageView iv = new ImageView(this);
			//iv.setPadding(20, 20, 20, 20);

			//imageList.add(iv);
			  

			iv.setImageResource(unselectedArray[position]);
			iv.setId(position);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

			lp.setMargins(0, 20, 0, 20);
			layout.addView(iv, lp);

			// 定义TextView

			TextView tv = new TextView(this);
			tv.setId(0x0010+position);

			tv.setGravity(Gravity.CENTER);

			tv.setSingleLine(true);

			tv.setText(s);

			tv.setTextColor(Color.rgb(158, 158, 158));

			tv.setTextSize(10);
			
			LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			tlp.setMargins(0, 0, 0, 10);
			
			layout.addView(tv,tlp);
			layout.setBackgroundColor(Color.rgb(246, 246, 246));
			return layout;

		}
		private void updateTab(TabHost tabHost) {
	        int curr = tabHost.getCurrentTab();
	        View view = tabHost.getTabWidget().getChildAt(curr);
	        //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_btn_bg));
	        
	        ImageView imageView = (ImageView)view.findViewById(curr);
	        imageView.setImageDrawable(getResources().getDrawable(selectedArray[curr]));
	        
	        TextView textView = (TextView)view.findViewById(0x0010+curr);
	        textView.setTextColor(Color.parseColor("#3E8FE0"));
	        
	        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
	            if(i!=curr){
	                View curr_view = tabHost.getTabWidget().getChildAt(i);

	                ImageView curr_imageView = (ImageView)curr_view.findViewById(i);
	                curr_imageView.setImageDrawable(getResources().getDrawable(unselectedArray[i]));
	                TextView curr_textView = (TextView)curr_view.findViewById(0x0010+i);
	                curr_textView.setTextColor(Color.rgb(158, 158, 158));
	            }
	        }
	    }
		public void setActionBarLayout(String title) {
			 ActionBar actionbar = getActionBar();
		        if(null != actionbar){
		        	actionbar.setDisplayShowHomeEnabled(false);
		        	actionbar.setDisplayShowCustomEnabled(true);
		        	LayoutInflater mInflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        	View v = mInflator.inflate(R.layout.actionbar_custom, null);
		            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		            actionbar.setCustomView(v,layout);
		            
		            TextView titleView = (TextView)v.findViewById(R.id.titleTextView);
		            titleView.setText(title);
//		            
		            ImageButton backButton = (ImageButton)v.findViewById(R.id.backButton);
		            backButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MineWorkspaceMainActivity.this.finish();
						}
					});
//		            backButton.setAlpha(1);
		            switch (mTabHost.getCurrentTab()) {
					case 0:
						titleView.setText("工作台");
						break;
					case 1:
						titleView.setText("消息");
						break;
					case 2:
						titleView.setText("聊天");
						break;
					default:
						break;
					}
		        }
		}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_workspace_main, menu);
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
