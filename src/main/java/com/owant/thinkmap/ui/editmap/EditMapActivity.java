package com.owant.thinkmap.ui.editmap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.owant.thinkmap.R;

import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.ui.codemode.CodeModeActivity;
import com.owant.thinkmap.util.DensityUtils;
import com.owant.thinkmap.view.RightTreeLayoutManager;
import com.owant.thinkmap.view.TreeView;
import com.owant.thinkmap.view.TreeViewItemLongClick;

import java.io.Serializable;

/**
 * Created by owant on 13/03/2017.
 */

public class EditMapActivity extends BaseActivity implements EditMapContract.View {

    private static final String TAG = "EditMapActivity";

    private static final String SAVE_TREE_MODEL_DATA = "tree_model";

    private TreeView editMapTreeView;
    private Button btnAddSub;
    private Button btnAddNode;
    private Button btnFocusMid;
    private Button btnCodeMode;

    private EditMapContract.Presenter mPresenter;

    @Override
    public int onBaseLayoutId() {
        return R.layout.activity_edit_think_map;
    }

    private void bindViews() {

        mPresenter = new EditMapPresenter(this);
        mPresenter.start();

        editMapTreeView = (TreeView) findViewById(R.id.edit_map_tree_view);
        btnAddSub = (Button) findViewById(R.id.btn_add_sub);
        btnAddNode = (Button) findViewById(R.id.btn_add_node);
        btnFocusMid = (Button) findViewById(R.id.btn_focus_mid);
        btnCodeMode = (Button) findViewById(R.id.btn_code_mode);

        btnAddSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addSubNote();
            }
        });

        btnAddNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addSomeFloorNote();
            }
        });

        btnFocusMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.focusMidMap();
            }
        });

        btnCodeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Intent to CodeMode
                intentToCodeMode();
            }
        });

        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {
                mPresenter.editTreeNote();
            }
        });

        int dx = DensityUtils.dp2px(this, 20);
        int dy = DensityUtils.dp2px(this, 20);
        int mHeight = DensityUtils.dp2px(this, 720);
        editMapTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, mHeight));
        editMapTreeView.setTreeModel(mPresenter.getTreeModel());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_TREE_MODEL_DATA, mPresenter.getTreeModel());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Serializable serializable = savedInstanceState.getSerializable(SAVE_TREE_MODEL_DATA);
        mPresenter.reSetTreeData(serializable);
        editMapTreeView.setTreeModel(mPresenter.getTreeModel());
    }

    private void intentToCodeMode() {
        Intent codeModeIntent = new Intent(EditMapActivity.this, CodeModeActivity.class);
        startActivity(codeModeIntent);
    }

    @Override
    public void onBaseBindView() {
        bindViews();
    }

    @Override
    public void setPresenter(EditMapContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        } else {
            Log.e(TAG, "setPresenter: the presenter is null");
        }
    }

    @Override
    public void showAddNode() {

    }

    @Override
    public void showAddSubNode() {

    }

    @Override
    public void showFocusMid() {

    }

    @Override
    public void showCodeMode() {

    }

    @Override
    public void showSaveMap() {

    }

    @Override
    public void showEditNote(EditMapContract.View view) {

    }
}
