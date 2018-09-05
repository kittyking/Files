package com.school.schoolapp.activity.technical;

import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.adapter.technical.TechnicalHomeAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TechnicalDemandActivity extends Activity {

	private ListView demandListview;
	private Button doBtn;
	private ImageButton backBtn;
	private RelativeLayout orderRL;
	private LinearLayout districtRL,categoryRL,filterRL;
	private String[] orders={"全部","最新发布","离我最近","价格最高","价格最低","成交量最高","评价最好"}; 
	private String[] distict1={"全国","青岛","南京","滁州","上海","北京","深圳","济南"}; 
	private String[] distict2={"崂山区","四方区","市南区","市北区"}; 
	private String[] category1={"全部分类","生活技能","运动技能","设计媒体","设计媒体","设计媒体","设计媒体","设计媒体"}; 
	private String[] category2={"全部生活技能","重装系统","代洗衣服","物品租借"}; 
	private ListView orderLV,districtLV1,districtLV2,categoryLV1,categoryLV2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_technical_demand);
		setActionBarLayout();
		
		demandListview=(ListView)findViewById(R.id.demandListview);
		demandListview.setAdapter(new TechnicalHomeAdapter(this));
		
		//排序页签
		orderRL=(RelativeLayout)findViewById(R.id.orderRL);
		orderLV=(ListView)findViewById(R.id.orderLV);
		orderLV.setAdapter(new OrderAdapter(orders));
		
		//区域页签
		districtRL=(LinearLayout)findViewById(R.id.districtRL);
		districtLV1=(ListView)findViewById(R.id.districtLV1);
		final OrderAdapter distirct1adapter = new OrderAdapter(distict1);
		districtLV1.setAdapter(distirct1adapter);
		districtLV1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				distirct1adapter.setSelectItem(position);
				distirct1adapter.notifyDataSetInvalidated();
			}
		});
		districtLV2=(ListView)findViewById(R.id.districtLV2);
		districtLV2.setAdapter(new OrderAdapter(distict2));
		
		//分类页签
		categoryRL=(LinearLayout)findViewById(R.id.categoryRL);
		categoryLV1=(ListView)findViewById(R.id.categoryLV1);
		final OrderAdapter category1adapter = new OrderAdapter(category1);
		categoryLV1.setAdapter(category1adapter);
		categoryLV1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				category1adapter.setSelectItem(position);
				category1adapter.notifyDataSetInvalidated();
			}
		});
		categoryLV2=(ListView)findViewById(R.id.categoryLV2);
		categoryLV2.setAdapter(new OrderAdapter(category2));
		
		
		//筛选页签
		filterRL=(LinearLayout)findViewById(R.id.filterRL);
		
	}
	
	public void showCategory(View v){
		filterRL.setVisibility(View.GONE);
		orderRL.setVisibility(View.GONE);
		districtRL.setVisibility(View.GONE);
		if(categoryRL.getVisibility()==View.GONE)
			categoryRL.setVisibility(View.VISIBLE);
		else
			categoryRL.setVisibility(View.GONE);
	}
	
	public void showDistict(View v){
		filterRL.setVisibility(View.GONE);
		orderRL.setVisibility(View.GONE);
		categoryRL.setVisibility(View.GONE);
		if(districtRL.getVisibility()==View.GONE)
			districtRL.setVisibility(View.VISIBLE);
		else
			districtRL.setVisibility(View.GONE);
	}
	
	public void showOrder(View v){
		filterRL.setVisibility(View.GONE);
		categoryRL.setVisibility(View.GONE);
		districtRL.setVisibility(View.GONE);
		if(orderRL.getVisibility()==View.GONE)
			orderRL.setVisibility(View.VISIBLE);
		else
			orderRL.setVisibility(View.GONE);
	}
	
	public class OrderAdapter extends BaseAdapter{
		private String[] listinfo;
		public OrderAdapter(String[] listinfo){
			this.listinfo =listinfo;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listinfo.length;
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
			convertView=LayoutInflater.from(TechnicalDemandActivity.this).inflate(android.R.layout.simple_list_item_1,null);
			TextView txt = (TextView)convertView.findViewById(android.R.id.text1);
			txt.setText(listinfo[position]);
			txt.setTextSize(14);
			
            if (position == selectItem) {  
                convertView.setBackgroundColor(Color.WHITE);  
            } 
			return convertView;
		}
		
		public  void setSelectItem(int selectItem) {  
            this.selectItem = selectItem;  
		}  
		private int  selectItem=0;  
		
	}
	
	public void setActionBarLayout() {
		 ActionBar actionbar = getActionBar();
	        if(null != actionbar){
	        	actionbar.setDisplayShowHomeEnabled(false);
	        	actionbar.setDisplayShowCustomEnabled(true);
	        	LayoutInflater mInflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        	View v = mInflator.inflate(R.layout.actionbar_technical_demand,null);
	            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	            actionbar.setCustomView(v,layout);
	            
	            this.doBtn = (Button)v.findViewById(R.id.doButton);
	            this.doBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						orderRL.setVisibility(View.GONE);
						categoryRL.setVisibility(View.GONE);
						districtRL.setVisibility(View.GONE);
	            
						if(filterRL.getVisibility()==View.GONE)
							filterRL.setVisibility(View.VISIBLE);
						else
							filterRL.setVisibility(View.GONE);
					}
				});
	            this.backBtn = (ImageButton)v.findViewById(R.id.backButton);
	            this.backBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						TechnicalDemandActivity.this.finish();
					}
				});
	        }
	}

	public void hideMenu(View v){
		filterRL.setVisibility(View.GONE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.technical_demand, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
