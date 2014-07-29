package com.lecture.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.lecture.localdata.DetailInfo;

import android.util.Log;

public class SubmitInterface {
	
	private static SubmitInterface singleEventsUtil;

	public static SubmitInterface getInstance(SubmitCallback callback) {
		if (singleEventsUtil == null)
			singleEventsUtil = new SubmitInterface(callback);
		else
			singleEventsUtil.setCallback(callback);
		return singleEventsUtil;
	}

	private SubmitCallback mCallback;

	private SubmitInterface(SubmitCallback callback) {
		mCallback = callback;
	}

	public void setCallback(SubmitCallback callback) {
		mCallback = callback;
	}
	
	///////////

	public SubmitInterface() {
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
					
					Log.i("�ύ","��ʼ�ύ������");
				//	URL url = new URL("http://lecture.xmu.edu.cn/submitCenter.php?xml=" + URLEncoder.encode(xml, "GBK"));
					URL url = new URL("http://lecture.xmu.edu.cn/submitCenter.php?xml=" + xml.replace(" ","%20") );
					connection = (HttpURLConnection) url.openConnection();
					if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
						Log.i("����","���ӳɹ���");
					else {
						Log.i("����","����ʧ�ܣ�");
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
		 * ֻ��д�����߳�handler
		 */
		public void onStart();

		/**
		 * ֻ��д�����߳�handler
		 */
		public void onEnd();

		/**
		 * ֻ��д�����߳�handler
		 */
		public void onNoInternet();
	}
	
}
