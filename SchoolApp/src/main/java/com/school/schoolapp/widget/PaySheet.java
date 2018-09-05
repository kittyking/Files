package com.school.schoolapp.widget;


import com.school.schoolapp.R;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class PaySheet {
	
	private Context mContext;
	
	private Dialog dialog;
	
	private Display display;
	
	private TextView infoTV,userTV,moneyTV;
	
	private Button payBtn;
	
	private ImageButton cancelBtn;
	
	private RadioButton zfbRB,wxRB,htxRB;
	
	private int currentRB = 0;
	public PaySheet(Context contenx){
		this.mContext = contenx;
		
		// 取得window对象
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		//得到窗口的尺寸对象
		display = windowManager.getDefaultDisplay();
	}
	
	public PaySheet builder(){
		// 获取Dialog布局
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.view_pay_actionsheet, null);
		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(display.getWidth());
		
		// 获取view中所有控件
		infoTV = (TextView)view.findViewById(R.id.infoTV);
		userTV = (TextView)view.findViewById(R.id.userTV);
		moneyTV = (TextView)view.findViewById(R.id.moneyTV);
		zfbRB = (RadioButton)view.findViewById(R.id.zfbRB);
		zfbRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(currentRB!=0 && isChecked){
					wxRB.setChecked(false);
					htxRB.setChecked(false);
					currentRB =0;
				}
			}
		});
		wxRB = (RadioButton)view.findViewById(R.id.wxRB);
		wxRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(currentRB!=1 && isChecked){
					zfbRB.setChecked(false);
					htxRB.setChecked(false);
					currentRB =1;
				}
			}
		});
		htxRB = (RadioButton)view.findViewById(R.id.htxRB);
		htxRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(currentRB!=2 && isChecked){
					zfbRB.setChecked(false);
					wxRB.setChecked(false);
					currentRB =2;
				}
			}
		});
		payBtn = (Button)view.findViewById(R.id.payBtn);
		cancelBtn = (ImageButton)view.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		// 定义Dialog布局和参数
		dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width =  display.getWidth();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);
		return this;
	}
	public PaySheet setInfo(String info){
		infoTV.setText(info);
		return this;
	}
	public PaySheet setUser(String user){
		userTV.setText(user);
		return this;
	}
	public PaySheet setMoney(String money){
		moneyTV.setText(money+"元");
		return this;
	}
	// 设置dialog是否能够取消
	public PaySheet setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}
	public PaySheet setPayButton(OnClickListener listener){
		payBtn.setOnClickListener(listener);
		return this;
	}

	public int getCurrentRB(){
		return currentRB;
	}
	// 设置dialog在屏幕外部是否能够取消
	public PaySheet setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}
	
	public void show() {
		dialog.show();
	}
	public void dismiss(){
		dialog.dismiss();
	}

}
