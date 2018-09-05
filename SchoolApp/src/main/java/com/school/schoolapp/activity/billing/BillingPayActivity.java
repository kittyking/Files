package com.school.schoolapp.activity.billing;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.Header;

import com.alipay.sdk.app.PayTask;
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
import com.school.schoolapp.activity.mine.workspace.MineWorkspaceActivity;
import com.school.schoolapp.activity.store.StoreCreateBillingActivity;
import com.school.schoolapp.activity.store.StorePaySuccessActivity;
import com.school.schoolapp.adapter.billing.BillingCustomAdapter;
import com.school.schoolapp.callback.wxpay.WxPayCallback;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.alipay.Base64;
import com.school.schoolapp.classes.alipay.PayResult;
import com.school.schoolapp.classes.alipay.SignUtils;
import com.school.schoolapp.classes.billing.BillingPayCallback;
import com.school.schoolapp.classes.billing.PayNoCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.tool.pay.AliTools;
import com.school.schoolapp.tool.pay.WXTools;
import com.school.schoolapp.widget.PaySheet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BillingPayActivity extends BaseActivity {
	private static final int SDK_PAY_FLAG = 1;
	
	private TextView nameTV,phoneTV,addressTV,timeBetweenTV;
	
	private String orderId;
	
	private long timeBetween;//距离订单过期时间。
	
	private BillingPayCallback billingPay;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			timeBetween --;
			long minute = timeBetween /60;
			long second = timeBetween %60;
			timeBetweenTV.setText("剩"+minute+"分"+second+"秒自动关闭");
			handler.sendEmptyMessageDelayed(0, 1000);
		};
	};
	
	private Button payBtn;
	private Button cancelBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_pay);
		
		this.titleTextView.setText("订单详情");
		payBtn = (Button)findViewById(R.id.payBtn);
		cancelBtn = (Button)findViewById(R.id.cancelBtn);
		
		Intent intent = getIntent();
		orderId = intent.getStringExtra("OrderId");
		
		String url = getString(R.string.base_url)+getString(R.string.billing_business_pay);
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
					setupPayButton();
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
		
	}
	
	public void setupPayButton(){
		
		if(billingPay.getData().getOrder().getPayment()==1){//货到付款
			cancelBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = getString(R.string.billing_custom_cancelnopay);
					manageOrder(url,billingPay.getData().getOrder().getOrderID(),billingPay.getData().getOrder().getShopid());
				
				}
			});
			payBtn.setText("提醒发货");
			payBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String urlStr = getString(R.string.base_url) + getString(R.string.billing_custom_remindship);
					RequestParams remindparams = new RequestParams();
					remindparams.put("ticket", ticket);
					remindparams.put("orderid", billingPay.getData().getOrder().getOrderID());
					remindparams.put("shopID",billingPay.getData().getOrder().getShopid());
					AsyncHttpClient remindclient = new AsyncHttpClient();
					remindclient.post(urlStr, remindparams, new AsyncHttpResponseHandler() {
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							try {
								BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
								Toast.makeText(BillingPayActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
							} catch (Exception e) {
								ToastTool.showWithMessage("提醒发送失败", BillingPayActivity.this);
							}
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			});
			
		}else{
			
			//判断订单状态
			switch (billingPay.getData().getOrder().getOrderStatus()) {
			case "1"://待付款
				if(billingPay.getData().getOrder().getPayStatus().equals("0")){
					cancelBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String url = getString(R.string.billing_custom_cancelnopay);
							manageOrder(url,billingPay.getData().getOrder().getOrderID(),billingPay.getData().getOrder().getShopid());
						
						}
					});
					payBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							final PaySheet paySheet = new PaySheet(BillingPayActivity.this).builder().setInfo(billingPay.getData().getOrder().getOrderSN()).setUser(BillingPayActivity.this.user.getData().getUserName()).setMoney(billingPay.getData().getOrder().getMoney()).setCancelable(true).setCanceledOnTouchOutside(true);
							paySheet.setPayButton(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									switch (paySheet.getCurrentRB()) {
									case 0:
										payByZFB();
										break;
									case 1:
										payByWX();
										break;
									case 2:
										payByHTX();
										break;
					
									default:
										break;
									}
									paySheet.dismiss();
								}
							});
							paySheet.show();
						}
					});
				}else if(billingPay.getData().getOrder().getPayStatus().equals("2")){//待接单
					cancelBtn.setVisibility(View.GONE);
					payBtn.setText("取消订单");;
					payBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String url = getString(R.string.billing_custom_cancelnoreceive);
							manageOrder(url,billingPay.getData().getOrder().getOrderID(),billingPay.getData().getOrder().getShopid());
						}
					});
				}
				break;
			case "2"://待发货
				payBtn.setText("提醒发货");
				payBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url = getString(R.string.billing_custom_remindship);
						manageOrder(url,billingPay.getData().getOrder().getOrderID(),billingPay.getData().getOrder().getShopid());
					}
				});
				break;
			case "3"://配送中
				cancelBtn.setVisibility(View.GONE);
				payBtn.setText("确认收货");
				payBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url = getString(R.string.billing_custom_confirmreceipt);
						manageOrder(url,billingPay.getData().getOrder().getOrderID(),billingPay.getData().getOrder().getShopid());
					}
				});
				break;
			case "4"://已完成
				cancelBtn.setVisibility(View.GONE);
				payBtn.setText("删除订单");
				payBtn.setOnClickListener(new OnClickListener() {//
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url =getString(R.string.billing_custom_deleteorder);
						manageOrder(url,billingPay.getData().getOrder().getOrderID(),billingPay.getData().getOrder().getShopid());
					}
				});
				break;
			default:
				break;
			}
			
			
		}
	}
	
	private void payByWX(){
		payOnline(2);
	}
	
	public void payByZFB(){
		Log.i("", "支付宝");
		//registerBroadCast();
		AliTools.getInstance().registResultReceiver(this, mBroadcastReceiver);
		payOnline(1);
	}
	
	private  BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String action = arg1.getAction();  
            if(action.equals(AliTools.BRODECAST_NAME)){
            	int flag = arg1.getIntExtra("flag",0);
            	String result = arg1.getStringExtra("result");
            	switch (flag) {
    			case SDK_PAY_FLAG: {
    				String resultStatus = AliTools.getInstance().getResultInfo(result);
    				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
    				if (TextUtils.equals(resultStatus, "9000")) {//这里需要加入更高级的判断方式在resultStatus=9000，并且success=“true”以及sign=“xxx”校验通过的情况下，证明支付成功
    					startActivity(new Intent(BillingPayActivity.this,StorePaySuccessActivity.class));
    					
    					finish();
    				} else {
    					// 判断resultStatus 为非"9000"则代表可能支付失败
    					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
    					if (TextUtils.equals(resultStatus, "8000")) {
    						Toast.makeText(BillingPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
    					} else {
    						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
    						Toast.makeText(BillingPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
    					}
    				}
    				break;
    			}
    			default:
    				break;
    			}
            }  
		}
	};
	
	public void payByHTX(){
		String url = getString(R.string.base_url) + getString(R.string.pay_order_blance);
		RequestParams params = new RequestParams();
		params.put("ticket", BillingPayActivity.this.ticket);
		params.put("orderid", orderId);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					Intent intent = new Intent(BillingPayActivity.this,StorePaySuccessActivity.class);
					intent.putExtra("way", 1);
					startActivity(intent);
					finish();
				}
				if(callback.getMsg()!=null)
					Toast.makeText(BillingPayActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void manageOrder(String subUrl,String orderId,String shopID){
		String url = getString(R.string.base_url) + subUrl;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("orderid", orderId);
		if(shopID!=null)
			params.put("shopID",shopID);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				try {
					BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
					if(callback.getResult().equals("1")){
						finish();
					}
					Toast.makeText(BillingPayActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					ToastTool.showWithMessage("提醒发送失败", BillingPayActivity.this);
				}
				
				
				BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					finish();
				}
				Toast.makeText(BillingPayActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(BillingPayActivity.this);
			}
		});
	}
	
	public String getPrivateKey(String content, String privateKey) throws Exception {
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
				Base64.decode(privateKey));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);

		java.security.Signature signature = java.security.Signature
				.getInstance("SHA1WithRSA");

		signature.initSign(priKey);
		signature.update(content.getBytes("UTF-8"));

		byte[] signed = signature.sign();

		return Base64.encode(signed);
  }
	
	public void initPayBilling(BillingPayCallback billingPay){

		//显示收货地址
		nameTV = (TextView)findViewById(R.id.nameTV);
		phoneTV = (TextView)findViewById(R.id.phoneTV);
		addressTV = (TextView)findViewById(R.id.addressTV);
		
		nameTV.setText(billingPay.getData().getAddress().getName());
		phoneTV.setText(billingPay.getData().getAddress().getMobile());
		addressTV.setText(billingPay.getData().getAddress().getSchoolName()+billingPay.getData().getAddress().getFloorName()+billingPay.getData().getAddress().getAddress());
		
		
		if(billingPay.getData().getOrder().getPayStatus() == "0"){
			((LinearLayout)findViewById(R.id.timeLinear)).setVisibility(View.VISIBLE);
			timeBetweenTV = (TextView)findViewById(R.id.timeBetween);
			//未付款
			//设置倒计时
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String finishTime = billingPay.getData().getOrder().getPayFinashTime();
			try {
				Date finishDate = df.parse(finishTime);
				Date currentDate = new Date(System.currentTimeMillis());
				timeBetween = (finishDate.getTime() - currentDate.getTime())/1000;//两时间差多少秒
				handler.sendEmptyMessageDelayed(0, 1000);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
		
		
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
			BadgeView badgeView = new BadgeView(BillingPayActivity.this, relativeLayout);  
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
	private void cancelOrder(String orderId){
		String url = getString(R.string.base_url) + getString(R.string.billing_custom_cancelnopay);
		RequestParams params = new RequestParams();
		params.put("ticket", BillingPayActivity.this.ticket);
		params.put("orderid", orderId);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1"))
					BillingPayActivity.this.finish();
				Toast.makeText(getApplicationContext(), callback.getMsg(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	public void payOnline(final int type){
		String url = getString(R.string.base_url) + getString(R.string.pay_order_online);
		RequestParams params = new RequestParams();
		params.put("ticket", BillingPayActivity.this.ticket);
		params.put("orderid", orderId);
		params.put("type", type);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Gson gson = new Gson();
				PayNoCallback payNo = gson.fromJson(new String(arg2), PayNoCallback.class);
				if(payNo.getResult().equals("1")){
					//final String orderInfo = getOrderInfo("支付商品", "商品描述：无", billingPay.getData().getOrder().getMoney(),payNo.getPayNo());
					if(type==1)
						payByAli(payNo.getPayNo());
					else
						payByWx(payNo.getPayNo());
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}

	private void payByAli(String tradeno){
		String url = getString(R.string.base_url) + getString(R.string.pay_get_sign);
		RequestParams params = new RequestParams();
		params.put("ticket", BillingPayActivity.this.ticket);
		params.put("tradeno",tradeno);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				PayNoCallback payNo = gson.fromJson(new String(arg2), PayNoCallback.class);
				if(payNo.getResult().equals("1")){
					AliTools.getInstance().aliPay(payNo.getSign(), payNo.getCode(), BillingPayActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	
	private void payByWx(String tradeno){
		new AsyncHttpClient().post(WXTools.getInstance().getUrl(this),WXTools.getInstance().getParams(tradeno, this.ticket), new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				WxPayCallback wxpay = gson.fromJson(new String(arg2), WxPayCallback.class);
				if(wxpay.getResult().equals("1")){
					WXTools.getInstance().payToWX(BillingPayActivity.this,wxpay.getMch(), wxpay.getPrepay()); 
					//finish();
					//WXTools.getInstance().payToWxByInfo(StoreCreateBillingActivity.this, wxpay.getMch(), wxpay.getPrepay(), wxpay.getSign(), wxpay.getNonce());
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.billing_pay, menu);
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
