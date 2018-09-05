package com.school.schoolapp.adapter.workspace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.activity.mine.workspace.MineWorkspaceReplenishActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.entity.store.StoreGoodsVO;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsManagerNotSellAdapter extends BaseAdapter {

	private Context mContext;
	
    private LayoutInflater mInflater;
	
    private List<StoreGoodsVO> goods;
    
    private int flag; //0未上架 1已上架 2已推荐 3已置顶
    
    public GoodsManagerNotSellAdapter(Context context,int flag){
    	this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		
		this.flag = flag;
	}
    public List<StoreGoodsVO> getGoods(){
    	return this.goods;
    }
    public void addGoods(List<StoreGoodsVO> goods,int tag){//tag标识是添加还是刷新了
    	if(goods!=null && goods.size()>0){
    		if(this.goods==null)
    			this.goods =new ArrayList<>();
    			
    		if(tag==0){
    			this.goods=goods;
    			//Log.i("", "tag="+tag+"size="+goods.size());
    		}else{
    			for(StoreGoodsVO good : goods)
    	    		this.goods.add(good);
    			//Log.i("", "tag="+tag+"size="+goods.size());
    		}
    	}
    	
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(goods!=null)
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

	private Button normalBtn,specialBtn;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stu
		convertView = mInflater.inflate(R.layout.adapter_goods_manager_cell, null);
		normalBtn = (Button)convertView.findViewById(R.id.normalBtn);
		specialBtn = (Button)convertView.findViewById(R.id.specialBtn);
		
		TextView goodsName = (TextView)convertView.findViewById(R.id.goodsName);
		goodsName.setText(goods.get(position).getGoodsName());
		
		TextView goodsTitle = (TextView)convertView.findViewById(R.id.goodsTitle);
		goodsTitle.setText(goods.get(position).getGoodsTitle());
		
		TextView marketPrice = (TextView)convertView.findViewById(R.id.marketPrice);
		marketPrice.setText(goods.get(position).getMarketPrice());
		marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
		
		TextView shopPrice = (TextView)convertView.findViewById(R.id.shopPrice);
		shopPrice.setText(goods.get(position).getShopPrice());
		
		//显示商品图片
		ImageView iv = (ImageView)convertView.findViewById(R.id.goodsImg);
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
		ImageLoader.getInstance().init(configuration);
		//显示图片的配置  
        DisplayImageOptions options = new DisplayImageOptions.Builder()  
                .cacheInMemory(true)  
                .cacheOnDisk(true)  
                .bitmapConfig(Bitmap.Config.RGB_565)  
                .build();  
        ImageLoader.getInstance().displayImage(goods.get(position).getGoodsImg(), iv,options);
		
		final int index = position;
		switch (this.flag) {
		case 0:
			normalBtn.setText("上架");
			normalBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url =mContext.getString(R.string.base_url)+ mContext.getString(R.string.workspace_goodmanager_setupsalegoods);
					setGoodsState(url, index);
				}
			});
			break;
		case 1:
			normalBtn.setText("已置顶");
			if(goods.get(position).getTop().equals("0")){
				normalBtn.setText("置顶");
	            normalBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url =mContext.getString(R.string.base_url)+ mContext.getString(R.string.workspace_goodmanager_settopsalegoods);
						setGoodsState(url, index);
					}
				});
			}
			specialBtn.setAlpha(1);
			specialBtn.setText("已推荐");
			specialBtn.setTextColor(Color.BLACK);
			if(goods.get(position).getHot().equals("0")){
				specialBtn.setText("推荐");
				specialBtn.setEnabled(true);
				specialBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url =mContext.getString(R.string.base_url)+ mContext.getString(R.string.workspace_goodmanager_sethotsalegoods);
						setGoodsState(url, index);
					}
				});
			}
			
			break;
		case 2:
			normalBtn.setText("已置顶");
			if(goods.get(position).getTop().equals("0")){
				normalBtn.setText("置顶");
				normalBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url =mContext.getString(R.string.base_url)+ mContext.getString(R.string.workspace_goodmanager_settopsalegoods);
						setGoodsState(url, index);
					}
				});
			}
			break;
		case 3:
			normalBtn.setAlpha(0);
			normalBtn.setEnabled(false);
			break;

		default:
			break;
		}
		
		return convertView;
	}
	
	public void setGoodsState(String url,final int position){
		SharedPreferences sp = mContext.getSharedPreferences("sp",mContext.MODE_PRIVATE);
		String userStr = sp.getString("USER_KEY", "NULL");
		Gson gson = new Gson();
		final UserCallback user = gson.fromJson(userStr, UserCallback.class);
		final String ticket = user.getTicket();
		RequestParams params = new RequestParams();
		params.put("ticket", ticket);
		params.put("goodsid", goods.get(position).getGoods_id());
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = new BaseCallback();
				callback = gson.fromJson(new String(arg2), BaseCallback.class);
				Toast.makeText(mContext, callback.getMsg(), Toast.LENGTH_SHORT).show();
				if(flag == 0){
					goods.remove(position);
					GoodsManagerNotSellAdapter.this.notifyDataSetChanged();
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(mContext);
			}
		});
	}

}
