package com.lecture.lectureapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.lecture.localdata.DetailInfo;
import com.lecture.localdata.SubmitLecture;
import com.lecture.util.SubmitInterface;
import com.lecture.util.SubmitInterface.SubmitCallback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SubmitCenter extends Activity {

	EditText string_title, string_speaker, string_address,
			string_speaker_information, string_more_information,
			string_information_source;

	TextView string_time;
	Spinner mSpinner;
	Button btnSubmit;

	int one = 0;
	
	private int year, monthOfYear, dayOfMonth, hourOfDay, minute;

	
	//——————————————————————下面是用于handler的消息标记
		private static final int MESSAGE_SUBMIT_START = 1;//刷新开始
		private static final int MESSAGE_SUBMIT_END = 2;//刷新结束
		private static final int MESSAGE_SUBMIT_FAILED = 3;//刷新失败
		
		private ProgressDialog mProgressDialog;
		
		private Handler submitHandler = new Handler(){
			
			@Override
			public void handleMessage(Message message){
				final String msg = (String) message.obj;
				Message msgRepost = Message.obtain();
				
				if(message.what == MESSAGE_SUBMIT_START){
					
					mProgressDialog = ProgressDialog.show(SubmitCenter.this,
							"请稍后", msg, true, false);
					
					
				}// end start
				else if(message.what == MESSAGE_SUBMIT_END){
					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}
					
					Toast.makeText(getApplicationContext(), "提交成功！",Toast.LENGTH_LONG).show();
					
					//下面进行输入框的文本清除
					//clearAllBlanks();
					
					
				}//end end
				else if(message.what == MESSAGE_SUBMIT_FAILED){
					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}
					
					Toast.makeText(getApplicationContext(), "提交失败，请检查网络连接！",Toast.LENGTH_LONG).show();
					
					
				}// end failed
				
			}
			
			
			
		};  // end handler
	
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submitcenter);

		// 绑定控件
		string_title = (EditText) findViewById(R.id.string_title);
		string_speaker = (EditText) findViewById(R.id.string_speaker);
		string_address = (EditText) findViewById(R.id.string_address);
		string_speaker_information = (EditText) findViewById(R.id.string_speaker_information);
		string_more_information = (EditText) findViewById(R.id.string_more_information);
		string_information_source = (EditText) findViewById(R.id.string_information_source);

		/*
		 * mSpinner = (Spinner) findViewById(R.id.string_campus); // 建立数据源
		 * String[] mItems = { "思明校区", "翔安校区", "漳州校区", "市图书馆" }; //
		 * 建立Adapter并且绑定数据源 ArrayAdapter<String> mAdapter = new
		 * ArrayAdapter<String>(this,
		 * android.R.layout.simple_dropdown_item_1line, mItems); // 设置下拉样式 //
		 * mAdapter
		 * .setDropDownViewResource(android.R.layout.simple_dropdown_item_1line
		 * );
		 */

		mSpinner = (Spinner) findViewById(R.id.string_campus);
		List<String> mList = new ArrayList<String>();
		mList.add("思明校区");
		mList.add("翔安校区");
		mList.add("漳州校区");
		mList.add("厦门市区");

		SpinnerAdapter mAdapter = new SpinnerAdapter(this, mList);
		
		// 绑定 Adapter到控件
		mSpinner.setAdapter(mAdapter);
		mSpinner.setSelection(0);
	

		// 选取时间框
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		monthOfYear = calendar.get(Calendar.MONTH);
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);

		string_time = (TextView) findViewById(R.id.string_time);
		string_time.setOnClickListener(new OnClickListener() {

			String time;

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				string_time.setClickable(false);
				string_time.setText("");
				time = new String("");

				// one++;
				// Toast.makeText(getApplicationContext(), one + "",
				// Toast.LENGTH_SHORT).show();
				TimePickerDialog timePickerDialog = new TimePickerDialog(
						SubmitCenter.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {

								if (one % 2 == 0) {
									time += " " + hourOfDay + ":" + minute;

									string_time.setText(time);

									string_time.setClickable(true);
									// Toast.makeText(getApplicationContext(),
									// time, Toast.LENGTH_SHORT).show();
								}

								one++;
							}

						}, hourOfDay, minute, true);

				timePickerDialog.show();

				DatePickerDialog datePickerDialog = new DatePickerDialog(
						SubmitCenter.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								if (one % 2 == 0) {
									time += " " + year + "-"
											+ (monthOfYear + 1) + "-"
											+ dayOfMonth;

									string_time.setText(time);
									// Toast.makeText(getApplicationContext(),
									// time, Toast.LENGTH_SHORT).show();
								}

								one++;
							}
						}, year, monthOfYear, dayOfMonth);

				datePickerDialog.show();

			}

		});

		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			boolean isOK;

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isOK = true;
				SubmitLecture sl = new SubmitLecture();
				if (!TextUtils.isEmpty(string_title.getText())) {
					sl.setTitle(string_title.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), "标题不能为空",
							Toast.LENGTH_SHORT).show();
					isOK = false;
				}

				if (!TextUtils.isEmpty(string_speaker.getText())) {
					sl.setSpeaker(string_speaker.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), "主讲人不能为空",
							Toast.LENGTH_SHORT).show();
					isOK = false;
				}

				if (!TextUtils.isEmpty(string_time.getText())) {
					sl.setTime(string_time.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), "时间不能为空",
							Toast.LENGTH_SHORT).show();
					isOK = false;
				}
				
				sl.setCampus( convertCampus( mSpinner.getSelectedItem().toString() ) );

				if (!TextUtils.isEmpty(string_address.getText())) {
					sl.setAddress(string_address.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), "详细地点不能为空",
							Toast.LENGTH_SHORT).show();
					isOK = false;
				}

				if (!TextUtils.isEmpty(string_speaker_information.getText())) {
					sl.setSpeaker_information(string_speaker_information
							.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), "主讲人信息不能为空",
							Toast.LENGTH_SHORT).show();
					isOK = false;
				}

				if (!TextUtils.isEmpty(string_more_information.getText())) {
					sl.setMore_information(string_more_information.getText()
							.toString());
				} else {
					Toast.makeText(getApplicationContext(), "更多信息不能为空",
							Toast.LENGTH_SHORT).show();
					isOK = false;
				}

				if (!TextUtils.isEmpty(string_information_source.getText())) {
					sl.setInformation_source(string_information_source
							.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), "信息来源不能为空",
							Toast.LENGTH_SHORT).show();
					isOK = false;
				}

				if (isOK) {
					//开始提交
					String xml = generateXML(sl) ;
					submitGo( xml);//开始提交
					
					//Toast.makeText(getApplicationContext(), "提交完成",Toast.LENGTH_SHORT).show();
				}

			}

		});

	}
	
	public String generateXML(SubmitLecture sl){
		/*
		String xmlToSubmit = 
			"<submitXML>" +
				"<lectitle>" + sl.getTitle() + "</lectitle>" +
				"<lecspeaker>" + sl.getSpeaker() + "</lecspeaker>" +
				"<lecwhen>" + sl.getTime() + "</lecwhen>" +
				"<leccampus>" + sl.getCampus() + "</leccampus>" +
				"<lecwhere>" + sl.getAddress() + "</lecwhere>" +
				"<lecaboutspeaker>" + sl.getSpeaker_information() + "</lecaboutspeaker>" +
				"<lecabout>" + sl.getMore_information() + "</lecabout>" +
				"<lecsource>" + sl.getInformation_source() + "</lecsource>" +
			"</submitXML>	";
		
		*/
		
		String xmlToSubmit = 
				"%3CsubmitXML%3E" +
					"%3Clectitle%3E" + sl.getTitle() + "%3C/lectitle%3E" +
					"%3Clecspeaker%3E" + sl.getSpeaker() + "%3C/lecspeaker%3E" +
					"%3Clecwhen%3E" + sl.getTime() + "%3C/lecwhen%3E" +
					"%3Cleccampus%3E" + sl.getCampus() + "%3C/leccampus%3E" +
					"%3Clecwhere%3E" + sl.getAddress() + "%3C/lecwhere%3E" +
					"%3Clecaboutspeaker%3E" + sl.getSpeaker_information() + "%3C/lecaboutspeaker%3E" +
					"%3Clecabout%3E" + sl.getMore_information() + "%3C/lecabout%3E" +
					"%3Clecsource%3E" + sl.getInformation_source() + "%3C/lecsource%3E" +
				"%3C/submitXML%3E";
			
		
		return xmlToSubmit;
		
	}
	
	public void submitGo(String xmlToSubmit){
		

		SubmitInterface submitInterface = SubmitInterface
				.getInstance(new SubmitCallback() {

					@Override
					public void onStart() {
						Message msg = new Message();
						msg.what = MESSAGE_SUBMIT_START;
						msg.obj = "正在提交...";
						submitHandler.sendMessage(msg);
					}

					@Override
					public void onEnd() {
						
						
						Log.i("提交讲座", "提交成功！");
						
						Message msg = new Message();
						msg.what = MESSAGE_SUBMIT_END;
						msg.obj = "提交成功！";
						
						submitHandler.sendMessage(msg);
								
					}

					@Override
					public void onNoInternet() {
						Message msg = new Message();
						msg.what = MESSAGE_SUBMIT_FAILED;
						msg.obj = "无法连接到网络...";
						submitHandler.sendMessage(msg);
					}
				});
		submitInterface.SubmitGo(xmlToSubmit);
		
	}
	
	public void clearAllBlanks(){
		
		/*提交成功后清除
		 * string_title, string_speaker, string_address,
			string_speaker_information, string_more_information,
			string_information_source;
		 * */
		string_title.setText(null);
		string_speaker.setText(null);
		string_address.setText(null);
		string_speaker_information.setText(null);
		string_more_information.setText(null);
		string_information_source.setText(null);
		
		
		
		
		
		
	}
	
	public String convertCampus(String toConvert){
		
		if(toConvert.equals("思明校区")){
			return "siming";
		}
		else if(toConvert.equals("翔安校区")){
			return "xiangan";
		}
		else if(toConvert.equals("漳州校区")){
			return "zhangzhou";
		}
		else if(toConvert.equals("厦门市区")){
			return "xiamen";
		}
		else{
			Toast.makeText(getApplicationContext(), "校区转换出错!",Toast.LENGTH_LONG).show();
			return "null";
			
		}
		
		
	}
}
