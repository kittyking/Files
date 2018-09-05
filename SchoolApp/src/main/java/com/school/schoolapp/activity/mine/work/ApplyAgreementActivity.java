package com.school.schoolapp.activity.mine.work;

import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class ApplyAgreementActivity extends BaseActivity {
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_agreement);
		
		this.titleTextView.setText("合作协议");
		
		webView = (WebView) findViewById(R.id.web);
		webView.loadUrl("http://app.wuque-store.com/index.php?m=home&c=news&a=ceo");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.apply_agreement, menu);
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
