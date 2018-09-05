package com.school.schoolapp.activity.store;

import java.lang.reflect.Field;
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
import com.school.schoolapp.actionview.GoodsCartAtionSheet;
import com.school.schoolapp.activity.chat.ChatDetailActivity;
import com.school.schoolapp.activity.home.HomeMainActivity;
import com.school.schoolapp.activity.mine.workspace.MineWorkspaceActivity;
import com.school.schoolapp.adapter.store.StoreBaseAdapter;
import com.school.schoolapp.adapter.store.StoreGoodsBuyAdapter;
import com.school.schoolapp.adapter.store.StoreGoosAdapter;
import com.school.schoolapp.callback.store.StoreGoodsCallback;
import com.school.schoolapp.callback.store.StoreInfoCallback;
import com.school.schoolapp.classes.billing.BillingBuyCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.store.StoreCategoryVO;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.school.schoolapp.entity.user.UserAdressVO;
import com.school.schoolapp.tool.animationtool.AnimationTool;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;
import com.school.schoolapp.tool.viewpagerscroll.FixedSpeedScroller;
import com.school.schoolapp.widget.PhoneSheet;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.animation.TimeInterpolator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextSwitcher;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ViewSwitcher.ViewFactory;

public class StoreGoodsActivity extends BaseActivity {

	private ListView goodsListView ;
	
	private RadioGroup categoryRG,invisRG;
	
	private String cid;
	private TabLayout mTab;
	private int curpage = 1,lastItem;

	private StoreBaseAdapter storebaseAdapter;
	
	private StoreGoodsCallback goodsCallback;
	
	private List<StoreGoodsVO> goodsList;
	
	private View headerV,categoryV;
	
	private StoreInfoCallback info;
	
	private List<ImageView> adImageViews;
	
	private TextSwitcher noticeSwitcher;
	
	private int moveIndex =0;
	
	private int noticePlayDelay = 5000;
	
	private ViewPager adViewpager,goodViewpager;
	
	private Button buyBtn;
	
	private ImageButton cartBtn,displayBtn;
	private LinearLayout cartLinear;
	private BadgeView cartBadge;
	
	private Handler refreshHandler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			initPageInfo();
			//getStoreInfo();
			storebaseAdapter.notifyDataSetChanged();
			//adapter.notifyDataSetChanged();
		};
	};
	private TextView animTxt;
	private Handler animationHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			final TextView txt = new TextView(StoreGoodsActivity.this);
			txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_animation_circle));
			txt.setLayoutParams(new LayoutParams(40,40));
			txt.setText("1");
			txt.setTextSize(12);
			txt.setGravity(Gravity.CENTER);
			txt.setTextColor(Color.WHITE);
			((RelativeLayout)findViewById(R.id.storebaserl)).addView(txt);
			
			int[] location = new  int[2] ;
			cartBtn.getLocationOnScreen(location);
			AnimationTool.getInstance().setEndX(location[0]);
			AnimationTool.getInstance().setEndY(location[1]);
			AnimationSet animationset = AnimationTool.getInstance().getParabola();
			txt.startAnimation(animationset);
			animationset.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					txt.setVisibility(View.GONE);
				}
			});
		};
	};
	
	private Handler cartHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//获取到购物车发生改变
			initPageInfo();
		};
	};
	
	private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		moveIndex++;
    		
    		//让ViewPager滑到下一页
    		if(info.getData().getAdlist()!=null && info.getData().getAdlist().size()>0){
        		adViewpager.setCurrentItem(adViewpager.getCurrentItem()+1);
        		mScroller.setmDuration(1* 1000);
			}
    		
    		//让TextSwitcher滚动
    		if(info.getData().getLetter()!=null && info.getData().getLetter().size()>0)
    			noticeSwitcher.setText(info.getData().getLetter().get(moveIndex%info.getData().getLetter().size()).getTitle());
    		//延时，循环调用handler
    		handler.sendEmptyMessageDelayed(0, noticePlayDelay);
    	}
    };
	BroadcastReceiver broadReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			initPageInfo();
			storebaseAdapter.notifyDataSetChanged();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_goods);
		
		setActionBar();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("Pay");
		registerReceiver(broadReceiver, intentFilter);
		
		//初始化tab
		mTab = (TabLayout)findViewById(R.id.main_tab); 
	    mTab.setTabMode(TabLayout.MODE_SCROLLABLE); 
	    displayBtn=(ImageButton)findViewById(R.id.displayheader);
	    displayBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(storebaseAdapter!=null){
					storebaseAdapter.displayWayChange();
				}
				
			}
		});
	    //初始化购物车
		StoreCartEntity.initCart();
		
		//立即购买按钮触发方法
		buyBtn = (Button)findViewById(R.id.buyBtn);
		buyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!buyBtn.getText().toString().equals("立即购买"))
					return;
				
				if(StoreCartEntity.getInstance().getCartGoods()==null)
					return;
				
				final ZProgressHUD progressHUD = ZProgressHUD.getInstance(StoreGoodsActivity.this); 
				progressHUD.setMessage("玩命加载中");
				progressHUD.show();
				
				if(buyBtn.getText().toString().equals("立即购买")){
					String url = getString(R.string.base_url) + getString(R.string.billing_business_buy);
					RequestParams params = new RequestParams();
					params.put("ticket", StoreGoodsActivity.this.ticket);
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
								intent.setClass(StoreGoodsActivity.this, StoreCreateBillingActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("UserInfo", buyCallback.getData());
								intent.putExtras(bundle);
								startActivity(intent);
							}else{
								if(buyCallback.getMsg()!=null)
									ToastTool.showWithMessage(buyCallback.getMsg(), StoreGoodsActivity.this);
							}
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							ToastTool.showNetworkError(getApplicationContext());
							progressHUD.dismiss(); 
						}
					});
					
				}
			}
		});
		
		//设置购物车badge
	    cartLinear = (LinearLayout)findViewById(R.id.cartsLinear);
	    cartBadge = new BadgeView(this,cartLinear);
	    cartBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        
		cartBtn = (ImageButton)findViewById(R.id.cartImageButton);
		((Button)(findViewById(R.id.cartcreate))).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GoodsCartAtionSheet(StoreGoodsActivity.this, Float.parseFloat(info.getLowest()),cartHandler).builder().show();
			}
		});
		
		//获取商品数据
		getStoreInfo();
		
		
		goodsListView = (ListView)findViewById(R.id.goosListView);
		
		
		headerV = View.inflate(this, R.layout.view_store_headerview, null);
		categoryV= View.inflate(this, R.layout.view_store_category_headview, null);
		categoryRG = (RadioGroup)categoryV.findViewById(R.id.categroyRG);
		categoryRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton irb=(RadioButton) invisRG.getChildAt(checkedId);
				if(irb!=null){
					irb.setChecked(true);
				}
				
				for(int i=0;i<group.getChildCount();i++){
					RadioButton rb = (RadioButton)group.getChildAt(i);
					rb.setTextColor(Color.BLACK);
					if(i==checkedId)
						rb.setTextColor(Color.rgb(239,91,79));
				}
			}
		});
		
		//添加公告控件
		noticeSwitcher = (TextSwitcher)headerV.findViewById(R.id.noticeSwitcher);
		// 设置切入动画  
		noticeSwitcher.setInAnimation(AnimationUtils.loadAnimation(StoreGoodsActivity.this,R.anim.slide_in_bottom));
        // 设置切出动画  
		noticeSwitcher.setOutAnimation(AnimationUtils.loadAnimation(StoreGoodsActivity.this, R.anim.slide_out_up));
		noticeSwitcher.setFactory(new ViewFactory() {
			
			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				TextView textView = new TextView(StoreGoodsActivity.this); 
				textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
				textView.setTextColor(Color.GRAY);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				return textView;
			}
		});
		
		goodsListView.addHeaderView(headerV,null,true);
		//goodsListView.addHeaderView(categoryV,null,true);
		
		goodsCallback = new StoreGoodsCallback();
		
		goodsListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					if(view.getLastVisiblePosition() == view.getCount() - 1){
						//滑动到底部后的操作
						if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
		                    if (view.getLastVisiblePosition() == view.getCount() - 1) { 
		                    	Log.i("", "到底部了");
		                    	storebaseAdapter.bottomAdd();
		                    }  
		                } 
					}
						
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.i("", "调用了firstVisibleItem="+firstVisibleItem);
				lastItem = firstVisibleItem + visibleItemCount -1 ;
				 if (firstVisibleItem >= 1 && storebaseAdapter!=null) {  
					 	displayBtn.setVisibility(View.VISIBLE);
	                    mTab.setVisibility(View.VISIBLE);
	                    mTab.setupWithViewPager(storebaseAdapter.getViewPager()); 
	                    
	                } else if(firstVisibleItem <= 1 && storebaseAdapter!=null){ // TODO Auto-generated method stub
	                	 mTab.setVisibility(View.GONE);
	                	 displayBtn.setVisibility(View.GONE);
	                	 
	                }  
			}
		});

		
	}
	private FixedSpeedScroller mScroller;
	public void getStoreInfo(){
		String url = getString(R.string.base_url)+getString(R.string.store_get_shop_info);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		final AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				info = new StoreInfoCallback();
				info = gson.fromJson(new String(arg2), StoreInfoCallback.class);
				if(info.getResult().equals("1")){
					buyBtn.setText("还差"+info.getLowest()+"元起送");
					//设置店铺名字
					if(info.getShopName()!=null)
			        //设置定位标题
					((TextView)findViewById(R.id.storename)).setText(info.getShopName());
					
					//添加广告
					if(info.getData().getAdlist()!=null && info.getData().getAdlist().size()>0){
						adViewpager = (ViewPager)headerV.findViewById(R.id.adViewpager);
						LinearLayout.LayoutParams paramas = ( LinearLayout.LayoutParams)adViewpager.getLayoutParams();
					    WindowManager wm = (WindowManager)StoreGoodsActivity.this.getSystemService(StoreGoodsActivity.this.WINDOW_SERVICE);
				        int width = wm.getDefaultDisplay().getWidth();
				        paramas.height =width/3;
				        paramas.width=width;
				        adViewpager.setLayoutParams(paramas);
						adViewpager.setAdapter(new AdPagerAdapter());
						
						try {
							Field mField = ViewPager.class.getDeclaredField("mScroller");
							mField.setAccessible(true);
							mScroller = new FixedSpeedScroller(adViewpager.getContext(),new AccelerateInterpolator());
							mField.set(adViewpager, mScroller);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
					if(info.getData().getLetter()!=null&& info.getData().getLetter().size()>0)
						noticeSwitcher.setText(info.getData().getLetter().get(0).getTitle());
			         
					if(info.getData().getAdlist().size()>0||info.getData().getLetter().size()>0)
						handler.sendEmptyMessageDelayed(0, noticePlayDelay);
					
					//初始化外层listview
					storebaseAdapter=new StoreBaseAdapter(StoreGoodsActivity.this, info.getData().getCategoryList(), StoreGoodsActivity.this.ticket);
					goodsListView.setAdapter(storebaseAdapter);
					storebaseAdapter.setHandler(cartHandler);
					storebaseAdapter.setAnimationHandler(animationHandler);
					storebaseAdapter.setDisplayHeader(displayBtn);
					
				}else if(info.getResult().equals("2")){
					Toast.makeText(getApplicationContext(), info.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	
	public class AdPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 100;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			//((ViewPager)container).removeView(adImageViews.get(position%adImageViews.size()));
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			ImageView iv = new ImageView(StoreGoodsActivity.this);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			ImageLoaderTool.getInstance().displayImage(info.getData().getAdlist().get(position%info.getData().getAdlist().size()).getAdImgsrc(), iv,StoreGoodsActivity.this);
	        ((ViewPager)container).addView(iv);
			return iv;
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//报错以后再弄
		if(requestCode==101 || requestCode==102){//购物车、商品详情返回后刷新
//			initPageInfo();
//			getStoreInfo();
//			refreshHandler.sendEmptyMessage(0);
		}
		
		if(requestCode==103){//点击搜索以后的回调
			//刷新商品列表
			//接口缺少一个关键字的字段，需要后台添加上
		}
	}
	
	public void initPageInfo(){
		TextView cartMoney = (TextView)findViewById(R.id.cartMoney);
		cartMoney.setText("￥"+(float)(Math.round(StoreCartEntity.getInstance().getAmountMoney()*100.00)/100.00));
		
		float lowest = Float.parseFloat(info.getLowest());
		if(StoreCartEntity.getInstance().getAmountMoney()>=lowest){
			buyBtn.setText("立即购买");
			buyBtn.setBackground(getResources().getDrawable(R.drawable.background_cart_orange));
			
			
			
		}else{
			float remain = lowest - StoreCartEntity.getInstance().getAmountMoney();
			buyBtn.setText("还差"+(float)(Math.round(remain*100.00)/100.00)+"元起送");
			Log.i("","remain="+remain);
			//buyBtn.setText("还差"+remain+"元起送");
			buyBtn.setBackground(getResources().getDrawable(R.drawable.background_cart_gray));
		}
		if(StoreCartEntity.getInstance().getNumCount()==0)
			cartBadge.hide();
		else {
			cartBadge.show();
		}
        cartBadge.setText(""+StoreCartEntity.getInstance().getNumCount());  
        cartBadge.setTextSize(9);
	    cartBadge.setPadding(15, 5, 15, 5);
	}
	
	public void setActionBar() {
            
        ImageButton back = (ImageButton)findViewById(R.id.backButton);
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        
        ImageButton search = (ImageButton)findViewById(R.id.search);
        search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StoreGoodsActivity.this,StoreGoodsSearchActivity.class);  
                startActivityForResult(intent, 103);
			}
		});
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_goods, menu);
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadReceiver);
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
