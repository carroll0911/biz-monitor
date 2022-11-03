package com.carroll.biz.monitor.data.common.utils;

/**
 * Created on 202017/11/22 15:12 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public class PasswordUtils {

    private static final String SEPERATOR="_";

    private PasswordUtils(){}

    public static String encodePassword(String projectTag,String password) {
        return MD5Util.md5Encode(projectTag+SEPERATOR+password);
    }

    public static boolean checkPassword(String projectTag,String password,String encoded){
        return MD5Util.md5Encode(projectTag+SEPERATOR+password).equals(encoded);
    }
}
