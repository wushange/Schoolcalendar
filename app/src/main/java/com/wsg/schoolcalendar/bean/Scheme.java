package com.wsg.schoolcalendar.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 事件标记服务，现在多类型的事务标记建议使用这个
 */
@Table(name = "t_scheme")
public class Scheme implements Serializable {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "type")
    private int type;
    @Column(name = "shcemeColor")
    private int shcemeColor;
    @Column(name = "scheme")
    private String scheme;
    @Column(name = "other")
    private String other;

    @Column(name = "parentId" /*, property = "UNIQUE"//如果是一对一加上唯一约束*/)
    private long parentId; // 外键表id


    public Scheme() {
    }

    public Scheme(int type, int shcemeColor, String scheme, String other) {
        this.type = type;
        this.shcemeColor = shcemeColor;
        this.scheme = scheme;
        this.other = other;
    }

    public Scheme(int type, int shcemeColor, String scheme) {
        this.type = type;
        this.shcemeColor = shcemeColor;
        this.scheme = scheme;
    }

    public Scheme(int shcemeColor, String scheme) {
        this.shcemeColor = shcemeColor;
        this.scheme = scheme;
    }

    public Scheme(int shcemeColor, String scheme, String other) {
        this.shcemeColor = shcemeColor;
        this.scheme = scheme;
        this.other = other;
    }

    public int getShcemeColor() {
        return shcemeColor;
    }

    public void setShcemeColor(int shcemeColor) {
        this.shcemeColor = shcemeColor;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}