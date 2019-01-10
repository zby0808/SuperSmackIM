package project.utils;

import android.hardware.Camera;
import android.os.Build;

/**
 * 摄像机的工具类
 * Created by zby on 2017/2/23.
 */

public class CameraUtil {

    /**
     * 判断是否支持前置摄像头，不支持换后置摄像头
     *
     * @return 前后置摄像头参数
     */
    public static int hasFrontCamera() {
        int cameraID = 0;
        boolean hasFront = hasFrontFacingCamera();//是否有前置摄像头
        cameraID = hasFront ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        return cameraID;
    }

    /**
     * 判断是是否支持后置摄像头
     *
     * @return
     */
    public static boolean hasBackFacingCamera() {
        final int CAMERA_FACING_BACK = 0;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    /**
     * 判断是否支持前置摄像头
     *
     * @return
     */
    public static boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    private static boolean checkCameraFacing(final int facing) {
        if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }
}
