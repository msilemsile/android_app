package me.msile.app.androidapp.common.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import me.msile.app.androidapp.common.core.ApplicationHolder;

/**
 * 进程工具类
 */
public final class ProcessUtils {

    private ProcessUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether app running in the main process.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainProcess() {
        return ApplicationHolder.getAppContext().getPackageName().equals(getCurrentProcessName());
    }

    /**
     * Return the name of current process.
     *
     * @return the name of current process
     */
    public static String getCurrentProcessName() {
        String name = getCurrentProcessNameByFile();
        if (!TextUtils.isEmpty(name)) return name;
        name = getCurrentProcessNameByAms();
        if (!TextUtils.isEmpty(name)) return name;
        name = getCurrentProcessNameByReflect();
        return name;
    }

    private static String getCurrentProcessNameByFile() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            Log.d("ProcessUtils", "getCurrentProcessNameByFile error");
            return "";
        }
    }

    private static String getCurrentProcessNameByAms() {
        try {
            ActivityManager am = (ActivityManager) ApplicationHolder.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null) return "";
            List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
            if (info == null || info.size() == 0) return "";
            int pid = android.os.Process.myPid();
            for (ActivityManager.RunningAppProcessInfo aInfo : info) {
                if (aInfo.pid == pid) {
                    if (aInfo.processName != null) {
                        return aInfo.processName;
                    }
                }
            }
        } catch (Exception e) {
            Log.d("ProcessUtils", "getCurrentProcessNameByAms error");
            return "";
        }
        return "";
    }

    private static String getCurrentProcessNameByReflect() {
        String processName = "";
        try {
            Application app = ApplicationHolder.getAppContext();
            Field loadedApkField = app.getClass().getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(app);

            Field activityThreadField = loadedApk.getClass().getDeclaredField("mActivityThread");
            activityThreadField.setAccessible(true);
            Object activityThread = activityThreadField.get(loadedApk);

            Method getProcessName = activityThread.getClass().getDeclaredMethod("getProcessName");
            processName = (String) getProcessName.invoke(activityThread);
        } catch (Exception e) {
            Log.d("ProcessUtils", "getCurrentProcessNameByReflect error");
        }
        return processName;
    }
}
