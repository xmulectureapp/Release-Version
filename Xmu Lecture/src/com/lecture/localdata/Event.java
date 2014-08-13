package com.lecture.localdata;

import java.io.Serializable;
import java.util.GregorianCalendar;

import android.R.integer;


public class Event implements Serializable {


	private String uid;
	private String title;
	private String address;
	private String time;
	private String link;
	private String speaker;
	private boolean isCalender;
	private boolean isLike;
	private boolean isReminded;
	private DetailInfo detailInfo;
	private ReminderInfo reminderInfo;
	
	//new interface
	private int likeCount;

	public Event() {
		uid = "";
		title = "";
		address = "";
		time = "";
		link = "";
		isCalender = false;
		isLike = false;
		isReminded = false;
		detailInfo = new DetailInfo();
		reminderInfo = new ReminderInfo(0, 0);
		likeCount = 0;
	}
	
	public Event(String id) {
		this();
		uid = id;
	}
	
	public int getLikeCount(){
		return likeCount;
	}

	public void updateLikeCount(int newValue){
		likeCount += newValue;
	}
	public void setLikeCount(String newValue){
		
		likeCount = Integer.parseInt(newValue);
	}
	

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		
		this.uid = uid;
	}
	public String trimUid(String uid) {
		uid=uid.trim();  
		String uid1="";  
		if(uid != null && !"".equals(uid))
		{  
			for(int i=0;i<uid.length();i++)
			{  
				if(uid.charAt(i)>=48 && uid.charAt(i)<=57)
				{  
					uid1+=uid.charAt(i);  
				}
			}
		}
		return uid1;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTime() {
		return time;
	}

	public GregorianCalendar getTimeCalendar() {
		GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(time
				.substring(0, 4)), Integer.parseInt(time.substring(5, 7)) - 1,
				Integer.parseInt(time.substring(8, 10)), Integer.parseInt(time
						.substring(11, 13)), Integer.parseInt(time.substring(
						14, 16)));
		return calDate;
	}
	
	public String getCustomTime(){
		
		String[] timeStrings = getTime().split("-| |:");
		String weekDay = "";
		
		switch (getTimeCalendar().DAY_OF_WEEK) {
		case 1:
			weekDay = "星期日";
			break;
		case 2:
			weekDay = "星期一";
			break;
		case 3:
			weekDay = "星期二";
			break;
		case 4:
			weekDay = "星期三";
			break;
		case 5:
			weekDay = "星期四";
			break;
		case 6:
			weekDay = "星期五";
			break;
		case 7:
			weekDay = "星期六";
			break;
		default:
			break;
		}
		
		
		return timeStrings[0] + "年" + timeStrings[1] + "月" + timeStrings[2] + "日(" + weekDay + ") " + timeStrings[3] + ":" + timeStrings[4];
		
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public DetailInfo getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(DetailInfo detailInfo) {
		this.detailInfo = detailInfo;
	}

	public boolean isCalender() {
		return isCalender;
	}

	public void setCalender(boolean isCalender) {
		this.isCalender = isCalender;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}
	
	public boolean isReminded(){
		return isReminded;
	}
	
	public void setReminded(boolean isReminded){
		this.isReminded = isReminded;
	}

	public ReminderInfo getReminderInfo() {
		return reminderInfo;
	}

	public void setReminderInfo(ReminderInfo reminderInfo) {
		this.reminderInfo = reminderInfo;
	}

	public Event merger(Event data2) {
		this.isLike = data2.isLike;
		this.isCalender = data2.isCalender;
		this.detailInfo = data2.detailInfo;
		this.reminderInfo = data2.reminderInfo;
		return this;
	}

	@Override
	public String toString() {
		return "讲座名称：" + title + "\n讲座地址：" + address + "\n讲座时间：" + time
				+ "\n主讲人：" + speaker;
	}
}
