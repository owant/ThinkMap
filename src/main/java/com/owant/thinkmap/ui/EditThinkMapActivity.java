package com.owant.thinkmap.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.owant.thinkmap.R;
import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.util.DensityUtils;
import com.owant.thinkmap.view.RightTreeLayoutManager;
import com.owant.thinkmap.view.TreeView;
import com.owant.thinkmap.view.TreeViewItemClick;
import com.owant.thinkmap.view.TreeViewItemLongClick;

/**
 * Created by owant on 09/03/2017.
 */

public class EditThinkMapActivity extends BaseActivity {

    static final String TAG = "EditThinkMapActivity";

    private TreeView editMapTreeView;
    private Button btnAddSub;
    private Button btnAddNode;
    private Button btnFocusMid;
    private Button btnCodeMode;
    private EditThinkMapPresenter mPresenter;

    private EditAlertDialog addNodeDialog = null;
    private EditAlertDialog editTreeItemDialog = null;
    private EditAlertDialog addSubNodeDialog = null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.saveTreeModel(outState, editMapTreeView);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPresenter.restoreTreeModel(editMapTreeView, savedInstanceState);
    }

    @Override
    public int onBaseLayoutId() {
        return R.layout.activity_edit_think_map;
    }

    public void bindViews() {

        editMapTreeView = (TreeView) findViewById(R.id.edit_map_tree_view);
        btnAddSub = (Button) findViewById(R.id.btn_add_sub);
        btnAddNode = (Button) findViewById(R.id.btn_add_node);
        btnFocusMid = (Button) findViewById(R.id.btn_focus_mid);
        btnCodeMode = (Button) findViewById(R.id.btn_code_mode);

    }

    @Override
    public void onBaseBindView() {

        bindViews();

        int dx = DensityUtils.dp2px(this, 20);
        int dy = DensityUtils.dp2px(this, 20);
        int mHeight = DensityUtils.dp2px(this, 720);
        editMapTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, mHeight));
        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {
                showTreeItemEditDialog(view);
            }
        });
        editMapTreeView.setTreeViewItemClick(new TreeViewItemClick() {
            @Override
            public void onItemClick(View item) {
                Log.i(TAG, "onItemClick: " + item.getTop() + "\t," + item.getLeft() + "\t," +
                        "" + item.getRight() + "\t," + item.getBottom());
            }
        });

        btnAddNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNodeDialog();
            }
        });

        btnAddSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSubNodeDialog();
            }
        });

        btnFocusMid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editMapTreeView.focusMidLocation();
            }
        });

        btnCodeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPresenter = new EditThinkMapPresenter();
        mPresenter.onCreated(getApplicationContext());
        mPresenter.initTreeViewModel(editMapTreeView);
    }

    private void showAddSubNodeDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        View inflate = factory.inflate(R.layout.dialog_edit_input, null);
        addSubNodeDialog = new EditAlertDialog(this);
        addSubNodeDialog.setView(inflate);
        addSubNodeDialog.setDivTitle(getString(R.string.add_a_sub_node));
        addSubNodeDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
            @Override
            public void onEdit(String value) {
                if (TextUtils.isEmpty(value)) {
                    value = "null_node";
                }
                editMapTreeView.addSubNode(value);
                clearDialog(addSubNodeDialog);
            }
        });
        addSubNodeDialog.show();
    }

    private void showAddNodeDialog() {
        if (mPresenter.currentViewIsRootNote(editMapTreeView)) {
            Toast.makeText(EditThinkMapActivity.this, R.string.cannot_add_node, Toast.LENGTH_SHORT).show();
            return;
        } else {
            LayoutInflater factory = LayoutInflater.from(this);
            View inflate = factory.inflate(R.layout.dialog_edit_input, null);
            addNodeDialog = new EditAlertDialog(this);
            addNodeDialog.setView(inflate);
            addNodeDialog.setDivTitle(getString(R.string.add_a_same_floor_node));
            addNodeDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
                @Override
                public void onEdit(String value) {
                    if (TextUtils.isEmpty(value)) {
                        value = getString(R.string.null_node);
                    }
                    editMapTreeView.addNode(value);
                    clearDialog(addNodeDialog);
                }
            });
            addNodeDialog.show();
        }
    }

    private void showTreeItemEditDialog(final View view) {
        LayoutInflater factory = LayoutInflater.from(EditThinkMapActivity.this);
        View dialogView = factory.inflate(R.layout.dialog_edit_input, null);
        editTreeItemDialog = new EditAlertDialog(this);
        editTreeItemDialog.setView(dialogView);
        editTreeItemDialog.setDivTitle(getString(R.string.edit_node));
        editTreeItemDialog.setInput(mPresenter.translationNoteViewValue(view));
        editTreeItemDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
            @Override
            public void onEdit(String value) {
                if (TextUtils.isEmpty(value)) {
                    value = getString(R.string.null_node);
                }
                editMapTreeView.changeNodeValue(mPresenter.getTreeModelFromView(view), value);
                clearDialog(editTreeItemDialog);
            }
        });
        editTreeItemDialog.setNodeModel(mPresenter.getTreeModelFromView(view));
        editTreeItemDialog.addDeleteCallBack(new EditAlertDialog.DeleteCallBack() {
            @Override
            public void onDeleteModel(NodeModel<String> nodeModel) {
                try {
                    editMapTreeView.deleteNode(nodeModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDelete() {

            }
        });
        editTreeItemDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onRecycle();

        clearDialog(editTreeItemDialog);
        clearDialog(addNodeDialog);
        clearDialog(addSubNodeDialog);
    }

    private void clearDialog(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
