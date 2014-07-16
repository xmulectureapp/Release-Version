package com.lecture.lectureapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class LectureDetail extends Activity {

	private TextView tvname;
	private TextView tvtime;
	private TextView tvaddr;
	private TextView tvspeaker;
	private TextView tvlabel;
	private TextView tvaboutspeaker;
	private TextView tvmore;
	private ImageView iv1;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lecture_detail);
		
		tvname = (TextView)findViewById(R.id.detail_lecture_name);
		tvtime = (TextView)findViewById(R.id.detail_lecture_time);
		tvaddr = (TextView)findViewById(R.id.detail_lecture_addr);
		tvspeaker = (TextView)findViewById(R.id.detail_lecture_speaker);
		tvlabel = (TextView)findViewById(R.id.detail_lecture_label);
		tvaboutspeaker = (TextView)findViewById(R.id.about_speaker);
		tvmore = (TextView)findViewById(R.id.more_information);
		
		tvname.setText("试图突破道德底线--当前美国小说的几个流派");
		tvtime.setText("2014年07月16日（星期三）15点00分");
		tvaddr.setText("【思明校区】外文学院囊萤楼三楼大会议室");
		tvspeaker.setText("常耀信");
		tvlabel.setText("美国小说\\道德底线 ");
		tvaboutspeaker.setText("常耀信，博士，教授，博士生导师，现任教于南开大学及美国关岛大学，研究方向为英美文学。1965年毕业于南开大学英文系，同年赴英国伦敦、剑桥进修。1984年在美国坦普尔大学英文系获博士学位。自1986年起为美国《文化》(Paideuma)杂志特邀编辑。1988年入选英国国际传记中心编纂的《远东及太平洋名人录》，多次入选《美国名师录》。著有《美国文学简史》、《英文学简史》、《美国文学史》、《希腊罗马神话》、《漫话英美文学》及《研究与写作》等；主编有《美国文学选读及《英国文学通史》等。在国内外刊物上发表过多篇论文。");
		tvmore.setText("人文学科的研究方法系列讲座第17期");
	}
}
