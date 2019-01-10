package com.example.administrator.testim.presenter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.Utils;
import com.example.administrator.testim.SmackApplication;
import com.example.administrator.testim.XYSpHelper;
import com.example.administrator.testim.bean.LoginUserBean;
import com.item.comm.base.BasePresenter;
import com.item.comm.util.ToastyUtil;

import rx.Observable;
import rx.functions.Action1;


public class LoginPresent extends BasePresenter {
    public void login(String userName, String passWord, int requestCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userName);
        jsonObject.put("password", passWord);
        ToastyUtil.infoShort(jsonObject.toJSONString());

        Observable<String> observable = SmackApplication.superApi.login(userName, "APP", jsonObject)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String bean) {
                        JSONObject jsonObject = JSON.parseObject(bean);
                        String token = jsonObject.getString("token");
                        XYSpHelper helper = XYSpHelper.getInstance(Utils.getApp());
                        helper.saveToken(token);
                    }
                });
        execute(observable, requestCode);

    }


}
