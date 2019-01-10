package myproject.model;

/**
 * Created by zby on 2018/11/30.
 */
public class LoginResult {

    private User mUser;
    private boolean mSuccess;
    private String mErrorMsg;


    public void setmSuccess(boolean mSuccess) {
        this.mSuccess = mSuccess;
    }

    public LoginResult(User user, boolean success) {

        mUser = user;
        mSuccess = success;
    }

    public LoginResult(boolean success, String errorMsg) {

        mSuccess = success;
        mErrorMsg = errorMsg;
    }

    public boolean isSuccess() {

        return mSuccess;
    }

    public String getErrorMsg() {

        return mErrorMsg;
    }

    public User getUser() {

        return mUser;
    }
}
