package com.lecture.localdata;

import java.io.Serializable;

import android.util.Log;

public class Comment implements Serializable{

	private String uid; // 标识讲座
	private String userName; // 用户名，没有设置就为 “无名的听者”
	private String userComment;// 用户的评论
	private String commentDate;// 评论的日期
	private String userEmail;

	public Comment(String uid) {
		
		this.uid = uid;
		
	}

	public Comment() {

		userName = "#17   xianyu";
		userComment = "A Test Comment!";
		commentDate = "2014年8月17日 17:00";

	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getCommentDate() {
		return commentDate;
	}
	
	public String getCommentDateCustomize(){
		//返回定制的时间格式
		String[] strings = null;
		strings = commentDate.split("-| |:");
		
		if(strings.length == 6)
			return strings[0] + "年" + strings[1] + "月" + strings[2] + "日 " + strings[3] + ":" + strings[4];
		else {
			//时间转换失败，染回默认值
			return commentDate;
		}
		
	}
	
	public void setEmail(String userEmail){
		this.userEmail = userEmail;
	}
	public String getEmail(){
		return userEmail;
	}
	

}
