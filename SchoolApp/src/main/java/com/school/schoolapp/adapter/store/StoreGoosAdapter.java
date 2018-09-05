package com.school.schoolapp.adapter.store;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.entity.store.StoreGoodsVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreGoosAdapter extends BaseAdapter {
	
    private LayoutInflater mInflater;
	private Context mContext;
    private List<StoreGoodsVO> goods;
	
	public StoreGoosAdapter(Context context,List<StoreGoodsVO> goods){
		this.mInflater = LayoutInflater.from(context);
		this.mContext=context;
		this.goods = goods;
		if(goods ==null)
			this.goods = new ArrayList<StoreGoodsVO>();
	}
	public void add(List<StoreGoodsVO> goodVOs) {
		// TODO Auto-generated method stub
		
		for(StoreGoodsVO good:goodVOs){
			goods.add(good);
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(goods !=null)
		    return goods.size();
		return 0;
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
		convertView = mInflater.inflate(R.layout.adapter_goods_listviewcell, null);
		
		if(goods!=null && goods.size()>0){
			TextView goodsName = (TextView)convertView.findViewById(R.id.goodsName);
			goodsName.setText(goods.get(position).getGoodsName());
			
			TextView goodsTitle = (TextView)convertView.findViewById(R.id.goodsTitle);
			goodsTitle.setText(goods.get(position).getGoodsTitle());
			
			TextView marketPrice = (TextView)convertView.findViewById(R.id.marketPrice);
			marketPrice.setText(goods.get(position).getMarketPrice());
			
			TextView shopPrice = (TextView)convertView.findViewById(R.id.shopPrice);
			shopPrice.setText(goods.get(position).getShopPrice());
			
			final TextView number = (TextView)convertView.findViewById(R.id.numberTV);
			
			ImageButton addBtn = (ImageButton)convertView.findViewById(R.id.addBtn);
			addBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int num = Integer.parseInt(number.getText().toString()) + 1;
					number.setText(""+num);
				}
			});
			ImageButton cutBtn = (ImageButton)convertView.findViewById(R.id.cutBtn);
			cutBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int cur = Integer.parseInt(number.getText().toString());
					if(cur>0){
						int num =  cur - 1;
						number.setText(""+num);
					}
				}
			});
			//显示商品图片
			ImageView iv = (ImageView)convertView.findViewById(R.id.goodsImg);
			ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
			if(!ImageLoader.getInstance().isInited())
		    	ImageLoader.getInstance().init(configuration);
			//显示图片的配置  
	        DisplayImageOptions options = new DisplayImageOptions.Builder()  
	                .cacheInMemory(true)  
	                .cacheOnDisk(true)  
	                .bitmapConfig(Bitmap.Config.RGB_565)  
	                .build();  
	        ImageLoader.getInstance().displayImage(goods.get(position).getGoodsImg(), iv,options);
	        
			//显示倒计时
		}
		
		
		return convertView;
	}

}
