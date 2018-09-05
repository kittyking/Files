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
import com.school.schoolapp.activity.mine.workspace.MineWorkspaceAddGoodsActivity;
import com.school.schoolapp.adapter.billing.BillingBusinessAdapter;
import com.school.schoolapp.classes.billing.BillingCallback;
import com.school.schoolapp.classes.billing.BillingCustomCountCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.view.refreshlist.RListView;
import com.school.schoolapp.view.refreshlist.RListView.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class BillingBusinessActivity extends BaseActivity {
	
	private List<ListView> goodListViews;
	
	private ViewPager viewpager;
	
	
	private int currentIndex = 0,whichPage;
	
	private int screenWidth;  
	
	private ImageView smoothIV;
	
	private int sendingType = 1; //1极速 2定时
	
	private int[] urlId = {R.string.billing_business_all,R.string.billing_business_nopay,R.string.billing_business_noreceive,R.string.billing_business_nopost,R.string.billing_business_sending};

	private int[] pagerId = {R.id.allTV,R.id.payTV,R.id.receiveTV,R.id.postingTV,R.id.postedTV};
	
	private int curpage = 1,flag=0;
	
	private LinearLayout allLinear,payLinear,receiveLinear,postingLinear,postedLinear;
	private Handler updateCircleHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			getOrderStatusCount();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_business);
		this.titleTextView.setText("订单管理");
		
		smoothIV = (ImageView)findViewById(R.id.smoothIV);
	    
		goodListViews = new ArrayList<ListView>();
		for(int i=0; i<5;i++){
			final RListView listview = new RListView(this);
			final BillingBusinessAdapter adapter = new BillingBusinessAdapter(this,this.ticket);
			listview.setAdapter(adapter);
			goodListViews.add(listview);
			
			getGoodsByPage(1,getString(urlId[i]),i,adapter);
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// 进入订单详情页面
					Intent intent = new Intent(BillingBusinessActivity.this,BusinessBillingDetailActivity.class);
					intent.putExtra("OrderId", adapter.getBillings().get(position).getOrderID());
					startActivityForResult(intent,101);
				}
				
			});
			final int position = i;
			listview.setonRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					getOrderStatusCount();
					getGoodsByPage(1, getString(urlId[position]),position,adapter);
					listview.onRefreshComplete();
				}
			});
		}
		
		//设置送达方式变更操作
		RadioGroup rg = (RadioGroup)findViewById(R.id.typeRG);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.fastRB:
					if(sendingType != 1){
						sendingType=1;
						//刷新数据
						curpage = 1;
						currentIndex = 0;
						viewpager.setCurrentItem(0);
						for(int i=0;i<5;i++){
							BillingBusinessAdapter adapter = new BillingBusinessAdapter(BillingBusinessActivity.this, BillingBusinessActivity.this.ticket);
							goodListViews.get(i).setAdapter(adapter);;
							getGoodsByPage(1,getString(urlId[i]),i,adapter);
						}
						
					}
					
					break;
				case R.id.slowRB:
					if(sendingType != 2){
						sendingType=2;
						curpage = 1;
						currentIndex = 0;
						viewpager.setCurrentItem(0);
						for(int i=0;i<5;i++){
							BillingBusinessAdapter adapter = new BillingBusinessAdapter(BillingBusinessActivity.this, BillingBusinessActivity.this.ticket);
							goodListViews.get(i).setAdapter(adapter);;
							getGoodsByPage(1,getString(urlId[i]),i,adapter);
						}
					}
					
					break;

				default:
					break;
				}
			}
		});
		
		allLinear= (LinearLayout)findViewById(R.id.allLinear);
		allLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(0);
			}
		});
		payLinear= (LinearLayout)findViewById(R.id.payLinear);
		payBadgeView = new BadgeView(BillingBusinessActivity.this, payLinear); 
		payLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(1);
			}
		});
		receiveLinear= (LinearLayout)findViewById(R.id.receiveLinear);
		receiveBadgeView = new BadgeView(BillingBusinessActivity.this, receiveLinear);  
		receiveLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(2);
			}
		});
		postingLinear= (LinearLayout)findViewById(R.id.postingLinear);
		postingBadgeView = new BadgeView(BillingBusinessActivity.this, postingLinear);  
		postingLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(3);
			}
		});
		postedLinear= (LinearLayout)findViewById(R.id.postedLinear);
		postedBadgeView = new BadgeView(BillingBusinessActivity.this, postedLinear);  
		postedLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(4);
			}
		});
		getOrderStatusCount();
		
		viewpager = (ViewPager)findViewById(R.id.goodsViewPager);
		viewpager.setAdapter(new MyPagerAdapter());
		viewpager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				curpage = 0;
				currentIndex = arg0;
				for(int i=0;i<pagerId.length;i++){
					TextView tv = (TextView)findViewById(pagerId[i]);
					tv.setTextColor(Color.BLACK);
				}
				TextView tv = (TextView)findViewById(pagerId[arg0]);
				tv.setTextColor(Color.rgb(239,91,79));
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				 
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		viewpager.setCurrentItem(0);
		
		
	}
	BadgeView payBadgeView,receiveBadgeView,postingBadgeView,postedBadgeView;
	public void getOrderStatusCount(){
		String url = getString(R.string.base_url)+getString(R.string.billing_business_statuscount) ;
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
	
	//传入页数、接口连接
	//查询成功，找到当前显示的listview是哪个
	public void getGoodsByPage(int curpage ,String subUrl,final int index,final BillingBusinessAdapter adapter){
		String url = getString(R.string.base_url)+subUrl ;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("curpage", curpage);
		params.put("type", sendingType);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BillingCallback billing = new BillingCallback();
				billing = gson.fromJson(new String(arg2), BillingCallback.class);
				if(billing.getResult().equals("1")){
					
					adapter.addBillings(billing.getData(),flag);
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
	
	/** 
     * 设置滑动条的宽度为屏幕的1/5(根据Tab的个数而定) 
     */  
//    private void initTabLineWidth() {  
//        DisplayMetrics dpMetrics = new DisplayMetrics();  
//        getWindow().getWindowManager().getDefaultDisplay()  
//                .getMetrics(dpMetrics);  
//        screenWidth = dpMetrics.widthPixels;  
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) smoothIV  
//                .getLayoutParams();  
//        lp.width = screenWidth / 5;  
//        smoothIV.setLayoutParams(lp);  
//    }  
    
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.billing_business, menu);
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
