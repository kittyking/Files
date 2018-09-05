package com.school.schoolapp.adapter.technical;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.entity.technical.SkillInfoEntity;
import com.school.schoolapp.entity.technical.SkillInfoEntity.ImgC;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;

public class TechnicalHomeAdapter extends BaseAdapter {

	private Context mContext;
	private List<SkillInfoEntity>  skills;
	public TechnicalHomeAdapter(Context context){
		mContext = context;
		skills =new ArrayList<>();
	}
	
	public void addSkills(List<SkillInfoEntity>  skills){
		for(SkillInfoEntity skill : skills){
			this.skills.add(skill);
		}
		this.notifyDataSetChanged();
	}
	public List<SkillInfoEntity> getSkills(){
		return this.skills;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return skills.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private List<View> viewlist=new ArrayList<>();
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SkillInfoEntity skill = skills.get(position);
		convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_technical_home,null);
		
		//避免重用以后需要重新加载
		if(position+1>viewlist.size()){
			viewlist.add(convertView);
		}else{
			convertView = viewlist.get(position);
			return convertView;
		}
		
		//昵称
		TextView nick = (TextView)convertView.findViewById(R.id.username);
		nick.setText(skill.getNick());
		//评分
		TextView userevelution = (TextView)convertView.findViewById(R.id.userevelution);
		userevelution.setText(skill.getScore());
		//标题
		TextView tectitle = (TextView)convertView.findViewById(R.id.tectitle);
		tectitle.setText(skill.getTitle());
		//描述
		TextView tecdecribe = (TextView)convertView.findViewById(R.id.tecdecribe);
		tecdecribe.setText(skill.getSummary());
		//单价
		TextView tecmoney = (TextView)convertView.findViewById(R.id.tecmoney);
		tecmoney.setText(skill.getMoney()+"/"+skill.getUnit());
		//浏览量
		TextView views = (TextView)convertView.findViewById(R.id.yue);
		views.setText(skill.getViews());
		//性别
		
		//图片
		GridView gridV = (GridView)convertView.findViewById(R.id.tecpicture);
		if(skill.getImgs().size()>0){
			gridV.setAdapter(new ImageAdapter(skill.getImgs()));
		}
		//头像
		ImageLoaderTool.getInstance().displayImage(skill.getAvatar(), (ImageView)convertView.findViewById(R.id.usericon),mContext);
		//点赞
		//评论
		
		
		return convertView;
	}
	
	
	
	private class ImageAdapter extends BaseAdapter{

		private List<ImgC> images;
		
		public ImageAdapter(List<ImgC> images){
			this.images =images;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView image = new ImageView(mContext);
			ImageLoaderTool.getInstance().displayImage(images.get(position).getImgsrc(), image,mContext);
			return image;
		}
		
	}

	
}


