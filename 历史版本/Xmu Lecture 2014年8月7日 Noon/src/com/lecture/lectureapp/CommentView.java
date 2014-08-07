package com.lecture.lectureapp;

import com.lecture.localdata.Comment;
import com.lecture.localdata.Event;
import com.lecture.localdata.SubmitLecture;
import com.lecture.util.SubmitCommentInterface;
import com.lecture.util.SubmitCommentInterface.SubmitCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommentView extends Activity {

	private EditText commentEditText;
	private Button commentButton;
	private Event toCommentEvent;
	private Comment comment;
	private String commentReplay;
	
	
	//——————————————————————下面是用于handler的消息标记
			private static final int MESSAGE_SUBMIT_START = 1;//刷新开始
			private static final int MESSAGE_SUBMIT_END = 2;//刷新结束
			private static final int MESSAGE_SUBMIT_FAILED = 3;//刷新失败
			
			private ProgressDialog mProgressDialog;
			
			private Handler submitHandler = new Handler(){
				
				@Override
				public void handleMessage(Message message){
					final String msg = (String) message.obj;
					Message msgRepost = Message.obtain();
					
					if(message.what == MESSAGE_SUBMIT_START){
						
						mProgressDialog = ProgressDialog.show(CommentView.this,
								"请稍后", msg, true, false);
						
						
					}// end start
					else if(message.what == MESSAGE_SUBMIT_END){
						if (mProgressDialog != null) {
							mProgressDialog.dismiss();
							mProgressDialog = null;
						}
						
						Toast.makeText(getApplicationContext(), "评论成功！",Toast.LENGTH_LONG).show();
						//评论成功后返回,返回前先设置返回的参数
						Intent intent = new Intent(); 
		                /*
						intent.putExtra( "username", comment.getUserName() );// 放入返回值
		                intent.putExtra( "commentdate", comment.getCommentDate() );
		                intent.putExtra( "usercomment", comment.getUserComment() );
		                */
						
		                intent.putExtra("returnComment", comment);
		                
		                setResult(1, intent);// 放入回传的值,并添加一个Code,方便区分返回的数据 
						
						CommentView.this.finish();
						
						
						//下面进行输入框的文本清除
						//clearAllBlanks();
						
						
					}//end end
					else if(message.what == MESSAGE_SUBMIT_FAILED){
						if (mProgressDialog != null) {
							mProgressDialog.dismiss();
							mProgressDialog = null;
						}
						
						Toast.makeText(getApplicationContext(), "评论失败，请检查网络连接！",Toast.LENGTH_LONG).show();
						
						
					}// end failed
					
				}
				
				
				
			};  // end handler
			
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_view);
		
		toCommentEvent = (Event) getIntent().getSerializableExtra(
				"LectureComment");
		
		commentReplay = (String)getIntent().getStringExtra("commentReply");
		
		//下面这句时为了避免从hotCenter那主要的三个Center点击评论而没有设置commentReply引起的错误
		if(commentReplay == null)
			commentReplay = "";
		
		commentEditText = (EditText)findViewById(R.id.comment_editText);
		commentButton   = (Button)findViewById(R.id.comment_button);
		
		if( ! commentReplay.isEmpty() )
			commentEditText.setHint( commentReplay + "请输入回复内容" );
		
		commentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(  isValidComment( commentEditText.getText().toString() )  ){
					
					//评论合法，准备开始提交评论
					//get user info
					SharedPreferences sharedPre = CommentView.this.getSharedPreferences("config",
							CommentView.this.MODE_PRIVATE);
					String userName  = sharedPre.getString("username", "无名的听者");
					if( userName.equals("") )
						userName = "无名的听者";
					String userEmail = sharedPre.getString("email", "出现为设置邮箱即可评论的错误，请联系-咸鱼!");
					//set date
					Time time = new Time();
					time.setToNow();
					String commentDate = String.format("%d", time.year) + "-" + String.format("%02d", time.month + 1) + "-" + String.format("%02d", time.monthDay) +
							" " + String.format("%02d", time.hour) + ":" + String.format("%02d", time.minute) + ":" + String.format("%02d", time.second);
					
					comment = new Comment();
					comment.setUid(toCommentEvent.getUid());
					comment.setUserName(userName);
					comment.setUserComment( commentReplay + " " + commentEditText.getText().toString() );
					comment.setCommentDate(commentDate);
					comment.setEmail(userEmail);
					
					//submit 
					submitComment(comment);
					
					
				}
				
				
				
			}
		});
		

	}// end onCreate()
	
	public Boolean isValidComment(String userComment){
		
		return true;
		
	}
	
	public void submitComment(Comment comment){
		
		submitGo( generateXML(comment) );
		
		
		
		
		
	}
	
	public String generateXML(Comment comment){
		
		String xmlToSubmit = 
				"%3CsubmitCommentXML%3E" +
					"%3Clectureuid%3E<![CDATA[" + charConvert( comment.getUid() ) + "]]>%3C/lectureuid%3E" +
					"%3Cusername%3E<![CDATA[" + charConvert( comment.getUserName() ) + "]]>%3C/username%3E" +
					"%3Cusercomment%3E<![CDATA[" + charConvert( comment.getUserComment() ) + "]]>%3C/usercomment%3E" +
					"%3Cuseremail%3E<![CDATA[" + charConvert( comment.getEmail() ) + "]]>%3C/useremail%3E" +
				"%3C/submitCommentXML%3E";
		
		
		return xmlToSubmit;
		
	}
	
public void submitGo(String xmlToSubmit){
		

		SubmitCommentInterface submitInterface = SubmitCommentInterface
				.getInstance(new SubmitCallback() {

					@Override
					public void onStart() {
						Message msg = new Message();
						msg.what = MESSAGE_SUBMIT_START;
						msg.obj = "正在提交...";
						submitHandler.sendMessage(msg);
					}

					@Override
					public void onEnd() {
						
						
						Log.i("提交讲座", "提交成功！");
						
						Message msg = new Message();
						msg.what = MESSAGE_SUBMIT_END;
						msg.obj = "提交成功！";
						
						submitHandler.sendMessage(msg);
								
					}

					@Override
					public void onNoInternet() {
						Message msg = new Message();
						msg.what = MESSAGE_SUBMIT_FAILED;
						msg.obj = "无法连接到网络...";
						submitHandler.sendMessage(msg);
					}
				});
		submitInterface.SubmitGo(xmlToSubmit);
		
	}
	
public String charConvert(String toConvertString){
		
		
		toConvertString.replace(" ","%20");
		toConvertString.replace("&", "%26");
		toConvertString.replace("<", "%8b");
		toConvertString.replace("]", "%5d");
		toConvertString.replace("[", "%5b");
		toConvertString.replace("\\", "%5c");
		
		return toConvertString;
		
	}
	

	public CommentView() {

	}

}
