package com.lecture.localdata;

import java.io.Serializable;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SubmitLecture implements Serializable {

	String title, speaker, timeNormal, campus, address, speaker_information,
			more_information, information_source;
	
	// edited by xianyu 2014 08 03 20:54 to get the user's phone info & number
	String phoneInfo;  // include phone's Model, SDK & OS
	String userEmail;

	public SubmitLecture() {

		title = "";
		speaker = "";
		timeNormal = "";
		campus = "";
		address = "";
		speaker_information = "";
		more_information = "";
		information_source = "";
	}
	
	public void setPhoneInfo(String phoneInfo){
		this.phoneInfo = phoneInfo;
		
	}
	public String getPhoneInfo(){
		String infoCopy = phoneInfo.toString();
		// xml禁止传输的五个字符，进行转换
		/*
		infoCopy.replace("&", "&amp");
		infoCopy.replace("'", "&apos");
		infoCopy.replace("\"", "&quot");
		infoCopy.replace(">", "&gt");
		infoCopy.replace("<", "&lt");
		*/
		return infoCopy;
	}
	public void setUserEmail(Context context){
		
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		
		this.userEmail = sharedPre.getString("email", "用户未填写!"); 
		
	}
	public String getUserEmail(){
		return userEmail;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setSpeaker(String speaker){
		this.speaker = speaker;
	}
	
	public String getSpeaker(){
		return speaker;
	}
	
	public void setTimeNormal(String time){
		this.timeNormal = time;
	}
	
	public String getTimeNormal(){
		return timeNormal;
	}
	
	public String getTimeAsLong(){
		//把时间转换成long型的值，存于wp_postmeta表中
		String[] strings = null;
		strings = timeNormal.split("-| |:");
		for (String string : strings) {
			Log.i("Split", string);
		}
		Calendar calendar = Calendar.getInstance();
		
		Log.i("calendar setting", "in SubmitLecture.java");
		
		if(Integer.parseInt(strings[3]) < 12){
			calendar.set(Calendar.AM_PM, 0);
			calendar.set( Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1,
					Integer.parseInt(strings[2]), Integer.parseInt(strings[3]), Integer.parseInt(strings[4]), 0 );
		}
		else{
			calendar.set(Calendar.AM_PM, 1);
			calendar.set( Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1,
					Integer.parseInt(strings[2]), Integer.parseInt(strings[3]) - 12, Integer.parseInt(strings[4]), 0 );
		}
		
		Log.i("Calendar time", calendar.toString());
		Log.i("Calendar long", String.format("%d", calendar.getTimeInMillis()) );
		
		return String.format("%d", calendar.getTimeInMillis()).substring(0, 10);

		
		
		
	}
	public void setCampus(String campus){
		this.campus = campus;
	}
	
	public String getCampus(){
		return campus;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public String getAddress(){
		return address;
	}
	
	public void setSpeaker_information(String speaker_information){
		this.speaker_information = speaker_information;
	}
	
	public String getSpeaker_information(){
		return speaker_information;
	}
	
	public void setMore_information(String more_information){
		this.more_information = more_information;
	}
	
	public String getMore_information(){
		return more_information;
	}
	
	public void setInformation_source(String information_source){
		this.information_source = information_source;
	}
	
	public String getInformation_source(){
		return information_source;
	}
}
