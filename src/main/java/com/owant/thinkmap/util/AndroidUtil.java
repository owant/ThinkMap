package com.owant.thinkmap.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AndroidUtil {

    /**
     * 隐藏键盘
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isShouldHideInput(View inputView, MotionEvent event) {
        boolean should = false;
        if (inputView != null && (inputView instanceof EditText)) {
            int[] leftTop = {0, 0};
            inputView.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];

            int bottom = top + inputView.getHeight();
            int right = left + inputView.getWidth();

            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                should = false;
            } else {
                should = true;
            }
        }
        return should;
    }


    /**
     * 显示Toast的信息
     *
     * @param mContext
     * @param toastInfo
     */
    public static void showToast(Context mContext, String toastInfo) {
        Toast mToast = Toast.makeText(mContext, toastInfo, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean netStatus = false;
        ConnectivityManager connectManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            netStatus = networkInfo.isAvailable();
        }
        return netStatus;
    }

    /**
     * 打开设置网络
     *
     * @param mContext
     */
    public static void openSettingsConn(Context mContext, Integer requestCode) {
        Intent settings = null;
        // SDK>10，就是3.0以上的版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            settings = new Intent();
            ComponentName component = new ComponentName("com.android.settings",
                    "com.android.setttins.WirelessSettings");
            settings.setAction("android.intent.action.VIEW");

        }

        if (requestCode == null) {
            mContext.startActivity(settings);
        } else {
            ((Activity) mContext).startActivityForResult(settings, requestCode);
        }
    }

    public static void openSettingsConn(Context mContext) {
        openSettingsConn(mContext, null);
    }

    /**
     * 设置是否启用WIFI网络
     */
    public static void toggleWiFi(Context context, boolean status) {

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);

        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public static String getCurrentSSID(Context context) {
        String ssid = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            ssid = wifiManager.getConnectionInfo().getSSID();
            ssid = ssid.replaceAll("\"", "");
            if (TextUtils.equals("<unknown ssid>", ssid)) {
                ssid = "";
            }
        }
        return ssid;
    }

    /**
     * 开启手机网络
     *
     * @param context
     */
    @Deprecated
    public static void openMobileState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class mClass = manager.getClass();
        boolean state = true;
        Method method = null;
        try {
            method = mClass.getMethod("setMobileDataEnabled", boolean.class);
            method.setAccessible(true);
            method.invoke(manager, state);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    /**
     * 关闭手机网络
     *
     * @param context
     */
    @Deprecated
    public static void closeMobileState(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Class mClass = manager.getClass();
        boolean mobileData = false;
        try {
            Method method = mClass.getMethod("setMobileDataEnabled", new Class[]{});
            method.invoke(manager, false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建并显示一个只包含“是”与“否”按钮简单对话框
     *
     * @param context
     * @param title
     * @param callback
     */
    public static void showAlertDialog(final Context context, final String title, final DialogCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onPositive();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 点击对话框确定按钮后的回调接口
     */
    public interface DialogCallback {
        void onPositive();
    }

    /**
     * 检测当前App是否在前台运行
     *
     * @param context
     * @return true 前台运行，false 后台运行
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        // 正在运行的应用
        ActivityManager.RunningTaskInfo foregroundTask = runningTasks.get(0);
        String packageName = foregroundTask.topActivity.getPackageName();
        String myPackageName = context.getPackageName();

        // 比较包名
        return packageName.equals(myPackageName);
    }

    public static String getAppVersion(Context context) {
        String myVersion = "v ";
        try {
            myVersion = myVersion + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return myVersion;
    }

    public static String getAndroidSystemVersion() {
        return "android " + android.os.Build.VERSION.SDK_INT;
    }

    public static String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     * 判断是否为android 6.0
     *
     * @return true or false
     */
    public static boolean isMPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if(grantResults.length < 1){
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
