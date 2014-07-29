package com.lecture.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;


public class GetInfoHttpUtil {

	private static GetInfoHttpUtil singleEventsUtil;

	public static GetInfoHttpUtil getInstance(GetInfoCallback callback) {
		if (singleEventsUtil == null)
			singleEventsUtil = new GetInfoHttpUtil(callback);
		else
			singleEventsUtil.setCallback(callback);
		return singleEventsUtil;
	}

	private GetInfoCallback mCallback;

	private GetInfoHttpUtil(GetInfoCallback callback) {
		mCallback = callback;
	}

	public void setCallback(GetInfoCallback callback) {
		mCallback = callback;
	}

	public static boolean isContainInfo(Context context, String fileName) {
		File outFile = new File(context.getCacheDir().getAbsolutePath()
				+ "/info", fileName);
		return outFile.exists();
	}

	public void getInfo(final Context context, final String strUrl,
			final String fileName) {
		mCallback.onStart();
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				FileOutputStream out = null;
				InputStream in = null;
				try {
					URL url = new URL(strUrl);
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
				} catch (MalformedURLException e) {
				} catch (IOException e) {
					mCallback.onNoInternet();
					e.printStackTrace();
				} finally {
					try {
						if (out != null)
							out.close();
						if (in != null)
							in.close();
						connection.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mCallback.onEnd();
			}
		}).start();
	}

	public static File getInfoPath(Context context, String fileName) {
		return new File(context.getCacheDir().getAbsolutePath() + "/info",
				fileName);
	}

	public interface GetInfoCallback {
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
