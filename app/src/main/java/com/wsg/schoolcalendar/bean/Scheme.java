package com.wsg.schoolcalendar.bean;

import com.alibaba.fastjson.annotation.JSONField;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 日程实体类 。数据库存储
 */
@Table(name = "t_scheme")
public class Scheme implements Serializable {
    @JSONField(serialize = false)
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
    private int schemetype;

    @Column(name = "schemetime")
    private String schemetime;

    /**
     * 各种自定义标记颜色、没有则选择默认颜色，如果使用多标记，请使用下面API
     * using addScheme(int schemeColor,String scheme); multi scheme
     */
    private int schemeColor;


    public int getId() {
        return id;
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

    public int getSchemetype() {
        return schemetype;
    }

    public void setSchemetype(int schemetype) {
        this.schemetype = schemetype;
    }

    public String getSchemetime() {
        return schemetime;
    }

    public void setSchemetime(String schemetime) {
        this.schemetime = schemetime;
    }

    public int getSchemeColor() {
        return schemeColor;
    }

    public void setSchemeColor(int schemeColor) {
        this.schemeColor = schemeColor;
    }

    @Override
    public String toString() {
        return "Scheme{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", leapMonth=" + leapMonth +
                ", day=" + day +
                ", scheme='" + scheme + '\'' +
                ", schemetype=" + schemetype +
                ", schemetime='" + schemetime + '\'' +
                ", schemeColor=" + schemeColor +
                '}';
    }
}

