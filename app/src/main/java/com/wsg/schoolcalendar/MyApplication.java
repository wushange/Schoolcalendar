package com.wsg.schoolcalendar;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import org.xutils.DbManager;
import org.xutils.x;

public class MyApplication extends Application {
    public static DbManager.DaoConfig daoConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
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


    }

    public static DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
}
