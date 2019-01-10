package myproject.smack.listener;

import android.util.Log;

import org.jivesoftware.smackx.muc.UserStatusListener;

/**
 * Created by Administrator on 2018/12/13.
 */

public class RoomUserStatusListener implements UserStatusListener {
    @Override
    public void kicked(String actor, String reason) {
        Log.e("----------","======1");
    }


    @Override
    public void voiceGranted() {
        Log.e("----------","======2");
    }

    @Override
    public void voiceRevoked() {
        Log.e("----------","======3");
    }

    @Override
    public void banned(String actor, String reason) {
        Log.e("----------","======4");
    }

    @Override
    public void membershipGranted() {
        Log.e("----------","======5");
    }

    @Override
    public void membershipRevoked() {
        Log.e("----------","======6");
    }

    @Override
    public void moderatorGranted() {
        Log.e("----------","======7");
    }

    @Override
    public void moderatorRevoked() {
        Log.e("----------","======8");
    }

    @Override
    public void ownershipGranted() {
        Log.e("----------","======9");
    }

    @Override
    public void ownershipRevoked() {
        Log.e("----------","======10");
    }

    @Override
    public void adminGranted() {
        Log.e("----------","======11");
    }

    @Override
    public void adminRevoked() {
        Log.e("----------","======12");
    }
}
