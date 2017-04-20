package com.owant.thinkmap.ui.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.owant.thinkmap.R;
import com.owant.thinkmap.base.BaseActivity;

/**
 * Created by owant on 30/03/2017.
 */

public class AboutUsActivity extends BaseActivity {
    @Override
    protected void onBaseIntent() {

    }

    @Override
    protected void onBasePreLayout() {

    }

    @Override
    protected int onBaseLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_about_us;
    }

    @Override
    protected void onBaseBindView() {
        getSupportActionBar().setTitle(R.string.about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
