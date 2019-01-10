package com.item.comm.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Fracesuit on 2017/8/29.
 */

public class InputFilterHelp {

    //字母或者数字
    public static InputFilter getLetterOrDigitInputFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                //过滤中文
                if (CharacterUtils.isChinese(source.toString())) {
                    return "";
                }

                //过滤非数字和字母
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

    }
}
