package com.wsg.schoolcalendar.manager;

import com.blankj.utilcode.util.LogUtils;
import com.haibin.calendarview.Calendar;
import com.wsg.schoolcalendar.bean.MyCalendar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AppManager {
    private static final AppManager ourInstance = new AppManager();
    private DbManager.DaoConfig daoConfig;

    public static AppManager getInstance() {
        return ourInstance;
    }

    private DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    public DbManager getDBManager() {
        return x.getDb(getDaoConfig());
    }

    private AppManager() {
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


    public boolean deleteScheme(Calendar calendar) {
        MyCalendar myCalendar = null;
        try {
            myCalendar = getDBManager()
                    .selector(MyCalendar.class)
                    .where("year", "=", calendar.getYear())
                    .and("month", "=", calendar.getMonth())
                    .and("day", "=", calendar.getDay())
                    .findFirst();
            if (myCalendar != null) {
                getDBManager().delete(myCalendar);
                return true;
            } else {
                return false;
            }
        } catch (DbException e) {
            return false;
        }

    }

    public Map<String, Calendar> getCalendarList() {
        Map<String, Calendar> map = new HashMap<>();
        try {
            //从数据库读取日历日程列表
            List<MyCalendar> myCalendars = x.getDb(getDaoConfig()).findAll(MyCalendar.class);
            if (myCalendars != null) {
                LogUtils.e("---" + myCalendars.size());
                if (myCalendars.size() > 0) {
                    //遍历列表设置日历日程数据源
                    for (int i = 0; i < myCalendars.size(); i++) {
                        Random random = new Random();
                        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
                        MyCalendar myCalendar = myCalendars.get(i);
                        Calendar calendar = getSchemeCalendar(myCalendar.getYear(),
                                myCalendar.getMonth(),
                                myCalendar.getDay(),
                                ranColor,
                                myCalendar.getSchemeType(),
                                myCalendar.getScheme());
                        map.put(calendar.toString(),
                                calendar);
                        LogUtils.e("--" + myCalendars.get(i).toString());
                    }
                }

            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return map;
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, int type, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        String s = "课";
        switch (type) {
            case 0:
                s = "课";
                break;
            case 1:
                s = "事";
                break;
            case 2:
                s = "议";
                break;
        }
        calendar.setScheme(s);
        return calendar;
    }
}