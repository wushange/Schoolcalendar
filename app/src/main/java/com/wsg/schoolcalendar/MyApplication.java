package com.wsg.schoolcalendar;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.wsg.schoolcalendar.bean.User;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    public static DbManager.DaoConfig daoConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        daoConfig = new DbManager.DaoConfig()
                .setDbName("test.db")
                .setDbVersion(2)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                });

        try {
            boolean userExist = x.getDb(daoConfig).getTable(User.class).tableIsExist();
            if (!userExist) {
                User user = new User();
                user.setName("test");
                user.setPassword("123456");
                x.getDb(daoConfig).save(user);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    public static DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
}
