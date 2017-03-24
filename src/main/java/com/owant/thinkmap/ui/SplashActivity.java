package com.owant.thinkmap.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.owant.thinkmap.AppPermissions;
import com.owant.thinkmap.R;
import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.ui.workspace.WorkSpaceActivity;
import com.owant.thinkmap.util.AndroidUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onBaseIntent() {

    }

    @Override
    protected void onBasePreLayout() {

    }

    @Override
    protected int onBaseLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    protected void onBaseBindView() {

        if (AndroidUtil.isMPermission()) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this,
                    AppPermissions.permission_storage[0]) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(SplashActivity.this,
                            AppPermissions.permission_storage[1]) != PackageManager.PERMISSION_GRANTED) {

                requestStoragePermission();

            } else {
                intentToWorkSpace();
            }

        } else {

            intentToWorkSpace();
        }

    }

    private void intentToWorkSpace() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1200);
                    Intent intent = new Intent(SplashActivity.this, WorkSpaceActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onLoadData() {

    }

    /**
     * 请求内存卡权限
     */
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(SplashActivity.this
                , AppPermissions.permission_storage,
                AppPermissions.request_permission_storage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppPermissions.request_permission_storage) {
            if (AndroidUtil.verifyPermissions(grantResults)) {
                intentToWorkSpace();
            } else {
                Toast.makeText(this, "the permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
