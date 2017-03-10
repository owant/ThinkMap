package com.owant.thinkmap.base;

import android.content.Context;

/**
 * Created by owant on 10/03/2017.
 */

public class BasePresenter implements Presenter {

    public Context mContext;

    @Override
    public  void onCreated(Context context) {
        this.mContext = context;
    }

    @Override
    public void onRecycle() {
        this.mContext = null;
    }
}
