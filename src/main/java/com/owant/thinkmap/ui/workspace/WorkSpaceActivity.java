package com.owant.thinkmap.ui.workspace;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.owant.thinkmap.AppConstants;
import com.owant.thinkmap.AppPermissions;
import com.owant.thinkmap.R;
import com.owant.thinkmap.adapter.CurrentWorkAdapter;
import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.line.EaseCubicInterpolator;
import com.owant.thinkmap.model.CurrentFileModel;
import com.owant.thinkmap.ui.editmap.EditMapActivity;
import com.owant.thinkmap.util.AndroidUtil;

import java.util.ArrayList;

/**
 * Created by owant on 08/03/2017.
 */
public class WorkSpaceActivity extends BaseActivity implements WorkSpaceContract.View {

    private static final String TAG = "WorkSpaceActivity";
    private WorkSpaceContract.Presenter mPresenter;

    private Toolbar toolBar;
    private ListView lvCurrentFiles;
    private TextView tvWorkSpaceEmptyView;

    private CurrentWorkAdapter mCurrentWorkAdapter;

    @Override
    protected void onBaseIntent() {

    }

    @Override
    protected void onBasePreLayout() {

    }

    @Override
    protected int onBaseLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_work_space;
    }

    public void bindViews() {
        toolBar = (Toolbar) findViewById(R.id.tool_bar);
        lvCurrentFiles = (ListView) findViewById(R.id.lv_current_files);
        tvWorkSpaceEmptyView = (TextView) findViewById(R.id.tv_work_space_empty_view);
    }

    @Override
    protected void onBaseBindView() {
        bindViews();
        setSupportActionBar(toolBar);
        initListViewAnim();

        lvCurrentFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mPresenter.getItemFilePath(position);
                //跳转到Edit
                intentToEditMap(view, path);
            }
        });

        lvCurrentFiles.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });


        mPresenter = new WorkSpacePresenter(this);
        mPresenter.start();
        mPresenter.onEmptyView();

        if (AndroidUtil.isMPermission()) {
            if (ContextCompat.checkSelfPermission(WorkSpaceActivity.this,
                    AppPermissions.permission_storage[0]) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(WorkSpaceActivity.this,
                            AppPermissions.permission_storage[1]) != PackageManager.PERMISSION_GRANTED) {

                requestStoragePermission();

            } else {
                mPresenter.onLoadOwantData();
            }

        } else {
            mPresenter.onLoadOwantData();
        }
    }

    private void initListViewAnim() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.3f);
        controller.setOrder(0);
        controller.setInterpolator(new EaseCubicInterpolator(0.47f, 0.01f, 0.44f, 0.99f));
        lvCurrentFiles.setLayoutAnimation(controller);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.work_space_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_work_space_add_a_map:
                intentToEditMap();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 请求内存卡权限
     */
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(WorkSpaceActivity.this
                , AppPermissions.permission_storage,
                AppPermissions.request_permission_storage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppPermissions.request_permission_storage) {
            if (AndroidUtil.verifyPermissions(grantResults)) {
                mPresenter.onLoadOwantData();
            } else {
                Toast.makeText(this, "You denied the storage permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    public void setPresenter(WorkSpaceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEmptyView() {
        tvWorkSpaceEmptyView.setVisibility(View.VISIBLE);
        lvCurrentFiles.setEmptyView(tvWorkSpaceEmptyView);
        lvCurrentFiles.setAdapter(null);
    }

    @Override
    public void setListData(ArrayList<CurrentFileModel> listData) {
        if (mCurrentWorkAdapter == null) {
            mCurrentWorkAdapter = new CurrentWorkAdapter(this, listData);
            lvCurrentFiles.setAdapter(mCurrentWorkAdapter);
        }

        mCurrentWorkAdapter.setLists(listData);
        mCurrentWorkAdapter.notifyDataSetInvalidated();

        Log.i(TAG, "setListData: notifyDataSetInvalidation");

    }

    @Override
    public void refreshListData() {
        mCurrentWorkAdapter.notifyDataSetChanged();
    }

    @Override
    public String getOwantDefaultPath() {
        String saveFileParentPath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstants.owant_maps;
        return saveFileParentPath;
    }

    private void intentToEditMap() {
        Intent editIntent = new Intent(WorkSpaceActivity.this, EditMapActivity.class);
        startActivity(editIntent);
    }

    public void intentToEditMap(View view, String filePath) {
        Intent transIntent = new Intent(WorkSpaceActivity.this, EditMapActivity.class);
        ActivityOptions transitionActivityOptions = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    WorkSpaceActivity.this,
                    Pair.create(view, getString(R.string.trans_item)));
        }

        Uri uri = Uri.parse(filePath);
        transIntent.setData(uri);
        if (transitionActivityOptions != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(transIntent, transitionActivityOptions.toBundle());
        } else {
            startActivity(transIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onLoadOwantData();
        }
    }
}
