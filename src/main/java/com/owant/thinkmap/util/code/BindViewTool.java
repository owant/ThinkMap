package com.owant.thinkmap.util.code;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by owant on 19/10/2016.
 * 查找XML布局下的控件,进行生产findViewById的代码
 */

public class BindViewTool {

//    static String path = "/Users/owant/AndroidStudioProjects/ThinkMap/app/src/main/res/layout/edit_menu_tool_bar.xml";

    static String path = "/Users/owant/AndroidStudioProjects/ThinkMap/app/src/main/res/layout/dialog_edit_input.xml";


    public static void main(String[] arg) {
        bindView(path);
    }

    /**
     * private TextView info;
     */
    private static String declare_format = "private {0} {1};";

    /**
     * info=(TextView)findViewById(R.id.text);
     * info=(TextView)getView().findViewById(R.id.text);
     */
    private static String find_view_format = "{0} = ({1}){2}findViewById({3});";

    private static boolean isFragment = false;
    /**
     * 忽略的标识
     */
    private static String ignoreMark = "";

    //找到了需要绑定的View
    private static ArrayList<Model> bindViews;

    public static void bindView(String xmlPath) {
        try {

            InputStream inputStream = new FileInputStream(new File(xmlPath));

            //bindView的集合
            bindViews = new ArrayList<>();

            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(inputStream, "utf-8");

            //xmlpullparser是以事件触发为设计的代码
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {//文档结束
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT://文档开始
                        break;
                    case XmlPullParser.START_TAG://标签开始
                        xmlTagBusiness(xmlPullParser);
                        break;
                    case XmlPullParser.END_TAG://标签结束
                        break;
                }

                eventType = xmlPullParser.next();
            }

            printfResult();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printfResult() {
        //打印需要BindView的控件
        for (Model m : bindViews) {
            String declare = declare_format.replace("{0}", m.mType);
            declare = declare.replace("{1}", m.mId);
            System.out.println(declare);
        }

        System.out.println("\n\n");

        for (Model m : bindViews) {
            String find = find_view_format.replace("{0}", m.mId);
            find = find.replace("{1}", m.mType);
            if (isFragment) {
                find = find.replace("{2}", "getView().");
            } else {
                find = find.replace("{2}", "");
            }
            find = find.replace("{3}", "R.id." + m.mId);
            System.out.println(find);
        }
    }

    private static void xmlTagBusiness(XmlPullParser xmlPullParser) {
        //对于这个情况需要进行com.owant.example.view.DivView
        String type = xmlPullParser.getName();
        int pointExist = type.lastIndexOf(".");
        if (pointExist != -1) {
            type = type.substring(pointExist + 1, type.length());
        }

        String androidIdValue = null;
        int count = xmlPullParser.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String androidIdTag = xmlPullParser.getAttributeName(i);
            if (androidIdTag.equals("android:id")) {
                String androidIdTagValue = xmlPullParser.getAttributeValue(i);
                if (androidIdTagValue.startsWith("@+id/")) {
                    androidIdValue = androidIdTagValue.replace("@+id/", "");

                    //TODO ignore
                }
            }
        }

        if (androidIdValue != null) {
            Model model = new Model();
            model.mId = androidIdValue;
            model.mType = type;
            bindViews.add(model);
        }
    }

    public static class Model {
        public String mType;
        public String mId;
    }

}
