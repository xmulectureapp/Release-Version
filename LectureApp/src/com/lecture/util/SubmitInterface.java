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
					Log.i("�ύ","��ʼ�ύ������");
					URL url = new URL("http://lecture.xmu.edu.cn/submitCenter.php?xml=" + xml);
					connection = (HttpURLConnection) url.openConnection();
					if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
						Log.i("����","���ӳɹ���");
					else {
						Log.i("����","����ʧ�ܣ�");
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
