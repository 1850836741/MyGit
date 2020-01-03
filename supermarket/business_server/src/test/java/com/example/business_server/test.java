package com.example.business_server;

import com.alibaba.fastjson.JSON;
import com.example.business_server.entity.OrderDetailed;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class test {

    @Test
    public void t() throws ParseException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(date));
        System.out.println(simpleDateFormat.format(date).substring(0,10));
    }
}
