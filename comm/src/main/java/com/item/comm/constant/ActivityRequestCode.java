package com.item.comm.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.item.comm.constant.ActivityRequestCode.ACTIVITY_REQUEST_1;
import static com.item.comm.constant.ActivityRequestCode.ACTIVITY_REQUEST_2;
import static com.item.comm.constant.ActivityRequestCode.ACTIVITY_REQUEST_3;
import static com.item.comm.constant.ActivityRequestCode.ACTIVITY_REQUEST_4;
import static com.item.comm.constant.ActivityRequestCode.ACTIVITY_REQUEST_5;
import static com.item.comm.constant.ActivityRequestCode.ACTIVITY_REQUEST_6;
import static com.item.comm.constant.ActivityRequestCode.ACTIVITY_REQUEST_7;
import static com.item.comm.constant.ActivityRequestCode.ACTIVITY_REQUEST_8;

/**
 * Created by Fracesuit on 2017/8/5.
 */

@IntDef({
        ACTIVITY_REQUEST_1,
        ACTIVITY_REQUEST_2,
        ACTIVITY_REQUEST_3,
        ACTIVITY_REQUEST_4,
        ACTIVITY_REQUEST_5,
        ACTIVITY_REQUEST_6,
        ACTIVITY_REQUEST_7,
        ACTIVITY_REQUEST_8})
@Retention(RetentionPolicy.SOURCE)
public @interface ActivityRequestCode {
    int ACTIVITY_REQUEST_1 = 1;
    int ACTIVITY_REQUEST_2 = 2;
    int ACTIVITY_REQUEST_3 = 3;
    int ACTIVITY_REQUEST_4 = 4;
    int ACTIVITY_REQUEST_5 = 5;
    int ACTIVITY_REQUEST_6 = 6;
    int ACTIVITY_REQUEST_7 = 7;
    int ACTIVITY_REQUEST_8 = 8;
}
