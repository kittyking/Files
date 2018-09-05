package com.school.schoolapp.activity.mine.work;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.FloorChooseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.SchoolChooseActivity;
import com.school.schoolapp.activity.login.UserCityActivity;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.mine.MineWorkInfoCallback;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MineApplyActivity extends BaseActivity {
	
	private EditText nameTxt ;
	
	private TextView unitBtn,cardBtn;
	private TextView city,school,floor;
	
	private String floorID,schoolID,cityID;
	
	private RadioGroup sexGroup;
	
	private String card;

	public static int CITY=1001;
	public static int SCHOOL=1002;
	public static int FLOOR=1003;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_apply);
		this.titleTextView.setText("我要赚钱");
		((TextView)findViewById(R.id.phoneNum)).setText(LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getMobile());
		
		schoolID=LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getSchoolID();
		cityID=LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getCityID();
		floorID=LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getFloorID();
		

		school=(TextView)findViewById(R.id.school);
		school.setText(LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getSchoolName());
		floor=(TextView)findViewById(R.id.unitBtn);
		floor.setText(LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getFloorName());
		city=(TextView)findViewById(R.id.city);
		city.setText(LocalSharedPreferenceSingleton.getInstance().getUserInfo(this).getData().getCityName());
		
		sexGroup = (RadioGroup)findViewById(R.id.sexGroup);
		
		nameTxt = (EditText)findViewById(R.id.realname);
		nameTxt.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        EditText _v=(EditText)v;
		        if (!hasFocus) {// 失去焦点
		            _v.setHint(_v.getTag().toString());
		        } else {
		            String hint=_v.getHint().toString();
		            _v.setTag(hint);
		            _v.setHint("");
		        }
		    }
		});
		
//		this.doBtn.setAlpha(1);
//	    this.doBtn.setText("帮助");
//	    this.doBtn.setPadding(0, 0, 40, 0);
//	    Drawable leftDrawable = getResources().getDrawable(R.drawable.icon_help_actionbar);
//	    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth()-10, leftDrawable.getMinimumHeight()-10);
//	    this.doBtn.setCompoundDrawables(leftDrawable, null, null, null);
		
		Button applyBtn = (Button)findViewById(R.id.applyBtn);
		applyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				applyInfo();
			}
		});
		((Button)findViewById(R.id.agreement)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MineApplyActivity.this,ApplyAgreementActivity.class));
			}
		});
				
		
		cardBtn = (TextView)findViewById(R.id.cardBtn);
		unitBtn = (TextView)findViewById(R.id.unitBtn);
	}
	
	public void applyInfo(){
		String truename = nameTxt.getText().toString();
		String  floor = floorID;
		RadioButton curBtn = (RadioButton) findViewById(sexGroup.getCheckedRadioButtonId());
		String sex = curBtn.getText().toString();
		String idcard = card;
		
		String url = getString(R.string.base_url)+getString(R.string.work_apply_submit);
		RequestParams params = new RequestParams();
		params.put("ticket", ticket);
		params.put("truename", truename);
		params.put("floor", floor);
		params.put("sex", sex);
		params.put("idcard", idcard);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				BaseCallback callback = new BaseCallback();
				callback = gson.fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					Intent intent = new Intent();
					intent.setClass(MineApplyActivity.this, MineSuccessActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(MineApplyActivity.this, callback.getMsg(), Toast.LENGTH_SHORT).show();
					//MineApplyActivity.this.finish();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getApplicationContext());
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==RESULT_OK && data!=null){
			switch (requestCode) {
			case 1003:
				unitBtn.setText(data.getStringExtra("Floor"));
				floorID = data.getStringExtra("FloorID");
				break;
			case 2:
				card = data.getStringExtra("cardImg");
				cardBtn.setText("已上传");
				break;
			case 1001:
				cityID = data.getStringExtra("cityid");
				city.setText(data.getStringExtra("cityname"));
				school.setText("");
				floor.setText("");
				break;
			case 1002:
				schoolID = data.getStringExtra("schoolid");
				school.setText(data.getStringExtra("schoolname"));
				floor.setText("");
				break;
			default:
				break;
			}
		}
		
		
	}
	public void cardUpload(View v){
		Intent intent = new Intent();
		intent.setClass(MineApplyActivity.this, MineCardActivity.class);
		startActivityForResult(intent, 2);
	}

	public void buildingUpload(View v){
		Intent intent = new Intent();
		intent.setClass(MineApplyActivity.this, FloorChooseActivity.class);
		intent.putExtra("from", "workapply");
		intent.putExtra("schoolid", schoolID);
		startActivityForResult(intent, FLOOR);
	}
	public void cityChoose(View v){
		Intent intent = new Intent();
		intent.setClass(MineApplyActivity.this, UserCityActivity.class);
		intent.putExtra("finish", "finish");
		startActivityForResult(intent, CITY);
	}
	
	public void schoolChoose(View v){
		Intent intent = new Intent();
		intent.setClass(MineApplyActivity.this, SchoolChooseActivity.class);
		intent.putExtra("cityid", cityID);
		startActivityForResult(intent, SCHOOL);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_apply, menu);
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
