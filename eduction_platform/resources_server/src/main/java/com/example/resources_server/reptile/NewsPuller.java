package com.example.resources_server.reptile;

import com.example.resources_server.entity.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Set;

public interface NewsPuller {

    Set<News> pullNews();
    // url:即新闻首页url
    // useHtmlUnit:是否使用htmlunit
    default Document getHtmlFromUrl(String url, boolean useHtmlUnit) throws Exception {
        if (!useHtmlUnit){
            return Jsoup.connect(url)
                    //模拟火狐浏览器
                    .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                    .get();
        }else {
            return null;
        }
    }
}
