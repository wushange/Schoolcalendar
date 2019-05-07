package com.wsg.schoolcalendar;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.wsg.schoolcalendar.bean.User;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //工具类初始化
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        //注冊推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //test 本地用户不存在默认创建测试用
//        try {
//            boolean userExist = x.getDb(daoConfig).getTable(User.class).tableIsExist();
//            if (!userExist) {
//                User user = new User();
//                user.setName("test");
//                user.setPassword("123456");
//                x.getDb(daoConfig).save(user);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }


    }

}
