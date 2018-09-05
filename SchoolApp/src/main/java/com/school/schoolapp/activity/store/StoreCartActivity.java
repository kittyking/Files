package com.school.schoolapp.activity.store;

import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.store.StoreCartAdapter;
import com.school.schoolapp.entity.store.StoreCartEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StoreCartActivity extends BaseActivity {
	
	private Button buyBtn;
	private LinearLayout cartLinear;
	private BadgeView cartBadge;
	
	private float lowest;
	private Handler handler= new Handler(){
		public void handleMessage(android.os.Message msg) {
			//获取到购物车发生改变
			TextView cartMoney = (TextView)findViewById(R.id.cartMoney);
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

	private ListView cartListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_cart);
		this.titleTextView.setText("购物车");
		
		Intent intent = getIntent();
		lowest = Float.parseFloat(intent.getStringExtra("Lowest"));
		
		buyBtn = (Button)findViewById(R.id.buyBtn);
		setBtnInfo();
		
		cartLinear = (LinearLayout)findViewById(R.id.cartsLinear);
		cartBadge = new BadgeView(this,cartLinear);
		cartBadge.setTextSize(9);
        cartBadge.setText(""+StoreCartEntity.getInstance().getNumCount());  
        cartBadge.setPadding(15, 5, 15, 5);
        if(StoreCartEntity.getInstance().getNumCount()==0)
			cartBadge.hide();
		else {
			cartBadge.show();
		}
		
		cartListView = (ListView)findViewById(R.id.cartListView);
		StoreCartAdapter adapter = new StoreCartAdapter(this);
		adapter.setHandler(handler);
		cartListView.setAdapter(adapter);
	}
	
	public void setBtnInfo(){
		if(StoreCartEntity.getInstance().getAmountMoney()<lowest){
			float remain = lowest - StoreCartEntity.getInstance().getAmountMoney();
			buyBtn.setText("还差"+remain+"元起送");
			buyBtn.setBackground(getResources().getDrawable(R.drawable.background_cart_gray));
		}else{
			buyBtn.setText("立即购买");
			buyBtn.setBackground(getResources().getDrawable(R.drawable.background_cart_orange));
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_cart, menu);
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
