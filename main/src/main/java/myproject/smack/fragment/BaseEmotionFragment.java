package myproject.smack.fragment;


import android.support.v4.app.Fragment;
import android.widget.EditText;

import myproject.smack.enumclass.EmotionType;

/**
 *
 * Created by zby on 2018/12/3.
 */
public class BaseEmotionFragment extends Fragment {
    protected EmotionType mEmotionType;
    protected EditText mEditText;

    public BaseEmotionFragment() {

    }

    public void setEmotionType(EmotionType emotionType) {

        mEmotionType = emotionType;
    }

    public void bindToEditText(EditText editText) {

        mEditText = editText;
    }
}
