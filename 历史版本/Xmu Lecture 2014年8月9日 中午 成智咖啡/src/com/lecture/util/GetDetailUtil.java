package com.lecture.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
	


	private static final String fileName = "eventsdetail.xml";

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
				FileOutputStream out = null;
				InputStream in = null;
				InputStream fileIn = null;
				
			
				try {
					Log.i("详细信息","开始下载详细信息!");
					URL url = new URL("http://lecture.xmu.edu.cn/lecture_detail.php?id=" + id);
					connection = (HttpURLConnection) url.openConnection();
					in = new BufferedInputStream(connection.getInputStream());
					File outFile = new File(context.getCacheDir()
							.getAbsolutePath() + "/info", fileName);
					if (!outFile.exists()) {
						if (!outFile.getParentFile().exists())
							outFile.getParentFile().mkdirs();
						outFile.createNewFile();
					}
					out = new FileOutputStream(outFile);
					byte[] buffer = new byte[256];
					int length = 0;
					while ((length = in.read(buffer)) != -1) {
						out.write(buffer, 0, length);
						// System.out.write(buffer, 0, length);
					}
				
					//下载成功
					//下面开始读取文件
					fileIn = new FileInputStream( GetDetailUtil.getDetailPath(context).getPath() );
					detailInfo = new DetailInfo();
					XmlPullParser mParser = null;
					mParser = XmlPullParserFactory.newInstance().newPullParser();
					mParser.setInput(fileIn, "UTF-8");
					int code = mParser.getEventType();
					while (code != XmlPullParser.END_DOCUMENT) {
						switch (code) {
						case XmlPullParser.START_DOCUMENT:
							//events = new ArrayList<Event>();
							//Log.i(Debug.TAG, "解析xml开始！");
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
					
					
					Log.i("详细信息","下载成功！");
					//mCallback.onEnd(new DetailInfo());//需要修改
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

	public static File getDetailPath(Context context) {
		return new File(context.getCacheDir().getAbsolutePath() + "/info",
				fileName);
	}

	public interface GetDetailCallback {
		/**
		 * 只能写入主线程handler
		 */
		public void onStart();

		/**
		 * 只能写入主线程handler
		 */
		public void onEnd(DetailInfo detailInfo);

		/**
		 * 只能写入主线程handler
		 */
		public void onNoInternet();
	}


	public GetDetailUtil() {
		// TODO Auto-generated constructor stub
	}

}
