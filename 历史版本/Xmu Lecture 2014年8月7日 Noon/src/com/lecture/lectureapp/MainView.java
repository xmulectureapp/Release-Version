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
import android.R.integer;
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
import android.text.format.Time;
import android.util.AttributeSet;
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
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private boolean menu_display = false;
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
						
						Log.i("SELECT", "Cursor游标采取数据开始！");

						
						initHot();
						initLikeCollection();
						refreshFoot("hotCenter");

						//下面用于测试subStrign函数
						//Log.i("subString", mDataHot.get(1).getAddress().substring(0, 1));
					
						
						myadapterHot = new HotMyadapter(MainView.this, mDataHot);
						myadapterRemind = new RemindMyadapter(MainView.this, mDataRemind);
						myadapterSubscribe= new SubscribeMyadapter(MainView.this, mDataSubscribe);
					
						myadapterHot.setDBCenter(dbCenter);
						myadapterRemind.setDBCenter(dbCenter);
						myadapterSubscribe.setDBCenter(dbCenter);
						
						Log.i("Myadapter", "适配器构建成功！");
					    
						hotList.setAdapter(myadapterHot);
						remindList.setAdapter(myadapterRemind);
						subscribeList.setAdapter(myadapterSubscribe);
						
						refreshHot();
						refreshLikeCollection();
						
						//下拉刷新执行部分
						refreshableView_hot.setOnRefreshListener(new PullToRefreshListener() {
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
									pullRefresh("hotCenter");
									
									
									Thread.sleep(3000);
									//Thread.sleep(3000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								//Toast.makeText(MainView.this, "正在刷新……", Toast.LENGTH_LONG).show();
								//refreshableView.finishRefreshing(myadapter);
							}
						}, 0);
						
						refreshableView_subscribe.setOnRefreshListener(new PullToRefreshListener() {
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
									//pullRefresh();
									
									//下面将修改下拉刷新，使得前三个Tabs都可以刷新
									//Thread.sleep(3000);
									//refreshableView_subscribe.finishRefreshing();
									pullRefresh("subscribeCenter");
									
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								//Toast.makeText(MainView.this, "正在刷新……", Toast.LENGTH_LONG).show();
								//refreshableView.finishRefreshing(myadapter);
							}
						}, 0);
						
						refreshableView_remind.setOnRefreshListener(new PullToRefreshListener() {
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
									//pullRefresh();
									
									
									//下面将修改下拉刷新，使得前三个Tabs都可以刷新
									//Thread.sleep(3000);
									//refreshableView_remind.finishRefreshing();
									pullRefresh("remindCenter");
									
									//Thread.sleep(3000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								//Toast.makeText(MainView.this, "正在刷新……", Toast.LENGTH_LONG).show();
								//refreshableView.finishRefreshing(myadapter);
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
								// TODO Auto-generated method stub
								
								Toast.makeText(MainView.this,"您选择了讲座：" + ((TextView)arg1.findViewById(R.id.lecture_id)).getText(),Toast.LENGTH_LONG ).show();
								
								//下面代码来自 KunCheng，用于显示详细信息
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataHot) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, DetailView.class);	
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
								 
								Toast.makeText(MainView.this,"您选择了讲座：" + ((TextView)arg1.findViewById(R.id.lecture_id)).getText(),Toast.LENGTH_LONG ).show();
								
								//下面代码来自 KunCheng，用于显示详细信息
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataRemind) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, DetailView.class);	
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
								
								//Toast.makeText(MainView.this,"您选择了讲座：" + ((TextView)arg1.findViewById(R.id.lecture_id)).getText(),Toast.LENGTH_LONG ).show();
								
								//下面代码来自 KunCheng，用于显示详细信息
								Bundle detail_bundle = new Bundle();
								for (Event event : mDataSubscribe) {
									if(event.getUid() == ((TextView)arg1.findViewById(R.id.lecture_id)).getText() )
									detail_bundle.putSerializable("LectureDetail", event);
								}
								
								Intent intent = new  Intent(MainView.this, DetailView.class);	
								intent.putExtras(detail_bundle);
								startActivity(intent);
							}  
					    });
				/*	// 这里是为了解决没有网络数据时的显示
					}
					else{
						
						
					}  // end else in MESSAGE_REFRESH_END
					*/
				} else if (message.what == MESSAGE_REFRESH_FAILED) {
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					//mProgressDialog = null;
					//mProgressDialog = ProgressDialog.show(MainView.this,"加载上次数据数据", "", true, false);
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
					
					refreshableView_hot.finishRefreshing(myadapterHot);
					
					Toast.makeText(MainView.this, "无法连接到网络，请检查您的网络设置！", Toast.LENGTH_LONG)
					.show();
					
			
				}
				else if(message.what == MESSAGE_PULL_REFRESH_LISTVIEW){
					
					Log.i("PULL REFRESH ", "刷新like和collection！");
					
					DBCenter.refreshLike(dbCenter.getReadableDatabase());
					DBCenter.refreshCollection(dbCenter.getReadableDatabase());
					
					Log.i("MESSAGE_XML_TO_LISTDB_SUCCESS", "光标Cursor准备就绪！");
					
					
					//下面是 Xianyu做的修改，用于实现三个Tabs都能够刷新
					if(    ( (String)message.obj  ).equals("hotCenter")    ){
						refreshableView_hot.finishRefreshing(myadapterHot);
						initHot();
						refreshHot();
						refreshFoot("hotCenter");
						
						
					}
					else 
					if(   ( (String)message.obj  ).equals("subscribeCenter")   ){
						
						refreshableView_subscribe.finishRefreshing();
						initLikeCollection();
						refreshLikeCollection();
						refreshFoot("subscribeCenter");
						
					}
					else
					if(   ( (String)message.obj  ).equals("remindCenter")   ){
						
						refreshableView_remind.finishRefreshing();
						initLikeCollection();
						refreshLikeCollection();
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
							//DBCenter.clearAllData(dbCenter.getReadableDatabase(), DBCenter.LECTURE_TABLE);
							xmlToList.insertListToDB(MainView.this, dbCenter, DBCenter.LECTURE_TABLE);
							// 上面可能出现错误
							Log.i("onEnd", "XMLToList已经将数据存入数据库！");
							
							Log.i("onEnd", "开始refresh like 和 收藏");
							
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
       
        
        //测试全角半角的区别
        if("&" == "&")
        	Log.i("全角半角", "相等");
       // mTabImg = (ImageView) findViewById(R.id.img_tab_now);
        /*
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));      
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));      
        mTab5.setOnClickListener(new MyOnClickListener(4));
        */
        
        //下面用于测试时间
        /*
        SettingsCenter testSetting = new SettingsCenter(MainView.this);
      //  int weekday = testSetting.stringToTime("2014-6-24 20:20").weekDay;
        Time time = testSetting.time;
        Log.i("最后测试", String.format("%d", time.weekDay));
        
        Log.i("WEEKDAY", String.format("%d", weekday));
        String week = testSetting.weekdaySettings;
        String place = testSetting.placeSettings;
        
        Time t = new Time();
        Log.i("系统时间", String.format("%d", t.weekDay));
        t.setToNow();
        Log.i("系统时间", t.toString());
        Log.i("系统时间", String.format("%d", t.weekDay));
        
        */
        
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
        View view3 = mLi.inflate(R.layout.noticecenter, null);
        View view4 = mLi.inflate(R.layout.submitcenter, null);
        View view5 = mLi.inflate(R.layout.mycenter, null);
        
        viewHeader = mLi.inflate(R.layout.head_view, null);
        viewHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
			}
        });
        /*
        viewFooter = mLi.inflate(R.layout.foot_view, null);
      
        viewFooter.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
				
				}
        });
       */
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
        
        
        
        //下面是来自Yebin的添加，用于解决没有任何讲座时的提醒显示
        //foot_TextView = (TextView)viewFooter.findViewById(R.id.foot_textView);

        subscribeList = (ListView)view1.findViewById(R.id.list_view_subscribe);
        hotList = (ListView) view2.findViewById(R.id.list_view);//把hot_ListView转成引用
        remindList = (ListView)view3.findViewById(R.id.list_view_notice);

       
        
        hotList.addHeaderView(viewHeader);
       // hotList.addFooterView(viewFooter);
        hotList.addFooterView(hotFootItem);
        
        subscribeList.addHeaderView(viewHeader);
        //subscribeList.addFooterView(viewFooter);
        subscribeList.addFooterView(subscribeFootItem);
        
        remindList.addHeaderView(viewHeader);
        //remindList.addFooterView(viewFooter);
        remindList.addFooterView(remindFootItem);

        refreshableView_subscribe = (RefreshableView)view1.findViewById(R.id.refreshable_view_subscribe);
        refreshableView_hot = (RefreshableView)view2.findViewById(R.id.refreshable_view);
        refreshableView_remind = (RefreshableView)view3.findViewById(R.id.refreshable_view_remind);
        //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view2);
        views.add(view1);
        views.add(view3);
       // views.add(view4);
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

		// 只能输入邮箱格式
		/*
		 * ETemail.addTextChangedListener(new TextWatcher() { public void
		 * afterTextChanged(Editable s) { if
		 * (ETemail.getText().toString().matches
		 * ("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && s.length() > 0) {
		 * Toast.makeText(MainView.this, "有效的邮箱地址", Toast.LENGTH_LONG).show(); }
		 * else { Toast.makeText(MainView.this, "无效的邮箱地址",
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
    public void setFootDisplay(String toDisplay, Boolean isDisplay){
    	
    	
    	foot_TextView.setText(toDisplay);
    	
    	//设置是否显示
    	if(isDisplay){
    		foot_TextView.setTextColor(getResources().getColor(R.color.item_content));
        	foot_TextView.setPadding(20,120,20,20);	
    	}
    	else{
    		foot_TextView.setTextColor(getResources().getColor(R.color.item_transparent));
        	foot_TextView.setPadding(20,20,20,20);
    	}
    }
    
    
    public void setFootDisplay(String whichPage, String toDisplay, Boolean isDisplay){
    	
    	
    	if(whichPage.equals("hotCenter")){
    		
    		if(mDataHot.size() == 0){
            	(  (TextView)hotFootItem.findViewById(R.id.top_textView)  ).setText( toDisplay );
            	
            	if( !isDisplay ){
            		(  (TextView)hotFootItem.findViewById(R.id.top_textView)  )
            					.setTextColor( getResources().getColor(R.color.item_transparent) );
                	(  (TextView)hotFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 10, 10, 100);
            	}
            	else{
            		(  (TextView)hotFootItem.findViewById(R.id.top_textView)  )
									.setTextColor( getResources().getColor(R.color.item_content) );

                	(  (TextView)hotFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 120, 10, 10);
            	}
            	
    		}
    		else{
    			(  (TextView)hotFootItem.findViewById(R.id.top_textView)  )
						.setTextColor( getResources().getColor(R.color.item_transparent) );
    		}
    		
    	}
    	else if(whichPage.equals("subscribeCenter")){
    		
    		if(mDataSubscribe.size() == 0){
            	(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  ).setText( toDisplay );
            	
            	if( !isDisplay ){
            		(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  )
            					.setTextColor( getResources().getColor(R.color.item_transparent) );
                	(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 10, 10, 100);
            	}
            	else{
            		(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  )
									.setTextColor( getResources().getColor(R.color.item_content) );

                	(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 120, 10, 10);
            	}
            	
    		}
    		else{
    			(  (TextView)subscribeFootItem.findViewById(R.id.top_textView)  )
						.setTextColor( getResources().getColor(R.color.item_transparent) );
    		}
    		
    	}
    	else if(whichPage.equals("remindCenter")){
    		
    		if(mDataRemind.size() == 0){
            	(  (TextView)remindFootItem.findViewById(R.id.top_textView)  ).setText( toDisplay );
            	
            	if( !isDisplay ){
            		(  (TextView)remindFootItem.findViewById(R.id.top_textView)  )
            					.setTextColor( getResources().getColor(R.color.item_transparent) );
                	(  (TextView)remindFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 10, 10, 100);
            	}
            	else{
            		(  (TextView)remindFootItem.findViewById(R.id.top_textView)  )
									.setTextColor( getResources().getColor(R.color.item_content) );

                	(  (TextView)remindFootItem.findViewById(R.id.top_textView)  ).setPadding(10, 120, 10, 10);
            	}
            	
    		}
    		else{
    			(  (TextView)remindFootItem.findViewById(R.id.top_textView)  )
						.setTextColor( getResources().getColor(R.color.item_transparent) );
    		}
    		
				
    	}
    	else {
			//setFootDisplay("隐藏FootView", false);
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
    /*
    public void refreshFoot(String whichPage){
    	
    	if(whichPage.equals("hotCenter")){
    		
    		if(mDataHot.size() == 0){
            	setFootDisplay("没有最新热门讲座", true);
            	
    		}
    		else{
    			setFootDisplay("隐藏", false);
    		}
    		
    	}
    	else if(whichPage.equals("subscribeCenter")){
    		
    		if(mDataSubscribe.size() == 0)
    			setFootDisplay("没有您订制的讲座，您可以在设置中心\"我\"中进行定制!", true);
    		else{
    			setFootDisplay("隐藏", false);
    		}
    	}
    	else if(whichPage.equals("remindCenter")){
    		
    		if(mDataRemind.size() == 0){
    			setFootDisplay("没有您收藏的讲座", true);
    		
    		}
    		else{
    			setFootDisplay("隐藏", false);
    		}
    		
				
    	}
    	else {
			setFootDisplay("隐藏FootView", false);
		}
    }
	*/
	
	//code来自Yang
	private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
	//初始化like和收藏数据
	public void initLikeCollection(){
		
		Log.i("initLikeCollection", "开始");
		subscribeCursor = dbCenter.select(dbCenter.getReadableDatabase(), null, null,null);//使用全部的讲座建立光标，然后再筛选
		remindCursor = dbCenter.collectionSelect(dbCenter.getReadableDatabase());
		
		//实现订阅  2014年 7月23 Typhoon today
		subscribeResult = DBCenter.L_convertCursorToListEventSubscribe(subscribeCursor,MainView.this);
		remindResult = DBCenter.L_convertCursorToListEvent(remindCursor);
		
		mDataRemind = remindResult;
		mDataSubscribe = subscribeResult;
		
		
	}
	//刷新Like和收藏界面
	public void refreshLikeCollection(){
		
		Log.i("refreshLikeCollection", "开始");
		
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
	
	/**
	 * 头标点击监听
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
				initHot();
				refreshHot();
				refreshFoot("hotCenter");

				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
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
				
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_pressed));
				mText2.setTextColor(getResources().getColor(R.color.main_menu_pressed));
				
				initLikeCollection();
				refreshLikeCollection();
				refreshFoot("subscribeCenter");
				
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
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
				refreshFoot("remindCenter");
				
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
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
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
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
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.hotlecturecenter_normal));
					mText1.setTextColor(getResources().getColor(R.color.main_menu_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, four, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_normal));
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
			animation.setFillAfter(true);// True:图片停在动画结束位置
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
	
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //获取 back键
//    		
//        	if(menu_display){         //如果 Menu已经打开 ，先关闭Menu
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
//    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //获取 Menu键			
//			if(!menu_display){
//				//获取LayoutInflater实例
//				inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
//				//这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
//				//该方法返回的是一个View的对象，是布局中的根
//				layout = inflater.inflate(R.layout.main_menu, null);
//				
//				//下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
//				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //后两个参数是width和height
//				//menuWindow.showAsDropDown(layout); //设置弹出效果
//				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
//				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
//				//如何获取我们main中的控件呢？也很简单
//				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
//				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
//				
//				
//				//下面对每一个Layout进行单击事件的注册吧。。。
//				//比如单击某个MenuItem的时候，他的背景色改变
//				//事先准备好一些背景图片或者颜色
//				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
//					@Override
//					public void onClick(View arg0) {						
//						//Toast.makeText(Main.this, "退出", Toast.LENGTH_LONG).show();
//						Intent intent = new Intent();
//			        	intent.setClass(MainWeixin.this,Exit.class);
//			        	startActivity(intent);
//			        	menuWindow.dismiss(); //响应点击事件之后关闭Menu
//					}
//				});				
//				menu_display = true;				
//			}else{
//				//如果当前已经为显示状态，则隐藏起来
//				menuWindow.dismiss();
//				menu_display = false;
//				}
//			
//			return false;
//		}
//    	return false;
//    }
//	//设置标题栏右侧按钮的作用
//	public void btnmainright(View v) {  
//		Intent intent = new Intent (MainWeixin.this,MainTopRightDialog.class);			
//		startActivity(intent);	
//		//Toast.makeText(getApplicationContext(), "点击了功能按钮", Toast.LENGTH_LONG).show();
//      }  	
//	public void startchat(View v) {      //小黑  对话界面
//		Intent intent = new Intent (MainWeixin.this,ChatActivity.class);			
//		startActivity(intent);	
//		//Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
//      }  
//	public void exit_settings(View v) {                           //退出  伪“对话框”，其实是一个activity
//		Intent intent = new Intent (MainWeixin.this,ExitFromSettings.class);			
//		startActivity(intent);	
//	 }
//	public void btn_shake(View v) {                                   //手机摇一摇
//		Intent intent = new Intent (MainWeixin.this,ShakeActivity.class);			
//		startActivity(intent);	
//	}
	
	

}
