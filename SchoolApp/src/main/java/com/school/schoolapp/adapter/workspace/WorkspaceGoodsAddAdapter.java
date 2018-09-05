package com.school.schoolapp.adapter.workspace;

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
import com.school.schoolapp.activity.mine.workspace.MineWorkspaceAddGoodsActivity;
import com.school.schoolapp.callback.store.StoreGoodsCallback.GoodsVO;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.school.schoolapp.entity.user.UserFloorVO;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;
import com.thuongnh.zprogresshud.ZProgressHUD;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WorkspaceGoodsAddAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	
	private Context mContext;
	
    private List<StoreGoodsVO> goods;
    
    private int flag;//用于判断是未添加列表还是已添加列表 0未添加 1已添加
    
    private String ticket;
    
    public WorkspaceGoodsAddAdapter(Context context,List<StoreGoodsVO> goods,int flag,String ticket){
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.goods = goods;
		this.flag = flag;
		this.ticket = ticket;
	}
    public void update(List<StoreGoodsVO> goods){
    	this.goods = goods;
    }
    public void addGoods(List<StoreGoodsVO> goods){
    	for(StoreGoodsVO good : goods)
    		this.goods.add(good);
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(goods!=null)
		    return goods.size();
		else
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
		convertView = mInflater.inflate(R.layout.adapter_goods_add, null);
		
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
		ImageLoaderTool.getInstance().displayImage(goods.get(position).getGoodsImg(), iv, mContext);
		
		Button addBtn = (Button)convertView.findViewById(R.id.addBtn);
		final int index = position;
		switch (flag) {
		case 0:
			addBtn.setText("添加");
			addBtn.setBackground(convertView.getResources().getDrawable(R.drawable.background_cart_blue));
			addBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String url = mContext.getString(R.string.base_url)+mContext.getString(R.string.workspace_goodsadd_add);
					RequestParams params = new RequestParams();
					params.put("ticket", ticket);
					params.put("goodsid", goods.get(index).getGoods_id());
					final ZProgressHUD progressHUD = ZProgressHUD.getInstance(mContext);
					progressHUD.setMessage("正在登录");
					progressHUD.show();
					AsyncHttpClient client = new AsyncHttpClient();
					client.post(url, params, new AsyncHttpResponseHandler() {
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							progressHUD.dismiss();
							Gson gson = new Gson();
							BaseCallback callback = new BaseCallback();
							callback = gson.fromJson(new String(arg2), BaseCallback.class);
							reloadData(index);
						    Toast.makeText(mContext, callback.getMsg(), Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							ToastTool.showNetworkError(mContext);
							progressHUD.dismiss();
						}
					});
				}
			});
			break;
        case 1:
        	addBtn.setVisibility(View.GONE);
        	//addBtn.setText("删除");
        	//addBtn.setBackground(convertView.getResources().getDrawable(R.drawable.background_cart_gray));
//        	addBtn.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					String url = mContext.getString(R.string.base_url)+mContext.getString(R.string.workspace_goodsadd_del);
//					RequestParams params = new RequestParams();
//					params.put("ticket", ticket);
//					params.put("goodsid", goods.get(index).getGoods_id());
//					AsyncHttpClient client = new AsyncHttpClient();
//					client.post(url, params, new AsyncHttpResponseHandler() {
//						
//						@Override
//						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//							// TODO Auto-generated method stub
//							Gson gson = new Gson();
//							BaseCallback callback = new BaseCallback();
//							callback = gson.fromJson(new String(arg2), BaseCallback.class);
//							reloadData(index);
//						    Toast.makeText(mContext, callback.getMsg(), Toast.LENGTH_SHORT).show();
//						}
//						
//						@Override
//						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//							// TODO Auto-generated method stub
//							ToastTool.showNetworkError(mContext);
//						}
//					});
//				}
//			});
			break;

		default:
			break;
		}
		return convertView;
	}
	
	public void reloadData(int index){
		goods.remove(index);
		WorkspaceGoodsAddAdapter.this.notifyDataSetChanged();
	}

}
