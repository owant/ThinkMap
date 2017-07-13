package com.owant.thinkmap.ui.workspace;

import com.owant.thinkmap.AppConstants;
import com.owant.thinkmap.file.ZipTool;
import com.owant.thinkmap.model.CurrentFileModel;
import com.owant.thinkmap.util.AndroidUtil;
import com.owant.thinkmap.util.LOG;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public void onLoadExamples(InputStream ism) {
        boolean state = mView.shouldLoadExample();
        if (state) {
            new Thread(new CopOwantExamples(ism)).start();
        }
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

    /**
     * 拷贝raw下的示例文件到内存卡
     */
    private class CopOwantExamples implements Runnable {

        private InputStream mInputStream;

        public CopOwantExamples(InputStream is) {
            mInputStream = is;
        }

        @Override
        public void run() {
            try {
                FileOutputStream fos = null;
                InputStream inputStream = null;
                try {
                    String sdCardPath = mView.getOwantDefaultPath();
                    String saveExamplesPath = sdCardPath + AppConstants.examples_names;
                    LOG.jLogi("copExamplesToStorage:%s", saveExamplesPath);
                    File saveFile = new File(saveExamplesPath);
                    if (!saveFile.exists()) {
                        saveFile.getParentFile().mkdirs();
                        saveFile.createNewFile();
                    }
                    fos = new FileOutputStream(saveFile);
                    inputStream = mInputStream;

                    byte[] buffer = new byte[1024];
                    int count;
                    while ((count = inputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, count);
                        fos.flush();
                    }

                    ZipTool.unZipFiles(
                            saveExamplesPath,
                            mView.getOwantDefaultPath());


                    mView.changeExampleVersion(mView.getExampleVersion());

                    onLoadOwantData();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
