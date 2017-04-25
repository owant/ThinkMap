package com.owant.thinkmap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by owant on 2016/9/1.
 */
public class MakeZipClient {

    private List<File> arrayFiles;
    private String sourcePath;
    private File saveFilePath;

    public void create(String sourcePath, File savePath) {
        arrayFiles = new ArrayList<>();
        this.sourcePath = sourcePath;
        this.saveFilePath = savePath;

        //遍历文件下文件
        findAllFiles(sourcePath);
        //生产.owant
        makeZipFile(arrayFiles.toArray(new File[arrayFiles.size()]), saveFilePath);
    }

    /**
     * 找到所有输入文件夹路径下的所有文件
     *
     * @param filePath
     */
    private void findAllFiles(String filePath) {
        if (filePath == null || filePath.trim().length() == 0) {
            System.out.println("请输入文件路径！");
            return;
        }

        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            System.out.println("输入的文件路径不存在！");
            return;
        } else {//遍历该目录下所有的文件
            if (sourceFile.isDirectory()) {//文件夹
                File[] subFiles = sourceFile.listFiles();
                for (File f : subFiles) {
                    findAllFiles(f.getAbsolutePath());
                }
            } else {//文件
                System.out.println("找到文件：\t" + sourceFile.getAbsolutePath());
                arrayFiles.add(sourceFile);
            }
        }
    }

    private void makeZipFile(File[] files, File zipNameFile) {
        //建立一个写入流
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(zipNameFile);
            //建立一个写入压缩流
            ZipOutputStream zos = new ZipOutputStream(fos);
            byte[] buffer = new byte[1024];
            for (File file : files) {
                //截取掉前面的字符
                String cutPath = file.getAbsolutePath().substring(sourcePath.length());
                System.out.println("entry:" + cutPath);
                ZipEntry entry = new ZipEntry(cutPath);
                FileInputStream fis = new FileInputStream(file);
                zos.putNextEntry(entry);
                int read = 0;
                while ((read = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, read);
                }

                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] arg) {
//        String sourcePath = "/Users/owant/Desktop/create_file";
//        MakeZipClient client = new MakeZipClient();
//        File saveFile = new File("/Users/owant/Desktop/myplan.owant");
//        client.create(sourcePath, saveFile);
    }


}
