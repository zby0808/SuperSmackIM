package com.example.administrator.testim.bean;

/**
 * zby
 * 改类根据接口文档来定义，字段名称对应接口文档相应的返回值字段名称
 */
public class RootBean {

    /**
     * message : 登陆成功
     * status : 200
     */

    private String message;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOK() {
        //0代表请求成功码，改请求码根据不同公司而设定
        return code == 200;
    }

}
