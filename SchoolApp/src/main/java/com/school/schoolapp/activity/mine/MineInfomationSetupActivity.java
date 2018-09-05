package com.school.schoolapp.activity.mine;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.activity.login.UserCityActivity;
import com.school.schoolapp.activity.mine.work.MineCardActivity;
import com.school.schoolapp.callback.mine.UserInfoCallback;
import com.school.schoolapp.classes.ImageUploadCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.PictureCompress;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.user.UserInfoVO;
import com.school.schoolapp.fragment.alert.DatePickerFragment;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MineInfomationSetupActivity extends BaseActivity{

	private String cityid,avatar;
	private ImageView icon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_infomation_setup);
		this.titleTextView.setText("个人资料");
		this.doBtn.setText("保存");
		this.doBtn.setAlpha(1);
		icon = (ImageView)findViewById(R.id.icon);
		requestUserinfo();
		
		this.doBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setupUserinfo();
			}
		});
	}
	
	private void setupUserinfo(){
		String url = getString(R.string.base_url)+getString(R.string.user_set_info);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		TextView birthday = (TextView)findViewById(R.id.birth);
		params.put("birthday",birthday.getText());
		RadioGroup rg = (RadioGroup)findViewById(R.id.sex);
		String sex ="";
		if(rg.getCheckedRadioButtonId()==R.id.male){
			sex = "男";
		}else if(rg.getCheckedRadioButtonId()==R.id.female){
			sex ="女";
		}
		params.put("sex", sex);
		TextView nickname = (TextView)findViewById(R.id.nick);
		params.put("nick",nickname.getText());
		params.put("city",cityid);
		params.put("avatar",avatar);
		//params.put("avatar",avatar);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				UserInfoCallback userinfo = new Gson().fromJson(new String(arg2), UserInfoCallback.class);
				
				if(userinfo.getResult().equals("1")){
					ToastTool.showWithMessage("设置成功", MineInfomationSetupActivity.this);
					finish();
				}else{
					ToastTool.showWithMessage(userinfo.getMsg(), MineInfomationSetupActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(MineInfomationSetupActivity.this);
			}
		});
		
		
	}

	
	public void birthdayPick(View v){
		DatePickerFragment fragment = DatePickerFragment.newInstance((TextView)findViewById(R.id.birth));
		fragment.show(getFragmentManager(), "选择日期");
	}
	public void cityChoose(View v){
		Intent intent = new Intent(this,UserCityActivity.class);
		intent.putExtra("finish", "finish");
		startActivityForResult(intent, 1001);
	}
	
	public void requestUserinfo(){
		String url = getString(R.string.base_url)+getString(R.string.user_get_info);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				UserInfoCallback userinfo = new Gson().fromJson(new String(arg2), UserInfoCallback.class);
				
				if(userinfo.getResult().equals("1")){

					((TextView)findViewById(R.id.school)).setText(LocalSharedPreferenceSingleton.getInstance().getUserInfo(MineInfomationSetupActivity.this).getData().getSchoolName());
					((TextView)findViewById(R.id.nick)).setText(userinfo.getData().getNick());
					((TextView)findViewById(R.id.birth)).setText(userinfo.getData().getBirthday());
					if(userinfo.getData().getSex().equals("男")){
						((RadioButton)findViewById(R.id.male)).setChecked(true);
					}else if(userinfo.getData().getSex().equals("女")){
						((RadioButton)findViewById(R.id.female)).setChecked(true);
					}
					((TextView)findViewById(R.id.city)).setText(userinfo.getData().getCityname());
					cityid = userinfo.getData().getCity();
					avatar = userinfo.getData().getAvatar();
					if(avatar!=null)
						ImageLoaderTool.getInstance().displayImage(getString(R.string.base_url)+avatar, (ImageView)findViewById(R.id.icon), MineInfomationSetupActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(MineInfomationSetupActivity.this);
			}
		});
	}
	
	public void setupIcon(View v){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 1002);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==1001&&resultCode==RESULT_OK){
			cityid = data.getStringExtra("cityid");
			((TextView)findViewById(R.id.city)).setText(data.getStringExtra("cityname"));
		}else if(requestCode == 1002){
			if(data == null)
				return;
			Uri uri =data.getData();
			if(uri ==null)
				return;
			
			ImageLoader.getInstance().displayImage("file://"+ getRealFilePath(MineInfomationSetupActivity.this,uri), icon);
			ContentResolver resolver = getContentResolver();
			try {
				Bitmap bitm = MediaStore.Images.Media.getBitmap(resolver, uri);
				String base64 = PictureCompress.getInstance().Bitmap2Base64(bitm);
				uploadImage(base64);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static String getRealFilePath( final Context context, final Uri uri ) {  
	    if ( null == uri ) return null;  
	    final String scheme = uri.getScheme();  
	    String data = null;  
	    if ( scheme == null )  
	        data = uri.getPath();  
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {  
	        data = uri.getPath();  
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {  
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );  
	        if ( null != cursor ) {  
	            if ( cursor.moveToFirst() ) {  
	                int index = cursor.getColumnIndex( ImageColumns.DATA );  
	                if ( index > -1 ) {  
	                    data = cursor.getString( index );  
	                }  
	            }  
	            cursor.close();  
	        }  
	    }  
	    return data;  
	}  
	

	private void uploadImage(String base64){
		String url = getString(R.string.base_url) + getString(R.string.image_upload_base64);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("filedata", base64);
		params.put("ticket",this.ticket);
		
		final ZProgressHUD progressHUD = ZProgressHUD.getInstance(this); 
		progressHUD.setMessage("正在上传");
		progressHUD.show();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				ToastTool.showWithMessage( "照片上传成功！", MineInfomationSetupActivity.this);
				
				Gson gson = new Gson();
				ImageUploadCallback callback = new ImageUploadCallback();
				callback = gson.fromJson(new String(arg2), ImageUploadCallback.class);
				if(callback.getResult().equals("1")){
					avatar=callback.getUrl();
				}
			}
		
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				ToastTool.showNetworkError(MineInfomationSetupActivity.this);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_infomation_setup, menu);
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
