package com.lazy.floatwindowdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

public class FloatWindowService extends Service{
	
	private Timer timer;
	
	private Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if (null == timer) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		
		if (null != timer) {
			timer.cancel();
			timer = null;
		}
		
		super.onDestroy();
	}
	
	class RefreshTask extends TimerTask{

		@Override
		public void run() {
			//是桌面但无窗口显示
			if (isHome() && !MyWindowManager.isShowingWindow()) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						MyWindowManager.createSmallWindow();
					}
				});
				
			}
			//是桌面但有窗口显示
			else if (isHome() && MyWindowManager.isShowingWindow()) {
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						MyWindowManager.updateUsedPercent();
					}
				});
			}
			//不是桌面但有窗口显示
			else if (!isHome() && MyWindowManager.isShowingWindow()) {
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						MyWindowManager.removeSmallWindow();
						MyWindowManager.removeBigWindow();
					}
				});
			}
		}
		
	}
	
	
	private boolean isHome() {
		ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos =  activityManager.getRunningTasks(1);
		return getHomes().contains(runningTaskInfos.get(0).topActivity.getPackageName());
	}
	
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager pManager = getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfos = pManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo resolveInfo : resolveInfos) {
			names.add(resolveInfo.activityInfo.packageName);
		}
		
		return names;
	}

}
