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
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.mine.MineWalletBalanceCallback;
import com.school.schoolapp.classes.tools.ToastTool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MineWalletCashActivity extends BaseActivity {

	private EditText money,account;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_wallet_cash);
		this.titleTextView.setText("提现");
		this.view.setBackgroundColor(Color.WHITE);
		
		TextView balanceTV = (TextView)findViewById(R.id.balance);
		balanceTV.setText(getIntent().getStringExtra("ye"));
		money=(EditText)findViewById(R.id.money);
		account=(EditText)findViewById(R.id.account);
		
		((Button)findViewById(R.id.submit)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cash();
			}
		});
	}
	private void cash(){
		String url = getString(R.string.base_url)+getString(R.string.workspace_cash);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("money",money.getText().toString());
		params.put("acount",account.getText().toString());
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				ToastTool.showMessage(callback.getMsg(), MineWalletCashActivity.this);
				if(callback.getResult().equals("1")){
					finish();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_wallet_cash, menu);
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
