package com.lazy.floatwindowdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button startFloatWindowbtn = (Button)findViewById(R.id.start_float_window);
		startFloatWindowbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
				startService(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		Log.e("MainActivity", "onStart");
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		Log.e("MainActivity", "onStop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Log.e("MainActivity", "onDestroy");
		super.onDestroy();
	}

}
