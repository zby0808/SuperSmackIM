package com.item.comm.help;


import com.item.comm.R;

import java.util.Random;


/**
 * 默认背景工厂类
 */
public final class DefIconHelp {

    private final static int[] DEF_ICON_ID = new int[] {
            R.drawable.ic_default_1,
            R.drawable.ic_default_2,
            R.drawable.ic_default_3,
            R.drawable.ic_default_4,
            R.drawable.ic_default_5
    };

    private static Random sRandom = new Random();

    private DefIconHelp() {
        throw new RuntimeException("DefIconFactory cannot be initialized!");
    }


    public static int provideIcon() {
        int index = sRandom.nextInt(DEF_ICON_ID.length);
        return DEF_ICON_ID[index];
    }
}
