package com.school.schoolapp.adapter.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.R;
import com.school.schoolapp.activity.billing.BillingCustomActivity;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatDialogAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private LayoutInflater mInflager;
	
	private List<String> userList;
	
	private String time;
	
	public ChatDialogAdapter(Context context){
		this.mContext = context;
		this.mInflager = LayoutInflater.from(context);
		this.userList = new ArrayList<>();
	}

	//收到新消息后调用，判断有没有此用户聊天记录，没有需要添加，最后刷新adapter
	public void receiveMessage(String username){
		for(int i=0;i<userList.size();i++){
			String name =userList.get(i);
			if(name.equals(username)){
				//把新收到的放第一个
				userList.remove(i);
				userList.add(0, username);
				saveUserlist();
				this.notifyDataSetChanged();
				return;
			}
		}
		userList.add(0,username);
		saveUserlist();
		this.notifyDataSetChanged();
	}
	private void saveUserlist(){
		Gson gson = new Gson();
		String userStr = gson.toJson(userList);
		SharedPreferences sp = mContext.getSharedPreferences("sp",mContext.MODE_PRIVATE);
		Editor editor = sp.edit();
        editor.putString("USERLIST",userStr);
        editor.commit();
	}
	
	private void getUserlist(){
		SharedPreferences sp =mContext.getSharedPreferences("sp",mContext.MODE_PRIVATE);
		String userStr = sp.getString("USERLIST", "NONE");
		if(!userStr.equals("NONE")){

			Gson gson = new Gson();
			userList = gson.fromJson(userStr, List.class);
		}
		
	}
	
	public void initMessage(List<String> users){
		for(String name:users){
			userList.add(name);
		}
	}
	
	public List<String> getUsers(){
		return this.userList;
	}
	
	public void setTime(String time){
		this.time = time;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		
		getUserlist();
		super.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView ==null)
			convertView = mInflager.inflate(R.layout.adapter_chat_dialog_cell, null);
		TextView messageTV = (TextView)convertView.findViewById(R.id.message);
		String username = userList.get(position);
		
		//获取所有会话
		Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
		//获取某用户会话
		EMConversation conversation = conversations.get(username);
		if(conversation!=null){
			//获取未读消息数量
			int remian = conversation.getUnreadMsgCount();
			String s = ""+remian;
			Log.i("", "未读数量"+s);
			if(remian>0){//有未读消息
				
				//显示列表小红点
				((ImageView)convertView.findViewById(R.id.circle)).setVisibility(View.VISIBLE);
				//显示消息模块下的小红点
				Intent intent = new Intent();
				intent.setAction("Circle");
				intent.putExtra("visibility", "visible");
				mContext.sendBroadcast(intent);
			}else{
				//显示列表小红点
				((ImageView)convertView.findViewById(R.id.circle)).setVisibility(View.GONE);
			}
			
			
			EMMessage message = conversation.getLastMessage();
			try {
				String nick = message.getStringAttribute("nick");
				String avatar = message.getStringAttribute("avatar");
				String msgtime = message.getStringAttribute("time");
				ImageView usericon = (ImageView)convertView.findViewById(R.id.userIcon);
				ImageLoaderTool.getInstance().displayImage(avatar, usericon, mContext);
				TextView name = (TextView)convertView.findViewById(R.id.name);
				name.setText(nick);
				TextView time = (TextView)convertView.findViewById(R.id.time);
				time.setText(msgtime);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(message.getType().toString().equals("TXT")){
				EMTextMessageBody body = (EMTextMessageBody) conversation.getLastMessage().getBody();
				messageTV.setText(body.getMessage());
			}else if(message.getType().toString().equals("IMAGE")){
				messageTV.setText("[图片]");
			}
		}
		
		if(position == userList.size()-1){
			LinearLayout line = (LinearLayout)convertView.findViewById(R.id.line);
			line.setVisibility(View.INVISIBLE);
		}
		
		
		
		return convertView;
	}

}
