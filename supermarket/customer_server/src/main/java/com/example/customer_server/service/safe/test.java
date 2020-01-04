package com.example.customer_server.service.safe;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class test {
    public static void main(String[] args) {
        List<String> n = Arrays.asList(new String[]{"a","b","c"});
        System.out.println(IntStream.range(0,n.size()).filter(i->n.get(i).equals("a")).findFirst().getAsInt());
    }
}
