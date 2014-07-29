package com.lecture.lectureapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lecture.DBCenter.DBCenter;
import com.lecture.lectureapp.R;
import com.lecture.lectureapp.SubscribeMyadapter.ViewHolder;
import com.lecture.localdata.Event;
import com.lecture.localdata.ReminderInfo;
import com.lecture.util.LikeInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SubscribeMyadapter extends BaseAdapter  
{
	  private LayoutInflater mInflater;  
	  private Context mContext;
	  private Event event;
	  private List<Event> mData; 
	  private DBCenter dbCenter;
	

	
	public final class ViewHolder
	{  
		  public TextView lectureName;  
		  public ImageView line1;  
		  public TextView lectureAddr;  
		  public TextView lectureTime; 
		  public TextView lectureSpeaker; 
		  public LinearLayout linearlayoutid;
		  public TextView lectureId;
		  public ImageView line2;  
		  public ImageView shareIcon; 
		  public TextView shareText; 
		  public ImageView line3; 
		  public ImageView commentIcon; 
		  public TextView commentText; 
		  public ImageView line4;
		  public ImageView likeIcon; 
		  public TextView likeText;
		  public ImageView line5;
		  public ImageView remindIcon; 
		  public TextView remindText;
		  public LinearLayout linearlayoutShare;
		  public LinearLayout linearlayoutComment;
		  public LinearLayout linearlayoutLike;
		  public LinearLayout linearlayoutRemind;
		  
	}
	//用于更新ListView时
	public void setMData(List<Event> list){
		
		mData = list;	
	}
	//用于设置DBCenter
	public void setDBCenter(DBCenter dbCenter){
		
		this.dbCenter = dbCenter;	
	}
	
	
	
	public SubscribeMyadapter(Context context)
	{  

		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);  
		
	}  
	    
	public SubscribeMyadapter(Context context, List<Event> list)
	{  

		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);  
		mData = list;
	}  

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			//if (convertView == null)
			//{
				holder=new ViewHolder(); 
				convertView = mInflater.inflate(R.layout.item, null);
				holder.lectureName = (TextView)convertView.findViewById(R.id.lecture_name); 
				holder.line1 = (ImageView)convertView.findViewById(R.id.line_1);
				holder.lectureTime = (TextView)convertView.findViewById(R.id.lecture_time);
				holder.lectureAddr = (TextView)convertView.findViewById(R.id.lecture_addr); 
				holder.linearlayoutid = (LinearLayout)convertView.findViewById(R.id.linearlayout_id);
				holder.lectureSpeaker = (TextView)convertView.findViewById(R.id.lecture_speaker);
				holder.lectureId = (TextView)convertView.findViewById(R.id.lecture_id); 
				holder.line2 = (ImageView)convertView.findViewById(R.id.line_2);
				holder.shareIcon = (ImageView)convertView.findViewById(R.id.share_icon);
				holder.shareText = (TextView)convertView.findViewById(R.id.share_text);
				holder.line3 = (ImageView)convertView.findViewById(R.id.line_3);
				holder.commentIcon = (ImageView)convertView.findViewById(R.id.comment_icon);
				holder.commentText = (TextView)convertView.findViewById(R.id.comment_text);
				holder.line4 = (ImageView)convertView.findViewById(R.id.line_4);
				holder.likeIcon = (ImageView)convertView.findViewById(R.id.like_icon);
				holder.likeText = (TextView)convertView.findViewById(R.id.like_text);
				holder.line5 = (ImageView)convertView.findViewById(R.id.line_5);
				holder.remindIcon = (ImageView)convertView.findViewById(R.id.remind_icon);
				holder.remindText = (TextView)convertView.findViewById(R.id.remind_text);
				holder.linearlayoutShare = (LinearLayout)convertView.findViewById(R.id.linearlayout_share);
				holder.linearlayoutComment = (LinearLayout)convertView.findViewById(R.id.linearlayout_comment);
				holder.linearlayoutLike = (LinearLayout)convertView.findViewById(R.id.linearlayout_like);
				holder.linearlayoutRemind = (LinearLayout)convertView.findViewById(R.id.linearlayout_remind);
				convertView.setTag(holder); 
			//}
			//else
			//{
				holder = (ViewHolder)convertView.getTag(); 
			//}
			  holder.lectureName.setText(mData.get(position).getTitle());  
			  holder.lectureTime.setText("时间: " + mData.get(position).getTime()); 
			  holder.lectureAddr.setText("地点: " + mData.get(position).getAddress());  
			  holder.lectureSpeaker.setText("主讲: " + mData.get(position).getSpeaker()); 
			  holder.lectureId.setText(mData.get(position).getUid());
			  
			  final ImageView likeIcon_change = holder.likeIcon;
			  final TextView likeText_change = holder.likeText;
			  final ImageView remindIcon_change = holder.remindIcon;
			  final TextView remindText_change = holder.remindText;
			  event = mData.get(position);
			  
			  //下面的代码用于实现点赞后消失的BUG,Yao,  尼玛，看来你还是不行呀！还是放弃吧！^ ^
			  if(mData.get(position).isLike()){
				  holder.likeIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.like_red));
				  holder.likeText.setTextColor(convertView.getResources().getColor(R.color.main_menu_pressed));
				  
			  }
			  else {
				  holder.likeIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.like));
				  holder.likeText.setTextColor(convertView.getResources().getColor(R.color.item_content));
				  
			  }
			  //按赞BUG以解决，You still have a long way to go, no a lot BUGs to go! ahahaha!
			 if(mData.get(position).getLikeCount() > 0){
				 holder.likeText.setText( adaptPlace( String.format("%d", mData.get(position).getLikeCount()) ) );
				 
		    		 
			 }
				//holder.likeText.setText(mData.get(position).getLikeCount()); //No package identifier when getting value for resource  
			  //下面的代码使用于 收藏按钮
			  if(mData.get(position).isReminded()){
				  holder.remindIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.remind_red));
				  remindText_change.setTextColor(convertView.getResources().getColor(R.color.main_menu_pressed));
			  }
			  else {
				  holder.remindIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.remind));
				  
			  }
			  
		
			 holder.linearlayoutShare.setOnClickListener(new View.OnClickListener() {  
				    public void onClick(View v) {  
				     //showInfo1();
				    	event = mData.get(position);
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
				
						mContext.startActivity(sendIntent);
				    	
				    }  
				   });  
			 holder.linearlayoutComment.setOnClickListener(new View.OnClickListener() {  
				    public void onClick(View v) {  
				  //   showInfo2();
				    	event = mData.get(position);
						//把该则讲座对应的event传入Bundle，来自KunCheng
						Bundle detail_bundle = new Bundle();
						detail_bundle.putSerializable("LectureComment", event);
						Intent intent = new Intent(mContext, Comment.class);
						intent.putExtras(detail_bundle);
						mContext.startActivity(intent);
				    	
				    	
				    }  
				   });
			 holder.linearlayoutLike.setOnClickListener(new View.OnClickListener() {  
				    public void onClick(View v) {  
				    // showInfo3();      
				    	 event = mData.get(position);
						 
				    	event.setLike(!event.isLike());
				    	if (event.isLike())
				    	{
				    		likeIcon_change.setImageDrawable(v.getResources().getDrawable(R.drawable.like_red));
				    		likeText_change.setTextColor(v.getResources().getColor(R.color.main_menu_pressed));
				    		//喜欢的话，进行数据表LikeTable更新
				    		DBCenter.setLike(dbCenter.getReadableDatabase(), event.getUid(), true);
				    		event.updateLikeCount(1);//1 表示＋1
				    		LikeInterface.LikeGo(event.getUid(), "1");
				    		DBCenter.likeDBSync(dbCenter.getReadableDatabase(), event.getUid(), "1");
				    		//下面一句解决马上变Like数字
				    		likeText_change.setText( adaptPlace( String.format("%d", mData.get(position).getLikeCount()) ) );
				    		
				    	}
						else
						{
							likeIcon_change.setImageDrawable(v.getResources().getDrawable(R.drawable.like));
							likeText_change.setTextColor(v.getResources().getColor(R.color.main_menu_normal));
							//喜欢的话，进行数据表LikeTable更新
				    		DBCenter.setLike(dbCenter.getReadableDatabase(), event.getUid(), false);
				    		event.updateLikeCount(-1);//-1 表示＋ (-1)
				    		LikeInterface.LikeGo(event.getUid(), "0");
				    		DBCenter.likeDBSync(dbCenter.getReadableDatabase(), event.getUid(), "0");
				    		//下面一句解决马上变Like数字
				    		likeText_change.setText( adaptPlace( String.format("%d", mData.get(position).getLikeCount()) ) );
				    		
				    		
						}
				    }  
				   });
			 holder.linearlayoutRemind.setOnClickListener(new View.OnClickListener() {  
				    public void onClick(View v) {  
				     //showInfo4();    
				    	event = mData.get(position);
				    	event.setReminded(!event.isReminded());
				    	if (event.isReminded())
				    	{
				    		remindIcon_change.setImageDrawable(v.getResources().getDrawable(R.drawable.remind_red));
				    		remindText_change.setTextColor(v.getResources().getColor(R.color.main_menu_pressed));
				    		//收藏的话，进行数据表CollectionTable更新
				    		DBCenter.setRemind(dbCenter.getReadableDatabase(), event.getUid(), true);
				    		//添加到日历
				    		insertIntoCalender();
				    	}
				    	else
				    	{
				    		remindIcon_change.setImageDrawable(v.getResources().getDrawable(R.drawable.remind));
				    		remindText_change.setTextColor(v.getResources().getColor(R.color.main_menu_normal));
				    		//收藏的话，进行数据表CollectionTable更新
				    		DBCenter.setRemind(dbCenter.getReadableDatabase(), event.getUid(), false);
				    		//从日历删除
				    		deleteFromCalender();
				    	}	
				    }  
				   });
			return convertView;
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
		
		public void insertIntoCalender() {
			long calId = 1;

			GregorianCalendar startDate = event.getTimeCalendar();
			GregorianCalendar endDate = event.getTimeCalendar();
			
			startDate.set(Calendar.HOUR_OF_DAY, 8);
			startDate.set(Calendar.MINUTE, 0);

			ContentResolver cr1 = mContext.getContentResolver(); // 添加新event，步骤是固定的
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

			ContentResolver cr2 = mContext.getContentResolver(); // 为刚才新添加的event添加reminder
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

			Toast.makeText(mContext, "添加到 收藏页面&日历 成功", Toast.LENGTH_SHORT).show();
		}

		public void deleteFromCalender() {
			Uri deleteReminderUri = null;
			Uri deleteEventUri = null;
			deleteReminderUri = ContentUris.withAppendedId(Reminders.CONTENT_URI,
					event.getReminderInfo().getReminderId());
			deleteEventUri = ContentUris.withAppendedId(Events.CONTENT_URI, event
					.getReminderInfo().getEventId());
			int rowR = mContext.getContentResolver().delete(deleteReminderUri,
					null, null);
			int rowE = mContext.getContentResolver().delete(deleteEventUri, null,
					null);
			if (rowE > 0 && rowR > 0) {
				Toast.makeText(mContext, "从 收藏页面&日历 删除成功", Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(mContext, "从 收藏页面&日历 删除失败", Toast.LENGTH_SHORT).show();
		}
		
}