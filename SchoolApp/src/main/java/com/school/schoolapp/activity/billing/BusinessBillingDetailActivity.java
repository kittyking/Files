package com.school.schoolapp.activity.billing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.billing.BillingBusinessAdapter;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.billing.BillingPayCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entitys.OrderVO;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BusinessBillingDetailActivity extends BaseActivity {
	private BillingPayCallback billingPay;
	private TextView nameTV,phoneTV,addressTV,timeBetweenTV;
	private String orderId;
	private Button submitBtn;
	private Button cancelBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_billing_detail);
		
		this.titleTextView.setText("订单详情");
		submitBtn = (Button)findViewById(R.id.payBtn);
		cancelBtn = (Button)findViewById(R.id.cancelBtn);
		

		Intent intent = getIntent();
		orderId = intent.getStringExtra("OrderId");
		
		String url = getString(R.string.base_url)+getString(R.string.billing_get_shop_orderinfo);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("orderid", orderId);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				billingPay = gson.fromJson(new String(arg2), BillingPayCallback.class);
				if(billingPay.getResult().equals("1")){
					initPayBilling(billingPay);
					setupPayButton(billingPay.getData().getOrder());
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	private void setupPayButton(final OrderVO order){
		switch (order.getOrderStatus()) {
		case "1"://待接单
			submitBtn.setText("我要接单");
			submitBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = getString(R.string.billing_business_confirm);
					manageOrder(url,order.getOrderID());
				}
			});
			cancelBtn.setText("取消订单");
			cancelBtn.setAlpha(1);
			cancelBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = getString(R.string.billing_business_cancel);
					manageOrder(url,order.getOrderID());
				}
			});
			if(order.getPayment()==1){
				submitBtn.setText("确认发货");
				submitBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url = getString(R.string.billing_business_complete);
						manageOrder(url,order.getOrderID());
					}
				});
			}
			break;
		case "2"://待发货
			submitBtn.setText("确认发货");
			submitBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = getString(R.string.billing_business_ship);
					manageOrder(url,order.getOrderID());
				}
			});
		    break;
		case "3"://配送中
			submitBtn.setText("完成订单");
			submitBtn.setAlpha(1);
			break;
		case "4"://已结单
			submitBtn.setText("删除订单");
			submitBtn.setBackground(getResources().getDrawable(R.drawable.background_cart_white));
			submitBtn.setTextColor(Color.GRAY);
			submitBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		

		default:
			break;
		}
	}
	
	private void initPayBilling(BillingPayCallback billingPay){

		//显示收货地址
		nameTV = (TextView)findViewById(R.id.nameTV);
		phoneTV = (TextView)findViewById(R.id.phoneTV);
		addressTV = (TextView)findViewById(R.id.addressTV);
		
		nameTV.setText(billingPay.getData().getAddress().getName());
		phoneTV.setText(billingPay.getData().getAddress().getMobile());
		addressTV.setText(billingPay.getData().getAddress().getSchoolName()+billingPay.getData().getAddress().getFloorName()+billingPay.getData().getAddress().getAddress());
		
		
		GridLayout gl = (GridLayout)findViewById(R.id.goodsGL);
		//获取屏幕宽度
		WindowManager wm = this.getWindowManager();
	    int width = wm.getDefaultDisplay().getWidth();
	    int goodsWidth = width/4;
	    
		//显示商品
		for(int i=0;i<billingPay.getData().getGoods().size();i++){
			//创建视图容器
			RelativeLayout relativeLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(goodsWidth, goodsWidth);
			relativeLayout.setLayoutParams(params);
			
			RelativeLayout.LayoutParams ivparams = new RelativeLayout.LayoutParams(goodsWidth , goodsWidth);
			ivparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			ivparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			
			//创建商品图片
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(ivparams);
			ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
			ImageLoader.getInstance().init(configuration);
			//显示图片的配置  
	        DisplayImageOptions options = new DisplayImageOptions.Builder()  
	                .cacheInMemory(true)  
	                .cacheOnDisk(true)  
	                .bitmapConfig(Bitmap.Config.RGB_565)  
	                .build();  
	        ImageLoader.getInstance().displayImage(billingPay.getData().getGoods().get(i).getGoodsImg(), iv,options);
			iv.setPadding(20, 20, 20, 20);
	        
			relativeLayout.addView(iv);
			//创建价格
			RelativeLayout.LayoutParams tvparams = new RelativeLayout.LayoutParams(goodsWidth-40, 50);
			tvparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			tvparams.setMargins(20, goodsWidth-70, 0, 0);
			TextView tv = new TextView(this);
			tv.setAlpha((float)0.30);
			tv.setLayoutParams(tvparams);
			tv.setText(billingPay.getData().getGoods().get(i).getGoodsPrice());
			tv.setTextColor(Color.WHITE);
			tv.setBackgroundColor(Color.BLACK);
			tv.setGravity(Gravity.CENTER);
			relativeLayout.addView(tv);
			
			//加入列表
			GridLayout.Spec rowSpec = GridLayout.spec(i/4);   // 行
	        GridLayout.Spec columnSpec = GridLayout.spec(i%4);   //列
	        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec,
	                columnSpec);
			gl.addView(relativeLayout,layoutParams);
			
			//添加提醒
			BadgeView badgeView = new BadgeView(BusinessBillingDetailActivity.this, relativeLayout);  
	        badgeView.setText(billingPay.getData().getGoods().get(i).getGoodsNum());  
	        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
	        badgeView.show(); 
			
		}
		
		//显示详情信息
		RadioButton zxzf = (RadioButton)findViewById(R.id.zaixianzhifu);
		RadioButton dssd = (RadioButton)findViewById(R.id.dingshisongda);
		TextView time = (TextView)findViewById(R.id.time);
		TextView remark = (TextView)findViewById(R.id.remark);
		TextView coupon = (TextView)findViewById(R.id.coupon);
		TextView markt = (TextView)findViewById(R.id.marketPrice);
		TextView shop = (TextView)findViewById(R.id.shopPrice);
		if(billingPay.getData().getOrder().getPayment()==2)
			zxzf.setChecked(true);
		if(billingPay.getData().getOrder().getShippingStatus().equals("2")){
			dssd.setChecked(true);
			time.setText(billingPay.getData().getOrder().getTiming());
		}
		if(billingPay.getData().getOrder().getRemark().length()>0)
			remark.setText(billingPay.getData().getOrder().getRemark());
		if(billingPay.getData().getCoupon()!=null)
			coupon.setText(billingPay.getData().getCoupon().getName());
		markt.setText("￥"+billingPay.getData().getOrder().getGoodsMoney());
		shop.setText("￥"+billingPay.getData().getOrder().getMoney());
		
		
		
	}

	private void manageOrder(String subUrl,String orderId){
		String url = getString(R.string.base_url) + subUrl;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("orderid", orderId);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					Gson gson = new Gson();
					BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
					Toast.makeText(BusinessBillingDetailActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(BusinessBillingDetailActivity.this);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.business_billing_detail, menu);
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
