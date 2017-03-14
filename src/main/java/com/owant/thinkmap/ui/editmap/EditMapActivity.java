package com.owant.thinkmap.ui.editmap;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.owant.thinkmap.R;
import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.ui.EditAlertDialog;
import com.owant.thinkmap.ui.codemode.CodeModeActivity;
import com.owant.thinkmap.util.DensityUtils;
import com.owant.thinkmap.view.NodeView;
import com.owant.thinkmap.view.RightTreeLayoutManager;
import com.owant.thinkmap.view.TreeView;
import com.owant.thinkmap.view.TreeViewItemClick;
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

    private EditAlertDialog addSubNodeDialog;
    private EditAlertDialog addNodeDialog;
    private EditAlertDialog editNodeDialog;

    @Override
    public int onBaseLayoutId() {
        return R.layout.activity_edit_think_map;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDialog(addNodeDialog);
        clearDialog(addSubNodeDialog);
    }

    @Override
    public void onBaseBindView() {
        bindViews();
    }

    private void intentToCodeMode() {
        Intent codeModeIntent = new Intent(EditMapActivity.this, CodeModeActivity.class);
        startActivity(codeModeIntent);
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
                intentToCodeMode();
            }
        });

        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {
                mPresenter.editTreeNote();
            }
        });

        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {
                NodeModel<String> treeNode = ((NodeView) view).getTreeNode();
                mPresenter.changeNode(treeNode);
            }
        });
        editMapTreeView.setTreeViewItemClick(new TreeViewItemClick() {
            @Override
            public void onItemClick(View item) {

            }
        });

        int dx = DensityUtils.dp2px(this, 20);
        int dy = DensityUtils.dp2px(this, 20);
        int mHeight = DensityUtils.dp2px(this, 720);
        editMapTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, mHeight));
        editMapTreeView.setTreeModel(mPresenter.getTreeModel());

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
        if (editMapTreeView.getCurrentFocusNode().getParentNode() == null) {
            Toast.makeText(this, getString(R.string.cannot_add_node), Toast.LENGTH_SHORT).show();
        } else if (addNodeDialog == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            View inflate = factory.inflate(R.layout.dialog_edit_input, null);
            addNodeDialog = new EditAlertDialog(EditMapActivity.this);
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
                    if (addNodeDialog != null && addNodeDialog.isShowing())
                        addNodeDialog.dismiss();
                }
            });
            addNodeDialog.show();
        } else {
            addNodeDialog.clearInput();
            addNodeDialog.show();
        }

    }

    private void clearDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showAddSubNode() {
        if (addSubNodeDialog == null) {
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
        } else {
            addSubNodeDialog.clearInput();
            addSubNodeDialog.show();
        }
    }

    @Override
    public void showFocusMid() {
        editMapTreeView.focusMidLocation();
    }

    @Override
    public void showCodeMode() {

    }

    @Override
    public void showSaveMap() {

    }

    @Override
    public void showEditNote(final NodeModel<String> nodeModel) {
        if (editNodeDialog == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            View inflate = factory.inflate(R.layout.dialog_edit_input, null);
            editNodeDialog = new EditAlertDialog(this);
            editNodeDialog.setView(inflate);
            editNodeDialog.setDivTitle(getString(R.string.edit_node));
        }
        editNodeDialog.setNodeModel(nodeModel);
        editNodeDialog.setInput(nodeModel.getValue());
        editNodeDialog.addDeleteCallBack(new EditAlertDialog.DeleteCallBack() {
            @Override
            public void onDeleteModel(NodeModel<String> model) {
                try {
                    editMapTreeView.deleteNode(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDelete() {
            }
        });
        editNodeDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
            @Override
            public void onEdit(String value) {
                if (TextUtils.isEmpty(value)) {
                    value = getString(R.string.null_node);
                }
                editMapTreeView.changeNodeValue(nodeModel, value);
                clearDialog(editNodeDialog);
            }
        });
        editNodeDialog.show();
    }

    @Override
    public String getDefualtPlanString() {
        return getString(R.string.defualt_my_plan);
    }
}
