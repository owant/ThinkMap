package com.owant.thinkmap.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.owant.thinkmap.R;
import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.view.ScrollMenuView;

/**
 * Created by owant on 31/03/2017.
 */

public class TestScrollView extends BaseActivity {

    private ScrollMenuView mScrollMenuView;

    @Override
    protected void onBaseIntent() {

    }

    @Override
    protected void onBasePreLayout() {

    }

    @Override
    protected int onBaseLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_test_scroll_view;
    }

    @Override
    protected void onBaseBindView() {

        mScrollMenuView = (ScrollMenuView) findViewById(R.id.scroll_menu);


        mScrollMenuView.addMenuView(R.mipmap.menu_delete,
                "delete1");
        mScrollMenuView.addMenuView(R.mipmap.menu_delete,
                "delete2");

        View view = LayoutInflater.from(this).inflate(R.layout.test_3, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mScrollMenuView.addMenuTopView(view);
    }

    @Override
    protected void onLoadData() {

    }
}
