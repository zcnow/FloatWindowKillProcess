package com.lazy.floatwindowdemo;

import java.io.BufferedReader;
import java.io.FileReader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class MyWindowManager {
	
	private static FloatWindowBig floatWindowBig;
	private static FloatWindowSmall floatWindowSmall;
	
	private static LayoutParams bigWindowParams;
	private static LayoutParams smallWindowParams;
	
	private static Context context = MyApplication.getContext();
	
	private static WindowManager windowManager 
					= (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);	

	public static void createSmallWindow() {
		Point point = new Point();
		windowManager.getDefaultDisplay().getSize(point);
		int screenWidth = point.x;
		int screenHeight = point.y;
		
		if (null == floatWindowSmall) {
			floatWindowSmall = new FloatWindowSmall(MyApplication.getContext());
			if (null == smallWindowParams) {
				smallWindowParams = new LayoutParams();
				smallWindowParams.x = screenWidth;
				smallWindowParams.y = screenHeight / 2;
				smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.format = PixelFormat.RGBA_8888;
				smallWindowParams.width = FloatWindowSmall.viewWidth;
				smallWindowParams.height = FloatWindowSmall.viewHeight;
				smallWindowParams.type = LayoutParams.TYPE_PHONE;
				smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
			}
			
			floatWindowSmall.setParams(smallWindowParams);
			windowManager.addView(floatWindowSmall, smallWindowParams);
			
		}
		
		updateUsedPercent();
	
	}
	
	public static void removeSmallWindow() {
		if (null != floatWindowSmall) {
			windowManager.removeView(floatWindowSmall);
			floatWindowSmall = null;
		}
	}
	
	public static void createBigWindow() {
		Point point = new Point();
		windowManager.getDefaultDisplay().getSize(point);
		int screenWidth = point.x;
		int screenHeight = point.y;
		
		if (null == floatWindowBig) {
			floatWindowBig = new FloatWindowBig(MyApplication.getContext());
			if (null == bigWindowParams) {
				bigWindowParams = new LayoutParams();
				bigWindowParams.type = LayoutParams.TYPE_PHONE;
				bigWindowParams.format = PixelFormat.RGBA_8888;
				bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				bigWindowParams.width = FloatWindowBig.viewWidth;
				bigWindowParams.height = FloatWindowBig.viewHeight;
				bigWindowParams.x = (screenWidth - FloatWindowBig.viewWidth) / 2;
				bigWindowParams.y = (screenHeight - FloatWindowBig.viewHeight) / 2;
			}
			windowManager.addView(floatWindowBig, bigWindowParams);
		}
	}
	
	public static void removeBigWindow() {
		if (null != floatWindowBig) {
			windowManager.removeView(floatWindowBig);
			floatWindowBig = null;
		}
	}
	
	public static void updateUsedPercent() {
		if (null != floatWindowSmall) {
			TextView tv = (TextView)floatWindowSmall.findViewById(R.id.mem_percent_tv);
			tv.setText(getUsedPercent());
		}
	}
	
	private static String getUsedPercent() {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr);
			String memLine = br.readLine();
			String SubLine = memLine.substring(memLine.indexOf("MemTotal:"));
			br.close();
			long totalMem = Integer.parseInt(SubLine.replaceAll("\\D+", ""));
			long availableMem = getAvailableMemory();
			
			int percent = (int)((totalMem - availableMem) / (float)totalMem * 100);
			
			return percent + "%";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Ðü¸¡´°";
	}

	public static long getAvailableMemory() {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager)MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		
		return mi.availMem / 1024;
	}

	public static boolean isShowingWindow() {
		return floatWindowSmall != null || floatWindowBig != null;
	}
}
