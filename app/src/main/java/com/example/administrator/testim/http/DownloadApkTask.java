package com.example.administrator.testim.http;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 下载apk更新
 * Created by zby on 2016/12/9.
 */
public class DownloadApkTask implements Runnable {

    public Context context;
    public ProgressListener listener;
    private String downloadPath;
    private boolean isCancel = false;
    public static final int CANCELED = -1;
    public static final int ERROR = -2;

    public DownloadApkTask(Context context, String downloadPath) {
        this.context = context;
        this.downloadPath = downloadPath;
    }

    //下载apk程序代码
    protected File downLoadFile(String httpUrl) {
        Log.i("DEBUG", "downLoadFile");
        final String fileName = "updata.apk";
        File directory = new File(Environment.getExternalStorageDirectory(), "update");
        if (!directory.exists()) {
            directory.mkdir();
        }
        final File file = new File(directory, fileName);

        try {
            URL url = new URL(httpUrl);
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                InputStream is = conn.getInputStream();
                long progress = 0;
                int max = conn.getContentLength();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[256];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
                    Log.i("DEBUG", "getResponseCode = " + 400);
                    if (listener != null) {
                        listener.onFail(ERROR);
                    }
                } else {
                    while (count <= 100) {
                        if (isCancel) {
                            if (listener != null) {
                                listener.onFail(CANCELED);
                            }
                            return null;
                        }
                        if (is != null) {
                            int numRead = is.read(buf);
                            if (numRead <= 0) {
                                break;
                            } else {
                                fos.write(buf, 0, numRead);
                                progress += numRead;
                                if (listener != null) {
                                    listener.update(progress, max);
                                }
                            }

                        } else {
                            break;
                        }
                    }
                }
                conn.disconnect();
                fos.close();
                is.close();
                if (progress < max) {
                    if (listener != null) {
                        listener.onFail(ERROR);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onFail(ERROR);
                }
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFail(ERROR);
            }
            return null;
        }

        if (listener != null) {
            listener.onSuccess(file);
        }

        return file;
    }

    public void addProgressListener(ProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        if (!TextUtils.isEmpty(downloadPath)) {
            downLoadFile(downloadPath);
        }
    }

    public interface ProgressListener {
        void update(long progress, long max);

        void onSuccess(File file);

        void onFail(int code);
    }

    public void cancel() {
        isCancel = true;
    }
}
