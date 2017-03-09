package com.owant.thinkmap;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.owant.thinkmap.databinding.ActivitySplashBinding;
import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.util.AndroidUtil;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;

    @Override
    public void onBaseBindView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        binding.tvVersion.setText(AndroidUtil.getAppVersion(this));
    }
}
