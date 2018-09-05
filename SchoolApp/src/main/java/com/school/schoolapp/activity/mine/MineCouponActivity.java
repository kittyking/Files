package com.school.schoolapp.activity.mine;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.user.UserCouponAdapter;
import com.school.schoolapp.callback.mine.UserCouponCallback;
import com.school.schoolapp.classes.tools.ToastTool;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MineCouponActivity extends BaseActivity {

	private ListView couponLV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_coupon);
		this.titleTextView.setText("我的优惠券");
	    couponLV = (ListView)findViewById(R.id.couponLV);
	    
	    requetCoupon();
	}

	public void requetCoupon(){
		String url = getString(R.string.base_url)+getString(R.string.user_get_coupon);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				UserCouponCallback coupon = new Gson().fromJson(new String(arg2), UserCouponCallback.class);
				if(coupon.getResult().equals("1")){
					couponLV.setAdapter(new UserCouponAdapter(MineCouponActivity.this,coupon.getData()));
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(MineCouponActivity.this);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_coupon, menu);
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
