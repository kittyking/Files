package com.school.schoolapp.adapter.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.actionview.GoodsDetailActionView;
import com.school.schoolapp.activity.store.StoreGoodsActivity;
import com.school.schoolapp.activity.store.StoreGoodsDetailActivity;
import com.school.schoolapp.callback.store.GoodDetailCallback;
import com.school.schoolapp.callback.store.StoreGoodsCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.store.StoreCategoryVO;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.school.schoolapp.tool.animationtool.AnimationTool;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnScrollChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StoreBaseAdapter extends BaseAdapter {

	 private LayoutInflater mInflater;
		
	    private List<StoreCategoryVO> categorys;
	    
	    private Context mContext;
	    
	    private String ticket;
	    
	    private Handler handler,animationHandler;
	    
	    private int currentPage=0;
	    
	    private GoodsDetailActionView detail;
	    private int isShow = 0;

	    private ImageButton display,displayHeader;
		public StoreBaseAdapter(Context context,List<StoreCategoryVO> categorys,String ticket){
			this.mInflater = LayoutInflater.from(context);
			this.mContext =context;
			this.categorys = categorys;
			this.ticket = ticket;
		}
		public void setHandler(Handler handler){
			this.handler = handler;
		}
		public void setAnimationHandler(Handler animationHandler){
			this.animationHandler = animationHandler;
		}
		public void setCategorys(List<StoreCategoryVO> categorys){
			this.categorys = categorys;
		}
		public List<StoreCategoryVO> getCategory(){
			return this.categorys;
		}
		public ViewPager getViewPager() {
			return viewpager;
		}
		public void setFloatTab(TabLayout tab){
			for(StoreCategoryVO category : categorys)
				tab.addTab(mTab.newTab().setText(category.getCatName()));
			tab.setupWithViewPager(viewpager); 
		}
		public void setDisplayHeader(ImageButton displayHeader){
			this.displayHeader = displayHeader;
		}
	@Override
	public int getCount() {
		
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	private TabLayout mTab;
	private ViewPager viewpager;
	private List<ListView> listviews;
	private List<Fragment> fragments;
	private int cellHeight;
	private int displayway =1;//判断列表显示还是块显示 0=列表 1=块
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.view_goods_viewpager, null);
		Log.i("", "商品请求，位置"+position);
		
		//列表布局改变的按钮
		display=(ImageButton)convertView.findViewById(R.id.display);
		display.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				displayWayChange();
			}
		});

		if(!ImageLoader.getInstance().isInited())
	    	ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
		if(displayway==0){
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_store_display_lie, display);
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_store_display_lie, displayHeader);
		}
		else{
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_store_display_kuai, display);
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_store_display_kuai, displayHeader);
		}
		
		

	    mTab = (TabLayout) convertView.findViewById(R.id.main_tab); 
	    mTab.setTabMode(TabLayout.MODE_SCROLLABLE);  
		if(categorys!=null){
			viewpager = (ViewPager)convertView.findViewById(R.id.goodsViewPager);
			
			mTab.addTab(mTab.newTab().setText(categorys.get(position).getCatName())); 
			
			listviews = new ArrayList<ListView>();
			for(int i=0;i<categorys.size();i++){
				ListView listview = new ListView(mContext);
				listview.setDivider(null);
				final StoreGoodsBuyAdapter adapter = new StoreGoodsBuyAdapter(mContext,null,displayway);
				adapter.setHandler(handler);
				adapter.setHandler(animationHandler);
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						
						//showDetail(adapter, position);
						
					}
					
				});
				listview.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						// TODO Auto-generated method stub
						if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
	                        if (view.getLastVisiblePosition() == view.getCount() - 1) { 
	                        	Log.i("", "到底部了");
	                        }  
	                    } 
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						
					}
				});
				listviews.add(listview);

				getGoodsList( 1, categorys.get(i).getCid(),i);
				
			}
			
			viewpager.setAdapter(new GoodsViewpagerAdapter());
			viewpager.setCurrentItem(currentPage);
			viewpager.addOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					currentPage = arg0;
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
			//把TabLayout和ViewPager关联起来  
		    mTab.setupWithViewPager(viewpager);  
			
		}
		return convertView;
	}
	
	private StoreGoodsCallback goodsCallback;
	public void getGoodsList(final int curpage,String cid,final int position){
		//请求商品数据
		String url = mContext.getString(R.string.base_url)+mContext.getString(R.string.store_get_goods_list);
		RequestParams params = new RequestParams();
		params.put("ticket", ticket);
		params.put("cid", cid);
		params.put("curpage", curpage);//下拉加载需要改变当前页的值
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				//刷新listview的数据
				Log.i("", "第几个分类请求成功"+position);
				Gson gson = new Gson();
				goodsCallback = gson.fromJson(new String(arg2), StoreGoodsCallback.class);
				if(goodsCallback.getResult().equals("1")){
					//需要判断是刷新还是添加
					StoreGoodsBuyAdapter adapter = (StoreGoodsBuyAdapter)listviews.get(position).getAdapter();
					adapter.setHandler(handler);
					adapter.setAnimationHandler(animationHandler);
					adapter.setCurpage(curpage);
					if(goodsCallback.getData().getGoodList().size()>0){
						adapter.add(goodsCallback.getData().getGoodList());
						adapter.notifyDataSetChanged();
						
						//每次根据获取的商品数量设置viewpager的高度
						 DisplayMetrics metric = new DisplayMetrics();
					     ((StoreGoodsActivity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
					     int densityDpi = metric.densityDpi;

						
						WindowManager wm = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
						int height = wm.getDefaultDisplay().getHeight();
						int viewHeight;
						if(height <= 1280){
							if(displayway==1){
								height = 200*densityDpi/160;
								viewHeight = (adapter.getGoodSize()/3+1) * height + 44*densityDpi/160;
							}else{
								height = 111*densityDpi/160;
								viewHeight = adapter.getGoodSize() * height + 44*densityDpi/160;
							}
						}
						else{
							if(displayway==1){
								height = 200*densityDpi/160;
								viewHeight = (adapter.getGoodSize()/3+1) * height + 44*densityDpi/160;
							}else{
								height = 111*densityDpi/160;
								viewHeight = adapter.getGoodSize() * height + 44*densityDpi/160;
							}
						}
						ViewGroup.LayoutParams params = viewpager.getLayoutParams();
						if(viewHeight>params.height)
							params.height = viewHeight;
						
						viewpager.setLayoutParams(params);
					}
					
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(mContext);
			}
		});
	}
	
	public void displayWayChange(){
		displayway=displayway==0?1:0;
		StoreBaseAdapter.this.notifyDataSetChanged();
		
	}
	
	public int getDisplayWay(){
		return this.displayway;
	}
	
	private void showDetail(final StoreGoodsBuyAdapter adapter,final int position){
		final int index = position;
		final ImageButton cut = adapter.getCutBtn().get(index);
		final TextView number = adapter.getNumberTextView().get(index);
		
		//首先获取商品详情
		String url =mContext.getString(R.string.base_url)+mContext.getString(R.string.store_get_good_detail);
		RequestParams params = new RequestParams();
		params.put("ticke", LocalSharedPreferenceSingleton.getInstance().getUserInfo(mContext).getTicket());
		params.put("gid", adapter.getGoods().get(index).getGoods_id());
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				GoodDetailCallback detailcallback = new Gson().fromJson(new String(arg2), GoodDetailCallback.class);
				if(detailcallback.getResult().equals("1")){
					//获取详情成功，打开详情页面
					if(detail!=null&&detail.isShowing()==1)
						return;
					
					detail = new GoodsDetailActionView(mContext,adapter.getGoods().get(index),detailcallback.getData(),handler).builder();
					detail.setAddBtnListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// 添加按钮按下效果
							int stock = Integer.parseInt(adapter.getGoods().get(index).getStock());
							int cur =Integer.parseInt(number.getText().toString()) ;
							if(cur < stock){
								cur++;
								number.setText(""+cur);
								cut.setVisibility(View.VISIBLE);
								number.setAlpha(1);
								StoreCartEntity.getInstance().addGoods(adapter.getGoods().get(index), adapter.getGoods().get(index).getGoods_id(),adapter.getGoods().get(index).getShopPrice(),adapter.getGoods().get(index).getMarketPrice(),cut,number);
								handler.sendMessage(new Message());
							}else {
								ToastTool.showWithMessage("库存不够啦", mContext);
							}
							detail.addBtnOpetion();
						}
					}).setCutBtnListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ImageButton cut = adapter.getCutBtn().get(index);
							TextView number = adapter.getNumberTextView().get(index);
							int cur = Integer.parseInt(number.getText().toString());
							if(cur>0){
								
								int num =  cur - 1;
								number.setText(""+num);
								StoreCartEntity.getInstance().delGoods(adapter.getGoods().get(index).getGoods_id(),adapter.getGoods().get(index).getShopPrice(),adapter.getGoods().get(index).getMarketPrice());
								handler.sendMessage(new Message());
								
								if(num ==0){
									cut.setVisibility(View.INVISIBLE);
									number.setAlpha(0);
								}
							}
							detail.cutBtnOpetion();
							
						}
					});
					
					detail.show();
					isShow = 1;
				}

				
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void bottomAdd(){
		//获取到当前的页面
		String cid = categorys.get(currentPage).getCid();
		//获取到当前是第几页
		StoreGoodsBuyAdapter adapter = (StoreGoodsBuyAdapter)listviews.get(currentPage).getAdapter();
		//判断是否需要更新
		if(adapter.getGoodSize()%10 != 0)
			return;
		int curpage = adapter.getCurpage()+1;
		//获取数据 并更新adapter
		getGoodsList(curpage, cid, currentPage);
		
		
	}
	
	public class GoodsViewpagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listviews.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewGroup)container).removeView(listviews.get(position));
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewGroup)container).addView(listviews.get(position));
			return listviews.get(position);
		}
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return categorys.get(position).getCatName();
		}
	}
	
	

}
