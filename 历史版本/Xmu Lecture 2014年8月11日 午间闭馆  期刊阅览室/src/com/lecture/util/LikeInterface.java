package com.lecture.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.lecture.DBCenter.DBCenter;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LikeInterface {
	
	

	public LikeInterface() {
		// TODO Auto-generated constructor stub
	}
	
	public static void LikeGo(final String id, final String isLiked){
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				FileOutputStream out = null;
				InputStream in = null;
				try {
					Log.i("DownLoad","开始设置Url并开始下载！");
					URL url = new URL("http://lecture.xmu.edu.cn/like_interface.php?id="+id+"&isliked="+isLiked);
					connection = (HttpURLConnection) url.openConnection();
					if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
						Log.i("按赞","连接成功！");
					else {
						Log.i("按赞","连接失败！");
					}
					
				}
				catch (MalformedURLException e) {
				} 
				catch (IOException e) {
					
					e.printStackTrace();
				} 
				finally {
					try {
						connection.disconnect();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	
	
	
	
}
