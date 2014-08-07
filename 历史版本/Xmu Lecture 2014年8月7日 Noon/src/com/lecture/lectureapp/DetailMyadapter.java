package com.lecture.lectureapp;


import java.util.List;

import com.lecture.localdata.Comment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class DetailMyadapter extends BaseAdapter 
{
	  private LayoutInflater mInflater;  
	  private Context mContext;
	  private List<Comment> mData; 
	  
	

	
	public final class ViewHolder
	{  
		  public TextView commentUserNameTextView;
		  public TextView commentDateTextView;
		  public TextView commentContentTextView;
		   
	}
	//用于更新ListView时
	public void setMData(List<Comment> list){
		
		mData = list;	
	}
	
	
	
	
	public DetailMyadapter(Context context)
	{  

		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);  
		
	}  
	    
	public DetailMyadapter(Context context, List<Comment> list)
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
				convertView = mInflater.inflate(R.layout.comment_item, null);
				holder.commentUserNameTextView = (TextView)convertView.findViewById(R.id.detail_comment_username); 
				holder.commentDateTextView = (TextView)convertView.findViewById(R.id.detail_comment_date);
				holder.commentContentTextView = (TextView)convertView.findViewById(R.id.detail_comment_content);
				
				convertView.setTag(holder); 
			//}
			//else
			//{
				holder = (ViewHolder)convertView.getTag(); 
			//}
			  holder.commentUserNameTextView.setText(mData.get(position).getUserName());  
			  holder.commentDateTextView.setText(mData.get(position).getCommentDate()); 
			  holder.commentContentTextView.setText("地点: " + mData.get(position).getUserComment());  
			  
			  
			  return convertView;
		}
		


} // end adapter


