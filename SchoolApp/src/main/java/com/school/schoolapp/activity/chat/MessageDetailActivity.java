package com.school.schoolapp.activity.chat;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.activity.chat.ChatDetailActivity.ExpressionPagerAdapter;
import com.school.schoolapp.adapter.chat.ChatDetailAdapter;
import com.school.schoolapp.classes.ChatEmojiClass;
import com.school.schoolapp.classes.voicerecode.URecorder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class MessageDetailActivity extends BaseActivity {

	private String username;//聊天对象的用户名

	private int isEmoji =0;//标识是否添加表情 0不是 1是
	private String inputStr="";//用户输入的文字，没次输入都进行记录
	private String htmlStr="";//封装成HTML以后的字符串
	private ListView chatListView;
	private ChatDetailAdapter adapter;
	
	private List<EMMessage> messages;
	
	private EditText chatET;
	private RelativeLayout rootViewRL,pictureRL,expressionRL;
	private ImageButton typeBtn,expressionBtn,addBtn,pictureBtn,cameraBtn;
	private Button soundBtn;
	private ViewPager expressVP;
	
	private int type = 0;//0代表文字 1代表语音
	private int isExpressionShow =0;//0代表未显示 
	private int showType = 0;//0代表表情 1图片
	
	
	private List<GridLayout> grids = new ArrayList<GridLayout>();
	
	private int screenHeight;
	private int screenWidth;
	
	private String mPhotoPath,voicePath;//拍照选取照片和录音生成的文件路径
	
	private URecorder recorder;
	
	private Handler dialogHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
//  *****************只有把添加消息的操作放在handler里 刷新才能显示*****************
			adapter.addMessage(messages.get(0));
			adapter.notifyDataSetChanged();
			chatListView.setSelection(adapter.getCount()-1); 
		};
	};
	
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_detail);
		
		Intent intent = getIntent();
		username = intent.getStringExtra("Username");
		this.titleTextView.setText(username);
		getScreenHeighAndWidth();
		
		EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
		
		//获取此会话20条的所有消息
		List<EMMessage> messages = conversation.loadMoreMsgFromDB(conversation.getLastMessage().getMsgId(), 20);
		
		chatListView = (ListView)findViewById(R.id.chatInfo);
		adapter = new ChatDetailAdapter(this, "13888888888");
		adapter.addMessages(messages);
		adapter.addMessage(conversation.getLastMessage());
		chatListView.setAdapter(adapter);
		chatListView.setSelection(adapter.getCount()-1); 
		chatListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				hideKeyboard();
			}
		});
		EMClient.getInstance().chatManager().addMessageListener(messageListener);
		
		chatET = (EditText)findViewById(R.id.chatET);
		chatET.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				hideExpression();
			}
		});
		//添加输入框值改变监听事件
		chatET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("", "htmlStr="+htmlStr);
				//点击删除按钮
				if(count==0){
					if(inputStr.equals("")){
						//如果htmlStr没有内容 不做任何操作
						if(htmlStr.length()==0)
							return;
						int position=0;
						if(htmlStr.substring(htmlStr.length()-1).equals(">")){//删除htmlStr中的表情
							//得到<所在的位置
							for(int i=htmlStr.length()-1;i>=0;i--){
								Log.i("", "substring="+htmlStr.substring(i-1,i));
								if(htmlStr.substring(i-1,i).equals("<")){
									position=i-1;
									i=-1;
								}
							}
							
							htmlStr = htmlStr.substring(0, position);
							if(position==0)
								htmlStr="";
							Log.i("", "删除表情后的htmlStr="+htmlStr);
						}else{//删除htmlStr中的文字
							
							if(htmlStr.length()==1){
								//所有文字都删除掉了 赋空并退出
								htmlStr.equals("");
								return;
							}
							htmlStr = htmlStr.substring(0, htmlStr.length()-1);
						}
							
					}else{
						//删除input中一个文字
						if(inputStr.length()>1)
							inputStr = inputStr.substring(0, inputStr.length()-1);
						else
							inputStr = "";
					}
					
					Log.i("","start="+start+"before"+before+"count"+count);
					return;
					
					
				}
				
				//判断如果是添加表情 不做任何操作
				if(isEmoji==1){
					isEmoji=0;
					return;
				}
				//获得用户输入的字符
				CharSequence inputChar = s.subSequence(start, start+count);
				inputStr += inputChar;
				
				Log.i("","start="+start+"before"+before+"count"+count);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		//用户点击“发送” 处理并发送文字信息
		chatET.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_SEND){
					//判断edittext是否为空
					if(chatET.getText().toString().length()<=0)
						return false;
					
					//对文字进行封装
					htmlStr += inputStr;
					inputStr="";
					//发送文字消息
					EMMessage message = EMMessage.createTxtSendMessage(htmlStr, username);
					EMClient.getInstance().chatManager().sendMessage(message);
					adapter.addMessage(message);
					adapter.notifyDataSetChanged();
					chatListView.setSelection(adapter.getCount()-1); 
					chatET.setText("");
					htmlStr ="";
					
				}
				return false;
			}
		});
		
		soundBtn = (Button)findViewById(R.id.soundBtn);
		soundBtn.setOnLongClickListener(new OnLongClickListener() {//录音
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				String sd = Environment.getExternalStorageDirectory().getPath()+ "/DCIM/MobileOffice";
		    	SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
			    String filename = "MT" + (t.format(new Date(System.currentTimeMillis()))) + ".pcm";
		    	voicePath = new String(sd+"/"+filename);
		    	File file = new File(sd);
		    	if(!file.exists())
		    		file.mkdirs();
		    	//录音
		    	recorder = new URecorder(voicePath);
		    	recorder.start();
		    	Log.i("", "长按开始啦"+voicePath);
				
				return true;
			}
			
		});
		soundBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_UP){
					Log.i("", "长按取消啦"+voicePath);
					//停止录音
					recorder.stop();
					//发送录音
					EMMessage message = EMMessage.createVoiceSendMessage(voicePath, 10, username);
					EMClient.getInstance().chatManager().sendMessage(message);
					adapter.addMessage(message);
					adapter.notifyDataSetChanged();
					chatListView.setSelection(adapter.getCount()-1); 
				}
				//返回false就不会覆盖setOnLongClickListener
				return false;
			}

		});
		//初始化表情容器
		expressVP = (ViewPager)findViewById(R.id.expressionVP);
		initEmoji();
		
		
		pictureRL = (RelativeLayout)findViewById(R.id.pictureRL);
		expressionRL = (RelativeLayout)findViewById(R.id.expressionRL);
		addBtn = (ImageButton)findViewById(R.id.add);
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeToPicture();
			}
		});
		
		expressionBtn = (ImageButton)findViewById(R.id.express);
		expressionBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeToExpression();
			}
		});
		
		typeBtn = (ImageButton)findViewById(R.id.inputType);
		typeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isExpressionShow ==1)
					hideExpression();
				if(type==0){
					changeToSound();
				}else{
					changeToText();
				}
			}
		});
		
		rootViewRL = (RelativeLayout)findViewById(R.id.rootView);
		rootViewRL.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				Log.i("", "探测到触摸时间");
				rootViewRL.setFocusable(true);
				rootViewRL.setFocusableInTouchMode(true);
				rootViewRL.requestFocus();

				return false;
			}
		});
		
		pictureBtn =(ImageButton)findViewById(R.id.picture);
		pictureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 1001);
			}
		});
		
		cameraBtn = (ImageButton)findViewById(R.id.camera);
		cameraBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String state = Environment.getExternalStorageState();  
			       if (state.equals(Environment.MEDIA_MOUNTED)) {  
			    	   String sd = Environment.getExternalStorageDirectory().getPath()+ "/DCIM/MobileOffice";
			    	   SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
				       String filename = "MT" + (t.format(new Date(System.currentTimeMillis()))) + ".jpg";
			    	   mPhotoPath = new String(sd+"/"+filename);
			    	   File file = new File(sd);
			    	   if(!file.exists())
			    		   file.mkdirs();
			    	   
			    	   File picture = new File(sd, filename);
			    	   Uri imageUri = Uri.fromFile(picture);
			           Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			           intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			           startActivityForResult(intent, 1002);  
			       }  
			       else {  
			           Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();  
			       }  
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK){
			Log.i("获取失败", "获取失败");
			return;
		}
		
		if(requestCode ==1001){//从相册得到图片
			Uri uri = data.getData();
			if(uri == null)
				return;
			
			//获得本地图片路径
			String[] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(uri, proj, null, null, null); 
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index);
			Log.i("", "imagepath:"+path);
			//发送图片
			EMMessage message = EMMessage.createImageSendMessage(path, false, username);
			EMClient.getInstance().chatManager().sendMessage(message);
			adapter.addMessage(message);
			adapter.notifyDataSetChanged();
			chatListView.setSelection(adapter.getCount()-1);
		}else if(requestCode ==1002){//拍照获取图片路径
			//发送图片
			EMMessage message = EMMessage.createImageSendMessage(mPhotoPath, false, username);
			EMClient.getInstance().chatManager().sendMessage(message);
			adapter.addMessage(message);
			adapter.notifyDataSetChanged();
			chatListView.setSelection(adapter.getCount()-1);
		}
	}
	public void getScreenHeighAndWidth(){
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
	}
	
	public void changeToSound(){
		typeBtn.setImageDrawable(getResources().getDrawable(R.drawable.icon_chat_txt));
		chatET.clearFocus();
		hideKeyboard();
		soundBtn.setVisibility(View.VISIBLE);
		type=1;
	}
	public void changeToText(){
		typeBtn.setImageDrawable(getResources().getDrawable(R.drawable.icon_chat_sound));
		soundBtn.setVisibility(View.GONE);
		type=0;
	}
	public void hideKeyboard(){
		InputMethodManager imm = 
				(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(chatET.getWindowToken(),0);
	}
	public void hideExpression(){
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)expressionRL.getLayoutParams();
		params.height=0;
		expressionRL.setLayoutParams(params);
		isExpressionShow=0;
	}

	public void changeToPicture(){

		chatET.clearFocus();
		if(type ==0)
			hideKeyboard();
		
		if(isExpressionShow ==0){
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)expressionRL.getLayoutParams();
			params.height=screenWidth/7*3;
			expressionRL.setLayoutParams(params);
			
			isExpressionShow =1;
		}


		expressVP.setVisibility(View.GONE);
		pictureRL.setVisibility(View.VISIBLE);
		
	}
	 
	public void changeToExpression(){
		chatET.clearFocus();
		if(type ==0)
			hideKeyboard();
		else
			changeToText();
		
		
		if(isExpressionShow ==0){
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)expressionRL.getLayoutParams();
			params.height=screenWidth/7*3;
			expressionRL.setLayoutParams(params);
			
			isExpressionShow =1;
		}
		pictureRL.setVisibility(View.GONE);
		expressVP.setVisibility(View.VISIBLE);
	}
	
	public void initEmoji(){
		int index=0;
		
		for(int i=0; i<5;i++){//106个表情总共5页
			
			//初始化Grid容器
			GridLayout grid = new GridLayout(this);
			GridLayout.LayoutParams params = new GridLayout.LayoutParams();
			params.height = GridLayout.LayoutParams.MATCH_PARENT;
			params.width = GridLayout.LayoutParams.MATCH_PARENT;
			grid.setLayoutParams(params);
			grid.setRowCount(3);
			grid.setColumnCount(7);
			
			for(int row=0;row<3;row++){
				for(int col =0;col<7;col++){
					ImageButton ib = new ImageButton(this);
					GridLayout.LayoutParams ibParams = new GridLayout.LayoutParams();
					ib.setBackground(null);
					ibParams.height = screenWidth/7;
					ibParams.width = screenWidth/7;
					ib.setPadding(25, 25, 25, 25);
					ib.setLayoutParams(ibParams);
					int[] emojis = ChatEmojiClass.getInstance().getEmojiArray();
					ib.setImageDrawable(getResources().getDrawable(emojis[index]));
					final int picIndex = index;
					ib.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							//添加表情
							if(inputStr.equals(""))
								htmlStr +="<img src='"+picIndex+"'>";
							else
								htmlStr += inputStr+"<img src='"+picIndex+"'>";
								//htmlStr += "<font>"+inputStr+"</font>"+"<img src='"+picIndex+"'>";
							Log.i("", "htmlStr="+htmlStr);
							CharSequence text= ChatEmojiClass.getInstance().setTxtByHtmlStrAndPosition(htmlStr, MessageDetailActivity.this, picIndex);
							inputStr="";
							isEmoji=1;
							chatET.setText(text);
						}
					});
					index++;
					
					grid.addView(ib);
				}
			}
			grids.add(grid);
		}
		
		expressVP.setAdapter(new ExpressionPagerAdapter());
	}
	public class ExpressionPagerAdapter extends PagerAdapter{
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return grids.size();
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			//super.destroyItem(container, position, object);
			((ViewPager)container).removeView(grids.get(position));
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewPager)container).addView(grids.get(position));
			return grids.get(position);
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_detail, menu);
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
