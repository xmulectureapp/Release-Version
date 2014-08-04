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
	 * //requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
	 * //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, //
	 * WindowManager.LayoutParams.FLAG_FULLSCREEN); //ȫ����ʾ
	 * //Toast.makeText(getApplicationContext(), "���ӣ��úñ��У�",
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
		// �ж��ǲ����״ε�¼��
		if (preferences.getBoolean("firststart", true)) 
		{
			editor = preferences.edit();
			// ����¼��־λ����Ϊfalse���´ε�¼ʱ������ʾ�״ε�¼����
			editor.putBoolean("firststart", false);
			editor.commit();
			
			//��������YeBin���޸ģ����ڵ�һ�δ�Appʱѡ��MyCenter������checkdbox��KunCheng����Ĵ���д�úܺ�
			
			SharedPreferences preferencesConfig = getSharedPreferences("config", Context.MODE_PRIVATE);
			// ��ȡEditor����
			Editor editor = preferencesConfig.edit();
			// ����У����ʱ��
			editor.putString("siming", "˼");
			editor.putString("xiangan", "��");
			editor.putString("zhangzhou", "��");
			editor.putString("xiamen", "��");
			editor.putString("sun", "1");
			editor.putString("mon", "2");
			editor.putString("tue", "3");
			editor.putString("wed", "4");
			editor.putString("thu", "5");
			editor.putString("fri", "6");
			editor.putString("sat", "7");
			
			// �ύ
			editor.commit();
			
			//����ʱ�޸���Щ  2014 07 14 18:09 Yebin Chen

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
