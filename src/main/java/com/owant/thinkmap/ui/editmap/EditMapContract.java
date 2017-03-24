package com.owant.thinkmap.ui.editmap;

import com.owant.thinkmap.base.BasePresenter;
import com.owant.thinkmap.base.BaseView;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;

import java.util.List;

/**
 * Created by owant on 21/03/2017.
 */

public interface EditMapContract {

    interface Presenter extends BasePresenter {

        /**
         * 设置读取的文件路径
         */
        void setLoadMapPath(String path);

        /**
         * 刷新文件路径下的owant文件集合
         */
        void refreshOwantFilesLists();

        /**
         * 读取owant文件
         */
        void readOwantFile();

        /**
         * 创建默认的TreeModel
         */
        void createDefaultTreeModel();

        /**
         * 添加节点
         */
        void addNote();

        /**
         * 添加子节点
         */
        void addSubNote();

        /**
         * 编辑节点
         */
        void editNote();

        /**
         * 对焦中心
         */
        void focusMid();

        /**
         * 保存文件
         */
        void saveFile();

        /**
         * 进行保存
         */
        void doSaveFile();

        /**
         * 设置树形模型
         *
         * @param treeModel
         */
        void setTreeModel(TreeModel<String> treeModel);

        /**
         * 获取树形模型
         *
         * @return 树形模型
         */
        TreeModel<String> getTreeModel();

        /**
         * 获取文件目录下的owant文件集合
         *
         * @return owant文件集合
         */
        List getOwantLists();

        String getSaveInput();
    }

    interface View extends BaseView<Presenter> {

        /**
         * 显示加载文件
         */
        void showLoadingFile();

        /**
         * 设置树形结构数据
         *
         * @param treeModel
         */
        void setTreeViewData(TreeModel<String> treeModel);

        /**
         * 隐藏加载数据
         */
        void hideLoadingFile();

        /**
         * 显示添加节点
         */
        void showAddNoteDialog();

        /**
         * 显示添加子节点
         */
        void showAddSubNoteDialog();

        /**
         * 显示编辑节点
         */
        void showEditNoteDialog();

        /**
         * 显示保存数据
         *
         * @param fileName
         */
        void showSaveFileDialog(String fileName);

        /**
         * 对焦中心
         */
        void focusingMid();

        /**
         * 获得默认root节点的text
         *
         * @return
         */
        String getDefaultPlanStr();

        /**
         * 获得最近对焦
         *
         * @return
         */
        NodeModel<String> getCurrentFocusNode();

        /**
         * 获取Plan的默认字符
         *
         * @return My Plan
         */
        String getDefaultSaveFilePath();

        /**
         * 获得app的版本
         *
         * @return 版本号
         */
        String getAppVersion();
    }
}
