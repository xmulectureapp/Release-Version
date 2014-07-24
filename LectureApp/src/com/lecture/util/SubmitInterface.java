package com.lecture.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class SubmitInterface {

	public SubmitInterface() {
		// TODO Auto-generated constructor stub
	}
	
	public static void SubmitGo(final String xml){
		
		new Thread(new Runnable() {	

			@Override
			public void run() {
				HttpURLConnection connection = null;
				FileOutputStream out = null;
				InputStream in = null;
				try {
					Log.i("提交","开始提交讲座！");
					URL url = new URL("http://lecture.xmu.edu.cn/submitCenter.php?xml=" + xml);
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
