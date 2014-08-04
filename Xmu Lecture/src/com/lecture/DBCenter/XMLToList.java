package com.lecture.DBCenter;

import java.io.FileNotFoundException;
import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.lecture.localdata.Event;
import com.lecture.util.GetEventsHttpUtil;
import com.lecture.util.XmlUtil;


public class XMLToList {

	private List<Event> events;
	
	public XMLToList() {
		// TODO Auto-generated constructor stub
		events = null;
	}
	
	public void insertListToDB(Context context, DBCenter listToDB, String tableName) {

			events = XmlUtil.readXml(
					GetEventsHttpUtil.getEventsPath(context).getPath(),
					context);
			//删除旧的数据表
			//listToDB.clearAllData(listToDB.getReadableDatabase(), tableName);
			// TODO 从本地获取其他信息
			for (Event event : events) {
				//Event tmp = null;
				try {
					//开始存入数据库
					
					listToDB.insertInto(listToDB.getReadableDatabase(), tableName, event);
				} catch (Exception e) {
					continue;
				}
				//if (tmp != null)
				//	event.merger(tmp);
			}
			
		
		
	}

}
