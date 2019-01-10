package myproject.smack.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import myproject.R;
import myproject.smack.enumclass.MessageType;
import myproject.smack.greendao.ChatMessageDaoBean;

/**
 * Created by Administrator on 2018/12/4.
 */

public class MessageFragmentAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public MessageFragmentAdapter() {
        super(R.layout.fragment_chat_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        List<ChatMessageDaoBean> mList = (List<ChatMessageDaoBean>) item;
        ChatMessageDaoBean lastObj = mList.get(mList.size() - 1);
        String content = null;
        if (lastObj.getMMessageType() == MessageType.MESSAGE_TYPE_TEXT.value())
            content = lastObj.getMContent();
        else if (lastObj.getMMessageType() == MessageType.MESSAGE_TYPE_IMAGE.value())
            content = "[ 图片 ]";
        else if (lastObj.getMMessageType() == MessageType.MESSAGE_TYPE_VOICE.value())
            content = "[ 语音 ] ";

        helper.setText(R.id.tv_name, lastObj.getMFriendNickname())
                .setText(R.id.tv_content, content);

    }
}
