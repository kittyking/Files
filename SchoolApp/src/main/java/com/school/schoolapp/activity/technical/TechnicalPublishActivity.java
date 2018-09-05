package com.school.schoolapp.activity.technical;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.activity.mine.MineInfomationSetupActivity;
import com.school.schoolapp.activity.photopicker.PhotoWallActivity;
import com.school.schoolapp.adapter.photopicker.MainGVAdapter;
import com.school.schoolapp.callback.technical.SkillWayCallback;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.ImageUploadCallback;
import com.school.schoolapp.classes.tools.PictureCompress;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.tool.photopicker.ScreenUtils;
import com.school.schoolapp.tool.photopicker.Utility;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TechnicalPublishActivity extends BaseActivity {

	private EditText titleEdit,decribeEdit,price,sale,unit;
	private TextView number,tecCategory;
	private RadioGroup wayRG;
    private MainGVAdapter adapter;
	private ArrayList<String> imagePathList,imgs;
	private GridView photoGv;
	private SkillWayCallback skillCallback;
	private String cid,wid;
	private ZProgressHUD progressHUD;
	int last = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_technical_publish);
		this.titleTextView.setText("发技能");
		
		progressHUD = ZProgressHUD.getInstance(this); 
        imagePathList = new ArrayList<String>();
        imgs=new ArrayList<>();
        photoGv=(GridView)findViewById(R.id.photoGV);
        adapter=new MainGVAdapter(this, imagePathList);
        photoGv.setAdapter(adapter);
        photoGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				addBtn(null);
			}
		});
        
		number=(TextView)findViewById(R.id.number);
		tecCategory=(TextView)findViewById(R.id.tecCategory);
		wayRG=(RadioGroup)findViewById(R.id.wayRG);
		wayRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.sm:
					((RelativeLayout)findViewById(R.id.skillArea)).setVisibility(View.VISIBLE);
					((TextView)findViewById(R.id.adressTxt)).setVisibility(View.GONE);
					((ImageView)findViewById(R.id.nextarrowarea)).setVisibility(View.GONE);
					((EditText)findViewById(R.id.area)).setVisibility(View.VISIBLE);
					break;
				case R.id.dd:
					((RelativeLayout)findViewById(R.id.skillArea)).setVisibility(View.VISIBLE);
					((TextView)findViewById(R.id.adressTxt)).setVisibility(View.VISIBLE);
					((ImageView)findViewById(R.id.nextarrowarea)).setVisibility(View.VISIBLE);
					((EditText)findViewById(R.id.area)).setVisibility(View.GONE);
					break;
				case R.id.xs:
					((RelativeLayout)findViewById(R.id.skillArea)).setVisibility(View.GONE);
					break;
				case R.id.yj:
					((RelativeLayout)findViewById(R.id.skillArea)).setVisibility(View.GONE);
					break;

				default:
					break;
				}
				//wid = skillCallback.getData().getList().get(checkedId-1).getWid();
			}
		});
		//requestSkillWay();
		
		titleEdit=(EditText)findViewById(R.id.titleEdit);
		price=(EditText)findViewById(R.id.price);
		sale=(EditText)findViewById(R.id.priceN);
		unit=(EditText)findViewById(R.id.unit);
		decribeEdit=(EditText)findViewById(R.id.decribeEdit);
		decribeEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count>500)
					Toast.makeText(TechnicalPublishActivity.this, "描述不得超过500字", Toast.LENGTH_SHORT).show();
				number.setText((start+count)+"/500");
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
	}
	
//	public void requestSkillWay(){
//		String url = getString(R.string.base_url)+getString(R.string.technical_get_skill_way);
//		RequestParams params = new RequestParams();
//		params.add("ticket", this.ticket);
//		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
//			
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				// TODO Auto-generated method stub
//				skillCallback = new Gson().fromJson(new String(arg2), SkillWayCallback.class);
//				if(skillCallback.getResult().equals("1")){
//					//获取成功
//					setupSkillWay(skillCallback);
//				}else {
//					//
//				}
//			}
//			
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				// TODO Auto-generated method stub
//				ToastTool.showNetworkError(TechnicalPublishActivity.this);
//			}
//		});
//	}
//
//	public void setupSkillWay(SkillWayCallback skillCallback){
//		for(int i=0;i<skillCallback.getData().getList().size();i++){
//			RadioButton rb = new RadioButton(this);
//			rb.setText(skillCallback.getData().getList().get(i).getTitle());
//			rb.setButtonDrawable(android.R.color.transparent);
//			
//	        rb.setBackground(getResources().getDrawable(R.drawable.selector_skill_way));
//			
//			wayRG.addView(rb);
//		}
//	}
	
	public void addBtn(View v){
		ScreenUtils.initScreen(this);
		Intent intent = new Intent(this, PhotoWallActivity.class);
        startActivity(intent);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		

        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }
        
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        if(paths==null)
        	return;
        
        //上传图片
        last=0;
		progressHUD.setMessage("正在上传");
		progressHUD.show();
        for(int i=0;i<paths.size();i++ ){
        	if(i==paths.size()-1)
        		last=1;
        	String path = paths.get(i);
        	try {
				uploadImage(PictureCompress.getInstance().string2Base64(path));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        //添加，去重
        boolean hasUpdate = false;
        for (String path : paths) {
            if (!imagePathList.contains(path)) {
                //最多9张
                if (imagePathList.size() == 9) {
                    Utility.showToast(this, "最多可添加9张图片。");
                    break;
                }

                imagePathList.add(path);
                hasUpdate = true;
            }
        }
        if (hasUpdate) {
        	
            adapter.notifyDataSetChanged();
        }

	}
	
	
	public void publishSkill(View v){
		String url = getString(R.string.base_url)+getString(R.string.skill_publish);
		RequestParams params = new RequestParams();
		params.add("ticket", this.ticket);
		params.add("cid", cid);
		params.add("wid", wid);
		params.add("title",titleEdit.getText().toString());
		params.add("summary",decribeEdit.getText().toString());
		params.add("money", price.getText().toString());
		params.add("activity", sale.getText().toString());
		params.add("unit", unit.getText().toString());
		String img="";
		if(imgs.size()>0){
			img=imgs.get(0);
			for(int i=1;i<imgs.size();i++){
				img=img+","+imgs.get(i);
			}
		}
		params.add("imgs", img);
		new AsyncHttpClient().post(url, params,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					ToastTool.showWithMessage("发布成功", TechnicalPublishActivity.this);
					//返回首页,未添加
					finish();
				}else{
					ToastTool.showWithMessage(callback.getMsg(), TechnicalPublishActivity.this);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(TechnicalPublishActivity.this);
			}
		});
	}
	
	public void categoryChoose(View v){
		startActivityForResult(new Intent(this,SkillCategoryPublishActivity.class), 101);
	}
	
	
	private void uploadImage(String base64){
		String url = getString(R.string.base_url) + getString(R.string.image_upload_base64);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("filedata", base64);
		params.put("ticket",this.ticket);
		
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
				Gson gson = new Gson();
				ImageUploadCallback callback = new ImageUploadCallback();
				callback = gson.fromJson(new String(arg2), ImageUploadCallback.class);
				if(callback.getResult().equals("1")){
					imgs.add(callback.getUrl());
					if(last==1){
						progressHUD.dismiss();
						ToastTool.showWithMessage( "照片上传成功！", TechnicalPublishActivity.this);
						
					}
				}
			}
		
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				ToastTool.showNetworkError(TechnicalPublishActivity.this);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==101 && resultCode==101){//选择技能分类回调
			tecCategory.setText(data.getStringExtra("name"));
			cid = data.getStringExtra("cid");
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.technical_publish, menu);
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
