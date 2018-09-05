package com.school.schoolapp.activity.mine.workspace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.R.string;
import com.school.schoolapp.activity.store.StoreCreateBillingActivity;
import com.school.schoolapp.adapter.store.StoreGoodsBuyAdapter;
import com.school.schoolapp.callback.store.StoreGoodsCallback;
import com.school.schoolapp.classes.CategoryCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.billing.BillingBuyCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MineWorkspaceBuyselfActivity extends Activity {

	private ViewPager viewpager;
	
	private TabLayout indicator;
	
	private CategoryCallback categorys;
	
	private List<ListView> goodsListviews = new ArrayList<ListView>();
	
	private List<String> titleList = new ArrayList<String>();
	
	private int curpage = 1;
	
	private Handler cartHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//获取到购物车发生改变
			TextView cartMoney = (TextView)findViewById(R.id.cartMoney);
			cartMoney.setText("￥"+StoreCartEntity.getInstance().getAmountMoney());
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_workspace_buyself);
		
		//this.titleTextView.setText("我要补单");
		//初始化购物车
		StoreCartEntity.initCart();
		//没给出购买逻辑
		//Button buyBtn = (Button)findViewById(id);
		
		viewpager = (ViewPager)findViewById(R.id.goodsViewPager);
		indicator = (TabLayout)findViewById(R.id.indicator); 
		
		String url = getString(R.string.base_url)+getString(R.string.category_get) ;
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				categorys = new CategoryCallback();
				categorys = gson.fromJson(new String(arg2), CategoryCallback.class);
				if(categorys.getResult().equals("1")){
					if(categorys.getData()!=null && categorys.getData().getCategoryList().size()>0){
						for(int i=0;i<categorys.getData().getCategoryList().size();i++){
							ListView listview = new ListView(MineWorkspaceBuyselfActivity.this);
							goodsListviews.add(listview);
							listview.setDivider(null);
							listview.setBackgroundColor(Color.WHITE);
							StoreGoodsBuyAdapter adapter = new StoreGoodsBuyAdapter(MineWorkspaceBuyselfActivity.this, null,0);
							listview.setAdapter(adapter);
							adapter.setHandler(cartHandler);
							requestGoodsData(categorys.getData().getCategoryList().get(i).getCid(),i);
							titleList.add(categorys.getData().getCategoryList().get(i).getCatName());
							
							final int index = i;
							listview.setOnScrollListener(new OnScrollListener() {
								
								@Override
								public void onScrollStateChanged(AbsListView view, int scrollState) {
									// TODO Auto-generated method stub
									requestGoodsData(categorys.getData().getCategoryList().get(index).getCid(),index);
								}
								
								@Override
								public void onScroll(AbsListView view, int firstVisibleItem,
										int visibleItemCount, int totalItemCount) {
									// TODO Auto-generated method stub
									
								}
							});
						}
						
						viewpager.setAdapter(new MyPagerAdapter());
//						设置viewpager标题
//						实例化TabPageIndicator然后设置ViewPager与之关联  
				         
				        viewpager.setCurrentItem(0);
				        indicator.setupWithViewPager(viewpager);
				        
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	public void requestGoodsData(String cid,final int categoryIndex){
		String url = getString(R.string.base_url)+getString(R.string.store_get_goods_list);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		params.put("cid", cid);
		params.put("curpage", curpage);//下拉加载需要改变当前页的值
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				StoreGoodsCallback goodsCallback = gson.fromJson(new String(arg2), StoreGoodsCallback.class);
				StoreGoodsBuyAdapter adapter = (StoreGoodsBuyAdapter)goodsListviews.get(categoryIndex).getAdapter();
				adapter.add(goodsCallback.getData().getGoodList());
				adapter.notifyDataSetChanged();
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
			return goodsListviews.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewPager)container).addView(goodsListviews.get(position));
			return goodsListviews.get(position);
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager)container).removeView(goodsListviews.get(position));
		}
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}
	}
	
	public void buyBtnPressed(View v){
		final ZProgressHUD progressHUD = ZProgressHUD.getInstance(this); 
		progressHUD.setMessage("玩命加载中");
		progressHUD.show();
		String url = getString(R.string.base_url) + getString(R.string.billing_business_buy);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		params.put("price", StoreCartEntity.getInstance().getAmountMoney());
		Gson gson = new Gson();
		String goods = gson.toJson( StoreCartEntity.getInstance().buyGoodsList());
		params.put("goods", goods);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				
				Gson gson = new Gson();
				Log.i("", new String(arg2));
				BillingBuyCallback buyCallback = new BillingBuyCallback();
				buyCallback = gson.fromJson(new String(arg2), BillingBuyCallback.class);
				if(buyCallback.getResult().equals("1")){
					Intent intent = new Intent();
					intent.setClass(MineWorkspaceBuyselfActivity.this, StoreCreateBillingActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("UserInfo", buyCallback.getData());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_workspace_buyself, menu);
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
