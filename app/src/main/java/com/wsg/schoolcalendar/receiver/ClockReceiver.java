package com.wsg.schoolcalendar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wsg.schoolcalendar.ClockActivity;
import com.wsg.schoolcalendar.bean.Scheme;
import com.wsg.schoolcalendar.manager.AppManager;
import com.wsg.schoolcalendar.util.WakeLockUtil;

import org.xutils.ex.DbException;

public class ClockReceiver extends BroadcastReceiver {
	private static final String TAG = "ClockReceiver";
	public static final String EXTRA_EVENT_ID = "extra.event.id";
	public static final String EXTRA_EVENT_REMIND_TIME = "extra.event.remind.time";
	public static final String EXTRA_EVENT = "extra.event";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive: " + intent.getAction());
		WakeLockUtil.wakeUpAndUnlock();
		postToClockActivity(context, intent);
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

	public ClockReceiver() {
		super();
		Log.d(TAG, "ClockReceiver: Constructor");
	}
}
