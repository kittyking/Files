package com.school.schoolapp.adapter.mine;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.R;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.technical.SkillMangerEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MineSillManagerAadapter extends BaseAdapter {

	private Context context;
	private List<SkillMangerEntity> skills;
	private String ticket;
	public MineSillManagerAadapter(Context context,String ticket){
		this.context=context;
		this.ticket=ticket;
		this.skills = new ArrayList<>();
	}
	
	public void addList(List<SkillMangerEntity> skills){
		for(SkillMangerEntity skill : skills){
			this.skills.add(skill);
		}
		this.notifyDataSetChanged();
	}
	
	public void cleanSkill(){
		this.skills= new ArrayList<>();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.skills.size();
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
		final SkillMangerEntity skill = skills.get(position);
		convertView=LayoutInflater.from(context).inflate(R.layout.cell_service_manger, null);
		((TextView)convertView.findViewById(R.id.serviceTitle)).setText(skill.getTitle());
		((TextView)convertView.findViewById(R.id.price)).setText("￥"+skill.getMoney()+"元");
		
		Button updown = (Button)convertView.findViewById(R.id.updown);
		updown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skillUpdown(skill.getSid());
			}
		});
		return convertView;
	}
	
	private void skillUpdown(String skid){
		String url = context.getString(R.string.base_url)+context.getString(R.string.skill_manager_up_down);
		RequestParams params = new RequestParams();
		params.put("ticket",this.ticket );
		params.put("skid", skid);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					ToastTool.showWithMessage("上架成功", context);
				}else{
					ToastTool.showWithMessage("上架失败，请重试", context);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(context);
			}
		});
	}

}
