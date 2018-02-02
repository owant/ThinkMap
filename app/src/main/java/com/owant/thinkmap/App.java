package com.owant.thinkmap;

import android.app.Application;

import com.owant.thinkmap.util.SharePreUtil;

/**
 * Created by owant on 22/03/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化工具
        SharePreUtil.getInstance().init(getApplicationContext());
//        LeakCanary.install(this);

    }
}
