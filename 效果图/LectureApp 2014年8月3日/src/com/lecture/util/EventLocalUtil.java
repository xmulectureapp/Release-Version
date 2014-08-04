package com.lecture.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import com.lecture.localdata.Event;

import android.content.Context;
import android.util.Log;

public class EventLocalUtil {
	
	public static final String TAG = "DEBUG TAG";


	public static void delAllFile(Context context) {
		File delFile = context.getCacheDir().getAbsoluteFile();
		doDel(delFile);
	}

	private static void doDel(File file) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					doDel(f);
					f.delete();
				} else {
					f.delete();
				}
			}
		}
	}

	public static List<Event> loadAllInfo(Context context) {
		List<Event> events = new ArrayList<Event>();
		File loadFile = new File(context.getCacheDir().getAbsolutePath());
		File[] files = loadFile.listFiles();
		for (File file : files) {
			if (!file.isDirectory() && file.getName().endsWith(".sdll")) {
				FileInputStream fileIn = null;
				ObjectInputStream in = null;
				try {
					fileIn = new FileInputStream(file);
					in = new ObjectInputStream(fileIn);
					events.add((Event) in.readObject());
				} catch (StreamCorruptedException e) {
					Log.e(TAG, file.getName() + "   读取讲座信息失败   ===>>" + e);
				} catch (IOException e) {
					Log.e(TAG, file.getName() + "   读取讲座信息失败   ===>>" + e);
				} catch (ClassNotFoundException e) {
					Log.e(TAG, file.getName() + "   读取讲座信息失败   ===>>" + e);
				} finally {
					try {
						if (in != null)
							in.close();
						if (fileIn != null)
							fileIn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return events;
	}

	public static Event loadInfo(Context context, String name)
			throws FileNotFoundException {
		File loadFile = new File(context.getCacheDir().getAbsolutePath(), name
				+ ".sdll");
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		Event mEvent = null;
		if (!loadFile.exists())
			throw new FileNotFoundException("不存在该讲座信息");
		try {
			fileIn = new FileInputStream(loadFile);
			in = new ObjectInputStream(fileIn);
			mEvent = (Event) in.readObject();
		} catch (StreamCorruptedException e) {
			Log.e(TAG, name + "   读取讲座信息失败   ===>>" + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, name + "   读取讲座信息失败   ===>>" + e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(TAG, name + "   读取讲座信息失败   ===>>" + e.getMessage());
		} finally {
			try {
				if (in != null)
					in.close();
				if (fileIn != null)
					fileIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mEvent;
	}

	public static void saveInfo(Context context, Event data) {
		File saveFile = new File(context.getCacheDir().getAbsolutePath(),
				data.getTitle() + ".sdll");
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;
		try {
			if (!saveFile.exists()) {
				if (!saveFile.getParentFile().exists())
					saveFile.getParentFile().mkdirs();
				saveFile.createNewFile();
			}
			fileOut = new FileOutputStream(saveFile);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(data);
		} catch (FileNotFoundException e) {
			Log.e(TAG,
					data.getTitle() + "   保存讲座信息失败   ===>>" + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG,
					data.getTitle() + "   保存讲座信息失败  ===>>" + e.getMessage());
		} finally {
			try {
				if (out != null)
					out.close();
				if (fileOut != null)
					fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
