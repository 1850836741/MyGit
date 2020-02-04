package com.example.resources_server.reptile;

import com.example.resources_server.entity.News;
import com.example.resources_server.tool.DateTool;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.HashSet;
import java.util.Set;

public class SohuNewsPuller implements NewsPuller{
    private String url="http://www.sohu.com/";
    @Override
    public Set<News> pullNews() {
        System.out.println("开始爬取搜狐新闻! ");
        Document document = null;
        try {
            document = getHtmlFromUrl(url,false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("爬取失败");
        }
        // 2.jsoup获取新闻<a>标签
        Elements elements = document.select("div.focus-news")
                .select("div.list16").select("li").select("a");
        // 3.从<a>标签中抽取基本信息，封装成news
        Set<News> newsSet = new HashSet<>();
        for (Element e: elements){
            String url = e.attr("href");
            String title = e.attr("title");
            News news = new News();
            news.setHeadline(title);
            news.setCreation_time(DateTool.getTime());
            news.setUrl(url);

            newsSet.add(news);
        }
        return newsSet;
    }
}
