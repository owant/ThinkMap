package com.owant.thinkmap.base;

import android.content.Context;
import android.view.View;

/**
 * Created by owant on 10/03/2017.
 */

public interface Presenter {

    void onCreated(Context context);

    void onRecycle();

}
