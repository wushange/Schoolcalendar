package com.wsg.schoolcalendar.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.wsg.schoolcalendar.bean.Scheme;
import com.wsg.schoolcalendar.manager.AppManager;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtils.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + msg);
                try {
                    JSONObject userJson = JSONObject.parseObject(msg);
                    Scheme scheme = JSON.toJavaObject(userJson, Scheme.class);
                    if (scheme != null) {
                        // TODO: 2019/5/7 接受开始查询最新日程
                        String[] dates = scheme.getSchemetime().split("-");
                        scheme.setYear(Integer.parseInt(dates[0]));
                        scheme.setMonth(Integer.parseInt(dates[1]));
                        scheme.setDay(Integer.parseInt(dates[2]));
                        LogUtils.e("--接受开始查询最新日程--" + scheme.toString());
                        AppManager.getInstance().addScheme(scheme);
                        EventBus.getDefault().postSticky(new EventBusCommon());
                    }
                } catch (Exception e) {
                    LogUtils.e("----" + e.getLocalizedMessage());
                    e.printStackTrace();
                }

//				LogUtils(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 用户点击打开了通知");
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtils.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                LogUtils.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


}
