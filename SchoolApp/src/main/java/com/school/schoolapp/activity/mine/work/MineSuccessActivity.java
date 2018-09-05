package com.school.schoolapp.activity.mine.work;

import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MineSuccessActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_success);
		this.titleTextView.setText("我要赚钱");
		
		this.doBtn.setAlpha(1);
	    this.doBtn.setText("帮助");
	    this.doBtn.setPadding(0, 0, 40, 0);
	    Drawable leftDrawable = getResources().getDrawable(R.drawable.icon_help_actionbar);
	    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth()-10, leftDrawable.getMinimumHeight()-10);
	    this.doBtn.setCompoundDrawables(leftDrawable, null, null, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine_success, menu);
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
