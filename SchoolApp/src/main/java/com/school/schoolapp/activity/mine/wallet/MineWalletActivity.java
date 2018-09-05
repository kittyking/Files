package com.school.schoolapp.activity.mine.wallet;

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
import com.school.schoolapp.adapter.wallet.WalletDetailAdapter;
import com.school.schoolapp.callback.wallet.WalletDetailCallback;
import com.school.schoolapp.classes.mine.MineWalletBalanceCallback;
import com.school.schoolapp.classes.tools.ToastTool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MineWalletActivity extends BaseActivity {

	private TextView balanceTV,transfringTV,earnedTV,canTransferTV;
	
	private String money;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_wallet);
		this.titleTextView.setText("我的钱包");
		this.doBtn.setText("明细");
		this.doBtn.setAlpha(1);
		this.view.setBackgroundColor(Color.WHITE);

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getRemainMoney();
		super.onResume();
	}
	
	public void getRemainMoney(){
		String url = getString(R.string.base_url)+getString(R.string.wallet_get_balace);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params= new RequestParams();
		params.put("ticket", this.ticket);
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				MineWalletBalanceCallback balance = gson.fromJson(new String(arg2), MineWalletBalanceCallback.class);
				if(balance.getResult().equals("1")){
					balanceTV = (TextView)findViewById(R.id.balance);
					balanceTV.setText(balance.getBalance());
					money =balance.getBalance();
					setupBtn();
					setupDetail();
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
		getMenuInflater().inflate(R.menu.mine_wallet, menu);
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
	private int curpage=1;
	private ListView detailLV;
	WalletDetailAdapter adapter;
	private void setupDetail(){

		detailLV = (ListView)findViewById(R.id.detail);
		adapter = new WalletDetailAdapter(this);
		detailLV.setAdapter(adapter);
		
		requestDetail();
	}
	private void requestDetail(){
		String url = getString(R.string.base_url)+getString(R.string.wallet_detail);
		RequestParams params= new RequestParams();
		params.put("ticket", this.ticket);
		params.put("curpage", curpage);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				WalletDetailCallback details = new Gson().fromJson(new String(arg2), WalletDetailCallback.class);
				if(details.getData().size()>0){
					adapter.addDetail(details.getData());
				}
				curpage++;
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setupBtn(){
		Button recharge = (Button)findViewById(R.id.rechargeBtnm);
		recharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MineWalletActivity.this,MineWalletRechargeActivity.class);
				startActivity(intent);
			}
		});
		Button cash = (Button)findViewById(R.id.cashBtn);
		cash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MineWalletActivity.this,MineWalletCashActivity.class);
				startActivity(intent);
			}
		});
		
//		Button add = (Button)findViewById(R.id.addBtn);
//		add.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MineWalletActivity.this,MineWalletAddAcountActivity.class);
//				startActivity(intent);
//			}
//		});
		
//		Button manage = (Button)findViewById(R.id.manageBtn);
//		manage.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MineWalletActivity.this,MineWalletIDManageActivity.class);
//				startActivity(intent);
//			}
//		});
	}
}
