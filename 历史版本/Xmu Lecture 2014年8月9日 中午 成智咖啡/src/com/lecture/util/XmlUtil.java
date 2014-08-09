package com.lecture.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lecture.localdata.Event;

import android.content.Context;


public class XmlUtil {


	public static List<Event> readXml(String xmlPath, Context context) {
		List<Event> events = null;
		XmlPullParser mParser = null;
		FileInputStream in = null;
		Event event = null;
		try {
			in = new FileInputStream(xmlPath);
			mParser = XmlPullParserFactory.newInstance().newPullParser();
			mParser.setInput(in, "UTF-8");
			int code = mParser.getEventType();
			while (code != XmlPullParser.END_DOCUMENT) {
				switch (code) {
				case XmlPullParser.START_DOCUMENT:
					events = new ArrayList<Event>();
					//Log.i(Debug.TAG, "解析xml开始！");
					break;
				case XmlPullParser.START_TAG:
					String tagName = mParser.getName();
					if (tagName.equals("event")) {
						event = new Event(
								mParser.getAttributeValue(null, "uid"));
					}
					if (event != null) {
						if (tagName.equals("what")) {
							event.setTitle(mParser.nextText());
						} else if (tagName.equalsIgnoreCase("starttime")) {
							event.setTime(mParser.nextText());
						} else if (tagName.equals("where")) {
							 event.setAddress(mParser.nextText());
						} else if (tagName.equals("who")) {
							event.setSpeaker(mParser.nextText());
						} else if (tagName.equals("link")) {
							event.setLink(mParser.nextText());
						} else if (tagName.equals("likecount")) {
							event.setLikeCount(mParser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if (mParser.getName().equals("event")) {
						events.add(event);
						event = null;
					}
					break;
				}
				code = mParser.next();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return events;
	}

}
