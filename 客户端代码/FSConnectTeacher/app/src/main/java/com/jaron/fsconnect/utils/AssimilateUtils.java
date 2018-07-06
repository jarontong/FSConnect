package com.jaron.fsconnect.utils;

import java.util.regex.Pattern;

/**
 * String 处理工具类
 */
public class AssimilateUtils {

    public static boolean machPhoneNum(CharSequence phoneNumber) {

        String regex = "^[1][34578][0-9]\\d{8}$";
        // Pattern pattern = Pattern.compile(regex);
        // pattern.matcher(phoneNumber).matches();

        //第二种就是对一种的一种封装
        return Pattern.matches(regex, phoneNumber);
    }

    public static boolean machEmail(CharSequence email) {
        String regex = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        return Pattern.matches(regex, email);
    }



}
