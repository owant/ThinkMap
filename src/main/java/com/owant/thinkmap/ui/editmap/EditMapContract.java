package com.owant.thinkmap.ui.editmap;

import com.owant.thinkmap.base.BasePresenter;
import com.owant.thinkmap.base.BaseView;
import com.owant.thinkmap.model.TreeModel;

import java.io.Serializable;

/**
 * Created by owant on 13/03/2017.
 */

public interface EditMapContract {

    interface Presenter extends BasePresenter {

        void addSomeFloorNote();

        void addSubNote();

        void focusMidMap();

        void editTreeNote();

        void reSetTreeData(Object treeObject);

        TreeModel<String> getTreeModel();
    }

    /**
     * 所有和View相关的Action
     */
    interface View extends BaseView<Presenter> {

        /**
         * 添加节点
         */
        void showAddNode();

        /**
         * 添加子节点
         */
        void showAddSubNode();

        /**
         * 对焦中心
         */
        void showFocusMid();

        /**
         * 进入命令模式
         */
        void showCodeMode();

        /**
         * 保存数据
         */
        void showSaveMap();

        /**
         * 修改节点的数据
         *
         * @param view
         */
        void showEditNote(View view);
    }
}
