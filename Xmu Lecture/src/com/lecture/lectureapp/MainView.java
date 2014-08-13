package com.lecture.lectureapp;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;




import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lecture.layoutUtil.PopMenuView;
import com.lecture.DBCenter.DBCenter;
import com.lecture.DBCenter.XMLToList;
import com.lecture.SettingAndSubmit.SettingsCenter;
import com.lecture.layoutUtil.RefreshableView;
import com.lecture.layoutUtil.RefreshableView.PullToRefreshListener;
import com.lecture.lectureapp.R;



import com.lecture.localdata.Comment;
import com.lecture.localdata.Event;
import com.lecture.util.GetEventsHttpUtil;
import com.lecture.util.GetEventsHttpUtil.GetEventsCallback;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
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
	
	//数据库
	public static final String DB_NAME = "LectureDB";
	private DBCenter dbCenter = new DBCenter(this, DB_NAME, 1);
	//private List<Map<String, Object>> mData;
	private List<Event> mDataHot;
	private List<Event> mDataRemind;
	private List<Event> mDataSubscribe;

	
	private ViewPager mTabPager;	
	//private ImageView mTabImg;// 动画图片
	private ImageView mTab1,mTab2,mTab3,mTab4;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;//单个水平动画位移
	private int two;
	private int three;
	private int popMenuPushTime = 0;
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	//private Button mRightBtn;
	
	private ImageView mTab5;
	private int four;
	
	//获取main menu下的文字标题
	private TextView mText1;
	private TextView mText2;
	private TextView mText3;
	private TextView mText4;
	private TextView mText5;
	private HotMyadapter myadapterHot;
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

	private RefreshableView refreshableView_hot;
	private RefreshableView refreshableView_remind;
	private RefreshableView refreshableView_subscribe;

	private View viewHeader;
	private View viewFooter;
	//获取foot view的TextView引用进行没有任何讲座时的提醒
	private TextView foot_TextView;
	
	//下面的变量用于获取主菜单的按钮的layout句柄
	private LinearLayout lBtn1;
	private LinearLayout lBtn2;
	private LinearLayout lBtn3;
	private LinearLayout lBtn4;
	private LinearLayout lBtn5;
	
	
		// 个人中心 from Kun
		private EditText ETusername;// 用户名
		private EditText ETemail;// 邮箱
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
		
		
	
	//代码来自Yang
	LocalActivityManager manager = null;
	
	
	
	//下面代码来自 咸鱼  用于解决三个主要Tab之间切换时foot view的显示问题
	private MainItemFootLinearLayout hotFootItem;
	private MainItemFootLinearLayout subscribeFootItem;
	private MainItemFootLinearLayout remindFootItem;
	
	
	//下面是咸鱼的添加的代码，用于构建android的三个物理按键 2014 08 08 21:05   by xianyu
	private PopMenuView popMenu;
	
	
	
	
	//——————————————————————下面是用于handler的消息标记
	private static final int MESSAGE_REFRESH_START = 1;//刷新开始
	private static final int MESSAGE_REFRESH_END = 2;//刷新结束
	private static final int MESSAGE_REFRESH_FAILED = 3;//刷新失败
	private static final int MESSAGE_PULL_REFRESH_START = 4;//pull刷新开始
	private static final int MESSAGE_PULL_REFRESH_END = 5;//pull刷新结束
	private static final int MESSAGE_PULL_REFRESH_FAILED = 6;//pull刷新失败
	private static final int MESSAGE_PULL_REFRESH_LISTVIEW = 7;//pull成功，listView开始更新
	
	
	private ProgressDialog mProgressDialog;
		
		//第二个handler
		private Handler refreshHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				final String msg = (String) message.obj;
				Message msgRepost = Message.obtain();
				if (message.what == MESSAGE_REFRESH_START) {
					mProgressDialog = ProgressDialog.show(MainView.this,
							"请稍后", msg, true, false);
				} else if (message.what == MESSAGE_REFRESH_END) {
					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}	
						
						Log.i("开始数据初始化", "开始执行init");

						
						initHot();
						initSubscribe();
						initRemind();
						
						
						
						myadapterHot = new HotMyadapter(MainView.this, mDataHot);
						myadapterRemind = new RemindMyadapter(MainView.this, mDataRemind);
						myadapterSubscribe= new SubscribeMyadapter(MainView.this, mDataSubscribe);
					
						myadapterHot.setDBCenter(dbCenter);
						myadapterRemind.setDBCenter(dbCenter);
						myadapterSubscribe.setDBCenter(dbCenter);
						
						
						hotList.setAdapter(myadapterHot);
						remindList.setAdapter(myadapterRemind);
						subscribeList.setAdapter(myadapterSubscribe);
						
						refreshHot();
						refreshSubscribe();
						refreshRemind();

						refreshFoot("hotCenter");
						refreshFoot("subscribeCenter");
						refreshFoot("remindCenter");
						
						
						
						//下拉刷新执行部分
						refreshableView_hot.setOnRefreshListener(new PullToRefreshListener() {
							@Override
							public void onRefresh() {
								
								try {
								
									pullRefresh("hotCenter");
									
									
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								
							}
						}, 0);
						
						refreshableView_subscribe.setOnRefreshListener(new PullToRefreshListener() {
							@Override
							public void onRefresh() {
								
								try {
									

									pullRefresh("subscribeCenter");
									
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								
							}
						}, 0);
						
						refreshableView_remind.setOnRefreshListener(new PullToRefreshListener() {
							@Override
							public void onRefresh() {
								
								try {
									
									
									pullRefresh("remindCenter");
									
									
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}, 0);
						
						//下面上对item的默认点击显示颜色进行改变, 把默认点击效果取消
						hotList.setSelector(getResources().getDrawable(R.drawable.item_none_selector));
						hotList.setSelected(false);	
						remindList.setSelector(getResources().getDrawable(R.drawable.item_none_selector));
						remindList.setSelected(false);
						subscribeList.setSelector(getResources().getDrawable(R.drawable.item_none_selector));
						subscribeList.setSelected(false);
						
						// ListView 中某项被选中后的逻辑  
						hotList.setOnItemClickListener(new OnItemClickListener() {  
					        
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								
								Toast.makeText(MainView.this,"您选择了讲座：" + ((TextView)arg1.findViewById(R.id.lecture_id)).getText(),Toast.LENGTH_LONG ).show();
								
								//下面代码来自 KunCheng，用于显示详细信息
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataHot) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, DetailView.class);	
								intent.putExtras(detail_bundle);
								intent.putExtra("whichCenter", "hotCenter");
								startActivityForResult(intent, 2);
								overridePendingTransition(R.anim.show_in_right, R.anim.hide_in_left	);
							}  
					    });
						//remind
						remindList.setOnItemClickListener(new OnItemClickListener() {  
					        
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								 
								
								//下面代码来自 KunCheng，用于显示详细信息
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataRemind) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, DetailView.class);	
								intent.putExtras(detail_bundle);
								intent.putExtra("whichCenter", "remindCenter");
								startActivityForResult(intent, 2);
								overridePendingTransition(R.anim.show_in_right, R.anim.hide_in_left	);
								
							}  
					    });
						
						//subscribe
						subscribeList.setOnItemClickListener(new OnItemClickListener() {  
					        
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								
								//下面代码来自 KunCheng，用于显示详细信息
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataSubscribe) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, DetailView.class);	
								intent.putExtras(detail_bundle);
								intent.putExtra("whichCenter", "subscribeCenter");
								startActivityForResult(intent, 2);
								overridePendingTransition(R.anim.show_in_right, R.anim.hide_in_left	);
							}  
					    });
				
				} else if (message.what == MESSAGE_REFRESH_FAILED) {
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					
					Toast.makeText(MainView.this, msg, Toast.LENGTH_LONG)
							.show();
					//无法连接到网络，所以下面直接从原本数据库调用已有数据进行适配器的初始化
					Log.i("无法连接到网络", "直接用已有数据进行初始化！");
					
					msgRepost.what = MESSAGE_REFRESH_END;
					msgRepost.obj = "无法连接数据库直接从已有数据初始化...";
					refreshHandler.sendMessage(msgRepost);
					
					
				}
				else if(message.what == MESSAGE_PULL_REFRESH_START){
					
				}
				else if(message.what == MESSAGE_PULL_REFRESH_END){
					msgRepost = Message.obtain();
					msgRepost.what = MESSAGE_PULL_REFRESH_LISTVIEW;
					msgRepost.obj = message.obj;  // store variable called whichCenter
					refreshHandler.sendMessage(msgRepost);
					
				}
				else if(message.what == MESSAGE_PULL_REFRESH_FAILED){
					
					refreshableView_hot.finishRefreshing();
					
					Toast.makeText(MainView.this, "无法连接到网络，请检查您的网络设置！", Toast.LENGTH_LONG)
					.show();
					
			
				}
				else if(message.what == MESSAGE_PULL_REFRESH_LISTVIEW){
					
					Log.i("PULL REFRESH ", "刷新like和collection！");
					
					DBCenter.refreshLike(dbCenter.getReadableDatabase());
					DBCenter.refreshCollection(dbCenter.getReadableDatabase());
					
				
					
					//下面是 Xianyu做的修改，用于实现三个Tabs都能够刷新
					if(    ( (String)message.obj  ).equals("hotCenter")    ){
						refreshableView_hot.finishRefreshing();
						initHot();
						refreshHot();
						refreshFoot("hotCenter");
						
						
					}
					else 
					if(   ( (String)message.obj  ).equals("subscribeCenter")   ){
						
						refreshableView_subscribe.finishRefreshing();
						initSubscribe();
						refreshSubscribe();
						refreshFoot("subscribeCenter");
						
					}
					else
					if(   ( (String)message.obj  ).equals("remindCenter")   ){
						
						refreshableView_remind.finishRefreshing();
						initRemind();
						refreshRemind();
						refreshFoot("remindCenter");
						
					}
					
					
					
					
					
					Log.i("ListView刷新", "刷新ListView成功！");
				}

			}

		};
		//第二个handler结束	
		
		// 下面是第二个handler对应处理的refresh()
		public void refresh() {
			GetEventsHttpUtil getEventsUtil = GetEventsHttpUtil
					.getInstance(new GetEventsCallback() {

						@Override
						public void onStart() {
							Message msg = new Message();
							msg.what = MESSAGE_REFRESH_START;
							msg.obj = "正在同步...";
							refreshHandler.sendMessage(msg);
						}

						@Override
						public void onEnd() {
							
							//XML TO List, then to db
							XMLToList xmlToList = new XMLToList();
							
							xmlToList.insertListToDB(MainView.this, dbCenter, DBCenter.LECTURE_TABLE);
							// 上面可能出现错误
							Log.i("onEnd", "XMLToList已经将数据存入数据库！");
							
							//刷新like 和 remind数据表
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
							msg.obj = "无法连接到网络...";
							refreshHandler.sendMessage(msg);
						}
					});
			getEventsUtil.getInfo(this);
			
		}
		//--------------refresh() 结束
		
		
		//-------------------pull refresh--------------------------
		
		public void pullRefresh(final String whichCenter) {
			GetEventsHttpUtil getEventsUtil = GetEventsHttpUtil
					.getInstance(new GetEventsCallback() {

						@Override
						
						public void onStart() {
							Message msg = new Message();
							msg.what = MESSAGE_PULL_REFRESH_START;
							msg.obj = "正在刷新...";
							refreshHandler.sendMessage(msg);
						}

						@Override
						public void onEnd() {
							
							//XML TO List, then to db
							
							XMLToList xmlToList = new XMLToList();
							
							//删除数据库
							DBCenter.clearAllData(dbCenter.getReadableDatabase(), DBCenter.LECTURE_TABLE);
							xmlToList.insertListToDB(MainView.this, dbCenter, DBCenter.LECTURE_TABLE);
							
							Log.i("在RefreshCenter进行的操作", "XMLToList已经将数据存入数据库！");
							Message msg = new Message();
							msg.what = MESSAGE_PULL_REFRESH_END;
							msg.obj = whichCenter;
							refreshHandler.sendMessage(msg);
							
							
							
						}

						@Override
						public void onNoInternet() {
							Message msg = new Message();
							msg.what = MESSAGE_PULL_REFRESH_FAILED;
							msg.obj = "无法连接到网络...";
							refreshHandler.sendMessage(msg);
						}
					});
			getEventsUtil.getInfo(this);
			
		}
		
		//-------------------pull refresh end--------------------------



	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainview);
		
		 //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
       
        mTab1 = (ImageView) findViewById(R.id.img_hot_center);
        mTab2 = (ImageView) findViewById(R.id.img_subscribe_center);
        mTab3 = (ImageView) findViewById(R.id.img_remind_center);
        mTab4 = (ImageView) findViewById(R.id.img_submit_center);  
        mTab5 = (ImageView) findViewById(R.id.img_my_center);
       
        
      
        
        //获取菜单文字句柄
        mText1 = (TextView)findViewById(R.id.mText1);
        mText2 = (TextView)findViewById(R.id.mText2);
        mText3 = (TextView)findViewById(R.id.mText3);
        mText4 = (TextView)findViewById(R.id.mText4);
        mText5 = (TextView)findViewById(R.id.mText5);
        
        
        //获取菜单句柄，用于解决灵敏度问题
        lBtn1 = (LinearLayout)findViewById(R.id.hot_btn_layout);
        lBtn2 = (LinearLayout)findViewById(R.id.subscribe_btn_layout);
        lBtn3 = (LinearLayout)findViewById(R.id.remind_btn_layout);
        lBtn4 = (LinearLayout)findViewById(R.id.submit_btn_layout);
        lBtn5 = (LinearLayout)findViewById(R.id.my_btn_layout);
        
        lBtn1.setOnClickListener(new LayoutOnClickListener(0));
        lBtn2.setOnClickListener(new LayoutOnClickListener(1));
        lBtn3.setOnClickListener(new LayoutOnClickListener(2));
        lBtn4.setOnClickListener(new LayoutOnClickListener(3));
        lBtn5.setOnClickListener(new LayoutOnClickListener(4));
        
       
        
        
        Display currDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        //one = displayWidth/4; //设置水平动画平移大小
        one = displayWidth/5;
        two = one*2;
        three = one*3;
        four = one*4;
        
        Log.i("info", "获取的屏幕分辨率为" + one + two + three + "屏幕分辨率为" + displayWidth + "X" + displayHeight);
        
        //InitImageView();//使用动画
        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.subscribecenter, null);
        View view2 = mLi.inflate(R.layout.hotlecturecenter, null);
        View view3 = mLi.inflate(R.layout.remindcenter, null);
        View view4 = mLi.inflate(R.layout.submitcenter, null);
        View view5 = mLi.inflate(R.layout.mycenter, null);
        
        viewHeader = mLi.inflate(R.layout.head_view, null);
        viewHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
			}
        });
       
        hotFootItem       = new MainItemFootLinearLayout(this);
        subscribeFootItem = new MainItemFootLinearLayout(this);
        remindFootItem    = new MainItemFootLinearLayout(this);
       
        hotFootItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
			}
        });
        subscribeFootItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
			}
        });
        remindFootItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
			}
        });
        
        
        
        
        subscribeList = (ListView)view1.findViewById(R.id.list_view_subscribe);
        hotList = (ListView) view2.findViewById(R.id.list_view);//把hot_ListView转成引用
        remindList = (ListView)view3.findViewById(R.id.list_view_notice);

       
        
        hotList.addHeaderView(viewHeader);
        hotList.addFooterView(hotFootItem);
        
        subscribeList.addHeaderView(viewHeader);
        subscribeList.addFooterView(subscribeFootItem);
        
        remindList.addHeaderView(viewHeader);
        remindList.addFooterView(remindFootItem);
        

        refreshableView_subscribe = (RefreshableView)view1.findViewById(R.id.refreshable_view_subscribe);
        refreshableView_hot = (RefreshableView)view2.findViewById(R.id.refreshable_view);
        refreshableView_remind = (RefreshableView)view3.findViewById(R.id.refreshable_view_remind);
        
        //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view2);
        views.add(view1);
        views.add(view3);
        //code来自Yang
        manager = new LocalActivityManager(this , true);
        manager.dispatchCreate(savedInstanceState);
        Intent intent = new Intent(MainView.this, SubmitCenter.class);
        views.add(getView("SubmitCenter", intent));
        
        views.add(view5);
        
       
        
        //填充ViewPager的数据适配器
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
			
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
			
	
		};
		
		mTabPager.setAdapter(mPagerAdapter);
		
		//refresh()用于刷新XML文件，下载成功后并把XML转存到数据库，然后用于适配器使用
		refresh();
		
		
		// 个人中心 from Kun
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

		

		siming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "siming", "思");
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
					saveCampusAndTime(MainView.this, "xiangan","翔");
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
					saveCampusAndTime(MainView.this, "xiamen", "厦");
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
							saveCampusAndTime(MainView.this, "zhangzhou", "漳");
						} else {
							deleteCampusAndTime(MainView.this, "zhangzhou");
						}
					}
				});
		
		//下面代码中 1 代表周日 2 代表周一
		mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					saveCampusAndTime(MainView.this, "mon", "2");
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
					saveCampusAndTime(MainView.this, "tue", "3");
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
					saveCampusAndTime(MainView.this, "wed", "4");
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
					saveCampusAndTime(MainView.this, "thu", "5");
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
					saveCampusAndTime(MainView.this, "fri", "6");
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
					saveCampusAndTime(MainView.this, "sat", "7");
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
					saveCampusAndTime(MainView.this, "sun", "1");
				} else {
					deleteCampusAndTime(MainView.this, "sun");
				}
			}
		});
		
	}    // end function onCreate()
	
	//下面是来自Yebin的添加，用于解决没有任何讲座时的提醒显示
    
    public void setFootDisplay(String whichPage, String toDisplay, Boolean isDisplay){
    	
    	
    	if(whichPage.equals("hotCenter")){
    		
    			(  (TextView)hotFootItem.findViewById(R.id.top_textView)  ).setText( toDisplay );
            	
            	if( !isDisplay ){
            		(  (TextView)hotFootItem.findViewById(R.id.top_textView)  )
            					.setTextColor( getResources().getColor(R.color.item_transparent) );
                	(  (TextView)hotFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 10, 10, 80);
            	}
            	else{
            		(  (TextView)hotFootItem.findViewById(R.id.top_textView)  )
									.setTextColor( getResources().getColor(R.color.item_content) );

                	(  (TextView)hotFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 80, 10, 10);
            	}
            	
    		
    		myadapterHot.notifyDataSetChanged();
    		
    	}
    	else if(whichPage.equals("subscribeCenter")){
    		
    		
    			(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  ).setText( toDisplay );
            	
            	if( !isDisplay ){
            		(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  )
            					.setTextColor( getResources().getColor(R.color.item_transparent) );
                	(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 10, 10, 80);
            	}
            	else{
            		(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  )
									.setTextColor( getResources().getColor(R.color.item_content) );

                	(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 80, 10, 10);
            	}
            	
    		
    		myadapterSubscribe.notifyDataSetChanged();
    	}
    	else if(whichPage.equals("remindCenter")){
    		
    			(  (TextView)remindFootItem.findViewById(R.id.top_textView)  ).setText( toDisplay );
            	
            	if( !isDisplay ){
            		(  (TextView)remindFootItem.findViewById(R.id.top_textView)  )
            					.setTextColor( getResources().getColor(R.color.item_transparent) );
                	(  (TextView)remindFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 10, 10, 80);
            	}
            	else{
            		(  (TextView)remindFootItem.findViewById(R.id.top_textView)  )
									.setTextColor( getResources().getColor(R.color.item_content) );

                	(  (TextView)remindFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 80, 10, 10);
            	}
            	
    		myadapterRemind.notifyDataSetChanged();  
    		
    	}
    	else {
			
    		Log.i("Which Page", "设置错误!");
		}
    	
    }
    
    //下面是来自Yebin的添加，用于解决没有任何讲座时的提醒显示
    public void refreshFoot(String whichPage){
    	
    	if(whichPage.equals("hotCenter")){
    		
    		if(mDataHot.size() == 0){
            	setFootDisplay(whichPage, "没有最新热门讲座", true);
            	
    		}
    		else{
    			setFootDisplay(whichPage, "隐藏", false);
    		}
    		
    	}
    	else if(whichPage.equals("subscribeCenter")){
    		
    		if(mDataSubscribe.size() == 0)
    			setFootDisplay(whichPage, "没有您订制的讲座，您可以在设置中心\"我\"中进行定制!", true);
    		else{
    			setFootDisplay(whichPage, "隐藏", false);
    		}
    	}
    	else if(whichPage.equals("remindCenter")){
    		
    		if(mDataRemind.size() == 0){
    			setFootDisplay(whichPage, "没有您收藏的讲座", true);
    		
    		}
    		else{
    			setFootDisplay(whichPage, "隐藏", false);
    		}
    		
				
    	}
    	else {
			//setFootDisplay("隐藏FootView", false);
    		Log.i("Which Page", "设置错误!");
		}
    }
    
	
	//code来自Yang
	private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
	//初始化like和收藏数据
	public void initSubscribe(){
		
		Log.i("initLikeCollection", "开始");
		subscribeCursor = dbCenter.select(dbCenter.getReadableDatabase());//使用全部的讲座建立光标，然后再筛选
		
		//实现订阅  2014年 7月23 Typhoon today
		subscribeResult = DBCenter.L_convertCursorToListEventSubscribe(subscribeCursor,MainView.this);
		mDataSubscribe = subscribeResult;
		
	}
	//刷新Like和收藏界面
	public void refreshSubscribe(){
	
		myadapterSubscribe.setMData(mDataSubscribe);
		myadapterSubscribe.notifyDataSetChanged();
		
	}
	
	
	
	public void initRemind(){
		
		remindCursor = dbCenter.collectionSelect(dbCenter.getReadableDatabase());
		remindResult = DBCenter.L_convertCursorToListEvent(remindCursor);
		mDataRemind = remindResult;
		
	}
	public void refreshRemind(){
		
		myadapterRemind.setMData(mDataRemind);
		myadapterRemind.notifyDataSetChanged();
		
	}
	
	public void initHot(){

		hotCursor = dbCenter.hotSelect( dbCenter.getReadableDatabase() );
		hotResult = DBCenter.L_convertCursorToListEvent(hotCursor);
		mDataHot = hotResult;
		
		
	}
	public void refreshHot(){
		myadapterHot.setMData(mDataHot);
		myadapterHot.notifyDataSetChanged();
		
		
		
	}
	
	/**
	 * 使用SharedPreferences保存用户登录信息
	 * 
	 * @param context
	 * @param username
	 * @param password
	 */
	public static void saveLoginInfo(Context context, String username,
			String email) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		// 获取Editor对象
		Editor editor = sharedPre.edit();
		// 设置用户名和邮箱
		editor.putString("username", username.trim());
		editor.putString("email", email.trim());
		// 提交
		editor.commit();
	}

	// 个人中心--保存选中的校区和时间
	public static void saveCampusAndTime(Context context, String key, String value) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		// 获取Editor对象
		Editor editor = sharedPre.edit();
		// 设置校区或时间
		editor.putString(key, value);
		// 提交
		editor.commit();
	}

	// 个人中心--删除取消选择的校区和时间
	public static void deleteCampusAndTime(Context context, String str) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		if (sharedPre.contains(str)) {
			// 获取Editor对象
			Editor editor = sharedPre.edit();
			// 删除校区或时间
			editor.remove(str);
			// 提交
			editor.commit();
		}
	}
	
	
	// 下面解决主菜单点击灵敏度问题
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
    
	 /* 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_pressed));
				mText1.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				
				

				if (currIndex == 1) {
					
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initHot();
							refreshHot();
							refreshFoot("hotCenter");
						}
					}, 300);
					
					
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
					mText2.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 2) {
					
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initHot();
							refreshHot();
							refreshFoot("hotCenter");
						}
					}, 300);
					
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_normal));
					mText3.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 3) {
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initHot();
							refreshHot();
							refreshFoot("hotCenter");
						}
					}, 300);
					
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_normal));
					mText4.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}else if (currIndex == 4) {
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initHot();
							refreshHot();
							refreshFoot("hotCenter");
						}
					}, 300);
					
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_normal));
					mText5.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				
				
				
				break;
			case 1:
				
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_pressed));
				mText2.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				
				
				
				if (currIndex == 0) {
					
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initSubscribe();
							refreshSubscribe();
							refreshFoot("subscribeCenter");
					
						}
					}, 300);
					
					
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 2) {
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initSubscribe();
							refreshSubscribe();
							refreshFoot("subscribeCenter");
					
						}
					}, 300);

					
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_normal));
					mText3.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 3) {
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initSubscribe();
							refreshSubscribe();
							refreshFoot("subscribeCenter");
					
						}
					}, 300);

					
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_normal));
					mText4.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}else if (currIndex == 4) {
					
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initSubscribe();
							refreshSubscribe();
							refreshFoot("subscribeCenter");
					
						}
					}, 300);

					
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_normal));
					mText5.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				
				
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_pressed));
				mText3.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				
				
				if (currIndex == 0) {

					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initRemind();
							refreshRemind();
							refreshFoot("remindCenter");
					
						}
					}, 300);

					
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {
					
					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initRemind();
							refreshRemind();
							refreshFoot("remindCenter");
					
						}
					}, 300);
					
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
					mText2.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 3) {


					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initRemind();
							refreshRemind();
							refreshFoot("remindCenter");
					
						}
					}, 300);
					
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_normal));
					mText4.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}else if (currIndex == 4) {


					//下面是咸鱼的修改，用于解决tab切换的卡顿！昨晚花了几小时，早上又是一早上，终于解决了   2014年8月10日 11:33
					new Handler().postDelayed(new Runnable() 
					{

						@Override
						public void run() 
						{
							Log.i("使用线程延迟", "咸鱼要加油！");
							initRemind();
							refreshRemind();
							refreshFoot("remindCenter");
					
						}
					}, 300);
					
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_normal));
					mText5.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				
				
				
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_pressed));
				mText4.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				if (currIndex == 0) {

					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {

					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
					mText2.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 2) {

					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_normal));
					mText3.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 4) {
					
					mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_normal));
					mText5.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}

				break;
			case 4:
				mTab5.setImageDrawable(getResources().getDrawable(R.drawable.mycenter_pressed));
				mText5.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				if (currIndex == 0) {

					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {

					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
					mText2.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				else if (currIndex == 2) {

					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.noticecenter_normal));
					mText3.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}else if (currIndex == 3) {

					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.submitcenter_normal));
					mText4.setTextColor(getResources().getColor(R.color.main_menu_normal));
				}
				//显示个人中心用户的设置 form Kun
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
			
			
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	
	// 个人中心的保存设置按钮
			public void save(View view) {

				String u = ETusername.getText().toString().trim();
				String e = ETemail.getText().toString().trim();
				
				/*
				if(u.equals("") || u.isEmpty())
				{
					Toast.makeText(MainView.this, "用户名不能为空", Toast.LENGTH_LONG).show();
					ETusername.requestFocus();
				}
				*/
				
				if (isEmail(e) || e.equals("") || e.isEmpty()) 
				{
					Toast.makeText(MainView.this, "保存成功", Toast.LENGTH_LONG).show();
					saveLoginInfo(MainView.this, u, e);
				}
				else
				{
					Toast.makeText(MainView.this, "无效的邮箱地址", Toast.LENGTH_LONG).show();
					ETemail.requestFocus();
				}
				
			}

			// 邮箱判断正则表达式
			public static boolean isEmail(String email) {
				
				Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
		        Matcher matcher = pattern.matcher(email);

		        if(matcher.matches()) {
		        	return true;
		        }else {
		        	return false;
		        }
			}


			public class MainItemFootLinearLayout extends LinearLayout {

				public MainItemFootLinearLayout(Context context) {
					super(context);
					// TODO Auto-generated constructor stub

					((Activity) getContext()).getLayoutInflater().inflate(
							R.layout.foot_view, this);
				}

				public MainItemFootLinearLayout(Context context, AttributeSet attrs) {
					super(context, attrs);
					// TODO Auto-generated constructor stub
				}

				public MainItemFootLinearLayout(Context context, AttributeSet attrs,
						int defStyle) {
					super(context, attrs, defStyle);
					// TODO Auto-generated constructor stub
				}

			}
	
	//下面的代码先做保留，用于处理android的 三颗 虚拟按键
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK ) {  //获取 back键
    		
    		
        	
    	}
    	else if( keyCode == KeyEvent.KEYCODE_MENU ){   //获取 Menu键			
    				
    			//实例化
    			popMenu = new PopMenuView(MainView.this, itemsOnClick);
    			//显示窗口
    			popMenu.showAtLocation( MainView.this.findViewById(R.id.mainview) , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    			
    			
    			
    		
    		
    		
		}
    	return false;
    }

	//为弹出窗口popMenu实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.about_author:
				Toast.makeText(MainView.this, "关于作者", Toast.LENGTH_LONG).show();
				break;
			case R.id.about_xmu_lecture:
				Toast.makeText(MainView.this, "关于讲座网", Toast.LENGTH_LONG).show();
				break;
			case R.id.about_app:
				Toast.makeText(MainView.this, "关于应用", Toast.LENGTH_LONG).show();
				break;
			case R.id.pop_exitImageView:
				Toast.makeText(MainView.this, "已退出！", Toast.LENGTH_LONG).show();
				MainView.this.finish();
				break;
				
			default:
				break;
			}
		}
    };
	
    
    
    
    
    
	// 下面的增加来自咸鱼，用于详情中点赞、收藏后的刷新操作！ 2014年8月10日 13:43
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (data != null && resultCode == 2) {
			// 数据存在，进行获取
			String whichCenter = data.getStringExtra("whichCenter");

			if (whichCenter == null) {
				whichCenter = "";
				Log.i("出现错误", "请通知开发者！");

			}

			if (whichCenter.equals("hotCeter")) {

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						initHot();
						refreshHot();
						refreshFoot("hotCenter");

					}
				}, 200);

			} else if (whichCenter.equals("subscribeCenter")) {

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						initSubscribe();
						refreshSubscribe();
						refreshFoot("subscribeCenter");

					}
				}, 200);

			} else if (whichCenter.equals("remindCenter")) {

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						initRemind();
						refreshRemind();
						refreshFoot("remindCenter");

					}
				}, 200);

			}  // end remindCenter if

		}  // data != null

	}   // end onActivityResult
    
    
	
	

}//  end class MainView
