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
	//���ڸ���ListViewʱ
	public void setMData(List<Event> list){
		
		mData = list;	
	}
	//��������DBCenter
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
			  holder.lectureTime.setText("ʱ��: " + mData.get(position).getTime()); 
			  holder.lectureAddr.setText("�ص�: " + mData.get(position).getAddress());  
			  holder.lectureSpeaker.setText("����: " + mData.get(position).getSpeaker()); 
			  holder.lectureId.setText(mData.get(position).getUid());
			  
			  final ImageView likeIcon_change = holder.likeIcon;
			  final TextView likeText_change = holder.likeText;
			  final ImageView remindIcon_change = holder.remindIcon;
			  final TextView remindText_change = holder.remindText;
			  event = mData.get(position);
			  
			  //����Ĵ�������ʵ�ֵ��޺���ʧ��BUG,Yao,  ���꣬�����㻹�ǲ���ѽ�����Ƿ����ɣ�^ ^
			  if(mData.get(position).isLike()){
				  holder.likeIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.like_red));
				  holder.likeText.setTextColor(convertView.getResources().getColor(R.color.main_menu_pressed));
				  
			  }
			  else {
				  holder.likeIcon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.like));
				  holder.likeText.setTextColor(convertView.getResources().getColor(R.color.item_content));
				  
			  }
			  //����BUG�Խ����You still have a long way to go, no a lot BUGs to go! ahahaha!
			 if(mData.get(position).getLikeCount() > 0){
				 holder.likeText.setText( adaptPlace( String.format("%d", mData.get(position).getLikeCount()) ) );
				 
		    		 
			 }
				//holder.likeText.setText(mData.get(position).getLikeCount()); //No package identifier when getting value for resource  
			  //����Ĵ���ʹ���� �ղذ�ť
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
						sendIntent.putExtra(Intent.EXTRA_TITLE, "����");
						sendIntent.putExtra(Intent.EXTRA_TEXT,
								"Hi���������һ����Ȥ�Ľ�����" + "\n"
								+ "���⣺" + event.getTitle()+ "\n" 
								+ "ʱ�䣺" + event.getTime()+"\n" 
								+ "�ص㣺" + event.getAddress()+ "\n" 
								+ "������" + event.getSpeaker() + "\n"
								+ "�������ô�������" + "\n"
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
						//�Ѹ�������Ӧ��event����Bundle������KunCheng
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
				    		//ϲ���Ļ����������ݱ�LikeTable����
				    		DBCenter.setLike(dbCenter.getReadableDatabase(), event.getUid(), true);
				    		event.updateLikeCount(1);//1 ��ʾ��1
				    		LikeInterface.LikeGo(event.getUid(), "1");
				    		DBCenter.likeDBSync(dbCenter.getReadableDatabase(), event.getUid(), "1");
				    		//����һ�������ϱ�Like����
				    		likeText_change.setText( adaptPlace( String.format("%d", mData.get(position).getLikeCount()) ) );
				    		
				    	}
						else
						{
							likeIcon_change.setImageDrawable(v.getResources().getDrawable(R.drawable.like));
							likeText_change.setTextColor(v.getResources().getColor(R.color.main_menu_normal));
							//ϲ���Ļ����������ݱ�LikeTable����
				    		DBCenter.setLike(dbCenter.getReadableDatabase(), event.getUid(), false);
				    		event.updateLikeCount(-1);//-1 ��ʾ�� (-1)
				    		LikeInterface.LikeGo(event.getUid(), "0");
				    		DBCenter.likeDBSync(dbCenter.getReadableDatabase(), event.getUid(), "0");
				    		//����һ�������ϱ�Like����
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
				    		//�ղصĻ����������ݱ�CollectionTable����
				    		DBCenter.setRemind(dbCenter.getReadableDatabase(), event.getUid(), true);
				    		//��ӵ�����
				    		insertIntoCalender();
				    	}
				    	else
				    	{
				    		remindIcon_change.setImageDrawable(v.getResources().getDrawable(R.drawable.remind));
				    		remindText_change.setTextColor(v.getResources().getColor(R.color.main_menu_normal));
				    		//�ղصĻ����������ݱ�CollectionTable����
				    		DBCenter.setRemind(dbCenter.getReadableDatabase(), event.getUid(), false);
				    		//������ɾ��
				    		deleteFromCalender();
				    	}	
				    }  
				   });
			return convertView;
		}
		//�������ڸ��������һ���ո����ֻ�и�λ���Ļ�
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

			ContentResolver cr1 = mContext.getContentResolver(); // �����event�������ǹ̶���
			ContentValues values = new ContentValues();
			values.put(Events.DTSTART, startDate.getTime().getTime()); // �������ʱ�䣬������������
			values.put(Events.DTEND, endDate.getTime().getTime());
			values.put(Events.TITLE, event.getTitle());
			values.put(Events.DESCRIPTION, event.getSpeaker());
			values.put(Events.EVENT_LOCATION, event.getAddress());
			values.put(Events.CALENDAR_ID, calId);
			values.put(Events.EVENT_TIMEZONE, "GMT+8");
			Uri uri = cr1.insert(Events.CONTENT_URI, values);
			Long eventId = Long.parseLong(uri.getLastPathSegment()); // ��ȡ�ղ���ӵ�event��Id

			ContentResolver cr2 = mContext.getContentResolver(); // Ϊ�ղ�����ӵ�event���reminder
			ContentValues values1 = new ContentValues();
			values1.put(Reminders.MINUTES, 10); // ������ǰ����������
			values1.put(Reminders.EVENT_ID, eventId);
			values1.put(Reminders.METHOD, Reminders.METHOD_ALERT);  //���ø��¼�Ϊ����
			Uri newReminder = cr2.insert(Reminders.CONTENT_URI, values1); // ���������������ֵ��һ��Uri
			long reminderId = Long.parseLong(newReminder.getLastPathSegment());

			// ��¼����
			event.setReminderInfo(new ReminderInfo(eventId, reminderId));

			// setAlarmDeal(startMillis); //
			// ����reminder��ʼ��ʱ��������һ��activity
			// // ����ȫ�ֶ�ʱ��
			// Intent intent = new Intent(this,
			// AlarmActivity.class);
			// PendingIntent pi =
			// PendingIntent.getActivity(this, 0, intent, 0);
			// AlarmManager aManager = (AlarmManager)
			// getSystemService(Service.ALARM_SERVICE);
			// aManager.set(AlarmManager.RTC_WAKEUP, time, pi);
			// //
			// ��ϵͳ����System.currentTimeMillis()��������ֵ��time��ͬʱ����pi��Ӧ�����

			Toast.makeText(mContext, "��ӵ� �ղ�ҳ��&���� �ɹ�", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(mContext, "�� �ղ�ҳ��&���� ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(mContext, "�� �ղ�ҳ��&���� ɾ��ʧ��", Toast.LENGTH_SHORT).show();
		}
		
}