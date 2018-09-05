package com.school.schoolapp.fragment.alert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.school.schoolapp.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements  
	DatePickerDialog.OnDateSetListener{

	private String dateStr;
	private TextView text;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();  
        int year = c.get(Calendar.YEAR);  
        int month = c.get(Calendar.MONTH);  
        int day = c.get(Calendar.DAY_OF_MONTH);  
        return new DatePickerDialog(getActivity(), this, year, month, day); 
	}
	public DatePickerFragment(){

	}
	public static DatePickerFragment newInstance(TextView txt) {
		DatePickerFragment  newFragment = new DatePickerFragment();
		newFragment.text = txt;
		return newFragment;

	}

	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		Log.d("OnDateSet", "select year:"+year+";month:"+monthOfYear+";day:"+dayOfMonth);
		Date date = new Date(year-1900, monthOfYear, dayOfMonth);
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
		this.dateStr=dateformat.format(date);
		text.setText(dateStr);
	}
	public String getDate(){
		return this.dateStr;
	}
}
