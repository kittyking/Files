package com.school.schoolapp.adapter.chat;

import java.util.ArrayList;
import java.util.List;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.school.schoolapp.R;
import com.school.schoolapp.classes.ChatEmojiClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SystemMessageAdapter extends BaseAdapter {
	
	private Context context;
	private List<EMMessage> messages;
	private String username;
	private List<ImageView> circles;
	public SystemMessageAdapter(Context context,String username){
		this.context = context;
		this.username = username;
		messages = new ArrayList<EMMessage>();
		circles= new ArrayList<>();
	}
	public void addMessages(List<EMMessage> messages){
		for(EMMessage message:messages)
			this.messages.add(message);
	}
	public void addMessage(EMMessage message){
		messages.add(message);
	}
	public EMMessage getMessage(int position){
		return messages.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messages.size();
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
		circles.clear();
		super.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.adapter_system_message, null);
		
		EMMessage message = messages.get(position);
	
		TextView tv = (TextView)convertView.findViewById(R.id.message);
		EMTextMessageBody body = (EMTextMessageBody) message.getBody();
		String html = body.getMessage();
		tv.setText(html);
		
		try {
			String time = message.getStringAttribute("time");
			TextView timeTxt = (TextView)convertView.findViewById(R.id.time);
			timeTxt.setText(time);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
				
		return convertView;
	}

}
