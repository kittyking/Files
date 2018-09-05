package com.school.schoolapp.adapter.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.classes.ChatEmojiClass;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.voicerecode.UPlayer;
import com.school.schoolapp.classes.voicerecode.URecorder;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChatDetailAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	private List<EMMessage> messages;
	
	private String username;
	
	public ChatDetailAdapter(Context context,String username){
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.username = username;
		
		messages = new ArrayList<EMMessage>();
	}
	public void addMessage(EMMessage message){
		messages.add(message);
	}
	
	public void addMessages(List<EMMessage> messages){
		
		for(EMMessage message:messages)
			this.messages.add(message);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String avatar="";
		
		final EMMessage message = messages.get(position);
		try {
			avatar = message.getStringAttribute("avatar");
		} catch (HyphenateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(message.getType().name().equals("TXT")){//文字消息
			if(message.getFrom().equals(username))
				convertView = mInflater.inflate(R.layout.adapter_chat_info_send_cell, null);
			else if(message.getTo().equals(username))
				convertView = mInflater.inflate(R.layout.adapter_chat_info_receive_cell, null);
			
			TextView tv = (TextView)convertView.findViewById(R.id.chat);
			EMTextMessageBody body = (EMTextMessageBody) message.getBody();
			String html = body.getMessage();
			if(html.contains("<"))
				tv.setText(ChatEmojiClass.getInstance().getTxtByHtmlStr(html, mContext));
			else
				tv.setText(html);
		}else if(message.getType().name().equals("IMAGE")){//图片消息
			EMImageMessageBody body = (EMImageMessageBody) message.getBody();
			
			if(message.getFrom().equals(username)){
				convertView = mInflater.inflate(R.layout.adapter_chat_image_send, null);
				String imgUrl = body.getLocalUrl();
				//String imgUr =body.getRemoteUrl();
				//显示商品图片
				ImageView iv = (ImageView)convertView.findViewById(R.id.image);
				ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
				if(!ImageLoader.getInstance().isInited())
			    	ImageLoader.getInstance().init(configuration);
				//显示图片的配置  
		        DisplayImageOptions options = new DisplayImageOptions.Builder()  
		                .cacheInMemory(true)  
		                .cacheOnDisk(true)  
		                .bitmapConfig(Bitmap.Config.RGB_565)  
		                .build();  
		        ImageLoader.getInstance().displayImage("file://"+imgUrl, iv,options);
			}
			else if(message.getTo().equals(username)){
				convertView = mInflater.inflate(R.layout.adapter_chat_image_receive, null);
				String thumbUrl = body.getThumbnailUrl();
				//显示商品图片
				ImageView iv = (ImageView)convertView.findViewById(R.id.image);
				ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
				if(!ImageLoader.getInstance().isInited())
			    	ImageLoader.getInstance().init(configuration);
				//显示图片的配置  
		        DisplayImageOptions options = new DisplayImageOptions.Builder()  
		                .cacheInMemory(true)  
		                .cacheOnDisk(true)  
		                .bitmapConfig(Bitmap.Config.RGB_565)  
		                .build();  
		        ImageLoader.getInstance().displayImage(thumbUrl, iv,options);
			}
		}else if(message.getType().name().equals("VOICE")){
			if(message.getFrom().equals(username)){
				convertView = mInflater.inflate(R.layout.adapter_chat_voice_send_cell, null);
				
				Button playBtn = (Button)convertView.findViewById(R.id.chat);
				playBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						EMVoiceMessageBody voice = (EMVoiceMessageBody)message.getBody();
						UPlayer player = new UPlayer(voice.getLocalUrl());
						Log.i("", "school:开始播放音频"+voice.getLocalUrl());
						player.start();
					}
				});
			}
			else if(message.getTo().equals(username)){
				convertView = mInflater.inflate(R.layout.adapter_chat_voice_receive_cell, null);
				
				Button playBtn = (Button)convertView.findViewById(R.id.chat);
				playBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						EMVoiceMessageBody voice = (EMVoiceMessageBody)message.getBody();
						UPlayer player = new UPlayer(voice.getLocalUrl());
						player.start();
					}
				});
			}
			
		}

		showIcon(avatar, (ImageView)convertView.findViewById(R.id.userIcon));
		return convertView;
	}
	private void showIcon(String url,ImageView iv){
		if(url.equals(""))
			return;
		ImageLoaderTool.getInstance().displayImage(url, iv, mContext);
	}

}
