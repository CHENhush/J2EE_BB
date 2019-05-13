package com.j2ee.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public static int getIntTime() throws ParseException {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
        int changeTime = (int) (time / 1000);
        return changeTime;
    }
    public static String getShowTime(long createTime){
        Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long t = createTime;
        t = t * 1000;
        date.setTime(t);
        String showTime=sdFormat.format(date);
        return showTime;
    }
}
