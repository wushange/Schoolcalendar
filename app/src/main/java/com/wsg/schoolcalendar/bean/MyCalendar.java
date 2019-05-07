package com.wsg.schoolcalendar.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

@Table(name = "t_calendar")
public class MyCalendar {
    @Column(name = "id", isId = true)
    private int id;


    /**
     * 年
     */
    @Column(name = "year")
    private int year;

    /**
     * 月1-12
     */
    @Column(name = "month")
    private int month;

    /**
     * 如果是闰月，则返回闰月
     */
    private int leapMonth;

    /**
     * 日1-31
     */
    @Column(name = "day")
    private int day;


    /**
     * 计划，可以用来标记当天是否有任务,这里是默认的，如果使用多标记，请使用下面API
     * using addScheme(int schemeColor,String scheme); multi scheme
     */
    @Column(name = "scheme")
    private String scheme;

    @Column(name = "schemetype")
    private int schemeType;


    /**
     * 各种自定义标记颜色、没有则选择默认颜色，如果使用多标记，请使用下面API
     * using addScheme(int schemeColor,String scheme); multi scheme
     */
    private int schemeColor;


    /**
     * 多标记
     * multi scheme,using addScheme();
     */
    private List<Scheme> schemes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(int leapMonth) {
        this.leapMonth = leapMonth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public int getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(int schemeType) {
        this.schemeType = schemeType;
    }

    public int getSchemeColor() {
        return schemeColor;
    }

    public void setSchemeColor(int schemeColor) {
        this.schemeColor = schemeColor;
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }

    @Override
    public String toString() {
        return "MyCalendar{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", leapMonth=" + leapMonth +
                ", day=" + day +
                ", scheme='" + scheme + '\'' +
                ", schemeType=" + schemeType +
                ", schemeColor=" + schemeColor +
                ", schemes=" + schemes +
                '}';
    }
}
