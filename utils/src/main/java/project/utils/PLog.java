package project.utils;

import android.util.Log;

/**
 * （log打印工具类，简化Log打印）
 * Created by zby on 2016/12/9.
 */
public class PLog {

    static String className;
    static String methodName;
    static int lineNumber;
    static boolean DEBUG = true;

    private PLog() {
    }

    public static boolean isDebuggable() {
        return DEBUG;
    }

    private static String createLog(String log) {
        String name = lineNumber + ":" + log;
        return name;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.e(className, createLog(message));
        }
    }

    public static void e(String message, Throwable tr) {
        if (isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.e(className, createLog(message), tr);
        }
    }

    public static void i(String message) {
        if (isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.i(className, createLog(message));
        }
    }

    public static void d(String message) {
        if (isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.d(className, createLog(message));
        }
    }

    public static void v(String message) {
        if (isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.v(className, createLog(message));
        }
    }

    public static void w(String message) {
        if (isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.w(className, createLog(message));
        }
    }
}
