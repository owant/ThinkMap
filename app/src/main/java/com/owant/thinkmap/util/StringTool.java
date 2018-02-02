package com.owant.thinkmap.util;

/**
 * Created by owant on 22/03/2017.
 * 脱开Android包
 */

public class StringTool {

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
