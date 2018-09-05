package com.school.schoolapp.activity.billing;

import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddressEditActivity extends BaseActivity {
	
	private TextView sexTV,schoolTV,buildingTV;
	
	private EditText nameET,phoneET,addressET;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_edit);
		this.titleTextView.setText("编辑地址");
		
		sexTV = (TextView)findViewById(R.id.sexTV);
		schoolTV= (TextView)findViewById(R.id.schoolTV);
		//buildingTV= (TextView)findViewById(R.id.buildingTV);
		//buildingSP = (Spinner)findViewById(R.id.buildingSP);
		
		nameET = (EditText)findViewById(R.id.nameET);
		phoneET= (EditText)findViewById(R.id.phoneET);
		addressET= (EditText)findViewById(R.id.addressET);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.address_edit, menu);
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
