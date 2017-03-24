package com.owant.thinkmap.file;

import android.os.Environment;
import android.util.Log;

import com.owant.thinkmap.AppConstants;
import com.owant.thinkmap.model.TreeModel;
import com.owant.thinkmap.util.MakeZipClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by owant on 25/02/2017.
 * (要进行清理)
 * 1.在owantmaps里创建一个temp_create_file文件夹;
 * 2.content文件进行写序列画的tree对象;
 * 3.创建一个conf.txt文件进行保存文件基本信息,修改日期,文件名,系统的版本;
 * 4.为file_name_temp和conf.txt进行压缩为owant文件;
 * 5.删除create_file文件夹下的所有文件;
 */

public class OwantFileCreate {

    private final static String TAG = "OwantFileCreate";


    public void createOwantMapsDirectory() {
        if (hansSDCard()) {
            String map_path = Environment.getExternalStorageDirectory().getPath() + AppConstants.owant_maps;
            File owantMapDirectory = new File(map_path);
            if (!owantMapDirectory.exists()) {
                boolean mkdir = owantMapDirectory.mkdirs();
                Log.i(TAG, "创建owantmaps文件路径:" + mkdir + owantMapDirectory.getAbsolutePath());
            }
        } else {
            Log.e(TAG, "createOwantMapsDirectory: 没有内存卡!");
        }
    }

    public void createTempDirectory() {
        if (hansSDCard()) {
            String path = Environment.getExternalStorageDirectory().getPath() + AppConstants.owant_maps + AppConstants.temp_create_file;
            File owantMapDirectory = new File(path);
            if (!owantMapDirectory.exists()) {
                owantMapDirectory.mkdirs();
                Log.i(TAG, "创建Temp文件夹:" + path);
            }
        } else {
            Log.e(TAG, "createTempDirectory: 没有内存卡!");
            return;
        }


    }

    public void writeContent(Object object) {
        try {

            String content_path = Environment.getExternalStorageDirectory().getPath() +
                    AppConstants.owant_maps +
                    AppConstants.temp_create_file + "content";
            writeTreeObject(content_path, object);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeConf(Conf conf) {
        try {
            String conf_path = Environment.getExternalStorageDirectory().getPath() +
                    AppConstants.owant_maps +
                    AppConstants.temp_create_file + "conf.txt";
            writeFile(conf_path, conf.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeOwantFile(String saveName) {
        MakeZipClient client = new MakeZipClient();
        String temp_path = Environment.getExternalStorageDirectory().getPath() +
                AppConstants.owant_maps + AppConstants.temp_create_file;
        String savePath = Environment.getExternalStorageDirectory().getPath() +
                AppConstants.owant_maps + saveName;
        if (!savePath.endsWith(".owant")) {
            savePath = savePath + ".owant";
        }
        File saveFile = new File(savePath);
        client.create(temp_path, saveFile);

        Log.i(TAG, "创建owant文件成功" + savePath);
    }

    public void deleteTemp() {
        String temp_path = Environment.getExternalStorageDirectory().getPath()
                + AppConstants.owant_maps +
                AppConstants.temp_create_file;
        File file = new File(temp_path);
        delete(file);
    }

    public String readConf(String zipFilePath) {
        String info = readZipFile(zipFilePath, AppConstants.conf);
        return info;
    }

    public Object readContentObject(String zipFilePath) throws ClassNotFoundException, InvalidClassException {
        Object o = null;
        o = readZipFileObject(zipFilePath, AppConstants.content);
        return o;
    }

    private void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    private void writeTreeObject(String filePath, Object object) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
    }

    public TreeModel<String> readTreeObject(String filePath) throws IOException, ClassNotFoundException, InvalidClassException {
        TreeModel<String> tree;
        FileInputStream fos = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fos);
        tree = (TreeModel<String>) ois.readObject();
        return tree;
    }

    private void writeFile(String path, String fileContext) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(fileContext.getBytes("iso8859-1"));
        fos.close();
    }

    private String readFile(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = fis.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        fis.close();
        baos.close();
        return baos.toString();
    }

    public boolean hansSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private Object readZipFileObject(String filePath, String fileName) throws ClassNotFoundException, InvalidClassException {
        InputStream inputStream = null;
        ZipFile zipFile = null;
        ObjectInputStream objectInputStream = null;
        Object targetObject = null;
        try {
            File file = new File(filePath);
            zipFile = new ZipFile(file);
            ZipEntry zipEntry = new ZipEntry(fileName);
            inputStream = zipFile.getInputStream(zipEntry);
            objectInputStream = new ObjectInputStream(inputStream);
            targetObject = objectInputStream.readObject();
            zipFile.close();
            inputStream.close();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
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
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return targetObject;
    }

    private String readZipFile(String filePath, String fileName) {
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        ZipFile zipFile = null;
        try {
            File file = new File(filePath);
            zipFile = new ZipFile(file);
            ZipEntry zipEntry = new ZipEntry(fileName);
            inputStream = zipFile.getInputStream(zipEntry);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                buffer.append(new String(bytes, 0, len));
            }
            inputStream.close();
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
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
        return buffer.toString();
    }

}
