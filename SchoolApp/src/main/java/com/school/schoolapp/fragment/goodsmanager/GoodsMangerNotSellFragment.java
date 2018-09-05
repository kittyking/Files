package com.school.schoolapp.fragment.goodsmanager;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.activity.mine.work.MineCardActivity;
import com.school.schoolapp.activity.mine.workspace.MineWorkspaceReplenishActivity;
import com.school.schoolapp.adapter.workspace.GoodsManagerNotSellAdapter;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.classes.workspace.WorkspaceGoodsManagerCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the
 * {@link GoodsMangerNotSellFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link GoodsMangerNotSellFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class GoodsMangerNotSellFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment GoodsMangerNotSellFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static GoodsMangerNotSellFragment newInstance(String param1,
			String param2) {
		GoodsMangerNotSellFragment fragment = new GoodsMangerNotSellFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public GoodsMangerNotSellFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_goods_manger_not_sell,
				container, false);
		
		managerLV = (ListView)v.findViewById(R.id.managerLV);
		adapter = new GoodsManagerNotSellAdapter(getActivity(),0);
		managerLV.setAdapter(adapter);
		getListView(0);
		return v;
	}
	private String ticket;
	private ListView managerLV;
	private int lastItem,curpage=1;
	private GoodsManagerNotSellAdapter adapter;
	public void getListView(final int tag){
		String url = getActivity().getString(R.string.base_url)+getActivity().getString(R.string.workspace_goodmanager_nosalegoods);
		RequestParams params = new RequestParams();
		SharedPreferences sp = getActivity().getSharedPreferences("sp",getActivity().MODE_PRIVATE);
		String userStr = sp.getString("USER_KEY", "NULL");
		Gson gson = new Gson();
		final UserCallback user = gson.fromJson(userStr, UserCallback.class);
		
		ticket = user.getTicket();
		params.put("ticket", ticket);
		params.put("curpage", curpage);
		params.put("keys", "");
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson =new Gson();
				WorkspaceGoodsManagerCallback managers = new WorkspaceGoodsManagerCallback();
				managers = gson.fromJson(new String(arg2), WorkspaceGoodsManagerCallback.class);
				//Log.i("", "manger="+new String(arg2));
				if(managers.getResult().equals("1")){
					adapter.addGoods(managers.getData().getGoodsList(),tag);
					adapter.notifyDataSetChanged();
					
					//滑动到底部
					managerLV.setOnScrollListener(new OnScrollListener() {
						
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							// TODO Auto-generated method stub
							if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
								if(view.getLastVisiblePosition() == view.getCount() - 1)
									curpage++;
									getListView(1);
							}
							
						}
						
						@Override
						public void onScroll(AbsListView view, int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {
							// TODO Auto-generated method stub
							lastItem = firstVisibleItem + visibleItemCount -1 ;
						}
					});
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getActivity());
			}
		});
	}
	
	public void deleteGoods(String ticket,final int index){
		String url = getActivity().getString(R.string.base_url)+getActivity().getString(R.string.workspace_goodmanager_setdownsalegoods);
		RequestParams params = new RequestParams();
		
		final GoodsManagerNotSellAdapter adapter = (GoodsManagerNotSellAdapter)managerLV.getAdapter();
		params.put("ticket", ticket);
		params.put("goodsid", adapter.getGoods().get(index).getGoods_id());
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = new BaseCallback();
				callback = gson.fromJson(new String(arg2), BaseCallback.class);
				Toast.makeText(getActivity(), callback.getMsg(), Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getActivity());
			}
		});
	}
	
	public void updateFragment() {
		curpage=1;
		getListView(0);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
