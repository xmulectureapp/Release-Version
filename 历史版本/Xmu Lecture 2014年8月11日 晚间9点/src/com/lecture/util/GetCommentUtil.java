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
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

import com.lecture.localdata.Comment;

public class GetCommentUtil {
	


	private static final String fileName = "commentsdetail.xml";

	private static GetCommentUtil singleEventsUtil;

	public static GetCommentUtil getInstance(GetCommentCallback callback) {
		if (singleEventsUtil == null)
			singleEventsUtil = new GetCommentUtil(callback);
		else
			singleEventsUtil.setCallback(callback);
		return singleEventsUtil;
	}

	private GetCommentCallback mCallback;

	private GetCommentUtil(GetCommentCallback callback) {
		mCallback = callback;
	}

	public void setCallback(GetCommentCallback callback) {
		mCallback = callback;
	}

	public static boolean isContainInfo(Context context) {
		File outFile = new File(context.getCacheDir().getAbsolutePath()
				+ "/info", fileName);
		return outFile.exists();
	}

	public void getComments(final Context context, final String id) {
		
		
		
		
		mCallback.onStart();
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				Comment commentInfo;
				
				HttpURLConnection connection = null;
				FileOutputStream out = null;
				InputStream in = null;
				InputStream fileIn = null;
				
			
				try {
					Log.i("详细信息","开始下载详细信息!");
					URL url = new URL("http://lecture.xmu.edu.cn/Comment.php?id=" + id);
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
					fileIn = new FileInputStream( GetCommentUtil.getDetailPath(context).getPath() );
					commentInfo = new Comment();
					String tempUid = "";
					ArrayList<Comment> commensList = new ArrayList<Comment>();
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
							if (tagName.equals("lecturecomments")) {
									tempUid = mParser.getAttributeValue(null, "uid");
							}
							
							if (tagName.equals("comment")) {
								commentInfo = new Comment(
										tempUid);
							}
							
							if (commentInfo != null) {
								if (tagName.equals("username")) {
									commentInfo.setUserName(mParser.nextText());
								} else if (tagName.equalsIgnoreCase("usercomment")) {
									commentInfo.setUserComment(mParser.nextText());
								} else if (tagName.equalsIgnoreCase("commentdate")) {
									commentInfo.setCommentDate(mParser.nextText());
								}
							}
							break;
						case XmlPullParser.END_TAG:
							if (mParser.getName().equals("comment")) {
								commensList.add(commentInfo);
								commentInfo = null;
							}
							break;
						}
						code = mParser.next();
					} // end while
					
					
					Log.i("评论","下载成功！");
					//mCallback.onEnd(new DetailInfo());//需要修改
					mCallback.onEnd(commensList);//
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

	public interface GetCommentCallback {
		/**
		 * 只能写入主线程handler
		 */
		public void onStart();

		/**
		 * 只能写入主线程handler
		 */
		public void onEnd(List<Comment> commentsList);

		/**
		 * 只能写入主线程handler
		 */
		public void onNoInternet();
	}


	public GetCommentUtil() {
		// TODO Auto-generated constructor stub
	}

}
