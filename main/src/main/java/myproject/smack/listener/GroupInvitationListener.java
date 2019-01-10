package myproject.smack.listener;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import myproject.smack.activity.GroupChatActivity;

/**
 * Created by Administrator on 2018/12/14.
 * 群邀请监听
 */

public class GroupInvitationListener implements InvitationListener {
    FragmentActivity mContext;

    public GroupInvitationListener(FragmentActivity context) {
        this.mContext = context;
    }

    /**
     * @param conn     收到邀请的XMPPConnection。
     * @param room     邀请所指的房间。
     * @param invite   邀请者
     * @param reason   邀请原因
     * @param password 群密码
     * @param message  邀请者用来发送邀请的消息。
     */
    @Override
    public void invitationReceived(XMPPConnection conn, final MultiUserChat room,
                                   final String invite, String reason,
                                   String password, Message message) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(mContext)
                        .title("群邀请")
                        .content(invite + "邀请你加入" + room.getNickname() + "的要请,是否加入该群？")
                        .positiveText("加入")
                        .autoDismiss(true)
                        .negativeText("拒绝")
                        .canceledOnTouchOutside(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //跳转群聊界面
                                intentGroupView(room);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        })
                        .show();
            }
        });


    }

    private void intentGroupView(MultiUserChat room) {
        Intent intent = new Intent();
        intent.setClass(mContext, GroupChatActivity.class);
        intent.putExtra("roomId", room.getRoom());
        intent.putExtra("roomName", room.getNickname());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

}
