package com.school.schoolapp.activity.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
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
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCityCallback;
import com.school.schoolapp.entity.user.UserCityVO;
import com.school.schoolapp.view.citylist.CityAdapter;
import com.school.schoolapp.view.citylist.CityData;
import com.school.schoolapp.view.citylist.ContactItemInterface;
import com.school.schoolapp.view.citylist.ContactListViewImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

public class UserCityActivity extends BaseActivity {
	private ContactListViewImpl listview;
	
	private List<ContactItemInterface> contactList;
	
	private List<String> citys;

	private Button curCity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_city);
		
		this.titleTextView.setText("选择城市");

		curCity = (Button)findViewById(R.id.curCity);
		//getCurCity();
		getCitys();
		
	}
	public void getCurCity(){
		//定位当前位置
		LocationClient client = new LocationClient(this);
		BDLocationListener bdLocation = new MyLocationListener();
		client.registerLocationListener(bdLocation);
		
		LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        client.setLocOption(option);
        
        client.start();
        
		//调用接口设置城市
	}
	public class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			//设置button值
			if(arg0.getLocType()==61||arg0.getLocType()==161){//定位成功代码
				curCity.setText("当前城市: "+arg0.getCity());
			}
			
		}
		
	}
	public void getCitys(){
		 
		String url = getString(R.string.base_url) + getString(R.string.user_city_get);
		final AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				final Gson gson = new Gson();
				final UserCityCallback citycallback = gson.fromJson(new String(arg2), UserCityCallback.class);
				citys = new ArrayList<String>();
				if(citycallback.getResult().equals("1")){
					for(int i=0;i<citycallback.getData().size();i++){
						citys.add(citycallback.getData().get(i).getCityName());
					}
					
					contactList = CityData.getSampleContactList(citys);
					CityAdapter adapter = new CityAdapter(UserCityActivity.this,
							R.layout.city_item, contactList);
					listview = (ContactListViewImpl)findViewById(R.id.listview);
					listview.setFastScrollEnabled(true);
					listview.setAdapter(adapter);
					
					//判断下一步需要设置学校 还是直接返回选择的城市
					Intent intent =getIntent();
					if(intent.getStringExtra("finish") != null){//直接返回
						listview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								cityid = contactList.get(position).getDisplayInfo();
								for(UserCityVO city : citycallback.getData()){
									if(contactList.get(position).getDisplayInfo().equals(city.getCityName())){
										cityid=city.getCityID();
										cityName=city.getCityName();
									}
								}
								intent.putExtra("cityid", cityid);
								intent.putExtra("cityname", cityName);
								//LocalSharedPreferenceSingleton.getInstance().setUserCity(UserCityActivity.this, citycallback.getData().get(position).getCityID(), citycallback.getData().get(position).getCityName());
								setResult(RESULT_OK, intent);
								finish();
							}
						});
					}else{//继续选择
						listview.setOnItemClickListener( new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								final int index = position;
								
								String url = getString(R.string.base_url) + getString(R.string.user_city_shop_set);
								RequestParams params = new RequestParams();
								params.put("ticket", UserCityActivity.this.ticket);
								cityid = contactList.get(position).getDisplayInfo();
								for(UserCityVO city : citycallback.getData()){
									if(contactList.get(position).getDisplayInfo().equals(city.getCityName())){
										cityid=city.getCityID();
										cityName=city.getCityName();
									}
									
								}
								params.put("cityid", cityid);
								client.post(url, params, new AsyncHttpResponseHandler() {
									
									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method stub
										BaseCallback callback = gson.fromJson(new String(arg2), BaseCallback.class);
										if(callback.getResult().equals("1")){
											Intent intent = new Intent(UserCityActivity.this,UserSchoolActivity.class);
											intent.putExtra("CityId", cityid);
											LocalSharedPreferenceSingleton.getInstance().setUserCity(UserCityActivity.this, cityid, cityName);
											startActivity(intent);
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
					
					
				}else {
					Toast.makeText(UserCityActivity.this, citycallback.getMsg(), Toast.LENGTH_SHORT).show();
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	private String cityid,cityName;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_city, menu);
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
