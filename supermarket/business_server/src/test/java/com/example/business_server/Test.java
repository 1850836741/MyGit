package com.example.business_server;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    @org.junit.Test
    public void dataTest() throws InterruptedException {
        Date date = new Date();
        Thread.sleep(2000);
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = new Date();
        date2.setTime(date1.getTime()-date.getTime());
        System.out.println(simpleDateFormat.format(date2));
    }
}
