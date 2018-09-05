package com.school.schoolapp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.entity.store.GoodDetailVO;
import com.school.schoolapp.entity.store.StoreCategoryVO;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.school.schoolapp.actionview.GoodsCartAtionSheet;
import com.school.schoolapp.actionview.GoodsDetailActionView;
import com.school.schoolapp.activity.store.StoreCreateBillingActivity;
import com.school.schoolapp.adapter.store.StoreGoodsBuyAdapter;
import com.school.schoolapp.callback.store.GoodDetailCallback;
import com.school.schoolapp.callback.store.StoreGoodsCallback;
import com.school.schoolapp.callback.store.StoreInfoCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.billing.BillingBuyCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.tool.animationtool.AnimationTool;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;
import com.school.schoolapp.tool.viewpagerscroll.FixedSpeedScroller;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
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
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class StoreGoodActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_good);

		setupHeader();
	    //初始化购物车
		StoreCartEntity.initCart();
		//设置购物车
		setupCart();
		//获取商品数据
		getStoreInfo();
	}
	private void setupHeader(){

		//添加公告控件
		noticeSwitcher = (TextSwitcher)findViewById(R.id.noticeSwitcher);
		// 设置切入动画  
		noticeSwitcher.setInAnimation(AnimationUtils.loadAnimation(StoreGoodActivity.this,R.anim.slide_in_bottom));
        // 设置切出动画  
		noticeSwitcher.setOutAnimation(AnimationUtils.loadAnimation(StoreGoodActivity.this, R.anim.slide_out_up));
		noticeSwitcher.setFactory(new ViewFactory() {
			
			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				TextView textView = new TextView(StoreGoodActivity.this); 
				textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
				textView.setTextColor(Color.GRAY);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				return textView;
			}
		});
	}
	
	private StoreInfoCallback info;
	private ViewPager adViewpager,goodsViewpager;
	private TabLayout cateTab;
	private FixedSpeedScroller mScroller;
	private int noticePlayDelay = 5000;//广告、公告切换时间
	private List<NestedScrollView> scrolls; 
	private TextSwitcher noticeSwitcher;
	private int displayType = 0;
	public void getStoreInfo(){
		String url = getString(R.string.base_url)+getString(R.string.store_get_shop_info);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		final AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				info = new StoreInfoCallback();
				info = gson.fromJson(new String(arg2), StoreInfoCallback.class);
				if(info.getResult().equals("1")){
					//设置起送金额
					if(buyBtn==null)
						buyBtn = (Button)findViewById(R.id.buyBtn);
					buyBtn.setText("还差"+info.getLowest()+"元起送");
					//设置店铺名字
					if(info.getShopName()!=null)
						((TextView)findViewById(R.id.storename)).setText(info.getShopName());
					//添加广告
					if(info.getData().getAdlist()!=null && info.getData().getAdlist().size()>0){
						adViewpager = (ViewPager)findViewById(R.id.adViewpager);
						LinearLayout.LayoutParams paramas = ( LinearLayout.LayoutParams)adViewpager.getLayoutParams();
					    WindowManager wm = (WindowManager)StoreGoodActivity.this.getSystemService(StoreGoodActivity.this.WINDOW_SERVICE);
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
			         
					if(info.getData().getAdlist().size()>0||info.getData().getLetter().size()>0){
						handler.removeMessages(0);
						handler.sendEmptyMessageDelayed(0, noticePlayDelay);
					}
					
					//初始化商品列表
					scrolls= new ArrayList<>();
					LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
					
					
					
					for(StoreCategoryVO category : info.getData().getCategoryList()){
						NestedScrollView scroll= new NestedScrollView(StoreGoodActivity.this);
						scroll.setBackgroundColor(Color.WHITE);
						scroll.setLayoutParams(params);
						LinearLayout base = new LinearLayout(StoreGoodActivity.this);
						base.setOrientation(LinearLayout.VERTICAL);
						base.setLayoutParams(params);
						requestGoodsList(1, category.getCid(),base);
						scroll.addView(base);
						scrolls.add(scroll);
				    }
					
					goodsViewpager = (ViewPager)findViewById(R.id.goodsViewpager);
					goodsViewpager.setAdapter(new GoodsPagerAdapter());
					cateTab = (TabLayout)findViewById(R.id.tabs);
					cateTab.setupWithViewPager(goodsViewpager);
					
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

	private int moveIndex =0;
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
    
    private void setupSubview(View cell,final StoreGoodsVO good){
    	((TextView)cell.findViewById(R.id.goodsTitle)).setText(good.getGoodsTitle());
		((TextView)cell.findViewById(R.id.shopPrice)).setText("￥"+good.getShopPrice());
		((TextView)cell.findViewById(R.id.marketPrice)).setText("￥"+good.getMarketPrice());
		((TextView)cell.findViewById(R.id.marketPrice)).getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
		final TextView number =(TextView)cell.findViewById(R.id.numberTV);
		number.setText("0");
		((TextView)cell.findViewById(R.id.stock)).setText(good.getStock());
		
		
		
		final ImageButton cutBtn=(ImageButton)cell.findViewById(R.id.cutBtn);
		cutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int cur = Integer.parseInt(number.getText().toString());
				if(cur>0){
					int num =  cur - 1;
					number.setText(""+num);
					StoreCartEntity.getInstance().delGoods(good.getGoods_id(),good.getShopPrice(),good.getMarketPrice());
					cartHandler.sendMessage(new Message());
					if(num ==0){
						cutBtn.setVisibility(View.INVISIBLE);
						number.setAlpha(0);
					}
				}
			}
		});
		
		final ImageButton addBtn=(ImageButton)cell.findViewById(R.id.addBtn);

		final int stock = Integer.parseInt(good.getStock());
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int cur =Integer.parseInt(number.getText().toString()) ;
				cur++;
				if(cur < stock){
					number.setText(""+cur);
					cutBtn.setAlpha(1);
					cutBtn.setVisibility(View.VISIBLE);
					number.setAlpha(1);
					StoreCartEntity.getInstance().addGoods(good, good.getGoods_id(),good.getShopPrice(),good.getMarketPrice(),cutBtn,number);
					cartHandler.sendMessage(new Message());
					startAnimation(v);
				}else {
					ToastTool.showWithMessage("库存不够啦", StoreGoodActivity.this);
				}
				
			}
		});
		
		ImageView icon = (ImageView)cell.findViewById(R.id.goodsImg);
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDetail(good, number, cutBtn, good.getGoods_id());
			}
		});
		ImageLoaderTool.getInstance().displayImage(good.getGoodsImg(),icon , StoreGoodActivity.this);
		
		TextView goodsName = (TextView)cell.findViewById(R.id.goodsName);
		goodsName.setText(good.getGoodsName());
		goodsName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDetail(good, number, cutBtn, good.getGoods_id());
			}
		});
    }
    
    public void requestGoodsList(final int curpage,String cid,final LinearLayout base){
		//请求商品数据
		String url = getString(R.string.base_url)+getString(R.string.store_get_goods_list);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		params.put("cid", cid);
		params.put("curpage", curpage);//下拉加载需要改变当前页的值
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				//刷新listview的数据
				Gson gson = new Gson();
				StoreGoodsCallback goodsCallback = gson.fromJson(new String(arg2), StoreGoodsCallback.class);
				if(goodsCallback.getResult().equals("1")){
					//需要判断是刷新还是添加
					if(goodsCallback.getData().getGoodList().size()>0){
						LinearLayout rowLinear = null;
						WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
						int width = wm.getDefaultDisplay().getWidth()/3;
						for(int i=0;i<goodsCallback.getData().getGoodList().size();i++){
							StoreGoodsVO good =goodsCallback.getData().getGoodList().get(i);
							View cell;
							if(displayType ==0){//块
								if(i%3==0){
									rowLinear = new LinearLayout(StoreGoodActivity.this);
									rowLinear.setOrientation(LinearLayout.HORIZONTAL);
									base.addView(rowLinear);
								}
								cell= LayoutInflater.from(StoreGoodActivity.this).inflate(R.layout.cell_goods_vertical, null);
								ViewGroup.LayoutParams params = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
								
								cell.setLayoutParams(params);
								setupSubview(cell, good);
								if(rowLinear !=null)
									rowLinear.addView(cell);
							}else{//列
								cell= LayoutInflater.from(StoreGoodActivity.this).inflate(R.layout.adapter_goods_listviewcell, null);
								setupSubview(cell, good);
								base.addView(cell);	
							}
							
							
						}
					}
					
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(StoreGoodActivity.this);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.store_good, menu);
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
			ImageView iv = new ImageView(StoreGoodActivity.this);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			ImageLoaderTool.getInstance().displayImage(info.getData().getAdlist().get(position%info.getData().getAdlist().size()).getAdImgsrc(), iv,StoreGoodActivity.this);
	        ((ViewPager)container).addView(iv);
			return iv;
		}
		
	}
	
	public class GoodsPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return scrolls.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager)container).removeView(scrolls.get(position));
		}
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(scrolls.get(position));
			return scrolls.get(position);
		}
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return info.getData().getCategoryList().get(position).getCatName();
		}
		
	}
	
	public void finish(View v){
		finish();
	}

	private Button buyBtn;
	private ImageButton cartBtn,displayBtn;
	private LinearLayout cartLinear;
	private BadgeView cartBadge;
	private Handler cartHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//获取到购物车发生改变
			initPageInfo();
		};
	};
	public void setupCart(){
		//设置显示类型切换按钮
		displayBtn = (ImageButton)findViewById(R.id.displayheader);
		displayBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(displayType==0){
					displayType =1;
					ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_store_display_lie, displayBtn);
				}
				else {
					displayType = 0;
					ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_store_display_kuai, displayBtn);
				}
				getStoreInfo();
			}
		});
		//设置购物车badge
	    cartLinear = (LinearLayout)findViewById(R.id.cartsLinear);
	    //立即购买按钮触发方法
	    if(buyBtn==null)
			buyBtn = (Button)findViewById(R.id.buyBtn);
	    buyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buyPressed();
			}
		});
	    cartBadge = new BadgeView(this,cartLinear);
	    cartBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        
		cartBtn = (ImageButton)findViewById(R.id.cartImageButton);
		((Button)(findViewById(R.id.cartcreate))).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GoodsCartAtionSheet(StoreGoodActivity.this, Float.parseFloat(info.getLowest()),cartHandler).builder().show();
			}
		});
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
	
	private void getDetail(final StoreGoodsVO good,final TextView number,final ImageButton cutBtn,String gid){
 		//首先获取商品详情
		String url =getString(R.string.base_url)+getString(R.string.store_get_good_detail);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		params.put("gid", gid);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				GoodDetailCallback detailcallback = new Gson().fromJson(new String(arg2), GoodDetailCallback.class);
				if(detailcallback.getResult().equals("1")){
					showDetail(good,detailcallback.getData(),number,cutBtn);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
 	}

    private GoodsDetailActionView detail;
	private void showDetail(final StoreGoodsVO good,GoodDetailVO detailVO,final TextView number,final ImageButton cutBtn){
 		//获取详情成功，打开详情页面
		if(detail!=null&&detail.isShowing()==1)
			return;
		
		detail = new GoodsDetailActionView(this,good,detailVO,cartHandler).builder();
		detail.setAddBtnListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 添加按钮按下效果
				int stock = Integer.parseInt(good.getStock());
				int cur =Integer.parseInt(number.getText().toString()) ;
				if(cur < stock){
					cur++;
					number.setText(""+cur);
					cutBtn.setAlpha(1);
					cutBtn.setVisibility(View.VISIBLE);
					number.setAlpha(1);
					StoreCartEntity.getInstance().addGoods(good, good.getGoods_id(),good.getShopPrice(),good.getMarketPrice(),cutBtn,number);
					cartHandler.sendMessage(new Message());
				}else {
					ToastTool.showWithMessage("库存不够啦", StoreGoodActivity.this);
				}
				detail.addBtnOpetion();
			}
			
		}).setCutBtnListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int cur = Integer.parseInt(number.getText().toString());
				if(cur>0){
					
					int num =  cur - 1;
					number.setText(""+num);
					StoreCartEntity.getInstance().delGoods(good.getGoods_id(),good.getShopPrice(),good.getMarketPrice());
					cartHandler.sendMessage(new Message());
					
					if(num ==0){
						cutBtn.setVisibility(View.INVISIBLE);
						number.setAlpha(0);
					}
				}
				detail.cutBtnOpetion();
				
			}
		});
		
		detail.show();
 	}
	private void buyPressed(){
		if(!buyBtn.getText().toString().equals("立即购买"))
			return;
		
		if(StoreCartEntity.getInstance().getCartGoods()==null)
			return;
		
		final ZProgressHUD progressHUD = ZProgressHUD.getInstance(this); 
		progressHUD.setMessage("玩命加载中");
		progressHUD.show();
		
		if(buyBtn.getText().toString().equals("立即购买")){
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
						intent.setClass(StoreGoodActivity.this, StoreCreateBillingActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("UserInfo", buyCallback.getData());
						intent.putExtras(bundle);
						startActivity(intent);
					}else{
						if(buyCallback.getMsg()!=null)
							ToastTool.showWithMessage(buyCallback.getMsg(), StoreGoodActivity.this);
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

	private void startAnimation(View view){
		int[] location = new  int[2] ;
		view.getLocationOnScreen(location);
		AnimationTool.getInstance().setStartX(location[0]);
		AnimationTool.getInstance().setStartY(location[1]);
		animationHandler.sendEmptyMessage(0);
	}
	private Handler animationHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			final TextView txt = new TextView(StoreGoodActivity.this);
			txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_animation_circle));
			txt.setLayoutParams(new LayoutParams(50,50));
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
}
