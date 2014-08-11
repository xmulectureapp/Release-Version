package com.lecture.localdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailInfo implements Serializable {

	private String uid;
	private String lec_aboutSpeaker;
	private String lec_about;
	private List<String> kinds;
	private List<String> keyWords;

	public DetailInfo() {
		uid = "";
		lec_aboutSpeaker = "";
		lec_about = "";
		kinds = new ArrayList<String>();
		keyWords = new ArrayList<String>();
	}
	public DetailInfo(String uid) {
		this.uid = uid;
		lec_aboutSpeaker = "";
		lec_about = "";
		kinds = new ArrayList<String>();
		keyWords = new ArrayList<String>();
	}

	public String getLec_aboutSpeaker() {
		return lec_aboutSpeaker;
	}

	public void setLec_aboutSpeaker(String aboutSpeaker) {
		if(aboutSpeaker == null || aboutSpeaker.equals(""))
			aboutSpeaker = "没有更多讲者信息！";
		
		this.lec_aboutSpeaker = aboutSpeaker;
	}

	public String getLec_about() {
		return lec_about;
	}

	public void setLec_about(String about) {
		
		if(about == null || about.equals(""))
			about = "没有更多讲座相关消息！";
		
		this.lec_about = about;
	}

	public List<String> getKinds() {
		return kinds;
	}

	public void setKinds(List<String> kinds) {
		this.kinds = kinds;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
	}
}
