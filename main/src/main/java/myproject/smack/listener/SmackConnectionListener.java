package myproject.smack.listener;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.Logger;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.util.Timer;
import java.util.TimerTask;

import myproject.MainXYSpHelper;
import myproject.model.LoginResult;
import myproject.model.User;
import myproject.smack.manager.SmackManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zby on 2018/11/30.
 * smack连接监听，掉线重连
 */
public class SmackConnectionListener implements ConnectionListener {
    private Timer tExit;
    private int loginTime = 2000;

    @Override
    public void connected(XMPPConnection connection) {
        Logger.d("已连接");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

        Logger.e("连接认证");
    }

    @Override
    public void connectionClosed() {
        //先关闭连接
//        SmackManager.getInstance().getConnection().disconnect();
//        // 重连服务器
//        tExit = new Timer();
//        tExit.schedule(new MyTimerTask(), loginTime);
        Logger.e("连接失败");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Logger.e("关闭连接错误");
    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
            final String account = helper.getUserName();
            final String passWord = helper.getPassWord();
            Logger.e("尝试重新登录----");
            if (account == null || passWord == null) {
                Logger.e("账号或密码为空----");
            }
            Observable.just(new User(account, passWord))
                    .subscribeOn(Schedulers.io())//指定下面的flatMap线程
                    .flatMap(new Func1<User, Observable<LoginResult>>() {
                        @Override
                        public Observable<LoginResult> call(User user) {
                            LoginResult loginResult = SmackManager.getInstance().login(account, passWord);
                            return Observable.just(loginResult);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())//给下面的subscribe设定线程
                    .subscribe(new Action1<LoginResult>() {
                        @Override
                        public void call(LoginResult loginResult) {
                            Logger.e("重新登录成功----");
                        }
                    });
        }
    }

}
