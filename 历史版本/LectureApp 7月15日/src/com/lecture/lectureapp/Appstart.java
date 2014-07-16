package com.lecture.lectureapp;

import java.io.OutputStream;

import com.lecture.lectureapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

public class Appstart extends Activity {

	/*
	 * @Override public void onCreate(Bundle savedInstanceState) { // TODO
	 * Auto-generated method stub super.onCreate(savedInstanceState);
	 * setContentView(R.layout.appstart);
	 * //requestWindowFeature(Window.FEATURE_NO_TITLE);//去锟斤拷锟斤拷锟斤拷锟斤拷
	 * //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, //
	 * WindowManager.LayoutParams.FLAG_FULLSCREEN); //全锟斤拷锟斤拷示
	 * //Toast.makeText(getApplicationContext(), "锟斤拷锟接ｏ拷锟矫好憋拷锟叫ｏ拷",
	 * Toast.LENGTH_LONG).show();
	 * //overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
	 * new Handler().postDelayed(new Runnable(){
	 * 
	 * @Override public void run(){ Intent intent = new Intent
	 * (Appstart.this,Guide.class); startActivity(intent);
	 * overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
	 * Appstart.this.finish(); } }, 1500); }
	 */

	private SharedPreferences preferences;
	private Editor editor;
	private OutputStream os;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
		// 判断是不是首次登录，
		if (preferences.getBoolean("firststart", true)) 
		{
			editor = preferences.edit();
			// 将登录标志位设置为false，下次登录时不在显示首次登录界面
			editor.putBoolean("firststart", false);
			editor.commit();

			new Handler().postDelayed(new Runnable() 
			{

				@Override
				public void run() 
				{
					Intent intent = new Intent(Appstart.this, Guide.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					Appstart.this.finish();
				}
			}, 1500);
		} 
		else 
		{
			new Handler().postDelayed(new Runnable() 
			{

				@Override
				public void run() 
				{
					Intent intent = new Intent(Appstart.this, MainView.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					Appstart.this.finish();
				}
			}, 1500);
		}
	}
}
