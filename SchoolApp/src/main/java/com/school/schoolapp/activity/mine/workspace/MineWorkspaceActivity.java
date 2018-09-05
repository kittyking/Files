package com.school.schoolapp.activity.mine.workspace;

import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.activity.billing.BillingBusinessActivity;
import com.school.schoolapp.activity.mine.wallet.MineWalletActivity;
import com.school.schoolapp.activity.mine.wallet.MineWalletCashActivity;
import com.school.schoolapp.classes.workspace.WorkspaceMainCallback;
import com.school.schoolapp.entitys.shopVO.LetterVO;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class MineWorkspaceActivity extends BaseActivity {
	
	private TextView orderTV,addTV,manageTV,bhTV,lrTV,xsTV,cjTV;
	
	private TextSwitcher noticeSwitcher;

	private List<LetterVO> letters;
	private String ye;
	private int index ;
	private Handler noticeHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			index++;
			if(index==letters.size())
				index = 0;
			noticeSwitcher.setText(letters.get(index).getTitle());
			noticeHandler.sendEmptyMessageDelayed(0, 3000);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_workspace);
		this.titleTextView.setText("我的工作台");
		
		orderTV=(TextView)findViewById(R.id.orderTV);
		addTV=(TextView)findViewById(R.id.addTV);
		manageTV=(TextView)findViewById(R.id.manageTV);
		bhTV=(TextView)findViewById(R.id.bhTV);
		
		lrTV=(TextView)findViewById(R.id.lrTV);
		xsTV=(TextView)findViewById(R.id.xsTV);
		cjTV=(TextView)findViewById(R.id.cjTV);
		
		noticeSwitcher = (TextSwitcher)findViewById(R.id.notice);
		noticeSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
			
			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				TextView tv = new TextView(MineWorkspaceActivity.this);
				tv.setTextSize(16);
				tv.setTextColor(Color.rgb(102, 102, 102));
				return tv;
			}
		});
		
        Button exchangeBtn = (Button)findViewById(R.id.exchangeBtn);
        exchangeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MineWorkspaceActivity.this, BillingBusinessActivity.class);
				startActivity(intent);
			}
		});
        
        Button storeBtn = (Button)findViewById(R.id.storeBtn);
        storeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MineWorkspaceActivity.this, MineWorkspaceSetupActivity.class);
				startActivity(intent);
			}
		});
        
        Button replenishBtn = (Button)findViewById(R.id.replenishmentBtn);
        replenishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MineWorkspaceActivity.this, MineWorkspaceReplenishActivity.class);
				startActivity(intent);
			}
		});
        
        Button addBtn = (Button)findViewById(R.id.tianjiaBtn);
        addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MineWorkspaceActivity.this, MineWorkspaceAddGoodsActivity.class);
				startActivity(intent);
			}
		});
        
        Button managerBtn = (Button)findViewById(R.id.managerBtn);
        managerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MineWorkspaceActivity.this, MineWorkspaceGoodsManagerActivity.class);
				startActivity(intent);
			}
		});
        
        //我要上班按钮
        Button gotoWorkBtn = (Button)findViewById(R.id.gotoWorkBtn);
        gotoWorkBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MineWorkspaceActivity.this, MineWorkspaceGotoWorkActivity.class);
				startActivity(intent);
			}
		});
        
        Button budanBtn = (Button)findViewById(R.id.budanBtn);
        budanBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MineWorkspaceActivity.this, MineWorkspaceBuyselfActivity.class);
				startActivity(intent);
			}
		});
        
        ((Button)findViewById(R.id.cashBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MineWorkspaceActivity.this,MineWalletCashActivity.class);
				intent.putExtra("ye", ye);
				startActivity(intent);
			}
		});
	}
	
	private void requestInfo(){

		//获取工作台页面信息
		String url = getString(R.string.base_url)+getString(R.string.workspace_page_main);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String s = new String(arg2);
				Gson gson = new Gson();
				WorkspaceMainCallback mainInfo = gson.fromJson(new String(arg2), WorkspaceMainCallback.class);
				if(mainInfo.getResult().equals("1")){
					//设置余额
					lrTV.setText(mainInfo.getData().getYe());
					ye=mainInfo.getData().getYe();
					//设置月利润
					xsTV.setText(mainInfo.getData().getYlr());
					
					//设置总利润
					cjTV.setText(mainInfo.getData().getZlr());
					
					//设置订单管理数字
					if(!mainInfo.getData().getOrder().equals("0")){
						BadgeView orderBadge = new BadgeView(MineWorkspaceActivity.this, orderTV); 
						orderBadge.setText(mainInfo.getData().getOrder());
						orderBadge.setTextSize(12);
						if(isMax(mainInfo.getData().getOrder())){
							orderBadge.setPadding(7, 3, 7, 3);
						}else{
							orderBadge.setPadding(15, 3, 15, 3);
						}
						orderBadge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
						orderBadge.show();
					}
					
					
					//设置添加商品数字
					if(!mainInfo.getData().getDsj().equals("0")){
						BadgeView addBadge = new BadgeView(MineWorkspaceActivity.this, addTV);  
				        addBadge.setText(mainInfo.getData().getDtj());  
				        addBadge.setTextSize(12);
				        if(isMax(mainInfo.getData().getDtj())){
				        	addBadge.setPadding(7, 3, 7, 3);
						}else{
							addBadge.setPadding(15, 3, 15, 3);
						}
				        addBadge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
				        addBadge.show();
					}
			        
			        
			      //设置商品管理数字
			        if(!mainInfo.getData().getDsj().equals("0")){
			        	BadgeView manageBadge = new BadgeView(MineWorkspaceActivity.this, manageTV);  
				        manageBadge.setText(mainInfo.getData().getDsj());  
				        manageBadge.setTextSize(12);
				        if(isMax(mainInfo.getData().getDsj())){
				        	manageBadge.setPadding(7, 3, 7, 3);
						}else{
							manageBadge.setPadding(15, 3, 15, 3);
						}
				        manageBadge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
				        manageBadge.show();
			        }
			        
			        
			      //设置补货商品数字
			        if(!mainInfo.getData().getDsj().equals("0")){
				        BadgeView bhBadge = new BadgeView(MineWorkspaceActivity.this, bhTV);  
				        bhBadge.setText(mainInfo.getData().getDbh());  
				        bhBadge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
				        bhBadge.setTextSize(12);
				        if(isMax(mainInfo.getData().getDbh())){
				        	bhBadge.setPadding(7, 3, 7, 3);
						}else{
							bhBadge.setPadding(15, 3, 15, 3);
						}
				        bhBadge.show();
			        }
			        
			        //设置店铺公告显示
			        letters = mainInfo.getData().getLetter();
			        if(letters!=null&&letters.size()>0){
				        index=0;
				        noticeSwitcher.setText(letters.get(index).getTitle());
				        // 设置切入动画  
						noticeSwitcher.setInAnimation(AnimationUtils.loadAnimation(MineWorkspaceActivity.this,R.anim.slide_in_bottom));
				        // 设置切出动画  
						noticeSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MineWorkspaceActivity.this, R.anim.slide_out_up));
						
				        noticeHandler.sendEmptyMessageDelayed(0, 3000);
			        }
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public Boolean isMax(String originStr){
		int num = Integer.parseInt(originStr);
		if(num>10)
			return true;
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method
		requestInfo();
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_workspace, menu);
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
