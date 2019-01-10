package myproject;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.item.comm.BaseApplication;
import com.item.comm.net.retrofit2.config.RetrofitManager;

import myproject.api.SuperChatApi;
import myproject.http.Okhttp3Help;
import myproject.smack.greendao.DaoMaster;
import myproject.smack.greendao.DaoSession;
import okhttp3.OkHttpClient;

/**
 * Created by zby on 2016/12/9.
 */

public class MyApplication {
    public static SuperChatApi superApi;
    public static DaoSession mDaoSession;
    public static Application application;


    public MyApplication(Application application) {
        this.application = application;
        //网络和缓存
        superApi = createApiService(Constants.BASE_URL,
                Okhttp3Help.getOkHttpClientBuilder(),
                SuperChatApi.class);
        setDatabase();
    }

    public void initMyApplication() {

    }


    //初始化数据库
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(application, "smack-db.db", null);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    protected <T> T createApiService(String baseUrl, OkHttpClient.Builder okBuilder, Class<T> clazz) {
        //网络
        RetrofitManager mRetrofitManager = RetrofitManager.init(application, false, baseUrl, true, okBuilder);
        return mRetrofitManager.createApiService(clazz);
    }
}
