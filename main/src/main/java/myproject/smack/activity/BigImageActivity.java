package myproject.smack.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.item.comm.util.OverridePendingTransitionUtls;
import com.item.comm.util.ToolbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import myproject.R;
import myproject.R2;


public class BigImageActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DATA_PIC_PATH = "data_pic_path";
    @BindView(R2.id.tool_bar)
    Toolbar toolBar;
    @BindView(R2.id.photo_view)
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        ButterKnife.bind(this);
        initToolbar();
        String path = getIntent().getStringExtra(DATA_PIC_PATH);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.error_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .load(path)
                .apply(options)
                .into(photoView);
        photoView.setOnClickListener(this);
    }

    private void initToolbar() {
        ToolbarUtils.with(this, toolBar)
                .setSupportBack(true)
                .setTitle("图片详情", true)
                .build();
    }


    @Override
    public void onClick(View v) {
        exit();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        finish();
        OverridePendingTransitionUtls.zoomInExit(this);
    }
}
