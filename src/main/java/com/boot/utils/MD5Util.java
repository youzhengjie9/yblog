package com.boot.utils;

import java.security.MessageDigest;

public class MD5Util {


    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

//    /**
//     * 测试主函数
//     * @param args
//     * @throws Exception
//     */
//    public static void main(String args[]) throws Exception {
//        String str = new String("amigoxiexiexingxing");
//        System.out.println("原始：" + str);
//        System.out.println("MD5后：" + md5Encode(str));
//    }
}
