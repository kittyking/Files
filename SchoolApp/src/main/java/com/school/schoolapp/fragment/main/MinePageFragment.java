package com.school.schoolapp.fragment.main;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.R;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.activity.login.UserLoginActivity;
import com.school.schoolapp.activity.mine.MineInfomationSetupActivity;
import com.school.schoolapp.activity.mine.MineIntegralActivity;
import com.school.schoolapp.activity.mine.MineSetupActivity;
import com.school.schoolapp.activity.mine.wallet.MineWalletActivity;
import com.school.schoolapp.activity.mine.work.MineCardActivity;
import com.school.schoolapp.activity.technical.TechnicalMenuActivity;
import com.school.schoolapp.callback.mine.UserInfoCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.mine.MineWalletBalanceCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;
import com.school.schoolapp.view.cornerimage.CircularImage;

import android.R.integer;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link MinePageFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link MinePageFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class MinePageFragment extends Fragment {
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
	 * @return A new instance of fragment MinePageFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MinePageFragment newInstance(String param1, String param2) {
		MinePageFragment fragment = new MinePageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public MinePageFragment() {
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
	
	private LinearLayout menuLayout;
    private LinearLayout cashLinear;
    private LinearLayout integralLinear;
    private Button setupBtn;
    private ImageButton realnamebtn;
    private CircularImage userIcon;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_mine_page, container, false);
		
		if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())!=null)
			((TextView)v.findViewById(R.id.userName)).setText(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getMobile());
		userIcon =  (CircularImage)v.findViewById(R.id.userIcon);
		
		setupBtn = (Button)v.findViewById(R.id.setup);
		setupBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())==null){
					startActivity(new Intent(getActivity(),UserLoginActivity.class));
					return;
				 }
				startActivity(new Intent(getActivity(),MineSetupActivity.class));
			}
		});
		
		menuLayout = (LinearLayout)v.findViewById(R.id.moreMenu);
		cashLinear = (LinearLayout)v.findViewById(R.id.cashLinear);
		cashLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())==null){
					startActivity(new Intent(getActivity(),UserLoginActivity.class));
					return;
				 }
				startActivity(new Intent(getActivity(),MineWalletActivity.class));
			}
		});
		
		integralLinear = (LinearLayout)v.findViewById(R.id.integralLinear);
		integralLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())==null){
					startActivity(new Intent(getActivity(),UserLoginActivity.class));
					return;
				 }
				startActivity(new Intent(getActivity(),MineIntegralActivity.class));
				//startActivity(new Intent(getActivity(),TechnicalMenuActivity.class));
			}
		});
		
		ImageButton more = (ImageButton)v.findViewById(R.id.usermore);
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(menuLayout.getVisibility() == View.VISIBLE){
					menuLayout.setVisibility(View.GONE);
				}else{
					menuLayout.setVisibility(View.VISIBLE);
				}
			}
		});
		
		
		realnamebtn =(ImageButton)v.findViewById(R.id.realname);
		realnamebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())==null){
					startActivity(new Intent(getActivity(),UserLoginActivity.class));
					return;
				 }
				startActivity(new Intent(getActivity(),MineCardActivity.class));
			}
		});
		
		userIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())==null){
					startActivity(new Intent(getActivity(),UserLoginActivity.class));
					return;
				 }
				startActivity(new Intent(getActivity(),MineInfomationSetupActivity.class));
			}
		});
		balanceTV = (TextView)v.findViewById(R.id.cash);
		getRemainMoney();
		
		return v;
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestUserinfo();
		getRemainMoney();
	}
	
	public void requestUserinfo(){
		String url = getString(R.string.base_url)+getString(R.string.user_get_info);
		RequestParams params = new RequestParams();
		params.put("ticket",LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getTicket() );
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				UserInfoCallback userinfo = new Gson().fromJson(new String(arg2), UserInfoCallback.class);
				if(userinfo.getResult().equals("1")){
					if(userinfo.getData().getAvatar()!=null)
						ImageLoaderTool.getInstance().displayImage(getString(R.string.base_url)+userinfo.getData().getAvatar(),userIcon, getActivity());
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
			}
		});
	}
	private TextView balanceTV;
	public void getRemainMoney(){
		String url = getString(R.string.base_url)+getString(R.string.wallet_get_balace);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params= new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getTicket());
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				MineWalletBalanceCallback balance = gson.fromJson(new String(arg2), MineWalletBalanceCallback.class);
				if(balance.getResult().equals("1")){
					
					balanceTV.setText(balance.getBalance());
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getActivity());
			}
		});
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
