package myproject.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.ImageUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by studyjun on 2016/5/15.
 */
public class MultipartUtil {

    public static MultipartBody createImg(String filepath) {
        if (!ImageUtils.isImage(filepath)) {
            throw new RuntimeException("不是图片格式");
        }
        File file = new File(filepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/otcet-stream"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        MultipartBody multipartBody = builder.addFormDataPart("file", file.getName(), requestFile).build();
        return multipartBody;
    }

    public static RequestBody createStringValue(String string) {
        if (TextUtils.isEmpty(string)) return null;
        return RequestBody.create(MediaType.parse("multipart/form-data"), string);

    }


    public static MultipartBody createAudio(String filepath) {
        File file = new File(filepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        MultipartBody multipartBody = builder
                .addFormDataPart("file", file.getName(), requestFile)
                .build();
        return multipartBody;
    }
}
