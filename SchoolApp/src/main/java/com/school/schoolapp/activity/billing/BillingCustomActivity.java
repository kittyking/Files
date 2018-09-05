package com.school.schoolapp.activity.billing;

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
import com.school.schoolapp.activity.billing.BillingBusinessActivity.MyPagerAdapter;
import com.school.schoolapp.activity.mine.workspace.MineWorkspaceActivity;
import com.school.schoolapp.adapter.billing.BillingBusinessAdapter;
import com.school.schoolapp.adapter.billing.BillingCustomAdapter;
import com.school.schoolapp.classes.billing.BillingCallback;
import com.school.schoolapp.classes.billing.BillingCustomCountCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.view.refreshlist.RListView;
import com.school.schoolapp.view.refreshlist.RListView.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BillingCustomActivity extends BaseActivity {

	private List<ListView> goodListViews;
	
	private ViewPager viewpager;
	
	private int curPager = 1,flag=0;//flag标志是否下拉加载
	
	private int currentIndex = 0,whichPage;
	private Boolean canAdd = false;

	
	private int[] pagerId = {R.id.allTV,R.id.payTV,R.id.receiveTV,R.id.postingTV,R.id.postedTV};
	
	private LinearLayout allLinear,payLinear,receiveLinear,postingLinear,postedLinear;
	
	private int[] urlId = {R.string.billing_custom_all,R.string.billing_custom_nopay,R.string.billing_custom_noreceive,R.string.billing_custom_nopost,R.string.billing_custom_noget};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_custom);
		this.titleTextView.setText("订单管理");
		allLinear= (LinearLayout)findViewById(R.id.allLinear);
		allLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentIndex == 0)
					return;
				viewpager.setCurrentItem(0);
			}
		});
		payLinear= (LinearLayout)findViewById(R.id.payLinear);
		payBadgeView = new BadgeView(BillingCustomActivity.this, payLinear);  
		payLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentIndex == 1)
					return;
				viewpager.setCurrentItem(1);
			}
		});
		receiveLinear= (LinearLayout)findViewById(R.id.receiveLinear);
		receiveBadgeView = new BadgeView(BillingCustomActivity.this, receiveLinear);  
		receiveLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentIndex == 2)
					return;
				viewpager.setCurrentItem(2);
			}
		});
		postingLinear= (LinearLayout)findViewById(R.id.postingLinear);
		postingBadgeView = new BadgeView(BillingCustomActivity.this, postingLinear); 
		postingLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentIndex == 3)
					return;
				viewpager.setCurrentItem(3);
			}
		});
		postedLinear= (LinearLayout)findViewById(R.id.postedLinear);
		postedBadgeView = new BadgeView(BillingCustomActivity.this, postedLinear); 
		postedLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentIndex == 4)
					return;
				viewpager.setCurrentItem(4);
			}
		});
		
		getOrderStatusCount();
	    displayView();
		
	}
	
	
	public void getGoodsByPage(String subUrl,final BillingCustomAdapter adapter){
		String url = getString(R.string.base_url)+subUrl ;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("curpage", adapter.getCurpage());
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					Gson gson = new Gson();
					BillingCallback billing = new BillingCallback();
					billing = gson.fromJson(new String(arg2), BillingCallback.class);
					if(billing.getResult().equals("1")){
						if(billing.getData().size()>0){
							adapter.addBillings(billing.getData());
							adapter.notifyDataSetChanged();
						}	
					}
				} catch (Exception e) {
					// TODO: handle exception
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
			return goodListViews.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewGroup)container).removeView(goodListViews.get(position));
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewGroup)container).addView(goodListViews.get(position));
			return goodListViews.get(position);
		}
	}
	BadgeView payBadgeView,receiveBadgeView,postingBadgeView,postedBadgeView;
	public void getOrderStatusCount(){
		String url = getString(R.string.base_url)+getString(R.string.billing_custom_statuscount) ;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					Gson gson = new Gson();
					BillingCustomCountCallback count = gson.fromJson(new String(arg2), BillingCustomCountCallback.class);
				    if(count.getResult().equals("1")){
				    	if(!count.getData().getDfk().equals("0")){
					    	payBadgeView.setText(count.getData().getDfk());  
					    	payBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
					    	payBadgeView.show(); 
				    	}else
				    		payBadgeView.hide(); 
				    	
				    	if(!count.getData().getDjd().equals("0")){
					        receiveBadgeView.setText(count.getData().getDjd());  
					        receiveBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
					        receiveBadgeView.show(); 
				    	}else
				    		receiveBadgeView.hide(); 
				        
				    	if(!count.getData().getDfh().equals("0")){ 
					        postingBadgeView.setText(count.getData().getDfh());  
					        postingBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
					        postingBadgeView.show(); 
				    	}else
				    		postingBadgeView.hide(); 
				    	
				    	if(!count.getData().getPsz().equals("0")){ 
					        postedBadgeView.setText(count.getData().getPsz());  
					        postedBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
					        postedBadgeView.show(); 
				    	}else
				    		postedBadgeView.hide(); 
				        
				        
				        
				    }
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		currentIndex = 0;
		for(int i=0;i<pagerId.length;i++){
			TextView tv = (TextView)findViewById(pagerId[i]);
			tv.setTextColor(Color.BLACK);
		}
		TextView tv = (TextView)findViewById(pagerId[0]);
		tv.setTextColor(Color.rgb(239,91,79));
		displayView();
	}
	
	private void displayView(){
		goodListViews = new ArrayList<ListView>();
		for(int i=0; i<5;i++){
			final RListView listview = new RListView(this);

			final BillingCustomAdapter adapter = new BillingCustomAdapter(this,this.ticket);
			
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(BillingCustomActivity.this,BillingPayActivity.class);
					intent.putExtra("OrderId", adapter.getBillings().get(position-1).getOrderID());
					startActivityForResult(intent,101);
				}
			});
			final int position = i;
			listview.setonRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					adapter.clearBillings();
					getGoodsByPage(getString(urlId[position]), adapter);
					listview.onRefreshComplete();
					getOrderStatusCount();
				}
			});
			listview.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        	getGoodsByPage(getString(urlId[position]), adapter);
                        	listview.onRefreshComplete();
                        } 
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					if (firstVisibleItem == 0) {  
						listview.isRefreshable = true;  
	                 } else {  
	                	 listview.isRefreshable = false;  
	                 } 
				}
			});
			
			goodListViews.add(listview);
			
			whichPage = i;
			getGoodsByPage(getString(urlId[i]),adapter);
			
		}
		
		viewpager = (ViewPager)findViewById(R.id.goodsViewPager);
		viewpager.setAdapter(new MyPagerAdapter());
		viewpager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				try {
					if(arg0==currentIndex)
						return;
					currentIndex = arg0;
					for(int i=0;i<pagerId.length;i++){
						TextView tv = (TextView)findViewById(pagerId[i]);
						tv.setTextColor(Color.BLACK);
					}
					TextView tv = (TextView)findViewById(pagerId[arg0]);
					tv.setTextColor(Color.rgb(239,91,79));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		viewpager.setCurrentItem(0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.billing_custom, menu);
		return false;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for(ListView lv:goodListViews)
			lv = null;
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
