package com.school.schoolapp.activity.home;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseFragmentActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.activity.billing.BillingCustomActivity;
import com.school.schoolapp.activity.login.UserFastLoginActivity;
import com.school.schoolapp.activity.mine.MineCouponActivity;
import com.school.schoolapp.activity.mine.seller.MineCompetitiveActivity;
import com.school.schoolapp.activity.mine.seller.MineServiceMangerActivity;
import com.school.schoolapp.activity.mine.seller.MineServiceSoldActivity;
import com.school.schoolapp.activity.mine.seller.MineSpearActivity;
import com.school.schoolapp.activity.mine.work.MineWorkActivity;
import com.school.schoolapp.activity.mine.workspace.MineWorkspaceActivity;
import com.school.schoolapp.activity.technical.TechnicalPublishActivity;
import com.school.schoolapp.adapter.user.UserCouponHomeAdapter;
import com.school.schoolapp.application.ActivityApplication;
import com.school.schoolapp.callback.system.AppConfigCallback;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.user.UserCouponVO;
import com.school.schoolapp.fragment.main.MinePageFragment;
import com.school.schoolapp.fragment.main.StoreMainFragment;
import com.school.schoolapp.fragment.main.TechnicalMainFragment;
import com.school.schoolapp.fragment.main.MomentMainFragment;
import com.school.schoolapp.fragment.main.ChatMainFragment;
import com.school.schoolapp.tool.apkdownload.ApkDownload;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.Fragment;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
public class HomeMainActivity extends BaseFragmentActivity implements TechnicalMainFragment.OnFragmentInteractionListener, MinePageFragment.OnFragmentInteractionListener,StoreMainFragment.OnFragmentInteractionListener,MomentMainFragment.OnFragmentInteractionListener,ChatMainFragment.OnFragmentInteractionListener{

	private TechnicalMainFragment technicalFragment;
	private StoreMainFragment storeMainFragment ;
	private ChatMainFragment chatMainFragment ;
	//private MomentMainFragment momentMainFragment;
	private MinePageFragment minePageFragment;
	
	private Fragment nowFragment ;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			((RelativeLayout)findViewById(R.id.couponView)).setVisibility(View.GONE);
			//initFragment();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_main);
		
		checkVersion();
		checkForUnreadedMsg();
        
		ActivityApplication.getInstance().addActivity(this);
		
		IntentFilter filter= new IntentFilter();
		filter.addAction("Circle");
		registerReceiver(receiver, filter);
		
		this.backBtn.setAlpha(0);
		//提示获得到的优惠券
		ArrayList<UserCouponVO> coupons =(ArrayList<UserCouponVO>) getIntent().getSerializableExtra("coupon");
		if(coupons!=null && coupons.size()>0){
			((RelativeLayout)findViewById(R.id.couponView)).setVisibility(View.VISIBLE);
			((ListView)findViewById(R.id.coupon)).setAdapter(new UserCouponHomeAdapter(this,coupons));
			handler.sendEmptyMessageDelayed(0, 5000);
		}

		initFragment();
		
		RadioGroup tab = (RadioGroup)findViewById(R.id.tabRadioGroup);
		tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.helpRadio:
					swith2Fragment(technicalFragment);
					setActionBarView("需求大厅", Color.argb(1, 254, 216, 83), Color.BLACK,false);
					break;
				case R.id.storeRadio:
					if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(HomeMainActivity.this)==null){
						Toast.makeText(HomeMainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(HomeMainActivity.this,UserFastLoginActivity.class));
						break ;
					}else{
						if(storeMainFragment==null){
							storeMainFragment = new StoreMainFragment();
							getFragmentManager().beginTransaction().add(R.id.mainContent,storeMainFragment, "store").hide(storeMainFragment).commit();
						}
						swith2Fragment(storeMainFragment);
						setActionBarView("零食盒子", Color.WHITE, Color.BLACK,false);
						break;
					}
					
				case R.id.chatRadio:
					if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(HomeMainActivity.this)==null){
						Toast.makeText(HomeMainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(HomeMainActivity.this,UserFastLoginActivity.class));
						break ;
					}else{
						if(chatMainFragment ==null){
							chatMainFragment = new ChatMainFragment();
							getFragmentManager().beginTransaction().add(R.id.mainContent,chatMainFragment, "chat").hide(chatMainFragment).commit();
						}
						swith2Fragment(chatMainFragment);
						setActionBarView("消息", Color.argb(1, 254, 216, 83), Color.WHITE,false);
						break;
					}
					
//				case R.id.momentRadio:
//					swith2Fragment(momentMainFragment);
//					setActionBarView("", Color.argb(1, 254, 216, 83), Color.WHITE,false);
//					break;
				case R.id.mineRadio:
					swith2Fragment(minePageFragment);
					setActionBarView("", Color.argb(1, 254, 216, 83), Color.WHITE,false);
					break;

				default:
					break;
				}
			}
		});
	}
	
	private void initFragment(){
		if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(HomeMainActivity.this)==null){
			Toast.makeText(HomeMainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(HomeMainActivity.this,UserFastLoginActivity.class));
			return;
		}else{
			technicalFragment = new TechnicalMainFragment();
			//momentMainFragment = new MomentMainFragment();
			minePageFragment = new MinePageFragment();
			chatMainFragment = new ChatMainFragment();
			
			
			getFragmentManager().beginTransaction().add(R.id.mainContent,technicalFragment, "technical").commit();
			//getFragmentManager().beginTransaction().add(R.id.mainContent,momentMainFragment, "moment").hide(momentMainFragment).commit();
			getFragmentManager().beginTransaction().add(R.id.mainContent,minePageFragment, "mine").hide(minePageFragment).commit();
			getFragmentManager().beginTransaction().add(R.id.mainContent,chatMainFragment, "chat").hide(chatMainFragment).commit();
			
			//getFragmentManager().beginTransaction().replace(R.id.mainContent, technicalFragment).commit();
			
			//设置首页为第一个选项卡
//					getFragmentManager().beginTransaction().show(technicalFragment).commit();
//					nowFragment = technicalFragment;
//					setActionBarView("需求大厅", Color.argb(1, 254, 216, 83), Color.BLACK,false);
			//设置首页为第二个选项卡
			if(storeMainFragment==null){
				storeMainFragment = new StoreMainFragment();
				getFragmentManager().beginTransaction().add(R.id.mainContent,storeMainFragment, "store").show(storeMainFragment).commit();
			}
			nowFragment = storeMainFragment;
			setActionBarView("零食盒子", Color.WHITE, Color.BLACK,false);
			
			
		}
	}
	
	private void swith2Fragment(Fragment fragment){
		if(nowFragment !=fragment){
			getFragmentManager().beginTransaction().hide(nowFragment).commit();
			getFragmentManager().beginTransaction().show(fragment).commit();
			nowFragment = fragment;
		}
	}
	
	private void setActionBarView(String title,int backColor,int textColor,Boolean willShow){
		HomeMainActivity.this.titleTextView.setText(title);
		HomeMainActivity.this.view.setBackgroundColor(backColor);
		HomeMainActivity.this.titleTextView.setTextColor(textColor);
		if(!willShow && HomeMainActivity.this.actionBar.isShowing()){
			HomeMainActivity.this.actionBar.hide();
		}else if(willShow && !HomeMainActivity.this.actionBar.isShowing()){
			HomeMainActivity.this.actionBar.show();
		}
		
	}
	
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getStringExtra("visibility").equals("visible"))
				((ImageView)findViewById(R.id.maincircle)).setVisibility(View.VISIBLE);
			else
				((ImageView)findViewById(R.id.maincircle)).setVisibility(View.GONE);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_main, menu);
		disableABCShowHideAnimation(actionBar);
		return false;
	}
	

	//关闭actionbar显示隐藏时的动画效果
	 public void disableABCShowHideAnimation(ActionBar actionBar) {
		 
        try{
            actionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(actionBar, false);
        }

        catch (Exception exception)
        {
            try {

                Field mActionBarField = actionBar.getClass().getSuperclass().getDeclaredField("mActionBar");

                mActionBarField.setAccessible(true);

                Object icsActionBar = mActionBarField.get(actionBar);

                Field mShowHideAnimationEnabledField = icsActionBar.getClass().getDeclaredField("mShowHideAnimationEnabled");

                mShowHideAnimationEnabledField.setAccessible(true);

                mShowHideAnimationEnabledField.set(icsActionBar,false);

                Field mCurrentShowAnimField = icsActionBar.getClass().getDeclaredField("mCurrentShowAnim");

                mCurrentShowAnimField.setAccessible(true);

                mCurrentShowAnimField.set(icsActionBar,null);

                //icsActionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(icsActionBar, false);

            }catch (Exception e){

            }
        }
    }
	 
	 private Class<?>[] buyers ={MineCouponActivity.class,TechnicalPublishActivity.class,MineCouponActivity.class,MineCouponActivity.class,BillingCustomActivity.class,MineCouponActivity.class,MineCouponActivity.class,MineCouponActivity.class};
	 public void buyerClick(View v){
		 if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(this)==null){
			startActivity(new Intent(this,UserFastLoginActivity.class));
			return;
		 }
		 
		 String tag = v.getTag().toString();
		 int index = Integer.parseInt(tag);
//		 startActivity(new Intent(this,buyers[index]));
		 
		 switch (index) {
		case 0:
			
			break;
		case 1:
			startActivity(new Intent(this,BillingCustomActivity.class));
			break;
		case 2:
			startActivity(new Intent(this,MineCouponActivity.class));
			break;
		case 3:
			//startActivity(new Intent(this,BillingCustomActivity.class));
			break;

		default:
			break;
		}
	 }
	 private Class<?>[] sellers ={MineServiceSoldActivity.class,MineCouponActivity.class,MineServiceMangerActivity.class,MineCouponActivity.class,MineSpearActivity.class,MineCompetitiveActivity.class,MineWorkspaceActivity.class,MineWorkActivity.class};
	 public void sellerClick(View v){
		 if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(this)==null){
			startActivity(new Intent(this,UserFastLoginActivity.class));
			return;
		 }
		 
		 String tag = v.getTag().toString();
		 int index = Integer.parseInt(tag);
		 //判断是不是店小二
		 if(index==0){
			 //需要判断是不是CEO的接口
			 isWater();
		 }
		 
		 switch (index) {
//			case 0:
//				startActivity(new Intent(this,MineServiceSoldActivity.class));
//				break;
//			case 1:
//			    startActivity(new Intent(this,TechnicalPublishActivity.class));
//				break;
//			case 2:
//				startActivity(new Intent(this,MineServiceMangerActivity.class));
//				break;
			case 1:
				isCanApply();
				break;
	
			default:
				break;
		}
		
	 }

	private void isWater(){
		String url =getString(R.string.base_url)+getString(R.string.workspace_iswater);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					startActivity(new Intent(HomeMainActivity.this,MineWorkspaceActivity.class));
				}else{
					ToastTool.showWithMessage(callback.getMsg(), HomeMainActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void isCanApply(){
		String url =getString(R.string.base_url)+getString(R.string.workspace_iswater);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("0")){
					startActivity(new Intent(HomeMainActivity.this,MineWorkActivity.class));
				}else{
					ToastTool.showWithMessage(callback.getMsg(), HomeMainActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
		        if((System.currentTimeMillis()-exitTime) > 2000){  
		        	Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
		            exitTime = System.currentTimeMillis();   
		        } else {
		        	finish();
		        }
		        return true;   
		    }
		return super.onKeyDown(keyCode, event);
	}
	
	public void dismissCoupon(View c){
		((RelativeLayout)findViewById(R.id.couponView)).setVisibility(View.GONE);
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
	
	public String getVersionCode(){
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo;
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void checkVersion(){
		//获得本地版本
		final String versionCode = getVersionCode();
				
		String url = getString(R.string.base_url)+getString(R.string.get_app_config);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getTicket());
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				AppConfigCallback config = new Gson().fromJson(new String(arg2), AppConfigCallback.class);
				if(!config.getData().getVersion().equals(versionCode)){//判断是否需要升级
					ApkDownload.getInstance().alertToUpdate(config.getData().getPath(), HomeMainActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void checkForUnreadedMsg(){
		SharedPreferences sp =getSharedPreferences("sp",MODE_PRIVATE);
		String userStr = sp.getString("USERLIST", "NONE");
		if(!userStr.equals("NONE")){
			Gson gson = new Gson();
			List<String> userList = gson.fromJson(userStr, List.class);
			for(String username : userList){
				//获取所有会话
				Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
				//获取某用户会话
				EMConversation conversation = conversations.get(username);
				if(conversation.getUnreadMsgCount()>0){
					((ImageView)findViewById(R.id.maincircle)).setVisibility(View.VISIBLE);
					return;
				}
			}
		}
	}
}
