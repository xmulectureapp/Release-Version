package com.lecture.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

import com.lecture.localdata.DetailInfo;
import com.lecture.localdata.Event;
import com.lecture.util.GetDetailUtil.GetDetailCallback;

public class GetDetailUtil {
	


	private static final String fileName = "eventsInfo.xml";

	private static GetDetailUtil singleEventsUtil;

	public static GetDetailUtil getInstance(GetDetailCallback callback) {
		if (singleEventsUtil == null)
			singleEventsUtil = new GetDetailUtil(callback);
		else
			singleEventsUtil.setCallback(callback);
		return singleEventsUtil;
	}

	private GetDetailCallback mCallback;

	private GetDetailUtil(GetDetailCallback callback) {
		mCallback = callback;
	}

	public void setCallback(GetDetailCallback callback) {
		mCallback = callback;
	}

	public static boolean isContainInfo(Context context) {
		File outFile = new File(context.getCacheDir().getAbsolutePath()
				+ "/info", fileName);
		return outFile.exists();
	}

	public void getDetail(final Context context, final String id) {
		
		
		
		
		mCallback.onStart();
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				DetailInfo detailInfo;
				
				HttpURLConnection connection = null;
				InputStream in = null;
				try {
					Log.i("��ϸ��Ϣ","��ʼ������ϸ��Ϣ!");
					URL url = new URL("http://lecture.xmu.edu.cn/lecture_detail.php?id=" + id);
					connection = (HttpURLConnection) url.openConnection();
					in = new BufferedInputStream(connection.getInputStream());
					//���سɹ�
					//���濪ʼ��ȡ�ļ�
					detailInfo = new DetailInfo();
					XmlPullParser mParser = null;
					mParser = XmlPullParserFactory.newInstance().newPullParser();
					mParser.setInput(in, "UTF-8");
					int code = mParser.getEventType();
					while (code != XmlPullParser.END_DOCUMENT) {
						switch (code) {
						case XmlPullParser.START_DOCUMENT:
							//events = new ArrayList<Event>();
							//Log.i(Debug.TAG, "����xml��ʼ��");
							break;
						case XmlPullParser.START_TAG:
							String tagName = mParser.getName();
							if (tagName.equals("lecturemore")) {
								detailInfo = new DetailInfo(
										mParser.getAttributeValue(null, "uid"));
							}
							if (detailInfo != null) {
								if (tagName.equals("aboutspeaker")) {
									detailInfo.setLec_aboutSpeaker(mParser.nextText());
								} else if (tagName.equalsIgnoreCase("aboutlecture")) {
									detailInfo.setLec_about(mParser.nextText());
								}
							}
							break;
						case XmlPullParser.END_TAG:
							if (mParser.getName().equals("event")) {
								//events.add(event);
								detailInfo = null;
							}
							break;
						}
						code = mParser.next();
					} // end while
					
					
					Log.i("��ϸ��Ϣ","���سɹ���");
					//mCallback.onEnd(new DetailInfo());//��Ҫ�޸�
					mCallback.onEnd(detailInfo);//
				}
				catch (MalformedURLException e) {
				} 
				catch (IOException e) {
					mCallback.onNoInternet();
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				finally {
					try {
						if (in != null)
							in.close();
						connection.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static File getEventsPath(Context context) {
		return new File(context.getCacheDir().getAbsolutePath() + "/info",
				fileName);
	}

	public interface GetDetailCallback {
		/**
		 * ֻ��д�����߳�handler
		 */
		public void onStart();

		/**
		 * ֻ��д�����߳�handler
		 */
		public void onEnd(DetailInfo detailInfo);

		/**
		 * ֻ��д�����߳�handler
		 */
		public void onNoInternet();
	}


	public GetDetailUtil() {
		// TODO Auto-generated constructor stub
	}

}
