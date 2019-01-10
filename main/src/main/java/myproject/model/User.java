package myproject.model;

import java.io.Serializable;

/**
 * Created by zby on 2018/11/30.
 */
public class User implements Serializable {

    private String mUsername;
    private String mNickname;
    private String mPassword;

    public User() {

    }

    public User(String username, String password) {

        mUsername = username;
        mPassword = password;
    }

    public String getUsername() {

        return mUsername;
    }

    public void setUsername(String username) {

        mUsername = username;
    }

    public String getPassword() {

        return mPassword;
    }

    public void setPassword(String password) {

        mPassword = password;
    }

    public void setNickname(String nickname) {

        mNickname = nickname;
    }

    public String getNickname() {

        return mNickname;
    }
}
