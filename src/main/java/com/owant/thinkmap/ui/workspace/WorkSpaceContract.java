package com.owant.thinkmap.ui.workspace;

import com.owant.thinkmap.base.BasePresenter;
import com.owant.thinkmap.base.BaseView;
import com.owant.thinkmap.model.CurrentFileModel;

import java.util.ArrayList;

/**
 * Created by owant on 24/03/2017.
 */
public interface WorkSpaceContract {

    interface Presenter extends BasePresenter {
        /**
         * 空ListView的提示
         */
        void onEmptyView();

        /**
         * 加载Owant文件
         */
        void onLoadOwantData();

        /**
         * 删除Item
         *
         * @param position
         */
        void removeItemFile(int position);

        String getItemFilePath(int position);
    }

    interface View extends BaseView<WorkSpaceContract.Presenter> {

        /**
         * 显示空的View
         */
        void showEmptyView();

        /**
         * 设置ListView的数据
         *
         * @param listData
         */
        void setListData(ArrayList<CurrentFileModel> listData);

        /**
         * 刷新ListView的数据
         */
        void refreshListData();

        String getOwantDefaultPath();

    }

}
