package com.owant.thinkmap.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.owant.thinkmap.R;
import com.owant.thinkmap.base.BaseAlertDialog;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.util.AndroidUtil;

import java.util.List;

/**
 * Created by owant on 24/02/2017.
 */

public class EditAlertDialog extends BaseAlertDialog {

    private TextView dialog_edit_tv_title;
    private EditText dialog_edit_et_input;
    private Button dialog_btn_enter;
    private Button dialog_btn_delete;
    private ImageView dialog_edit_iv_input_clear;
    private TextView dialog_edit_tv_had_same_file;
    private RelativeLayout dialog_edit_check_state;

    private String conditonDeleteTextValue;
    private String conditonEnterTextValue;

    private EnterCallBack enterCallBack;
    private DeleteCallBack deleteCallBack;

    private NodeModel<String> nodeModel;
    private List<String> checkLists;


    protected EditAlertDialog(@NonNull Context context) {
        super(context);
    }

    protected EditAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onBaseBindView() {
        dialog_edit_tv_title = (TextView) findViewById(R.id.dialog_edit_tv_title);
        dialog_edit_et_input = (EditText) findViewById(R.id.dialog_edit_et_input);
        dialog_btn_enter = (Button) findViewById(R.id.dialog_btn_enter);
        dialog_btn_delete = (Button) findViewById(R.id.dialog_btn_delete);
        dialog_edit_iv_input_clear = (ImageView) findViewById(R.id.dialog_edit_iv_input_clear);
        dialog_edit_check_state = (RelativeLayout) findViewById(R.id.dialog_edit_check_state);
        dialog_edit_tv_had_same_file = (TextView) findViewById(R.id.dialog_edit_tv_had_same_file);

        if (dialog_btn_enter != null) {
            dialog_btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (enterCallBack != null) {
                        String value = getInput().toString() + "";

                        //同名文件提示更改
                        if (checkLists != null && !TextUtils.isEmpty(value)
                                && dialog_edit_check_state != null
                                && dialog_edit_tv_had_same_file != null) {

                            if (checkLists.contains(value)) {

                                dialog_edit_tv_had_same_file.setVisibility(View.VISIBLE);

                                Animation shake = new TranslateAnimation(0, 10, 0, 0);
                                shake.setDuration(1000);
                                //重三次
                                shake.setInterpolator(new CycleInterpolator(7));
                                dialog_edit_check_state.startAnimation(shake);
                                return;
                            } else {
                                dialog_edit_tv_had_same_file.setVisibility(View.INVISIBLE);
                            }
                        }

                        enterCallBack.onEdit(value);
                        AndroidUtil.hideKeyboard(getContext(), dialog_edit_et_input);
                    }
                }
            });
        }

        if (dialog_edit_iv_input_clear != null) {
            dialog_edit_iv_input_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setInput("");
                }
            });
        }

        if (dialog_btn_delete != null) {
            dialog_btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteCallBack != null) {

                        if (dialog_edit_tv_title.getText() == EditAlertDialog.this.getContext()
                                .getResources().getString(R.string.save_file)) {
                            deleteCallBack.onDelete();

                        } else if (nodeModel != null) {
                            //根节点不能进行删除
                            NodeModel<String> parentNode = nodeModel.getParentNode();
                            if (parentNode != null) {
                                deleteCallBack.onDeleteModel(nodeModel);
                            }
                        }

                    }
                    dismiss();
                    AndroidUtil.hideKeyboard(getContext(), dialog_edit_et_input);
                }
            });
        }
    }

    public void setDivTitle(String title) {
        dialog_edit_tv_title.setText(title);
    }

    private Editable getInput() {
        return dialog_edit_et_input.getText();
    }

    public void setInput(String value) {
        if (dialog_edit_et_input != null) {
            dialog_edit_et_input.setText(value);
        }
    }

    public void setConditionEnterTextValue(String conditionEnterTextValue) {
        this.conditonEnterTextValue = conditionEnterTextValue;
        if (dialog_btn_enter != null) {
            dialog_btn_enter.setText(conditionEnterTextValue);
        }
    }

    public void setConditionDeleteTextValue(String conditionDeleteTextValue) {
        this.conditonDeleteTextValue = conditionDeleteTextValue;
        if (dialog_btn_delete != null) {
            dialog_btn_delete.setText(conditionDeleteTextValue);
        }
    }

    public void setCheckLists(List<String> checkLists) {
        this.checkLists = checkLists;
    }

    public void setNodeModel(NodeModel<String> nodeModel) {
        this.nodeModel = nodeModel;
        if (nodeModel.getParentNode() == null) {
            if (dialog_btn_delete != null) {
                dialog_btn_delete.setEnabled(false);
            }
        }
    }

    public void addEnterCallBack(EnterCallBack callBack) {
        this.enterCallBack = callBack;
    }

    public void addDeleteCallBack(DeleteCallBack callBack) {
        deleteCallBack = callBack;
    }

    public interface EnterCallBack {
        void onEdit(String value);
    }

    public interface DeleteCallBack {
        void onDeleteModel(NodeModel<String> nodeModel);

        void onDelete();
    }

}
