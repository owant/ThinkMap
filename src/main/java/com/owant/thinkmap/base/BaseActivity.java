package com.owant.thinkmap.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by owant on 22/02/2017.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onBaseIntent();

        onBasePreLayout();

        setContentView(onBaseLayoutId());

        onBaseBindView();
    }

    /**
     * Intent进来的数据处理
     */
    public void onBaseIntent() {
    }

    /**
     * 设置布局之前的处理
     */
    public void onBasePreLayout() {
    }

    /**
     * 返回布局文件
     *
     * @return id
     */
    public abstract int onBaseLayoutId();

    public abstract void onBaseBindView();

}
