package com.example.user_server;

import com.example.user_server.entity.User;
import com.example.user_server.tool.MathTool;
import com.example.user_server.tool.UnsafeTool;
import org.junit.Test;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

public class simpleTest {

    @Test
    public void unsafeTest(){
        User user = new User();
        user.setUser_id(1);
        user.setName("QAQ");
        System.out.println("==="+user.getName()+"===");
        try {
            UnsafeTool.setObject(user,"name","QAQ","QWQ");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.println("==="+user.getName()+"===");
    }

    @Test
    public void atomicTest(){
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread t = null;
        for (int i = 0;i<1000;i++){
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    int x = atomicInteger.get();
                    while (!atomicInteger.compareAndSet(x,x+1)){
                        x = atomicInteger.get();
                    }
                    System.out.println(x+1);
                }
            });
            t.start();
        }
    }

    @Test
    public void timeTest(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        System.out.println(simpleDateFormat.format(date));
        System.out.println(Integer.parseInt("01"));
    }

    @Test
    public void typeTest(){
        User user = new User();
        Type uType = user.getClass().getClass().getGenericSuperclass();
        System.out.println(uType.getTypeName());

        Type mySuperClass = new ArrayList<String>().getClass().getGenericSuperclass();
        Type type = ((ParameterizedType)mySuperClass).getActualTypeArguments()[0];
        System.out.println(type.getTypeName());
    }

    @Test
    public void listTest(){
        List<User> users = new ArrayList<>();
        User user = new User(170912010,"123456","1");
        users.add(user);
        user.setCell_phone("2");
        System.out.println(users.get(0));
    }

    @Test
    public void bitTest(){
        User user = new User();
        user.setSex(new BitSet(1));
        System.out.println(user.getSex().get(0));
    }

    @Test
    public void pattenTest(){
        Pattern pattern = Pattern.compile("[0-9]*");
        System.out.println(pattern.matcher("123q").matches());
    }

    @Test
    public void stringTest(){
        String x = "123456";
        System.out.println(x.substring(x.indexOf("23")));
    }

    static int exchFlag = 1;
    static synchronized  void setExchFlag(int v){
        exchFlag = v;
    }
    static synchronized int getExchFlag(){
        return exchFlag;
    }

    public static class OddEventSortTask implements Runnable{
        int arr[];
        int i;
        CountDownLatch countDownLatch;
        public OddEventSortTask(int i, CountDownLatch countDownLatch, int[] arr){
            this.i = i;
            this.countDownLatch = countDownLatch;
            this.arr = arr;
        }

        @Override
        public void run() {
            if (arr[i] > arr[i+1]){
                MathTool.swap(i,i+1,arr);
                setExchFlag(1);
            }
            countDownLatch.countDown();
        }
    }

    @Test
    public void OddTest() throws InterruptedException {
        int arr[] = {3,2,8,4,6,7,9,13,10,1,5};
        int start = 0;
        while (getExchFlag() == 1 || start == 1){
            System.out.println("????????????????");
            setExchFlag(0);
            CountDownLatch countDownLatch = new CountDownLatch(arr.length/2-(arr.length%2==0?start:0));
            for (int i = start; i < arr.length-1; i+=2){
                new Thread(new OddEventSortTask(i,countDownLatch,arr)).start();
            }
            countDownLatch.await();;
            if (start == 0){
                start = 1;
            }else {
                start = 0;
            }
        }
        for (int i = 0; i < arr.length; i++){
            System.out.println(arr[i]);
        }
        IntConsumer intConsumer = MathTool::abs;
    }
}
