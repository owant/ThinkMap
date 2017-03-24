package com.owant.thinkmap;

import android.Manifest;

/**
 * Created by owant on 23/03/2017.
 */

public class AppPermissions {

    //权限请求
    public static int request_permission_storage = 2;

    /**
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     */
    public static String[] permission_storage = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

}
