package com.school.schoolapp.classes.voicerecode;

import android.media.MediaPlayer;
import android.util.Log;

public class UPlayer {

	private String path;  
	private MediaPlayer mPlayer; 
	
	public UPlayer(String path){  
        this.path = path;  
        mPlayer = new MediaPlayer();  
    }  

	public boolean start() {  
        try {    
             //设置要播放的文件  
             mPlayer.setDataSource(path);  
             mPlayer.prepare();  
             //播放  
             mPlayer.start();         
         }catch(Exception e){  
             Log.e("", "prepare() failed");    
         }  
  
        return false;  
    }  
	
	public boolean stop() {  
        mPlayer.stop();  
        mPlayer.release();  
        mPlayer = null;  
        return false;  
    }  
}
