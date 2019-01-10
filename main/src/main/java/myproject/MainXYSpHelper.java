package myproject;

import android.content.Context;
import android.content.SharedPreferences;

public class MainXYSpHelper {


    private static MainXYSpHelper instance;
    private String token;//请求需要的验证的token

    private Context context;
    private SharedPreferences sharedPreferences;


    public static MainXYSpHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (MainXYSpHelper.class) {
                if (instance == null)
                    instance = new MainXYSpHelper(context.getApplicationContext());
                return instance;
            }
        }
        return instance;
    }

    public MainXYSpHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.SP_FILENAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存token
     */
    public void saveToken(String token) {
        this.token = token;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("token", token);
        edit.apply();
    }


    /**
     * 保存token
     */
    public void savePassWord(String pwd) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("password", pwd);
        edit.apply();
    }

    public String getPassWord() {
        return sharedPreferences.getString("password", "");
    }


    /**
     * 清除token
     */
    public void clearToken() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("token", "");
        edit.apply();
    }

    /**
     * 初始化token
     */
    public String initToken() {
        return sharedPreferences.getString("token", "");
    }

    /**
     * 获取token
     */
    public String getToken() {
        return sharedPreferences.getString("token", "");
    }


    /**
     * 登录成功返回userId
     */
    public void saveUserId(String loginUserId) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("userId", loginUserId);
        edit.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString("userId", "");
    }


    /**
     * 保存用户姓名
     */
    public void saveUserName(String userName) {

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("userName", userName);
        edit.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", "");
    }


}
