package com.school.schoolapp.activity.chat;

import java.text.SimpleDateFormat;
import java.util.List;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.chat.ChatDialogAdapter;
import com.school.schoolapp.classes.ChatMessageClass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChatDialogListActivity extends BaseActivity {
	
	private ListView dialogListView;
	
	private ChatDialogAdapter adapter;
	
//	用于装载收到的新消息
	private List<EMMessage> messages;
	
//	用于收到新消息后更新主界面。 由于获取消息在子线程，必须使用handler
	private Handler dialogHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss"); 
			String date = sDateFormat.format(new java.util.Date());
			adapter.setTime(date);
			
//  *****************只有把添加消息的操作放在handler里 刷新才能显示*****************
			adapter.receiveMessage(messages.get(0).getUserName());
			adapter.notifyDataSetChanged();
		};
	};
	
	private EMMessageListener messageListener = new EMMessageListener() {
		
		@Override
		public void onMessageReceived(List<EMMessage> arg0) {
			// TODO Auto-generated method stub
			messages = arg0;
			dialogHandler.sendEmptyMessageDelayed(0, 0);
		}
		
		@Override
		public void onMessageReadAckReceived(List<EMMessage> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onMessageDeliveryAckReceived(List<EMMessage> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onMessageChanged(EMMessage arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onCmdMessageReceived(List<EMMessage> arg0) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_dialog_list);
		
		//this.titleTextView.setText("聊天");
		dialogListView = (ListView)findViewById(R.id.dialogList);
		adapter = new ChatDialogAdapter(this);
		SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
		String userStr = sp.getString("USERLIST", "NONE");
		if(!userStr.equals("NONE")){
			Gson gson = new Gson();
			List<String> users = gson.fromJson(userStr, List.class);
			adapter.initMessage(users);
		}
		dialogListView.setAdapter(adapter);
		dialogListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChatDialogListActivity.this,ChatDetailActivity.class);
				if(adapter.getUsers().size()>0){
					intent.putExtra("Username", adapter.getUsers().get(position));
					
					//阅读信息 刷新列表 去掉小红点
					EMConversation conversation = EMClient.getInstance().chatManager().getConversation(adapter.getUsers().get(position));
					//指定会话消息未读数清零
					conversation.markAllMessagesAsRead();

					adapter.notifyDataSetChanged();
					startActivity(intent);
				}
				
				
			}
		});
		
		EMClient.getInstance().chatManager().addMessageListener(messageListener);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_dialog_list, menu);
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
