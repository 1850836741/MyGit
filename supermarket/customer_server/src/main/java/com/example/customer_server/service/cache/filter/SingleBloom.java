package com.example.customer_server.service.cache.filter;

/**
 * 获取单例Bloom
 */
public class SingleBloom {
    private static class HoldClass{
        private static final Bloom bloom = new Bloom();
    }

    public Bloom getBloom(){
        return HoldClass.bloom;
    }
}
