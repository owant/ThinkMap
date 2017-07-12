package com.owant.thinkmap.test;

import com.owant.thinkmap.file.Conf;
import com.owant.thinkmap.file.OwantFileCreate;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;
import com.owant.thinkmap.util.AndroidUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by owant on 31/03/2017.
 */

public class ExampleCreator {

    public static void main(String[] args) {
//        createTreeNodes();

        createExampleMapVersion();
    }


    public static String format = "NodeModel<String> node{0} = new NodeModel<>(\"{0}\");";

    public static void createTreeNodes() {
        for (char i = 'A'; i < 'Z'; i++) {
            String value = format;
            value = value.replace("{0}", i + "");
            System.out.println(value);
        }
    }

    public static void createExampleMapVersion() {

        NodeModel<String> root = new NodeModel<>("ThinkMap版本情况");
        TreeModel<String> treeModel = new TreeModel<>(root);

        NodeModel<String> version1 = new NodeModel<>("V1.0.1");
        NodeModel<String> version2 = new NodeModel<>("V1.0.2");
        NodeModel<String> version3 = new NodeModel<>("V1.0.3");
        NodeModel<String> version4 = new NodeModel<>("V1.0.4");

        treeModel.addNode(root, version1, version2, version3, version4);

        //版本1的情况
        NodeModel<String> v1_draw_tree = new NodeModel<>("绘制树形结构");
        NodeModel<String> v1_add_child_note = new NodeModel<>("添加子节点");
        NodeModel<String> v1_add_node = new NodeModel<>("添加同层节点");
        NodeModel<String> v1_front = new NodeModel<>("对焦中心");
        NodeModel<String> v1_map_move = new NodeModel<>("视图移动");
        NodeModel<String> v1_open_owant_file = new NodeModel<>("打开.owant文件");

        treeModel.addNode(version1, v1_draw_tree, v1_add_child_note,
                v1_add_node, v1_front, v1_map_move, v1_open_owant_file);

        //版本2情况
        NodeModel<String> v2_fixed_location = new NodeModel<>("Fixed位置错误");
        NodeModel<String> v2_add_handle = new NodeModel<>("添加了缩放手势");
        NodeModel<String> v2_tree_manager = new NodeModel<>("抽取TreeLayoutManger\n提供更多的Style可能");
        NodeModel<String> v2_fixed_click = new NodeModel<>("Fixed长按和点击问题");
        NodeModel<String> v2_add_example = new NodeModel<>("添加演示例子");

        treeModel.addNode(version2, v2_fixed_location, v2_add_handle, v2_tree_manager,
                v2_tree_manager, v2_fixed_click, v2_add_example);

        //版本3情况
        NodeModel<String> v3_android4 = new NodeModel<>("Fixed Android4.0出现BUG");
        NodeModel<String> v3_splash = new NodeModel<>("添加欢迎界面");
        NodeModel<String> v3_permission = new NodeModel<>("修复动态权限申请问题");
        NodeModel<String> v3_review = new NodeModel<>("ReView优化");

        treeModel.addNode(version3, v3_android4, v3_splash, v3_permission, v3_review);


        //版本4情況
        NodeModel<String> v4_fixed_scale_light = new NodeModel<>("Fixed 缩放出现抖动");
        NodeModel<String> v4_review = new NodeModel<>("ReView优化，Github上\n建立release分支");
        treeModel.addNode(version4, v4_fixed_scale_light, v4_review);


        //穿件owant文件
        OwantFileCreate owantFileCreate = new OwantFileCreate();

        //创建文件夹
        owantFileCreate.createOwantMapsDirectory();
        //删除临时文件
        owantFileCreate.createTempDirectory();
        //写入内容
        owantFileCreate.writeContent(treeModel);

        Conf conf = new Conf();
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        conf.date = simpleDateFormat.format(time);
        conf.app_version = "v1.0.4";
        conf.android_version = AndroidUtil.getAndroidSystemVersion();
        conf.map_name = treeModel.getRootNode().getValue();

        //写入配置
        owantFileCreate.writeConf(conf);
        //创建压缩包
        owantFileCreate.makeOwantFile("演示-App版本情况");
        //删除临时文件
        owantFileCreate.deleteTemp();
    }

    public static void createExampleHowToUse() {

        NodeModel<String> root = new NodeModel<>("ThinkMap使用教程");
        TreeModel<String> treeModel = new TreeModel<>(root);

        NodeModel<String> subNode = new NodeModel<>("SubNode");
        NodeModel<String> node = new NodeModel<>("Node");
        NodeModel<String> frontView = new NodeModel<>("FrontView");
        NodeModel<String> codeMode = new NodeModel<>("CodeMode");

        treeModel.addNode(root, subNode, node, frontView, codeMode);

        NodeModel<String> subNode1 = new NodeModel<>("添加子节点");
        NodeModel<String> node1 = new NodeModel<>("添加同层节点");
        NodeModel<String> frontView1 = new NodeModel<>("对焦中心");
        NodeModel<String> codeMode1 = new NodeModel<>("编码模式");

        treeModel.addNode(subNode, subNode1);
        treeModel.addNode(node, node1);
        treeModel.addNode(frontView, frontView1);
        treeModel.addNode(codeMode, codeMode1);

        //穿件owant文件
        OwantFileCreate owantFileCreate = new OwantFileCreate();
        owantFileCreate.createOwantMapsDirectory();
        owantFileCreate.createTempDirectory();
        owantFileCreate.writeContent(treeModel);

        Conf conf = new Conf();
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        conf.date = simpleDateFormat.format(time);
        conf.app_version = "v1.0.3";
        conf.android_version = AndroidUtil.getAndroidSystemVersion();
        conf.map_name = treeModel.getRootNode().getValue();
        owantFileCreate.writeConf(conf);
        owantFileCreate.makeOwantFile("演示-使用教程");
        owantFileCreate.deleteTemp();
    }


}
