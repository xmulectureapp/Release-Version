package com.lecture.localdata;

public class Comment {

	private String userName;   //用户名，没有设置就为 “无名的听者”
	private String userComment;//用户的评论
	private String commentDate;//评论的日期
	
	
	public Comment() {
		
		userName = "#17   xianyu";
		userComment = "A Test Comment!";
		commentDate = "2014年8月17日 17:00";

	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return userName;
	}
	
	public void setUserComment(String userComment){
		this.userComment = userComment;
	}
	public String getUserComment(){
		return "      " + userComment;//add 6 spaces
	}
	
	public void setCommentDate(String commentDate){
		this.commentDate = commentDate;
	}
	public String getCommentDate(){
		return commentDate;
	}

}
