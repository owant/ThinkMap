package com.owant.thinkmap.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.owant.thinkmap.R;
import com.owant.thinkmap.base.BaseActivity;

/**
 * Created by owant on 08/03/2017.
 */

public class TestActivity extends BaseActivity {

    @Override
    protected void onBaseIntent() {

    }

    @Override
    protected void onBasePreLayout() {

    }

    @Override
    protected int onBaseLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_test;
    }

    @Override
    protected void onBaseBindView() {

    }

    @Override
    protected void onLoadData() {

    }

    public void makeOwantFile(View view) {
        ExampleCreator.createExampleMapVersion();
        ExampleCreator.createExampleHowToUse();
    }
}
