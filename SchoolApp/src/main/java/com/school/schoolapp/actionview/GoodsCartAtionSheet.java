package com.school.schoolapp.actionview;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.R;
import com.school.schoolapp.activity.store.StoreCreateBillingActivity;
import com.school.schoolapp.adapter.store.StoreCartAdapter;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.billing.BillingBuyCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsCartAtionSheet {

	private Context context;
	private Display display;
	private Dialog dialog;
	
	public GoodsCartAtionSheet(Context context,float lowest,Handler handler){
		this.context = context;
		this.lowest = lowest;
		this.handler = handler;
		// 取得window对象
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		//得到窗口的尺寸对象
		display = windowManager.getDefaultDisplay();
	}
	
	public GoodsCartAtionSheet builder(){
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_goods_cart_actionsheet, null);
		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(display.getWidth());
		
		setupView(view);
		
		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width =  display.getWidth();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);
		return this;
	}
	private Button buyBtn;
	private LinearLayout cartLinear;
	private BadgeView cartBadge;
	private ListView cartListView;
	private Handler handler;
	private TextView cartMoney;
	private Handler carthandler= new Handler(){
		public void handleMessage(android.os.Message msg) {
			//获取到购物车发生改变
			cartMoney.setText("￥"+StoreCartEntity.getInstance().getAmountMoney());
			
			setBtnInfo();
			
	        cartBadge.setText(""+StoreCartEntity.getInstance().getNumCount());  
	        cartBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
	        cartBadge.setTextSize(9);
//	        if(isMax(mainInfo.getData().getDbh())){
//	        	bhBadge.setPadding(7, 3, 7, 3);
//			}else{
	        if(StoreCartEntity.getInstance().getNumCount()==0)
				cartBadge.hide();
	        else {
	        	cartBadge.show();
			}
	        cartBadge.setPadding(15, 5, 15, 5);
	        
		};
	};
	
	private void setupView(View view){
		buyBtn = (Button)view.findViewById(R.id.buyBtn);
		((ImageButton)view.findViewById(R.id.close)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		setBtnInfo();
		

		cartMoney = (TextView)view.findViewById(R.id.cartMoney);
		cartMoney.setText("￥"+StoreCartEntity.getInstance().getAmountMoney());
		cartLinear = (LinearLayout)view.findViewById(R.id.cartsLinear);
		cartBadge = new BadgeView(context,cartLinear);
		cartBadge.setTextSize(9);
        cartBadge.setText(""+StoreCartEntity.getInstance().getNumCount());  
        cartBadge.setPadding(15, 5, 15, 5);
        if(StoreCartEntity.getInstance().getNumCount()==0)
			cartBadge.hide();
		else {
			cartBadge.show();
		}
		
		cartListView = (ListView)view.findViewById(R.id.cartListView);
		StoreCartAdapter adapter = new StoreCartAdapter(context);
		adapter.setHandler(handler);
		adapter.setCartHandler(carthandler);
		cartListView.setAdapter(adapter);
	}
	
	private float lowest;
	private void setBtnInfo(){
		if(StoreCartEntity.getInstance().getAmountMoney()<lowest){
			float remain = lowest - StoreCartEntity.getInstance().getAmountMoney();
			buyBtn.setText("还差"+remain+"元起送");
			buyBtn.setBackground(context.getResources().getDrawable(R.drawable.background_cart_gray));
		}else{
			buyBtn.setText("立即购买");
			buyBtn.setBackground(context.getResources().getDrawable(R.drawable.background_cart_orange));
		}
		
		buyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!buyBtn.getText().toString().equals("立即购买"))
					return;
				
				final ZProgressHUD progressHUD = ZProgressHUD.getInstance(context); 
				progressHUD.setMessage("玩命加载中");
				progressHUD.show();
				
				if(buyBtn.getText().toString().equals("立即购买")){
					String url = context.getString(R.string.base_url) + context.getString(R.string.billing_business_buy);
					RequestParams params = new RequestParams();
					params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(context).getTicket());
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
							dismiss();
							Gson gson = new Gson();
							Log.i("", new String(arg2));
							BillingBuyCallback buyCallback = new BillingBuyCallback();
							buyCallback = gson.fromJson(new String(arg2), BillingBuyCallback.class);
							if(buyCallback.getResult().equals("1")){
								Intent intent = new Intent();
								intent.setClass(context, StoreCreateBillingActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("UserInfo", buyCallback.getData());
								intent.putExtras(bundle);
								context.startActivity(intent);
								
							}
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							ToastTool.showNetworkError(context);
							progressHUD.dismiss(); 
						}
					});
					
				}
			}
		});
	}
	
	
	public void show() {
		dialog.show();
	}
	public void dismiss(){
		dialog.dismiss();
	}
}
