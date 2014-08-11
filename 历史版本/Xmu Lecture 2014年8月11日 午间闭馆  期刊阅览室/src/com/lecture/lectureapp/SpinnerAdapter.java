package com.lecture.lectureapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter {

	private List<String> mList;
	private Context mContext;
	
	public SpinnerAdapter(Context cContext, List<String> cList){
		this.mList = cList;
		this.mContext = cContext;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);  //获取
		convertView = mLayoutInflater.inflate(R.layout.spinner_item, null);
		/*LayoutInflater.inflate()将Layout文件转换为View，专门供Layout使用的Inflater。
		虽然Layout也是View的子类，但在android中如果想将xml中的Layout转换为View放入.java代码中
		操作，只能通过Inflater，而不能通过findViewById()。*/
		
		if(convertView != null){
			TextView txtName = (TextView)convertView.findViewById(R.id.txtName);
			txtName.setText(mList.get(position));
		}
		
		return convertView;
	}

}
