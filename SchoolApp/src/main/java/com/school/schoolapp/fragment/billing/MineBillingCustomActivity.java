package com.school.schoolapp.fragment.billing;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.school.schoolapp.BaseFragmentActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.billing.BillingCustomAdapter;
import com.school.schoolapp.adapter.store.StoreGoosAdapter;
import com.school.schoolapp.fragment.billing.BillingCustomFragment.OnFragmentInteractionListener;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MineBillingCustomActivity extends BaseFragmentActivity implements BillingCustomFragment.OnFragmentInteractionListener {
	
	private ViewPager billingViewPager;
	private View allView,payingView,receivingView,postingView,sendingView;
	private ArrayList<View> viewList = new ArrayList<View>();
	private ArrayList<String> titleList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_billing_custom);
		this.titleTextView.setText("订单管理");
		
		LayoutInflater lf = getLayoutInflater().from(this); 
		
		allView = lf.inflate(R.layout.viewpager_billing_listview, null);
		payingView = lf.inflate(R.layout.viewpager_billing_listview, null);
		receivingView = lf.inflate(R.layout.viewpager_billing_listview, null);
		postingView = lf.inflate(R.layout.viewpager_billing_listview, null);
		sendingView = lf.inflate(R.layout.viewpager_billing_listview, null);
		
		viewList.add(allView);
		viewList.add(payingView);
		viewList.add(receivingView);
		viewList.add(postingView);
		viewList.add(sendingView);
		
		titleList.add("全部");
		titleList.add("待付款");
		titleList.add("待接单");
		titleList.add("待发货");
		titleList.add("配送中");
		
		billingViewPager = (ViewPager)findViewById(R.id.billingViewPager);
		PagerAdapter adapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return viewList.size();
			}
			
			@Override
			public CharSequence getPageTitle(int position) {
				// TODO Auto-generated method stub
				return titleList.get(position);
			}
			
			@Override
			public Object instantiateItem(View container, int position) {

				ListView listview = new ListView(MineBillingCustomActivity.this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				          LinearLayout.LayoutParams.MATCH_PARENT,
				          LinearLayout.LayoutParams.MATCH_PARENT);
				BillingCustomAdapter adapter = new BillingCustomAdapter(MineBillingCustomActivity.this,MineBillingCustomActivity.this.ticket);
				listview.setAdapter(adapter);
				listview.setLayoutParams(params);
				
			      //用代码创建一个layout
			      LinearLayout layout = new LinearLayout(MineBillingCustomActivity.this);
			      
			      //layout.setGravity(Gravity.CENTER);
			      layout.setLayoutParams(params);
			      layout.addView(listview);
			      ((ViewGroup) container).addView(layout);
			      return layout;
			}
			
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				container.removeView(viewList.get(position)); 
			}
		};
		billingViewPager.setAdapter(adapter);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_billing_custom, menu);
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
	
	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}
	
}
