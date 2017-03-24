package com.owant.thinkmap.ui.workspace;

import com.owant.thinkmap.model.CurrentFileModel;
import com.owant.thinkmap.util.AndroidUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by owant on 24/03/2017.
 */

public class WorkSpacePresenter implements WorkSpaceContract.Presenter {

    private WorkSpaceContract.View mView;
    private ArrayList<CurrentFileModel> mLists;

    private String mDefaultPath;

    public WorkSpacePresenter(WorkSpaceContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        mLists = new ArrayList<>();
    }

    @Override
    public void onRecycle() {

    }


    @Override
    public void onEmptyView() {
        mView.showEmptyView();
    }

    @Override
    public void onLoadOwantData() {
        if (mDefaultPath == null) {
            mDefaultPath = mView.getOwantDefaultPath();
        }

        File saveFileParent = new File(mDefaultPath);
        mLists.clear();
        if (saveFileParent.exists()) {
            File[] files = saveFileParent.listFiles();
            for (File v : files) {
                if (v.isFile() && v.getAbsolutePath().endsWith(".owant")) {
                    CurrentFileModel model = new CurrentFileModel();
                    model.filePath = v.getAbsolutePath();
                    model.editTime = AndroidUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss", v.lastModified());
                    String fileName = v.getName();
                    if (fileName.indexOf(".") > 0) {
                        fileName = fileName.substring(0, fileName.indexOf("."));
                    }
                    model.mapRoot = fileName;
                    mLists.add(model);
                }
            }
        }
        mView.setListData(mLists);
    }

    @Override
    public void removeItemFile(int position) {

    }

    @Override
    public String getItemFilePath(int position) {
        return mLists.get(position).filePath;
    }


}
