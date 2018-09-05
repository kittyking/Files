package com.school.schoolapp.activity.chat;

import java.util.List;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.chat.SystemMessageAdapter;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SystemMessageActivity extends BaseActivity {

	private ListView chatListView;
	private SystemMessageAdapter adapter;
	private String username;
	private EMConversation conversation;
	private List<EMMessage> messages;
	private Handler dialogHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
//  *****************只有把添加消息的操作放在handler里 刷新才能显示*****************
			
			if(messages.get(0).getFrom().equals(username)){
				conversation.markMessageAsRead(messages.get(0).getMsgId());
				adapter.addMessage(messages.get(0));
				adapter.notifyDataSetChanged();
				chatListView.setSelection(adapter.getCount()-1); 
				
				Intent intent = new Intent();
				intent.setAction("Circle");
				intent.putExtra("visibility", "gone");
				sendBroadcast(intent);
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_message);
		
		Intent intent = getIntent();
		username = intent.getStringExtra("Username");
		
		chatListView = (ListView)findViewById(R.id.message);
		adapter = new SystemMessageAdapter(this,LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getUserName());
		
		if(EMClient.getInstance()!=null){
			conversation = EMClient.getInstance().chatManager().getConversation(username);
			//获取此会话20条的所有消息
			if(conversation!=null){
				List<EMMessage> messages = conversation.loadMoreMsgFromDB(conversation.getLastMessage().getMsgId(), 20);
				adapter.addMessages(messages);
				adapter.addMessage(conversation.getLastMessage());
				try {
					this.titleTextView.setText(conversation.getLastMessage().getStringAttribute("nick"));
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			chatListView.setAdapter(adapter);
			chatListView.setSelection(adapter.getCount()-1);
			//点击进入详情页
			chatListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
				}
			});

			EMClient.getInstance().chatManager().addMessageListener(messageListener);
		}
	}
	
	//监听收到新消息
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.system_message, menu);
		return false;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
		super.onDestroy();
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
