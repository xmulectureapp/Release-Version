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
	
	//������������������������������������������������������handler����Ϣ���
	private static final int MESSAGE_DOWNLOAD_START = 1;//ˢ�¿�ʼ
	private static final int MESSAGE_DOWNLOAD_END = 2;//ˢ�½���
	private static final int MESSAGE_DOWNLOAD_FAILED = 3;//ˢ��ʧ��
	
	
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
		tvlabel.setText("��ǩ���ڿ����У������ڴ���");
		//tvaboutspeaker.setText("��ҫ�ţ���ʿ�����ڣ���ʿ����ʦ�����ν����Ͽ���ѧ�������ص���ѧ���о�����ΪӢ����ѧ��1965���ҵ���Ͽ���ѧӢ��ϵ��ͬ�기Ӣ���׶ء����Ž��ޡ�1984��������̹�ն���ѧӢ��ϵ��ʿѧλ����1986����Ϊ�������Ļ���(Paideuma)��־�����༭��1988����ѡӢ�����ʴ������ı���ġ�Զ����̫ƽ������¼���������ѡ��������ʦ¼�������С�������ѧ��ʷ������Ӣ��ѧ��ʷ������������ѧʷ������ϣ�������񻰡���������Ӣ����ѧ�������о���д�����ȣ������С�������ѧѡ������Ӣ����ѧͨʷ���ȡ��ڹ����⿯���Ϸ������ƪ���ġ�");
		//tvmore.setText("����ѧ�Ƶ��о�����ϵ�н�����17��");
		tvaboutspeaker.setText("������Ϣ������...");
		tvmore.setText("�������������...");
		
		//��ȡ����������Ϣ
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
						msg.obj = "������������...";
						detailHandler.sendMessage(msg);
					}

					@Override
					public void onEnd(DetailInfo detailInfo) {
						
						
						Log.i("��ȡ��ϸ��Ϣ", "��ȡ������׼������MESSAGE_DOWNLOAD_END ��Ϣ��");
						
						Message msg = new Message();
						msg.what = MESSAGE_DOWNLOAD_END;
						
						msg.obj = detailInfo;
						
						
						
						detailHandler.sendMessage(msg);
								
					}

					@Override
					public void onNoInternet() {
						Message msg = new Message();
						msg.what = MESSAGE_DOWNLOAD_FAILED;
						msg.obj = "�޷����ӵ�����...";
						detailHandler.sendMessage(msg);
					}
				});
		getDetailUtil.getDetail( this, detail_lectureEvent.getUid() );
		
	}

}


