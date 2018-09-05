package com.school.schoolapp.activity.mine.workspace;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

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
import com.school.schoolapp.activity.mine.work.MineCardActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.ImageUploadCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.classes.workspace.WorkspaceShopInfoCallback;
import com.school.schoolapp.classes.workspace.WorkspaceShopInfoCallback.ShopInfoVO.ShopInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MineWorkspaceSetupActivity extends BaseActivity {
	
	private EditText shopnameET,moneyET;
	
	private int[] layoutId = {R.id.addLinear1,R.id.addLinear2,R.id.addLinear3};
	
	private int[] reLayoutId = {R.id.addRelative1,R.id.addRelative2,R.id.addRelative3};
	
	private int[] noticeId = {R.id.noticeEditText1,R.id.noticeEditText2,R.id.noticeEditText3};
	
	private String headImg;

	private static final int IMAGE_RESULT = 1; 
	
	private static final int CAMERA_RESULT = 2; 
	
	private String mPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_workspace_setup);
		this.titleTextView.setText("店铺设置");
		
		shopnameET= (EditText)findViewById(R.id.shopnameET);
		moneyET = (EditText)findViewById(R.id.moneyET);
		
		String url = getString(R.string.base_url)+getString(R.string.workspace_shopInfo_get);
		RequestParams params = new RequestParams();
		params.put("ticket", this.ticket);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				WorkspaceShopInfoCallback shopInfo = new WorkspaceShopInfoCallback();
				shopInfo = gson.fromJson(new String(arg2), WorkspaceShopInfoCallback.class);
				
				if(shopInfo.getData().getShop().getShopName()!=null)
					shopnameET.setText(shopInfo.getData().getShop().getShopName());
				if(shopInfo.getData().getShop().getLowest()!=null)
					moneyET.setText(shopInfo.getData().getShop().getLowest());
				headImg = shopInfo.getData().getShop().getHeadImg();
				if(headImg.contains(".jpg")){
					TextView img = (TextView)findViewById(R.id.imgStatus);
					img.setText("已上传");
				}
					
				if(shopInfo.getData().getLetter() !=null && shopInfo.getData().getLetter().size()>0){
					for(int i=0;i<shopInfo.getData().getLetter().size();i++){
						LinearLayout addLinear = (LinearLayout)findViewById(layoutId[i]);
						addLinear.setAlpha(0);
						if(i<2){
							RelativeLayout addRelative2 = (RelativeLayout)findViewById(reLayoutId[i+1]);
							if(addRelative2 !=null)
								addRelative2.setAlpha(1);
						}
						EditText dt = (EditText)findViewById(noticeId[i]);
						dt.setText(shopInfo.getData().getLetter().get(i).getTitle());
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
		
		this.doBtn.setAlpha(1);
		this.doBtn.setText("保存");
		this.doBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = getString(R.string.base_url)+getString(R.string.workspace_shopInfo_set);
				RequestParams params = new RequestParams();
				params.put("ticket", MineWorkspaceSetupActivity.this.ticket);
				params.put("headimg", headImg);
				params.put("shopname", shopnameET.getText().toString());
				params.put("price", moneyET.getText().toString());
				params.put("letter", getLetters());
				AsyncHttpClient client = new AsyncHttpClient();
				client.post(url, params, new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						BaseCallback callback = new BaseCallback();
						callback = gson.fromJson(new String(arg2), BaseCallback.class);
						if(callback.getResult().equals("1"))
							finish();
						Toast.makeText(MineWorkspaceSetupActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						ToastTool.showNetworkError(getApplicationContext());
					}
				});
			}
		});
		
		Button addBtn1 = (Button)findViewById(R.id.addBtn1);
		addBtn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout addLinear1 = (LinearLayout)findViewById(R.id.addLinear1);
				addLinear1.setAlpha(0);
				RelativeLayout addRelative2 = (RelativeLayout)findViewById(R.id.addRelative2);
				addRelative2.setAlpha(1);
			}
		});
		
		Button addBtn2 = (Button)findViewById(R.id.addBtn2);
		addBtn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout addLinear2 = (LinearLayout)findViewById(R.id.addLinear2);
				addLinear2.setAlpha(0);
				RelativeLayout addRelative3 = (RelativeLayout)findViewById(R.id.addRelative3);
				addRelative3.setAlpha(1);
			}
		});
		
		Button addBtn3 = (Button)findViewById(R.id.addBtn3);
		addBtn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout addLinear3 = (LinearLayout)findViewById(R.id.addLinear3);
				addLinear3.setAlpha(0);
			}
		});
		
		Button imgBtn = (Button)findViewById(R.id.imgBtn);
		imgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MineWorkspaceSetupActivity.this).setTitle("请选择照片选取方式").setPositiveButton("从相册里选取", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						startActivityForResult(intent, IMAGE_RESULT);
					}
				}).setNeutralButton("拍照", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
					           startActivityForResult(intent, CAMERA_RESULT);  
					       }  
					       else {  
					           Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();  
					       }  
					}
				});
				
				builder.show();
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		//获取图片失败，返回
				if(resultCode != RESULT_OK){
					Log.i("获取失败", "获取失败");
					return;
				}
				
				Bitmap bitm = null;
				//Uri uri =data.getData();
				ContentResolver resolver = getContentResolver();
				
				if(requestCode == IMAGE_RESULT){
					try {
						Uri uri =data.getData();
						if(uri == null)
							return;
							
						bitm = MediaStore.Images.Media.getBitmap(resolver, uri);
						Bitmap b = this.comp(bitm);
						bitm = b;
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(CAMERA_RESULT == requestCode){
					bitm = BitmapFactory.decodeFile(mPhotoPath);
				
					 if(bitm == null){
						 Bundle bundle =data.getExtras();
					       if (bundle != null){
					    	   bitm =(Bitmap) bundle.get("data");
					       } 
					 }
					 
					 Bitmap b = this.comp(bitm);
					 bitm = b;
				}
				
				//上传图片，保存返回值到数组中
				String base64 = this.Bitmap2Base64(bitm);
				this.uploadImage(base64);
	}
	private void uploadImage(String base64){
		String url = getString(R.string.base_url) + getString(R.string.image_upload_base64);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		//String page = new String(base64);
		params.put("filedata", base64);
		SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
		String userStr = sp.getString("USER_KEY", "NULL");
		Gson gson = new Gson();
		final UserCallback user = gson.fromJson(userStr, UserCallback.class);
		params.put("ticket",user.getTicket());
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Toast toast =Toast.makeText(MineWorkspaceSetupActivity.this, "照片上传成功！", Toast.LENGTH_SHORT);
				toast.show();
				
				Gson gson = new Gson();
				ImageUploadCallback callback = new ImageUploadCallback();
				callback = gson.fromJson(new String(arg2), ImageUploadCallback.class);
				if(callback.getResult().equals("1")){
					
					headImg = callback.getUrl();
					TextView img = (TextView)findViewById(R.id.imgStatus);
					img.setText("已上传");
				}
			}
		
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Log.i("fff","fff");
			}
		});
	}
	//bitmap转换base64数据
	public String Bitmap2Base64(Bitmap bitmap) {
        try {
            // 先将bitmap转换为普通的字节数组
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);
            
            
            out.flush();
            out.close();
            byte[] buffer = out.toByteArray();
            // 将普通字节数组转换为base64数组
            String encode = Base64.encodeToString(buffer, Base64.DEFAULT);
            
            return encode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	//图片压缩
	private Bitmap comp(Bitmap image) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		
		if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	
	 private Bitmap compressImage(Bitmap image) {  
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
	        int options = 100;  
	        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
	            baos.reset();//重置baos即清空baos  
	            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
	            options -= 10;//每次都减少10  
	        }  
	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
	        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
	        return bitmap;  
	    }  
			  
	public String getLetters(){
		String letter="";
		for(int id:noticeId){
			EditText et = (EditText)findViewById(id);
			if(et.getText().length()>0&&et.getText()!=null){
				if(id==R.id.noticeEditText1)
					letter = letter+et.getText().toString();
				else
					letter = letter+","+et.getText().toString();
			}
		}
		return letter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_workspace_setup, menu);
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
