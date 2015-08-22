package com.lazy.floatwindowdemo;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class FloatWindowSmall extends LinearLayout{

	public static int viewWidth;
	public static int viewHeight;
	
	private static int statusBarHeight;
	private static float xInView;
	private static float yInView;
	private static float xDownInScreen;
	private static float yDownInScreen;
	private static float xInScreen;
	private static float yInScreen;
	
	private WindowManager windowManager;
	private WindowManager.LayoutParams parms;

	public FloatWindowSmall(Context context) {
		super(context);
		windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		View view = (View)findViewById(R.id.small_window);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}

	public void setParams(WindowManager.LayoutParams smallWindowParams) {
		parms = smallWindowParams;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xInView = event.getX();
			yInView = event.getY();
			
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			
			xInScreen = xDownInScreen;
			yInScreen = yDownInScreen;
			
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			updateViewPosition();
			
			break;
		case MotionEvent.ACTION_UP:
			if (xInScreen == xDownInScreen && yInScreen == yDownInScreen) {
				openBigWindow();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	private float getStatusBarHeight() {
		if (0 == statusBarHeight) {
			
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");  
				int x = (Integer) field.get(o); 
				statusBarHeight = getResources().getDimensionPixelSize(x);  
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return statusBarHeight;
	}

	private void openBigWindow() {
		MyWindowManager.removeSmallWindow();
		MyWindowManager.createBigWindow();
		
	}

	private void updateViewPosition() {
		parms.x = (int)(xInScreen - xInView);
		parms.y = (int)(yInScreen - yInView);
		windowManager.updateViewLayout(this, parms);
		
	}

}
