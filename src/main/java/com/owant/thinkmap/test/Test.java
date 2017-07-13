package com.owant.thinkmap.test;

/**
 * Created by OlaWang on 2017/7/13.
 */

public class Test {

    public static void main(String[] args) {
        Professor p = new Professor("wangwu", 50);
        Student s1 = new Student("zhangsan", 18, p);
        Student s2 = (Student) s1.clone();

        s2.p.name = "lisi";
        s2.p.age = 30;

        System.out.println("name=" + s1.p.name + "," + "age=" + s1.p.age);//学生1的教授不改变。
        System.out.println("name=" + s2.p.name + "," + "age=" + s2.p.age);//学生2的教授不改变。
    }
}

class Professor implements Cloneable {
    String name;
    int age;

    Professor(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }
}

class Student implements Cloneable {
    String name;
    int age;
    Professor p;

    Student(String name, int age, Professor p) {
        this.name = name;
        this.age = age;
        this.p = p;
    }

    public Object clone() {
        Student o = null;
        try {
            o = (Student) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        o.p = (Professor) p.clone();
        return o;
    }


}