package com.boot.utils;


import java.util.Calendar;

public class TimeUtil {

    /**
     * 时间工具类
     */

    /**
     * 获取当前时间到晚上12点整还剩的秒数
     *
     * @return 秒
     */
    public static Long getSecondByCurTimeTo12Point() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }



}
