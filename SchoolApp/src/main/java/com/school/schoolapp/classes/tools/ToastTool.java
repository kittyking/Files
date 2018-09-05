package com.school.schoolapp.classes.tools;

import android.content.Context;
import android.widget.Toast;

public class ToastTool {
	private static String messages="";
	
	public static void showWithMessage(String message,Context mContext){

		if(message.equals(messages))
			return;
		
		makeToast(message, mContext);
	}
	public static void showNetworkError(Context mContext){
		//makeToast("网络连接错误", mContext);
	}
	private static void makeToast(String message,Context mContext){
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();;
		messages = message;
	}
	public static void showMessage(String message,Context mContext){
		makeToast(message, mContext);
	}
}
