package com.item.comm.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.item.comm.constant.NetRequestCode.NET_REQUEST_1;
import static com.item.comm.constant.NetRequestCode.NET_REQUEST_2;
import static com.item.comm.constant.NetRequestCode.NET_REQUEST_3;
import static com.item.comm.constant.NetRequestCode.NET_REQUEST_4;
import static com.item.comm.constant.NetRequestCode.NET_REQUEST_5;
import static com.item.comm.constant.NetRequestCode.NET_REQUEST_6;
import static com.item.comm.constant.NetRequestCode.NET_REQUEST_7;
import static com.item.comm.constant.NetRequestCode.NET_REQUEST_8;

/**
 * Created by Fracesuit on 2017/8/5.
 */

@IntDef({NET_REQUEST_1,
        NET_REQUEST_2,
        NET_REQUEST_3,
        NET_REQUEST_4,
        NET_REQUEST_5,
        NET_REQUEST_6,
        NET_REQUEST_7,
        NET_REQUEST_8})
@Retention(RetentionPolicy.SOURCE)
public @interface NetRequestCode {
    int NET_REQUEST_1 = 1;
    int NET_REQUEST_2 = 2;
    int NET_REQUEST_3 = 3;
    int NET_REQUEST_4 = 4;
    int NET_REQUEST_5 = 5;
    int NET_REQUEST_6 = 6;
    int NET_REQUEST_7 = 7;
    int NET_REQUEST_8 = 8;
}
