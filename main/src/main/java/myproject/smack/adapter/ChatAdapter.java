package myproject.smack.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import myproject.R;
import myproject.R2;
import myproject.smack.enumclass.FileLoadState;
import myproject.smack.enumclass.MessageType;
import myproject.smack.greendao.ChatMessageDaoBean;
import myproject.smack.myview.BigImageView;
import myproject.smack.recyclerview.HeaderAndFooterAdapter;
import myproject.smack.recyclerview.ViewHolder;
import myproject.utils.ChatTimeUtil;

/**
 * 单聊适配器
 * Created by zby on 2018/12/3.
 */
public class ChatAdapter extends HeaderAndFooterAdapter<ChatMessageDaoBean> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_RECEIVER = 0;
    private Context mContext;
    /**
     * 音频播放器
     */
    private MediaPlayer mediaPlayer;

    public ChatAdapter(Context context, List<ChatMessageDaoBean> list) {

        super(list);
        mContext = context;
    }

    @Override
    public int getItemViewTypeForData(int position) {
        ChatMessageDaoBean item = getItem(position);

        return getItem(position).getMIsMeSend() ? VIEW_TYPE_ME : VIEW_TYPE_RECEIVER;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_ME) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_messgae_item_right_layout, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_messgae_item_left_layout, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position, final ChatMessageDaoBean message) {

        final ChatViewHolder viewHolder = (ChatViewHolder) holder;
        if (message.getMIsMeSend()) {
            viewHolder.chatNickname.setText(message.getMMeNickname());
        } else {
            viewHolder.chatNickname.setText(message.getMFriendNickname());
        }
        viewHolder.chatContentTime.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(message.getMDatetime()));
        setMessageViewVisible(message.getMMessageType(), viewHolder);

        if (message.getMMessageType() == MessageType.MESSAGE_TYPE_TEXT.value()) {//文本消息
            //处理表情
            viewHolder.chatContentText.setText(message.getMContent());
        } else if (message.getMMessageType() == MessageType.MESSAGE_TYPE_IMAGE.value()) {//图片消息
            String url = "file://" + message.getMFilePath();
            viewHolder.chatContentImage.show(url);
            showLoading(viewHolder, message);
        } else if (message.getMMessageType() == MessageType.MESSAGE_TYPE_VOICE.value()) {//语音消息
            viewHolder.chatContentVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    playVoice(viewHolder.chatContentVoice, message);
                }
            });
            showLoading(viewHolder, message);
        }
    }

    private void showLoading(ChatViewHolder viewHolder, ChatMessageDaoBean message) {

        if (message.getMFileLoadState() == FileLoadState.STATE_LOAD_START.value()) {//加载开始
            viewHolder.chatContentLoading.setBackgroundResource(R.drawable.chat_file_content_loading_anim);
            final AnimationDrawable animationDrawable = (AnimationDrawable) viewHolder.chatContentLoading.getBackground();
            viewHolder.chatContentLoading.post(new Runnable() {
                @Override
                public void run() {

                    animationDrawable.start();
                }
            });
//            viewHolder.chatContentLoading.setVisibility(View.VISIBLE);
        } else if (message.getMFileLoadState() == FileLoadState.STATE_LOAD_SUCCESS.value()) {//加载完成
//            viewHolder.chatContentLoading.setVisibility(View.GONE);
        } else if (message.getMFileLoadState() == FileLoadState.STATE_LOAD_ERROR.value()) {
//            viewHolder.chatContentLoading.setBackgroundResource(R.drawable.file_load_fail);
        }
    }

    /**
     * 根据消息类型显示对应的消息展示控件
     *
     * @param messageType
     * @param viewHolder
     */
    private void setMessageViewVisible(int messageType, ChatViewHolder viewHolder) {

        if (messageType == MessageType.MESSAGE_TYPE_TEXT.value()) {//文本消息
            viewHolder.chatContentText.setVisibility(View.VISIBLE);
            viewHolder.chatContentImage.setVisibility(View.GONE);
            viewHolder.chatContentVoice.setVisibility(View.GONE);
        } else if (messageType == MessageType.MESSAGE_TYPE_IMAGE.value()) {//图片消息
            viewHolder.chatContentText.setVisibility(View.GONE);
            viewHolder.chatContentImage.setVisibility(View.VISIBLE);
            viewHolder.chatContentVoice.setVisibility(View.GONE);
        } else if (messageType == MessageType.MESSAGE_TYPE_VOICE.value()) {//语音消息
            viewHolder.chatContentText.setVisibility(View.GONE);
            viewHolder.chatContentImage.setVisibility(View.GONE);
            viewHolder.chatContentVoice.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 播放语音信息
     *
     * @param iv
     * @param message
     */
    private void playVoice(final ImageView iv, final ChatMessageDaoBean message) {

        if (message.getMIsMeSend()) {
            iv.setBackgroundResource(R.drawable.anim_chat_voice_right);
        } else {
            iv.setBackgroundResource(R.drawable.anim_chat_voice_left);
        }
        final AnimationDrawable animationDrawable = (AnimationDrawable) iv.getBackground();
        iv.post(new Runnable() {
            @Override
            public void run() {

                animationDrawable.start();
            }
        });
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {//点击播放，再次点击停止播放
            // 开始播放录音
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    animationDrawable.stop();
                    // 恢复语音消息图标背景
                    if (message.getMIsMeSend()) {
                        iv.setBackgroundResource(R.drawable.gxu);
                    } else {
                        iv.setBackgroundResource(R.drawable.gxx);
                    }
                }
            });
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(message.getMFilePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            animationDrawable.stop();
            // 恢复语音消息图标背景
            if (message.getMIsMeSend()) {
                iv.setBackgroundResource(R.drawable.gxu);
            } else {
                iv.setBackgroundResource(R.drawable.gxx);
            }
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    public class ChatViewHolder extends ViewHolder {
        @BindView(R2.id.tv_chat_msg_username)
        public TextView chatNickname;//消息来源人昵称
        @BindView(R2.id.tv_chat_msg_time)
        public TextView chatContentTime;//消息时间
        @BindView(R2.id.iv_chat_avatar)
        public ImageView chatUserAvatar;//用户头像
        @BindView(R2.id.tv_chat_msg_content_text)
        public TextView chatContentText;//文本消息
        @BindView(R2.id.iv_chat_msg_content_image)
        public BigImageView chatContentImage;//图片消息
        @BindView(R2.id.iv_chat_msg_content_voice)
        public ImageView chatContentVoice;//语音消息
        @BindView(R2.id.iv_chat_msg_content_loading)
        public ImageView chatContentLoading;//发送接收文件时的进度条

        public ChatViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
