package myproject.smack.enumclass;

import myproject.R;
import myproject.smack.fragment.BaseEmotionFragment;

/**
 * 表情类型
 *
 * Created by zby on 2018/12/3.
 */
public enum EmotionType {
//    EMOTION_TYPE_CLASSIC(R.drawable.vector_keyboard_emotion, EmotionFragment.class),   //经典表情
    EMOTION_TYPE_MORE(R.drawable.more_button, BaseEmotionFragment.class);       //+点击更多

    private int mEmotionTypeIcon;
    private Class mFragmentClass;

    EmotionType(int emotionTypeIcon, Class fragmentClass) {

        mEmotionTypeIcon = emotionTypeIcon;
        mFragmentClass = fragmentClass;
    }

    public int getEmotionTypeIcon() {

        return mEmotionTypeIcon;
    }

    public Class getFragmentClass() {

        return mFragmentClass;
    }
}
