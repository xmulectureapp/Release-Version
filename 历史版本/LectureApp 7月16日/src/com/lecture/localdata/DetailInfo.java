package com.lecture.localdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailInfo implements Serializable {

	private String info;
	private String background;
	private List<String> kinds;
	private List<String> keyWords;

	public DetailInfo() {
		info = "";
		background = "";
		kinds = new ArrayList<String>();
		keyWords = new ArrayList<String>();
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
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
