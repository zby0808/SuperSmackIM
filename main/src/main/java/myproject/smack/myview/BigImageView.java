package myproject.smack.myview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.item.comm.util.ToastyUtil;

import myproject.R;
import myproject.smack.activity.BigImageActivity;


/**
 * Created by Fracesuit on 2017/7/28.
 */

public class BigImageView extends AppCompatImageView implements View.OnClickListener {
    private int padding;//支持设置padding值
    private boolean supportFrame;//支持设置边框

    public void setSupportBig(boolean supportBig) {
        this.supportBig = supportBig;
    }

    private boolean supportBig;//支持查看大图
    private float ratio = 1.0f;//支持是否在查看大图里改变

    public BigImageView(Context context) {
        super(context);
        init(context, null);
    }

    public BigImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BigImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 控件宽度固定,已知图片的宽高比,求控件的高度
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (parentWidthMode == MeasureSpec.EXACTLY && ratio != 0) {// 控件宽度固定,已知图片的宽高比,求控件的高度
            // 得到父容器的宽度
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            // 得到孩子的宽度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            // 控件的宽度/控件的高度 = ratio;

            // 计算孩子的高度
            int childHeight = (int) (childWidth / ratio + .5f);

            // 计算父容器的高度
            int parentHeight = childHeight + getPaddingBottom() + getPaddingTop();

            // 设置自己的测试结果
            setMeasuredDimension(parentWidth, parentHeight);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }

    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BigImageView);
            padding = typedArray.getDimensionPixelSize(R.styleable.BigImageView_supportPadding, 5);
            supportFrame = typedArray.getBoolean(R.styleable.BigImageView_supportFrame, false);
            supportBig = typedArray.getBoolean(R.styleable.BigImageView_supportBig, true);
            ratio = typedArray.getFloat(R.styleable.BigImageView_supportRatio, 1);
            typedArray.recycle();
        }
        if (supportFrame) {
            setBackgroundResource(R.drawable.shape_image_frame);
        }
        setPadding(padding, padding, padding, padding);

        setOnClickListener(this);
    }

    private String imgUrl;

    public String getPath() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void show(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            this.imgUrl = imgUrl;
        }
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.loading_img)
                .centerCrop()
                .error(R.drawable.error_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .apply(options)
                .into(this);
    }

    @Override
    public void onClick(View v) {
        if (onImageViewClickListener != null) {
            onImageViewClickListener.onClick(!TextUtils.isEmpty(imgUrl));
        }
        if (supportBig && !TextUtils.isEmpty(imgUrl)) {
            //查看大图
            Intent intent = new Intent(Utils.getApp(), BigImageActivity.class);
            intent.putExtra(BigImageActivity.DATA_PIC_PATH, imgUrl);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utils.getApp().startActivity(intent);
        } else {
            ToastyUtil.warningShort("图片路径无效");
        }
    }

    public interface OnImageViewClickListener {
        void onClick(boolean hasDrawable);
    }

    private OnImageViewClickListener onImageViewClickListener;


    public void setOnImageViewClickListener(OnImageViewClickListener onImageViewClickListener) {
        this.onImageViewClickListener = onImageViewClickListener;
    }
}
