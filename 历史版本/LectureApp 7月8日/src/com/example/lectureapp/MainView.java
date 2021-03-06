package com.example.lectureapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainView extends Activity 
{
	public static MainView instance = null;
	 
	private ViewPager mTabPager;	
	private ImageView mTabImg;// 动画图片
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.mainview);
		
		 //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        /*
        mRightBtn = (Button) findViewById(R.id.right_btn);
        mRightBtn.setOnClickListener(new Button.OnClickListener()
		{	@Override
			public void onClick(View v)
			{	showPopupWindow (MainWeixin.this,mRightBtn);
			}
		  });*/
        
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        mTab1 = (ImageView) findViewById(R.id.img_subscribecenter);
        mTab2 = (ImageView) findViewById(R.id.img_hotlecturecenter);
        mTab3 = (ImageView) findViewById(R.id.img_noticecenter);
        mTab4 = (ImageView) findViewById(R.id.img_submitcenter);
        
        mTab5 = (ImageView) findViewById(R.id.img_mycenter);
        
        mTabImg = (ImageView) findViewById(R.id.img_tab_now);
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));
        
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));
        
        mTab5.setOnClickListener(new MyOnClickListener(4));
        
        //获取菜单文字句柄
        mText1 = (TextView)findViewById(R.id.mText1);
        mText2 = (TextView)findViewById(R.id.mText2);
        mText3 = (TextView)findViewById(R.id.mText3);
        mText4 = (TextView)findViewById(R.id.mText4);
        mText5 = (TextView)findViewById(R.id.mText5);
        
        
        Display currDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        //one = displayWidth/4; //设置水平动画平移大小
        one = displayWidth/5;
        two = one*2;
        three = one*3;
        four = one*4;
        //Log.i("info", "获取的屏幕分辨率为" + one + two + three + "X" + displayHeight);
        
        //InitImageView();//使用动画
      //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.subscribecenter, null);
        View view2 = mLi.inflate(R.layout.hotlecturecenter, null);
        View view3 = mLi.inflate(R.layout.noticecenter, null);
        View view4 = mLi.inflate(R.layout.submitcenter, null);
        
        View view5 = mLi.inflate(R.layout.mycenter, null);
        ListView list = (ListView) view2.findViewById(R.id.hot_ListView);//把hot_ListView转成引用
      //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        
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
		
		//来自Yao的更改 2014年7月7号
		final String[] LTitle = { "An introduction to nonparametric regression", 
				"台湾土壤与地下水污染及整治技术现况",
				"中国证监会对股市改革政策的创新与发展",
				"戏剧与哲学",
				"哈佛诗生活"};  
	    String[] LTime = { "2014年07月08日（星期二）14点30分", 
	    		"2014年07月08日（星期二）14点30分", 
	    			"2014年07月08日（星期二）16点30分",
	    			"2014年07月08日（星期二）19点00分",
	    			"2014年07月08日（星期二）19点00分" };  
	    String[] LAddr = { "【思明校区】经济楼N301",
	    		"【翔安校区】环境与生态学院A201",
	    		"【漳州校区】人文大楼B#301",
	    		"【思明校区】南光一214", 
	    		"【思明校区】外文学院三楼会议室" }; 
	    String[] LSpeaker = { "Daniel Henderson 副教授", 
	    		"林财富教授",
	    		"王春源", 
	    		"方旭东 教授", 
	    		"李美华" };
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
	    for(int i=0;i<5;i++)
	    {
	    	HashMap<String,Object> map = new HashMap<String,Object>();
	    	map.put("lecture_name",LTitle[i]);
	    	map.put("lecture_time","时间: "+LTime[i]);
	    	map.put("lecture_addr","地点: "+LAddr[i]);
	    	map.put("lecture_speaker","主讲: "+LSpeaker[i]);
	    	listItem.add(map);
		}
	    //构造适配器
	    SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.item,
	    		new String[]{"lecture_name","lecture_time","lecture_addr","lecture_speaker"},
	    		new int[]{R.id.lecture_name,R.id.lecture_time,R.id.lecture_addr,R.id.lecture_speaker
	    		});
	    list.setAdapter(listItemAdapter);
	    list.setOnItemClickListener(new OnItemClickListener() {  
	        
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 Toast.makeText(MainView.this,"您选择了讲座：" 
				+ LTitle[arg2],Toast.LENGTH_LONG ).show();  
			}  
	    });  
	}
	
	/**
	 * 头标点击监听
	 */
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
    
	 /* 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.subscribecenter_pressed));
				mText1.setTextColor(getResources().getColor(R.color.main_menu_pressed));
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
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
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
