package com.owant.thinkmap.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by owant on 24/02/2017.
 */

public abstract class BaseAlertDialog extends AlertDialog {

    private View mainView;

    protected BaseAlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected BaseAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        mainView = view;
        onBaseBindView();
    }

    @Nullable
    @Override
    public View findViewById(@IdRes int id) {
        if (mainView != null) {
            return mainView.findViewById(id);
        }
        return super.findViewById(id);
    }

    protected abstract void onBaseBindView();



}
