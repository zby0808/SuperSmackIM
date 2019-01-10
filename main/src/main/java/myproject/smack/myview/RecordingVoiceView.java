package myproject.smack.myview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import myproject.R;
import myproject.R2;

/**
 * Created by zby on 2018/12/3.
 */
public class RecordingVoiceView extends FrameLayout {
    @BindView(R2.id.recording_voice_container)
    View mRecordingContainer;
    @BindView(R2.id.cancel_recording_voice_container)
    View mCancelRecordingContainer;

    public RecordingVoiceView(Context context) {

        super(context);
        initView(context);
    }

    public RecordingVoiceView(Context context, AttributeSet attrs) {

        super(context, attrs);
        initView(context);
    }

    public RecordingVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecordingVoiceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {

        inflate(context, R.layout.keyboard_recording_voice_layout, this);
        ButterKnife.bind(this);
        setBackgroundResource(R.drawable.recording_vocie_view_bg);
        setAlpha(0.6f);
    }

    public void showStartRecordingView() {

        if (getVisibility() == GONE) {
            setVisibility(VISIBLE);
        }
        Log.e("-----", getVisibility() + "---" +
                mRecordingContainer.getVisibility() + "----" +
                mCancelRecordingContainer.getVisibility());
        if (mRecordingContainer.getVisibility() == GONE) {
            mRecordingContainer.setVisibility(VISIBLE);
        }
        if (mCancelRecordingContainer.getVisibility() == VISIBLE) {
            mCancelRecordingContainer.setVisibility(GONE);
        }
    }

    public void showCancelRecordingView() {

        if (getVisibility() == GONE) {
            setVisibility(VISIBLE);
        }
        if (mRecordingContainer.getVisibility() == VISIBLE) {
            mRecordingContainer.setVisibility(GONE);
        }
        if (mCancelRecordingContainer.getVisibility() == GONE) {
            mCancelRecordingContainer.setVisibility(VISIBLE);
        }
    }

    public void hide() {

        setVisibility(GONE);
    }
}
