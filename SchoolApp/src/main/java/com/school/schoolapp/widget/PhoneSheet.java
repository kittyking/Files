package com.school.schoolapp.widget;

import com.school.schoolapp.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PhoneSheet {
	private Context mContext;
	
	private Dialog dialog;
	
	private Display display;
	
	private Button phoneBtn,telBtn,cancelBtn;

	public PhoneSheet(Context contenx){
		this.mContext = contenx;
		
		// 取得window对象
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		//得到窗口的尺寸对象
		display = windowManager.getDefaultDisplay();
	}
	
	public PhoneSheet builder(){
		// 获取Dialog布局
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.view_phone_actionsheet, null);
		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(display.getWidth());
		
		// 获取view中所有控件
		phoneBtn = (Button)view.findViewById(R.id.phone);
		telBtn = (Button)view.findViewById(R.id.tel);
		cancelBtn = (Button)view.findViewById(R.id.cancel);
		
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
	public PhoneSheet setPhoneBtn(String phone,OnClickListener listener){

		phoneBtn.setVisibility(View.VISIBLE);
		phoneBtn.setText(phone);
		phoneBtn.setOnClickListener(listener);
		return this;
	}
	public PhoneSheet setTelBtn(String tel,OnClickListener listener){
		telBtn.setVisibility(View.VISIBLE);
		telBtn.setText(tel);
		telBtn.setOnClickListener(listener);
		return this;
	}
	
	// 设置dialog是否能够取消
	public PhoneSheet setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}
	// 设置dialog在屏幕外部是否能够取消
	public PhoneSheet setCanceledOnTouchOutside(boolean cancel) {
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
