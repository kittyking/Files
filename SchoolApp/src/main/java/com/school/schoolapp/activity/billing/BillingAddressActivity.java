package com.school.schoolapp.activity.billing;

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
import com.school.schoolapp.adapter.user.UserAddressAdapter;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserAddressCallback;
import com.school.schoolapp.entity.user.UserAdressVO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BillingAddressActivity extends BaseActivity {
	
	private UserAddressCallback addresses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_address);
		this.titleTextView.setText("选择地址");
		
		
		
		Button addBtn = (Button)findViewById(R.id.addBtn);
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BillingAddressActivity.this,AddressAddActivity.class);
				intent.putExtra("Flag", "0");
				startActivity(intent);
			}
		});
	}
	
	public void getAddressList(){
		String url = getString(R.string.base_url)+getString(R.string.user_address_get);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				addresses=  new UserAddressCallback();
				addresses = gson.fromJson(new String(arg2), UserAddressCallback.class);
				if(addresses.getResult().equals("1")){
					ListView lv = (ListView)findViewById(R.id.addressLV);
					UserAddressAdapter adapter = new UserAddressAdapter(BillingAddressActivity.this, addresses.getData().getAddressList());
					lv.setAdapter(adapter);
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							UserAdressVO address =  addresses.getData().getAddressList().get(position);
							Bundle bundle = new Bundle();
							bundle.putSerializable("Address",address);
							intent.putExtras(bundle);
							setResult(101,intent);
							finish();
						}
						
					});
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
	protected void onResume() {
		// TODO Auto-generated method stub
		getAddressList();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.billing_address, menu);
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
