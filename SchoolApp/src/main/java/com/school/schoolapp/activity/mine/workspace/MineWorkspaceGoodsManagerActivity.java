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
import com.school.schoolapp.BaseFragmentActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.workspace.GoodsManagerNotSellAdapter;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.NumberCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.classes.workspace.WorkspaceGoodsManagerCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MineWorkspaceGoodsManagerActivity extends BaseActivity {
	

	private int currentIndex;  
    /** 
     * 屏幕的宽度 
     */  
    private int screenWidth;  
    
    private ImageView smoothIV;
    
    private TextView notsellTV;
    
    private TextView sellingTV;
    
    private TextView recommendTV;
    
    private TextView stickTV;
    
    private int[] pagerId = {R.id.notsellTV,R.id.sellingTV,R.id.recommendTV,R.id.stickTV};
    private int[] urls ={R.string.workspace_goodmanager_nosalegoods,R.string.workspace_goodmanager_salegoods,R.string.workspace_goodmanager_hotsalegoods,R.string.workspace_goodmanager_topgoods};
    private int[] delurls={0,R.string.workspace_goodmanager_setdownsalegoods,R.string.workspace_goodmanager_setnohotsalegoods,R.string.workspace_goodmanager_topgoods};
    private int lastItem;
    private List<ListView> listviews;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_workspace_goods_manager);
		this.titleTextView.setText("商品管理");
		//设置未添加商品badge
		//setNoAddedCount();
		
	    listviews = new ArrayList<>();
	    
		
	    smoothIV = (ImageView)findViewById(R.id.smoothIV);
	    initTabLineWidth();
	    
		
		final ViewPager viewpager = (ViewPager)findViewById(R.id.managerViewPager);
		viewpager.setOffscreenPageLimit(0);
		viewpager.setAdapter(new MyViewPager());
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentIndex = arg0;
				
				ListView listview = (ListView)viewpager.getChildAt(arg0);
				GoodsManagerNotSellAdapter adapter = new GoodsManagerNotSellAdapter(MineWorkspaceGoodsManagerActivity.this, arg0);
				getListView(arg0, 1, listview, adapter, 0);
				
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
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) smoothIV.getLayoutParams();  
				 /** 
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来 
                 * 设置mTabLineIv的左边距 滑动场景： 
                 * 记3个页面, 
                 * 从左到右分别为0,1,2  
                 * 0->1; 1->2; 2->3;3->2; 2->1; 1->0 
                 */  
  
                if (currentIndex == 0 && arg0 == 0)// 0->1  
                {  
                    lp.leftMargin = (int) (arg1 * (screenWidth * 1.0 / 4) + currentIndex  
                            * (screenWidth / 4));  
  
                }else if (currentIndex == 1 && arg0 == 1) // 1->2  
                {  
                    lp.leftMargin = (int) (arg1 * (screenWidth * 1.0 / 4) + currentIndex  
                            * (screenWidth / 4));  
                }else if(currentIndex == 2 && arg0 == 2)//2->3
                {
                	 lp.leftMargin = (int) (arg1 * (screenWidth * 1.0 / 4) + currentIndex  
                             * (screenWidth / 4)); 
                } else if (currentIndex == 1 && arg0 == 0) // 1->0  
                {  
                    lp.leftMargin = (int) (-(1 - arg1)  
                            * (screenWidth * 1.0 / 4) + currentIndex  
                            * (screenWidth / 4));  
  
                }  else if (currentIndex == 2 && arg0 == 1) // 2->1  
                {  
                    lp.leftMargin = (int) (-(1 - arg1)  
                            * (screenWidth * 1.0 / 4) + currentIndex  
                            * (screenWidth / 4));  
                }  else if (currentIndex == 3 && arg0 == 2) // 3->2  
                {  
                    lp.leftMargin = (int) (-(1 - arg1)  
                            * (screenWidth * 1.0 / 4) + currentIndex  
                            * (screenWidth / 4));  
                }  
                smoothIV.setLayoutParams(lp);  
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		notsellTV = (TextView)findViewById(R.id.notsellTV);
		notsellTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(0);
			}
		});
	    
	    sellingTV = (TextView)findViewById(R.id.sellingTV);
	    sellingTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(1);
			}
		});
	    
	    recommendTV = (TextView)findViewById(R.id.recommendTV);
	    recommendTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(2);
			}
		});
	    
	    stickTV = (TextView)findViewById(R.id.stickTV);
	    stickTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewpager.setCurrentItem(3);
			}
		});
	}
	
	public void setNoAddedCount(){
		String url = getString(R.string.base_url)+getString(R.string.workspace_goodsadd_noaddcount) ;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson =new Gson();
				NumberCallback number = gson.fromJson(new String(arg2), NumberCallback.class);
				if(number.getCount()!=null){
					TextView tv = (TextView)findViewById(R.id.notsellTV);
					BadgeView badgeView = new BadgeView(MineWorkspaceGoodsManagerActivity.this, tv);  
			        badgeView.setText(number.getCount());  
			        badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
			        //badgeView.setTextSize(20);
			        badgeView.show();  
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}

	public class MyViewPager extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			
			//((ViewPager)container).removeView(listviews.get(position));
		}
		@Override
		public Object instantiateItem(View container, int position) {
			
			ListView listview = (ListView)((ViewPager)container).getChildAt(position);
			if(listview ==null){
				listview = new ListView(MineWorkspaceGoodsManagerActivity.this);
		    	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		    	listview.setLayoutParams(params);
		    	listview.setDivider(null);
		    	setupDeleteAction(listview, position);
				((ViewPager)container).addView(listview);
			}
			GoodsManagerNotSellAdapter adapter = new GoodsManagerNotSellAdapter(MineWorkspaceGoodsManagerActivity.this, position);
	    	listview.setAdapter(adapter);
			getListView(position, 1,listview,adapter,0);
	    	
			
			return listview;
		}
		
	}
	private void setupDeleteAction(final ListView listview,final int tag){
		switch (tag) {
		case 1:
			listview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					final int index = position;
					showDelDialog(index, listview, tag, "下架商品", "确定要下架商品？");
					return false;
				}
			});
			break;
		case 2:
			listview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					final int index = position;
					showDelDialog(index, listview, tag, "取消推荐", "确定要取消推荐商品？");
					return false;
				}
			});
			break;
		case 3:
			listview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					final int index = position;
					showDelDialog(index, listview, tag, "取消置顶", "确定要取消商品置顶？");
					return false;
				}
			});
	
			break;

		default:
			break;
		}
	}
	
	
	private void showDelDialog(final int index,final ListView listview, final int tag,String title,String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(MineWorkspaceGoodsManagerActivity.this).setTitle(title).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteGoods(ticket, index,listview,tag);
			}
		}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}
	
	/** 
     * 设置滑动条的宽度为屏幕的1/4(根据Tab的个数而定) 
     */  
    private void initTabLineWidth() {  
        DisplayMetrics dpMetrics = new DisplayMetrics();  
        getWindow().getWindowManager().getDefaultDisplay()  
                .getMetrics(dpMetrics);  
        screenWidth = dpMetrics.widthPixels;  
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) smoothIV  
                .getLayoutParams();  
        lp.width = screenWidth / 4;  
        smoothIV.setLayoutParams(lp);  
    }  
    
	
	public void getListView(final int tag,int curpage,final ListView listview,final GoodsManagerNotSellAdapter adapter,final int flag){
		String url = getString(R.string.base_url)+getString(urls[tag]);
		RequestParams params = new RequestParams();
		
		final UserCallback user = LocalSharedPreferenceSingleton.getInstance().getUserInfo(this);

		ticket = user.getTicket();
		params.put("ticket", ticket);
		params.put("curpage", curpage);
		params.put("keys", "");

		final int nextpage = curpage+1;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson =new Gson();
				WorkspaceGoodsManagerCallback managers = new WorkspaceGoodsManagerCallback();
				managers = gson.fromJson(new String(arg2), WorkspaceGoodsManagerCallback.class);
				//Log.i("", "manger="+new String(arg2));
				if(managers.getResult().equals("1")){
					adapter.addGoods(managers.getData().getGoodsList(),flag);
					adapter.notifyDataSetChanged();
					
					//滑动到底部
					listview.setOnScrollListener(new OnScrollListener() {
						
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							// TODO Auto-generated method stub
							if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
								if(view.getLastVisiblePosition() == view.getCount() - 1)
									getListView(tag,nextpage,listview,adapter,1);
							}
							
						}
						
						@Override
						public void onScroll(AbsListView view, int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {
							// TODO Auto-generated method stub
							lastItem = firstVisibleItem + visibleItemCount -1 ;
						}
					});
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	public void deleteGoods(String ticket,final int index,ListView listview,int tag){
		String url = getString(R.string.base_url)+getString(delurls[tag]);
		RequestParams params = new RequestParams();
		
		final GoodsManagerNotSellAdapter adapter = (GoodsManagerNotSellAdapter)listview.getAdapter();
		params.put("ticket", ticket);
		params.put("goodsid", adapter.getGoods().get(index).getGoods_id());
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = new BaseCallback();
				callback = gson.fromJson(new String(arg2), BaseCallback.class);
				Toast.makeText(MineWorkspaceGoodsManagerActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
				adapter.getGoods().remove(index);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_workspace_goods_manager, menu);
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
