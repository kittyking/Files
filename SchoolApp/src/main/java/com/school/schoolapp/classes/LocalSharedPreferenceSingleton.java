package com.school.schoolapp.classes;

import com.google.gson.Gson;
import com.school.schoolapp.activity.login.UserRegisterActivity;
import com.school.schoolapp.activity.login.UserSchoolActivity;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.entitys.UserVO;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalSharedPreferenceSingleton {

	public static LocalSharedPreferenceSingleton localSharedPreferenceSingleton = null;
	
	private static Context context;
	
	public static LocalSharedPreferenceSingleton getInstance(){
		if(localSharedPreferenceSingleton==null){
			localSharedPreferenceSingleton = new LocalSharedPreferenceSingleton();
		}
		
		return localSharedPreferenceSingleton;
	}
	
	public void setUserInfo(Context context,String userinfo,String pwd){
		//保存返回的用户信息和密码
	 	SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
	 	Editor editor = sp.edit();
        editor.putString("USER_KEY",userinfo);
        editor.putString("PASSWORD_KEY",pwd);
        editor.commit();
	}
	
	public UserCallback getUserInfo(Context context){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
		String userStr = sp.getString("USER_KEY", null);
		UserCallback user = new Gson().fromJson(userStr, UserCallback.class);
		return user;
	}
	public String getUserPwd(Context context){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
		String pwd = sp.getString("PASSWORD_KEY", "NULL");
		return pwd;
	}
	
	public void clearUserInfo(Context context){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove("USER_KEY");
		editor.remove("PASSWORD_KEY");
		editor.commit();
//		sp.edit().clear().commit();
//		Editor editor = sp.edit();
//      editor.putString("GUIDE","N");
	}
	
	public Boolean isShowGuide(Context context){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
		String isGuide = sp.getString("GUIDE", "Y");
		if(isGuide.equals("Y"))
			return true;
		else {
			return false;
		}
	}
	public void setShowedGuide(Context context){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
	 	Editor editor = sp.edit();
        editor.putString("GUIDE","N");
        editor.commit();
	}
	
	public void setUserCity(Context context,String cityid,String cityname){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
	 	Editor editor = sp.edit();
        editor.putString("CITY_ID",cityid);
        editor.putString("CITY_NAME",cityname);
        editor.commit();
	}
	public String getUserCityId(){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
		String cityid = sp.getString("CITY_ID", "NULL");
		return cityid;
	}
	
	public String getUserCityId(Context context){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
		String cityid = sp.getString("CITY_ID", "NULL");
		return cityid;
	}
	
	public String getUserCityName(Context context){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
		String cityid = sp.getString("CITY_NAME", "NULL");
		return cityid;
	}
	
	public void setUsername(Context context,String username){
		SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
		String defaultname = sp.getString("USERNAME", "NULL");
		if(!defaultname.equals(username) ){
			Editor editor = sp.edit();
	        editor.putString("USERNAME",username);
			editor.remove("USERLIST");
	        editor.commit();
		}
	}
	
}
