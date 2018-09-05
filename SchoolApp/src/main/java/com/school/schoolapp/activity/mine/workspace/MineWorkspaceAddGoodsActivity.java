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
import com.school.schoolapp.adapter.workspace.WorkspaceGoodsAddAdapter;
import com.school.schoolapp.callback.store.StoreGoodsCallback;
import com.school.schoolapp.classes.CategoryCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.NumberCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.AbsListView.OnScrollListener;

public class MineWorkspaceAddGoodsActivity extends Activity {
	
	private String curCid;
	
	private int curPage= 1;
	
	private String curKeys;

	private List<ListView> goodsListviews = new ArrayList<ListView>();
	
	private List<String> titleList = new ArrayList<String>();
	
	private ViewPager viewpager;
	
	private TabLayout indicator;
	
	private int currentType = 0;
	
	private int curCategoryIndex;
	
	private int initOrAdd = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_workspace_add_goods);
		((TextView)findViewById(R.id.storename)).setText("添加商品");
		
		setupRadioGroup();
		//setNoAddedCount();
		
		viewpager = (ViewPager)findViewById(R.id.goodsViewPager);
		indicator = (TabLayout)findViewById(R.id.indicator); 
	
		networking(getString(R.string.workspace_goodsadd_getgoodslist));	
	}
	
	public void setNoAddedCount(){
		String url = getString(R.string.base_url)+getString(R.string.workspace_goodsadd_noaddcount) ;
		RequestParams params = new RequestParams();
		params.put("ticket",LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson =new Gson();
				NumberCallback number = gson.fromJson(new String(arg2), NumberCallback.class);
				if(number.getCount()!=null){
					//加上以后radiobutton无法调用事件
//					RadioButton rb = (RadioButton)findViewById(R.id.weiRB);
//					BadgeView badgeView = new BadgeView(MineWorkspaceAddGoodsActivity.this, rb);  
//			        badgeView.setText(number.getCount());  
//			        badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
//			        //badgeView.setTextSize(20);
//			        badgeView.show();  
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	public void finish(View v){
		finish();
	}
	
	private CategoryCallback categorys;
	
	public void networking(final String subUrl){
		String url = getString(R.string.base_url)+getString(R.string.category_get) ;
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		final ZProgressHUD progressHUD = ZProgressHUD.getInstance(this);
		progressHUD.setMessage("正在加载");
		progressHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				Gson gson = new Gson();
				categorys = new CategoryCallback();
				categorys = gson.fromJson(new String(arg2), CategoryCallback.class);
				if(categorys.getResult().equals("1")){
					if(categorys.getData()!=null && categorys.getData().getCategoryList().size()>0){
					
						//创建个分类的listview
						
						for(int i=0;i<categorys.getData().getCategoryList().size();i++){
							ListView listview = new ListView(MineWorkspaceAddGoodsActivity.this);
							listview.setDivider(null);
							listview.setBackgroundColor(Color.WHITE);
							final int index = i;
							listview.setOnScrollListener(new OnScrollListener() {
								
								@Override
								public void onScrollStateChanged(AbsListView view, int scrollState) {
									// TODO Auto-generated method stub
									 if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
					                        if (view.getLastVisiblePosition() == view.getCount() - 1) {  
					                        	initOrAdd = 1;
					                        	curPage++;
					                        	updateListView(subUrl,categorys.getData().getCategoryList().get(index).getCid(), curPage, curKeys,index);  
					                        }  
					                    }  
								}
								
								@Override
								public void onScroll(AbsListView view, int firstVisibleItem,
										int visibleItemCount, int totalItemCount) {
									// TODO Auto-generated method stub
									
								}
							});
							goodsListviews.add(listview);
							titleList.add(categorys.getData().getCategoryList().get(i).getCatName());
							//调用查询商品接口
							updateListView(subUrl,categorys.getData().getCategoryList().get(i).getCid(), curPage, curKeys,i);
						}
						
						
						viewpager.setAdapter(new MyPagerAdapter());
//						设置viewpager标题
//						实例化TabPageIndicator然后设置ViewPager与之关联  
				         
				        viewpager.setCurrentItem(0);
				        indicator.setupWithViewPager(viewpager);
						
					}
				}else{
					Toast.makeText(MineWorkspaceAddGoodsActivity.this, categorys.getMsg(), Toast.LENGTH_SHORT).show();
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
	
	public void updateListView(String suburl,String cid, int curpage,String keys,final int categoryIndex){
		String url = getString(R.string.base_url)+suburl;
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		params.put("cid", cid);
		params.put("curpage", curpage);
		params.put("keys", keys);
		curCid = cid;
		curPage =curpage;
		curKeys= keys;
		curCategoryIndex =categoryIndex;
		
		final ZProgressHUD progressHUD = ZProgressHUD.getInstance(this);
		progressHUD.setMessage("正在加载");
		progressHUD.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub

				progressHUD.dismiss();
				Gson gson = new Gson();
				StoreGoodsCallback goods = new StoreGoodsCallback();
				goods = gson.fromJson(new String(arg2), StoreGoodsCallback.class);
				if(goods.getResult().equals("1")){
					if(initOrAdd == 0){
						WorkspaceGoodsAddAdapter adapter = new WorkspaceGoodsAddAdapter(MineWorkspaceAddGoodsActivity.this,goods.getData().getGoodList() , currentType,LocalSharedPreferenceSingleton.getInstance().getUserInfo(MineWorkspaceAddGoodsActivity.this).getTicket());
						goodsListviews.get(categoryIndex).setAdapter(adapter);
					}else{
						WorkspaceGoodsAddAdapter adapter = (WorkspaceGoodsAddAdapter)goodsListviews.get(categoryIndex).getAdapter();
						adapter.addGoods(goods.getData().getGoodList());
						adapter.notifyDataSetChanged();
					}
					
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
	
	public void setupRadioGroup(){
		RadioGroup tab = (RadioGroup)findViewById(R.id.typeRG);
		tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				goodsListviews = new ArrayList<ListView>();
				titleList = new ArrayList<String>();
				switch (checkedId) {
				case R.id.weiRB:
					//刷新
					currentType = 0;
					curPage = 1;
					initOrAdd = 0;
					networking(getString(R.string.workspace_goodsadd_getgoodslist));
					break;
				case R.id.yiRB:
					//刷新
					currentType = 1;
					curPage = 1;
					initOrAdd = 0;
					networking(getString(R.string.workspace_goodsadd_getgoodsaddedlist));
					break;
				default:
					break;
				}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_workspace_add_goods, menu);
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
