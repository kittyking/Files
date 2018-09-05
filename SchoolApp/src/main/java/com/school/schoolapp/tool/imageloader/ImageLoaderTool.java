package com.school.schoolapp.tool.imageloader;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ImageLoaderTool {
	public static ImageLoaderTool imageLoaderTool= null;
	
	public static ImageLoaderTool getInstance(){
		if(imageLoaderTool==null){
			imageLoaderTool=new ImageLoaderTool();
		}
		return imageLoaderTool;
	}
	
	public void displayImage(String url,ImageView imageView,Context context){
		
		
		//显示图片的配置  
        DisplayImageOptions options = new DisplayImageOptions.Builder()  
                .cacheInMemory(true)  
                .cacheOnDisk(true)
                .build();  
        if(!ImageLoader.getInstance().isInited()){
        	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
        			context)
        			.threadPoolSize(3)
        			// default
        			.threadPriority(Thread.NORM_PRIORITY - 2)
        			.denyCacheImageMultipleSizesInMemory()
        			.diskCacheFileNameGenerator(new Md5FileNameGenerator())
        			.tasksProcessingOrder(QueueProcessingType.LIFO)
        			.denyCacheImageMultipleSizesInMemory()
        			// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
        			.memoryCache(new WeakMemoryCache())
        			.memoryCacheSize((int) (2 * 1024 * 1024))
        			.memoryCacheSizePercentage(13)
        			// default
        			.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
        			.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
        			.defaultDisplayImageOptions(options).writeDebugLogs() // Remove
        			.build();

	    	ImageLoader.getInstance().init(config);
        }
		
        ImageLoader.getInstance().displayImage(url,imageView,options);
		
	}
}
