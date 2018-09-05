package com.school.schoolapp.adapter.billing;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.R;
import com.school.schoolapp.activity.billing.BillingBusinessActivity;
import com.school.schoolapp.activity.billing.BillingCustomActivity;
import com.school.schoolapp.activity.chat.ChatDetailActivity;
import com.school.schoolapp.activity.store.StorePaySuccessActivity;
import com.school.schoolapp.callback.wxpay.WxPayCallback;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.alipay.PayResult;
import com.school.schoolapp.classes.billing.PayNoCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.billing.BillingVO;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;
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
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BillingCustomAdapter extends BaseAdapter {

	 private LayoutInflater mInflater;
		
	 private List<BillingVO> billings;
	 
	 private Context mContext;
	    
	 private String ticket;
	 
	 private String orderId;
	 
	 private int payIndex;
	 private int curpage;
	 private TextView orderStatutsTV;
	 
	 public BillingCustomAdapter(Context context,String ticket){
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.ticket = ticket;
		this.curpage = 1;
	}	
	 
	 public void addBillings(List<BillingVO> billings){
			if(this.billings==null)
				this.billings = new ArrayList<BillingVO>();
			for(BillingVO billing:billings)
				this.billings.add(billing);
	}
	 
	 public int getCurpage() {
		 int res = curpage;
		 curpage++;
		return res;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public void clearBillings(){
		curpage=1;
		 if(this.billings!=null)
				this.billings = new ArrayList<BillingVO>();
	 }
	 
	 public List<BillingVO> getBillings(){
		 return this.billings;
	 }
	 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(billings !=null)
			return billings.size();
		return 0;
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
	private String[] statusStr = {"待付款","待接单","待发货","配送中"};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.adapter_billing_custom, null);
		if(billings!=null&&billings.size()>0){
			TextView orderId = (TextView)convertView.findViewById(R.id.orderId);
			orderId.setText("订单号:"+billings.get(position).getOrderSn());
			
			orderStatutsTV = (TextView)convertView.findViewById(R.id.orderStatus);
			int statusIndex = Integer.parseInt(billings.get(position).getOrderStatus());
			orderStatutsTV.setText(statusStr[statusIndex -1]);
			
			TextView goodsNum = (TextView)convertView.findViewById(R.id.goodsnum);
			goodsNum.setText("共"+billings.get(position).getGoodsNum()+"件商品");
			
			TextView money = (TextView)convertView.findViewById(R.id.money);
			money.setText("实收款"+billings.get(position).getMoney()+"元");
			
			if(billings.get(position).getTiming()!=null &&!billings.get(position).getTiming().equals("请选择")){
				TextView time = (TextView)convertView.findViewById(R.id.time);
				time.setText(billings.get(position).getTiming()+"送达");
			}
			
			final int index = position;
			ImageButton phoneBtn = (ImageButton)convertView.findViewById(R.id.phoneBtn);
			phoneBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					payIndex=index;
					Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+billings.get(index).getMobile()));  
	                mContext.startActivity(intent); 
				}
			});
			
			ImageButton chatBtn = (ImageButton)convertView.findViewById(R.id.chatBtn);
			chatBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext,ChatDetailActivity.class);
					//需要传入店铺信息
					intent.putExtra("Username", billings.get(index).getUserName());
					mContext.startActivity(intent);
		            
				}
			});
			
			//显示商品列表
			GridLayout gl = (GridLayout)convertView.findViewById(R.id.goodsGL);
			//获取屏幕宽度
			WindowManager wm = ((Activity) mContext).getWindowManager();
		    int width = wm.getDefaultDisplay().getWidth();
		    int goodsWidth = width/4;
		    int count = billings.get(position).getGoods().size();
		    if(count>4)
		    	count=4;
			//显示商品
			for(int i=0;i<count;i++){
				//创建视图容器
				RelativeLayout relativeLayout = new RelativeLayout(mContext);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(goodsWidth, goodsWidth);
				relativeLayout.setLayoutParams(params);
				
				RelativeLayout.LayoutParams ivparams = new RelativeLayout.LayoutParams(goodsWidth , goodsWidth);
				ivparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				ivparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				
				//创建商品图片
				ImageView iv = new ImageView(mContext);
				iv.setLayoutParams(ivparams);
				ImageLoaderTool.getInstance().displayImage(billings.get(position).getGoods().get(i).getGoodsImg(), iv, mContext);
				iv.setPadding(20, 20, 20, 20);
		        
				relativeLayout.addView(iv);
				//创建价格
//				RelativeLayout.LayoutParams tvparams = new RelativeLayout.LayoutParams(goodsWidth-40, 50);
//				tvparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//				tvparams.setMargins(20, goodsWidth-70, 0, goodsWidth-70);
//				TextView tv = new TextView(this);
//				tv.setAlpha((float)0.30);
//				tv.setLayoutParams(tvparams);
//				tv.setText(billingPay.getData().getGoods().get(0).getGoodsPrice());
//				tv.setTextColor(Color.WHITE);
//				tv.setBackgroundColor(Color.BLACK);
//				tv.setGravity(Gravity.CENTER);
//				relativeLayout.addView(tv);
				
				//加入列表
				GridLayout.Spec rowSpec = GridLayout.spec(0);   // 行
		        GridLayout.Spec columnSpec = GridLayout.spec(i);   //列
		        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec,
		                columnSpec);
				gl.addView(relativeLayout,layoutParams);
				
				//添加提醒
				BadgeView badgeView = new BadgeView(mContext, relativeLayout);  
		        badgeView.setText(billings.get(position).getGoods().get(i).getGoodsNum());  
		        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		        badgeView.show(); 
				
			}
			
			setBtn(position,convertView);
			
		}
		return convertView;
	}
	
	public void setBtn(final int position,View view){
		Button submitBtn = (Button)view.findViewById(R.id.submitBtn);
		Button cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
		switch (billings.get(position).getOrderStatus()) {
		case "1":
			if(billings.get(position).getPayStatus()==0){//待付款
				if(billings.get(position).getPayment()==1){//payment=1 货到付款 不让用户做其他处理
					orderStatutsTV.setText("货到付款");
					submitBtn.setText("取消订单");
					submitBtn.setBackground(mContext.getResources().getDrawable(R.drawable.background_cart_gray));
					submitBtn.setTextColor(Color.WHITE);
					submitBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String url = mContext.getString(R.string.billing_custom_cancelnopay);
							manageOrder(url,billings.get(position).getOrderID(),position,billings.get(position).getShopID());
						}
					});
				}else{
					orderStatutsTV.setText("待付款");
					submitBtn.setText("确认付款");
					submitBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							orderId=billings.get(position).getOrderID();
							showPaySheet(position);
//							String url = mContext.getString(R.string.billing_business_confirm);
//							manageOrder(url,billings.get(position).getOrderID(),position,billings.get(position).getShopID());
						}
					});
					cancelBtn.setAlpha(1);
					cancelBtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String url = mContext.getString(R.string.billing_custom_cancelnopay);
							manageOrder(url,billings.get(position).getOrderID(),position,billings.get(position).getShopID());
						}
					});
				}
				
			}else if(billings.get(position).getPayStatus()==2){//待接单
				orderStatutsTV.setText("待接单");
				submitBtn.setText("取消订单");
				submitBtn.setBackground(mContext.getResources().getDrawable(R.drawable.background_cart_gray));
				submitBtn.setTextColor(Color.WHITE);
				submitBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url = mContext.getString(R.string.billing_custom_cancelnoreceive);
						manageOrder(url,billings.get(position).getOrderID(),position,billings.get(position).getShopID());
					}
				});
				
				
			}
			
			
			break;
		case "2"://待发货
			orderStatutsTV.setText("待发货");
			submitBtn.setText("提醒发货");
			submitBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = mContext.getString(R.string.billing_custom_remindship);
					manageOrder(url,billings.get(position).getOrderID(),position,billings.get(position).getShopID());
				}
			});
			break;
		case "3"://配送中
			orderStatutsTV.setText("配送中");
			submitBtn.setText("确认收货");
			submitBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = mContext.getString(R.string.billing_custom_confirmreceipt);
					manageOrder(url,billings.get(position).getOrderID(),position,billings.get(position).getShopID());
				}
			});
			cancelBtn.setText("删除订单");
			cancelBtn.setOnClickListener(new OnClickListener() {// =2在线支付
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = mContext.getString(R.string.billing_custom_deleteorder);
					manageOrder(url,billings.get(position).getOrderID(),position,billings.get(position).getShopID());
				}
			});
			
			
			break;
		case "4":
			orderStatutsTV.setText("已完成");
			submitBtn.setText("删除订单");
			submitBtn.setBackground(mContext.getResources().getDrawable(R.drawable.background_cart_gray));
			submitBtn.setTextColor(Color.WHITE);
			submitBtn.setOnClickListener(new OnClickListener() {// =2在线支付
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = mContext.getString(R.string.billing_custom_deleteorder);
					manageOrder(url,billings.get(position).getOrderID(),position,billings.get(position).getShopID());
				}
			});
			break;
		

		default:
			break;
		}
	}
	
	public void showPaySheet(int position){
		final PaySheet paySheet = new PaySheet(mContext).builder().setInfo(billings.get(position).getOrderSn()).setUser(LocalSharedPreferenceSingleton.getInstance().getUserInfo(mContext).getData().getUserName()).setMoney(billings.get(position).getMoney()).setCancelable(true).setCanceledOnTouchOutside(true);
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
	
	public void payByHTX(){
		String url =mContext.getString(R.string.base_url) + mContext.getString(R.string.pay_order_blance);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(mContext).getTicket());
		params.put("orderid", orderId);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
					if(callback.getResult().equals("1")){
						Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(mContext,StorePaySuccessActivity.class);
						intent.putExtra("way", 1);
						mContext.startActivity(intent);

						//清空购物车
						noticeCartInit();
					}else{
						if(callback.getMsg()!=null)
							Toast.makeText(mContext, callback.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void payByWx(String tradeno){
		new AsyncHttpClient().post(WXTools.getInstance().getUrl(mContext),WXTools.getInstance().getParams(tradeno, this.ticket), new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				WxPayCallback wxpay = gson.fromJson(new String(arg2), WxPayCallback.class);
				if(wxpay.getResult().equals("1")){
					WXTools.getInstance().payToWX(mContext,wxpay.getMch(), wxpay.getPrepay()); 
					//finish();
					//WXTools.getInstance().payToWxByInfo(StoreCreateBillingActivity.this, wxpay.getMch(), wxpay.getPrepay(), wxpay.getSign(), wxpay.getNonce());
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(mContext);
			}
		});
	}
	public void manageOrder(final String subUrl,String orderId,final int position,String shopID){
		String url = mContext.getString(R.string.base_url) + subUrl;
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
				//String s= new String(arg2);
				try {
					BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
					if(callback.getResult().equals("1")){
						if(!subUrl.equals(mContext.getString(R.string.billing_custom_remindship))){
							billings.remove(position);
							BillingCustomAdapter.this.notifyDataSetChanged();

							((BillingCustomActivity)mContext).getOrderStatusCount();
						}
					}
					Toast.makeText(mContext, callback.getMsg(), Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					ToastTool.showWithMessage("提醒发货失败", mContext);
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(mContext);
			}
		});
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
         mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
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
    					mContext.startActivity(new Intent(mContext,StorePaySuccessActivity.class));
    					//清空购物车
    					noticeCartInit();
    				} else {
    					// 判断resultStatus 为非"9000"则代表可能支付失败
    					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
    					if (TextUtils.equals(resultStatus, "8000")) {
    						Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();
    					} else {
    						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
    						Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
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
		
	
	private void noticeCartInit(){
		StoreCartEntity.initCart();
		Intent intent = new Intent();
		intent.setAction("Pay");
	    mContext.sendBroadcast(intent);
	}
	
	public void payOnline(final int type){
		String url = mContext.getString(R.string.base_url) + mContext.getString(R.string.pay_order_online);
		RequestParams params = new RequestParams();
		params.put("ticket", ticket);
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
				ToastTool.showNetworkError(mContext);
			}
		});
	}
	private void payByAli(String tradeno){
		String url = mContext.getString(R.string.base_url) + mContext.getString(R.string.pay_get_sign);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(mContext).getTicket());
		params.put("tradeno",tradeno);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				PayNoCallback payNo = gson.fromJson(new String(arg2), PayNoCallback.class);
				if(payNo.getResult().equals("1")){
					AliTools.getInstance().aliPay(payNo.getSign(), payNo.getCode(), (BillingCustomActivity)mContext);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(mContext);
			}
		});
	}

	private static final int SDK_PAY_FLAG = 1;
	
	private Handler payHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				Log.i("", "school===="+resultInfo);
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {//这里需要加入更高级的判断方式在resultStatus=9000，并且success=“true”以及sign=“xxx”校验通过的情况下，证明支付成功
					Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
					//关闭订单刷新数据
					billings.remove(payIndex);
					BillingCustomAdapter.this.notifyDataSetChanged();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			default:
				break;
			}
			
		};
	};

}
