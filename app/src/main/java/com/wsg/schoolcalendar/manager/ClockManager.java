package com.wsg.schoolcalendar.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

import java.util.Date;

/**
 * @author jsbintask@gmail.com
 * @date 2018/4/26 23:16
 */

public class ClockManager {
	private static ClockManager instance = new ClockManager();

	private ClockManager() {
	}

	public static ClockManager getInstance() {
		return instance;
	}

	/**
	 * 获取系统闹钟服务
	 *
	 * @return
	 */
	private static AlarmManager getAlarmManager() {
		return (AlarmManager) Utils.getApp().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * 取消闹钟
	 *
	 * @param pendingIntent
	 */
	public void cancelAlarm(PendingIntent pendingIntent) {
		getAlarmManager().cancel(pendingIntent);
	}

	/**
	 * 添加闹钟
	 *
	 * @param pendingIntent 执行动作
	 * @param performTime   执行时间
	 */
	public void addAlarm(PendingIntent pendingIntent, Date performTime) {
		cancelAlarm(pendingIntent);
		getAlarmManager().set(AlarmManager.RTC_WAKEUP, performTime.getTime(), pendingIntent);
	}
}