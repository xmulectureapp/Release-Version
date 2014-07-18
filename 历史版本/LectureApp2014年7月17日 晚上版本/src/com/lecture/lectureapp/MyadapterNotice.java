package com.lecture.lectureapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lecture.DBCenter.DBCenter;
import com.lecture.lectureapp.R;
import com.lecture.lectureapp.Myadapter.ViewHolder;
import com.lecture.localdata.Event;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class MyadapterNotice extends BaseAdapter  
{
	private LayoutInflater mInflater;  
	private Context mContext;
	private Event event;
	private List<Event> mData; 

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
	
	public MyadapterNotice(Context context, List<Event> list)
	{  

		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);  
		mData = list;
		
	} 
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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
		 // holder.lectureName.setText(mData.get(position).getTitle());  
		//  holder.lectureTime.setText("时间: " + mData.get(position).getTime()); 
		//  holder.lectureAddr.setText("地点: " + mData.get(position).getAddress());  
		//  holder.lectureSpeaker.setText("主讲: " + mData.get(position).getSpeaker()); 
		//  holder.lectureId.setText("id : " + mData.get(position).getUid());
		//  final ImageView likeIcon_change = holder.likeIcon;
		//  final TextView likeText_change = holder.likeText;
		//  final ImageView remindIcon_change = holder.remindIcon;
		//  final TextView remindText_change = holder.remindText;
		  event = mData.get(position);
		  
		return convertView;
	}

}
