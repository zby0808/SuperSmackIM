package project.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 图片处理
 * Created by zby on 2016/12/9.
 */
public class PImageUtil {
    public PImageUtil() {
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {


        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 获取远程图片数据
     *
     * @param path url图片路径
     * @return
     */
    public static Bitmap getUrlPicture(String path) {
        Bitmap bm = null;
        try {
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bm = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * bitmap转String
     *
     * @param bitmap
     * @return
     */
    public static String compressToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }


    /**
     * 路径转Bitmap
     *
     * @param srcPath
     * @return
     */
    public static Bitmap compressImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int hh = 800;
        int ww = 480;
        int be = 1;
        if (w > h && w > ww) {
            be = (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage2Bitmap(bitmap);
    }

    /**
     * Bitmap自压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage2Bitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        int options = 60;
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * Bitmap转byte[]
     *
     * @param image
     * @return
     */
    public static byte[] compressImage2Byte(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        image.recycle();
        byte[] result = baos.toByteArray();
        return result;
    }

    /**
     * Bitmap转byte[](不回收Bitmap)
     *
     * @param image
     * @return
     */
    public static byte[] compressImage2ByteNoRecycle(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] result = baos.toByteArray();
        PLog.e("Result Length:" + result.length);
        return result;
    }


    /**
     * 图片压缩方法实现
     *
     * @param srcPath    原图地址
     * @param finishPath 压缩后保存图片地址
     * @param avatorPath 保存的文件夹路径
     * @return
     */
    public static Bitmap compressImage(String srcPath, String finishPath, String avatorPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int hh = 800;// 这里设置高度为800f
        int ww = 480;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据高度固定大小缩放
            be = (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap, finishPath, avatorPath);// 压缩好比例大小后再进行质量压缩
    }

    public static byte[] compressImageToByteData(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        int options = 60;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        return baos.toByteArray();
    }

    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image, String finishPath, String avatorpath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 60, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 60;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        try {
            File fileDir = new File(avatorpath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();// 创建文件夹
            }
            FileOutputStream out = new FileOutputStream(finishPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left;
            float top;
            float right;
            float bottom;
            float dst_left;
            float dst_top;
            float dst_right;
            float dst_bottom;
            if (width <= height) {
                roundPx = (float) (width / 2);
                top = 0.0F;
                bottom = (float) width;
                left = 0.0F;
                right = (float) width;
                height = width;
                dst_left = 0.0F;
                dst_top = 0.0F;
                dst_right = (float) width;
                dst_bottom = (float) width;
            } else {
                roundPx = (float) (height / 2);
                float output = (float) ((width - height) / 2);
                left = output;
                right = (float) width - output;
                top = 0.0F;
                bottom = (float) height;
                width = height;
                dst_left = 0.0F;
                dst_top = 0.0F;
                dst_right = (float) height;
                dst_bottom = (float) height;
            }

            Bitmap output1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output1);
            Paint paint = new Paint();
            Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
            Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
            RectF rectF = new RectF(dst);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output1;
        }
    }

    public static synchronized Bitmap getCompressdImage(String filePath, int height, int width) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inSampleSize = computeSampleHQ(opts, height, width);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return bitmap;
    }

    public static synchronized Bitmap getCompressedImage(String filePath, int maxNumOfPixels) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return bitmap;
    }

    public static boolean bitmap2jpeg(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        } else {
            Bitmap bm = null;
            FileOutputStream fos = null;

            boolean e;
            try {
                bm = BitmapFactory.decodeFile(filename);
                if (bm == null) {
                    e = false;
                    return e;
                }

                fos = new FileOutputStream(new File(filename.replace(".bmp", ".jpg")));
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                file.delete();
                e = true;
            } catch (OutOfMemoryError var19) {
                var19.printStackTrace();
                System.gc();
                return false;
            } catch (FileNotFoundException var20) {
                var20.printStackTrace();
                return false;
            } catch (IOException var21) {
                var21.printStackTrace();
                return false;
            } finally {
                if (bm != null && !bm.isRecycled()) {
                    bm.recycle();
                }

                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException var18) {
                    var18.printStackTrace();
                }

            }

            return e;
        }
    }

    public static boolean bitmap2File(Bitmap bitmap, String absoluteFileName) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File(absoluteFileName));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            boolean e = true;
            return e;
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return false;
    }

    public static int computeSampleHQ(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        int height = options.outHeight;
        int width = options.outWidth;
        byte inSampleSize = 1;
        return inSampleSize;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            for (roundedSize = 1; roundedSize < initialSize; roundedSize <<= 1) {
                ;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = (double) options.outWidth;
        double h = (double) options.outHeight;
        int lowerBound = maxNumOfPixels == -1 ? 1 : (int) Math.ceil(Math.sqrt(w * h / (double) maxNumOfPixels));
        int upperBound = minSideLength == -1 ? 128 : (int) Math.min(Math.floor(w / (double) minSideLength), Math.floor(h / (double) minSideLength));
        return upperBound < lowerBound ? lowerBound : (maxNumOfPixels == -1 && minSideLength == -1 ? 1 : (minSideLength == -1 ? lowerBound : upperBound));
    }

    public static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        if (bmp == null) {
            return null;
        } else {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, output);
            byte[] result = output.toByteArray();
            try {
                output.close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }
            if (needRecycle) {
                bmp.recycle();
            }
            return result;
        }
    }

    @SuppressLint({"NewApi"})
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        boolean be = true;
        int be1;
        if (beWidth < beHeight) {
            be1 = beWidth;
        } else {
            be1 = beHeight;
        }

        if (be1 <= 0) {
            be1 = 1;
        }

        options.inSampleSize = be1;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, 2);
        return bitmap;
    }

    /**
     * 匹配不同手机进行相应的图片压缩
     *
     * @param photo   图片
     * @param context 上下文
     * @return
     */
    public static int getScaleForBitmap(Bitmap photo, Context context) {
        //拿到图片宽高
        int imageWidth = photo.getWidth();
        int imageHeight = photo.getHeight();

        Display dp = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //拿到屏幕宽高
        int screenWidth = dp.getWidth();
        int screenHeight = dp.getHeight();

        //计算缩放比例
        int scale = 1;
        int scaleWidth = imageWidth / screenWidth;
        int scaleHeight = imageHeight / screenHeight;
        //缩小尺度为较大的值
        if (scaleWidth >= scaleHeight && scaleWidth >= 1) {
            scale = scaleWidth;
        } else if (scaleWidth < scaleHeight && scaleHeight >= 1) {
            scale = scaleHeight;
        }
        return scale;
    }

    /**
     * 重新设置图片大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = context.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     *
     * @param file
     * @return
     */
    public static Bitmap getBitmapByFile(File file) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }

    public static Bitmap getBitmapByPath(String filePath,
                                         BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

}
