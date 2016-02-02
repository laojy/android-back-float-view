package com.example.lao;

import java.io.DataOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	private static WindowManager wm;  
	private static WindowManager.LayoutParams params;  
	private ImageButton btn_floatView;
	 private Process localProcess;
	 private static boolean kk=true;
	 private int width,height,h;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 width=getWindowManager().getDefaultDisplay().getWidth();
		  height=getWindowManager().getDefaultDisplay().getHeight();
		  h=getTitleHeight(this);
		  Log.e("h",String.valueOf(h));
			if(kk){
				createFloatView();
				kk=false;
			}
			finish();
	}
	public int getTitleHeight(Activity activity) {
		Rect rect = new Rect();
		  activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		  return rect.top;
		}
	private void createFloatView() {  
	    btn_floatView = new ImageButton(getApplicationContext());
	    btn_floatView.setBackgroundResource(R.drawable.tou);
	    btn_floatView.setAlpha(100);
	       //btn_floatView.setText("返回");  
	         
	       wm = (WindowManager) getApplicationContext()  
	        .getSystemService(Context.WINDOW_SERVICE);  
	       params = new WindowManager.LayoutParams();  
	         
	       // 设置window type  
	       params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;  
	       /* 
	        * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 
	        * 那么优先级会降低一些, 即拉下通知栏不可见 
	        */  
	         
	       params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明  
	         params.gravity=Gravity.LEFT|Gravity.TOP;
	       // 设置Window flag  
	       params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL  
	                             | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  
	       /* 
	        * 下面的flags属性的效果形同“锁定”。 
	        * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。 
	       wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL 
	                              | LayoutParams.FLAG_NOT_FOCUSABLE 
	                              | LayoutParams.FLAG_NOT_TOUCHABLE; 
	        */  
	         
	       // 设置悬浮窗的长得宽  
	       params.width = 70;  
	       params.height = 70; 
	       params.x=0;
	       params.y=height/2-35;
	        btn_floatView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 try {
						 Log.e("click","ccccccccccc");
						 localProcess = Runtime.getRuntime().exec("su");
						DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
		                localDataOutputStream.writeBytes("input keyevent 4\n");
		               // localDataOutputStream.flush();
		                localDataOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
	       // 设置悬浮窗的Touch监听  
	       btn_floatView.setOnTouchListener(new OnTouchListener() {  
	           int lastX, lastY;  
	           int paramX, paramY;  
	             
	           public boolean onTouch(View v, MotionEvent event) {  
	               switch(event.getAction()) {  
	               case MotionEvent.ACTION_DOWN:  
	                   lastX = (int) event.getRawX();  
	                   lastY = (int) event.getRawY();  
	                   paramX = params.x;  
	                   paramY = params.y;  
	                  break;
	               case MotionEvent.ACTION_MOVE: 
	            	   btn_floatView.setBackgroundResource(R.drawable.ic_launcher);
	                   int dx = (int) event.getRawX() - lastX;  
	                   int dy = (int) event.getRawY() - lastY;  
	                   	  params.x = paramX + dx;  
		                   params.y = paramY + dy;
		                   wm.updateViewLayout(btn_floatView, params);
		                   break;
	               case MotionEvent.ACTION_UP:
	            	   btn_floatView.setBackgroundResource(R.drawable.tou);
	            	   int x=(int) event.getRawX();
	            	   int y=(int) event.getRawY();
	            	   if(x<width/2){
	            		   params.x = 0;  
		                   params.y =y-70;
	            	   }else{
	            		   params.x = width;  
		                   params.y =y-70;
	            	   }
	            	   if(Math.abs(x-lastX)<5&&Math.abs(y-lastY)<5){
	            		   wm.updateViewLayout(btn_floatView, params);
	            		   return false;
	            	   }else{
	            		   wm.updateViewLayout(btn_floatView, params);
	            		   return true;
	            	   }
	               }  
	               return false;  
	           }  
	       });  
	       btn_floatView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return true;
			}
		});;
	         
	       wm.addView(btn_floatView, params);  
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
