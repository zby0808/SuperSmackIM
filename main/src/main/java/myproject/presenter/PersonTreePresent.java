package myproject.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.item.comm.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import myproject.MyApplication;
import myproject.smack.bean.PersonBean;
import myproject.smack.myview.tree.Node;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2018/11/30.
 */

public class PersonTreePresent extends BasePresenter {

    public void searchDeptAndUserInfo(String deptId, int requestCode) {
        Observable<List<Node>> observable = MyApplication.superApi.searchDeptAndUserInfo(deptId)
                .map(new Func1<String, List<Node>>() {
                    @Override
                    public List<Node> call(String s) {
                        List<Node> mData = new ArrayList<>();
                        boolean isShowPerson = false;
                        boolean isShowUnit = false;
                        JSONObject jsonObject = JSON.parseObject(s);
                        String parentId = jsonObject.getString("id");
                        List<JSONObject> userJson = JSON.parseArray(jsonObject.getString("userList"), JSONObject.class);
                        List<JSONObject> deptJson = JSON.parseArray(jsonObject.getString("deptList"), JSONObject.class);
                        if (null != userJson && userJson.size() != 0) {
                            for (JSONObject obj : userJson) {
                                String userId = obj.getString("id");
                                String userName = obj.getString("userName");
                                PersonBean bean = new PersonBean();
                                String sex = obj.getString("gender");
                                String sexValue = "";
                                if (TextUtils.isEmpty(sex)) {
                                    sexValue = "未知";
                                } else if (sex.equals("1")) {
                                    sexValue = "男";
                                } else if (sex.equals("2")) {
                                    sexValue = "女";
                                } else {
                                    sexValue = "未知";
                                }
                                bean.setUserSex(sexValue);
                                bean.setUserName(obj.getString("userCode"));
                                bean.setPhone(obj.getString("mobilePhone"));
                                Node userNode = new Node(userId, parentId, userName, bean);
                                userNode.setHaveChild(false);
                                userNode.setAdd(false);
                                userNode.setTreeType(0);
                                if (isShowPerson == false) {
                                    isShowPerson = true;
                                    userNode.setShowHead(true);
                                    userNode.setHeadDesc("单位人员：");
                                }
                                mData.add(userNode);
                            }
                        }

                        if (null != deptJson && deptJson.size() != 0) {
                            for (JSONObject obj : deptJson) {
                                Node deptNode = new Node();
                                deptNode.setId(obj.getString("id"));
                                deptNode.setpId(parentId);
                                deptNode.setName(obj.getString("name"));
                                deptNode.setHaveChild(true);
                                deptNode.setAdd(true);
                                deptNode.setTreeType(0);
                                if (isShowUnit == false) {
                                    isShowUnit = true;
                                    deptNode.setShowHead(true);
                                    deptNode.setHeadDesc("下属单位：");
                                }
                                mData.add(deptNode);
                            }
                        }
                        return mData;
                    }
                });
        execute(observable, requestCode);
    }
}
