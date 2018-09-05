package com.school.schoolapp.activity.mine.work;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
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
import com.school.schoolapp.activity.login.UserLoginActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.ImageUploadCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MineCardActivity extends BaseActivity {
	
	private ImageButton cardTop;

	private static final int IMAGE_RESULT = 1; 
	
	private static final int CAMERA_RESULT = 2; 
	
	private String mPhotoPath;
	
	private String topImgStr,bottomImgStr;
	
	private int cur;
	
	private ImageView topImageView,bottomImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_card);
		this.titleTextView.setText("我的证件");
		
		topImageView = (ImageView)findViewById(R.id.topImage);
		bottomImageView = (ImageView)findViewById(R.id.bottomImage);
		
		this.doBtn.setAlpha(1);
	    this.doBtn.setText("规则");
	    this.doBtn.setPadding(0, 0, 10, 0);
	    Drawable leftDrawable = getResources().getDrawable(R.drawable.icon_help_actionbar);
	    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth()-10, leftDrawable.getMinimumHeight()-10);
	    this.doBtn.setCompoundDrawables(leftDrawable, null, null, null);
		
		
		ImageButton topBtn = (ImageButton)findViewById(R.id.cardTopAddBtn);
		topBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				uploadImg();
				cur = 0;
			}
		});
		
		ImageButton bottomBtn = (ImageButton)findViewById(R.id.cardBottomAddBtn);
		bottomBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				uploadImg();
				cur = 1;
			}
		});
		
		
		
		Button submitBtn = (Button)findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("cardImg", topImgStr+","+bottomImgStr);
				setResult(RESULT_OK, intent);
				MineCardActivity.this.finish();
			}
		});
		
	}
	
	public void uploadImg(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MineCardActivity.this).setTitle("请选择照片选取方式").setPositiveButton("从相册里选取", new DialogInterface.OnClickListener() {
			
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
		
		switch (cur) {
		case 0:
			topImageView.setImageBitmap(bitm);
			break;
		case 1:
			bottomImageView.setImageBitmap(bitm);
			break;

		default:
			break;
		}
		//topImageView.setImageBitmap(bitm);
		//上传图片，保存返回值到数组中
		String base64 = this.Bitmap2Base64(bitm);
		this.uploadImage(base64);
		
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
				Toast toast =Toast.makeText(MineCardActivity.this, "照片上传成功！", Toast.LENGTH_SHORT);
				toast.show();
				
				Gson gson = new Gson();
				ImageUploadCallback callback = new ImageUploadCallback();
				callback = gson.fromJson(new String(arg2), ImageUploadCallback.class);
				if(callback.getResult().equals("1")){
					switch (cur) {
					case 0:
						topImgStr = callback.getUrl();
						break;
					case 1:
						bottomImgStr = callback.getUrl();
						break;

					default:
						break;
					}
					
				}
			}
		
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				progressHUD.dismiss();
				ToastTool.showNetworkError(getApplicationContext());
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
		  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_card, menu);
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
