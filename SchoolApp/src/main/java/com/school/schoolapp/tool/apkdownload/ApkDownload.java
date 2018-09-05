package com.school.schoolapp.tool.apkdownload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;

public class ApkDownload {
	private static ApkDownload apkDownload;
	private Context context;
	public static ApkDownload getInstance(){
		if(apkDownload==null)
			apkDownload= new ApkDownload();
		return apkDownload;
	}

	public void alertToUpdate(final String url,Context context){
		this.context = context;
		//提示用户更新
		Dialog dialog = new AlertDialog.Builder(this.context).setTitle("发现新版本").setMessage("1.新版本加入了更新提示的功能").setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).setPositiveButton("更新", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 下载安装包
			    downloadAPK(url);
			}
		}).create();
		dialog.show();
	}
	private void downloadAPK(final String url){
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载");
		pd.show();
		new Thread(){
			@Override
			public void run() {
				try {
					File file = getFileFromServer(url, pd);
					sleep(2000);
					if(file != null)
					    installApk(file);
					pd.dismiss();
				} catch (Exception e) {
					Message msg = new Message();  
	                msg.what = 0;  
	                //handler.sendMessage(msg);  
	                e.printStackTrace();  
				}
				
				
			};
		}.start();
	}
	
	private File getFileFromServer(String path, ProgressDialog pd) throws Exception{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//获取文件大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;   
			while ((len =bis.read(buffer))!=-1) {
				fos.write(buffer, 0, len); 
				total+= len;
				//获得当前下载量
				pd.setProgress(total); 
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}else{
			return null;
		}
	}
	
	//安装apk    
	protected void installApk(File file) {  
	    Intent intent = new Intent();  
	    //执行动作   
	    intent.setAction(Intent.ACTION_VIEW);  
	    //执行的数据类型   
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
	    context.startActivity(intent);  
	}  
}
