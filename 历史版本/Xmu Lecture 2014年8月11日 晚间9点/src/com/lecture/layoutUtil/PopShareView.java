package com.lecture.layoutUtil;
import com.lecture.lectureapp.MainView;
import com.lecture.lectureapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class PopShareView extends PopupWindow  {


	private ImageView popExitImageView;
	private View mMenuView;
	
	
	public PopShareView(Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_share, null);
		
		//下面是咸鱼的增加，用于实现 menu键的菜单出现／消失 2014-08-11  20:23
		this.setAnimationStyle(android.R.style.Animation_InputMethod);  
	    this.update(); 
	    this.setOutsideTouchable(true);
		this.setTouchable(true); 
		 mMenuView.setFocusableInTouchMode(true);  
		 /*
		 mMenuView.setOnKeyListener(new View.OnKeyListener() {
			 private int tap = 0;
		        @Override  
		        public boolean onKey(View v, int keyCode, KeyEvent event) {  
		            // TODO Auto-generated method stub 
		        	if(keyCode == KeyEvent.KEYCODE_MENU)
		        		tap++;
		            if ((keyCode == KeyEvent.KEYCODE_MENU) && tap == 2 ) {  
		            	PopShareView.this.dismiss();// 这里写明模拟menu的PopupWindow退出就行  
		              
		            }  
		            return false;  
		        }  
		    });
		    
		    */
		
		// end edited by xianyu   2014-08-11  20:23
		 
		popExitImageView = (ImageView) mMenuView.findViewById(R.id.share_exit);
		popExitImageView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dismiss();
				
			}
		});
		
		(  (RelativeLayout)mMenuView.findViewById(R.id.wechat_share)  ).setOnClickListener( itemsOnClick );
		(  (RelativeLayout)mMenuView.findViewById(R.id.wechat_circle_share)  ).setOnClickListener( itemsOnClick );
		(  (RelativeLayout)mMenuView.findViewById(R.id.weibo_share)  ).setOnClickListener( itemsOnClick );
		
		
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		
		
		//xianyu edit
		
		
		ColorDrawable dw = new ColorDrawable(0x80000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}
	
	
	

	
   

}
