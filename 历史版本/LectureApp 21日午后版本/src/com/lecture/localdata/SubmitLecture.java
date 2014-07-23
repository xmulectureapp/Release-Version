package com.lecture.localdata;

import java.io.Serializable;

public class SubmitLecture implements Serializable {

	String title, speaker, time, campus, address, speaker_information,
			more_information, information_source;

	public SubmitLecture() {

		title = "";
		speaker = "";
		time = "";
		campus = "";
		address = "";
		speaker_information = "";
		more_information = "";
		information_source = "";
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
	
	public void setTime(String time){
		this.time = time;
	}
	
	public String getTime(){
		return time;
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
