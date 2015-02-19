package com.example.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity 
	implements OnClickListener
{
	
	private IMyService binder;
	private MyServiceConnection conn = null;
	private Intent svc = null;
	
	class MyServiceConnection
		implements ServiceConnection
	{

		// サービス接続時に呼ばれる
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = IMyService.Stub.asInterface(service);
			try {
				binder.setLogText("ABC");
			} catch (Exception e) {
			}
			
		}

		// サービス切断時に呼ばれる
		@Override
		public void onServiceDisconnected(ComponentName name) {
			binder = null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundColor(Color.WHITE);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);
		
		Button btn1 = new Button(this);
		btn1.setText("Start Service");
		btn1.setTag("btn1");
		btn1.setOnClickListener(this);
		layout.addView(btn1);
		
		Button btn2 = new Button(this);
		btn2.setText("Stop Service");
		btn2.setTag("btn2");
		btn2.setOnClickListener(this);
		layout.addView(btn2);
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

	@Override
	public void onClick(View v) {
//		Log.v("onClick", (String)v.getTag());
		String tag = (String)v.getTag();
		if(tag == "btn1"){
			if(conn != null && svc != null){
				return;
			}
			conn = new MyServiceConnection();
			svc = new Intent(this, MyService.class);
			startService(svc);
			bindService(svc, conn, BIND_AUTO_CREATE);
		}else if(tag == "btn2"){
			if( conn == null && svc == null){
				return;
			}
			unbindService(conn);
			stopService(svc);
			conn = null;
			svc = null;
		}
	}
}
