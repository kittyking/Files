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
import com.school.schoolapp.activity.billing.BillingPayActivity;
import com.school.schoolapp.activity.chat.ChatDetailActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.billing.BillingVO;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SumPathEffect;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BillingBusinessAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
    private List<BillingVO> billings;
	 
    private Context mContext;
    
    private String ticket;

	private int payIndex;
	
	private TextView orderStatusTV;
	
    public BillingBusinessAdapter(Context context,String ticket){
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.ticket = ticket;
	}
	 
	public void addBillings(List<BillingVO> billings,int flag){
		if(this.billings==null)
			this.billings = new ArrayList<BillingVO>();
		
		if(flag==0){
			if(billings!=null&&billings.size()>0){
				this.billings=billings;
			}
		}else{

			if(billings!=null&&billings.size()>0){
				for(BillingVO billing:billings)
					this.billings.add(billing);
			}
		}
		
	}
	
	public List<BillingVO> getBillings(){
		return billings;
	}
	
	public void addBilling(BillingVO billing){
		this.billings.add(billing);
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
	
	private String[] statusStr = {"待接单","待发货","配送中","已结单"};
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.adapter_billing_custom, null);
		if(billings!=null&&billings.size()>0){
			TextView orderId = (TextView)convertView.findViewById(R.id.orderId);
			orderId.setText("订单号:"+billings.get(position).getOrderSn());
			
			orderStatusTV = (TextView)convertView.findViewById(R.id.orderStatus);
			int statusIndex = Integer.parseInt(billings.get(position).getOrderStatus());
			orderStatusTV.setText(statusStr[statusIndex -1]);
			
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
			//显示商品
		    int count = billings.get(position).getGoods().size();
		    if(count>4)
		    	count=4;
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
		case "1"://待接单
			submitBtn.setText("我要接单");
			submitBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = mContext.getString(R.string.billing_business_confirm);
					manageOrder(url,billings.get(position).getOrderID(),position);
				}
			});
			cancelBtn.setText("取消订单");
			cancelBtn.setAlpha(1);
			cancelBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = mContext.getString(R.string.billing_business_cancel);
					manageOrder(url,billings.get(position).getOrderID(),position);
				}
			});
			if(billings.get(position).getPayment()==1){
				orderStatusTV.setText("货到付款");
				submitBtn.setText("确认发货");
				submitBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url = mContext.getString(R.string.billing_business_complete);
						finishOrder(url,billings.get(position).getOrderID(),position);
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
					String url = mContext.getString(R.string.billing_business_ship);
					manageOrder(url,billings.get(position).getOrderID(),position);
				}
			});
		    break;
		case "3"://配送中
			submitBtn.setText("完成订单");
			submitBtn.setAlpha(1);
			break;
		case "4"://已结单
			submitBtn.setText("删除订单");
			submitBtn.setBackground(mContext.getResources().getDrawable(R.drawable.background_cart_white));
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
	
	public void manageOrder(String subUrl,String orderId,final int position){
		String url = mContext.getString(R.string.base_url) + subUrl;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("orderid", orderId);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					billings.remove(position);
					BillingBusinessAdapter.this.notifyDataSetChanged();
					((BillingBusinessActivity)mContext).getOrderStatusCount();
				}
				Toast.makeText(mContext, callback.getMsg(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(mContext);
			}
		});
	}
	
	public void finishOrder(String subUrl,String orderId,final int position){
		String url = mContext.getString(R.string.base_url) + subUrl;
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("orderid", orderId);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					billings.get(position).setOrderStatus("4");
					BillingBusinessAdapter.this.notifyDataSetChanged();
					
					((BillingBusinessActivity)mContext).getOrderStatusCount();
				}
				Toast.makeText(mContext, callback.getMsg(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(mContext);
			}
		});
	}

}
