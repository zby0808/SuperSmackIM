package myproject.smack.keyboard;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import myproject.R;
import myproject.R2;
import myproject.smack.myview.RecordingVoiceView;
import myproject.smack.manager.RecordVoiceManager;
import project.utils.PDpAndPx;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * 聊天键盘
 * Created by zby on 2018/12/3.
 */
public class ChatKeyboard extends BaseKeyboardLayout implements
        KeyBoardMoreFunView.OnMoreFunItemClickListener{
    @BindView(R2.id.keyboardContentContainer)
    View mKeyboardContentContainer;
    /**
     * 输入框及其他功能布局视图
     */
    @BindView(R2.id.keyboard_toolbox_view)
    KeyBoardToolBoxView mToolBoxView;
    /**
     * 选择更多聊天功能布局
     */
    @BindView(R2.id.keyboard_more_fun_view)
    KeyBoardMoreFunView mKeyBoardMoreView;

    /**
     * 聊天操作监听，如：发送消息，发送文件，选择图片
     */
    private KeyboardOperateListener mKeyboardOperateListener;
    /**
     * 录音过程中的提示视图
     */
    private RecordingVoiceView mRecordingVoiceView;

    public ChatKeyboard(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    public ChatKeyboard(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public ChatKeyboard(Context context) {

        super(context);
    }

    @Override
    protected void inflateView(Context context) {

        inflate(context, R.layout.chat_keyboard_layout, this);
        ButterKnife.bind(this);
        initView();
    }

    void initView() {

        mToolBoxView.setRecordFinishListener(new KeyboardRecordFinishListener());
        mKeyBoardMoreView.setOnMoreFunItemClickListener(this);

        mToolBoxView.getVoiceButton().setOnClickListener(this);

        mToolBoxView.getMoreFunButton().setOnClickListener(this);
        addToShowViewList(mKeyBoardMoreView);
        addToViewMappingMap(mToolBoxView.getMoreFunButton(), SHOW_MORE_FUN, mKeyBoardMoreView);

        mToolBoxView.getEmotionButton().setOnClickListener(this);


    }

    @Override
    protected View getKeyboradContentContainer() {

        return mKeyboardContentContainer;
    }

    @Override
    public EditText getInputEditText() {

        return mToolBoxView.getInputEditText();
    }

    @Override
    protected View getEvokeKeyBoardView() {

        return null;
    }

    @Override
    public void hideKeyBoardView() {

        super.hideKeyBoardView();
        mToolBoxView.unFocusAllToolButton();
    }

    public boolean onInterceptBackPressed() {

        if(isKeyboardViewShow()) {
            hideKeyBoardView();
            return true;
        }
        return false;
    }

    @Override
    public void onKeyboardToolButtonClick(View view, int clickViewType) {

        super.onKeyboardToolButtonClick(view, clickViewType);
        if(view instanceof CheckBox) {
            boolean checked = ((CheckBox) view).isChecked();
            if(checked) {
                switch (clickViewType) {
                    case SHOW_EMOTION://表情
                        mToolBoxView.emotionButtonFocus();
                        break;
                    case SHOW_MORE_FUN://更多功能
                        mToolBoxView.moreFunButtonFocus();
                        break;
                    case SHOW_NONE://为Checkbox且当前无View展示时说明点击的是语音按钮
                        mToolBoxView.voiceButtonFocus();
                        super.hideKeyBoardView();
                        break;
                }
            } else {
                if(clickViewType == SHOW_NONE) {
                    mToolBoxView.switchToTextInput();
                    showSoftInput();
                }
            }
        }
    }


    private RecordingVoiceView getRecordingVoiceView() {

        if(mRecordingVoiceView == null) {
            int screenWidth = PDpAndPx.getScreenWidthPixels((Activity) getContext());
            mRecordingVoiceView = new RecordingVoiceView(getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth / 2, screenWidth / 2);
            params.addRule(CENTER_IN_PARENT);
            mRecordingVoiceView.setLayoutParams(params);
            ((ViewGroup)getRootView()).addView(mRecordingVoiceView);
        }
        return mRecordingVoiceView;
    }

    /**
     * 更多功能点击响应
     *
     * @param type
     */
    @Override
    public void onMoreFunItemClick(KeyBoardMoreFunType type) {
        if (mKeyboardOperateListener != null) {
            mKeyboardOperateListener.functionClick(type);
        }
    }

    /**
     * 录音过程中的监听
     */
    class KeyboardRecordFinishListener extends RecordVoiceManager.RecordFinishListener {

        @Override
        public void onFinish(File audioFile) {

            if (mKeyboardOperateListener != null && audioFile != null) {
                mKeyboardOperateListener.sendVoice(audioFile);
            }
            getRecordingVoiceView().hide();
        }

        @Override
        public void onStart() {

            getRecordingVoiceView().showStartRecordingView();
        }

        @Override
        public void onCancel(File audioFile) {

            if(audioFile != null) {
                audioFile.deleteOnExit();
            }
            getRecordingVoiceView().hide();
        }

        @Override
        public void prepareCancel() {

            getRecordingVoiceView().showCancelRecordingView();
        }
    };

    public void setKeyboardOperateListener(KeyboardOperateListener keyboardOperateListener) {

        mKeyboardOperateListener = keyboardOperateListener;
        mToolBoxView.setKeyboardOperateListener(mKeyboardOperateListener);
    }

    /**
     * 聊天操作监听接口
     *
     * Created by zby on 2018/12/3.
     */
    public interface KeyboardOperateListener {
        /**
         * 发送文本消息接口
         *
         * @param message
         */
        void send(String message);

        /**
         * 录音完成，发送语音文件，UI线程
         *
         * @param audioFile
         */
        void sendVoice(File audioFile);

        /**
         * 点击触发的功能
         *
         * @param funType 功能类型
         */
        void functionClick(KeyBoardMoreFunType funType);
    }
}
