package com.wsg.schoolcalendar.bean;

/**
 * 网络请求实体
 */
public class Result {

    //状态码
    private int code;
    //泛型对象
    private Object obj;
    //信息
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
