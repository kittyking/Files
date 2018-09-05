package com.school.schoolapp.activity.technical;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.technical.SkillContractAdapter;
import com.school.schoolapp.adapter.technical.SkillDetailAdapter;
import com.school.schoolapp.callback.technical.SkillContractCallback;
import com.school.schoolapp.callback.technical.SkillDetailCallback;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TechnicalDetailActivity extends BaseActivity {

	private View headerV;
	private ListView mListview;
	private String sid;
	private SkillContractAdapter contractAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_technical_detail);
		this.titleTextView.setText("服务详情");
		Intent intent = getIntent();
		sid = intent.getStringExtra("sid");
		headerV = View.inflate(this, R.layout.view_skill_detail_header, null);
		requestDetail();
		
		mListview = (ListView)findViewById(R.id.infoLV);
		mListview.addHeaderView(headerV,null,true);
		mListview.setAdapter(new SkillDetailAdapter());
	}
	
    public void collectSkill(View v){
    	String url = getString(R.string.base_url) + getString(R.string.skill_collect);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("sid", "");
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1"))
					ToastTool.showWithMessage("收藏成功", TechnicalDetailActivity.this);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(TechnicalDetailActivity.this);
			}
		});
    }
    
    public void buySkill(View v){
    	startActivity(new Intent(this,SkillBuyActivity.class));
    }
    
    private void requestDetail(){
    	String url = getString(R.string.base_url) + getString(R.string.skill_detail);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("sid", sid);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				SkillDetailCallback detail = new Gson().fromJson(new String(arg2), SkillDetailCallback.class);
				if(detail.getResult().equals("1")){
					//昵称
					TextView nick = (TextView)headerV.findViewById(R.id.username);
					nick.setText(detail.getData().getNick());
					//评分
					TextView userevelution = (TextView)headerV.findViewById(R.id.userevelution);
					userevelution.setText(detail.getData().getScore());
					//标题
					TextView tectitle = (TextView)headerV.findViewById(R.id.tectitle);
					tectitle.setText(detail.getData().getTitle());
					//描述
					TextView tecdecribe = (TextView)headerV.findViewById(R.id.tecdecribe);
					tecdecribe.setText(detail.getData().getSummary());
					//单价
					TextView tecmoney = (TextView)headerV.findViewById(R.id.tecmoney);
					tecmoney.setText(detail.getData().getMoney()+"/"+detail.getData().getUnit());
					//头像
					ImageLoader.getInstance().displayImage(detail.getData().getAvatar(), (ImageView)headerV.findViewById(R.id.usericon));
					
					if(!detail.getData().getOrder().equals("0")){
						TextView qy = (TextView)headerV.findViewById(R.id.qiyue);
						qy.setText("契约("+detail.getData().getOrder()+"条)");
						//获取契约条目
						requestContract();
					}
					if(!detail.getData().getMsg().equals("0")){
						TextView pl = (TextView)headerV.findViewById(R.id.pinglun);
						pl.setText("评论("+detail.getData().getOrder()+"条)");
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(TechnicalDetailActivity.this);
			}
		});
    }

    private int contractPage=1;
    private void requestContract(){//获取合同
    	String url = getString(R.string.base_url) + getString(R.string.skill_detail);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		params.put("sid", sid);
		params.put("curpage", contractPage);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				SkillContractCallback contract = new Gson().fromJson(new String(arg2), SkillContractCallback.class);
				if(contract.getResult().equals("1")){
					contractAdapter = new SkillContractAdapter(TechnicalDetailActivity.this);
					mListview.setAdapter(contractAdapter);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(TechnicalDetailActivity.this);
			}
		});
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.technical_detail, menu);
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
