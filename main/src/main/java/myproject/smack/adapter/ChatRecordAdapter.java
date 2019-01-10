package myproject.smack.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import myproject.R;
import myproject.R2;
import myproject.smack.bean.ChatRecordBean;
import myproject.utils.ChatTimeUtil;
import myproject.smack.recyclerview.HeaderAndFooterAdapter;
import myproject.smack.recyclerview.ViewHolder;

/**
 * 聊天记录列表适配器
 * Created by zby on 2018/12/3.
 */
public class ChatRecordAdapter extends HeaderAndFooterAdapter<ChatRecordBean> {
    private Context mContext;

    public ChatRecordAdapter(Context context, List<ChatRecordBean> list) {

        super(list);
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_record_item_layout, parent, false);
        return new ChatRecordViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position, ChatRecordBean item) {

        ChatRecordViewHolder viewHolder = (ChatRecordViewHolder) holder;

        if (!TextUtils.isEmpty(item.getFriendAvatar())) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.error_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext)
                    .load(item.getFriendAvatar())
                    .apply(options)
                    .into(viewHolder.avatar);
        }
        viewHolder.nickName.setText(item.getFriendNickname());
        if (!TextUtils.isEmpty(item.getLastMessage())) {
            if (viewHolder.message.getVisibility() == View.GONE) {
                viewHolder.message.setVisibility(View.VISIBLE);
            }

            viewHolder.message.setText(item.getLastMessage());
        }
        viewHolder.chatTime.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(item.getChatTime()));
        String messageCount = item.getUnReadMessageCount() > 0 ? String.valueOf(item.getUnReadMessageCount()) : "";
        viewHolder.messageCount.setText(messageCount);
    }

    class ChatRecordViewHolder extends ViewHolder {
        @BindView(R2.id.chat_friend_avatar)
        public ImageView avatar;
        @BindView(R2.id.chat_friend_nickname)
        public TextView nickName;
        @BindView(R2.id.chat_message)
        public TextView message;
        @BindView(R2.id.chat_time)
        public TextView chatTime;
        @BindView(R2.id.chat_message_count)
        public TextView messageCount;

        public ChatRecordViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
