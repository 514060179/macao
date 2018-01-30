package com.yinghai.macao.common.model;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/6/28.
 */
public class Time {
    //時間
    private Long hour;
    //分鐘
    private Long minute;
    //秒數
    private Long seconds;

    public Time() {
    }

    public Time(Long hour, Long minute, Long seconds) {
        this.hour = hour;
        this.minute = minute;
        this.seconds = seconds;
    }

    public Long getHour() {
        return hour;
    }

    public void setHour(Long hour) {
        this.hour = hour;
    }

    public Long getMinute() {
        return minute;
    }

    public void setMinute(Long minute) {
        this.minute = minute;
    }

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return "Time{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", seconds=" + seconds +
                '}';
    }

    public static void main(String[] args) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long between = 0;
        try {
            java.util.Date begin = dfs.parse("2009-07-20 10:22:21.214");
            java.util.Date end = dfs.parse("2009-07-21 11:24:49.145");
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
        System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
                + "毫秒");
        System.out.println(day + "天" + (hour+day*24) + "小时" + min + "分" + s + "秒" + ms
                + "毫秒");
    }
}
