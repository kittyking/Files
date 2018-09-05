package com.school.schoolapp.adapter.photopicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;







import android.widget.ImageView.ScaleType;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.tool.photopicker.SDCardImageLoader;
import com.school.schoolapp.tool.photopicker.ScreenUtils;

/**
 * 主页面中GridView的适配器
 *
 * @author hanj
 */

public class MainGVAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;

    private SDCardImageLoader loader;

    public MainGVAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;

        loader = new SDCardImageLoader(ScreenUtils.getScreenW(), ScreenUtils.getScreenH());
    }

    @Override
    public int getCount() {
    	if(imagePathList == null)
    		return 1;
    	else if(imagePathList.size()<9)
    		return imagePathList.size()+1;
		else
			return 9;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	//获取view
        final ViewHolder holder;
        convertView = LayoutInflater.from(context).inflate(R.layout.main_gridview_item, null);
        holder = new ViewHolder();

        holder.imageView = (ImageView) convertView.findViewById(R.id.main_gridView_item_photo);
        convertView.setTag(holder);
        
        //判断显示
        if(imagePathList==null){
        	ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_skill_publish_add, holder.imageView);
        }else if(imagePathList.size()==9){
        	String filePath = imagePathList.get(position);
        	ImageLoader.getInstance().displayImage("file://"+filePath, holder.imageView);
        }else{
        	if(position == imagePathList.size()){
        		ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_skill_publish_add, holder.imageView);
        	}else{
        		String filePath = imagePathList.get(position);
            	ImageLoader.getInstance().displayImage("file://"+filePath, holder.imageView);
        	}
        }


        //String filePath = (String) getItem(position);
        //ImageLoader.getInstance().displayImage("file://"+filePath, holder.imageView);
        
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }

}
