package com.owant.thinkmap.test;

/**
 * Created by owant on 31/03/2017.
 */

public class Test {

    public static void main(String[] args) {

    }

    public static int add(int num1, int num2) {
        return num1 + num2;
    }

    public static int function(int number) {
        if (number == 1) {
            return 1;
        }
        if (number == 2) {
            return 1;
        }
        return function(number - 1) + function(number - 2);
    }


}
