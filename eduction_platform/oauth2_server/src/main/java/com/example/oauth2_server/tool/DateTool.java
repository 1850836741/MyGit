package com.example.oauth2_server.tool;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 操纵日期的工具类
 */
@Component
public class DateTool {

    /**
     * 静态内部类保证dayList的单例
     */
    private static class HoldDayList{
        private static List<Integer> dayList = Arrays.asList(31,28,31,30,31,30,31,31,30,31,30,31);         //存放平年每月天数
    }

    /**
     * 获取dayList
     * @return
     */
    public static List<Integer> getDayList(){
        return HoldDayList.dayList;
    }

    /**
     * 判断是否为闰年
     * @param year
     * @return
     */
    public static boolean isLeapYear(String year){
        if (year.substring(year.length()-1,year.length()).equals("00")){
            return Integer.parseInt(year) %400 == 0;
        }
        return Integer.parseInt(year)%4==0 && Integer.parseInt(year)%100!=0;
    }

    /**
     * 获取当前年份
     * @return
     */
    public static String getNowYear(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return  simpleDateFormat.format(date);
    }

    /**
     * 获取当前月份
     * @return
     */
    public static String getNowMouth(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        return  simpleDateFormat.format(date);
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getNowDay(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        return  simpleDateFormat.format(date);
    }

    /**
     * 获取当前小时
     * @return
     */
    public static String getNowHour(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        return  simpleDateFormat.format(date);
    }

    /**
     * 获取本月总天数
     * @return
     */
    public static int getNowMouthAllDay(){
        String nowYear = getNowYear();
        String nowMouth = getNowMouth();
        if (isLeapYear(nowYear) && nowMouth.equals("02")){
            return 29;
        }
        return getDayList().get(Integer.parseInt(nowMouth));
    }

    /**
     * 获取今日到本年1月1日的总天数
     * @return
     */
    public static int getDaysApartNowYearFirstDay(){
        String nowYear = getNowYear();
        String nowMouth = getNowMouth();
        if (nowMouth.equals("01")){
            return Integer.valueOf(getNowDay());
        }else {
            int totalDays = 0;
            for (int i=0; i<Integer.parseInt(nowMouth)-1; i++){
                totalDays += getDayList().get(i);
                if (i==1 && isLeapYear(nowYear)){
                    totalDays += 1;
                }
            }
            totalDays += Integer.valueOf(getNowDay());
            return totalDays;
        }
    }
}
