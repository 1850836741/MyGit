package com.example.resources_server.entity;

import lombok.Data;

/**
 * 新闻实体类
 */
@Data
public class News{
    private String headline;
    private String url;
    private String creation_time;

    public News() {
    }

    public News(String headline, String url, String creation_time) {
        this.headline = headline;
        this.url = url;
        this.creation_time = creation_time;
    }
}
