package com.wsg.schoolcalendar.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wsg.schoolcalendar.ClockActivity;
import com.wsg.schoolcalendar.bean.Scheme;
import com.wsg.schoolcalendar.manager.AppManager;
import com.wsg.schoolcalendar.util.WakeLockUtil;

import org.xutils.ex.DbException;

/**
 * Service和Broadcast都行，此处选一个，service存活率更高
 */
public class ClockService extends Service {
	private static final String TAG = "ClockService";
	public static final String EXTRA_EVENT_ID = "extra.event.id";
	public static final String EXTRA_EVENT_REMIND_TIME = "extra.event.remind.time";
	public static final String EXTRA_EVENT = "extra.event";

	public ClockService() {
		Log.d(TAG, "ClockService: Constructor");
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand: onStartCommand");
		WakeLockUtil.wakeUpAndUnlock();
		postToClockActivity(getApplicationContext(), intent);
		return super.onStartCommand(intent, flags, startId);
	}

	private void postToClockActivity(Context context, Intent intent) {
		Intent i = new Intent();
		i.setClass(context, ClockActivity.class);
		i.putExtra(EXTRA_EVENT_ID, intent.getIntExtra(EXTRA_EVENT_ID, -1));
		Scheme event = null;
		try {
			event = AppManager.getInstance().getDBManager().findById(Scheme.class, intent.getIntExtra(EXTRA_EVENT_ID, -1));
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (event == null) {
			return;
		}
		i.putExtra(EXTRA_EVENT_REMIND_TIME, intent.getStringExtra(EXTRA_EVENT_REMIND_TIME));
		i.putExtra(EXTRA_EVENT, event);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy: ");
		super.onDestroy();
	}
}
