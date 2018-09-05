package com.school.schoolapp.fragment.main;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.school.schoolapp.R;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.activity.chat.ChatDetailActivity;
import com.school.schoolapp.activity.chat.SystemMessageActivity;
import com.school.schoolapp.activity.login.UserLoginActivity;
import com.school.schoolapp.adapter.chat.ChatDialogAdapter;
import com.school.schoolapp.adapter.chat.SystemMessageAdapter;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link ChatMainFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ChatMainFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class ChatMainFragment extends Fragment {
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
	 * @return A new instance of fragment ChatMainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ChatMainFragment newInstance(String param1, String param2) {
		ChatMainFragment fragment = new ChatMainFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ChatMainFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())==null){
			Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getActivity(),UserLoginActivity.class));
			return ;
		}
		
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		
		
	}


	private ListView dialogListView;
	
	private ChatDialogAdapter adapter;
	
//	用于装载收到的新消息
	private List<EMMessage> messages;
	
//	用于收到新消息后更新主界面。 由于获取消息在子线程，必须使用handler
	private Handler dialogHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss"); 
			String date = sDateFormat.format(new java.util.Date());
			adapter.setTime(date);
			
//  *****************只有把添加消息的操作放在handler里 刷新才能显示*****************
			adapter.receiveMessage(messages.get(0).getUserName());
			adapter.notifyDataSetChanged();
			
			Intent intent = new Intent();
			intent.setAction("Circle");
			intent.putExtra("visibility", "visible");
			getActivity().sendBroadcast(intent);
			//收到消息加红点
			
			
		};
	};
	
	private EMMessageListener messageListener = new EMMessageListener() {
		
		@Override
		public void onMessageReceived(List<EMMessage> arg0) {
			// TODO Auto-generated method stub
			messages = arg0;
			dialogHandler.sendEmptyMessageDelayed(0, 0);
		}
		
		@Override
		public void onMessageReadAckReceived(List<EMMessage> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onMessageDeliveryAckReceived(List<EMMessage> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onMessageChanged(EMMessage arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onCmdMessageReceived(List<EMMessage> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_chat_main, container, false);
		
		if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())==null){
			startActivity(new Intent(getActivity(),UserLoginActivity.class));
			return null;
		}

		EMClient.getInstance().groupManager().loadAllGroups();
		EMClient.getInstance().chatManager().loadAllConversations();
		
		
		dialogListView =(ListView)v.findViewById(R.id.chatLV);
		adapter = new ChatDialogAdapter(getActivity());
		SharedPreferences sp = getActivity().getSharedPreferences("sp",getActivity().MODE_PRIVATE);
		String userStr = sp.getString("USERLIST", "NONE");
		if(!userStr.equals("NONE")){
			Gson gson = new Gson();
			List<String> users = gson.fromJson(userStr, List.class);
			adapter.initMessage(users);
		}
		dialogListView.setAdapter(adapter);
		dialogListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),ChatDetailActivity.class);
				if(adapter.getUsers().size()>0){
					intent.putExtra("Username", adapter.getUsers().get(position));
					//判断是系统消息 显示另外的页面
					if(adapter.getUsers().get(position).equals("shop") || adapter.getUsers().get(position).equals("system"))
						intent.setClass(getActivity(),SystemMessageActivity.class);
					
					
					//阅读信息 刷新列表 去掉小红点
					EMConversation conversation = EMClient.getInstance().chatManager().getConversation(adapter.getUsers().get(position));
					//指定会话消息未读数清零
					conversation.markAllMessagesAsRead();
					adapter.notifyDataSetChanged();
					//移除小红点
					Intent bIntent = new Intent();
					bIntent.setAction("Circle");
					bIntent.putExtra("visibility", "gone");
					getActivity().sendBroadcast(bIntent);
					
					startActivityForResult(intent, 1001);
				}
			}
		});
		
		EMClient.getInstance().chatManager().addMessageListener(messageListener);
		return v;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
		super.onDestroy();
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1001)
			adapter.notifyDataSetChanged();
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
