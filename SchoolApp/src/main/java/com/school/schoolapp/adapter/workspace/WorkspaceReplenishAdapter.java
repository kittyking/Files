package com.school.schoolapp.adapter.workspace;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.store.StoreGoodsVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkspaceReplenishAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
    private List<StoreGoodsVO> goods;
    
    private Context mContext;
	
	public WorkspaceReplenishAdapter(Context context,List<StoreGoodsVO> goods){
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
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
		
		final int index = position;
		if(goods!=null && goods.size()>0){
			TextView goodsName = (TextView)convertView.findViewById(R.id.goodsName);
			goodsName.setText(goods.get(position).getGoodsName());
			
			TextView goodsTitle = (TextView)convertView.findViewById(R.id.goodsTitle);
			goodsTitle.setText(goods.get(position).getGoodsTitle());
			
			TextView marketPrice = (TextView)convertView.findViewById(R.id.marketPrice);
			marketPrice.setText("￥"+goods.get(position).getMarketPrice());
			marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
			
			TextView shopPrice = (TextView)convertView.findViewById(R.id.shopPrice);
			shopPrice.setText("￥"+goods.get(position).getShopPrice());
			
//			TextView timerTV = (TextView)convertView.findViewById(R.id.timerTV);
//			timerTV.setAlpha(0);
			
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
			
			final TextView numberTV = (TextView)convertView.findViewById(R.id.numberTV);
			numberTV.setText(""+StoreCartEntity.getInstance().getGoodAddedCount(goods.get(position).getGoods_id()));
			numberTV.setAlpha(1);
			
			((TextView)convertView.findViewById(R.id.stock)).setText("库存:"+goods.get(position).getStock());
			((TextView)convertView.findViewById(R.id.stock)).setAlpha(1);
			
			final ImageButton cutBtn = (ImageButton)convertView.findViewById(R.id.cutBtn);
			cutBtn.setVisibility(View.VISIBLE);
			cutBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int number = Integer.parseInt( numberTV.getText().toString()) -1;
					if(number>=0){
						numberTV.setText(""+number);
						//保存到补货购物车中
						StoreCartEntity.getInstance().delReplenishGoods(goods.get(index), goods.get(index).getGoods_id(), goods.get(index).getShopPrice(), goods.get(index).getMarketPrice(), number);
						
						
					}
					
				}
			});
			
			ImageButton addBtn = (ImageButton)convertView.findViewById(R.id.addBtn);
			addBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int number = Integer.parseInt( numberTV.getText().toString()) +1;
					numberTV.setText(""+number);
					numberTV.setAlpha(1);
					//保存到补货购物车中
					StoreCartEntity.getInstance().addGoods(goods.get(index),goods.get(index).getGoods_id(), goods.get(index).getShopPrice(),goods.get(index).getMarketPrice(),cutBtn,numberTV);
					
					
				}
			});
			
			
		}
		
		
		return convertView;
	}

}
