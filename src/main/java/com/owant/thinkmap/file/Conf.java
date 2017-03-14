package com.owant.thinkmap.file;

/**
 * Created by owant on 25/02/2017.
 * conf.txt
 */

public class Conf {
    public String app_version;
    public String android_version;
    public String map_name;
    public String date;

    @Override
    public String toString() {
        return "Conf{" +
                "app_version='" + app_version + '\'' +
                ", android_version='" + android_version + '\'' +
                ", map_name='" + map_name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Conf conf = new Conf();
        conf.android_version = "android5.0";
        conf.app_version = "1.0.1";
        conf.date = "2017-2-27";
        System.out.println(conf.toString());
    }
}
