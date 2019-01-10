package project.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 创建本地文件夹工具类
 * (该部分可以根据自己的项目创建相应的资源文件夹)
 * Created by zby on 2016/12/9.
 */
public class PFileUtil {
    public static final int RECORD = -1;
    public static final int SNAPSHOT = -2;
    private static PFileUtil fileUtil;
    private final String sropImagePath = "/Pac/Srop/照片";
    private final String sropVideoPath = "/Pac/Srop/视频";
    private final String picturePath = "/Pac/消息中心";
    private final String recordPath = "/Pac/我的录像";
    private final String cameraPath = "/Pac/保存图片";
    private final String downloadPath = "/Pac/下载";
    private final String recordPreviewPath = "/Pac/我的录像/缩略图";
    private final String logPath = "/Pac/Log";
    private File snapshotFile;
    private File recordFile;
    private File cameraFile;
    private File downloadFile;
    private File recordPreviewFile;
    private File logFile;
    private File sropImageFile, sropVideoFile;

    private PFileUtil() {
    }

    public static PFileUtil getInstance() {
        if (fileUtil == null) {
            fileUtil = new PFileUtil();
        }
        return fileUtil;
    }

    public static void writeFile(File file, byte[] content, boolean append) throws IOException {
        FileOutputStream pushOutputStream = new FileOutputStream(file, append);
        pushOutputStream.write(content);
        pushOutputStream.close();
    }

    public static String readStringFile(File file) throws IOException {
        return new String(readFile(file));
    }

    public static byte[] readFile(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        boolean len = false;

        int len1;
        while ((len1 = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, len1);
        }

        byte[] result = baos.toByteArray();
        fis.close();
        baos.close();
        return result;
    }

    public static File createFile(File dir, String name) throws IOException {
        return createFile(dir.getPath() + "/" + name);
    }

    public static File createFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }

    }

    public static void EventCacheManagement(File eventFile) {
        if (eventFile.exists()) {
            List events = Arrays.asList(eventFile.listFiles());
            if (events.size() > 100) {
                Collections.sort(events, new PFileUtil.FileComparator());

                for (int i = 50; i < events.size(); ++i) {
                    File event = (File) events.get(i);
                    if (event != null && event.exists() && event.isFile()) {
                        event.delete();
                    }
                }
            }
        }

    }

    /**
     * 得到所有的缓存文件大小
     *
     * @param context 上下文
     * @return 文件大小(字符串形式)
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 删除所有的缓存
     *
     * @param context 上下文
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 获取某个文件夹的总大小
     *
     * @param file getExternalCacheDir() 获取缓存文件夹
     * @return 文件大小
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size 转 字符串
     * @return 格式化后的文件大小
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 根据uri转换成为文件的绝对路径
     *
     * @param context 上下文
     * @param uri     uri路径
     * @return string 文件路径
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 检查文件是否存在
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    private void initDir() {
        if (Environment.getExternalStorageState().equals("mounted") || !Environment.isExternalStorageRemovable()) {
            sropVideoFile = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getPath()).append(sropVideoPath).toString());
            sropImageFile = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getPath()).append(sropImagePath).toString());
            snapshotFile = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getPath()).append(picturePath).toString());
            recordFile = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getPath()).append(recordPath).toString());
            cameraFile = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getPath()).append(cameraPath).toString());
            recordPreviewFile = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getPath()).append(recordPreviewPath).toString());
            downloadFile = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getPath()).append(downloadPath).toString());
            logFile = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory().getPath()).append(logPath).toString());
        } else {
            sropVideoFile = new File((new StringBuilder()).append(Environment.getRootDirectory().getPath()).append(sropVideoPath).toString());
            sropImageFile = new File((new StringBuilder()).append(Environment.getRootDirectory().getPath()).append(sropImagePath).toString());
            snapshotFile = new File((new StringBuilder()).append(Environment.getRootDirectory().getPath()).append(picturePath).toString());
            recordFile = new File((new StringBuilder()).append(Environment.getRootDirectory().getPath()).append(recordPath).toString());
            cameraFile = new File((new StringBuilder()).append(Environment.getRootDirectory().getPath()).append(cameraPath).toString());
            recordPreviewFile = new File((new StringBuilder()).append(Environment.getRootDirectory().getPath()).append(recordPreviewPath).toString());
            downloadFile = new File((new StringBuilder()).append(Environment.getRootDirectory().getPath()).append(downloadPath).toString());
            logFile = new File((new StringBuilder()).append(Environment.getRootDirectory().getPath()).append(logPath).toString());
        }
        if (!sropImageFile.exists()) {
            sropImageFile.mkdirs();
        }
        if (!sropVideoFile.exists()) {
            sropVideoFile.mkdirs();
        }
        if (!snapshotFile.exists())
            snapshotFile.mkdirs();
        if (!recordFile.exists())
            recordFile.mkdirs();
        if (!cameraFile.exists())
            cameraFile.mkdirs();
        if (!recordPreviewFile.exists())
            recordPreviewFile.mkdirs();
        if (!downloadFile.exists())
            downloadFile.mkdirs();
        if (!logFile.exists())
            logFile.mkdirs();
    }

    /**
     * 获取srop照片保存路径
     */
    public File getSropImageFile() {
        if (sropImageFile == null || !sropImageFile.exists()) {
            fileUtil.initDir();
        }
        return sropImageFile;
    }

    /**
     * 获取srop视频保存路径
     */
    public File getSropVideoFile() {
        if (sropVideoFile == null || !sropVideoFile.exists()) {
            fileUtil.initDir();
        }
        return sropVideoFile;
    }

    /**
     * 获取截图文件夹路径
     *
     * @return 路径
     */
    public File getSnapShotFile() {
        if (snapshotFile == null || !snapshotFile.exists()) {
            fileUtil.initDir();
        }
        return snapshotFile;
    }

    /**
     * 获取录像文件夹路径
     *
     * @return 路径
     */
    public File getRecordFile() {
        if (recordFile == null || !recordFile.exists()) {
            fileUtil.initDir();
        }
        return recordFile;
    }

    /**
     * 获取保存图片路径
     *
     * @return 路径
     */
    public File getCameraFile() {
        if (cameraFile == null || !cameraFile.exists()) {
            fileUtil.initDir();
        }
        return cameraFile;
    }

    /**
     * 获取下载路径
     *
     * @return 路径
     */
    public File getDownloadFile() {
        if (downloadFile == null || !downloadFile.exists()) {
            fileUtil.initDir();
        }
        return downloadFile;
    }

    //获取视频缩略图文件路径(录像时调用)
    public File getPreviewFile() {
        if (recordPreviewFile == null || !recordPreviewFile.exists()) {
            fileUtil.initDir();
        }
        return recordPreviewFile;
    }

    /**
     * 获取日志路径
     *
     * @return 路径
     */
    public File getLogFile() {
        if (logFile == null || !logFile.exists()) {
            fileUtil.initDir();
        }
        return logFile;
    }

    /**
     * 删除下载APK
     *
     * @return 是否下载成功
     */
    public boolean deleteAPK() {
        File path = getDownloadFile();
        File[] files = path.listFiles();
        if (files == null || files.length < 1)
            return true;
        return path.delete();
    }

    /**
     * 得到指定文件夹下文件个数
     *
     * @param type RECORD 或 SNAPSHOT
     * @return 文件个数
     */
    public int getFileNum(int type) {
        int length = 0;
        File file = null;
        if (type == RECORD) {
            file = getRecordFile();
        } else if (type == SNAPSHOT) {
            file = getSnapShotFile();
        }
        File[] files = file.listFiles();
        if (file != null && files != null && files.length != 0) {
            length = files.length;
            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    length -= 1;
                }
            }
        }
        return length;
    }

    public long getAvailableSpare() {
        return this.calcAvailableSpare();
    }

    @SuppressLint({"NewApi"})
    public void updateGallery(String filename, Context context) {
        MediaScannerConnection.scanFile(context, new String[]{filename}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                PLog.i("Scanned " + path + ":");
                PLog.i("-> uri=" + uri);
            }
        });
    }

    public long calcAvailableSpare() {
        String sdCard = "";
        if (Environment.getExternalStorageState().equals("mounted")) {
            sdCard = Environment.getExternalStorageDirectory().getPath();
        } else {
            sdCard = Environment.getDataDirectory().getPath();
        }

        StatFs statFs = new StatFs(sdCard);
        long blockSize = (long) statFs.getBlockSize();
        long blocks = (long) statFs.getAvailableBlocks();
        long spare = blocks * blockSize / 1048576L;
        return spare;
    }

    public static class FileComparator implements Comparator<File> {
        public FileComparator() {
        }

        public int compare(File file1, File file2) {
            return file1.lastModified() < file2.lastModified() ? -1 : 1;
        }
    }
}
