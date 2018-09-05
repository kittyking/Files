package com.school.schoolapp.adapter.store;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.entitys.shopVO;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;
import com.school.schoolapp.view.RoundRectImageView;
import com.school.schoolapp.view.verticalpager.MyPagerAdapter;
import com.school.schoolapp.view.verticalpager.VerticalViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.school.schoolapp.view.verticalpager.PagerAdapter;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class StoreListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	
	private List<shopVO> shops;
	
	private Context mContext;
	
	private int index=0;
	
	private int letterIndex = 0;
	
	private List<TextView> letterTVs;
	
	private TextSwitcher noticeTS;
	
	private int noticePlayDelay = 5000;
	
	private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		letterIndex++;
    		if(letterIndex>=shops.get(index).getShopLetter().size())
    			letterIndex = 0;
    		//让TextSwitcher滚动
    		if(shops.get(index).getShopLetter()!=null && shops.get(index).getShopLetter().size()>0)
    			noticeTS.setText(shops.get(index).getShopLetter().get(letterIndex).getTitle());
    		//延时，循环调用handler
    		handler.sendEmptyMessageDelayed(0, noticePlayDelay);
    	}
    };
	
	public StoreListAdapter(Context context,List<shopVO> shops){
		this.mInflater = LayoutInflater.from(context);
		this.shops =shops;
		this.mContext = context;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shops.size();
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
	    convertView = mInflater.inflate(R.layout.adapter_store_listviewcell, null);
	    
	    index = position;
	    
	    TextView shopname = (TextView)convertView.findViewById(R.id.shopnameTV);
	    shopname.setText(shops.get(position).getShopName());
	    
	    TextView lowest = (TextView)convertView.findViewById(R.id.lowestTV);
	    lowest.setText(shops.get(position).getShopLowest()+"元起送价");
	    
	    TextView goodsNum = (TextView)convertView.findViewById(R.id.goodsNumberTV);
	    goodsNum.setText("共"+shops.get(position).getShopGoodsnum()+"种商品");
	    
//	    TextView opendTV = (TextView)convertView.findViewById(R.id.openedTV);
	    String opened = shops.get(position).getShopOpened();
//	    if(opened.equals("1")){
//	    	opendTV.setText("营业中");
//	    	opendTV.setTextColor(Color.RED);
//	    }else{
//	    	opendTV.setText("休息中");
//	    	opendTV.setTextColor(Color.GRAY);
//	    }
	    if(opened.equals("1")){
	    	ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_store_yy, ((ImageView)convertView.findViewById(R.id.openedIV)));
	    	
	    }else{
	    	ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_store_xx, ((ImageView)convertView.findViewById(R.id.openedIV)));
	    	
	    }
	    
	    
	    
	    //设置头像未添加
	    RoundRectImageView imageview = (RoundRectImageView)convertView.findViewById(R.id.shopHeadIV);
        ImageLoaderTool.getInstance().displayImage(shops.get(position).getShopHead(), imageview,mContext);
        if(shops.get(position).getShopHead().equals("http://shopapp.chunchennet.cn/")){
        	imageview.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_x144));
        }
	    
        //添加公告
        noticeTS = (TextSwitcher)convertView.findViewById(R.id.noticeSwitcher);
        noticeTS.setFactory(new ViewFactory() {
			
			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				TextView tv =new TextView(mContext);  
				tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT)); 
                tv.setTextSize(16);  
                tv.setTextColor(Color.GRAY);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                return tv;
			}
		});
        if(shops.get(position).getShopLetter()!=null&& shops.get(index).getShopLetter().size()>0)
        	noticeTS.setText(shops.get(position).getShopLetter().get(letterIndex).getTitle());
        // 设置切入动画  
        noticeTS.setInAnimation(AnimationUtils.loadAnimation(mContext,R.anim.slide_in_bottom));
        // 设置切出动画  
        noticeTS.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_up));
  
        handler.sendEmptyMessageDelayed(0, noticePlayDelay);
        
	    
		return convertView;
	}

}
