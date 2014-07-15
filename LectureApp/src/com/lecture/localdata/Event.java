package com.lecture.localdata;

import java.io.Serializable;
import java.util.GregorianCalendar;


public class Event implements Serializable {


	private String uid;
	private String title;
	private String address;
	private String time;
	private String link;
	private String speaker;
	private boolean isCalender;
	private boolean isLove;
	private DetailInfo detailInfo;
	private ReminderInfo reminderInfo;

	public Event() {
		uid = "";
		title = "";
		address = "";
		time = "";
		link = "";
		isCalender = false;
		isLove = false;
		detailInfo = new DetailInfo();
		reminderInfo = new ReminderInfo(0, 0);
	}

	public Event(String id) {
		this();
		uid = id;
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

	public boolean isLove() {
		return isLove;
	}

	public void setLove(boolean isLove) {
		this.isLove = isLove;
	}

	public ReminderInfo getReminderInfo() {
		return reminderInfo;
	}

	public void setReminderInfo(ReminderInfo reminderInfo) {
		this.reminderInfo = reminderInfo;
	}

	public Event merger(Event data2) {
		this.isLove = data2.isLove;
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
