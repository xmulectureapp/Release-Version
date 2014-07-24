package com.lecture.lectureapp;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;




import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lecture.DBCenter.DBCenter;
import com.lecture.DBCenter.XMLToList;
import com.lecture.SettingAndSubmit.SettingsCenter;
import com.lecture.lectureapp.R;



import com.lecture.localdata.Event;
import com.lecture.pulltorefresh.RefreshableView;
import com.lecture.pulltorefresh.RefreshableView.PullToRefreshListener;
import com.lecture.util.GetEventsHttpUtil;
import com.lecture.util.GetEventsHttpUtil.GetEventsCallback;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainView extends Activity 
{
	
	
	
	public static MainView instance = null;
	
	//���ݿ�
	public static final String DB_NAME = "LectureDB";
	private DBCenter dbCenter = new DBCenter(this, DB_NAME, 1);
	//private List<Map<String, Object>> mData;
	private List<Event> mDataHot;
	private List<Event> mDataRemind;
	private List<Event> mDataSubscribe;

	
	private ViewPager mTabPager;	
	//private ImageView mTabImg;// ����ͼƬ
	private ImageView mTab1,mTab2,mTab3,mTab4;
	private int zero = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int one;//����ˮƽ����λ��
	private int two;
	private int three;
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	//private Button mRightBtn;
	
	private ImageView mTab5;
	private int four;
	
	//��ȡmain menu�µ����ֱ���
	private TextView mText1;
	private TextView mText2;
	private TextView mText3;
	private TextView mText4;
	private TextView mText5;
	private HotMyadapter myadapter;
	private RemindMyadapter myadapterRemind;
	private SubscribeMyadapter myadapterSubscribe;
	public ListView hotList;
	public ListView remindList;
	public ListView subscribeList;
	public List<Event> hotResult;
	public List<Event> subscribeResult;
	public List<Event> remindResult; 
	public Cursor hotCursor;
	public Cursor subscribeCursor;
	public Cursor remindCursor;

	private RefreshableView refreshableView;

	private View viewHeader;
	private View viewFooter;
	
	//����ı������ڻ�ȡ���˵��İ�ť��layout���
	private LinearLayout lBtn1;
	private LinearLayout lBtn2;
	private LinearLayout lBtn3;
	private LinearLayout lBtn4;
	private LinearLayout lBtn5;
	
	
		// �������� from Kun
		private EditText ETusername;// �û���
		private EditText ETemail;// ����
		private CheckBox siming;
		private CheckBox xiangan;
		private CheckBox zhangzhou;
		private CheckBox xiamen;
		private CheckBox mon;
		private CheckBox tue;
		private CheckBox wed;
		private CheckBox thu;
		private CheckBox fri;
		private CheckBox sat;
		private CheckBox sun;
	
	//��������Yang
	LocalActivityManager manager = null;
	
	
	
	
	
	//������������������������������������������������������handler����Ϣ���
	private static final int MESSAGE_REFRESH_START = 1;//ˢ�¿�ʼ
	private static final int MESSAGE_REFRESH_END = 2;//ˢ�½���
	private static final int MESSAGE_REFRESH_FAILED = 3;//ˢ��ʧ��
	private static final int MESSAGE_PULL_REFRESH_START = 4;//pullˢ�¿�ʼ
	private static final int MESSAGE_PULL_REFRESH_END = 5;//pullˢ�½���
	private static final int MESSAGE_PULL_REFRESH_FAILED = 6;//pullˢ��ʧ��
	private static final int MESSAGE_PULL_REFRESH_LISTVIEW = 7;//pull�ɹ���listView��ʼ����
	
	
		private ProgressDialog mProgressDialog;
		
		//�ڶ���handler
		private Handler refreshHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				final String msg = (String) message.obj;
				Message msgRepost = Message.obtain();
				if (message.what == MESSAGE_REFRESH_START) {
					mProgressDialog = ProgressDialog.show(MainView.this,
							"���Ժ�", msg, true, false);
				} else if (message.what == MESSAGE_REFRESH_END) {
					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
						mProgressDialog = null;
						
						
						Log.i("SELECT", "Cursor�α��ȡ���ݿ�ʼ��");

						
						initHot();
						initLikeCollection();
						

						//�������ڲ���subStrign����
						Log.i("subString", mDataHot.get(1).getAddress().substring(0, 1));
					
						
						myadapter = new HotMyadapter(MainView.this, mDataHot);
						myadapterRemind = new RemindMyadapter(MainView.this, mDataRemind);
						myadapterSubscribe= new SubscribeMyadapter(MainView.this, mDataSubscribe);
					
						myadapter.setDBCenter(dbCenter);
						myadapterRemind.setDBCenter(dbCenter);
						myadapterSubscribe.setDBCenter(dbCenter);
						
						Log.i("Myadapter", "�����������ɹ���");
					    
						hotList.setAdapter(myadapter);
						remindList.setAdapter(myadapterRemind);
						subscribeList.setAdapter(myadapterSubscribe);
						
						refreshHot();
						refreshLikeCollection();
						
						//����ˢ��ִ�в���
						refreshableView.setOnRefreshListener(new PullToRefreshListener() {
							@Override
							public void onRefresh() {
								
								try {
									/*
									View view = hotList.getFocusedChild();
									if(view == null)
										Log.i("onRefresh", "view is null");
									( (RelativeLayout)view.findViewById(R.id.itemAll) )
									.setBackground(getResources().getDrawable(R.color.item_background));
									*/
									pullRefresh();
									
									
									Thread.sleep(3000);
									//Thread.sleep(3000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								//Toast.makeText(MainView.this, "����ˢ�¡���", Toast.LENGTH_LONG).show();
								//refreshableView.finishRefreshing(myadapter);
							}
						}, 0);
						//�����϶�item��Ĭ�ϵ����ʾ��ɫ���иı�, ��Ĭ�ϵ��Ч��ȡ��
						hotList.setSelector(getResources().getDrawable(R.drawable.item_none_selector));
						hotList.setSelected(false);	
						// ListView ��ĳ�ѡ�к���߼�  
						hotList.setOnItemClickListener(new OnItemClickListener() {  
					        
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								// TODO Auto-generated method stub
								
								Toast.makeText(MainView.this,"��ѡ���˽�����" + ((TextView)arg1.findViewById(R.id.lecture_id)).getText(),Toast.LENGTH_LONG ).show();
								
								//����������� KunCheng��������ʾ��ϸ��Ϣ
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataHot) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, LectureDetail.class);	
								intent.putExtras(detail_bundle);
								startActivity(intent);
							}  
					    });
						//remind
						remindList.setOnItemClickListener(new OnItemClickListener() {  
					        
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								// TODO Auto-generated method stub
								 
								Toast.makeText(MainView.this,"��ѡ���˽�����" + ((TextView)arg1.findViewById(R.id.lecture_id)).getText(),Toast.LENGTH_LONG ).show();
								
								//����������� KunCheng��������ʾ��ϸ��Ϣ
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataRemind) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, LectureDetail.class);	
								intent.putExtras(detail_bundle);
								startActivity(intent);
							}  
					    });
						//subscribe
						subscribeList.setOnItemClickListener(new OnItemClickListener() {  
					        
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								// TODO Auto-generated method stub
								 
								Toast.makeText(MainView.this,"��ѡ���˽�����" + ((TextView)arg1.findViewById(R.id.lecture_id)).getText(),Toast.LENGTH_LONG ).show();
								
								//����������� KunCheng��������ʾ��ϸ��Ϣ
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataSubscribe) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, LectureDetail.class);	
								intent.putExtras(detail_bundle);
								startActivity(intent);
							}  
					    });
					
					}
				} else if (message.what == MESSAGE_REFRESH_FAILED) {
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					mProgressDialog = null;
					Toast.makeText(MainView.this, msg, Toast.LENGTH_SHORT)
							.show();
				}
				else if(message.what == MESSAGE_PULL_REFRESH_START){
					
				}
				else if(message.what == MESSAGE_PULL_REFRESH_END){
					msgRepost = Message.obtain();
					msgRepost.what = MESSAGE_PULL_REFRESH_LISTVIEW;
					msgRepost.obj = "����ˢ�³ɹ�����һ����ʼ���� ListView";
					refreshHandler.sendMessage(msgRepost);
					
				}
				else if(message.what == MESSAGE_PULL_REFRESH_FAILED){
					
				}
				else if(message.what == MESSAGE_PULL_REFRESH_LISTVIEW){
					
					Log.i("PULL REFRESH ", "ˢ��like��collection��");
					
					DBCenter.refreshLike(dbCenter.getReadableDatabase());
					DBCenter.refreshCollection(dbCenter.getReadableDatabase());
					
					Log.i("MESSAGE_XML_TO_LISTDB_SUCCESS", "���Cursor׼��������");
					
					
					initHot();
					refreshHot();
					
					initLikeCollection();
					refreshLikeCollection();
					
					refreshableView.finishRefreshing(myadapter);
					
					Log.i("ListViewˢ��", "ˢ��ListView�ɹ���");
				}

			}

		};
		//�ڶ���handler����	
		
		// �����ǵڶ���handler��Ӧ�����refresh()
		public void refresh() {
			GetEventsHttpUtil getEventsUtil = GetEventsHttpUtil
					.getInstance(new GetEventsCallback() {

						@Override
						public void onStart() {
							Message msg = new Message();
							msg.what = MESSAGE_REFRESH_START;
							msg.obj = "���ڸ���...";
							refreshHandler.sendMessage(msg);
						}

						@Override
						public void onEnd() {
							
							//XML TO List, then to db
							
							XMLToList xmlToList = new XMLToList();
							//DBCenter.clearAllData(dbCenter.getReadableDatabase(), DBCenter.LECTURE_TABLE);
							xmlToList.insertListToDB(MainView.this, dbCenter, DBCenter.LECTURE_TABLE);
							
							Log.i("onEnd", "XMLToList�Ѿ������ݴ������ݿ⣡");
							
							Log.i("onEnd", "��ʼrefresh like �� �ղ�");
							
							DBCenter.refreshLike(dbCenter.getReadableDatabase());
							DBCenter.refreshCollection(dbCenter.getReadableDatabase());
							
							
							Message msg = new Message();
							msg.what = MESSAGE_REFRESH_END;
							refreshHandler.sendMessage(msg);
							
							
							
							
						}

						@Override
						public void onNoInternet() {
							Message msg = new Message();
							msg.what = MESSAGE_REFRESH_FAILED;
							msg.obj = "�޷����ӵ�����...";
							refreshHandler.sendMessage(msg);
						}
					});
			getEventsUtil.getInfo(this);
			
		}
		//--------------refresh() ����
		
		
		//-------------------pull refresh--------------------------
		
		public void pullRefresh() {
			GetEventsHttpUtil getEventsUtil = GetEventsHttpUtil
					.getInstance(new GetEventsCallback() {

						@Override
						public void onStart() {
							Message msg = new Message();
							msg.what = MESSAGE_PULL_REFRESH_START;
							msg.obj = "���ڸ���...";
							refreshHandler.sendMessage(msg);
						}

						@Override
						public void onEnd() {
							
							//XML TO List, then to db
							
							XMLToList xmlToList = new XMLToList();
							
							//ɾ�����ݿ�
							DBCenter.clearAllData(dbCenter.getReadableDatabase(), DBCenter.LECTURE_TABLE);
							xmlToList.insertListToDB(MainView.this, dbCenter, DBCenter.LECTURE_TABLE);
							
							Log.i("��RefreshCenter���еĲ���", "XMLToList�Ѿ������ݴ������ݿ⣡");
							Message msg = new Message();
							msg.what = MESSAGE_PULL_REFRESH_END;
							refreshHandler.sendMessage(msg);
							
							
							
						}

						@Override
						public void onNoInternet() {
							Message msg = new Message();
							msg.what = MESSAGE_PULL_REFRESH_FAILED;
							msg.obj = "�޷����ӵ�����...";
							refreshHandler.sendMessage(msg);
						}
					});
			getEventsUtil.getInfo(this);
			
		}
		
		//-------------------pull refresh end--------------------------


/*
 * onCreate	(non-Javadoc)
 * @see android.app.Activity#onCreate(android.os.Bundle)
 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainview);
		
		 //����activityʱ���Զ����������
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
       
        mTab1 = (ImageView) findViewById(R.id.img_subscribecenter);
        mTab2 = (ImageView) findViewById(R.id.img_hotlecturecenter);
        mTab3 = (ImageView) findViewById(R.id.img_remindcenter);
        mTab4 = (ImageView) findViewById(R.id.img_submitcenter);  
        mTab5 = (ImageView) findViewById(R.id.img_mycenter);
       
       // mTabImg = (ImageView) findViewById(R.id.img_tab_now);
        /*
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));      
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));      
        mTab5.setOnClickListener(new MyOnClickListener(4));
        */
        
        //�������ڲ���ʱ��
        SettingsCenter sqlMaker = new SettingsCenter(MainView.this);
        SettingsCenter.stringToTime("2014-7-18 20:20");
        //��ȡ�˵����־��
        mText1 = (TextView)findViewById(R.id.mText1);
        mText2 = (TextView)findViewById(R.id.mText2);
        mText3 = (TextView)findViewById(R.id.mText3);
        mText4 = (TextView)findViewById(R.id.mText4);
        mText5 = (TextView)findViewById(R.id.mText5);
        
        
        //��ȡ�˵���������ڽ������������
        lBtn1 = (LinearLayout)findViewById(R.id.subscribe_button_layout);
        lBtn2 = (LinearLayout)findViewById(R.id.hot_button_layout);
        lBtn3 = (LinearLayout)findViewById(R.id.remind_button_layout);
        lBtn4 = (LinearLayout)findViewById(R.id.submit_button_layout);
        lBtn5 = (LinearLayout)findViewById(R.id.my_button_layout);
        
        lBtn1.setOnClickListener(new LayoutOnClickListener(0));
        lBtn2.setOnClickListener(new LayoutOnClickListener(1));
        lBtn3.setOnClickListener(new LayoutOnClickListener(2));
        lBtn4.setOnClickListener(new LayoutOnClickListener(3));
        lBtn5.setOnClickListener(new LayoutOnClickListener(4));
        
       
        
        
        Display currDisplay = getWindowManager().getDefaultDisplay();//��ȡ��Ļ��ǰ�ֱ���
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        //one = displayWidth/4; //����ˮƽ����ƽ�ƴ�С
        one = displayWidth/5;
        two = one*2;
        three = one*3;
        four = one*4;
        
        Log.i("info", "��ȡ����Ļ�ֱ���Ϊ" + one + two + three + "��Ļ�ֱ���Ϊ" + displayWidth + "X" + displayHeight);
        
        //InitImageView();//ʹ�ö���
        //��Ҫ��ҳ��ʾ��Viewװ��������
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.subscribecenter, null);
        View view2 = mLi.inflate(R.layout.hotlecturecenter, null);
        View view3 = mLi.inflate(R.layout.noticecenter, null);
        View view4 = mLi.inflate(R.layout.submitcenter, null);
        View view5 = mLi.inflate(R.layout.mycenter, null);
        
        viewHeader = mLi.inflate(R.layout.head_view, null);
        viewFooter = mLi.inflate(R.layout.foot_view, null);

        subscribeList = (ListView)view1.findViewById(R.id.list_view_subscribe);
        hotList = (ListView) view2.findViewById(R.id.list_view);//��hot_ListViewת������
        remindList = (ListView)view3.findViewById(R.id.list_view_notice);

        subscribeList.addHeaderView(viewHeader);
        //subscribeList.addHeaderView(viewFooter);
        
        hotList.addHeaderView(viewHeader);
        hotList.addFooterView(viewFooter);
        
        remindList.addHeaderView(viewHeader);
        //remindList.addHeaderView(viewFooter);

        
        refreshableView = (RefreshableView)view2.findViewById(R.id.refreshable_view);
        
        //ÿ��ҳ���view����
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view2);
        views.add(view1);
        views.add(view3);
       // views.add(view4);
        //code����Yang
        manager = new LocalActivityManager(this , true);
        manager.dispatchCreate(savedInstanceState);
        Intent intent = new Intent(MainView.this, SubmitCenter.class);
        views.add(getView("SubmitCenter", intent));
        
        views.add(view5);
        
     
        
       
        
        //���ViewPager������������
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			//@Override
			//public CharSequence getPageTitle(int position) {
				//return titles.get(position);
			//}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
			
	
		};
		
		mTabPager.setAdapter(mPagerAdapter);
		
		//refresh()����ˢ��XML�ļ������سɹ��󲢰�XMLת�浽���ݿ⣬Ȼ������������ʹ��
		refresh();
		
		
		// �������� from Kun
		ETusername = (EditText) view5.findViewById(R.id.username);
		ETemail = (EditText) view5.findViewById(R.id.email);
		siming = (CheckBox) view5.findViewById(R.id.siming);
		xiangan = (CheckBox) view5.findViewById(R.id.xiangan);
		zhangzhou = (CheckBox) view5.findViewById(R.id.zhangzhou);
		xiamen = (CheckBox) view5.findViewById(R.id.xiamen);
		mon = (CheckBox) view5.findViewById(R.id.Monday);
		tue = (CheckBox) view5.findViewById(R.id.Tuesday);
		wed = (CheckBox) view5.findViewById(R.id.Wednesday);
		thu = (CheckBox) view5.findViewById(R.id.Thursday);
		fri = (CheckBox) view5.findViewById(R.id.Friday);
		sat = (CheckBox) view5.findViewById(R.id.Saturday);
		sun = (CheckBox) view5.findViewById(R.id.Sunday);

		// ֻ�����������ʽ
		/*
		 * ETemail.addTextChangedListener(new TextWatcher() { public void
		 * afterTextChanged(Editable s) { if
		 * (ETemail.getText().toString().matches
		 * ("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && s.length() > 0) {
		 * Toast.makeText(MainView.this, "��Ч�������ַ", Toast.LENGTH_LONG).show(); }
		 * else { Toast.makeText(MainView.this, "��Ч�������ַ",
		 * Toast.LENGTH_LONG).show(); ETemail.requestFocus(); } } public void
		 * beforeTextChanged(CharSequence s, int start, int count, int after) {}
		 * public void onTextChanged(CharSequence s, int start, int before, int
		 * count) {} });
		 */

		siming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "siming", "˼");
				} else {
					deleteCampusAndTime(MainView.this, "siming");
				}
			}
		});
		xiangan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "xiangan","��");
				} else {
					deleteCampusAndTime(MainView.this, "xiangan");
				}
			}
		});
		xiamen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "xiamen", "��");
				} else {
					deleteCampusAndTime(MainView.this, "xiamen");
				}
			}
		});
		zhangzhou
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							saveCampusAndTime(MainView.this, "zhangzhou", "��");
						} else {
							deleteCampusAndTime(MainView.this, "zhangzhou");
						}
					}
				});
		mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "mon", "1");
				} else {
					deleteCampusAndTime(MainView.this, "mon");
				}
			}
		});
		tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "tue", "2");
				} else {
					deleteCampusAndTime(MainView.this, "tue");
				}
			}
		});
		wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "wed", "3");
				} else {
					deleteCampusAndTime(MainView.this, "wed");
				}
			}
		});
		thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "thu", "4");
				} else {
					deleteCampusAndTime(MainView.this, "thu");
				}
			}
		});
		fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "fri", "5");
				} else {
					deleteCampusAndTime(MainView.this, "fri");
				}
			}
		});
		sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "sat", "6");
				} else {
					deleteCampusAndTime(MainView.this, "sat");
				}
			}
		});
		sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "sun", "7");
				} else {
					deleteCampusAndTime(MainView.this, "sun");
				}
			}
		});
		
	}    // end function onCreate()
	
	
	//code����Yang
	private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
	//��ʼ��like���ղ�����
	public void initLikeCollection(){
		
		Log.i("initLikeCollection", "��ʼ");
		subscribeCursor = dbCenter.likeSelect(dbCenter.getReadableDatabase());
		remindCursor = dbCenter.collectionSelect(dbCenter.getReadableDatabase());
		
		//ʵ�ֶ���  2014�� 7��23 Typhoon today
		subscribeResult = DBCenter.L_convertCursorToListEventSubscribe(subscribeCursor,MainView.this);
		remindResult = DBCenter.L_convertCursorToListEvent(remindCursor);
		
		mDataRemind = remindResult;
		mDataSubscribe = subscribeResult;
		
		
	}
	//ˢ��Like���ղؽ���
	public void refreshLikeCollection(){
		
		Log.i("refreshLikeCollection", "��ʼ");
		
		myadapterRemind.setMData(mDataRemind);
		myadapterSubscribe.setMData(mDataSubscribe);
		
		myadapterRemind.notifyDataSetChanged();
		myadapterSubscribe.notifyDataSetChanged();
	}
	public void initHot(){

		hotCursor = dbCenter.select(dbCenter.getReadableDatabase(), null, null, null);
		hotResult = DBCenter.L_convertCursorToListEvent(hotCursor);
		mDataHot = hotResult;
		
		
	}
	public void refreshHot(){
		myadapter.setMData(mDataHot);
		myadapter.notifyDataSetChanged();
		
	}
	
	/**
	 * ʹ��SharedPreferences�����û���¼��Ϣ
	 * 
	 * @param context
	 * @param username
	 * @param password
	 */
	public static void saveLoginInfo(Context context, String username,
			String email) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		// ��ȡEditor����
		Editor editor = sharedPre.edit();
		// �����û���������
		editor.putString("username", username);
		editor.putString("email", email);
		// �ύ
		editor.commit();
	}

	// ��������--����ѡ�е�У����ʱ��
	public static void saveCampusAndTime(Context context, String key, String value) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		// ��ȡEditor����
		Editor editor = sharedPre.edit();
		// ����У����ʱ��
		editor.putString(key, value);
		// �ύ
		editor.commit();
	}

	// ��������--ɾ��ȡ��ѡ���У����ʱ��
	public static void deleteCampusAndTime(Context context, String str) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		if (sharedPre.contains(str)) {
			// ��ȡEditor����
			Editor editor = sharedPre.edit();
			// ɾ��У����ʱ��
			editor.remove(str);
			// �ύ
			editor.commit();
		}
	}
	
	/**
	 * ͷ��������
	 */
	/*
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};
	*/
	// ���������˵��������������
	public class LayoutOnClickListener implements View.OnClickListener {
		private int index = 0;

		public LayoutOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};
    
	 /* ҳ���л�����(ԭ����:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_pressed));
				mText1.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				initHot();
				refreshHot();

				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText2.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_normal));
					mText3.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_normal));
					mText4.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}else if (currIndex == 4) {
					animation = new TranslateAnimation(four, 0, 0, 0);
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_normal));
					mText5.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				
				break;
			case 1:
				
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_pressed));
				mText2.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				
				initLikeCollection();
				refreshLikeCollection();
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_normal));
					mText3.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_normal));
					mText4.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}else if (currIndex == 4) {
					animation = new TranslateAnimation(four, one, 0, 0);
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_normal));
					mText5.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_pressed));
				mText3.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				
				initLikeCollection();
				refreshLikeCollection();
				
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText2.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_normal));
					mText4.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}else if (currIndex == 4) {
					animation = new TranslateAnimation(four, two, 0, 0);
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_normal));
					mText5.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_pressed));
				mText4.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText2.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_normal));
					mText3.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 4) {
					animation = new TranslateAnimation(four, three, 0, 0);
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_normal));
					mText5.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}

				break;
			case 4:
				mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_pressed));
				mText5.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, four, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, four, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText2.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, four, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_normal));
					mText3.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}else if (currIndex == 3) {
					animation = new TranslateAnimation(three, four, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_normal));
					mText4.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				//��ʾ���������û������� form Kun
				SharedPreferences sharedPre = getSharedPreferences("config",
						MODE_PRIVATE);

				String username = sharedPre.getString("username", "");
				ETusername.setText(username);
				String email = sharedPre.getString("email", "");
				ETemail.setText(email);

				if (sharedPre.contains("siming"))
					siming.setChecked(true);
				if (sharedPre.contains("xiangan"))
					xiangan.setChecked(true);
				if (sharedPre.contains("xiamen"))
					xiamen.setChecked(true);
				if (sharedPre.contains("zhangzhou"))
					zhangzhou.setChecked(true);
				if (sharedPre.contains("mon"))
					mon.setChecked(true);
				if (sharedPre.contains("tue"))
					tue.setChecked(true);
				if (sharedPre.contains("wed"))
					wed.setChecked(true);
				if (sharedPre.contains("thu"))
					thu.setChecked(true);
				if (sharedPre.contains("fri"))
					fri.setChecked(true);
				if (sharedPre.contains("sat"))
					sat.setChecked(true);
				if (sharedPre.contains("sun"))
					sun.setChecked(true);
				
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(150);
			//mTabImg.startAnimation(animation);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	
	// �������ĵı������ð�ť
		public void save(View view) {

			String u = ETusername.getText().toString();
			String e = ETemail.getText().toString().trim();
			
			if (isEmail(e) || e.equals("") || e.isEmpty()) {
				saveLoginInfo(MainView.this, u, e);
			}
			else {
				Toast.makeText(MainView.this, "��Ч�������ַ", Toast.LENGTH_LONG).show();
				ETemail.requestFocus();
			}
		}

		// �����ж�������ʽ
		public static boolean isEmail(String email) {
			//String emailPattern = "[a-zA-Z0-9][a-zA-Z0-9._-]{2,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
			//boolean result = Pattern.matches(emailPattern, email);
			//return result;
			Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
	        Matcher matcher = pattern.matcher(email);

	        if(matcher.matches()) {
	        	return true;
	        }else {
	        	return false;
	        }
		}

	
	//����Ĵ����������������ڴ���android�� ���� ���ⰴ��
	
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //��ȡ back��
//    		
//        	if(menu_display){         //��� Menu�Ѿ��� ���ȹر�Menu
//        		menuWindow.dismiss();
//        		menu_display = false;
//        		}
//        	else {
//        		Intent intent = new Intent();
//            	intent.setClass(MainWeixin.this,Exit.class);
//            	startActivity(intent);
//        	}
//    	}
//    	
//    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //��ȡ Menu��			
//			if(!menu_display){
//				//��ȡLayoutInflaterʵ��
//				inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
//				//�����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
//				//�÷������ص���һ��View�Ķ����ǲ����еĸ�
//				layout = inflater.inflate(R.layout.main_menu, null);
//				
//				//��������Ҫ�����ˣ����������ҵ�layout���뵽PopupWindow���أ������ܼ�
//				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //������������width��height
//				//menuWindow.showAsDropDown(layout); //���õ���Ч��
//				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
//				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
//				//��λ�ȡ����main�еĿؼ��أ�Ҳ�ܼ�
//				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
//				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
//				
//				
//				//�����ÿһ��Layout���е����¼���ע��ɡ�����
//				//���絥��ĳ��MenuItem��ʱ�����ı���ɫ�ı�
//				//����׼����һЩ����ͼƬ������ɫ
//				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
//					@Override
//					public void onClick(View arg0) {						
//						//Toast.makeText(Main.this, "�˳�", Toast.LENGTH_LONG).show();
//						Intent intent = new Intent();
//			        	intent.setClass(MainWeixin.this,Exit.class);
//			        	startActivity(intent);
//			        	menuWindow.dismiss(); //��Ӧ����¼�֮��ر�Menu
//					}
//				});				
//				menu_display = true;				
//			}else{
//				//�����ǰ�Ѿ�Ϊ��ʾ״̬������������
//				menuWindow.dismiss();
//				menu_display = false;
//				}
//			
//			return false;
//		}
//    	return false;
//    }
//	//���ñ������Ҳఴť������
//	public void btnmainright(View v) {  
//		Intent intent = new Intent (MainWeixin.this,MainTopRightDialog.class);			
//		startActivity(intent);	
//		//Toast.makeText(getApplicationContext(), "����˹��ܰ�ť", Toast.LENGTH_LONG).show();
//      }  	
//	public void startchat(View v) {      //С��  �Ի�����
//		Intent intent = new Intent (MainWeixin.this,ChatActivity.class);			
//		startActivity(intent);	
//		//Toast.makeText(getApplicationContext(), "��¼�ɹ�", Toast.LENGTH_LONG).show();
//      }  
//	public void exit_settings(View v) {                           //�˳�  α���Ի��򡱣���ʵ��һ��activity
//		Intent intent = new Intent (MainWeixin.this,ExitFromSettings.class);			
//		startActivity(intent);	
//	 }
//	public void btn_shake(View v) {                                   //�ֻ�ҡһҡ
//		Intent intent = new Intent (MainWeixin.this,ShakeActivity.class);			
//		startActivity(intent);	
//	}
	
	

}
