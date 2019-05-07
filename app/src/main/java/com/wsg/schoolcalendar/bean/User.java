package com.wsg.schoolcalendar.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 用戶实体类
 */
@Table(name = "user")
public class User implements Serializable {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
