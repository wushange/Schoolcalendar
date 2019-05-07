package com.wsg.schoolcalendar.bean;

import java.io.Serializable;

/**
 * 用戶实体类
 */
public class User implements Serializable {
    private String name;
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
