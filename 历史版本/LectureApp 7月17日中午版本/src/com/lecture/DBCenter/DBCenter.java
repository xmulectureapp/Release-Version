package com.lecture.DBCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lecture.lectureapp.Myadapter;
import com.lecture.localdata.Event;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBCenter extends SQLiteOpenHelper {
	
	
	// --------------------LECTURE_TABLE, Total:13 ROWS---------------------------//
		private static String LECTURE_ID = "Lid";// 讲座ID==>primary key
		private static String LECTURE_UID = "Luid";// 从网上获取的作为讲座的唯一标识
		//private static String TITLE = "Ltitle";// 讲座标题
		private static String LECTURE_NAME = "Lwhat";// 讲座详细标题
		private static String LECTURE_DATE = "Lwhen";// 讲座时间
		private static String PLACE = "Lwhere";// 讲座校区
		private static String SPEAKER = "Lwho";// 主讲人
		//private static String SPEAKER_MESSAGE = "whomsg";// 主讲人详细信息
		//private static String DATE_MESSAGE = "whatmsg";// 讲座详细内容
		//private static String PLACE_MESSAGE = "wheremsg";// 讲座详细地点
		//private static String SUBJECT = "sub";// 讲座分类
		//private static String TAG = "tag";// 讲座标签
		private static String LINK = "link";// 讲座链接
		
		
		public static final String LECTURE_TABLE = "LectureTable";// 讲座信息表名
		
		
		
		// -------------------用于存储从数据库中读取的详细信息结束---------//
		
		//--------------------------
		private String msg_luid;
		private String msg_what;

		private String msg_when;

		private String msg_where;

		private String msg_who;
		
		
		// ------------------------------字符串--》创建讲座信息表-------------------//
		private static final String LTABLE_CREATE = "create table " + LECTURE_TABLE
				+ "(" + LECTURE_ID + " integer primary key autoincrement,"
				+ LECTURE_UID + " varchar(64) UNIQUE," + LECTURE_NAME + " varchar(64)," 
				+ LECTURE_DATE + " varchar(64)," + PLACE + " varchar(64)," + SPEAKER + " varchar(64),"
				+ LINK + " varchar(64)"
				+ ")";
		// ------------------------------字符串--》创建讲座信息表结束-------------------//

		// ------------------------------插入到数据库------------------------------／／
		
		public void insertInto(SQLiteDatabase db, String tableName, Event event){
			
			Log.i("insert Lecture", "开始尝试把讲座插入到数据库");
			
			db.execSQL(
					"insert OR IGNORE into " + tableName + " values(null , ? , ? , ? , ? , ?, ?)",
					new String[] { event.getUid(), event.getTitle(),
							event.getTime(), event.getAddress(), event.getSpeaker(), event.getLink() });
			Log.i("insert Lecture", "插入数据库结束！");
			
			//return true;
		}
		
		

		// ------------------------------插入到数据库 end------------------------------／／

		//-----------------------------主页选择分类-----------------------------------------------//
		public Cursor select(SQLiteDatabase db, String time, String place, String subject){
			
			Log.i("SELECT", "开始查找数据分类");
			String[] selectString = new String[]{time, place, subject};
			//Cursor selectResult = db.rawQuery("select * from LectureTable where sub like ?",new String[] { "%"+SUBJECT+"%"});
			Cursor selectResult;
			//if(time == null)
				selectResult = db.rawQuery("select * from " + LECTURE_TABLE + " where 1",new String[]{});
			//else
				//selectResult = db.rawQuery("select * from " + LECTURE_TABLE + " where 1 LIMIT 0,4",new String[]{});
			Log.i("SELECT", "Select查找结束");
			
			return selectResult;
		}
		
		// --------将数据库查询LECTURETABLE结果CURSOR转化为List 读取 1 2 3 4 5 列-------//
		public static List<Map<String, Object>> L_converCursorToList(Cursor cursor) {
			ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

			while (cursor.moveToNext()) {

				Map<String, Object> map = new HashMap<String, Object>();
	           // map.put("luid", cursor.getString(1));
				map.put("lecture_name", cursor.getString(2));
				map.put("lecture_time","时间: " + cursor.getString(3));
				map.put("lecture_addr", "地点: " +  cursor.getString(4));
				map.put("lecture_speaker", "主讲: " + cursor.getString(5));
				//map.put("tag", cursor.getString(11));
				result.add(map);
			}
			
			return result;
		
		}
		
		public static List<Event> L_convertCursorToListEvent(Cursor cursor){
			
			ArrayList<Event> result = new ArrayList<Event>();
			
			while(cursor.moveToNext()){
				Event event = new Event();
				event.setUid(cursor.getString(1));
				event.setTitle(cursor.getString(2));
				event.setTime(cursor.getString(3));
				event.setAddress(cursor.getString(4));
				event.setSpeaker(cursor.getString(5));
				event.setLink(cursor.getString(6));	
				result.add(event);
			}
			
			return result;
		}

		// --------将数据库查询LECTURETABLE结果CURSOR转化为List 读取4 5 10 7 12列结束-------//
		
		//------下面用于清除数据库所有数据
		public static void clearAllData(SQLiteDatabase dbToClear, String tableName){
			
			Log.i("删除 LectureTable", "开始尝试 删除");
			
			dbToClear.execSQL(
					"DELETE FROM " + LECTURE_TABLE + " WHERE Lid IS NOT NULL ", new String[]{});
			Log.i("删除 LectureTable", "删除 LectureTable结束！");
			Log.i("删除 LectureTable", "删除 LectureTable结束！");
			
		}
		//=-------- 数据清除结束
		
	public DBCenter(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DBCenter(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}
	public DBCenter(Context context, String name, int version) {
		super(context, name, null, version);
	}

	



	@Override
	public void onCreate(SQLiteDatabase db) {
		// 第一次使用数据库时自动建表
		db.execSQL(LTABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Print in console Window ? 
		System.out.println("--------onUpdate Called--------" + oldVersion
				+ "--->" + newVersion);
	}

}
