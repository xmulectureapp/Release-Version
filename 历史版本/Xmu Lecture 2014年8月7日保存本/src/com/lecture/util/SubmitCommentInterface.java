package com.lecture.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.lecture.localdata.DetailInfo;

import android.util.Log;

public class SubmitCommentInterface {
	
	private static SubmitCommentInterface singleEventsUtil;

	public static SubmitCommentInterface getInstance(SubmitCallback callback) {
		if (singleEventsUtil == null)
			singleEventsUtil = new SubmitCommentInterface(callback);
		else
			singleEventsUtil.setCallback(callback);
		return singleEventsUtil;
	}

	private SubmitCallback mCallback;

	private SubmitCommentInterface(SubmitCallback callback) {
		mCallback = callback;
	}

	public void setCallback(SubmitCallback callback) {
		mCallback = callback;
	}
	
	///////////

	public SubmitCommentInterface() {
		// TODO Auto-generated constructor stub
	}
	
	public void SubmitGo(final String xml){
		
		new Thread(new Runnable() {	

			@Override
			public void run() {
				HttpURLConnection connection = null;
				FileOutputStream out = null;
				InputStream in = null;
				try {
					mCallback.onStart();
					
					Log.i("提交评论","开始提交！");
					URL url = new URL("http://lecture.xmu.edu.cn/submitComment.php?xml=" + URLEncoder.encode(xml, "UTF-8") );
					
					connection = (HttpURLConnection) url.openConnection();
					if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
						Log.i("提交评论","连接成功！");
					else {
						Log.i("提交评论","连接失败！");
					}
					
					mCallback.onEnd();
					
				}
				catch (MalformedURLException e) {
				} 
				catch (IOException e) {
					mCallback.onNoInternet();
					
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
	
	
	
	
	public interface SubmitCallback {
		/**
		 * 只能写入主线程handler
		 */
		public void onStart();

		/**
		 * 只能写入主线程handler
		 */
		public void onEnd();

		/**
		 * 只能写入主线程handler
		 */
		public void onNoInternet();
	}
	
}
