package com.lecture.lectureapp.wxapi;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.lecture.layoutUtil.PopMenuView;
import com.lecture.lectureapp.MainView;
import com.lecture.lectureapp.R;
import com.lecture.localdata.Event;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.modelmsg.WXImageObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler  {
	
	
	private final String WX_PACKAGE_NAME = "com.tencent.mm";
	public static final String APP_ID = "wx932efd1a7f9f947c";
	Event eventToShare;
	String shareDirection;
	Boolean isReopen = false;
	private final int THUMB_SIZE = 40;
	private Event event;
	
	
	
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
		
		
		setContentView(R.layout.wechat_share);
		
		event = (Event) getIntent().getSerializableExtra("shareEvent");
		if(event == null){
			finish();
			// error
			Log.i("微信分享错误", "event为NULL！");
		}
		
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(WXEntryActivity.this, APP_ID, true);
		api.registerApp(APP_ID);
		api.handleIntent(getIntent(), WXEntryActivity.this);
		
	/*	
		isReopen = new Bundle().getBoolean("isReturn");
		
		if(isReopen == true)
			finish();
		
		*/
		Log.i("顺序", " onCreate");
		
		if(isReopen){
			Log.i("顺序", " 重新打开!");
			finish();
		}
		

		//finish();
		
	}

	public WXEntryActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		
		
		
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			Log.i("顺序", " onResp");
			isReopen = true;
			
			result = "发送成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送失败";
			break;
		default:
			result = "出现异常";
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		Log.i("顺序", " onNewIntent");
		super.onNewIntent(intent);

		
		setIntent(intent);
		api.handleIntent(intent, this);
		
	}
	
	/**
	 * 更新为微信朋友圈状态
	 * 
	 * @param text
	 *            文本
	 * @param desc
	 *            描述
	 */
	public void updateWXStatus(String text, String desc) {

		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = desc;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); //
		// transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;

		// 调用api接口发送数据到微信
		api.sendReq(req);
		
		finish();
	}
	
	public void wxUrl(String text, String desc) {

		
		
		//
		
		WXWebpageObject webPage = new WXWebpageObject();
		webPage.webpageUrl = "http://lecture.xmu.edu.cn";
		
		WXImageObject imgObj;
		WXMediaMessage msg = new WXMediaMessage();
		
		
		try {
			String url = "http://lecture.xmu.edu.cn/wechatshare.png"; 
			imgObj = new WXImageObject();
			imgObj.imageUrl = url;

			//msg.mediaObject = imgObj;
/*
			Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
					THUMB_SIZE, true);
			bmp.recycle();
	*/		
			Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.wechatshare);  
		    msg.setThumbImage(thumb);  
			//msg.thumbData = bmpToByteArray(thumbBmp, true);
			//msg.setThumbImage(thumbBmp);

			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//
		

		
		// 用WXTextObject对象初始化一个WXMediaMessage对象
		msg.mediaObject = webPage;
		
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = desc;
		msg.title="厦大讲座网";
		

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img"); //
		// transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;

		// 调用api接口发送数据到微信
		api.sendReq(req);
		Bundle bundle = new Bundle();
		bundle.putBoolean("isReturn", true);
		
		finish();
	}
	
	private String buildTransaction(final String type) {

		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();

	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK ) {  //获取 back键
    		
    		finish();
    		
        	
    	}
    	else if( keyCode == KeyEvent.KEYCODE_MENU ){   //获取 Menu键			
    				
    		//updateWXStatus(" 来自厦大讲座的第一条测试分享","测试");
    		wxUrl("", "一则讲座，改变一生");
    		
    		
    		
		}
    	return false;
    }
	
	private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	

}// end activity
