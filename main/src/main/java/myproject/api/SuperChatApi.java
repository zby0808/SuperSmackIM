package myproject.api;

import com.alibaba.fastjson.JSONObject;

import myproject.bean.LoginUserBean;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2018/11/30.
 */

public interface SuperChatApi {
    String rootUrl = "http://xydev.cn/sjdc/api";

    //三级督查（查询子单位及该单位下用户）
    @GET(rootUrl + "/dept/dept/findMembersAndChilds")
    Observable<String> searchDeptAndUserInfo(@Query("deptId") String deptId);

    @POST(rootUrl + "/login")
    Observable<LoginUserBean> login(@Body JSONObject jsonObject);

}
