package com.lecture.lectureapp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.lecture.DBCenter.DBCenter;
import com.lecture.DBCenter.XMLToList;
import com.lecture.localdata.DetailInfo;
import com.lecture.localdata.Event;
import com.lecture.localdata.ReminderInfo;
import com.lecture.util.GetDetailUtil;
import com.lecture.util.LikeInterface;
import com.lecture.util.GetDetailUtil.GetDetailCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LectureDetail extends Activity {

	private TextView tvname;
	private TextView tvtime;
	private TextView tvaddr;
	private TextView tvspeaker;
	private TextView tvlabel;
	private TextView tvaboutspeaker;
	private TextView tvmore;
	private ImageView iv1;
	
	private Event detail_lectureEvent; 

	private LinearLayout linearBtnShare;
	private LinearLayout linearBtnComment;
	private LinearLayout linearBtnLike;
	private LinearLayout linearBtnRemind;

	private ImageView imageViewLike;
	private ImageView imageViewRemind;

	private TextView textViewLike;
	private TextView textViewRemind;
	
	//——————————————————————下面是用于handler的消息标记
	private static final int MESSAGE_DOWNLOAD_START = 1;//刷新开始
	private static final int MESSAGE_DOWNLOAD_END = 2;//刷新结束
	private static final int MESSAGE_DOWNLOAD_FAILED = 3;//刷新失败
	
	
	private ProgressDialog mProgressDialog;
	
	private Handler detailHandler = new Handler(){
		
		@Override
		public void handleMessage(Message message){
			//final String msg = (String) message.obj;
			Message msgRepost = Message.obtain();
			
			if(message.what == MESSAGE_DOWNLOAD_START){
				
				
				
			}// end start
			else if(message.what == MESSAGE_DOWNLOAD_END){
				
				
				DetailInfo detailInfo = (DetailInfo)message.obj;
				
				setDetail(detailInfo);
				
				
				
				
			}//end end
			else if(message.what == MESSAGE_DOWNLOAD_FAILED){
				
				
				
			}// end failed
			
		}
		
		
		
	};  // end handler
		
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lecture_detail);
		
		tvname = (TextView)findViewById(R.id.detail_lecture_name);
		tvtime = (TextView)findViewById(R.id.detail_lecture_time);
		tvaddr = (TextView)findViewById(R.id.detail_lecture_addr);
		tvspeaker = (TextView)findViewById(R.id.detail_lecture_speaker);
		tvlabel = (TextView)findViewById(R.id.detail_lecture_label);
		tvaboutspeaker = (TextView)findViewById(R.id.about_speaker);
		tvmore = (TextView)findViewById(R.id.more_information);
		
		detail_lectureEvent = (Event)getIntent().getSerializableExtra("LectureDetail");
		
		tvname.setText("主题：" + detail_lectureEvent.getTitle());
		tvtime.setText("时间：" + detail_lectureEvent.getTime());
		tvaddr.setText("地点：" + detail_lectureEvent.getAddress());
		tvspeaker.setText("主讲：" + detail_lectureEvent.getSpeaker());
		tvlabel.setText("标签：" + "标签还在开发中，敬请期待！");
		//tvaboutspeaker.setText("常耀信，博士，教授，博士生导师，现任教于南开大学及美国关岛大学，研究方向为英美文学。1965年毕业于南开大学英文系，同年赴英国伦敦、剑桥进修。1984年在美国坦普尔大学英文系获博士学位。自1986年起为美国《文化》(Paideuma)杂志特邀编辑。1988年入选英国国际传记中心编纂的《远东及太平洋名人录》，多次入选《美国名师录》。著有《美国文学简史》、《英文学简史》、《美国文学史》、《希腊罗马神话》、《漫话英美文学》及《研究与写作》等；主编有《美国文学选读及《英国文学通史》等。在国内外刊物上发表过多篇论文。");
		//tvmore.setText("人文学科的研究方法系列讲座第17期");
		tvaboutspeaker.setText("主讲资料：" + "主讲信息加载中...");
		tvmore.setText("讲座背景及更多消息：" + "讲座详情加载中...");
		
		
		//下面开始初始化四个按钮
		linearBtnShare = (LinearLayout)findViewById(R.id.detail_linearlayout_share);
		linearBtnComment = (LinearLayout)findViewById(R.id.detail_linearlayout_comment);
		linearBtnLike = (LinearLayout)findViewById(R.id.detail_linearlayout_like);
		linearBtnRemind = (LinearLayout)findViewById(R.id.detail_linearlayout_remind);
		
		imageViewLike = (ImageView)findViewById(R.id.detail_like_icon);
		imageViewRemind = (ImageView)findViewById(R.id.detail_remind_icon);
		
		textViewLike = (TextView)findViewById(R.id.detail_like_text);
		textViewRemind = (TextView)findViewById(R.id.detail_remind_text);
		
		
		  if(detail_lectureEvent.isLike()){
			  imageViewLike.setImageDrawable(getResources().getDrawable(R.drawable.like_red));
			  textViewLike.setTextColor(getResources().getColor(R.color.main_menu_pressed));
			  
		  }
		  else {
			  imageViewLike.setImageDrawable(getResources().getDrawable(R.drawable.like));
			  textViewLike.setTextColor(getResources().getColor(R.color.item_content));
			  
		  }
		  
		  if(detail_lectureEvent.getLikeCount() > 0){
			  textViewLike.setText( adaptPlace( String.format("%d",detail_lectureEvent.getLikeCount()) ) );
			  }
		  
		//下面的代码使用于 收藏按钮
		  if(detail_lectureEvent.isReminded()){
			  imageViewRemind.setImageDrawable(getResources().getDrawable(R.drawable.remind_red));
			  textViewRemind.setTextColor(getResources().getColor(R.color.main_menu_pressed));
	    		
		  }
		  else {
			  imageViewRemind.setImageDrawable(getResources().getDrawable(R.drawable.remind));
			  
		  }
		  
		  linearBtnShare.setOnClickListener(new View.OnClickListener() {  
			    public void onClick(View v) {  
			     //showInfo1();
			    	Event event = detail_lectureEvent;
			    	Intent sendIntent = new Intent();
					sendIntent.setAction(Intent.ACTION_SEND);
					//sendIntent.setType("text/plain");
					sendIntent.setType("image/jpg");
					sendIntent.putExtra(Intent.EXTRA_TITLE, "分享");
					sendIntent.putExtra(Intent.EXTRA_TEXT,
							"Hi，跟你分享一个有趣的讲座。" + "\n"
							+ "主题：" + event.getTitle()+ "\n" 
							+ "时间：" + event.getTime()+"\n" 
							+ "地点：" + event.getAddress()+ "\n" 
							+ "主讲：" + event.getSpeaker() + "\n"
							+ "（来自厦大讲座网）" + "\n"
							+ event.getLink());
					//sendIntent.putExtra(Intent.EXTRA_TEXT, event.getAddress());
					//sendIntent.putExtra(Intent.EXTRA_TEXT, event.getSpeaker());
					sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			
					LectureDetail.this.startActivity(sendIntent);
			    	
			    }  
			   });  
		
		  linearBtnComment.setOnClickListener(new View.OnClickListener() {  
			    public void onClick(View v) {  
			  //   showInfo2();
			    	 Event event = detail_lectureEvent;
					//把该则讲座对应的event传入Bundle，来自KunCheng
					Bundle detail_bundle = new Bundle();
					detail_bundle.putSerializable("LectureComment", event);
					Intent intent = new Intent(LectureDetail.this, Comment.class);
					intent.putExtras(detail_bundle);
					LectureDetail.this.startActivity(intent);
			    	
			    	
			    }  
			   });
		
		linearBtnLike.setOnClickListener(new View.OnClickListener() {  
		    public void onClick(View v) {  
		    // showInfo3();
		    	Event event = detail_lectureEvent;
 
		    	event.setLike(!event.isLike());
		    	if (event.isLike())
		    	{
		    		imageViewLike.setImageDrawable(getResources().getDrawable(R.drawable.like_red));
		    		textViewLike.setTextColor(getResources().getColor(R.color.main_menu_pressed));
		    		//喜欢的话，进行数据表LikeTable更新
		    		DBCenter.setLike(DBCenter.getStaticDBCenter(LectureDetail.this).getReadableDatabase(), event.getUid(), true);
		    		event.updateLikeCount(1);//1 表示＋1
		    		LikeInterface.LikeGo(event.getUid(), "1");
		    		DBCenter.likeDBSync(DBCenter.getStaticDBCenter(LectureDetail.this).getReadableDatabase(), event.getUid(), "1");
		    		//下面一句解决马上变Like数字
		    		textViewLike.setText( adaptPlace( String.format("%d", event.getLikeCount()) ) );
		    		
		    	}
				else
				{
					imageViewLike.setImageDrawable(getResources().getDrawable(R.drawable.like));
					textViewLike.setTextColor(getResources().getColor(R.color.main_menu_normal));
					//喜欢的话，进行数据表LikeTable更新
		    		DBCenter.setLike(DBCenter.getStaticDBCenter(LectureDetail.this).getReadableDatabase(), event.getUid(), false);
		    		event.updateLikeCount(-1);//-1 表示＋ (-1)
		    		LikeInterface.LikeGo(event.getUid(), "0");
		    		DBCenter.likeDBSync(DBCenter.getStaticDBCenter(LectureDetail.this).getReadableDatabase(), event.getUid(), "0");
		    		//下面一句解决马上变Like数字
		    		textViewLike.setText( adaptPlace( String.format("%d", event.getLikeCount()) ) );
		    		
		    		
				}
		    }  
		   });
		
		linearBtnRemind.setOnClickListener(new View.OnClickListener() {  
			    public void onClick(View v) {  
			     //showInfo4();    
			    	Event event = detail_lectureEvent;
			    	event.setReminded(!event.isReminded());
			    	if (event.isReminded())
			    	{
			    		imageViewRemind.setImageDrawable(v.getResources().getDrawable(R.drawable.remind_red));
			    		textViewRemind.setTextColor(v.getResources().getColor(R.color.main_menu_pressed));
			    		//收藏的话，进行数据表CollectionTable更新
			    		DBCenter.setRemind(DBCenter.getStaticDBCenter(LectureDetail.this).getReadableDatabase(), event.getUid(), true);
			    		//添加到日历
			    		insertIntoCalender();
			    	}
			    	else
			    	{
			    		imageViewRemind.setImageDrawable(v.getResources().getDrawable(R.drawable.remind));
			    		textViewRemind.setTextColor(v.getResources().getColor(R.color.main_menu_normal));
			    		//收藏的话，进行数据表CollectionTable更新
			    		DBCenter.setRemind(DBCenter.getStaticDBCenter(LectureDetail.this).getReadableDatabase(), event.getUid(), false);
			    		//从日历删除
			    		deleteFromCalender();
			    	}	
			    }  
			   });
		
	
		
		//获取讲座更多信息
		downloadDetail();
	}
	
	//下面用于给按赞添加一个空格如果只有个位数的话
			public String adaptPlace(String s){
				
				if(s.length() == 1)
					return " " + s + "  ";
				else if(s.length() == 2)
					return " " + s + " ";
				else
					return s;
				
				
			}
	
	
	public void setDetail(DetailInfo detailInfo){
		tvaboutspeaker.setText( "主讲资料：\n★" + detailInfo.getLec_aboutSpeaker() );
		tvmore.setText( "讲座背景及更多消息：\n★" + detailInfo.getLec_about() );
		
		
	}
	
	
	
	
	public void downloadDetail(){
		

		GetDetailUtil getDetailUtil = GetDetailUtil
				.getInstance(new GetDetailCallback() {

					@Override
					public void onStart() {
						Message msg = new Message();
						msg.what = MESSAGE_DOWNLOAD_START;
						msg.obj = "正在下载详情...";
						detailHandler.sendMessage(msg);
					}

					@Override
					public void onEnd(DetailInfo detailInfo) {
						
						
						Log.i("获取详细信息", "获取结束！准备发送MESSAGE_DOWNLOAD_END 消息！");
						
						Message msg = new Message();
						msg.what = MESSAGE_DOWNLOAD_END;
						
						msg.obj = detailInfo;
						
						
						
						detailHandler.sendMessage(msg);
								
					}

					@Override
					public void onNoInternet() {
						Message msg = new Message();
						msg.what = MESSAGE_DOWNLOAD_FAILED;
						msg.obj = "无法连接到网络...";
						detailHandler.sendMessage(msg);
					}
				});
		getDetailUtil.getDetail( this, detail_lectureEvent.getUid() );
		
	}
	
	public void insertIntoCalender() {
		long calId = 1;
        Event event = detail_lectureEvent;
		GregorianCalendar startDate = event.getTimeCalendar();
		GregorianCalendar endDate = event.getTimeCalendar();
		
		startDate.set(Calendar.HOUR_OF_DAY, 8);
		startDate.set(Calendar.MINUTE, 0);

		ContentResolver cr1 = this.getContentResolver(); // 添加新event，步骤是固定的
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startDate.getTime().getTime()); // 添加提醒时间，即讲座的日期
		values.put(Events.DTEND, endDate.getTime().getTime());
		values.put(Events.TITLE, event.getTitle());
		values.put(Events.DESCRIPTION, event.getSpeaker());
		values.put(Events.EVENT_LOCATION, event.getAddress());
		values.put(Events.CALENDAR_ID, calId);
		values.put(Events.EVENT_TIMEZONE, "GMT+8");
		Uri uri = cr1.insert(Events.CONTENT_URI, values);
		Long eventId = Long.parseLong(uri.getLastPathSegment()); // 获取刚才添加的event的Id

		ContentResolver cr2 = this.getContentResolver(); // 为刚才新添加的event添加reminder
		ContentValues values1 = new ContentValues();
		values1.put(Reminders.MINUTES, 10); // 设置提前几分钟提醒
		values1.put(Reminders.EVENT_ID, eventId);
		values1.put(Reminders.METHOD, Reminders.METHOD_ALERT);  //设置该事件为提醒
		Uri newReminder = cr2.insert(Reminders.CONTENT_URI, values1); // 调用这个方法返回值是一个Uri
		long reminderId = Long.parseLong(newReminder.getLastPathSegment());

		// 记录数据
		event.setReminderInfo(new ReminderInfo(eventId, reminderId));

		// setAlarmDeal(startMillis); //
		// 设置reminder开始的时候，启动另一个activity
		// // 设置全局定时器
		// Intent intent = new Intent(this,
		// AlarmActivity.class);
		// PendingIntent pi =
		// PendingIntent.getActivity(this, 0, intent, 0);
		// AlarmManager aManager = (AlarmManager)
		// getSystemService(Service.ALARM_SERVICE);
		// aManager.set(AlarmManager.RTC_WAKEUP, time, pi);
		// //
		// 当系统调用System.currentTimeMillis()方法返回值与time相同时启动pi对应的组件

		Toast.makeText(this, "添加到 收藏页面&日历 成功", Toast.LENGTH_SHORT).show();
	}

	public void deleteFromCalender() {
		Event event = detail_lectureEvent;
		Uri deleteReminderUri = null;
		Uri deleteEventUri = null;
		deleteReminderUri = ContentUris.withAppendedId(Reminders.CONTENT_URI,
				event.getReminderInfo().getReminderId());
		deleteEventUri = ContentUris.withAppendedId(Events.CONTENT_URI, event
				.getReminderInfo().getEventId());
		int rowR = this.getContentResolver().delete(deleteReminderUri,
				null, null);
		int rowE = this.getContentResolver().delete(deleteEventUri, null,
				null);
		if (rowE > 0 && rowR > 0) {
			Toast.makeText(this, "从 收藏页面&日历 删除成功", Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(this, "从 收藏页面&日历 删除失败", Toast.LENGTH_SHORT).show();
	}

}


