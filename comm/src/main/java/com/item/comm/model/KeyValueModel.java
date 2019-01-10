package com.item.comm.model;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Created by Fracesuit on 2017/8/4.
 */

public class KeyValueModel implements Serializable{
    public String key;
    public String value;
    public int iconRes;

    public KeyValueModel(String key, String value) {
        this(key, value, 0);
    }

    public KeyValueModel(String key, String value, @DrawableRes int iconRes) {
        this.key = key;
        this.value = value;
        this.iconRes = iconRes;
    }
}
