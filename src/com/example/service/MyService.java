package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
	private boolean continueflag = false;
	private String logtext = "???";
	private MyThread thread = null;
	private int count = 1;
	
	private final IMyService.Stub IMyServiceBinder =
			new IMyService.Stub() {
				
				@Override
				public void setLogText(String s) throws RemoteException {
					logtext  = s;
				}
			};
			
	private class MyThread extends Thread{

		@Override
		public void run() {
			while(continueflag){
				Log.v("MyService:", logtext + count);
				count++;
				if(count>100){
					count = 1;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return IMyServiceBinder;
	}

	// サービス開始時に呼ばれる
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		continueflag = true;
		if(thread==null){
			thread = new MyThread();
		}
		thread.start();
		return START_STICKY;
	}

	// サービス停止時に呼ばれる
	@Override
	public void onDestroy() {
		continueflag = false;
		super.onDestroy();
	}
}
