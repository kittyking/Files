package com.school.schoolapp.activity.mine.workspace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.store.StoreGoosAdapter;
import com.school.schoolapp.adapter.workspace.WorkspaceReplenishAdapter;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.classes.workspace.WorkspaceReplenishCallback;
import com.school.schoolapp.entity.store.StoreCartEntity;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MineWorkspaceReplenishActivity extends Activity {
	
	private ViewPager viewpager;
	
    private String curCid;
	
	private int curpage=1;
	
	private String curKeys;
	
	//private String ticket;
	
	private ArrayList<View> views = new ArrayList<View>();
	
	private List<String> titles = new ArrayList<String>();
	
	private static final String[] TITLE = new String[] { "全部", "急需补货", "需补货"};  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_workspace_replenish);
		((TextView)findViewById(R.id.storename)).setText("我要补货");
		
		StoreCartEntity.initCart();
		
		viewpager = (ViewPager)findViewById(R.id.replenishViewPager);
		viewpager.setAdapter(new MyPagerAdapter());
		//实例化TabPageIndicator然后设置ViewPager与之关联  
        TabLayout indicator = (TabLayout)findViewById(R.id.indicator);  
        indicator.setupWithViewPager(viewpager);
        
        
		titles.add("全部");
		titles.add("急需补货");
		titles.add("需补货");
		
		LayoutInflater inflater = getLayoutInflater();
		for(int i = 0 ; i < 3; i++){
			View view = inflater.inflate(R.layout.viewpager_billing_listview, null);
			ListView  lv = (ListView)view.findViewById(R.id.viewpagerListView);
			final int index = i;
			lv.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
                        if (view.getLastVisiblePosition() == view.getCount() - 1) { 
                        	curpage++;
                        	getListViewData(index);
                        }  
                    } 
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});
			WorkspaceReplenishAdapter adapter = new WorkspaceReplenishAdapter(MineWorkspaceReplenishActivity.this,null);
			lv.setAdapter(adapter);
			views.add(view);
			getListViewData(i);
		}
		
		Button submitBtn = (Button)findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				uploadReplenish();
			}
		});
		
		
		
		
	}
	
	public void uploadReplenish(){
		String url = getString(R.string.base_url)+getString(R.string.workspace_replenish_uploadgoodslist) ;
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		Gson gson = new Gson();
		String goodStr = gson.toJson(StoreCartEntity.getInstance().buyGoodsList());
		params.put("json", goodStr);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = new BaseCallback();
				callback = gson.fromJson(new String(arg2), BaseCallback.class);
				Toast.makeText(MineWorkspaceReplenishActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
				if(callback.getResult().equals("1"))
					MineWorkspaceReplenishActivity.this.finish();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	public void getListViewData(final int position){
		
		String url = getString(R.string.base_url)+getString(R.string.workspace_replenish_getgoodslist) ;
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		params.put("type", position);
		params.put("keys", curKeys);
		params.put("curpage", curpage);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				WorkspaceReplenishCallback replenish = new WorkspaceReplenishCallback();
				replenish = gson.fromJson(new String(arg2), WorkspaceReplenishCallback.class);
				if(replenish.getResult().equals("1")){
					ListView  lv = (ListView)views.get(position).findViewById(R.id.viewpagerListView);
					lv.setDivider(null);
					WorkspaceReplenishAdapter adapter = (WorkspaceReplenishAdapter)lv.getAdapter();
					adapter.add(replenish.getData().getGoodsList());
					adapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
		
	}
	
	public class MyPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TITLE.length;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewPager)container).addView(views.get(position));
			return views.get(position);
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager)container).removeView(views.get(position));
		}
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return TITLE[position%TITLE.length];
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_workspace_replenish, menu);
		return false;
	}

	public void finish(View v){
		finish();
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
