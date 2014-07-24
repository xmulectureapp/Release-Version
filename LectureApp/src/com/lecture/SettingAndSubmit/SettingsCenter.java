package com.lecture.SettingAndSubmit;


import java.util.Calendar;
import java.util.Date;

import com.lecture.localdata.Event;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;

public class SettingsCenter {

	public Time time;
	public String weekdaySettings;//只存   1、2、3、4、5、6、7
	public String placeSettings;  //只存   思、漳、翔、厦
	public Calendar calendar;
	
	
	
	public SettingsCenter(Context context) {
		time = new Time();
		time.setToNow();
		
		Log.i("Settings Center", String.format("%d", time.weekDay) );
		Log.i("Date WeekDay", String.format("%d:%d", time.weekDay, Time.FRIDAY));
		
		calendar = Calendar.getInstance();
		
		
		//calendar.set(Calendar.YEAR, 2014);
		//calendar.set(Calendar.MONTH, 6);
		
		Log.i("日历时间1", calendar.toString());
		Log.i("Time和日历12", String.format("%d:%d", time.weekDay, calendar.get(Calendar.DAY_OF_WEEK)));
		calendar.set(Calendar.MONTH, 7);
		calendar.set(Calendar.DAY_OF_MONTH, 25);
		calendar.set(2014, 7, 25, 8, 37);
		
		Log.i("日历时间2", calendar.toString());
		if(calendar.get(Calendar.DAY_OF_WEEK) == 5)
			Log.i("日历", "YES");
		
		Log.i("Time和日历2最后", String.format("%d:%d", time.weekDay, calendar.get(Calendar.DAY_OF_WEEK)));
		
		SharedPreferences sharedPre = context.getSharedPreferences("config",context.MODE_PRIVATE);
		
		String weekdaySettings = "";
		weekdaySettings += sharedPre.getString("mon", "");
		weekdaySettings += sharedPre.getString("tue", "");
		weekdaySettings += sharedPre.getString("wed", "");
		weekdaySettings += sharedPre.getString("thu", "");
		weekdaySettings += sharedPre.getString("fri", "");
		weekdaySettings += sharedPre.getString("sat", "");
		weekdaySettings += sharedPre.getString("sun", "");
		
		String placeSettingString = "";
		placeSettingString += sharedPre.getString("siming", "");
		placeSettingString += sharedPre.getString("xiangan", "");
		placeSettingString += sharedPre.getString("zhangzhou", "");
		placeSettingString += sharedPre.getString("xiamen", "");
		Log.i("Settings Center", "weekday:" + weekdaySettings + " Place:" + placeSettingString);
		this.initSettings(weekdaySettings, placeSettingString);
		
	}
	public String getWeekdaySettings(){
		return weekdaySettings;
	}
	public String getPlaceSettings(){
		return placeSettings;
	}
	public void initSettings(String weekdaySettings, String placeSettings){
		this.weekdaySettings = weekdaySettings;
		this.placeSettings = placeSettings;
		
	}
	//时间转换
	public Time stringToTime(String timeString){
		
		String[] strings = null;
		strings = timeString.split("-| |:");
		for (String string : strings) {
			Log.i("Split", string);
		}
		//time = new Time("GMT+8");
		
		//  秒 分 时 天 月 年
		Log.i("TIME!!!!!", time.toString());
		Log.i("TIME!!!!!", time.timezone);
		Log.i("TIME!!!!!", String.format("%d", time.weekDay));
		time.set( 0, Integer.parseInt(strings[4]), Integer.parseInt(strings[3]),
				Integer.parseInt(strings[2]), Integer.parseInt(strings[1]), Integer.parseInt(strings[0]) );
		
		Log.i("TIME!!!!!", time.toString());
		Log.i("TIME!!!!!", String.format("%d", time.weekDay));
		
		return new Time(time);
		
	}
	
	public Boolean isNeededLecture(Event e){
		int weekday = stringToTime(e.getTime()).weekDay;
		if( weekdaySettings.contains(String.format("%d", weekday)) ||
				placeSettings.contains(e.getAddress().substring(0, 1)) )	
			return true;//如果weekSettings中存在该数字就返回true
		return false;
		
		
		
	}
	
	

}
