package myproject.presenter;


import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.Utils;
import com.item.comm.base.BasePresenter;
import com.item.comm.util.ToastyUtil;

import myproject.MyApplication;
import myproject.MainXYSpHelper;
import myproject.bean.LoginUserBean;
import rx.Observable;
import rx.functions.Action1;


public class LoginPresent extends BasePresenter {
    public void login(String userName, String passWord, int requestCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userName);
        jsonObject.put("password", passWord);
        ToastyUtil.infoShort(jsonObject.toJSONString());

        Observable<LoginUserBean> observable = MyApplication.superApi.login(jsonObject)
                .doOnNext(new Action1<LoginUserBean>() {
                    @Override
                    public void call(LoginUserBean bean) {
//                        JSONObject jsonObject = JSON.parseObject(bean);
//                        String token = jsonObject.getString("token");
                        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
                        helper.saveToken(bean.getToken());
                    }
                });
        execute(observable, requestCode);

    }

}
