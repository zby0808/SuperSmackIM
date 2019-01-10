package myproject.smack.activity;


import android.os.Build;
import android.os.Bundle;

import myproject.R;

/**
 *
 * Created by zby on 2018/12/3.
 */
public class IMBaseActivity extends BaseIMActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initNavigationBarColor();
    }

    private void initNavigationBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.main_color));
        }
    }

    @Override
    public boolean isLceActivity() {

        return false;
    }
}
