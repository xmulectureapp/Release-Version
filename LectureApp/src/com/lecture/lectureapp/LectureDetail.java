package com.lecture.lectureapp;

import com.lecture.DBCenter.DBCenter;
import com.lecture.DBCenter.XMLToList;
import com.lecture.localdata.DetailInfo;
import com.lecture.localdata.Event;
import com.lecture.util.GetDetailUtil;
import com.lecture.util.GetDetailUtil.GetDetailCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	
	private Event detail_lectureEvent; 
	
	//——————————————————————下面是用于handler的消息标记
	private static final int MESSAGE_DOWNLOAD_START = 1;//刷新开始
	private static final int MESSAGE_DOWNLOAD_END = 2;//刷新结束
	private static final int MESSAGE_DOWNLOAD_FAILED = 3;//刷新失败
	
	
	private ProgressDialog mProgressDialog;
	
	private Handler detailHandler = new Handler(){
		
		@Override
		public void handleMessage(Message message){
			final String msg = (String) message.obj;
			Message msgRepost = Message.obtain();
			
			if(message.what == MESSAGE_DOWNLOAD_START){
				
				
				
			}// end start
			else if(message.what == MESSAGE_DOWNLOAD_END){
				
				
				DetailInfo detailInfo = (DetailInfo)message.obj;
				
				setDetail(detailInfo);
				
				
				
				
			}//end end
			else if(message.what == MESSAGE_DOWNLOAD_FAILED){
				
				
				
			}// end failed
			
		}
		
		
		
	};  // end handler
		
	
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
		
		detail_lectureEvent = (Event)getIntent().getSerializableExtra("LectureDetail");
		
		tvname.setText(detail_lectureEvent.getTitle());
		tvtime.setText(detail_lectureEvent.getTime());
		tvaddr.setText(detail_lectureEvent.getAddress());
		tvspeaker.setText(detail_lectureEvent.getSpeaker());
		tvlabel.setText("标签还在开发中，敬请期待！");
		//tvaboutspeaker.setText("常耀信，博士，教授，博士生导师，现任教于南开大学及美国关岛大学，研究方向为英美文学。1965年毕业于南开大学英文系，同年赴英国伦敦、剑桥进修。1984年在美国坦普尔大学英文系获博士学位。自1986年起为美国《文化》(Paideuma)杂志特邀编辑。1988年入选英国国际传记中心编纂的《远东及太平洋名人录》，多次入选《美国名师录》。著有《美国文学简史》、《英文学简史》、《美国文学史》、《希腊罗马神话》、《漫话英美文学》及《研究与写作》等；主编有《美国文学选读及《英国文学通史》等。在国内外刊物上发表过多篇论文。");
		//tvmore.setText("人文学科的研究方法系列讲座第17期");
		tvaboutspeaker.setText("主讲信息加载中...");
		tvmore.setText("讲座详情加载中...");
		
		//获取讲座更多信息
		downloadDetail();
	}
	
	public void setDetail(DetailInfo detailInfo){
		tvaboutspeaker.setText( detailInfo.getLec_aboutSpeaker() );
		tvmore.setText( detailInfo.getLec_about() );
		
		
	}
	
	
	
	
	public void downloadDetail(){
		

		GetDetailUtil getDetailUtil = GetDetailUtil
				.getInstance(new GetDetailCallback() {

					@Override
					public void onStart() {
						Message msg = new Message();
						msg.what = MESSAGE_DOWNLOAD_START;
						msg.obj = "正在下载详情...";
						detailHandler.sendMessage(msg);
					}

					@Override
					public void onEnd(DetailInfo detailInfo) {
						
						
						Log.i("获取详细信息", "获取结束！准备发送MESSAGE_DOWNLOAD_END 消息！");
						
						Message msg = new Message();
						msg.what = MESSAGE_DOWNLOAD_END;
						
						msg.obj = detailInfo;
						
						
						
						detailHandler.sendMessage(msg);
								
					}

					@Override
					public void onNoInternet() {
						Message msg = new Message();
						msg.what = MESSAGE_DOWNLOAD_FAILED;
						msg.obj = "无法连接到网络...";
						detailHandler.sendMessage(msg);
					}
				});
		getDetailUtil.getDetail( this, detail_lectureEvent.getUid() );
		
	}

}


