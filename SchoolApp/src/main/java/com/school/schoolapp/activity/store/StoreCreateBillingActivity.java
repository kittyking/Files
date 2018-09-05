package com.school.schoolapp.activity.store;

import java.net.URLEncoder;

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
import com.school.schoolapp.activity.billing.BillingAddressActivity;
import com.school.schoolapp.activity.billing.BillingPayActivity;
import com.school.schoolapp.adapter.billing.BillingCreateAdapter;
import com.school.schoolapp.callback.wxpay.WxPayCallback;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.alipay.PayResult;
import com.school.schoolapp.classes.billing.BillingPayCallback;
import com.school.schoolapp.classes.billing.PayNoCallback;
import com.school.schoolapp.classes.billing.BillingBuyCallback.BillingBuyVO;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.user.UserAdressVO;
import com.school.schoolapp.tool.pay.AliTools;
import com.school.schoolapp.tool.pay.WXTools;
import com.school.schoolapp.widget.PaySheet;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.school.schoolapp.entity.user.UserCouponVO;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class StoreCreateBillingActivity extends BaseActivity{
	private BillingBuyVO buyVO;
	
	private static final int SDK_PAY_FLAG = 1;
	
	private TextView nameTV,phoneTV,addressTV,moneyTV,timeTV,amountTV;
	
	private String addressid;
	
	private int payType = 2; //1货到付款 2在线支付
	
	private int shippingType = 1;//1极速配送 2定时送达

	private EditText remarkET;
	
	private String couponId ="0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_create_billing);
		this.titleTextView.setText("填写订单");
		
		amountTV = (TextView)findViewById(R.id.amountTV);
		amountTV.setText("￥"+StoreCartEntity.getInstance().getAmountMoney());
		
		TextView numTV = (TextView)findViewById(R.id.numTV);
		numTV.setText(StoreCartEntity.getInstance().getNumCount()+"");
		
		remarkET = (EditText)findViewById(R.id.remarkET);

		nameTV = (TextView)findViewById(R.id.nameTV);
		phoneTV = (TextView)findViewById(R.id.phoneTV);
		addressTV = (TextView)findViewById(R.id.addressTV);
		moneyTV = (TextView)findViewById(R.id.moneyTV);
		moneyTV.setText("￥"+StoreCartEntity.getInstance().getAmountMoney());
		timeTV = (TextView)findViewById(R.id.time);
		
		Intent intent = getIntent();
		buyVO = (BillingBuyVO)intent.getSerializableExtra("UserInfo");
		if(buyVO.getAddressList().size()>0)
			setAddress(buyVO.getAddressList().get(0));
		
		//设置优惠券
		if(buyVO.getCouponList().size()<=0){//无可用优惠券
			((Button)findViewById(R.id.couponBtn)).setText("无可用优惠券");
		}else{
			((Button)findViewById(R.id.couponBtn)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(StoreCreateBillingActivity.this);
	                builder.setTitle("请选择优惠券");
	                String[] cities=new String[buyVO.getCouponList().size()];
	                for(int i=0;i<buyVO.getCouponList().size();i++){
	                	UserCouponVO coupon =buyVO.getCouponList().get(i);
	                	cities[i]=coupon.getName();
	                }
	                builder.setItems(cities, new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                       couponId = buyVO.getCouponList().get(which).getCoupon_id();
	                       ((Button)findViewById(R.id.couponBtn)).setText(buyVO.getCouponList().get(which).getName());
	                       float price = StoreCartEntity.getInstance().getAmountMoney() -Float.parseFloat(buyVO.getCouponList().get(which).getPrice()); 
	                       moneyTV.setText("￥"+price);
	                       amountTV.setText("￥"+price);
	                    }
	                });
	                builder.show();
				}
			});
		}
		setupRadioGroup();
		setupGoods();
		
		Button address = (Button)findViewById(R.id.addressBtn);
		address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StoreCreateBillingActivity.this,BillingAddressActivity.class);
				//intent.putExtra("Flag", "0");
				startActivityForResult(intent, 101);
			}
		});
		
		Button buy = (Button)findViewById(R.id.buyBtn);
		buy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//创建订单
				String url = getString(R.string.base_url) + getString(R.string.billing_business_create);
				RequestParams params = new RequestParams();
				params.put("ticket", StoreCreateBillingActivity.this.ticket);
				Gson gson = new Gson();
				String goods = gson.toJson( StoreCartEntity.getInstance().buyGoodsList());
				params.put("goods", goods);
				params.put("couponid", couponId);
				params.put("payment", payType);
				params.put("shipping", shippingType);
				params.put("timing",timeTV.getText().toString());
				params.put("remark", remarkET.getText().toString());
				params.put("addressid",addressid);
				
				AsyncHttpClient client = new AsyncHttpClient();
				client.post(url, params, new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						String s = new String(arg2);
						Gson gson = new Gson();
						BillingCreateAdapter callback = new BillingCreateAdapter();
						callback = gson.fromJson(new String(arg2), BillingCreateAdapter.class);
						//创建订单成功弹出付款窗口
						if(callback.getResult().equals("1")){
							if(payType==1){//货到付款
								startActivity(new Intent(StoreCreateBillingActivity.this,StorePaySuccessActivity.class));
								noticeCartInit();
								finish();
							}else{
								//进行付款操作
								orderId = callback.getOrderid();
								gotoPay(orderId);
							}
							
							
						}else{
							if(callback.getMsg()!=null)
								Toast.makeText(getApplicationContext(), callback.getMsg(), Toast.LENGTH_SHORT).show();
						}
							
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						ToastTool.showNetworkError(getApplicationContext());
					}
				});
			}
		});
	}
	
	private String orderId,ordersn;
	private void gotoPay(String orderId){
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
				BillingPayCallback billingPay = gson.fromJson(new String(arg2), BillingPayCallback.class);
				if(billingPay.getResult().equals("1")){
					//获取了订单详情 准备进行支付
					//判断是在线支付还是货到付款
					if(billingPay.getData().getOrder().getPayment()==1){//货到付款
						Intent intent = new Intent(StoreCreateBillingActivity.this,StorePaySuccessActivity.class);
						intent.putExtra("way", 0);
						startActivity(intent);
						finish();
					}else{
						ordersn = billingPay.getData().getOrder().getOrderSN();
						showPayAciton(billingPay);
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
	private void showPayAciton(BillingPayCallback billingPay){
		final PaySheet paySheet = new PaySheet(StoreCreateBillingActivity.this).builder().setInfo(billingPay.getData().getOrder().getOrderSN()).setUser(StoreCreateBillingActivity.this.user.getData().getUserName()).setMoney(billingPay.getData().getOrder().getMoney()).setCancelable(true).setCanceledOnTouchOutside(true);
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
	
	private void noticeCartInit(){
		StoreCartEntity.initCart();
		
		Intent intent = new Intent();
		intent.setAction("Pay");
	    sendBroadcast(intent);
	}
	
	private void payByWX(){
		payOnline(2);
	}
	
	public void payByZFB(){
		Log.i("", "支付宝");
		registerBroadCast();
		payOnline(1);
	}
	
	//注册广播
	private void registerBroadCast(){
		 IntentFilter myIntentFilter = new IntentFilter();  
	     myIntentFilter.addAction(AliTools.BRODECAST_NAME);  
	     //注册广播        
         registerReceiver(mBroadcastReceiver, myIntentFilter);
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
    					startActivity(new Intent(StoreCreateBillingActivity.this,StorePaySuccessActivity.class));
    					//清空购物车
    					noticeCartInit();
    					finish();
    				} else {
    					// 判断resultStatus 为非"9000"则代表可能支付失败
    					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
    					if (TextUtils.equals(resultStatus, "8000")) {
    						Toast.makeText(StoreCreateBillingActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
    					} else {
    						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
    						Toast.makeText(StoreCreateBillingActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
		params.put("ticket", StoreCreateBillingActivity.this.ticket);
		params.put("orderid", orderId);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					Intent intent = new Intent(StoreCreateBillingActivity.this,StorePaySuccessActivity.class);
					intent.putExtra("way", 1);
					startActivity(intent);
					finish();
					
					//清空购物车
					noticeCartInit();
				}else{
					if(callback.getMsg()!=null)
						ToastTool.showWithMessage(callback.getMsg(), StoreCreateBillingActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void payOnline(final int type){
		String url = getString(R.string.base_url) + getString(R.string.pay_order_online);
		RequestParams params = new RequestParams();
		params.put("ticket", StoreCreateBillingActivity.this.ticket);
		params.put("orderid", orderId);
		params.put("type", type);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
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
		params.put("ticket", StoreCreateBillingActivity.this.ticket);
		params.put("tradeno",tradeno);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				PayNoCallback payNo = gson.fromJson(new String(arg2), PayNoCallback.class);
				if(payNo.getResult().equals("1")){
					AliTools.getInstance().aliPay(payNo.getSign(), payNo.getCode(), StoreCreateBillingActivity.this);
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
					WXTools.getInstance().payToWX(StoreCreateBillingActivity.this,wxpay.getMch(), wxpay.getPrepay()); 
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
	
	public void setupGoods(){
		GridLayout gl = (GridLayout)findViewById(R.id.goodsGL);

		//获取屏幕宽度
		WindowManager wm = this.getWindowManager();
	    int width = wm.getDefaultDisplay().getWidth();
	    int goodsWidth = width/4;
		//显示商品
		for(int i=0;i<buyVO.getGoodsList().size();i++){
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
	        ImageLoader.getInstance().displayImage(buyVO.getGoodsList().get(i).getGoods_img(), iv,options);
			iv.setPadding(20, 20, 20, 20);
	        
			relativeLayout.addView(iv);
			//创建价格
			RelativeLayout.LayoutParams tvparams = new RelativeLayout.LayoutParams(goodsWidth-40, 50);
			tvparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			tvparams.setMargins(20, goodsWidth-70, 0, 0);
			TextView tv = new TextView(this);
			tv.setAlpha((float)0.30);
			tv.setLayoutParams(tvparams);
			tv.setText(buyVO.getGoodsList().get(i).getShop_price());
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
			BadgeView badgeView = new BadgeView(StoreCreateBillingActivity.this, relativeLayout);  
	        badgeView.setText(buyVO.getGoodsList().get(i).getNum());  
	        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
	        badgeView.show(); 
			
		}
	}
	
	public void setAddress(UserAdressVO address){
		addressid= address.getAddress_id();
		nameTV.setText(address.getName());
		phoneTV.setText(address.getMobile());
		addressTV.setText(address.getSchoolName() +address.getFloorName()+ address.getAddress());
	}
	
	public void setupRadioGroup(){
		RadioGroup arrival = (RadioGroup)findViewById(R.id.arrivalRadioGroup);
		arrival.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.limitRB:
					showTime(0);
					shippingType= 1;
					break;
				case R.id.timeRB:
					showTime(1);
					shippingType = 2;
					break;

				default:
					break;
				}
			}
		});
		
		RadioGroup pay = (RadioGroup)findViewById(R.id.payRadioGroup);
		pay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.huodao:
					payType = 1;
					break;
				case R.id.zaixian:
					payType = 2;
					break;
				default:
					break;
				}
			}
		});
		
	}
	
	public void showTime(int flag){
		Button timeBtn = (Button)findViewById(R.id.timeBtn);
		timeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String[] arrayTime = new String[] { "11:30","12:00", "12:30", "13:00", "18:00", "18:30", "19:00", "19:30","20:00", "20:30", "21:30", "22:00" };
				Dialog alertDialog = new AlertDialog.Builder(StoreCreateBillingActivity.this).setTitle("设置送达时间").setItems(arrayTime, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						timeTV.setText(arrayTime[which]);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				}).create();
				alertDialog.show();
			}
		});
		
		RelativeLayout arrival = (RelativeLayout)findViewById(R.id.timeLinearLayout);
		LinearLayout.LayoutParams params =(LinearLayout.LayoutParams) arrival.getLayoutParams();
		switch (flag) {
		case 0://隐藏
			params.height = 0;
			arrival.setLayoutParams(params);
			break;
		case 1://显示
			params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			arrival.setLayoutParams(params);
			break;
		default:
			break;
		}
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == 101){
			UserAdressVO address = (UserAdressVO)data.getSerializableExtra("Address");
			setAddress(address);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_create_billing, menu);
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
//	@Override
//	public void onReq(BaseReq arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void onResp(BaseResp arg0) {
//		// TODO Auto-generated method stub
//		int result = 0;
//		
//		switch (arg0.errCode) {
//		case BaseResp.ErrCode.ERR_OK:
//			//result = R.string.errcode_success;
//			break;
//		case BaseResp.ErrCode.ERR_USER_CANCEL:
//			//result = R.string.errcode_cancel;
//			break;
//		case BaseResp.ErrCode.ERR_AUTH_DENIED:
//			//result = R.string.errcode_deny;
//			break;
//		default:
//			//result = R.string.errcode_unknown;
//			break;
//		}
//		
//		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//	}
}
